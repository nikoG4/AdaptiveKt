#!/bin/bash
set -euo pipefail

FAILED=0

echo "Running navigation guards..."

# 1. BackHandler should not be used manually inside demo screens.
while IFS= read -r file; do
    case "$(basename "$file")" in
        AiWorkspaceApp.kt|EcommerceApp.kt|AdminDemoApp.kt)
            continue
            ;;
    esac

    if grep -q "BackHandler(" "$file"; then
        echo "ERROR: Manual BackHandler found in $file. Use AdaptiveNavigationBackHandler at root instead."
        FAILED=1
    fi
    if grep -q "androidx.activity.compose.BackHandler" "$file"; then
        echo "ERROR: Direct BackHandler import found in $file. Use AdaptiveNavigationBackHandler at root instead."
        FAILED=1
    fi
done < <(find examples -name "*.kt" -type f)

# 2. Android-only imports in commonMain.
while IFS= read -r file; do
    if grep -q "androidx.activity" "$file"; then
        echo "ERROR: Android-only import found in commonMain: $file."
        FAILED=1
    fi
done < <(find . -path "*/src/commonMain/*.kt" -type f)

if [ "$FAILED" -eq 1 ]; then
    echo "Navigation guard checks FAILED."
    exit 1
fi

echo "Navigation guard checks PASSED."
