# Light/Dark Component Audit

Date: 2026-06-10

## Summary

The current component library is token-first. Most component colors are resolved from `AdaptiveTheme.colors`, `AdaptiveComponentDefaults`, or `AdaptiveColorScheme`.

## Findings

| Component | Light Result | Dark Result | Issue Found | Fix Applied |
|---|---|---|---|---|
| `AdaptiveButton` | Good contrast across variants | Good contrast across variants | Cursor missing | Added shared cursor |
| `AdaptiveIconButton` | Good hover border/surface | Good hover border/surface | Cursor missing | Added shared cursor |
| `AdaptiveCard` | Surface/border readable | Surface/border readable | Clickable cards had no cursor | Added cursor for `onClick` branch |
| `AdaptiveBadge` | Tone tokens readable | Tone tokens readable | Static component, no cursor needed | None |
| `AdaptiveChip` | Tone/selected states readable | Tone/selected states readable | Clickable chips had no cursor | Added cursor |
| `AdaptiveSelect` | Border/open/error states readable | Border/open/error states readable | Cursor missing on trigger/options | Added cursor |
| `AdaptiveMultiSelect` | Chips/options readable | Chips/options readable | Cursor missing on trigger/options/chips | Added cursor |
| `AdaptiveTabs` | Selected state clear | Selected state clear | Cursor missing | Added cursor |
| `AdaptiveAccordion` | Surface/border readable | Surface/border readable | Header cursor missing | Added cursor |
| `AdaptiveBreadcrumbs` | Current/action colors readable | Current/action colors readable | Action cursor missing | Added cursor |
| `AdaptiveCarousel` | Controls and indicators readable | Controls and indicators readable | Indicator cursor missing | Added cursor |
| `AdaptiveNavigationScaffold` | Selected/hover states readable | Selected/hover states readable | Nav cursor missing | Added cursor |
| `AdaptiveNavigationTree` | Disabled/selected states readable | Disabled/selected states readable | Cursor missing on enabled rows | Added cursor |
| `AdaptiveDataView` | Table/card surfaces readable | Table/card surfaces readable | Clickable table rows had no cursor | Added cursor |
| `AdaptiveThumbnail` | Label contrast calculated | Label contrast calculated | Named `Color.White` outside token scheme | Replaced with explicit contrast value |

## Guard Coverage

`scripts/check-component-interaction-guards.ps1` fails on:
- `pointerHoverIcon` outside the central helper.
- `MaterialTheme.colorScheme` in library components.
- named hardcoded colors such as `Color.White`, `Color.Black`, `Color.Gray`, `Color.Red`, `Color.Green` outside the color scheme definition.
- `clickable` in common library code without a nearby `adaptiveInteractiveCursor`.

