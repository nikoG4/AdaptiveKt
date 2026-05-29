# Tokens Inventory

PR C6 records the current `AdaptiveTokens` surface and likely gaps for future theming work.

T1 update: `AdaptiveTheme` now adds color, shape, typography, and state token families in `:adaptive-core`.

T2 update: feedback, forms, data, and navigation internals now consume the shared theme token families.

T3 update: `AdaptiveColorSchemes.defaultDark()` now maps the same color token family for dark mode.

## Current Tokens

### Spacing

```kotlin
XSmall = 4.dp
Small = 8.dp
Medium = 16.dp
Large = 24.dp
XLarge = 32.dp
XXLarge = 48.dp

ExtraSmall = XSmall
ExtraLarge = XLarge
```

### Radius

```kotlin
Small = 8.dp
Medium = 12.dp
Large = 16.dp
XLarge = 24.dp
Pill = 999.dp
```

### Widths

```kotlin
AuthForm = 360.dp
Form = 720.dp
Content = 1000.dp
Page = 1200.dp
Wide = 1400.dp
Card = 320.dp
```

### PaneWidths

```kotlin
NavigationRail = 96.dp
Sidebar = 280.dp
Master = 360.dp
DetailMin = 320.dp
Filter = 320.dp
```

### Sizes

```kotlin
NavItemHeight = 44.dp
ButtonHeight = 44.dp
IconBox = 32.dp
CardMinHeight = 72.dp
TableRowMinHeight = 48.dp
TopBarHeight = 56.dp
```

## Theme Token Families Added In T1

- `AdaptiveColorScheme`
  - surfaces, borders, text, primary, semantic tones, disabled/focus/overlay.
  - Built-in schemes: `defaultLight()` and `defaultDark()`.
- `AdaptiveShapeScheme`
  - small, medium, large, pill, circle.
- `AdaptiveTypography`
  - title, subtitle, body, bodySmall, label, caption.
- `AdaptiveStateScheme`
  - hover, pressed, selected, disabled, focus border width.

## Remaining Token Gaps

Recommended future additions:

- `AdaptiveElevation`
  - flat, raised, floating/overlay if needed
- Table tokens
  - header background, row border, row hover, numeric alignment guidance, density
- Form tokens
  - label color, required marker, validation tones, input background/border/focus
- Navigation tokens
  - sidebar background, selected item, rail item, drawer scrim, topbar
- Feedback tokens
  - empty/loading/error/info panel, glyph backgrounds, state icon colors
- Media/avatar/thumbnail tokens
  - avatar size, thumbnail size, media radius, placeholder backgrounds

## Guidance

Keep future token work incremental. Light and dark schemes are now in place, and shared modules consume them through `AdaptiveTheme`. Branding and platform presets should remain separate follow-up PRs.
