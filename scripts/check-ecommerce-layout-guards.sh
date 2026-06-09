#!/usr/bin/env bash
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DEMO_DIR="$DIR/../examples/ecommerce-demo/src/commonMain/kotlin"
echo -e "\033[0;36mRunning Ecommerce Demo Layout Guard checks...\033[0m"

if grep -rn "BoxWithConstraints" "$DEMO_DIR"; then
    echo -e "\033[0;31mERROR: BoxWithConstraints found in ecommerce-demo. Use AdaptiveKt primitives instead.\033[0m"
    exit 1
fi

if grep -rn "breakpointForWidth" "$DEMO_DIR"; then
    echo -e "\033[0;31mERROR: breakpointForWidth found in ecommerce-demo.\033[0m"
    exit 1
fi

INFO_COUNT=$(grep -rn "LocalAdaptiveLayoutInfo" "$DEMO_DIR" | wc -l)
if [ "$INFO_COUNT" -gt 2 ]; then
    echo -e "\033[0;31mERROR: Too many LocalAdaptiveLayoutInfo usages. Expected max 2, found $INFO_COUNT.\033[0m"
    grep -rn "LocalAdaptiveLayoutInfo" "$DEMO_DIR"
    exit 1
fi

echo -e "\033[0;32mSUCCESS: Ecommerce Demo Layout Guard checks passed.\033[0m"
exit 0
