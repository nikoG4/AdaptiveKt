#!/bin/bash
set -eo pipefail

MANIFEST_PATH="artifacts/screenshots/home-code-comparison/manifest.json"

echo "Checking Home Code Comparison Guards..."

if [ -f "$MANIFEST_PATH" ]; then
    CONSOLE_ERRORS=$(grep -o '"consoleErrors": *[0-9]*' "$MANIFEST_PATH" | awk -F: '{print $2}' | tr -d ' ' | head -n 1)
    PAGE_ERRORS=$(grep -o '"pageErrors": *[0-9]*' "$MANIFEST_PATH" | awk -F: '{print $2}' | tr -d ' ' | head -n 1)
    FAILED_REQUESTS=$(grep -o '"failedRequests": *[0-9]*' "$MANIFEST_PATH" | awk -F: '{print $2}' | tr -d ' ' | head -n 1)
    OVERFLOWS=$(grep -o '"horizontalOverflowFailures": *[0-9]*' "$MANIFEST_PATH" | awk -F: '{print $2}' | tr -d ' ' | head -n 1)
    SAVED_LINES=$(grep -o '"savedLines": *[0-9]*' "$MANIFEST_PATH" | awk -F: '{print $2}' | tr -d ' ' | head -n 1)
    REDUCTION=$(grep -o '"reductionPercent": *[0-9]*' "$MANIFEST_PATH" | awk -F: '{print $2}' | tr -d ' ' | head -n 1)

    if [ "$CONSOLE_ERRORS" -gt 0 ]; then
        echo "Validation failed: Found $CONSOLE_ERRORS console errors" >&2
        exit 1
    fi

    if [ "$PAGE_ERRORS" -gt 0 ]; then
        echo "Validation failed: Found $PAGE_ERRORS page errors" >&2
        exit 1
    fi

    if [ "$FAILED_REQUESTS" -gt 0 ]; then
        echo "Validation failed: Found $FAILED_REQUESTS failed requests" >&2
        exit 1
    fi

    if [ "$OVERFLOWS" -gt 0 ]; then
        echo "Validation failed: Found $OVERFLOWS horizontal overflows" >&2
        exit 1
    fi

    if [ "$SAVED_LINES" -lt 40 ]; then
        echo "Validation failed: savedLines is $SAVED_LINES, expected >= 40" >&2
        exit 1
    fi

    if [ "$REDUCTION" -lt 35 ]; then
        echo "Validation failed: reductionPercent is $REDUCTION, expected >= 35" >&2
        exit 1
    fi

    if ! grep -q '"state": *"base"' "$MANIFEST_PATH"; then
        echo "Validation failed: Missing base screenshots" >&2
        exit 1
    fi

    if ! grep -q '"state": *"expanded' "$MANIFEST_PATH"; then
        echo "Validation failed: Missing expanded screenshots" >&2
        exit 1
    fi
else
    echo "Manifest not found at $MANIFEST_PATH. Skipping dynamic checks in bash."
fi

# Static Checks
echo "Running static analysis checks..."

if ! grep -q "HomeCodeComparisonSection" docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteHomePage.kt; then
    echo "Static check failed: HomeCodeComparisonSection is not integrated in SiteHomePage" >&2
    exit 1
fi

if [ -f "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteRoute.kt" ]; then
    if grep -q "SiteRoute.Compare" docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteRoute.kt; then
        echo "Static check failed: SiteRoute.Compare exists" >&2
        exit 1
    fi

    if grep -q '"/compare"' docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteRoute.kt; then
        echo "Static check failed: route /compare exists" >&2
        exit 1
    fi
fi

if ! grep -q "840.dp" docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSnippets.kt; then
    echo "Static check failed: 840.dp breakpoint not found" >&2
    exit 1
fi

if ! grep -q "960.dp" docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonModels.kt; then
    echo "Static check failed: 960.dp visual threshold not found" >&2
    exit 1
fi

if grep -q "maxWidth < 600.dp" docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSection.kt; then
    echo "Static check failed: maxWidth < 600.dp is present" >&2
    exit 1
fi

if grep -q "AdaptiveKt: 28" docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSection.kt; then
    echo "Static check failed: metrics are hardcoded in UI" >&2
    exit 1
fi

if [ ! -f "docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSnippets.kt" ]; then
    echo "Static check failed: Snippets file is missing" >&2
    exit 1
fi

if [ ! -f "docs-site/src/commonTest/kotlin/io/github/adaptivekt/site/HomeCodeComparisonCompileFixtures.kt" ]; then
    echo "Static check failed: Fixtures file is missing" >&2
    exit 1
fi

if [ ! -f "docs-site/src/commonTest/kotlin/io/github/adaptivekt/site/HomeCodeComparisonMetricsTest.kt" ]; then
    echo "Static check failed: Parity tests file is missing" >&2
    exit 1
fi

if [ ! -f "tools/capture-home-code-comparison.ps1" ]; then
    echo "Static check failed: Capture wrapper missing" >&2
    exit 1
fi

if ! grep -q 'capture-home-code-comparison.ps1' .github/workflows/ci.yml; then
    echo "Static check failed: workflow does not use correct wrapper" >&2
    exit 1
fi

if grep -q 'ci/capture-code-comparison.mjs' .github/workflows/ci.yml; then
    echo "Static check failed: workflow references old capture script" >&2
    exit 1
fi

if grep -q 'tabModifier' adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/AdaptiveTabs.kt; then
    echo "Static check failed: AdaptiveTabs was modified with tabModifier" >&2
    exit 1
fi

echo "All static checks and home code comparison guards passed."
exit 0
