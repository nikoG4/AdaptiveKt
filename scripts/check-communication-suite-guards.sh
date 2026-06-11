#!/bin/bash
set -e

WORKSPACE_ROOT=$(dirname "$0")/..
SOURCE_DIR="$WORKSPACE_ROOT/examples/communication-suite-demo/src"
FAILED=0

echo "Checking Communication Suite Demo Guards..."

PATTERNS=(
    "BoxWithConstraints"
    "breakpointForWidth"
    "import androidx.compose.foundation.text.selection.SelectionContainer"
    "import androidx.compose.ui.window.Dialog"
    "import androidx.compose.material.Button"
    "import androidx.compose.material3.Button"
    "import androidx.compose.material.Card"
    "import androidx.compose.material3.Card"
    "import androidx.compose.material.TextField"
    "import androidx.compose.material3.TextField"
    "TODO()"
    "error(\"not implemented\")"
    "NotImplementedError"
)

for pattern in "${PATTERNS[@]}"; do
    if grep -rnw "$SOURCE_DIR" -e "$pattern" > /dev/null 2>&1; then
        echo "ERROR: Found forbidden pattern in Communication Suite: $pattern"
        FAILED=1
    fi
done

if [ $FAILED -ne 0 ]; then
    echo "Communication Suite guards failed."
    exit 1
else
    echo "Communication Suite guards passed."
    exit 0
fi
