# Visual foundations for beta

This is an incremental Compose-native design foundation, **not** a finished UI overhaul.

## Elevation

AdaptiveTheme.elevations exposes configurable light/dark elevations: None, Low, Medium, High, Floating.
AdaptiveCard defaults to Low (subtle raised card), while AdaptiveSurface stays flat.
Both keep their existing border and shape logic. An explicit None preserves the old flat card.

```kotlin
AdaptiveTheme {
    AdaptiveCard(elevation = AdaptiveElevationLevel.Medium) {
        // content
    }
    AdaptiveSurface(elevation = AdaptiveElevationLevel.None) {
        // content
    }
}
```

Customize AdaptiveElevationScheme through the AdaptiveTheme elevations argument.
Custom color schemes should be paired with appropriate light/dark shadow values.

## Motion

AdaptiveTheme.motion defines fastMillis (120), standardMillis (180), emphasizedMillis (260).
For animation specs, pass durationMillis(token) so explicit reduced motion works.

```kotlin
val tokens = AdaptiveTheme.motion
val spec = tween<Float>(durationMillis = tokens.durationMillis(tokens.standardMillis))
```

AdaptiveMotionScheme.reduced() sets effective durations to zero. The host application is
responsible for detecting reduced-motion preferences. Existing animations are NOT yet
migrated to these tokens, and visuals still need Desktop, Android, and Wasm verification.

## Next

- Migrate hover/press, menus, dialogs, and loading transitions to shared motion tokens.
- Review focus indicators, accessible interaction states, and dark mode contrast.
- Add visual baselines for compact, medium, and expanded layouts.
- Audit API compatibility before the beta tag.
