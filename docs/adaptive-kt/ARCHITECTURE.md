# AdaptiveKt — Architecture

## Overview
AdaptiveKt is a modular Compose Multiplatform library for adaptive enterprise UI scaffolding.

v0.1-alpha is intentionally limited to a core adaptive foundation and the most essential adaptive surfaces:

- `adaptive-core`
- `adaptive-layout`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`
- `adaptive-feedback`
- `admin-demo`

## Modules in v0.1-alpha

1. `adaptive-core`
   - adaptive breakpoint model,
   - window size representation,
   - `AdaptiveScope` and helpers,
   - design tokens for spacing, width, radius, and pane widths.

2. `adaptive-layout`
   - `AdaptiveContainer`, `AdaptiveGrid`, `AdaptiveGridScope.item`.

3. `adaptive-navigation`
   - `AdaptiveNavigationScaffold`, `AdaptiveNavItem`, `Sidebar`, `Drawer`, `BottomNavigation`, `NavigationRail`.

4. `adaptive-forms`
   - `AdaptiveFormLayout`, `FormSection`, `FormField`, `FormActions`.

5. `adaptive-data`
   - `AdaptiveDataView`, `AdaptiveDataColumn`, `AdaptiveDataState`.

6. `adaptive-feedback`
   - `AdaptiveEmptyState`, `AdaptiveLoadingState`, `AdaptiveErrorState`.

## Adaptive state model

v0.1-alpha uses a single size taxonomy across the public API:

- `AdaptiveBreakpoint.Compact`
- `AdaptiveBreakpoint.Medium`
- `AdaptiveBreakpoint.Expanded`
- `AdaptiveBreakpoint.Large`

The core contract includes:

- `AdaptiveWindowSize` — width and height in Dp.
- `AdaptiveBreakpoint` — current breakpoint category.
- `AdaptiveInfo` — current window size and breakpoint.
- `AdaptiveScope` — composable receiver exposing adaptive helpers.
- `rememberAdaptiveInfo()` — remembers current adaptive info.
- `adaptiveValue(...)` — selects values by breakpoint.
- `AdaptiveVisibility` — shows content only on selected breakpoints.

## AdaptiveContent and scope

`AdaptiveContent` is the top-level adaptive context provider. For v0.1-alpha it uses `BoxWithConstraints` in commonMain to compute available width and height, then maps width to `AdaptiveBreakpoint` using `breakpointForWidth(width)`.

Layout and navigation components use `AdaptiveScope` to make width-driven decisions without requiring the app to manage breakpoint state manually.

## Layout primitives

- `AdaptiveContainer` provides a responsive page shell with max-width constraints.
- `AdaptiveGrid` with `AdaptiveGridScope.item(...)` provides a 12-column style responsive layout for v0.1-alpha.

The grid API is intentionally simple: developers specify `span` values on `item(...)` rather than using string classes.

## Navigation patterns

`AdaptiveNavigationScaffold` maps breakpoints to structural navigation modes:

- `Compact` -> drawer + optional bottom navigation,
- `Medium` -> drawer or rail with top bar,
- `Expanded` -> sidebar + top bar,
- `Large` -> sidebar + top bar + wide content.

The scaffold does not manage routing. It only renders navigation chrome and emits `onItemSelected` callbacks.

## Form layout contract

`AdaptiveFormLayout` is a responsive form shell that supports:

- one-column mobile forms in `Compact`,
- two-column forms in `Medium`,
- three-column forms in `Expanded` and `Large`,
- explicit `FieldSpan` control for field placement.

Labels can adapt between `Top` and `Inline` positions based on breakpoint and developer settings.

## Data view contract

`AdaptiveDataView` drives a shared data presentation flow with two render modes:

- desktop: table-like presentation,
- mobile: card list presentation.

For v0.1-alpha, the component exposes slots for `cardContent`, `rowActions`, `filterSlot`, and `actions`, while `AdaptiveDataState` governs loading, empty, and error states.

## Multi-platform window measurement strategy

v0.1-alpha defines a strategy for concrete platform measurement without implementing platform-specific code yet.

- Android: derive width/height from `LocalConfiguration.current` or platform window metrics.
- Desktop: use host window bounds or `WindowState`/`ComposeWindow` size.
- Web/WASM: use browser viewport size via JS interop (`window.innerWidth`, `window.innerHeight`) or a Compose DOM measurement helper.

### Limitations

- Web/WASM may require manual resize event wiring for some browsers.
- Desktop window systems may expose different size events depending on the host window toolkit.
- Mobile orientation changes must be reflected through `rememberAdaptiveInfo()` updates.

## Demo integration

The `admin-demo` app is the reference implementation for v0.1-alpha. It should demonstrate:

- desktop: sidebar, top bar, table lists, 2-column forms,
- mobile: drawer/hamburger, card lists, 1-column forms,
- `AdaptiveDataView`, `AdaptiveFormLayout`, and feedback states.
