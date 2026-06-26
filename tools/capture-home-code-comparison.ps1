param(
    [string]$OutputDir = "artifacts\screenshots\home-code-comparison",
    [string]$BaseUrl = "",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

function Wait-LocalSite {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Url,
        [int]$TimeoutSeconds = 45
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    $lastError = $null

    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 2
            if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 500) {
                Write-Host "Local site is ready at $Url" -ForegroundColor Green
                return
            }
        } catch {
            $lastError = $_.Exception.Message
        }

        Start-Sleep -Milliseconds 500
    }

    throw "Timed out waiting for local site at $Url. Last error: $lastError"
}

if (-not $SkipBuild) {
    Write-Host "Preparing Pages site..." -ForegroundColor Cyan
    .\tools\prepare-pages-site.ps1
}

if (-not (Test-Path "site-dist\index.html")) {
    throw "site-dist index.html route is missing. Run tools\prepare-pages-site.ps1 first or omit -SkipBuild."
}

Write-Host "Installing Playwright dependencies..." -ForegroundColor Cyan
Push-Location tools\docs-site-capture
npm install
npx playwright install chromium
Pop-Location

$serverProcess = $null
$captureBaseUrl = $BaseUrl

if ([string]::IsNullOrWhiteSpace($captureBaseUrl)) {
    $captureBaseUrl = "http://localhost:8080"
    Write-Host "Starting local site-dist server at $captureBaseUrl..." -ForegroundColor Cyan
    $serverProcess = Start-Process -FilePath python -ArgumentList @("-m", "http.server", "8080", "-d", "site-dist") -WindowStyle Hidden -PassThru
    Wait-LocalSite -Url "$captureBaseUrl/"
}

try {
    if (Test-Path $OutputDir) {
        Remove-Item -LiteralPath $OutputDir -Recurse -Force
    }
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

    Write-Host "Running home code comparison Playwright captures..." -ForegroundColor Cyan
    Push-Location tools\docs-site-capture
    node .\capture-home-code-comparison.js $captureBaseUrl (Join-Path $root $OutputDir)
    $captureExitCode = $LASTEXITCODE
    Pop-Location
    if ($captureExitCode -ne 0) {
        throw "Home code comparison Playwright capture failed with exit code $captureExitCode."
    }
}
finally {
    if ((Get-Location).Path -like "*tools\docs-site-capture") {
        Pop-Location
    }
    if ($serverProcess -and -not $serverProcess.HasExited) {
        Write-Host "Stopping local site-dist server..." -ForegroundColor Cyan
        Stop-Process -Id $serverProcess.Id -Force
    }
}

Write-Host "Home code comparison captures saved in $OutputDir" -ForegroundColor Green
