# AdaptiveBreadcrumbs

Purpose: breadcrumb navigation for hierarchical pages and path-based workflows.

Use it when you need a compact page hierarchy indicator that can also navigate back along a path.

Primary API: `AdaptiveBreadcrumbs(items, selectedItem, onItemSelected, itemLabel, separator)`.

Example:

```kotlin
AdaptiveBreadcrumbs(
    items = listOf("Home", "Projects", "Billing"),
    selectedItem = "Billing",
    onItemSelected = { /* navigate */ },
    itemLabel = { it },
)
```

Notes:

- Supports a custom separator slot.
- Designed for inline path navigation and shallow breadcrumb trails.
- Uses the selected item to highlight the current page.
- Ideal for admin pages with nested sections or drill-down details.
