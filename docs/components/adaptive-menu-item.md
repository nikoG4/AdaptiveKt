# AdaptiveMenuItem

Purpose: row item for menus and dropdowns.

Use it inside dropdown surfaces for commands.

Primary API: `AdaptiveMenuItem(text, onClick, enabled, destructive, leadingIcon, trailingContent)`.

Simple example:

```kotlin
AdaptiveMenuItem("Sign out", destructive = true, onClick = {})
```

Advanced example: add a leading icon or trailing metadata.

Responsive notes: keep menu labels short.

Multiplatform notes: common Compose implementation.

Limitations: no built-in keyboard navigation yet.
