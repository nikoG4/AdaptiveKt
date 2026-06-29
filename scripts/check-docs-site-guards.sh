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

echo "Running DocsRegistry validation..."
python "$WORKSPACE_ROOT/scripts/check-docs-site-registry.py" || FAILED=1

if [ $FAILED -ne 0 ]; then
    echo "Docs site guards failed."
    exit 1
else
    echo "Docs site guards passed."
    exit 0
fi


