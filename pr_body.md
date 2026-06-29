## Summary
This draft PR initiates the LIB-3 campaign to bring Production Data Workflows to AdaptiveKt. Phase 1 introduces the anchored interaction foundation, adding purely resolved popup logic and new components capable of supporting complex overlays (like Async Selects and context menus).

## Motivation
To decouple dropdown menus from integrated selection state, allowing independent data views, infinite scrolling options, and complex anchoring logic.

## Public API additions
- `AdaptiveAnchoredMenu`
- `AdaptiveAnchoredMenuPolicy`
- `AdaptiveOptionNavigationState`
- `resolveAdaptiveMenuPlacement()` pure reducer.

## Anchored interaction foundation
- Custom popup positioning provider.
- Comprehensive boundary checks and alignment calculations.

## Select evolution
*(Pending Phase 2)*

## DataView v2
*(Pending Phase 3)*

## Selection
*(Pending Phase 3)*

## Bulk actions
*(Pending Phase 3)*

## Sorting
*(Pending Phase 3)*

## Pagination
*(Pending Phase 4)*

## Keyboard interaction
- Added `resolveOptionNavigation()` for arrow key traversal with disabled key skipping.

## Tests
- `AdaptiveMenuPlacementTest` added for edge-case bounds checking.
- `AdaptiveOptionNavigationStateTest` added.

## Compatibility
Fully backwards compatible; existing `AdaptiveDropdownMenu` will be deprecated but maintained until migration completes.

## Remaining work
- Evolve `AdaptiveSelect` to consume the new anchored menu.
- Introduce `rowKey` and `AdaptiveDataSelectionState`.
- Snapshot and Pagination models.
