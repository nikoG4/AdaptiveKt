#!/usr/bin/env bash
set -euo pipefail

roots=(
  adaptive-components
  adaptive-data
  docs-site
  examples/ai-workspace-demo
  examples/ecommerce-demo
)

failures=0
warnings=0

echo "AdaptiveKt overlay and selection guard"

dialog_file="adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/AdaptiveDialog.kt"
if [[ ! -f "$dialog_file" ]]; then
  echo "[fail] AdaptiveDialog.kt is missing"
  failures=$((failures + 1))
else
  if ! grep -q "androidx.compose.ui.window.Dialog" "$dialog_file" || ! grep -qE "\bDialog[[:space:]]*\(" "$dialog_file"; then
    echo "[fail] AdaptiveDialog must render through Compose Dialog, not inline layout-flow content"
    failures=$((failures + 1))
  fi
  if grep -qE "Color\.(Black|White|Gray)|Color\(0x" "$dialog_file"; then
    echo "[fail] AdaptiveDialog uses hardcoded scrim/surface colors; use AdaptiveTheme/AdaptiveOverlayDefaults"
    failures=$((failures + 1))
  fi
fi

while IFS= read -r -d '' file; do
  if [[ "$file" != *"/src/commonMain/kotlin/"* ]]; then
    continue
  fi

  if [[ "$file" != *"AdaptiveSelectionArea.kt" ]] && grep -n "SelectionContainer" "$file" >/tmp/adaptivekt_overlay_guard.$$; then
    sed "s#^#[fail] $file:#" /tmp/adaptivekt_overlay_guard.$$
    failures=$((failures + 1))
  fi

  if grep -n "androidx\.activity" "$file" >/tmp/adaptivekt_overlay_guard.$$; then
    sed "s#^#[fail] $file:#" /tmp/adaptivekt_overlay_guard.$$
    failures=$((failures + 1))
  fi

  if [[ "$file" == examples/* ]] && grep -nE "BoxWithConstraints|breakpointForWidth" "$file" >/tmp/adaptivekt_overlay_guard.$$; then
    sed "s#^#[fail] $file:#" /tmp/adaptivekt_overlay_guard.$$
    failures=$((failures + 1))
  fi

  if [[ "$file" == examples/* || "$file" == docs-site/* ]] && grep -nE "androidx\.compose\.material3\.(Dialog|AlertDialog)" "$file" >/tmp/adaptivekt_overlay_guard.$$; then
    sed "s#^#[fail] $file:#" /tmp/adaptivekt_overlay_guard.$$
    failures=$((failures + 1))
  fi

  if [[ "$file" == adaptive-components/* && "$file" =~ (Dialog|Dropdown|Select|Tooltip) ]] && grep -nE "Color\.(Black|White|Gray)" "$file" >/tmp/adaptivekt_overlay_guard.$$; then
    sed "s#^#[fail] $file:#" /tmp/adaptivekt_overlay_guard.$$
    failures=$((failures + 1))
  fi

  if grep -nE "\b(Text|Row|Column|Box|Spacer)[[:space:]]*\(" "$file" >/tmp/adaptivekt_overlay_guard.$$; then
    warnings=$((warnings + $(wc -l < /tmp/adaptivekt_overlay_guard.$$)))
  fi
done < <(find "${roots[@]}" -type f -name '*.kt' -print0 2>/dev/null)

rm -f /tmp/adaptivekt_overlay_guard.$$

echo "Warnings: $warnings"

if [[ "$failures" -gt 0 ]]; then
  echo "Overlay and selection guard failed."
  exit 1
fi

echo "Overlay and selection guard passed."

