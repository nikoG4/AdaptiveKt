#!/bin/bash
set -e

OUTPUT_DIR="artifacts/screenshots/home-code-comparison"
BASE_URL=""
SKIP_BUILD=false

while [[ "$#" -gt 0 ]]; do
    case $1 in
        --output-dir) OUTPUT_DIR="$2"; shift ;;
        --base-url) BASE_URL="$2"; shift ;;
        --skip-build) SKIP_BUILD=true ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if [ "$SKIP_BUILD" = false ]; then
    echo "Preparing Pages site..."
    pwsh ./tools/prepare-pages-site.ps1 || ./gradlew :docs-site:wasmJsBrowserDistribution
fi

if [ ! -f "site-dist/index.html" ]; then
    echo "site-dist index.html route is missing."
    exit 1
fi

echo "Installing Playwright dependencies..."
cd tools/docs-site-capture
npm install
npx playwright install chromium
cd ../..

SERVER_PID=""
CAPTURE_BASE_URL="$BASE_URL"

if [ -z "$CAPTURE_BASE_URL" ]; then
    CAPTURE_BASE_URL="http://localhost:8080"
    echo "Starting local site-dist server at $CAPTURE_BASE_URL..."
    python3 -m http.server 8080 -d site-dist &
    SERVER_PID=$!
    
    # Wait for server
    timeout=45
    while ! curl -s "$CAPTURE_BASE_URL" > /dev/null; do
        timeout=$((timeout - 1))
        if [ $timeout -eq 0 ]; then
            echo "Timed out waiting for local site."
            kill $SERVER_PID
            exit 1
        fi
        sleep 1
    done
    echo "Local site is ready."
fi

mkdir -p "$OUTPUT_DIR"

echo "Running home code comparison Playwright captures..."
cd tools/docs-site-capture
if node ./capture-home-code-comparison.js "$CAPTURE_BASE_URL" "$ROOT_DIR/$OUTPUT_DIR"; then
    echo "Home code comparison captures saved."
else
    echo "Home code comparison Playwright capture failed."
    EXIT_CODE=1
fi
cd ../..

if [ -n "$SERVER_PID" ]; then
    echo "Stopping local site-dist server..."
    kill $SERVER_PID || true
fi

exit ${EXIT_CODE:-0}
