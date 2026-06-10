#!/usr/bin/env bash
set -e

echo "============================================================"
echo " PHASE 1: ROOT LIBRARY BUILD"
echo "============================================================"
./gradlew clean build

echo ""
echo "============================================================"
echo " PHASE 2: CORE JVM TESTS"
echo "============================================================"
./gradlew :adaptive-core:jvmTest
./gradlew :adaptive-layout:jvmTest
./gradlew :adaptive-navigation:jvmTest

echo ""
echo "============================================================"
echo " PHASE 3: LAYOUT GUARDS"
echo "============================================================"
if [ -f "./scripts/check-ai-workspace-layout-guards.sh" ]; then
    ./scripts/check-ai-workspace-layout-guards.sh
elif [ -f "./scripts/check-ai-workspace-layout-guards.ps1" ]; then
    # In bash environments, if powershell core (pwsh) is available, use it.
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
