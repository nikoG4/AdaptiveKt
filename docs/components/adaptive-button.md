# AdaptiveButton

Purpose: action button with variants, sizes, disabled state, and icon slots.

Use it for primary, secondary, ghost, and destructive actions.

Primary API: `AdaptiveButton(text, onClick, variant, size, enabled, leadingIcon, trailingIcon)`.

Simple example:

```kotlin
AdaptiveButton("Save", onClick = {})
```

Advanced example:

```kotlin
AdaptiveButton(
    text = "Create",
    variant = AdaptiveButtonVariant.Primary,
    size = AdaptiveButtonSize.Large,
    leadingIcon = { AdaptiveIcons.Plus() },
    onClick = {},
)
```

Responsive notes: buttons size from tokens; parent layout decides wrapping.

Multiplatform notes: no platform-specific dependency.

Limitations: no loading state parameter yet.
