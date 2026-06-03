# AdaptiveKt — API Design

## API Goals

- Discoverable composable APIs.
- Typed adaptive primitives instead of string-based styles.
- Slots for content injection and customization.
- `Modifier` support in every public composable.
- Minimal built-in visuals; structure-first API.

## Core API Shapes

### AdaptiveContent

A root provider for adaptive context.

```kotlin
@Composable
fun AdaptiveContent(
    modifier: Modifier = Modifier,
    content: @Composable AdaptiveScope.() -> Unit,
)
```

`AdaptiveContent` uses `BoxWithConstraints` in commonMain to derive available width and height. For v0.1-alpha this is the canonical measurement strategy; platform-specific measurement refinements are future work.

### Breakpoint thresholds

- `Compact`: width < 600.dp
- `Medium`: 600.dp <= width < 840.dp
- `Expanded`: 840.dp <= width < 1200.dp
- `Large`: width >= 1200.dp

### AdaptiveContainer

A page shell that applies responsive width constraints and gutters.

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

A lightweight responsive grid container.

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

### AdaptiveGrid items

A grid child is defined inside `AdaptiveGrid` using `item(span = ...)`.

### AdaptiveNavigationScaffold

A scaffold that renders adaptive navigation chrome and emits selection events. The scaffold selects the navigation mode automatically by breakpoint:

- `Compact` -> `Drawer` + optional `BottomNavigation`
- `Medium` -> `NavigationRail`
- `Expanded` -> `Sidebar`
- `Large` -> `Sidebar`

The `sidebarContent`, `drawerContent`, and `bottomBar` slots are optional customization points and are not intended to force manual breakpoint handling.

```kotlin
@Composable
fun AdaptiveNavigationScaffold(
    modifier: Modifier = Modifier,
    navItems: List<AdaptiveNavItem>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    topBar: @Composable (AdaptiveNavItem?) -> Unit = {},
    sidebarContent: @Composable (() -> Unit)? = null,
    drawerContent: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
)
```

### Navigation components

```kotlin
@Composable
fun Sidebar(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)

@Composable
fun Drawer(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)

@Composable
fun BottomNavigation(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)

@Composable
fun NavigationRail(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
)
```

### AdaptiveFormLayout

A responsive form layout container.

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

### AdaptiveDataView

A wrapper that renders a list as a desktop table or mobile cards.

```kotlin
@Composable
fun <T> AdaptiveDataView(
    modifier: Modifier = Modifier,
    state: AdaptiveDataState<T>,
    columns: List<AdaptiveDataColumn<T>>,
    cardContent: @Composable (item: T) -> Unit,
    filterSlot: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    rowActions: @Composable (item: T) -> Unit = {},
    onItemClick: ((T) -> Unit)? = null,
)
```

### AdaptiveEmptyState / AdaptiveLoadingState / AdaptiveErrorState

These are simple feedback primitives used by data-driven screens.

```kotlin
@Composable
fun AdaptiveEmptyState(...)

@Composable
fun AdaptiveLoadingState(...)

@Composable
fun AdaptiveErrorState(...)
```

## Adaptive utilities

### rememberAdaptiveInfo

```kotlin
@Composable
fun rememberAdaptiveInfo(): AdaptiveInfo
```

### adaptiveValue

```kotlin
fun <T> AdaptiveScope.adaptiveValue(
    compact: T,
    medium: T? = null,
    expanded: T? = null,
    large: T? = null,
): T
```

Resolution fallback rules:

- Compact -> compact
- Medium -> medium ?: compact
- Expanded -> expanded ?: medium ?: compact
- Large -> large ?: expanded ?: medium ?: compact

### AdaptiveVisibility

```kotlin
@Composable
fun AdaptiveVisibility(
    visibleOn: Set<AdaptiveBreakpoint>,
    content: @Composable () -> Unit,
)
```

## Example API usage

### AdaptiveContent with breakpoint-aware values

```kotlin
AdaptiveContent {
    val horizontalPadding = adaptiveValue(
        compact = 16.dp,
        medium = 20.dp,
        expanded = 24.dp,
        large = 32.dp,
    )

    AdaptiveContainer(Modifier.padding(horizontal = horizontalPadding)) {
        // content
    }
}
```

### AdaptiveGrid items

```kotlin
AdaptiveGrid(columns = 12) {
    item(span = 8) {
        Text("Main content")
    }
    item(span = 4) {
        Text("Sidebar")
    }
}
```

### AdaptiveNavigationScaffold

```kotlin
val onItemSelected: (String) -> Unit = { id -> navigateTo(id) }

AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = selectedId,
    onItemSelected = onItemSelected,
    topBar = { selected -> AppTopBar(title = selected?.label ?: "Admin") },
    sidebarContent = {
        Sidebar(
            items = navItems,
            selectedItemId = selectedId,
            onItemSelected = onItemSelected
        )
    },
    drawerContent = {
        Drawer(
            items = navItems,
            selectedItemId = selectedId,
            onItemSelected = onItemSelected
        )
    },
    bottomBar = {
        BottomNavigation(
            items = navItems,
            selectedItemId = selectedId,
            onItemSelected = onItemSelected
        )
    }
) { padding ->
    Box(Modifier.padding(padding)) {
        ContentScreen()
    }
}
```

### AdaptiveFormLayout

```kotlin
AdaptiveFormLayout {
    section(title = "Empleado") {
        field(label = "Nombre", fieldSpan = FieldSpan.Full) {
            TextField(value = name, onValueChange = onNameChange)
        }

        field(label = "Departamento", fieldSpan = FieldSpan.Half) {
            TextField(value = department, onValueChange = onDepartmentChange)
        }

        field(label = "Cargo", fieldSpan = FieldSpan.Half) {
            TextField(value = role, onValueChange = onRoleChange)
        }
    }

    actions {
        primary { Button(onClick = save) { Text("Guardar") } }
        secondary { TextButton(onClick = cancel) { Text("Cancelar") } }
    }
}
```

### AdaptiveDataView

```kotlin
AdaptiveDataView(
    state = dataState,
    columns = listOf(
        AdaptiveDataColumn(
            id = "name",
            header = "Empleado",
            cell = { Text(it.name) },
        ),
        AdaptiveDataColumn(
            id = "role",
            header = "Cargo",
            cell = { Text(it.role) },
        ),
    ),
    cardContent = { employee -> EmployeeCard(employee) },
    filterSlot = { EmployeeFilterPanel() },
    actions = { EmployeeActions() },
    rowActions = { employee -> EmployeeRowActions(employee) },
    onItemClick = { employee -> openEmployee(employee) }
)
```

## Design notes

- Public APIs avoid duplicate names: layout uses `AdaptiveGridScope.item`, data uses `AdaptiveDataColumn<T>`.
- `AdaptiveNavigationScaffold` is intentionally callback-based and does not introduce routing primitives.
- `AdaptiveFormLayout` is a structure provider; field content is supplied by developer composables.
