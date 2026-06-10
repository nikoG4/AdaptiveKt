<#
.SYNOPSIS
Verifies the root AdaptiveKt library and core layout components.

.DESCRIPTION
Runs a clean build of the root project, executes JVM tests for core adaptive modules,
and runs layout constraint static guards to ensure legacy primitives (BoxWithConstraints)
are not used.
#>

$ErrorActionPreference = "Stop"

Write-Host "============================================================"
Write-Host " PHASE 1: ROOT LIBRARY BUILD"
Write-Host "============================================================"
.\gradlew.bat clean build
if ($LASTEXITCODE -ne 0) { throw "Root build failed." }

Write-Host "`n============================================================"
Write-Host " PHASE 2: CORE JVM TESTS"
Write-Host "============================================================"
.\gradlew.bat :adaptive-core:jvmTest
if ($LASTEXITCODE -ne 0) { throw ":adaptive-core:jvmTest failed." }

.\gradlew.bat :adaptive-layout:jvmTest
if ($LASTEXITCODE -ne 0) { throw ":adaptive-layout:jvmTest failed." }

.\gradlew.bat :adaptive-navigation:jvmTest
if ($LASTEXITCODE -ne 0) { throw ":adaptive-navigation:jvmTest failed." }

Write-Host "`n============================================================"
Write-Host " PHASE 3: LAYOUT GUARDS"
Write-Host "============================================================"
if (Test-Path ".\scripts\check-ai-workspace-layout-guards.ps1") {
    powershell.exe -ExecutionPolicy Bypass -File ".\scripts\check-ai-workspace-layout-guards.ps1"
    if ($LASTEXITCODE -ne 0) { throw "Layout guards failed." }
} else {
    Write-Host "Layout guard script not found, skipping."
}

Write-Host "`n============================================================"
Write-Host " VERIFICATION SUCCESSFUL"
Write-Host "============================================================"
