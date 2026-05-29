param (
    [string]$OutputDir = "build\web-visual-captures",
    [string]$ZipPath = "build\adaptivekt-admin-demo-web-visual-captures.zip",
    [string]$BaseUrl = "",
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

function Test-PortAvailable {
    param([int]$Port)
    $listener = $null
    try {
        $listener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Loopback, $Port)
        $listener.Start()
        return $true
    }
    catch {
        return $false
    }
    finally {
        if ($listener) {
            $listener.Stop()
        }
    }
}

$ServerProcess = $null
$CaptureBaseUrl = $BaseUrl

if ([string]::IsNullOrWhiteSpace($CaptureBaseUrl)) {
    $Port = 8080
    while (-not (Test-PortAvailable -Port $Port)) {
        $Port++
    }
    $CaptureBaseUrl = "http://localhost:$Port"

    Write-Host "Starting local server at $CaptureBaseUrl..." -ForegroundColor Cyan
    $ServerProcess = Start-Process -FilePath python -ArgumentList @("-m", "http.server", $Port.ToString(), "-d", $DistDir) -WindowStyle Hidden -PassThru
    Start-Sleep -Seconds 3
}

try {
    Write-Host "Running Playwright captures..." -ForegroundColor Cyan
    node tools\web-capture\capture-admin-demo-web.js $CaptureBaseUrl $OutputDir
}
finally {
    if ($ServerProcess -and -not $ServerProcess.HasExited) {
        Write-Host "Stopping local server..." -ForegroundColor Cyan
        Stop-Process -Id $ServerProcess.Id -Force
    }
}

Write-Host "Generating ZIP..." -ForegroundColor Cyan
if (Test-Path $ZipPath) { Remove-Item $ZipPath }
Compress-Archive -Path "$OutputDir\*" -DestinationPath $ZipPath

Write-Host "Done! Captures saved in $OutputDir" -ForegroundColor Green
Write-Host "ZIP generated at $ZipPath" -ForegroundColor Green
