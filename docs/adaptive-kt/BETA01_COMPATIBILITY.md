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
| AdaptiveCard | Flat framed surface | Low elevation by default; pass elevation = AdaptiveElevationLevel.None for flat style |
| AdaptiveSurface | Flat frame | Still flat by default; optional elevation |
| AdaptiveTheme | Colors, shapes, typography, states | Adds optional elevations and motion; trailing lambda remains |
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
