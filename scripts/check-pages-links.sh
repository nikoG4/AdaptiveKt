#!/usr/bin/env bash
set -e

DIST_DIR="site-dist"
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DIST_PATH="$ROOT_DIR/$DIST_DIR"

if [ ! -d "$DIST_PATH" ]; then
    echo "Error: Distribution directory not found. Please run tools/prepare-pages-site.sh first."
    exit 1
fi

EXPECTED_PATHS=(
    "index.html"
    "demo/app/index.html"
    "examples/ecommerce/index.html"
    "examples/ai-workspace/index.html"
)

ALL_FOUND=true

echo "Validating local Pages output at $DIST_PATH..."

for path in "${EXPECTED_PATHS[@]}"; do
    if [ -f "$DIST_PATH/$path" ]; then
        echo "[OK] $path exists"
    else
        echo "[FAIL] Missing expected route index: $path"
        ALL_FOUND=false
    fi
done

if [ "$ALL_FOUND" = false ]; then
    echo "Error: One or more required demo index pages are missing from the generated site."
    exit 1
fi

echo "Static link check passed successfully."
exit 0
