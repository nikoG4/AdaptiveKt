# AdaptiveTheme

`AdaptiveTheme` is the theme foundation for AdaptiveKt. It lives in `:adaptive-core` so every module can read the same tokens without creating dependency cycles.

T1 introduced the API and migrated the safest base components. T2 migrated the remaining shared modules. T3 adds the default dark color scheme and demo toggles. Platform presets and broad visual redesign remain out of scope.

## API

```kotlin
AdaptiveTheme {
    App()
}
```

```kotlin
val customColors = AdaptiveColorSchemes.defaultLight().copy(
    primary = Color(0xFF0F766E),
    primarySubtle = Color(0xFFCCFBF1),
    primaryText = Color(0xFF134E4A),
)

AdaptiveTheme(colorScheme = customColors) {
    App()
}
```

```kotlin
AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultDark()) {
    App()
}
```

The current theme is exposed through `AdaptiveTheme.colors`, `AdaptiveTheme.shapes`, `AdaptiveTheme.typography`, and `AdaptiveTheme.states`.

## Color Scheme

`AdaptiveColorScheme` includes app/surface backgrounds, borders, text colors, primary accent colors, semantic tones, disabled state, focus ring, and overlay.

`AdaptiveColorSchemes.defaultLight()` approximates the visual defaults that existed before T1.

`AdaptiveColorSchemes.defaultDark()` provides the first production dark palette for the same token surface. It can be passed directly to `AdaptiveTheme(colorScheme = ...)`.

## Shapes, Typography, And States

`AdaptiveShapeScheme` includes `small`, `medium`, `large`, `pill`, and `circle`.

`AdaptiveTypography` includes `title`, `subtitle`, `body`, `bodySmall`, `label`, and `caption`.

`AdaptiveStateScheme` includes hover, pressed, selected, disabled, and focus-border tokens.

## Components Migrated In T1

- `AdaptiveButton`
- `AdaptiveIconButton`
- `AdaptiveBadge`
- `AdaptiveChip`
- `AdaptiveCard`
- `AdaptiveSurface`
- `AdaptiveTextField`
- `AdaptiveSearchField`
- `AdaptiveMenuItem`
- `AdaptiveSelect`
- `AdaptiveMultiSelect`
- `AdaptiveDropdownMenu`
- `AdaptiveDivider`
- `AdaptiveSectionHeader`

`AdaptiveAvatar` and `AdaptiveThumbnail` still keep some local tone generation because their placeholder palettes are content/media-specific.

## Modules Migrated In T2

T2 extends theme consumption beyond base components:

- `:adaptive-feedback`
  - Empty, loading, and error state glyph colors now use semantic theme tokens.
  - Feedback text now reads primary/muted text tokens.
  - Default feedback icon shape uses `AdaptiveTheme.shapes.large`.
- `:adaptive-forms`
  - Section titles, descriptions, labels, required markers, validation messages, and compact sticky action background use theme tokens.
  - Typography uses `AdaptiveTheme.typography` with local size/weight overrides where needed.
- `:adaptive-data`
  - Table header/background, metadata text, status badge shell, overflow menu shell, and overflow icon tint use theme tokens.
  - Card/table behavior and public data APIs are unchanged.
- `:adaptive-navigation`
  - Sidebar, drawer, rail, bottom navigation, top bars, selected item state, glyph state, drawer overlay, borders, and text colors use theme tokens.
  - Navigation mode behavior, widths, breakpoints, and public APIs are unchanged.

## Dark Mode In T3

T3 adds:

- `AdaptiveColorSchemes.defaultDark()`.
- A light/dark toggle in `:docs-site`.
- A light/dark toggle in `:admin-demo`.
- Web query parameter support for deterministic captures:
  - docs-site: `/?theme=dark`, `/components/?theme=dark`
  - admin-demo: `/?screen=dashboard&theme=dark`
- Desktop capture support via `--theme dark` on the admin-demo capture entry point.

The dark palette uses the same `AdaptiveColorScheme` fields as light mode. Component implementations should keep reading `AdaptiveTheme.colors` rather than branching on theme mode.

## Out Of Scope

- Platform presets.
- A full text component or typography migration.
- Brand preset APIs.

## Next Steps

T4 can evaluate platform and brand presets. Additional visual baseline automation can follow once the light/dark Pages deployment is stable.
