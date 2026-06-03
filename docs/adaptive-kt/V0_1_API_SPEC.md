# AdaptiveKt — v0.1-alpha API Specification

## Scope

v0.1-alpha is a minimal, implementable adaptive toolkit for Compose Multiplatform. It includes:

- `adaptive-core`
- `adaptive-layout`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`
- `adaptive-feedback`
- `admin-demo`

v0.1-alpha intentionally excludes advanced action surfaces, overlay components, and extended data features such as sorting, bulk actions, and pagination.

## Official Size Taxonomy

AdaptiveKt uses a single breakpoint taxonomy for v0.1-alpha:

- `AdaptiveBreakpoint.Compact`
- `AdaptiveBreakpoint.Medium`
- `AdaptiveBreakpoint.Expanded`
- `AdaptiveBreakpoint.Large`

This taxonomy is used everywhere in the public API.

### Concrete breakpoint definitions

- `Compact`: width < 600.dp
- `Medium`: 600.dp <= width < 840.dp
- `Expanded`: 840.dp <= width < 1200.dp
- `Large`: width >= 1200.dp

These values are the canonical v0.1-alpha width thresholds for `breakpointForWidth(width: Dp)`.

## adaptive-core

### Core types

```kotlin
enum class AdaptiveBreakpoint {
    Compact,
    Medium,
    Expanded,
    Large,
}

data class AdaptiveWindowSize(
    val width: Dp,
    val height: Dp,
)

data class AdaptiveInfo(
    val windowSize: AdaptiveWindowSize,
    val breakpoint: AdaptiveBreakpoint,
) {
    val isCompact: Boolean get() = breakpoint == AdaptiveBreakpoint.Compact
    val isMedium: Boolean get() = breakpoint == AdaptiveBreakpoint.Medium
    val isExpanded: Boolean get() = breakpoint == AdaptiveBreakpoint.Expanded
    val isLarge: Boolean get() = breakpoint == AdaptiveBreakpoint.Large
}

interface AdaptiveScope {
    val adaptiveInfo: AdaptiveInfo

    fun <T> adaptiveValue(
        compact: T,
        medium: T? = null,
        expanded: T? = null,
        large: T? = null,
    ): T
}

// Resolution fallback rules for adaptiveValue:
// Compact -> compact
// Medium -> medium ?: compact
// Expanded -> expanded ?: medium ?: compact
// Large -> large ?: expanded ?: medium ?: compact

object AdaptiveTokens {
    object Spacing {
        val ExtraSmall: Dp = 4.dp
        val Small: Dp = 8.dp
        val Medium: Dp = 16.dp
        val Large: Dp = 24.dp
        val ExtraLarge: Dp = 32.dp
    }

    object Radius {
        val Small: Dp = 4.dp
        val Medium: Dp = 8.dp
        val Large: Dp = 16.dp
    }

    object Widths {
        val Page: Dp = 1200.dp
        val Content: Dp = 1000.dp
        val Card: Dp = 320.dp
    }

    object PaneWidths {
        val Sidebar: Dp = 280.dp
        val Filter: Dp = 320.dp
    }
}

fun breakpointForWidth(width: Dp): AdaptiveBreakpoint

### AdaptiveContent

```kotlin
@Composable
fun AdaptiveContent(
    modifier: Modifier = Modifier,
    content: @Composable AdaptiveScope.() -> Unit,
)
```

### rememberAdaptiveInfo

```kotlin
@Composable
fun rememberAdaptiveInfo(): AdaptiveInfo
```

For v0.1-alpha, `AdaptiveContent` uses `BoxWithConstraints` in `commonMain` to derive available width and height.
It then computes `AdaptiveBreakpoint` through `breakpointForWidth(width)`.
The platform-specific measurement refinements for Android, Desktop, and Web/WASM remain future improvements.

### AdaptiveVisibility

```kotlin
@Composable
fun AdaptiveVisibility(
    visibleOn: Set<AdaptiveBreakpoint>,
    content: @Composable () -> Unit,
)
```

## adaptive-layout

### AdaptiveContainer

```kotlin
@Composable
fun AdaptiveContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = AdaptiveTokens.Widths.Page,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    content: @Composable () -> Unit,
)
```

### AdaptiveGrid

```kotlin
interface AdaptiveGridScope {
    fun item(
        span: Int = 12,
        content: @Composable () -> Unit,
    )
}

@Composable
fun AdaptiveGrid(
    modifier: Modifier = Modifier,
    columns: Int = 12,
    horizontalGap: Dp = AdaptiveTokens.Spacing.Medium,
    verticalGap: Dp = AdaptiveTokens.Spacing.Medium,
    content: @Composable AdaptiveGridScope.() -> Unit,
)
```

## adaptive-navigation

### AdaptiveNavItem

```kotlin
data class AdaptiveNavItem(
    val id: String,
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
    val enabled: Boolean = true,
)
```

### AdaptiveNavigationMode

```kotlin
enum class AdaptiveNavigationMode {
    Drawer,
    BottomNavigation,
    NavigationRail,
    Sidebar,
}
```

### AdaptiveNavigationScaffold

AdaptiveNavigationScaffold chooses the active navigation presentation automatically using the breakpoint derived from `AdaptiveContent`. It uses `navItems`, `selectedItemId`, and `onItemSelected` to render the right pattern by width:

- Compact -> Drawer + optional BottomNavigation
- Medium -> NavigationRail
- Expanded -> Sidebar
- Large -> Sidebar

The `sidebarContent`, `drawerContent`, and `bottomBar` slots are optional customization points and are not required to manually implement breakpoint selection.

```kotlin
@Composable
fun AdaptiveNavigationScaffold(
    modifier: Modifier = Modifier,
    navItems: List<AdaptiveNavItem>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    topBar: @Composable (selectedItem: AdaptiveNavItem?) -> Unit = {},
    sidebarContent: @Composable (() -> Unit)? = null,
    drawerContent: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
)
```

### Sidebar

```kotlin
@Composable
fun Sidebar(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)
```

### Drawer

```kotlin
@Composable
fun Drawer(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)
```

### BottomNavigation

```kotlin
@Composable
fun BottomNavigation(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)
```

### NavigationRail

```kotlin
@Composable
fun NavigationRail(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)
```

## adaptive-forms

### AdaptiveFormColumns

```kotlin
enum class AdaptiveFormColumns {
    Auto,
    One,
    Two,
    Three,
}
```

### LabelPosition

```kotlin
enum class LabelPosition {
    Top,
    Inline,
}
```

### FieldSpan

```kotlin
sealed interface FieldSpan {
    object Full : FieldSpan
    object Half : FieldSpan
    object Third : FieldSpan
    object TwoThirds : FieldSpan
    data class Columns(val count: Int) : FieldSpan
}
```

### AdaptiveValidationMessage

```kotlin
data class AdaptiveValidationMessage(
    val message: String,
    val isError: Boolean = true,
)
```

### AdaptiveFormScope

```kotlin
interface AdaptiveFormScope {
    fun section(
        title: String,
        description: String? = null,
        content: @Composable AdaptiveFormSectionScope.() -> Unit,
    )

    fun actions(
        content: @Composable AdaptiveFormActionsScope.() -> Unit,
    )
}

interface AdaptiveFormSectionScope {
    fun field(
        label: String,
        fieldSpan: FieldSpan = FieldSpan.Full,
        labelPosition: LabelPosition? = null,
        validationMessage: AdaptiveValidationMessage? = null,
        content: @Composable () -> Unit,
    )
}

interface AdaptiveFormActionsScope {
    fun primary(content: @Composable () -> Unit)
    fun secondary(content: @Composable () -> Unit)
}
```

### AdaptiveFormLayout

```kotlin
@Composable
fun AdaptiveFormLayout(
    modifier: Modifier = Modifier,
    columns: AdaptiveFormColumns = AdaptiveFormColumns.Auto,
    labelPosition: LabelPosition = LabelPosition.Top,
    sectionSpacing: Dp = AdaptiveTokens.Spacing.Medium,
    stickyActionsOnCompact: Boolean = true,
    content: @Composable AdaptiveFormScope.() -> Unit,
)
```


## adaptive-data

### AdaptiveDataState

```kotlin
sealed interface AdaptiveDataState<out T>

data object AdaptiveDataLoading : AdaptiveDataState<Nothing>

data class AdaptiveDataError(
    val message: String,
) : AdaptiveDataState<Nothing>

data class AdaptiveDataLoaded<T>(
    val items: List<T>,
    val emptyMessage: String? = null,
) : AdaptiveDataState<T>
```

If the target Kotlin version does not support `data object`, use `object AdaptiveDataLoading : AdaptiveDataState<Nothing>` instead.

### AdaptiveDataColumn

```kotlin
data class AdaptiveDataColumn<T>(
    val id: String,
    val header: String,
    val cell: @Composable (T) -> Unit,
    val minBreakpoint: AdaptiveBreakpoint = AdaptiveBreakpoint.Expanded,
)
```

### AdaptiveFilterSlot

```kotlin
typealias AdaptiveFilterSlot = @Composable () -> Unit
```

### AdaptiveDataView

```kotlin
@Composable
fun <T> AdaptiveDataView(
    modifier: Modifier = Modifier,
    state: AdaptiveDataState<T>,
    columns: List<AdaptiveDataColumn<T>>, 
    cardContent: @Composable (item: T) -> Unit,
    filterSlot: AdaptiveFilterSlot = {},
    actions: @Composable () -> Unit = {},
    rowActions: @Composable (item: T) -> Unit = {},
    onItemClick: ((T) -> Unit)? = null,
)
```

## adaptive-feedback

### AdaptiveEmptyState

```kotlin
@Composable
fun AdaptiveEmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    action: (@Composable () -> Unit)? = null,
)
```

### AdaptiveLoadingState

```kotlin
@Composable
fun AdaptiveLoadingState(
    modifier: Modifier = Modifier,
    message: String? = null,
)
```

### AdaptiveErrorState

```kotlin
@Composable
fun AdaptiveErrorState(
    modifier: Modifier = Modifier,
    title: String = "Error",
    message: String,
    retryAction: (@Composable () -> Unit)? = null,
)
```

## admin-demo v0.1

### Screens

- `DashboardScreen`
- `EmployeesScreen`
- `ProductsScreen`
- `InvoicesScreen`
- `SettingsScreen`

### Mock data

- `Employee(id: String, name: String, role: String, department: String)`
- `Product(id: String, name: String, category: String, price: String)`
- `Invoice(id: String, customer: String, total: String, status: String)`

### v0.1 demo requirements

- Desktop: fixed sidebar, top bar, table-based lists, 2-column forms.
- Mobile: drawer/hamburger navigation, card-based lists, 1-column forms.
- Use `AdaptiveDataView` for lists.
- Use `AdaptiveFormLayout` for forms.
- Use `AdaptiveEmptyState`, `AdaptiveLoadingState`, `AdaptiveErrorState` for data states.

### Demo structure example

```kotlin
val onItemSelected: (String) -> Unit = { route -> navigate(route) }

AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = currentRoute,
    onItemSelected = onItemSelected,
    topBar = { selectedItem -> AdminTopBar(selectedItem) },
    sidebarContent = { Sidebar(items = navItems, selectedItemId = currentRoute, onItemSelected = onItemSelected) },
    drawerContent = { Drawer(items = navItems, selectedItemId = currentRoute, onItemSelected = onItemSelected) },
    bottomBar = { BottomNavigation(items = navItems, selectedItemId = currentRoute, onItemSelected = onItemSelected) }
) { padding ->
    Box(Modifier.padding(padding)) {
        when (currentRoute) {
            "dashboard" -> DashboardScreen()
            "employees" -> EmployeesScreen()
            "products" -> ProductsScreen()
            "invoices" -> InvoicesScreen()
            "settings" -> SettingsScreen()
        }
    }
}
```
