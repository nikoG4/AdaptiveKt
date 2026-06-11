#!/bin/bash
set -e

WORKSPACE_ROOT=$(dirname "$0")/..
SOURCE_DIR="$WORKSPACE_ROOT/docs-site/src"
FAILED=0

echo "Checking for unsafe SelectionContainer imports in docs-site..."
if grep -rnw "$SOURCE_DIR" -e "import androidx.compose.foundation.text.selection.SelectionContainer"; then
    echo "ERROR: SelectionContainer should not be used directly in docs-site to prevent layout overlaps. Use AdaptiveSelectionArea."
    FAILED=1
fi

echo "Checking for duplicate hash IDs in SiteComponentsPage.kt..."
DUPLICATES=$(grep -oE 'id = "([^"]+)"' "$SOURCE_DIR/commonMain/kotlin/io/github/adaptivekt/site/SiteComponentsPage.kt" | sort | uniq -d)
if [ -n "$DUPLICATES" ]; then
    echo "ERROR: Duplicate component IDs found:"
    echo "$DUPLICATES"
    FAILED=1
fi

if [ $FAILED -ne 0 ]; then
    echo "Docs site guards failed."
    exit 1
else
    echo "Docs site guards passed."
    exit 0
fi
