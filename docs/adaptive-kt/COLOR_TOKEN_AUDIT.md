# Color Token Audit

PR C6 records hardcoded color usage as preparation for a future theme/dark-mode foundation.

T1 update: `AdaptiveTheme` and `AdaptiveColorScheme` now exist in `:adaptive-core`, and base `:adaptive-components` primitives read shared theme tokens where the migration is low risk. Dark mode is still not implemented.

T2 update: `adaptive-feedback`, `adaptive-forms`, `adaptive-data`, and `adaptive-navigation` now consume `AdaptiveTheme` for their internal surfaces, borders, semantic tones, selected states, and text colors.

T3 update: `AdaptiveColorSchemes.defaultDark()` now maps the full shared color scheme. Docs-site and admin-demo can be switched to dark mode through UI toggles and web query parameters for capture automation.

## Summary By Module

### `:adaptive-components`

Primary color ownership now lives in `AdaptiveColorScheme`, with `AdaptiveComponentDefaults` acting as an internal bridge for component defaults.

Categories found:

- Surface/background: white, `SurfaceSubtle`
- Border: `Border`, `BorderStrong`, avatar border
- Text: primary, muted, disabled
- Primary/accent: primary button normal/hover/pressed
- Danger: danger button and destructive menu item states
- Status tones: badge neutral/success/warning/danger/info
- State colors: hover, pressed, disabled, validation error
- Icon tint default: `AdaptiveIcons` default tint

Risk:

- T1 covers the safest shared primitives.
- Avatar/thumbnail placeholder palettes still retain local generated tones.
- Remaining work is mostly module-level migration and specialized component-family tokens.

### `:adaptive-data`

Categories found:

- Surface/background: table/card surface
- Border: data table/card border and row dividers
- Header background: table header row
- Text: primary and muted cell text
- Status/media fallback: generated media pill background/border
- Popup/menu surface: local overflow menu background/border
- Icon tint: overflow affordance

Risk:

- DataView now uses shared surface, border, text, info, and primary tokens for its internal shells.
- Dedicated table tokens may still be useful later for richer table density/hover/selection variants.

### `:adaptive-navigation`

Categories found:

- Navigation surface background
- Item background and selected background
- Item border and selected border
- Glyph background and selected glyph background
- Text and selected text
- Drawer scrim and drawer panel background
- Topbar/content background
- Menu toggle icon tint

Risk:

- Navigation now uses shared surface, border, primary, muted, inverse, and overlay tokens.
- Future dark mode may still need dedicated navigation tokens if generic surface tokens are not expressive enough.

### `:adaptive-forms`

Categories found:

- Compact sticky-action background
- Section title and description text
- Label text and required marker
- Validation message colors: error, warning, info

Risk:

- Validation colors, label text, description text, and compact sticky background now use shared theme tokens.
- More specialized form density/field-group tokens can wait until a future form polish PR.

### `:adaptive-feedback`

Categories found:

- Empty/error icon backgrounds and tints
- Loading indicator border/fill
- Description/muted text
- State panel now uses `AdaptiveSurface`, but inner glyph colors remain local

Risk:

- Feedback visual variants now use shared info/danger/text tokens.
- Error currently uses the Close icon because the minimal icon set has no warning/error icon.

### `:admin-demo`

Categories found:

- Mock employee/product tones
- KPI, status text, and selected toggle-chip colors
- Remote image placeholder backgrounds and borders
- Account menu surface, hover, and borders
- Components showcase example colors
- Icon slot tints in example buttons/fields

Risk:

- Demo colors are useful for examples but should not drive library tokens directly.
- Demo-only media placeholders should become cleaner once a public thumbnail/media primitive exists.

## Future Token Groups

Implemented in T1:

- Surface/background: app, panel, subtle, raised.
- Border: default, strong, focus ring.
- Text: primary, secondary, muted, disabled, inverse.
- Accent: primary plus subtle/text.
- Semantic: success, warning, danger, info.
- State: hover/pressed/selected/disabled alpha, focus border width.

Implemented token groups through T3:

- Surface/background: app, panel, subtle, raised
- Border: default, strong, focus
- Text: primary, secondary, muted, disabled, inverse
- Accent: primary, primary hover, primary pressed
- Semantic: success, warning, danger, info
- State: hover, pressed, selected, disabled, focus ring
- Component families: table, form, navigation, feedback, media/avatar

## Scope Note

T1 introduced theme foundation, T2 migrated shared modules, and T3 introduced the default dark scheme. Platform presets and broad redesign remain out of scope.
