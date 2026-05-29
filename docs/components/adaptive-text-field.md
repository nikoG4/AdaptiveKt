# AdaptiveTextField

Purpose: text input with label, placeholder, validation, and icon slots.

Use it for forms and filters.

Primary API: `AdaptiveTextField(value, onValueChange, label, placeholder, enabled, validationMessage, leadingIcon, trailingIcon)`.

Simple example:

```kotlin
AdaptiveTextField(value = name, onValueChange = { name = it }, label = "Name")
```

Advanced example: provide icons and validation message.

Responsive notes: it fills available width; constrain through parent layout.

Multiplatform notes: common Compose text field primitives.

Limitations: single-line behavior only in the current component.
