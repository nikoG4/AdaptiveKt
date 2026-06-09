param(
    [string]$DistDir = "site-dist"
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
$distPath = Join-Path $root $DistDir

if (-not (Test-Path $distPath)) {
    throw "Distribution directory not found. Please run tools/prepare-pages-site.ps1 first."
}

$expectedPaths = @(
    "index.html",
    "demo/app/index.html",
    "examples/ecommerce/index.html",
    "examples/ai-workspace/index.html"
)

$allFound = $true

Write-Host "Validating local Pages output at $distPath..." -ForegroundColor Cyan

foreach ($path in $expectedPaths) {
    $fullPath = Join-Path $distPath $path
    if (Test-Path $fullPath) {
        Write-Host "[OK] $path exists" -ForegroundColor Green
    } else {
        Write-Host "[FAIL] Missing expected route index: $path" -ForegroundColor Red
        $allFound = $false
    }
}

if (-not $allFound) {
    throw "One or more required demo index pages are missing from the generated site."
}

Write-Host "Static link check passed successfully." -ForegroundColor Green
exit 0
