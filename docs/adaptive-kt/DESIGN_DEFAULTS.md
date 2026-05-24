# AdaptiveKt Design Defaults

## Principle

Default simple code should produce a good UI.

PR8 moved AdaptiveKt from technical demo styling toward professional admin defaults inspired by AdminLTE, AdminKit, and CoreUI. PR9 polishes interaction states, media behavior, topbar account chrome, and table/card hierarchy while keeping the library Foundation-only and Compose Multiplatform friendly.

## Tokens

Core tokens define shared admin measurements:

- `AdaptiveTokens.Sizes.TopBarHeight = 56.dp`
- `AdaptiveTokens.Sizes.NavItemHeight = 44.dp`
- `AdaptiveTokens.Sizes.ButtonHeight = 44.dp`
- `AdaptiveTokens.Sizes.IconBox = 32.dp`
- `AdaptiveTokens.Sizes.TableRowMinHeight = 48.dp`
- `AdaptiveTokens.Sizes.CardMinHeight = 72.dp`
- `AdaptiveTokens.Radius.Pill = 999.dp`

These are used by navigation, data actions, badges, form controls in the demo, and feedback states to avoid repeated magic numbers.

## Navigation Defaults

Navigation surfaces now default to admin-style chrome:

- Sidebar has a quiet app header, pale background, rounded items, and clear selected state.
- Rail uses fallback glyphs and short labels with one-line clipping instead of broken wrapping.
- Compact topbar uses a glyph menu button plus the supplied title slot.
- Drawer uses a real white panel with border and a dim dismiss layer.
- Bottom navigation uses glyph plus compact label.

When an `AdaptiveNavItem` has no icon, AdaptiveKt generates a fallback glyph from the first letter of the label.

## Data Defaults

`AdaptiveDataView` is the strongest default component:

- `cardContent` is optional. When omitted, mobile cards are generated from columns.
- Mobile cards show media when present, title, subtitle, status, up to three metadata fields, one primary action, and overflow actions.
- Desktop tables render as one table surface with subtle header background, row separators, consistent min height, and action cells.
- Status columns are inferred by id/header containing `status`, `state`, or `estado`.
- Media columns are inferred by id/header containing `avatar`, `image`, `photo`, `thumb`, `thumbnail`, `logo`, or `media`.
- Long table values get more predictable width from a custom proportional layout.
- Desktop row actions use quiet secondary buttons by default to reduce repeated blue pills in dense tables.
- Mobile cards keep a single primary action visible and place secondary/overflow actions behind the overflow control.

Public additions:

- `AdaptiveDataMobileRole`
- `AdaptiveActionPriority`
- `AdaptiveDataAction<T>`
- `AdaptiveDataColumn.mobileRole`
- `AdaptiveDataColumn.mobilePriority`
- `AdaptiveDataColumn.showInMobileCard`
- `AdaptiveDataView.rowActions`
- `AdaptiveDataView.cardContent: (@Composable (T) -> Unit)? = null`

Default mobile role heuristic:

- Media-looking column -> `Media`
- First non-media column -> `Title`
- Second non-media column -> `Subtitle`
- `status/state/estado` column -> `Status`
- Next 2 or 3 columns -> `Metadata`
- Remaining columns -> `Hidden`

## Button Interaction Defaults

Admin buttons must keep their shape across all interaction states:

- Hover and pressed backgrounds are drawn inside the clipped rounded shape.
- Buttons use subtle state colors and border changes instead of rectangular overlays.
- Focus and hover avoid parent-box backgrounds.
- Overflow buttons use the same shape-safe interaction model.

The intended modifier order is shape-first: clip, background, border, hover/click handling, then padding/content.

## Media and Avatar Defaults

Adaptive data media should survive the table-to-card transition:

- Desktop tables can place avatar or thumbnail columns at the start of a row.
- Generated mobile cards move media into the card header beside the title group.
- Employee-like rows can use circular/initial avatars.
- Product-like rows can use rounded thumbnails or neutral placeholders.
- If no media column is supplied, cards simply omit the media area.

## Forms Defaults

`AdaptiveFormLayout` now has a default max width:

- `maxWidth: Dp = AdaptiveTokens.Widths.Form`

This prevents settings-style forms from stretching across large admin screens. Compact fields remain one column; desktop fields can use 2 or 3 columns through `AdaptiveFormColumns`. Compact actions render inline when short enough, keeping final actions visible in the demo.

## Feedback Defaults

Empty, loading, and error states now use:

- Default glyphs when no icon is supplied.
- Centered layout with max readable width.
- Strong title and muted description.
- A more polished Foundation-only loading indicator.

## Demo Defaults

`admin-demo` intentionally uses the library defaults:

- Data screens rely on generated mobile cards instead of custom `cardContent`.
- Tables use `AdaptiveDataView` desktop defaults.
- Row actions demonstrate primary, secondary, and overflow behavior.
- Employees demonstrate avatar media in table and card layouts.
- Products demonstrate thumbnail media in table and card layouts.
- The topbar demonstrates a user avatar and account dropdown.
- Settings uses `AdaptiveFormLayout` max width.
- Dashboard uses responsive KPI cards: 1 column compact, 2 columns medium/expanded, 4 columns large.

## Constraints Preserved

PR8 and PR9 do not add Web, JS, Wasm, backend, Material 3, Maven publication, v0.2 versioning, or external dependencies.
