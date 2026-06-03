# AdaptiveSelect

Purpose: nullable single-select dropdown.

Use it when a form or filter needs exactly one optional value.

Primary API: `AdaptiveSelect(options, selectedOption, onSelectedOptionChange, optionLabel, searchable, clearable, enabled, isError, supportingText, optionContent, selectedContent)`.

Simple example:

```kotlin
AdaptiveSelect(
    options = listOf("Draft", "Sent", "Paid"),
    selectedOption = status,
    onSelectedOptionChange = { status = it },
    optionLabel = { it },
)
```

Advanced example: enable `searchable`, `clearable`, and custom row content.

Responsive notes: menu can match the anchor width and has a max height.

Multiplatform notes: common Compose popup and fallback-safe content.

Limitations: no MultiSelect.
