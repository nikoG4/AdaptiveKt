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
    node capture-communication-suite.js "http://localhost:8080/examples/communication-suite" "$workspaceRoot/artifacts/screenshots/communication-suite"
    if ($LASTEXITCODE -ne 0) { throw "Capture script failed" }

    $outDir = "$workspaceRoot/artifacts/screenshots/communication-suite"
    if (-not (Test-Path $outDir)) { throw "Output directory missing: $outDir" }
    $pngs = Get-ChildItem -Path $outDir -Filter "*.png"
    if ($pngs.Count -eq 0) { throw "No PNGs created in $outDir" }
    if (-not (Test-Path "$outDir/contact-sheet-communication-suite-light.png")) { throw "Missing light contact sheet" }
    if (-not (Test-Path "$outDir/contact-sheet-communication-suite-dark.png")) { throw "Missing dark contact sheet" }
    if (-not (Test-Path "$outDir/contact-sheet-communication-suite-all.png")) { throw "Missing all contact sheet" }
} finally {
    Pop-Location
    Write-Host "Stopping local server..."
    Stop-Job -Name "HttpServer"
    Remove-Job -Name "HttpServer"
    Pop-Location
}
