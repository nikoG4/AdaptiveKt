<#
.SYNOPSIS
Verifies the AI Workspace Demo app.

.DESCRIPTION
Builds the AI Workspace Demo, runs its desktop routing/component tests,
executes the Wasm distribution build to verify memory constraints, and finally
checks the layout guards.
#>

$ErrorActionPreference = "Stop"

$RootDir = Get-Location
$DemoDir = Join-Path $RootDir "examples\ai-workspace-demo"

Write-Host "============================================================"
Write-Host " PHASE 1: AI WORKSPACE BUILD"
Write-Host "============================================================"
Set-Location $DemoDir
.\gradlew.bat clean build
if ($LASTEXITCODE -ne 0) { throw "Demo build failed." }

Write-Host "`n============================================================"
Write-Host " PHASE 2: AI WORKSPACE DESKTOP & ROUTE TESTS"
Write-Host "============================================================"
.\gradlew.bat desktopTest
if ($LASTEXITCODE -ne 0) { throw "Demo desktopTest failed." }

Write-Host "`n============================================================"
Write-Host " PHASE 3: AI WORKSPACE WASM DISTRIBUTION"
Write-Host "============================================================"
.\gradlew.bat wasmJsBrowserDistribution
if ($LASTEXITCODE -ne 0) { throw "Demo Wasm build failed." }

Write-Host "`n============================================================"
Write-Host " PHASE 4: LAYOUT GUARDS"
Write-Host "============================================================"
Set-Location $RootDir
if (Test-Path ".\scripts\check-ai-workspace-layout-guards.ps1") {
    powershell.exe -ExecutionPolicy Bypass -File ".\scripts\check-ai-workspace-layout-guards.ps1"
    if ($LASTEXITCODE -ne 0) { throw "Layout guards failed." }
} else {
    Write-Host "Layout guard script not found, skipping."
}

Write-Host "`n============================================================"
Write-Host " VERIFICATION SUCCESSFUL"
Write-Host "============================================================"
