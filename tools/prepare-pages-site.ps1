param(
    [string]$DistDir = "site-dist",
    [string]$DocsSiteDist = "docs-site/build/dist/wasmJs/productionExecutable",
    [string]$AdminDemoDist = "admin-demo/build/dist/wasmJs/productionExecutable",
    [string]$EcommerceDemoDist = "examples/ecommerce-demo/build/dist/wasmJs/productionExecutable",
    [string]$AiWorkspaceDemoDist = "examples/ai-workspace-demo/build/dist/wasmJs/productionExecutable",
    [string]$CommunicationSuiteDemoDist = "examples/communication-suite-demo/build/dist/wasmJs/productionExecutable",
    [string]$BasePath = "/",
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

    Write-Host "Building ecommerce-demo Wasm distribution..." -ForegroundColor Cyan
    .\gradlew.bat -p examples/ecommerce-demo wasmJsBrowserDistribution --console=plain --stacktrace

    Write-Host "Building ai-workspace-demo Wasm distribution..." -ForegroundColor Cyan
    .\gradlew.bat -p examples/ai-workspace-demo wasmJsBrowserDistribution --console=plain --stacktrace

    Write-Host "Building communication-suite-demo Wasm distribution..." -ForegroundColor Cyan
    .\gradlew.bat -p examples/communication-suite-demo wasmJsBrowserDistribution --console=plain --stacktrace
}

$distPath = Join-Path $root $DistDir
$docsPath = Join-Path $root $DocsSiteDist
$demoPath = Join-Path $root $AdminDemoDist
$ecommercePath = Join-Path $root $EcommerceDemoDist
$aiWorkspacePath = Join-Path $root $AiWorkspaceDemoDist
$communicationSuitePath = Join-Path $root $CommunicationSuiteDemoDist

$demoTarget = Join-Path $distPath "demo\app"
$ecommerceTarget = Join-Path $distPath "examples\ecommerce"
$aiWorkspaceTarget = Join-Path $distPath "examples\ai-workspace"
$communicationSuiteTarget = Join-Path $distPath "examples\communication-suite"

function Normalize-BasePath {
    param([string]$Path)
    if ([string]::IsNullOrWhiteSpace($Path)) {
        return "/"
    }
    $normalized = $Path.Trim()
    if (-not $normalized.StartsWith("/")) {
        $normalized = "/$normalized"
    }
    if (-not $normalized.EndsWith("/")) {
        $normalized = "$normalized/"
    }
    return $normalized
}

function Join-BasePath {
    param(
        [string]$Base,
        [string]$Route
    )
    $normalizedBase = Normalize-BasePath $Base
    $normalizedRoute = $Route.Trim("/")
    if ([string]::IsNullOrWhiteSpace($normalizedRoute)) {
        return $normalizedBase
    }
    return "$normalizedBase$normalizedRoute/"
}

$normalizedBasePath = Normalize-BasePath $BasePath

if (-not (Test-Path $docsPath)) {
    throw "Docs site distribution not found: $docsPath"
}

if (-not (Test-Path $demoPath)) {
    throw "Admin demo distribution not found: $demoPath"
}

if (-not (Test-Path $ecommercePath)) {
    throw "Ecommerce demo distribution not found: $ecommercePath"
}

if (-not (Test-Path $aiWorkspacePath)) {
    throw "AI Workspace demo distribution not found: $aiWorkspacePath"
}

if (-not (Test-Path $communicationSuitePath)) {
    throw "Communication Suite demo distribution not found: $communicationSuitePath"
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

New-Item -ItemType Directory -Force -Path $ecommerceTarget | Out-Null
Copy-Item -Path (Join-Path $ecommercePath "*") -Destination $ecommerceTarget -Recurse -Force

New-Item -ItemType Directory -Force -Path $aiWorkspaceTarget | Out-Null
Copy-Item -Path (Join-Path $aiWorkspacePath "*") -Destination $aiWorkspaceTarget -Recurse -Force

New-Item -ItemType Directory -Force -Path $communicationSuiteTarget | Out-Null
Copy-Item -Path (Join-Path $communicationSuitePath "*") -Destination $communicationSuiteTarget -Recurse -Force

New-Item -ItemType File -Force -Path (Join-Path $distPath ".nojekyll") | Out-Null

$routeTemplate = @"
<!doctype html>
<html lang="en">
<head>
  <base href="$normalizedBasePath">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>AdaptiveKt - Compose Multiplatform Admin UI Toolkit</title>
  <meta name="description" content="AdaptiveKt documentation site rendered with Compose Multiplatform and real AdaptiveKt components.">
  <link rel="icon" type="image/svg+xml" href="assets/brand/adaptivekt-symbol.svg">
  <meta name="theme-color" content="#2563EB">
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
  <script src="docs-site.js"></script>
</body>
</html>
"@

foreach ($route in @("components", "docs", "demo")) {
    $routeDir = Join-Path $distPath $route
    New-Item -ItemType Directory -Force -Path $routeDir | Out-Null
    Set-Content -Path (Join-Path $routeDir "index.html") -Value $routeTemplate -Encoding UTF8
}

function Inject-BaseHref {
    param([string]$FilePath, [string]$BaseHref)
    $content = Get-Content $FilePath -Raw
    if ($content -notmatch "<base href=") {
        $content = $content -replace "(?i)(<head.*?>)", "`$1`n  <base href=`"$BaseHref`">"
        Set-Content -Path $FilePath -Value $content -Encoding UTF8
    }
}

Inject-BaseHref -FilePath (Join-Path $distPath "index.html") -BaseHref $normalizedBasePath
Inject-BaseHref -FilePath (Join-Path $demoTarget "index.html") -BaseHref (Join-BasePath $normalizedBasePath "demo/app")
Inject-BaseHref -FilePath (Join-Path $ecommerceTarget "index.html") -BaseHref (Join-BasePath $normalizedBasePath "examples/ecommerce")
Inject-BaseHref -FilePath (Join-Path $aiWorkspaceTarget "index.html") -BaseHref (Join-BasePath $normalizedBasePath "examples/ai-workspace")
Inject-BaseHref -FilePath (Join-Path $communicationSuiteTarget "index.html") -BaseHref (Join-BasePath $normalizedBasePath "examples/communication-suite")

Write-Host "Prepared Pages site: $distPath" -ForegroundColor Green
Write-Host "Base path: $normalizedBasePath" -ForegroundColor Green
Write-Host "Docs site copied from: $docsPath" -ForegroundColor Green
Write-Host "Admin demo copied to: $demoTarget" -ForegroundColor Green
Write-Host "Ecommerce showcase copied to: $ecommerceTarget" -ForegroundColor Green
Write-Host "AI Workspace showcase copied to: $aiWorkspaceTarget" -ForegroundColor Green
Write-Host "Communication Suite showcase copied to: $communicationSuiteTarget" -ForegroundColor Green
