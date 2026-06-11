# Interactive Cursor Audit

Date: 2026-06-10

Scope:
- `adaptive-core`
- `adaptive-components`
- `adaptive-layout`
- `adaptive-navigation`
- `adaptive-data`
- `adaptive-forms`
- `adaptive-feedback`

## Rule

Interactive AdaptiveKt controls should request the platform hand/pointer cursor on pointer-capable targets. Static content should keep the default cursor. Text input areas should keep the platform text/input cursor.

The shared API is:

```kotlin
Modifier.adaptiveInteractiveCursor(enabled = true)
```

The helper lives in `adaptive-core` so all library modules can consume it without duplicating `pointerHoverIcon` usage.

## Audit Table

| Component | File | Interactive? | Previous cursor | Desired cursor | Fix |
|---|---|---:|---|---|---|
| `AdaptiveButton` | `adaptive-components/.../AdaptiveButton.kt` | Yes when enabled | Default | Hand when enabled | Added `adaptiveInteractiveCursor(enabled)` |
| `AdaptiveIconButton` | `adaptive-components/.../AdaptiveIconButton.kt` | Yes when enabled | Default | Hand when enabled | Added `adaptiveInteractiveCursor(enabled)` |
| `AdaptiveCard` | `adaptive-components/.../AdaptiveCard.kt` | Only with `onClick` | Default | Hand only when clickable | Added cursor in clickable branch |
| `AdaptiveChip` | `adaptive-components/.../AdaptiveChip.kt` | Only with `onClick` and enabled | Default | Hand only when clickable/enabled | Added cursor in clickable branch |
| `AdaptiveTabs` | `adaptive-components/.../AdaptiveTabs.kt` | Yes | Default | Hand on tab items | Added cursor to tab item |
| `AdaptiveSelect` | `adaptive-components/.../AdaptiveSelect.kt` | Trigger/options | Default | Hand for trigger/options when enabled | Added cursor to trigger and options |
| `AdaptiveMultiSelect` | `adaptive-components/.../AdaptiveMultiSelect.kt` | Trigger/options/removable chips | Default | Hand for trigger/options/chip remove when enabled | Added cursor to trigger, options, custom chips |
| `AdaptiveMenuItem` | `adaptive-components/.../AdaptiveMenuItem.kt` | Yes | Default | Hand | Added cursor |
| `AdaptiveAccordion` | `adaptive-components/.../AdaptiveAccordion.kt` | Header toggles | Default | Hand on header | Added cursor |
| `AdaptiveBreadcrumbs` | `adaptive-components/.../AdaptiveBreadcrumbs.kt` | Non-selected crumbs | Default | Hand on non-selected crumbs | Added cursor to non-selected branch |
| `AdaptiveCarousel` | `adaptive-components/.../AdaptiveCarousel.kt` | Controls inherit button; indicators clickable | Default on indicators | Hand on enabled indicators | Added cursor to enabled indicators |
| `AdaptiveDialog` | `adaptive-components/.../AdaptiveDialog.kt` | Scrim dismisses | Default | Hand on dismiss scrim | Added cursor to scrim |
| `AdaptivePaneListItem` | `adaptive-layout/.../AdaptivePage.kt` | Only with `onClick` | Default | Hand only when clickable | Added cursor in clickable branch |
| Compact list/detail back row | `adaptive-layout/.../AdaptiveListDetailScaffold.kt` | Yes | Default | Hand | Added cursor |
| Navigation item | `adaptive-navigation/.../NavigationSurfaces.kt` | Yes | Default | Hand | Added cursor |
| Navigation drawer scrim | `adaptive-navigation/.../AdaptiveNavigationScaffold.kt` | Yes | Default | Hand | Added cursor |
| Navigation tree row | `adaptive-navigation/.../AdaptiveNavigationTree.kt` | Enabled rows | Default | Hand only when enabled | Added cursor |
| Data table row | `adaptive-data/.../AdaptiveDataView.kt` | Only with `onItemClick` | Default | Hand only when clickable | Added cursor in clickable branch |

## Exceptions

- `AdaptiveTextField` and `AdaptiveSearchField` do not apply hand cursors to their text input area.
- Static badges, avatars, thumbnails, surfaces and cards without `onClick` keep the default cursor.
- Action slots inside feedback states inherit behavior from the button or custom control provided by the consumer.

