# Layout Edge Case Review

This document audits the default configurations of all `AdaptiveKt` primitives to ensure they handle standard edge cases correctly without requiring boilerplate from app developers.

## 1. `AdaptivePage` padding propagation
- **Edge Case:** Wrapping scrollable content in a container with fixed outer padding clips the scrollbar to the padded bounds, making it look broken.
- **Handling:** Correct. `AdaptivePage` passes `PaddingValues` downwards instead of wrapping content in a pre-padded Box. `AdaptiveScrollablePage` specifically applies `.verticalScroll(state).padding(padding)` so that the padding is applied *inside* the scroll viewport.

## 2. `AdaptiveContainer` max widths
- **Edge Case:** Nested adaptive containers restrict max width multiple times, potentially offsetting padding recursively.
- **Handling:** Correct. By default, it defers to `LocalAdaptiveLayoutInfo.current.contentMaxWidth`. If the root App already limited the viewport, the nested container won't accidentally double-pad unless developers inject manual `BoxWithConstraints` padding.

## 3. `AdaptiveSection` empty headers
- **Edge Case:** Rendering a section without a title, subtitle, or trailing actions leaves empty space.
- **Handling:** Correct. All fields are nullable (`String?` / `(@Composable RowScope.() -> Unit)?`). If all are null, the `Row` header simply does not emit, cleanly bypassing the spacing arrangement.

## 4. `AdaptiveActionBar` leading and trailing actions
- **Edge Case:** Showing an action bar with only a search field (leading) or only a primary action (trailing).
- **Handling:** Correct. The modifier weights gracefully push content to the edges or collapse depending on what is non-null. On compact, it stacks the leading and trailing rows vertically.

## 5. `AdaptiveTwoPane` Compact Collisions
- **Edge Case:** A side-by-side pane on a mobile phone squeezes content into illegibility.
- **Handling:** Correct. Controlled by `collapseOnCompact = true` by default. It automatically detects `isCompact` and switches from a weighted `Row` to a vertically spaced `Column`.

## 6. `AdaptiveListDetailScaffold` Selection States
- **Edge Case:** Going from Medium (Two Pane) to Compact (One Pane) when an item is selected.
- **Handling:** Correct. `resolveAdaptiveListDetailMode` ensures `ShowListUntilSelection` maps to `DetailOnly`. This seamlessly transforms the UI into a navigation-stack-like behavior.
- **Edge Case:** Custom detail header on compact screens.
- **Handling:** Correct. `compactDetailHeader` allows complete overrides. If omitted, `showBackButtonOnCompactDetail = true` injects a default back button triggering `onBackToList`.

## Summary
All defaults are sane and intuitively map to expected behaviors across both wide desktop windows and compact mobile screens.
