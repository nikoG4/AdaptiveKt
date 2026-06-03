# Public API Inventory

PR C6 inventories the visible API surface after the component migrations. PR P0/P1 adds platform target declarations only; no public API was changed.

## `:adaptive-core`

Stable for v0.1:

- `AdaptiveBreakpoint`
- `breakpointForWidth(width)`
- `AdaptiveInfo`
- `AdaptiveWindowSize`
- `AdaptiveScope`
- `AdaptiveContent`
- `rememberAdaptiveInfo`
- `adaptiveValue`
- `AdaptiveVisibility`
- `AdaptiveTokens`

Candidate future improvement:

- Theme/color/typography tokens should extend, not replace, `AdaptiveTokens` without a deprecation plan.

## `:adaptive-components`

Stable enough for v0.1 dogfooding:

- `AdaptiveButton`
- `AdaptiveButtonVariant`
- `AdaptiveButtonSize`
- `AdaptiveIconButton`
- `AdaptiveBadge`
- `AdaptiveBadgeTone`
- `AdaptiveAvatar`
- `AdaptiveAvatarShape`
- `initialsForName`
- `AdaptiveCard`
- `AdaptiveSurface`
- `AdaptiveDropdownMenu`
- `AdaptiveMenuItem`
- `AdaptiveTextField`
- `AdaptiveSearchField`
- `AdaptiveSectionHeader`
- `AdaptiveDivider`
- `AdaptiveIcons`

Experimental/candidate improvement:

- `AdaptiveDropdownMenu` is currently simple/inline and needs an anchored API before broader DataView migration.
- `AdaptiveIcons` is a minimal functional icon set, not a general-purpose icon pack.
- Colors are component defaults, not a theme system.

## `:adaptive-layout`

Stable for v0.1:

- `AdaptiveContainer`
- `AdaptiveGrid`
- `AdaptiveGridScope`

Candidate future improvement:

- More explicit responsive density and alignment policies may be useful later.

## `:adaptive-data`

Stable for v0.1:

- `AdaptiveDataView`
- `AdaptiveDataColumn`
- `AdaptiveDataAction`
- `AdaptiveActionPriority`
- `AdaptiveDataMobileRole`
- `AdaptiveDataError`
- `AdaptiveDataEmpty`
- `AdaptiveDataContent`
- `shouldUseTableLayout`
- `visibleColumnsForBreakpoint`

Do not break without deprecation:

- Column metadata fields and action behavior.
- Mobile-card heuristics.
- Existing state/action types.

Candidate future improvement:

- DataView v2, anchored overflow menu, pagination, sorting, richer status/media primitives.

## `:adaptive-forms`

Stable for v0.1:

- `AdaptiveFormLayout`
- `AdaptiveFormScope`
- `AdaptiveFormSectionScope`
- `AdaptiveFormActionsScope`
- `AdaptiveFormColumns`
- `LabelPosition`
- `AdaptiveValidationMessage`
- `AdaptiveValidationMessageType`
- `columnsForBreakpoint`
- `resolveFieldSpan`

Do not break without deprecation:

- Slots for fields and actions.
- Responsive column and sticky-action behavior.

Candidate future improvement:

- `AdaptiveSelect`, validation message primitive, field shell primitive.

## `:adaptive-navigation`

Stable for v0.1:

- `AdaptiveNavigationScaffold`
- `AdaptiveNavItem`
- `AdaptiveNavigationMode`
- `AdaptiveNavigationItemStyle`
- `AdaptiveNavigationDensity`
- `navigationModeForBreakpoint`
- `Sidebar`
- `Drawer`
- `BottomNavigation`
- `NavigationRail`

Do not break without deprecation:

- Breakpoints and mode mapping.
- Scaffold slots and selected item behavior.
- Sidebar/rail/drawer widths.

Candidate future improvement:

- Navigation tokens, account/topbar primitives, better anchored menus.

## `:adaptive-feedback`

Stable for v0.1:

- `AdaptiveEmptyState`
- `AdaptiveLoadingState`
- `AdaptiveErrorState`

Do not break without deprecation:

- Action/retry slots stay caller-owned.

Candidate future improvement:

- Feedback variants, semantic state icons, progress primitive.

## API Policy Notes

- Component migrations should preserve existing feature-module public APIs unless a dedicated deprecation PR is planned.
- New reusable primitives should live in `:adaptive-components` only when they are generic enough to avoid feature-module dependency cycles.
- Demo-only helpers and Kamel/Ktor image loading should stay outside library APIs.
- Platform target additions are build configuration changes and should not change source-level API without a dedicated compatibility review.
