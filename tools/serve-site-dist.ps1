param(
    [int]$Port = 8090,
    [string]$DistDir = "site-dist"
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

if (-not (Test-Path $DistDir)) {
    throw "Distribution directory not found: $DistDir. Run tools\prepare-pages-site.ps1 first."
}

Write-Host "Serving $DistDir at:" -ForegroundColor Cyan
Write-Host "  http://localhost:$Port/" -ForegroundColor Green
Write-Host "  http://localhost:$Port/demo/app/" -ForegroundColor Green
Write-Host "Press Ctrl+C to stop." -ForegroundColor Cyan

python -m http.server $Port -d $DistDir
