# AdaptiveMultiSelect

Purpose: multi-select dropdown with selected chips.

Use it when a form or filter needs multiple optional values from a known local option list.

Primary API: `AdaptiveMultiSelect(options, selectedOptions, onSelectedOptionsChange, optionLabel, searchable, clearable, enabled, isError, supportingText, maxVisibleChips, optionContent, chipContent)`.

Simple example:

```kotlin
AdaptiveMultiSelect(
    options = listOf("Operations", "Finance", "Support"),
    selectedOptions = selectedTeams,
    onSelectedOptionsChange = { selectedTeams = it },
    optionLabel = { it },
)
```

Advanced example: enable `searchable`, set `maxVisibleChips`, and provide `optionContent` for avatar rows or `chipContent` for custom selected chips.

Responsive notes: the menu matches the anchor width, has a max height, and selected chips collapse to `+N` when `maxVisibleChips` is exceeded.

Multiplatform notes: implemented in common Compose using Foundation and AdaptiveKt primitives; it compiles for JVM, Android, iOS targets, and Wasm.

Limitations: search is local only; async/server search and virtualized option lists are future work.
