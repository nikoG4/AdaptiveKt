#!/usr/bin/env pwsh
$ErrorActionPreference = "Stop"

$demoDir = "$PSScriptRoot/../examples/ecommerce-demo/src/commonMain/kotlin"
Write-Host "Running Ecommerce Demo Layout Guard checks..." -ForegroundColor Cyan
$ktFiles = Get-ChildItem -Path $demoDir -Recurse -Filter *.kt

# Check for BoxWithConstraints
$bwc = @($ktFiles | Select-String -Pattern "BoxWithConstraints")
if ($bwc) {
    Write-Host "ERROR: BoxWithConstraints found in ecommerce-demo. Use AdaptiveKt primitives instead." -ForegroundColor Red
    $bwc | ForEach-Object { Write-Host "$($_.Path): $($_.Line.Trim())" }
    exit 1
}

# Check for breakpointForWidth
$bp = @($ktFiles | Select-String -Pattern "breakpointForWidth")
if ($bp) {
    Write-Host "ERROR: breakpointForWidth found in ecommerce-demo." -ForegroundColor Red
    $bp | ForEach-Object { Write-Host "$($_.Path): $($_.Line.Trim())" }
    exit 1
}

$layoutInfo = @($ktFiles | Select-String -Pattern "LocalAdaptiveLayoutInfo")
$adaptiveGridColumns = @($ktFiles | Select-String -Pattern "AdaptiveGrid\s*\([^)]*columns\s*=")
$genericButtons = @($ktFiles | Select-String -Pattern "(?<!Adaptive)Button\s*\(")
$genericCards = @($ktFiles | Select-String -Pattern "(?<!Adaptive)Card\s*\(")
$genericTextFields = @($ktFiles | Select-String -Pattern "(?<!Adaptive)TextField\s*\(")
$collectionViews = @($ktFiles | Select-String -Pattern "AdaptiveCollectionView\s*\(")
$dataViewDisplayModes = @($ktFiles | Select-String -Pattern "AdaptiveDataView\s*\([^)]*displayMode\s*=")

Write-Host "WARN: LocalAdaptiveLayoutInfo usage count: $($layoutInfo.Count)" -ForegroundColor Yellow
Write-Host "WARN: AdaptiveGrid(columns =) usage count: $($adaptiveGridColumns.Count)" -ForegroundColor Yellow
Write-Host "WARN: generic Button( usage count: $($genericButtons.Count)" -ForegroundColor Yellow
Write-Host "WARN: generic Card( usage count: $($genericCards.Count)" -ForegroundColor Yellow
Write-Host "WARN: generic TextField( usage count: $($genericTextFields.Count)" -ForegroundColor Yellow
Write-Host "INFO: AdaptiveCollectionView usage count: $($collectionViews.Count)" -ForegroundColor Cyan
Write-Host "INFO: AdaptiveDataView displayMode usage count: $($dataViewDisplayModes.Count)" -ForegroundColor Cyan

Write-Host "SUCCESS: Ecommerce Demo Layout Guard checks passed." -ForegroundColor Green
exit 0
