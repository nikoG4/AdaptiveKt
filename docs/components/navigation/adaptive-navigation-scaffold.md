# Adaptive Navigation Scaffold

## Purpose

`AdaptiveNavigationScaffold` is a layout container that manages responsive navigation for admin UIs. It automatically selects the appropriate navigation surface (Drawer, Sidebar, Rail, or Bottom Navigation) based on screen size.

## When to Use

Use `AdaptiveNavigationScaffold` when building:
- Admin dashboards with persistent navigation
- Multi-screen applications that need responsive menu layouts
- Apps with global navigation items that should adapt to screen size

## API Signature

```kotlin
@Composable
fun AdaptiveNavigationScaffold(
    adaptiveInfo: AdaptiveInfo,
    navigationItems: List<AdaptiveNavItem>,
    selectedId: String,
    onNavigate: (itemId: String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (navigationBarHeight: Dp) -> Unit,
)
```

## Simple Example

```kotlin
@Composable
fun AdminApp() {
    val adaptiveInfo = rememberAdaptiveInfo()
    var currentScreen by remember { mutableStateOf("dashboard") }
    
    AdaptiveNavigationScaffold(
        adaptiveInfo = adaptiveInfo,
        navigationItems = listOf(
            AdaptiveNavItem("dashboard", "Dashboard", Icons.Filled.Dashboard),
            AdaptiveNavItem("users", "Users", Icons.Filled.People),
            AdaptiveNavItem("settings", "Settings", Icons.Filled.Settings),
        ),
        selectedId = currentScreen,
        onNavigate = { currentScreen = it },
    ) { navigationHeight ->
        Box(modifier = Modifier.padding(bottom = navigationHeight)) {
            when (currentScreen) {
                "dashboard" -> DashboardScreen()
                "users" -> UsersScreen()
                "settings" -> SettingsScreen()
            }
        }
    }
}
```

## Navigation Modes by Breakpoint

| Breakpoint | Navigation Mode | Behavior |
|------------|-----------------|----------|
| **Compact** (< 600 dp) | Drawer | Slides in from left, overlay |
| **Medium** (600-840 dp) | Bottom Navigation | Bottom bar with labels |
| **Expanded** (840-1200 dp) | Rail | Left side, narrow, icon + label |
| **Large** (≥ 1200 dp) | Sidebar | Left side, always visible, full width |

## Advanced Example

```kotlin
@Composable
fun FullAdminApp() {
    val adaptiveInfo = rememberAdaptiveInfo()
    var currentScreen by remember { mutableStateOf("dashboard") }
    var isDrawerOpen by remember { mutableStateOf(false) }
    
    val navItems = listOf(
        AdaptiveNavItem(
            id = "dashboard",
            label = "Dashboard",
            icon = Icons.Filled.Dashboard,
        ),
        AdaptiveNavItem(
            id = "users",
            label = "Users",
            icon = Icons.Filled.People,
        ),
        AdaptiveNavItem(
            id = "products",
            label = "Products",
            icon = Icons.Filled.ShoppingCart,
        ),
        AdaptiveNavItem(
            id = "analytics",
            label = "Analytics",
            icon = Icons.Filled.BarChart,
        ),
        AdaptiveNavItem(
            id = "settings",
            label = "Settings",
            icon = Icons.Filled.Settings,
        ),
    )
    
    AdaptiveNavigationScaffold(
        adaptiveInfo = adaptiveInfo,
        navigationItems = navItems,
        selectedId = currentScreen,
        onNavigate = { newScreen ->
            currentScreen = newScreen
            // Close drawer on navigation (if compact)
            if (adaptiveInfo.breakpoint == Compact) {
                isDrawerOpen = false
            }
        },
    ) { navigationHeight ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = navigationHeight),
        ) {
            // Top app bar (optional)
            AppTopBar(title = navItems.find { it.id == currentScreen }?.label ?: "")
            
            // Content area
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    "dashboard" -> DashboardScreen()
                    "users" -> UsersScreen()
                    "products" -> ProductsScreen()
                    "analytics" -> AnalyticsScreen()
                    "settings" -> SettingsScreen()
                }
            }
        }
    }
}
```

## Responsive Behavior

### Compact Screens (Phone Portrait)
```
┌─────────────────────┐
│ ☰ | Dashboard       │  ← Drawer toggle in header
├─────────────────────┤
│                     │
│  Content Area       │
│                     │
└─────────────────────┘
```

### Medium Screens (Tablet Portrait / Phone Landscape)
```
┌─────────────────────┐
│  Dashboard Content  │
│                     │
└─────────────────────┘
┌─────┬─────┬─────────┐
│ 🏠  │ 👥  │ ⚙️      │  ← Bottom Navigation
└─────┴─────┴─────────┘
```

### Expanded Screens (Tablet Landscape)
```
┌───┬───────────────┐
│ 🏠│               │
├───┤ Dashboard     │
│ 👥│ Content       │
├───┤               │
│ 📊│               │
├───┤───────────────┤
│ ⚙️│               │
└───┴───────────────┘
```

### Large Screens (Desktop)
```
┌──────────┬─────────────┐
│ Dashboard│             │
├──────────┤ Dashboard   │
│ Users    │ Content     │
├──────────┤             │
│ Products │             │
├──────────┤─────────────┤
│ Analytics│             │
├──────────┤             │
│ Settings │             │
└──────────┴─────────────┘
```

## Navigation Item Definition

```kotlin
data class AdaptiveNavItem(
    val id: String,        // Unique identifier
    val label: String,     // Display label
    val icon: ImageVector, // Icon (from Compose Icons)
)

// Usage
val navItem = AdaptiveNavItem(
    id = "users",
    label = "User Management",
    icon = Icons.Filled.People,
)
```

## Multiplatform Notes

| Platform | Navigation Mode | Notes |
|----------|-----------------|-------|
| **JVM/Desktop** | Sidebar (default) | Resizable window; adapt by resizing |
| **Android** | Bottom Navigation (default) | Drawer available when app bar added |
| **iOS** | Tab Bar (equivalent) | iOS validation pending |
| **Wasm** | Dynamic based on window | Responsive to browser resize |

## Handling Navigation Height

The `navigationHeight` parameter indicates the space reserved by the navigation surface:

```kotlin
AdaptiveNavigationScaffold(
    // ... params
) { navigationHeight ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = navigationHeight),  // Avoid overlap
    ) {
        // Your content
    }
}
```

On **Compact** (Drawer) and **Expanded** (Rail/Sidebar), `navigationHeight` is typically `0.dp` because those surfaces don't reserve bottom space.

On **Medium** (Bottom Navigation), `navigationHeight` is non-zero to avoid content overlap.

## Known Limitations

- ⚠️ Nested navigation (sub-items) not yet supported
- ⚠️ Badges on navigation items not yet available
- ⚠️ Animation transitions between modes not fully polished
- ⚠️ iOS tab bar styling needs native validation

## Related Components

- [`AdaptiveInfo`](../core/adaptive-window-info.md) — Responsive context
- [`AdaptiveBreakpoints`](../core/adaptive-breakpoints.md) — Breakpoint values
- [`AdaptiveContainer`](../layout/adaptive-container.md) — Content layout

## See Also

- [Admin Demo](../../admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoApp.kt)
- [Navigation Patterns](../../adaptive-kt/NAVIGATION_PATTERNS.md)
- [Architecture Guide](../../adaptive-kt/ARCHITECTURE.md)
