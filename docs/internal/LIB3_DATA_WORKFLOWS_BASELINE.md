# LIB3: Production Data Workflows Baseline

## Base Information
- **Base SHA:** 949826964d822c092303e58f71578640ac79a74f
- **Target Modules:** `adaptive-components`, `adaptive-data`, `adaptive-layout`, `adaptive-navigation`, `adaptive-core`

## Public API Overview (Current State)
- `AdaptiveDropdownMenu`: A simple unanchored or basically anchored popup.
- `AdaptiveSelect` / `AdaptiveMultiSelect`: Present but lack virtualization, deterministic option identity, and asynchronous loading.
- `AdaptiveDataView`: Displays tabular data, but pagination is simple, lacks granular row identity, bulk actions, expandable rows, and advanced remote states.

## Confirmed Problems & Technical Debt
- **Dropdown Placement:** No sophisticated `AdaptiveAnchoredMenu` that adjusts offset, width matching, or viewport collision detection via pure functions.
- **Selects Scaling:** Render all options at once (O(N) layout). Fails for large datasets. Hard to integrate with server-backed data (async load/retry).
- **DataView Capabilities:** Misses fundamental data-grid capabilities (sorting, column toggling, bulk actions, precise row keys).
- **Mutable Selection:** State often locked within components rather than being completely hoisted and pure.

## Compatibility Risks
- We must preserve existing overloads for `AdaptiveSelect`, `AdaptiveDataView`, `AdaptiveDataColumn`, etc.
- Any breaking change requires the old signature with `@Deprecated` and `ReplaceWith`.
- Package structures must remain stable.

## Proposed Design & New APIs
1. **Anchored Interaction Foundation:**
   - `AdaptiveAnchoredMenu`, `AdaptiveMenuPlacement`, `AdaptiveAnchoredMenuPolicy`.
   - `resolveAdaptiveMenuPlacement()` (pure function).
   - `AdaptiveOptionNavigationState` for keyboard/focus navigation.
   - Virtualized menu content via LazyColumn.
2. **Select Evolution:**
   - Migrate `AdaptiveSelect`/`MultiSelect` to `AdaptiveAnchoredMenu`.
   - Add `optionKey: (T) -> Any` for stable identity.
   - Introduce `AdaptiveAsyncSelect` with `AdaptiveOptionsState` (Idle, Loading, Content, Error).
3. **DataView v2 (Additive):**
   - Introduces `rowKey` and `AdaptiveDataSelectionState`.
   - Adds `AdaptiveBulkAction` and evolves `AdaptiveDataAction`.
   - Adds sortable fields to `AdaptiveDataColumn` and `AdaptiveColumnState`.
   - Expanded rows `expandedKeys`.
   - Introduces `AdaptiveDataSnapshot` for remote states (initialLoading, refreshing, loadingMore, error).
   - Adds offset and cursor pagination via `AdaptivePaginationMode`.
   - Adds keyboard navigation pure state `AdaptiveDataFocusState`.

## APIs that must not break
- `AdaptiveSelect` current signature.
- `AdaptiveDataView` current signature.
- Module dependency boundaries (e.g. `adaptive-components` must not depend on `adaptive-data`).
