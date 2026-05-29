# AdaptiveMultiSelect

`AdaptiveMultiSelect<T>` is a multi-selection dropdown component in `:adaptive-components`.

## Contract

- Uses `AdaptiveAnchoredDropdownMenu` for popup positioning.
- `selectedOptions` represents the current complete selection.
- `onSelectedOptionsChange` emits the next complete selection list.
- `optionLabel` is required.
- `optionContent` customizes option rows and receives the selected state.
- `chipContent` customizes selected chip content.
- `maxVisibleChips` limits rendered chips and shows a `+N` overflow chip.
- `clearable` shows a clear-all action when at least one option is selected.
- `searchable` filters options locally with `selectMatchesQuery`.
- `enabled = false` blocks trigger interaction.
- `isError` and `supportingText` render validation feedback.
- The dropdown stays open while options are toggled.

## Demo Routes

- `components-multiselects`
- `components-multiselects-open`

These routes work for Desktop capture and Wasm query-string routing.

## Example

```kotlin
var teams by remember { mutableStateOf(listOf("Operations", "Finance")) }

AdaptiveMultiSelect(
    options = listOf("Operations", "Finance", "Support", "Sales"),
    selectedOptions = teams,
    onSelectedOptionsChange = { teams = it },
    optionLabel = { it },
    label = "Teams",
    placeholder = "Choose teams",
    searchable = true,
    clearable = true,
    maxVisibleChips = 2,
)
```

## Custom Option Content

```kotlin
AdaptiveMultiSelect(
    options = people,
    selectedOptions = selectedPeople,
    onSelectedOptionsChange = { selectedPeople = it },
    optionLabel = { it.name },
    optionContent = { person, selected ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            AdaptiveAvatar(name = person.name, size = 28.dp)
            Text(person.name)
            if (selected) {
                AdaptiveBadge("Selected")
            }
        }
    },
)
```

## Custom Chip Content

```kotlin
AdaptiveMultiSelect(
    options = tags,
    selectedOptions = selectedTags,
    onSelectedOptionsChange = { selectedTags = it },
    optionLabel = { it },
    chipContent = { tag ->
        Text(tag)
    },
)
```

## Limitations

- Search is local only.
- Async/server search is out of scope.
- Virtualized menus are out of scope.
- The selection list uses equality to detect selected options.
