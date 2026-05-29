# AdaptiveAnchoredDropdownMenu

Purpose: popup dropdown anchored to a trigger.

Use it for selects, overflow menus, and anchored action panels.

Primary API: `AdaptiveAnchoredDropdownMenu(expanded, onExpandedChange, enabled, matchAnchorWidth, maxHeight, placement, anchor, content)`.

Simple example:

```kotlin
AdaptiveAnchoredDropdownMenu(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    anchor = { _, toggle -> AdaptiveButton("Open", onClick = toggle) },
) {
    AdaptiveMenuItem("Action", onClick = {})
}
```

Advanced example: set `matchAnchorWidth = true` for form-like dropdowns.

Responsive notes: placement provider avoids viewport overflow.

Multiplatform notes: uses Compose `Popup`.

Limitations: collision handling is basic and menu content is caller-managed.
