param(
    [string]$OutputDir = "artifacts\screenshots\ai-workspace-premium-refactor",
    [string]$BaseUrl = "",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

if (-not $SkipBuild) {
    Write-Host "Preparing Pages site..." -ForegroundColor Cyan
    .\tools\prepare-pages-site.ps1
}

if (-not (Test-Path "site-dist\examples\ai-workspace\index.html")) {
    throw "site-dist AI Workspace route is missing. Run tools\prepare-pages-site.ps1 first or omit -SkipBuild."
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
    Start-Sleep -Seconds 3
}

try {
    if (Test-Path $OutputDir) {
        Remove-Item -LiteralPath $OutputDir -Recurse -Force
    }
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

    Write-Host "Running AI Workspace Playwright captures..." -ForegroundColor Cyan
    Push-Location tools\docs-site-capture
    node .\capture-ai-workspace-premium.js $captureBaseUrl (Join-Path $root $OutputDir)
    Pop-Location
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

Write-Host "AI Workspace captures saved in $OutputDir" -ForegroundColor Green

