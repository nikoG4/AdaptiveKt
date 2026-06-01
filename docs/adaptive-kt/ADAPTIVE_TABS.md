# AdaptiveTabs

`AdaptiveTabs<T>` is a segmented tab row in `:adaptive-components`.

## Contract

- Accepts a list of tabs and a selected tab.
- `onTabSelected` is invoked when a tab is tapped.
- Uses `tabLabel` to render custom tab text.
- Selected tab is highlighted with a stronger background and border.

## Example

```kotlin
val tabs = listOf("Overview", "Activity", "Team")
var selectedTab by remember { mutableStateOf(tabs.first()) }

AdaptiveTabs(
    tabs = tabs,
    selectedTab = selectedTab,
    onTabSelected = { selectedTab = it },
    tabLabel = { it },
)
```

## Use cases

- Section switching within a page
- Compact navigation between views
- Detail panels with shared content areas
