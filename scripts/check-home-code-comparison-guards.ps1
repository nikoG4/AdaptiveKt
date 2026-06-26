$ErrorActionPreference = "Stop"

$manifestPath = "artifacts\screenshots\home-code-comparison\manifest.json"

if (-not (Test-Path $manifestPath)) {
    Write-Error "Manifest not found at $manifestPath"
    exit 1
}

$manifest = Get-Content $manifestPath | ConvertFrom-Json

Write-Host "Checking Home Code Comparison Guards..."

if ($manifest.consoleErrors -gt 0) {
    Write-Error "Validation failed: Found $($manifest.consoleErrors) console errors"
    exit 1
}

if ($manifest.pageErrors -gt 0) {
    Write-Error "Validation failed: Found $($manifest.pageErrors) page errors"
    exit 1
}

if ($manifest.failedRequests -gt 0) {
    Write-Error "Validation failed: Found $($manifest.failedRequests) failed requests"
    exit 1
}

if ($manifest.horizontalOverflowFailures -gt 0) {
    Write-Error "Validation failed: Found $($manifest.horizontalOverflowFailures) horizontal overflows"
    exit 1
}

if ($manifest.savedLines -lt 40) {
    Write-Error "Validation failed: savedLines is $($manifest.savedLines), expected >= 40"
    exit 1
}

if ($manifest.reductionPercent -lt 35) {
    Write-Error "Validation failed: reductionPercent is $($manifest.reductionPercent), expected >= 35"
    exit 1
}

$hasBase = $false
$hasExpanded = $false

foreach ($screenshot in $manifest.screenshots) {
    if ($screenshot.state -eq "base") { $hasBase = $true }
    if ($screenshot.state -like "expanded*") { $hasExpanded = $true }
}

if (-not $hasBase) {
    Write-Error "Validation failed: Missing base screenshots"
    exit 1
}

if (-not $hasExpanded) {
    Write-Error "Validation failed: Missing expanded screenshots"
    exit 1
}

Write-Host "All home code comparison guards passed." -ForegroundColor Green
exit 0
