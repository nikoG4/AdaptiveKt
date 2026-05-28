# Tokens Inventory

PR C6 records the current `AdaptiveTokens` surface and likely gaps for future theming work. No tokens were added or changed in this PR.

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

## Missing Token Families

Recommended future additions:

- `AdaptiveColorScheme`
  - surface, surface subtle, border, text, muted text, primary, semantic tones
- `AdaptiveTypography`
  - title, section title, body, caption, label, table header, button
- `AdaptiveElevation`
  - flat, raised, floating/overlay if needed
- Component state tokens
  - hover, pressed, selected, disabled, focus
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

Keep future token work separate from migration PRs. The current hardcoded colors are stable enough for v0.1 demos, but dark mode and branding require a real color-scheme API before broad replacement.
