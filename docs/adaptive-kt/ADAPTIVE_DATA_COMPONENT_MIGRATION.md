# Adaptive Data Component Migration

## PR C2 - Controlled Migration to :adaptive-components

Status: Completed

Objective:
- Make `:adaptive-data` consume shared primitives from `:adaptive-components`.
- Preserve `AdaptiveDataView` behavior and public API.
- Avoid a DataView v2 redesign.

## Dependency Change

`adaptive-data/build.gradle.kts` now includes:

```kotlin
implementation(project(":adaptive-components"))
```

Allowed dependency graph after PR C2:
- `adaptive-data -> adaptive-core`
- `adaptive-data -> adaptive-layout`
- `adaptive-data -> adaptive-feedback`
- `adaptive-data -> adaptive-components`

No reverse dependency was added.

## Public API

Preserved without changes:
- `AdaptiveDataView`
- `AdaptiveDataColumn`
- `AdaptiveDataAction`
- `AdaptiveActionPriority`
- `AdaptiveDataMobileRole`
- `AdaptiveDataState`
- `shouldUseTableLayout`
- `visibleColumnsForBreakpoint`

## Helpers Found

| Internal helper / area | Previous implementation | Shared equivalent | PR C2 decision | Risk |
|---|---|---|---|---|
| Table surface | Local `Column` with background/border/radius | `AdaptiveCard` | Migrated with zero content padding | Low |
| Mobile card container | Local `Column` with background/border/radius/clickable | `AdaptiveCard` | Migrated | Medium visual risk due shared radius |
| Row action button | Local `DefaultActionButton` custom hover/pressed | `AdaptiveButton` | Migrated through wrapper | Low |
| Overflow trigger | Local pill icon button | `AdaptiveIconButton` | Migrated | Low |
| Overflow menu items | Local clickable text rows | `AdaptiveMenuItem` | Migrated | Low |
| Divider | Local 1dp box | `AdaptiveDivider` | Migrated | Low |
| Status badge wrapper | Custom composable-content pill | `AdaptiveBadge` is text-based | Kept | Medium API/visual risk if forced |
| Anchored overflow popup | `Popup` with measured button position | `AdaptiveDropdownMenu` is inline/simple | Kept | High UX risk if replaced now |
| Toolbar layout | Local simple slots | none | Kept | Low |
| Metadata row | Local label/value row | none | Kept | Low |

## Helpers Migrated

- `AdaptiveDataTable` now uses `AdaptiveCard` as the table surface.
- Generated mobile cards now use `AdaptiveCard` as their container.
- `DefaultActionButton` now delegates to `AdaptiveButton`.
- Overflow trigger now uses `AdaptiveIconButton`.
- Overflow menu rows now use `AdaptiveMenuItem`.
- `DataDivider` now delegates to `AdaptiveDivider`.

## Helpers Kept

`DefaultStatusBadge` remains local because it wraps arbitrary composable cell content. `AdaptiveBadge` accepts text, and using it here would either change cell contracts or risk nested/duplicated badge rendering.

The overflow popup remains local because it is currently anchored to the measured overflow button. `AdaptiveDropdownMenu` is still an inline/simple menu panel, so replacing the popup would degrade table/card overflow behavior.

## Captures

PR C2 regenerated the main admin-demo capture matrix.

Expected outputs:
- `build/visual-captures`
- `build/visual-captures/manifest.json`
- `build/visual-captures/visual-capture-report.md`
- `build/adaptivekt-admin-demo-visual-captures.zip`

## Verification

Commands executed:
- `.\gradlew.bat :adaptive-data:jvmTest`
- `.\gradlew.bat :admin-demo:build`
- `.\gradlew.bat build`
- `.\tools\capture-admin-demo.ps1`

## Risks Remaining

- Status badges remain local until a content-slot badge or text extraction strategy exists.
- Anchored overflow menus should move to a future anchored menu primitive instead of the current inline `AdaptiveDropdownMenu`.
- Table and card styling now inherits `AdaptiveCard` shape/padding behavior; this was visually reviewed through admin-demo captures.

## Recommended Next PR

PR C3 should migrate a low-risk slice of `adaptive-forms` to `:adaptive-components`, especially form action buttons and text-field defaults, while preserving `AdaptiveFormLayout` API.
