# API Naming and Parameter Consistency Review

A full audit of the new API parameters across `adaptive-core`, `adaptive-layout`, and `adaptive-navigation` was conducted to ensure semantic consistency.

## 1. `maxWidth` vs `contentMaxWidth`
- **Context:** `AdaptiveLayoutPolicy` uses `contentMaxWidth`. `AdaptiveContainer` accepts a `maxWidth` override.
- **Decision:** Kept as is. `contentMaxWidth` is appropriate in a global policy object because it disambiguates from window or device constraints. `maxWidth` is appropriate for a Composable parameter override because it strictly applies to that specific component's boundaries.

## 2. `contentPadding` vs `pagePadding`
- **Context:** `AdaptiveLayoutInfo` uses `pagePadding`. `AdaptivePage` accepts `contentPadding`.
- **Decision:** Kept as is. `pagePadding` implies a global boundary offset inside layout configuration. `contentPadding` is the Compose Multiplatform standard naming convention for internal insets (e.g., `LazyColumn(contentPadding = ...)`), making the override syntax deeply intuitive for Android/Compose developers.

## 3. `listPane` vs `listContent`
- **Context:** `AdaptiveListDetailScaffold` uses `listPane` and `detailPane`.
- **Decision:** Kept as is. The word "Pane" correctly aligns with the standard multi-window and folding-device UI lexicon (e.g. `TwoPaneLayout`, `ListDetailPaneScaffold`). Using `content` would dilute the structural intent.

## 4. `behavior` vs `mode`
- **Context:** `AdaptiveNavigationMode` vs `AdaptiveListDetailBehavior`.
- **Decision:** Kept as is. This forms a distinct and powerful semantic split:
  - A **Mode** (`AdaptiveNavigationMode.Drawer`, `AdaptiveListDetailPaneMode.DetailOnly`) represents a *concrete, physical state* at a given moment.
  - A **Behavior** (`AdaptiveListDetailBehavior`, `AdaptiveListDetailCompactBehavior`) represents a *configuration policy* that maps breakpoints to Modes.

## 5. `paneSpec` vs `paneConfig`
- **Context:** `listPaneSpec` in `AdaptiveListDetailScaffold`.
- **Decision:** Kept as is. `Spec` maps perfectly to Compose structural API conventions like `AdaptivePaneSpec` encapsulating weight, minWidth, and maxWidth. 

## Summary
No parameter renames were required during this audit because the parameters consistently follow established Compose idioms (like `contentPadding`) or cleanly separate policy objects (`Behavior`) from runtime states (`Mode`).
