<#
.SYNOPSIS
    Espera un GitHub Actions run y devuelve un resultado compacto al agente.

.DESCRIPTION
    Diseñado para agentes como Antigravity/Gemini:
    1. Resuelve repositorio, PR y head SHA.
    2. Espera que aparezca el workflow de ese commit.
    3. Usa gh run watch sin polling del modelo.
    4. Guarda JSON y logs fallidos en disco.
    5. Imprime ANTIGRAVITY_CI_COMPLETE para que el agente continúe.

.EXAMPLE
    .\tools\watch-ci.ps1 -PrNumber 16 -Workflow CI
#>

[CmdletBinding()]
param(
    [string]$Repo,
    [int]$PrNumber,
    [string]$Workflow = "CI",
    [string]$HeadSha,
    [ValidateRange(3, 300)]
    [int]$PollSeconds = 10,
    [ValidateRange(1, 240)]
    [int]$RunDiscoveryTimeoutMinutes = 5,
    [string]$OutputDirectory = ".agent/ci-watch",
    [switch]$NotifyWindows,
    # No hagas otro push mientras watch-ci.ps1 está esperando un run.
    # Espera a que termine, corrige o continúa, realiza el siguiente push y ejecuta nuevamente el watcher.
    [switch]$FollowPrHead
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Invoke-GhJson {
    param([Parameter(Mandatory)][string[]]$Arguments)

    $raw = & gh @Arguments 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "gh $($Arguments -join ' ') failed:`n$($raw -join [Environment]::NewLine)"
    }

    $text = ($raw -join [Environment]::NewLine).Trim()
    if ([string]::IsNullOrWhiteSpace($text)) { return $null }
    return $text | ConvertFrom-Json
}

function Resolve-Repository {
    if (-not [string]::IsNullOrWhiteSpace($Repo)) { return $Repo }

    $repoInfo = Invoke-GhJson -Arguments @("repo", "view", "--json", "nameWithOwner")
    if ($null -eq $repoInfo -or [string]::IsNullOrWhiteSpace($repoInfo.nameWithOwner)) {
        throw "No se pudo determinar el repositorio. Usa -Repo owner/name."
    }
    return [string]$repoInfo.nameWithOwner
}

function Resolve-PrState {
    param([Parameter(Mandatory)][string]$ResolvedRepo)

    $args = @("pr", "view")
    if ($PrNumber -gt 0) { $args += $PrNumber.ToString() }
    $args += @(
        "--repo", $ResolvedRepo,
        "--json", "number,headRefName,headRefOid,url,state,isDraft"
    )
    return Invoke-GhJson -Arguments $args
}

function Get-WorkflowRuns {
    param(
        [Parameter(Mandatory)][string]$ResolvedRepo,
        [Parameter(Mandatory)][string]$CommitSha
    )

    $args = @(
        "run", "list",
        "--repo", $ResolvedRepo,
        "--commit", $CommitSha,
        "--limit", "20",
        "--json", "databaseId,status,conclusion,url,headSha,workflowName,createdAt,number,name,event"
    )
    if (-not [string]::IsNullOrWhiteSpace($Workflow)) {
        $args += @("--workflow", $Workflow)
    }

    $runs = Invoke-GhJson -Arguments $args
    if ($null -eq $runs) { return @() }
    return @($runs | Sort-Object createdAt -Descending)
}

function Save-FailedLogs {
    param(
        [Parameter(Mandatory)][string]$ResolvedRepo,
        [Parameter(Mandatory)][long]$RunId,
        [Parameter(Mandatory)][string]$Directory
    )

    $logPath = Join-Path $Directory "run-$RunId-failed.log"
    $logs = & gh run view $RunId --repo $ResolvedRepo --log-failed 2>&1
    $exitCode = $LASTEXITCODE
    $logs | Set-Content -LiteralPath $logPath -Encoding UTF8
    if ($exitCode -ne 0) {
        Add-Content -LiteralPath $logPath -Encoding UTF8 -Value "`n[watch-ci] log command exit code: $exitCode"
    }
    return $logPath
}

function Show-DesktopNotification {
    param(
        [Parameter(Mandatory)][string]$Title,
        [Parameter(Mandatory)][string]$Message
    )

    if (-not $NotifyWindows) { return }

    try {
        if (Get-Command New-BurntToastNotification -ErrorAction SilentlyContinue) {
            New-BurntToastNotification -Text $Title, $Message | Out-Null
            return
        }

        [System.Media.SystemSounds]::Asterisk.Play()
        if (Get-Command msg.exe -ErrorAction SilentlyContinue) {
            & msg.exe $env:USERNAME "$Title`n$Message" 2>$null
        }
    }
    catch {
        Write-Warning "No se pudo mostrar la notificación: $($_.Exception.Message)"
    }
}

function Write-AgentSignal {
    param([Parameter(Mandatory)][System.Collections.IDictionary]$Result)

    $compactJson = $Result | ConvertTo-Json -Compress -Depth 8
    Write-Host ""
    Write-Host "ANTIGRAVITY_CI_COMPLETE $compactJson"
    Write-Host ""
}

if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    throw "GitHub CLI (gh) no está instalado o no está en PATH."
}

& gh auth status *> $null
if ($LASTEXITCODE -ne 0) {
    throw "GitHub CLI no está autenticado. Ejecuta: gh auth login"
}

$resolvedRepo = Resolve-Repository
$prState = $null

if ($PrNumber -gt 0 -or [string]::IsNullOrWhiteSpace($HeadSha)) {
    $prState = Resolve-PrState -ResolvedRepo $resolvedRepo
}

if ([string]::IsNullOrWhiteSpace($HeadSha)) {
    if ($null -eq $prState -or [string]::IsNullOrWhiteSpace($prState.headRefOid)) {
        throw "No se pudo resolver el head SHA. Usa -PrNumber o -HeadSha."
    }
    $HeadSha = [string]$prState.headRefOid
}

$resolvedPrNumber = if ($null -ne $prState) { [int]$prState.number } else { 0 }
$resolvedPrUrl = if ($null -ne $prState) { [string]$prState.url } else { $null }

New-Item -ItemType Directory -Force -Path $OutputDirectory | Out-Null

Write-Host "Repositorio : $resolvedRepo"
if ($resolvedPrNumber -gt 0) { Write-Host "PR          : #$resolvedPrNumber" }
Write-Host "Workflow    : $Workflow"
Write-Host "Head SHA    : $HeadSha"
Write-Host "Intervalo   : $PollSeconds segundos"
Write-Host ""
Write-Host "Esperando que aparezca el workflow..."

$deadline = (Get-Date).AddMinutes($RunDiscoveryTimeoutMinutes)
$selectedRun = $null

while ((Get-Date) -lt $deadline) {
    if ($FollowPrHead -and $resolvedPrNumber -gt 0) {
        $latestPr = Resolve-PrState -ResolvedRepo $resolvedRepo
        $latestHead = [string]$latestPr.headRefOid
        if ($latestHead -ne $HeadSha) {
            Write-Host "El PR cambió de head: $HeadSha -> $latestHead"
            $HeadSha = $latestHead
        }
    }

    $runs = Get-WorkflowRuns -ResolvedRepo $resolvedRepo -CommitSha $HeadSha
    if ($null -ne $runs -and @($runs).Count -gt 0) {
        $selectedRun = @($runs)[0]
        break
    }
    Start-Sleep -Seconds $PollSeconds
}

if ($null -eq $selectedRun) {
    $timeoutResult = @{
        repo       = $resolvedRepo
        prNumber   = $resolvedPrNumber
        headSha    = $HeadSha
        workflow   = $Workflow
        status     = "timeout"
        conclusion = $null
        runId      = $null
        runUrl     = $null
        resultFile = $null
        failedLog  = $null
    }
    Write-AgentSignal -Result $timeoutResult
    throw "No apareció un run dentro de $RunDiscoveryTimeoutMinutes minutos."
}

$runId = [long]$selectedRun.databaseId
$runUrl = [string]$selectedRun.url
$runNumber = [int]$selectedRun.number

Write-Host "Run encontrado: #$runNumber, ID $runId"
Write-Host $runUrl
Write-Host ""
Write-Host "Esperando su finalización sin consultas del agente..."
Write-Host ""

& gh run watch $runId --repo $resolvedRepo --compact --exit-status --interval $PollSeconds
$watchExitCode = $LASTEXITCODE

$runDetails = Invoke-GhJson -Arguments @(
    "run", "view", $runId.ToString(),
    "--repo", $resolvedRepo,
    "--json", "databaseId,number,name,status,conclusion,url,headSha,createdAt,startedAt,updatedAt,jobs"
)

$failedJobs = @(
    $runDetails.jobs |
    Where-Object { $_.conclusion -notin @("success", "skipped", "neutral", $null) } |
    ForEach-Object {
        [ordered]@{
            name       = $_.name
            conclusion = $_.conclusion
        }
    }
)

$failedLog = $null
if ($runDetails.conclusion -ne "success") {
    $failedLog = Save-FailedLogs -ResolvedRepo $resolvedRepo -RunId $runId -Directory $OutputDirectory
}

$result = [ordered]@{
    repo        = $resolvedRepo
    prNumber    = $resolvedPrNumber
    prUrl       = $resolvedPrUrl
    workflow    = $Workflow
    headSha     = [string]$runDetails.headSha
    runId       = [long]$runDetails.databaseId
    runNumber   = [int]$runDetails.number
    runUrl      = [string]$runDetails.url
    status      = [string]$runDetails.status
    conclusion  = [string]$runDetails.conclusion
    failedJobs  = $failedJobs
    failedLog   = $failedLog
    resultFile  = $null
    completedAt = (Get-Date).ToUniversalTime().ToString("o")
}

$resultPath = Join-Path $OutputDirectory "latest.json"
$result["resultFile"] = [System.IO.Path]::GetFullPath($resultPath)

$result |
    ConvertTo-Json -Depth 8 |
    Set-Content -LiteralPath $resultPath -Encoding UTF8

Show-DesktopNotification -Title "GitHub CI #$runNumber termino" -Message "$($runDetails.conclusion) - $resolvedRepo"
Write-AgentSignal -Result $result

if ($runDetails.conclusion -eq "success") {
    Write-Host "CI verde. Continua con la siguiente fase."
    exit 0
}

Write-Host "CI no termino en verde. Revisa failedLog."
if ($watchExitCode -ne 0) { exit $watchExitCode }
exit 1
