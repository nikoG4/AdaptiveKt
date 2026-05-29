param(
    [string]$OutputDir = "build\docs-site-web-captures",
    [string]$ZipPath = "build\adaptivekt-docs-site-web-captures.zip",
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

if (-not (Test-Path "site-dist\index.html")) {
    throw "site-dist is missing. Run tools\prepare-pages-site.ps1 first or omit -SkipBuild."
}

Write-Host "Installing Playwright dependencies..." -ForegroundColor Cyan
Push-Location tools\docs-site-capture
npm install
npx playwright install chromium
Pop-Location

$serverProcess = $null
$captureBaseUrl = $BaseUrl

if ([string]::IsNullOrWhiteSpace($captureBaseUrl)) {
    $captureBaseUrl = "http://localhost:8090"
    Write-Host "Starting local site-dist server at $captureBaseUrl..." -ForegroundColor Cyan
    $serverProcess = Start-Process -FilePath python -ArgumentList @("-m", "http.server", "8090", "-d", "site-dist") -WindowStyle Hidden -PassThru
    Start-Sleep -Seconds 3
}

try {
    if (Test-Path $OutputDir) {
        Remove-Item -LiteralPath $OutputDir -Recurse -Force
    }
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

    Write-Host "Running docs-site Playwright captures..." -ForegroundColor Cyan
    node tools\docs-site-capture\capture-docs-site-web.js $captureBaseUrl $OutputDir
}
finally {
    if ($serverProcess -and -not $serverProcess.HasExited) {
        Write-Host "Stopping local site-dist server..." -ForegroundColor Cyan
        Stop-Process -Id $serverProcess.Id -Force
    }
}

Write-Host "Generating ZIP..." -ForegroundColor Cyan
if (Test-Path $ZipPath) {
    Remove-Item -LiteralPath $ZipPath -Force
}
Compress-Archive -Path "$OutputDir\*" -DestinationPath $ZipPath

Write-Host "Docs-site captures saved in $OutputDir" -ForegroundColor Green
Write-Host "ZIP generated at $ZipPath" -ForegroundColor Green
