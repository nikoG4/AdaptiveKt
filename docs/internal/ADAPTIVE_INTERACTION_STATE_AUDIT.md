# Adaptive Interaction State Audit

Date: 2026-06-10

## Summary

AdaptiveKt now centralizes pointer cursor behavior through `Modifier.adaptiveInteractiveCursor`. Existing hover/pressed/selected/disabled states were reviewed and kept token-based.

| Component | Hover | Pressed | Focus/Open | Disabled | Selected | Fix Applied |
|---|---|---|---|---|---|---|
| `AdaptiveButton` | Token surface/primary variants | Uses pressed state | Future focus ring work | Disabled background/text | n/a | Added cursor |
| `AdaptiveIconButton` | Token surface | Uses pressed state | Future focus ring work | Disabled surface/text via content | n/a | Added cursor |
| `AdaptiveCard` | Static by default | Click handled by branch | n/a | n/a | n/a | Added cursor only for clickable cards |
| `AdaptiveChip` | Tone-based visual | Native clickable press | n/a | Disabled background/text | Selected tones | Added cursor for clickable chips |
| `AdaptiveTabs` | Selected/background tokens | Native clickable press | n/a | No disabled tab API yet | Selected token state | Added cursor |
| `AdaptiveSelect` | Trigger/options use subtle surface | Native clickable press | Open border uses primary | Disabled trigger | Selected option highlight/check | Added cursor |
| `AdaptiveMultiSelect` | Trigger/options use subtle surface | Native clickable press | Open border uses primary | Disabled trigger/chips | Selected option/chip highlight | Added cursor |
| `AdaptiveMenuItem` | Surface/danger subtle hover | Native clickable press | n/a | No disabled menu item API yet | n/a | Added cursor |
| `AdaptiveAccordion` | Header toggles | Native clickable press | Expanded chevron rotation | n/a | Expanded state | Added cursor |
| `AdaptiveBreadcrumbs` | Non-selected action | Native clickable press | n/a | n/a | Current crumb is not clickable | Added cursor |
| `AdaptiveCarousel` | Button controls inherit hover | Button controls inherit pressed | n/a | Disabled controls | Selected indicator | Added cursor to indicators |
| `AdaptiveNavigationScaffold` surfaces | Navigation items use hover | Native clickable press | Drawer open state | Tree rows can disable | Selected nav item | Added cursor |
| `AdaptiveNavigationTree` | Hover surface tokens | Native clickable press | Expanded chevron | Disabled text/no cursor | Selected row | Added cursor |
| `AdaptiveDataView` | Rows inherit row visuals; actions inherit buttons | Native clickable press | Menus use component menu | Actions inherit disabled where supported | n/a | Added cursor to clickable table rows |
| Feedback states | Static body | Action slot controlled by consumer | n/a | n/a | n/a | No cursor on static state body |

## Remaining Work

- Add explicit focus-ring support across button/select/tabs once keyboard navigation policy is formalized.
- Consider disabled menu items as a future API addition.
- Add hover state to clickable cards if product usage demands stronger affordance.

