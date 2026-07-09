$ErrorActionPreference = "Stop"

$manifestPath = "artifacts\screenshots\home-code-comparison\manifest.json"

if (Test-Path $manifestPath) {
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

    if ($manifest.httpErrors -gt 0) {
        Write-Error "Validation failed: Found $($manifest.httpErrors) HTTP error responses"
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
} else {
    Write-Host "Manifest not found at $manifestPath. Skipping dynamic checks."
}

Write-Host "Running static analysis checks..."

$homePage = Get-Content "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteHomePage.kt" -ErrorAction SilentlyContinue
if (-not ($homePage -match "HomeCodeComparisonSection")) {
    Write-Error "Static check failed: HomeCodeComparisonSection is not integrated in SiteHomePage"
    exit 1
}

if (Test-Path "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteRoute.kt") {
    $siteRoute = Get-Content "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteRoute.kt"
    if ($siteRoute -match "SiteRoute\.Compare") {
        Write-Error "Static check failed: SiteRoute.Compare exists"
        exit 1
    }
    if ($siteRoute -match '"/compare"') {
        Write-Error "Static check failed: route /compare exists"
        exit 1
    }
}

$snippets = Get-Content "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSnippets.kt"
if (-not ($snippets -match "840\.dp")) {
    Write-Error "Static check failed: 840.dp breakpoint not found"
    exit 1
}

$models = Get-Content "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonModels.kt"
if (-not ($models -match "960\.dp")) {
    Write-Error "Static check failed: 960.dp visual threshold not found"
    exit 1
}

$section = Get-Content "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSection.kt"
if ($section -match "maxWidth < 600\.dp") {
    Write-Error "Static check failed: maxWidth < 600.dp is present"
    exit 1
}
if ($section -match "AdaptiveKt: 28") {
    Write-Error "Static check failed: metrics are hardcoded in UI"
    exit 1
}

if (-not (Test-Path "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSnippets.kt")) {
    Write-Error "Static check failed: Snippets file is missing"
    exit 1
}
if (-not (Test-Path "docs-site/src/commonTest/kotlin/io/github/adaptivekt/site/HomeCodeComparisonCompileFixtures.kt")) {
    Write-Error "Static check failed: Fixtures file is missing"
    exit 1
}
if (-not (Test-Path "docs-site/src/commonTest/kotlin/io/github/adaptivekt/site/HomeCodeComparisonMetricsTest.kt")) {
    Write-Error "Static check failed: Parity tests file is missing"
    exit 1
}
if (-not (Test-Path "tools/capture-home-code-comparison.ps1")) {
    Write-Error "Static check failed: Capture wrapper missing"
    exit 1
}

$ciyml = Get-Content ".github/workflows/ci.yml"
if (-not ($ciyml -match "capture-home-code-comparison\.ps1")) {
    Write-Error "Static check failed: workflow does not use correct wrapper"
    exit 1
}
if ($ciyml -match "ci/capture-code-comparison\.mjs") {
    Write-Error "Static check failed: workflow references old capture script"
    exit 1
}

$adaptiveTabs = Get-Content "adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/AdaptiveTabs.kt"
if ($adaptiveTabs -match "tabModifier") {
    Write-Error "Static check failed: AdaptiveTabs was modified with tabModifier"
    exit 1
}

Write-Host "All static checks and home code comparison guards passed." -ForegroundColor Green
exit 0
