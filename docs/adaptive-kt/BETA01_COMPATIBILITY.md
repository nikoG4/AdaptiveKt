# Beta01 compatibility policy — proposal

This document describes the intended behavior for **0.2.0-beta01**. It is not
an active stability guarantee until a beta release has been validated and published.

## Contract

- Kotlin Multiplatform components continue to be Compose-native, with Modifier
  for layout/styling and composable slots for richer content.
- New optional arguments should preserve source compatibility for normal named
  arguments and trailing-lambda calls.
- Breaking API adjustments before beta must have migration notes in CHANGELOG.
- During beta, prefer deprecation plus replacement guidance over immediate removal.
- Do not promise binary compatibility between beta builds; document changes.
- Desktop, Android, and Wasm are target platforms; iOS remains subject to
  real macOS/iOS artifact and runtime validation.
- A published beta only covers supported, documented APIs and platforms.

## Migration guidance for the visual foundation branch

Existing component calls continue using their previous signatures in most cases.
The UI defaults are intentionally more polished:

| Change | Old behavior | New behavior / migration |
| --- | --- | --- |
| AdaptiveCard | Flat framed surface | Modern defaults to Low; Classic defaults to None; explicitly pass an elevation to override |
| AdaptiveSurface | Flat frame | Still flat by default; optional elevation |
| AdaptiveTheme | Colors, shapes, typography, states | Adds elevations, motion and Modern/Classic styling; trailing lambda still works |
| Visual defaults | Alpha appearance | Modern is the default; pass `visualStyle = AdaptiveVisualStyle.Classic` to restore it |
| AdaptiveIconButton | Icon-only interaction | Optional contentDescription for accessibility; use for every icon-only action |
| AdaptiveTabs | Clickable UI row | Selectable tab semantics with animated state |
| AdaptiveDialog | Immediate display | Animated entrance; caller still owns dismissal |

Use explicit mode with an explicit colorScheme when offering a theme toggle.
Otherwise the default elevation scheme follows the system-selected mode. Apps
can instead provide their own AdaptiveElevationScheme through AdaptiveTheme.

Motion durations are opt-out via AdaptiveMotionScheme.reduced(). The platform
does not automatically read an OS reduced-motion flag for the application.

## Before any beta tag

Confirm these contracts in real consumers, add migration notes for every future
breaking API change, and run platform verification. Draft PR status is not an
indicator that binary/API compatibility has been tested.

## Preset migration examples

```kotlin
// New premium baseline, light/dark automatically follows the system.
AdaptiveTheme {
    MyApp()
}

// Previous alpha visual defaults without reverting the new APIs.
AdaptiveTheme(visualStyle = AdaptiveVisualStyle.Classic) {
    MyApp()
}

// App-specific branding takes precedence over either preset.
AdaptiveTheme(
    visualStyle = AdaptiveVisualStyle.Modern,
    colorScheme = AdaptiveVisualPresets.modernLight().copy(primary = brandColor),
) {
    MyApp()
}
```

This is a visual default change made **before** the first beta.
The theme parameter types for shapes/typography/states now accept null as an
internal indication to use the selected style defaults. Callers still pass
non-null customization values as before. Platform runtime verification is
still required before claiming a stable compatibility contract.
