param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$OutputDir = "artifacts/route-validation",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$workspaceRoot = Split-Path -Parent $PSScriptRoot

if (-not $SkipBuild) {
    Write-Host "Rebuilding site distribution..."
    & "$workspaceRoot\tools\prepare-pages-site.ps1"
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to build site distribution."
    }
}

Write-Host "Installing Playwright dependencies..."
Push-Location "$workspaceRoot\tools\docs-site-capture"
npm install
if ($LASTEXITCODE -ne 0) {
    Pop-Location
    throw "Failed to install capture dependencies."
}
Pop-Location

Write-Host "Starting local site-dist server at $BaseUrl..."
$serverProcess = Start-Process -FilePath "python" -ArgumentList "-m http.server 8080 --directory ""$workspaceRoot\site-dist""" -PassThru -NoNewWindow
Start-Sleep -Seconds 3

try {
    Write-Host "Running route validation..."
    Push-Location "$workspaceRoot\tools\docs-site-capture"
    node validate-component-routes.js $BaseUrl "..\..\$OutputDir"
    $nodeExitCode = $LASTEXITCODE
    Pop-Location

    if ($nodeExitCode -ne 0) {
        throw "Component routes validation failed."
    }
} finally {
    Write-Host "Stopping local site-dist server..."
    Stop-Process -Id $serverProcess.Id -Force -ErrorAction SilentlyContinue
    Write-Host "Route validation reports saved in $OutputDir"
}
