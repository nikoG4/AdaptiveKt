#!/usr/bin/env bash
set -e

ROOT_DIR=$(pwd)
DEMO_DIR="$ROOT_DIR/examples/ai-workspace-demo"

echo "============================================================"
echo " PHASE 1: AI WORKSPACE BUILD"
echo "============================================================"
cd "$DEMO_DIR"
./gradlew clean build

echo ""
echo "============================================================"
echo " PHASE 2: AI WORKSPACE DESKTOP & ROUTE TESTS"
echo "============================================================"
./gradlew desktopTest

echo ""
echo "============================================================"
echo " PHASE 3: AI WORKSPACE WASM DISTRIBUTION"
echo "============================================================"
./gradlew wasmJsBrowserDistribution

echo ""
echo "============================================================"
echo " PHASE 4: LAYOUT GUARDS"
echo "============================================================"
cd "$ROOT_DIR"
if [ -f "./scripts/check-ai-workspace-layout-guards.sh" ]; then
    ./scripts/check-ai-workspace-layout-guards.sh
elif [ -f "./scripts/check-ai-workspace-layout-guards.ps1" ]; then
    if command -v pwsh &> /dev/null; then
        pwsh -ExecutionPolicy Bypass -File "./scripts/check-ai-workspace-layout-guards.ps1"
    else
        echo "pwsh not found, skipping powershell layout guards."
    fi
else
    echo "Layout guard script not found, skipping."
fi

echo ""
echo "============================================================"
echo " VERIFICATION SUCCESSFUL"
echo "============================================================"
