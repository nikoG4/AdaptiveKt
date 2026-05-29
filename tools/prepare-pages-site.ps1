param(
    [string]$DistDir = "site-dist",
    [string]$DocsSiteDist = "docs-site/build/dist/wasmJs/productionExecutable",
    [string]$AdminDemoDist = "admin-demo/build/dist/wasmJs/productionExecutable",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

if (-not $SkipBuild) {
    Write-Host "Building docs-site Wasm distribution..." -ForegroundColor Cyan
    .\gradlew.bat :docs-site:wasmJsBrowserDistribution --console=plain --stacktrace

    Write-Host "Building admin-demo Wasm distribution..." -ForegroundColor Cyan
    .\gradlew.bat :admin-demo:wasmJsBrowserDistribution --console=plain --stacktrace
}

$distPath = Join-Path $root $DistDir
$docsPath = Join-Path $root $DocsSiteDist
$demoPath = Join-Path $root $AdminDemoDist
$demoTarget = Join-Path $distPath "demo\app"

if (-not (Test-Path $docsPath)) {
    throw "Docs site distribution not found: $docsPath"
}

if (-not (Test-Path $demoPath)) {
    throw "Admin demo distribution not found: $demoPath"
}

if (Test-Path $distPath) {
    $resolvedRoot = (Resolve-Path $root).Path
    $resolvedDist = (Resolve-Path $distPath).Path
    if (-not $resolvedDist.StartsWith($resolvedRoot, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Refusing to remove outside workspace: $resolvedDist"
    }
    Remove-Item -LiteralPath $resolvedDist -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $distPath | Out-Null
Copy-Item -Path (Join-Path $docsPath "*") -Destination $distPath -Recurse -Force

New-Item -ItemType Directory -Force -Path $demoTarget | Out-Null
Copy-Item -Path (Join-Path $demoPath "*") -Destination $demoTarget -Recurse -Force

New-Item -ItemType File -Force -Path (Join-Path $distPath ".nojekyll") | Out-Null

$routeTemplate = @"
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>AdaptiveKt - Compose Multiplatform Admin UI Toolkit</title>
  <meta name="description" content="AdaptiveKt documentation site rendered with Compose Multiplatform and real AdaptiveKt components.">
  <style>
    html, body, #webApp {
      width: 100%;
      height: 100%;
      margin: 0;
      overflow: hidden;
      background: #f7f9fc;
    }
  </style>
</head>
<body>
  <div id="webApp"></div>
  <script src="../docs-site.js"></script>
</body>
</html>
"@

foreach ($route in @("components", "docs", "demo")) {
    $routeDir = Join-Path $distPath $route
    New-Item -ItemType Directory -Force -Path $routeDir | Out-Null
    Set-Content -Path (Join-Path $routeDir "index.html") -Value $routeTemplate -Encoding UTF8
}

Write-Host "Prepared Pages site: $distPath" -ForegroundColor Green
Write-Host "Docs site copied from: $docsPath" -ForegroundColor Green
Write-Host "Admin demo copied to: $demoTarget" -ForegroundColor Green
