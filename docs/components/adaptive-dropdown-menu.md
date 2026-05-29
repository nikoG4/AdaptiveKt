# AdaptiveDropdownMenu

Purpose: inline dropdown panel for menu content.

Use it where a simple expanded block is enough.

Primary API: `AdaptiveDropdownMenu(expanded, onDismissRequest, modifier, content)`.

Simple example:

```kotlin
AdaptiveDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
    AdaptiveMenuItem("Settings", onClick = {})
}
```

Advanced example: use `AdaptiveAnchoredDropdownMenu` when popup anchoring is needed.

Responsive notes: width is constrained for compact menus.

Multiplatform notes: common Compose implementation.

Limitations: this primitive does not anchor to a trigger by itself.
