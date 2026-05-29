# AdaptiveFormLayout

Purpose: responsive form sections, fields, validation messages, and actions.

Use it for admin settings and edit screens.

Primary API: `AdaptiveFormLayout(columns, labelPosition, stickyActionsOnCompact, maxWidth) { section { field { ... } } actions { ... } }`.

Simple example:

```kotlin
AdaptiveFormLayout {
    section(title = "Company") {
        field(label = "Name", required = true) {
            AdaptiveTextField(value = name, onValueChange = { name = it })
        }
    }
}
```

Advanced example: use `FieldSpan.Half` and `ValidationMessage`.

Responsive notes: compact screens resolve labels to top position.

Multiplatform notes: common Compose layout.

Limitations: validation state is supplied by the caller.
