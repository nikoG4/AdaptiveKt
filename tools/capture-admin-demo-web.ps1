param (
    [string]$OutputDir = "build\web-visual-captures",
    [string]$ZipPath = "build\adaptivekt-admin-demo-web-visual-captures.zip",
    [string]$BaseUrl = "http://localhost:8080",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

if (-not $SkipBuild) {
    Write-Host "Building Wasm distribution..." -ForegroundColor Cyan
    .\gradlew.bat :admin-demo:wasmJsBrowserDistribution --console=plain --stacktrace
}

$DistDir = "admin-demo\build\dist\wasmJs\productionExecutable"
if (-not (Test-Path $DistDir)) {
    Write-Error "Distribution directory not found: $DistDir"
}

Write-Host "Installing Playwright dependencies..." -ForegroundColor Cyan
Push-Location tools\web-capture
npm install
npx playwright install chromium
Pop-Location

Write-Host "Starting local server..." -ForegroundColor Cyan
# Start http-server in the background
$ServerProcess = Start-Process npx.cmd -ArgumentList "http-server", $DistDir, "-p", "8080", "--silent" -PassThru -NoNewWindow

# Wait for server to start
Start-Sleep -Seconds 5

try {
    Write-Host "Running Playwright captures..." -ForegroundColor Cyan
    node tools\web-capture\capture-admin-demo-web.js $BaseUrl $OutputDir
}
finally {
    Write-Host "Stopping local server..." -ForegroundColor Cyan
    Stop-Process -Id $ServerProcess.Id -Force
}

Write-Host "Generating ZIP..." -ForegroundColor Cyan
if (Test-Path $ZipPath) { Remove-Item $ZipPath }
Compress-Archive -Path "$OutputDir\*" -DestinationPath $ZipPath

Write-Host "Done! Captures saved in $OutputDir" -ForegroundColor Green
Write-Host "ZIP generated at $ZipPath" -ForegroundColor Green
