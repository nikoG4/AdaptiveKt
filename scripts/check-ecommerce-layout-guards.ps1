#!/usr/bin/env pwsh
$ErrorActionPreference = "Stop"

$demoDir = "$PSScriptRoot/../examples/ecommerce-demo/src/commonMain/kotlin"
Write-Host "Running Ecommerce Demo Layout Guard checks..." -ForegroundColor Cyan

# Check for BoxWithConstraints
$bwc = Select-String -Path "$demoDir\**\*.kt" -Pattern "BoxWithConstraints"
if ($bwc) {
    Write-Host "ERROR: BoxWithConstraints found in ecommerce-demo. Use AdaptiveKt primitives instead." -ForegroundColor Red
    $bwc | ForEach-Object { Write-Host $_.Line }
    exit 1
}

# Check for breakpointForWidth
$bp = Select-String -Path "$demoDir\**\*.kt" -Pattern "breakpointForWidth"
if ($bp) {
    Write-Host "ERROR: breakpointForWidth found in ecommerce-demo." -ForegroundColor Red
    $bp | ForEach-Object { Write-Host $_.Line }
    exit 1
}

# Check for LocalAdaptiveLayoutInfo count
$layoutInfo = Select-String -Path "$demoDir\**\*.kt" -Pattern "LocalAdaptiveLayoutInfo"
if ($layoutInfo.Count -gt 2) {
    Write-Host "ERROR: Too many LocalAdaptiveLayoutInfo usages. Expected max 2, found $($layoutInfo.Count)." -ForegroundColor Red
    $layoutInfo | ForEach-Object { Write-Host "$($_.Filename): $($_.Line)" }
    exit 1
}

Write-Host "SUCCESS: Ecommerce Demo Layout Guard checks passed." -ForegroundColor Green
exit 0
