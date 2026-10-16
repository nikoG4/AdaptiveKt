# Visual foundations for beta

This is an incremental Compose-native design foundation, **not** a finished UI overhaul.

## Modern visual preset

The modern preset is the new default in the beta development branch.
It focuses on the illustrated admin and communication mockups:
balanced blue accents, soft but deliberate shadows, white/pale panels,
dark navy alternatives, approachable rounded corners, crisp readable type,
and quick interactions. It remains entirely Compose Multiplatform code.

```kotlin
AdaptiveTheme(visualStyle = AdaptiveVisualStyle.Modern) {
    AdaptiveCard(elevation = AdaptiveElevationLevel.Medium) {
        AdaptiveButton(text = "Continue", onClick = {})
    }
}
```

Use `AdaptiveVisualStyle.Classic` for the previous alpha look, or customize
`colorScheme`, `shapes`, `typography`, `states` and `elevations` without
giving up the rest of the preset. The default semantic palette and the Modern
preset are both preserved as public helpers. The demo now has an explicit
Modern/Classic switch on expanded widths, and the UI Kit renders a comparison
of both presets even on compact screens.

The preset is an incremental style foundation; real screenshots, polish,
motion accessibility and user-testing are still mandatory for beta.

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
responsible for detecting reduced-motion preferences. The first interaction batch now consumes motion tokens in buttons, icon buttons, chips,
checkboxes, tabs, text-field focus, interactive card elevation, accordion expansion, and
dialog entrance. Other components still require an audit. Every platform still needs
real render and interaction verification.

## Next

- Migrate hover/press, menus, dialogs, and loading transitions to shared motion tokens.
- Review focus indicators, accessible interaction states, and dark mode contrast.
- Add visual baselines for compact, medium, and expanded layouts.
- Audit API compatibility before the beta tag.

## Light/dark consistency

When an app overrides the color scheme, pass a matching AdaptiveThemeMode (or provide
an explicit elevations scheme). Do not silently rely on a different system dark mode.
The admin demo now sets both colorScheme and mode when its theme toggle changes.

## Changes in the first interactive component pass

- Primary buttons use distinct idle, hovered and pressed background targets.
- Buttons, icon-only buttons, clickable cards and menu items now expose a Button role
  with visible focus outlines using the semantic focusRing color.
- Icon-only buttons accept an optional contentDescription; use it for every action.
- Tabs advertise selected state through the platform selectable semantics and Role.Tab.
- Clickable chips expose a selected state; decorative chips remain noninteractive.
- Text inputs surface validation errors via semantics as well as visual error styles.
- Accordion reveals content and rotates its chevron with shared motion durations.
- Dropdown and dialog surfaces use theme-controlled High/Floating elevations.
- Dialogs animate entrance; exit animations are not yet implemented because removal
  remains controlled by the calling app.

The admin demo UI Kit's Cards section demonstrates None, Low, Medium and High
elevation levels. Review it in both themes, with keyboard and narrow widths.

## Known limitations

The code has not been tested on all targets. Focus behavior may differ across
Wasm, Android and iOS. Overlay and popup focus traps, reduced-motion OS detection,
locale-specific accessibility text and visual baselines remain beta release gates.

## Review documents

See the [beta compatibility proposal](BETA01_COMPATIBILITY.md) for source
migration rules and the [visual QA matrix](BETA01_VISUAL_QA.md) for screenshots
and manual interaction checks. These documents are more important than a
single attractive mockup: the visual language must remain functional across
touch, keyboard, desktop and mobile.
