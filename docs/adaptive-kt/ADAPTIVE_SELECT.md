# AdaptiveSelect

`AdaptiveSelect<T>` is a single-selection dropdown component in `:adaptive-components`.

## Contract

- Uses `AdaptiveAnchoredDropdownMenu` for popup positioning.
- `selectedOption` is nullable.
- `onOptionSelected(null)` clears selection.
- `optionLabel` is required.
- `optionContent` and `selectedContent` are optional slots.
- `searchable` filters options locally with `selectMatchesQuery`.
- `clearable` shows a close action when a selection exists.
- `enabled = false` blocks trigger interaction.
- `isError` and `supportingText` render validation feedback.

## Demo Routes

- `components-selects`
- `components-selects-open`

These routes work for Desktop capture and Wasm query-string routing.

## Example

```kotlin
var status by remember { mutableStateOf<String?>(null) }

AdaptiveSelect(
    options = listOf("Open", "Pending", "Closed"),
    selectedOption = status,
    onOptionSelected = { status = it },
    optionLabel = { it },
    label = "Status",
    placeholder = "Choose status",
    searchable = true,
    clearable = true,
)
```

## Limitations

This is a single-select component. MultiSelect is not implemented.
