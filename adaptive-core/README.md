# AdaptiveCore

`adaptive-core` is the foundational layer for AdaptiveKt. It defines tokens, breakpoints, structural interfaces, and state objects that back the higher-level layout and component modules.

## Key APIs

- **`AdaptiveApp`**: The root configuration provider. It propagates styling, layout constraints, breakpoints, and colors to all children via `CompositionLocal`.
- **`AdaptiveConfig`**: A data class holding configuration parameters. Allows customized breakpoint overrides or column overrides.
- **`AdaptiveBreakpoints`**: Defines max-width boundaries for screen sizes (`compactMax`, `mediumMax`, `expandedMax`).
- **`AdaptiveLayoutInfo`**: Represents the current adaptive state (e.g. `isCompact`, `isMedium`, `columns`, `pagePadding`).
- **`LocalAdaptiveLayoutInfo`**: The `CompositionLocal` holding the current info.

## Core Components

### AdaptiveApp
`AdaptiveApp` is the root composable that injects the `AdaptiveConfig` and theme tokens into the composition hierarchy.

### AdaptiveConfig
Provides configuration mapping for theme values, breakpoint bounds, and layout behaviors.

### AdaptiveLayoutInfo
Provides runtime window measurements without requiring a full `BoxWithConstraints` recomposition tree.

## Architecture Rule
**Application code should not use `BoxWithConstraints` for layout measurement.**
`AdaptiveKt` measures the layout bounds internally at the `AdaptiveApp` and `AdaptivePage` level. Rely on the `LocalAdaptiveLayoutInfo.current` (internally managed by the layout primitives) to determine the active viewport bucket rather than breaking the Compose subcomposition tree with arbitrary `BoxWithConstraints` calls.

## Usage Rules

> [!WARNING]
> You **should not** use `BoxWithConstraints` to manually calculate breakpoints in your app screens.
> You **should not** manually fetch `LocalAdaptiveLayoutInfo.current` to assign generic padding, spacing, or grid column counts.

Instead, rely on the `AdaptiveContainer`, `AdaptiveGrid`, and `AdaptiveTwoPane` components from `adaptive-layout`. They inherently read from `LocalAdaptiveLayoutInfo`.

### When to use `LocalAdaptiveLayoutInfo.current` Directly

Only use `LocalAdaptiveLayoutInfo.current` when performing a fundamentally different structural paradigm shift. For instance:
- Swapping the navigation drawer implementation.
- Selecting a radically different top-level layout that relies on distinct composables (e.g., hiding a Desktop-only Search Bar on mobile).

### Example Configuration

```kotlin
AdaptiveApp(
    config = AdaptiveConfig(
        breakpoints = AdaptiveBreakpoints(
            compactMax = 640.dp,
            mediumMax = 960.dp,
            expandedMax = 1280.dp
        ),
        layout = AdaptiveLayoutPolicy(
            compactColumns = 4,
            mediumColumns = 8,
            expandedColumns = 12,
            largeColumns = 12,
            contentMaxWidth = 1440.dp
        )
    )
) {
    App()
}
```
