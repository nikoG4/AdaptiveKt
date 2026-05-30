# AdaptiveNavigationTree

`AdaptiveNavigationTree` is a controlled hierarchical navigation primitive for admin sidebars, settings trees, and nested workspace navigation.

## API

```kotlin
data class AdaptiveNavigationTreeItem(
    val id: String,
    val label: String,
    val children: List<AdaptiveNavigationTreeItem> = emptyList(),
    val enabled: Boolean = true,
    val badge: String? = null
)

@Composable
fun AdaptiveNavigationTree(
    items: List<AdaptiveNavigationTreeItem>,
    selectedItemId: String?,
    onItemSelected: (AdaptiveNavigationTreeItem) -> Unit,
    expandedItemIds: Set<String>,
    onExpandedItemIdsChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    maxDepth: Int = 6,
)
```

## Behavior

- Expansion is controlled by `expandedItemIds`.
- Selection is controlled by `selectedItemId`.
- Nested items are indented by depth.
- Disabled items are visible but not clickable.
- Badges use `AdaptiveBadge`.
- Expand/collapse icons use embedded `AdaptiveIcons`.
- Hover and selected states use `AdaptiveTheme` colors so light and dark mode stay legible.

`AdaptiveNavigationTree` does not replace `AdaptiveNavigationScaffold`; it is a smaller primitive for hierarchical content inside a sidebar or panel.
