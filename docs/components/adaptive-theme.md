# AdaptiveTheme

## Purpose

`AdaptiveTheme` provides shared colors, shapes, typography, and state tokens for AdaptiveKt components.

## When To Use

Wrap an app, demo, or documentation surface once near the root:

```kotlin
AdaptiveTheme {
    AdminApp()
}
```

Use a custom color scheme when an app needs brand colors without rewriting component implementations:

```kotlin
AdaptiveTheme(
    colorScheme = AdaptiveColorSchemes.defaultLight().copy(
        primary = Color(0xFF0F766E),
        primarySubtle = Color(0xFFCCFBF1),
        primaryText = Color(0xFF134E4A),
    ),
) {
    AdminApp()
}
```

Use the built-in dark scheme when an app wants a dark UI without defining a custom palette:

```kotlin
AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultDark()) {
    AdminApp()
}
```

## Main API

```kotlin
@Composable
fun AdaptiveTheme(
    colorScheme: AdaptiveColorScheme = AdaptiveColorSchemes.defaultLight(),
    shapes: AdaptiveShapeScheme = AdaptiveShapeScheme.default(),
    typography: AdaptiveTypography = AdaptiveTypography.default(),
    states: AdaptiveStateScheme = AdaptiveStateScheme.default(),
    content: @Composable () -> Unit,
)
```

Current values are available from `AdaptiveTheme.colors`, `AdaptiveTheme.shapes`, `AdaptiveTheme.typography`, and `AdaptiveTheme.states`.

## Responsive Notes

Theme tokens are independent from breakpoint logic. Use them with layout primitives such as `AdaptiveGrid`, `AdaptiveContainer`, and responsive modules.

## Multiplatform Notes

The theme foundation lives in `:adaptive-core` `commonMain` and works on JVM, Android, Wasm, and declared iOS targets.

## Limitations

- `defaultLight()` and `defaultDark()` are available.
- Platform presets are planned for a later PR.
- Some demo-only examples still own illustrative colors that are not library tokens.
