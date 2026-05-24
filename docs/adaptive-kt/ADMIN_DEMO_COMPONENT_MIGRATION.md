# Admin Demo Component Migration

## PR C1 - Duplicate Primitives to :adaptive-components

Status: Completed

Objective:
- Start dogfooding `:adaptive-components` in real `admin-demo` screens.
- Replace duplicated demo-only primitives where there is a direct shared component.
- Keep the migration controlled and avoid changing `adaptive-data`, `adaptive-navigation`, `adaptive-forms`, or `adaptive-feedback`.

## Helpers Found

| Demo helper | File | Shared equivalent | PR C1 decision |
|---|---|---|---|
| `DemoButton` | `AdminDemoUi.kt` | `AdaptiveButton` | Migrated and removed |
| `DemoButtonVariant` | `AdminDemoUi.kt` | `AdaptiveButtonVariant` | Migrated and removed |
| `DemoBadge` | `AdminDemoUi.kt` | `AdaptiveBadge` | Kept as status-tone adapter using `AdaptiveBadge` |
| `DemoAvatar` | `AdminDemoUi.kt` | `AdaptiveAvatar` | Migrated and removed |
| `DemoThumbnail` | `AdminDemoUi.kt` | none yet | Kept for product thumbnails |
| `DemoCard` | `AdminDemoUi.kt` | `AdaptiveCard` | Kept as KPI composite backed by `AdaptiveCard` |
| `DemoPanel` | `AdminDemoUi.kt` | `AdaptiveCard` / `AdaptiveSurface` | Kept as panel composite backed by `AdaptiveCard` |
| `DemoSectionTitle` | `AdminDemoUi.kt` | `AdaptiveSectionHeader` | Migrated and removed |
| `DemoTextField` | `AdminDemoUi.kt` | `AdaptiveTextField` / `AdaptiveSearchField` | Migrated and removed |
| `AccountMenuItem` | `AdminDemoUi.kt` | `AdaptiveMenuItem` | Migrated and removed |
| `InfoRow` | `AdminDemoUi.kt` | none needed | Removed, unused |
| `DemoToggleChip` | `AdminDemoUi.kt` | none yet | Kept for invoice state toggles |
| `DemoStatusText` | `AdminDemoUi.kt` | partial | Kept as plain data-cell text helper to avoid nested badges inside `AdaptiveDataView` |

## Screens Affected

- Dashboard: section header and KPI/panel containers now use shared components indirectly.
- Employees: avatar column, search field, and toolbar action use shared components.
- Products: toolbar action uses shared button; product thumbnails remain demo-specific.
- Invoices: refresh action uses shared button; state toggle chips remain demo-specific.
- Settings: text fields and actions use shared components.
- Topbar account menu: user avatar and menu items use shared components.

## Component Corrections

- `AdaptiveSearchField` was adjusted after dogfooding because the old default rendered a textual "Search" pseudo-icon before the placeholder. In real screens this produced duplicated copy such as "Search Search employees". The field now relies on the placeholder and clear affordance without duplicating text.

## Helpers Kept

`DemoThumbnail` remains because product thumbnails are not the same primitive as an avatar. A future `AdaptiveThumbnail` or media component can cover this.

`DemoCard` and `DemoPanel` remain as demo-specific composites, but their visual container now comes from `AdaptiveCard`.

`DemoStatusText` remains as text-only cell content because `AdaptiveDataView` already owns table/card status presentation. Replacing it with a badge everywhere would risk nested pills in data surfaces.

`DemoToggleChip` remains because `:adaptive-components` does not yet expose a segmented control or chip primitive.

## Captures

PR C1 regenerated the main admin-demo capture matrix and the component-only capture matrix.

Expected outputs:
- `build/visual-captures`
- `build/adaptivekt-admin-demo-visual-captures.zip`
- `build/visual-captures-components`
- `build/adaptivekt-components-showcase-captures.zip`

## Risks Remaining

- Product thumbnails still use a demo helper until a shared media/thumbnail primitive exists.
- `DemoCard` and `DemoPanel` are still demo composites; this is intentional to avoid a broad redesign.
- `adaptive-data`, `adaptive-navigation`, `adaptive-forms`, and `adaptive-feedback` have not been migrated yet.

## Recommended Next PR

PR C2 should migrate the lowest-risk shared primitives into `adaptive-data`: action buttons, badges, card surface, and overflow/menu treatment. Keep data behavior unchanged.
