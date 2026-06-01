# AdaptiveBreadcrumbs

`AdaptiveBreadcrumbs<T>` is a compact navigation component in `:adaptive-components`.

## Contract

- Accepts a list of items and a `selectedItem`.
- `onItemSelected` is invoked when the user taps a crumb.
- The selected item renders with stronger text and is not clickable.
- Accepts a generic `itemLabel` slot for flexible labels.

## Example

```kotlin
var selected by remember { mutableStateOf("Overview") }
val items = listOf("Overview", "Projects", "Billing", "Invoice #123")

AdaptiveBreadcrumbs(
    items = items,
    selectedItem = selected,
    onItemSelected = { selected = it },
    itemLabel = { it },
)
```

## Use cases

- Page hierarchy navigation
- Document path breadcrumbs
- Multi-step wizard headers
