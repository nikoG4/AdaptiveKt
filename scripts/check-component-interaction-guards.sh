#!/usr/bin/env bash
set -euo pipefail

roots=(
  adaptive-core
  adaptive-components
  adaptive-layout
  adaptive-navigation
  adaptive-data
  adaptive-forms
  adaptive-feedback
)

failures=0

echo "AdaptiveKt component interaction guard"

while IFS= read -r -d '' file; do
  if [[ "$file" != *"/src/commonMain/kotlin/"* ]]; then
    continue
  fi

  if [[ "$file" != *"AdaptiveInteractionModifiers.kt" ]] && grep -n "pointerHoverIcon" "$file" >/tmp/adaptivekt_guard_matches.$$; then
    sed "s#^#$file:#" /tmp/adaptivekt_guard_matches.$$
    failures=$((failures + 1))
  fi

  if [[ "$file" != *"AdaptiveColorScheme.kt" ]] && grep -nE "Color\.(Black|White|Gray|Red|Green)" "$file" >/tmp/adaptivekt_guard_matches.$$; then
    sed "s#^#$file:#" /tmp/adaptivekt_guard_matches.$$
    failures=$((failures + 1))
  fi

  if grep -n "MaterialTheme\.colorScheme" "$file" >/tmp/adaptivekt_guard_matches.$$; then
    sed "s#^#$file:#" /tmp/adaptivekt_guard_matches.$$
    failures=$((failures + 1))
  fi
done < <(find "${roots[@]}" -type f -name '*.kt' -print0)

rm -f /tmp/adaptivekt_guard_matches.$$

if [[ "$failures" -gt 0 ]]; then
  echo "Component interaction guard failed."
  exit 1
fi

echo "Component interaction guard passed."
