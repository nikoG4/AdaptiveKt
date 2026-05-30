# AdaptiveNavigationTree

Use `AdaptiveNavigationTree` for nested admin navigation.

```kotlin
var selected by remember { mutableStateOf("overview") }
var expanded by remember { mutableStateOf(setOf("workspace")) }

AdaptiveNavigationTree(
    items = treeItems,
    selectedItemId = selected,
    onItemSelected = { selected = it.id },
    expandedItemIds = expanded,
    onExpandedItemIdsChange = { expanded = it },
)
```

It is controlled by the caller and keeps selected, hover, disabled, and badge states consistent with the shared theme.
