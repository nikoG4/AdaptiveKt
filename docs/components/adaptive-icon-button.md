# AdaptiveIconButton

Purpose: compact icon-only action.

Use it for toolbar, clear, close, overflow, and repeated row actions.

Primary API: `AdaptiveIconButton(onClick, modifier, enabled, size, content)`.

Simple example:

```kotlin
AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Close() }
```

Advanced example: pair with tooltips at the app layer when meaning is not obvious.

Responsive notes: keep a stable `size` in dense toolbars.

Multiplatform notes: implemented with common Compose primitives.

Limitations: tooltip behavior is not built in.
