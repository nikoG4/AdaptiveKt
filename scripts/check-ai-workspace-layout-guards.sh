#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SOURCE_DIR="${1:-examples/ai-workspace-demo/src/commonMain/kotlin}"
SOURCE_PATH="$ROOT/$SOURCE_DIR"

if [[ ! -d "$SOURCE_PATH" ]]; then
  echo "AI Workspace source directory not found: $SOURCE_PATH" >&2
  exit 1
fi

declare -a HARD_NAMES=(
  "BoxWithConstraints"
  "breakpointForWidth"
  "BoxWithConstraints import"
  "Material Button import"
  "Material Card import"
  "Material TextField import"
  "Material OutlinedTextField import"
  "Material wildcard import"
  "User-facing Button"
  "User-facing Card"
  "User-facing TextField"
  "User-facing OutlinedTextField"
)

declare -a HARD_PATTERNS=(
  "BoxWithConstraints"
  "breakpointForWidth"
  "import[[:space:]]+androidx\\.compose\\.foundation\\.layout\\.BoxWithConstraints"
  "import[[:space:]]+androidx\\.compose\\.material3\\.Button"
  "import[[:space:]]+androidx\\.compose\\.material3\\.Card"
  "import[[:space:]]+androidx\\.compose\\.material3\\.TextField"
  "import[[:space:]]+androidx\\.compose\\.material3\\.OutlinedTextField"
  "import[[:space:]]+androidx\\.compose\\.material3\\.\\*"
  "\\bButton[[:space:]]*\\("
  "\\bCard[[:space:]]*\\("
  "\\bTextField[[:space:]]*\\("
  "\\bOutlinedTextField[[:space:]]*\\("
)

declare -a WARNING_PATTERNS=(
  "LocalAdaptiveLayoutInfo.current"
  "AdaptiveGrid[[:space:]]*\\([[:space:]]*columns[[:space:]]*="
  "\\bRow[[:space:]]*\\("
  "\\bColumn[[:space:]]*\\("
  "\\bBox[[:space:]]*\\("
  "\\bSpacer[[:space:]]*\\("
  "\\bText[[:space:]]*\\("
  "\\bLazyColumn[[:space:]]*\\("
  "\\bLazyRow[[:space:]]*\\("
)

failed=0
echo "AI Workspace layout guard: $SOURCE_PATH"
echo
echo "Hard-banned patterns:"

for i in "${!HARD_PATTERNS[@]}"; do
  pattern="${HARD_PATTERNS[$i]}"
  name="${HARD_NAMES[$i]}"
  matches="$(grep -RInE --include='*.kt' "$pattern" "$SOURCE_PATH" || true)"
  if [[ -n "$matches" ]]; then
    count="$(printf '%s\n' "$matches" | wc -l | tr -d ' ')"
    echo "[FAIL] $name: $count"
    printf '%s\n' "$matches"
    failed=1
  else
    echo "[OK] $name: 0"
  fi
done

echo
echo "Warning-only usage counts:"
for pattern in "${WARNING_PATTERNS[@]}"; do
  count="$(grep -RInE --include='*.kt' "$pattern" "$SOURCE_PATH" 2>/dev/null | wc -l | tr -d ' ')"
  echo "$pattern: $count"
done

if [[ "$failed" -ne 0 ]]; then
  echo "AI Workspace layout guard failed." >&2
  exit 1
fi

echo
echo "AI Workspace layout guard passed."

