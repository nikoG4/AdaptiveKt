# AdaptiveNavigationScaffold

Purpose: admin shell with adaptive navigation surfaces.

Use it as the outer layout for dashboard-style apps.

Primary API: `AdaptiveNavigationScaffold(navItems, selectedItemId, onItemSelected, topBar, content)`.

Simple example:

```kotlin
AdaptiveNavigationScaffold(
    navItems = listOf(AdaptiveNavItem("dashboard", "Dashboard")),
    selectedItemId = "dashboard",
    onItemSelected = {},
) { padding -> /* screen content */ }
```

Advanced example: provide a top bar and switch content by selected item.

Responsive notes: mode is derived from breakpoints.

Multiplatform notes: common Compose layout.

Limitations: routing is intentionally app-owned.
