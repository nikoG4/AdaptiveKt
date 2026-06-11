param(
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"
$workspaceRoot = Split-Path -Parent $PSScriptRoot

Push-Location $workspaceRoot

if (-not $SkipBuild) {
    Write-Host "Building Pages site..."
    .\tools\prepare-pages-site.ps1
}

Write-Host "Starting local server..."
Start-Job -Name "HttpServer" -ScriptBlock {
    python -m http.server 8080 --directory "$using:workspaceRoot\site-dist"
}

Start-Sleep -Seconds 3

try {
    Write-Host "Installing Playwright dependencies..."
    Push-Location tools\docs-site-capture
    npm install
    npx playwright install chromium
    
    Write-Host "Running visual capture..."
    node capture-communication-suite.js
} finally {
    Pop-Location
    Write-Host "Stopping local server..."
    Stop-Job -Name "HttpServer"
    Remove-Job -Name "HttpServer"
    Pop-Location
}
