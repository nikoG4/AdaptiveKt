$ErrorActionPreference = "Stop"

$manifestPath = "artifacts\screenshots\home-code-comparison\manifest.json"
$reportPath = "docs\internal\HOME_CODE_COMPARISON_REPORT.md"

if (-not (Test-Path $manifestPath)) {
    Write-Error "Manifest not found at $manifestPath"
    exit 1
}

$manifest = Get-Content $manifestPath | ConvertFrom-Json

$dateStr = (Get-Date).ToString("yyyy-MM-dd HH:mm:ssZ")

$report = @"
# Home Code Comparison Visual Report

Generated on: $dateStr
Git SHA: $($manifest.gitSha)

## Metrics Validation

- AdaptiveKt Lines: $($manifest.adaptiveLines)
- Plain Compose Lines: $($manifest.composeLines)
- Lines Saved: $($manifest.savedLines)
- Reduction: $($manifest.reductionPercent)%

## Screenshots

| Viewport | Theme | State | File |
|----------|-------|-------|------|
"@

foreach ($screenshot in $manifest.screenshots) {
    $report += "`n| $($screenshot.viewport) | $($screenshot.theme) | $($screenshot.state) | [image](../../artifacts/screenshots/home-code-comparison/$($screenshot.file)) |"
}

$report += @"

## Stability

- Console Errors: $($manifest.consoleErrors)
- Page Errors: $($manifest.pageErrors)
- Failed Requests: $($manifest.failedRequests)
- Horizontal Overflows: $($manifest.horizontalOverflowFailures)
"@

if (!(Test-Path "docs\internal")) {
    New-Item -ItemType Directory -Force -Path "docs\internal" | Out-Null
}

Set-Content -Path $reportPath -Value $report

Write-Host "Report generated at $reportPath" -ForegroundColor Green
