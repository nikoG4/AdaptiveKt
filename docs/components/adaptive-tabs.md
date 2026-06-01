# AdaptiveTabs

Purpose: segmented tabs for switching compact content sections.

Use it when you want to present a small set of mutually exclusive views in a single container.

Primary API: `AdaptiveTabs(tabs, selectedTab, onTabSelected, tabLabel, modifier)`.

Example:

```kotlin
AdaptiveTabs(
    tabs = listOf("Overview", "Activity", "Team"),
    selectedTab = selectedTab,
    onTabSelected = { selectedTab = it },
    tabLabel = { it },
)
```

Notes:

- Tabs are controlled by the caller.
- Supports horizontal scrolling on narrower widths.
- Use with content panels that update when the selected tab changes.
