# Color Token Audit

PR C6 records hardcoded color usage as preparation for a future theme/dark-mode foundation. No colors were changed in this PR.

## Summary By Module

### `:adaptive-components`

Primary color ownership currently lives in `AdaptiveComponentDefaults` and component-local state functions.

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

- This is the closest thing to a color scheme, but it is not yet a theme API.
- Button, badge, avatar, text field, and icon colors should eventually read from a shared `AdaptiveColorScheme`.

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

- DataView still owns table-specific colors because there are no table tokens yet.
- Status badges are local and should eventually align with shared tone tokens.

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

- Navigation colors are strongly tied to admin-shell identity.
- Future dark mode needs dedicated navigation tokens rather than generic card colors.

### `:adaptive-forms`

Categories found:

- Compact sticky-action background
- Section title and description text
- Label text and required marker
- Validation message colors: error, warning, info

Risk:

- Validation colors should become shared semantic tokens.
- Form label/description text should use typography/color tokens.

### `:adaptive-feedback`

Categories found:

- Empty/error icon backgrounds and tints
- Loading indicator border/fill
- Description/muted text
- State panel now uses `AdaptiveSurface`, but inner glyph colors remain local

Risk:

- Feedback visual variants need semantic status colors.
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

Recommended color-token groups for a later theme PR:

- Surface/background: app, panel, subtle, raised
- Border: default, strong, focus
- Text: primary, secondary, muted, disabled, inverse
- Accent: primary, primary hover, primary pressed
- Semantic: success, warning, danger, info
- State: hover, pressed, selected, disabled, focus ring
- Component families: table, form, navigation, feedback, media/avatar

## Scope Note

This audit is intentionally descriptive. PR C6 does not introduce `AdaptiveTheme`, `AdaptiveColorScheme`, dark mode, or any visual color migration.
