# AdaptiveKt — Adaptive Data View

## Purpose
Adaptive data views let apps reuse the same data model across desktop table and mobile card presentations.

v0.1-alpha focuses on a simple adaptive data wrapper with state-aware rendering and content slots.

## Size taxonomy

Adaptive rendering is driven by `AdaptiveBreakpoint`:

- `Compact` -> card list
- `Medium` -> card list or compact table
- `Expanded`, `Large` -> table

## Core types

### AdaptiveDataState

```kotlin
sealed interface AdaptiveDataState<out T>

object AdaptiveDataLoading : AdaptiveDataState<Nothing>

data class AdaptiveDataError(
    val message: String,
) : AdaptiveDataState<Nothing>

data class AdaptiveDataLoaded<T>(
    val items: List<T>,
    val emptyMessage: String? = null,
) : AdaptiveDataState<T>
```

### AdaptiveDataColumn

```kotlin
data class AdaptiveDataColumn<T>(
    val id: String,
    val header: String,
    val cell: @Composable (T) -> Unit,
    val minBreakpoint: AdaptiveBreakpoint = AdaptiveBreakpoint.Expanded,
)
```

### AdaptiveSortState

```kotlin
data class AdaptiveSortState(
    val sortedColumn: String,
    val ascending: Boolean,
)
```

### AdaptiveSelectionState

```kotlin
data class AdaptiveSelectionState<T>(
    val selectedItems: Set<T>,
)
```

### AdaptivePaginationState

```kotlin
data class AdaptivePaginationState(
    val currentPage: Int,
    val pageSize: Int,
    val totalItems: Int,
)
```

### AdaptiveFilterSlot

```kotlin
typealias AdaptiveFilterSlot = @Composable () -> Unit
```

## AdaptiveDataView

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

### v0.1-alpha behavior

This version supports:

- `state` for loading, empty, error, and loaded items,
- `columns` for table headers,
- `cardContent` for mobile card rendering,
- `filterSlot` for filter UI,
- `actions` for toolbar/action surface insertion,
- `rowActions` for per-item contextual actions,
- `onItemClick` for item selection.

### v0.2 planned extensions

The following concepts are defined but not required for v0.1-alpha:

- `AdaptiveSortState` for sortable columns,
- `AdaptiveSelectionState<T>` for bulk selection,
- `AdaptivePaginationState` for paged data,
- advanced filter panels and column visibility.

## Example usage

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
            id = "department",
            header = "Departamento",
            cell = { Text(it.department) },
        ),
    ),
    cardContent = { employee -> EmployeeCard(employee) },
    filterSlot = { EmployeeFilterControls() },
    actions = { EmployeeToolbar() },
    rowActions = { employee -> EmployeeRowActions(employee) },
    onItemClick = { employee -> onEmployeeSelected(employee) }
)
```

## Developer guidance

- Use `AdaptiveDataState` to centralize loading and error handling.
- Keep table cell rendering simple by using `AdaptiveDataColumn<T>`.
- Use `cardContent` to define the mobile card appearance while preserving row actions on desktop.
- `filterSlot` may render either a sidebar panel or a sheet/dialog depending on breakpoint.

## Compose Multiplatform considerations

- Desktop and Web table interactions may need separate tuning in the demo.
- Web/WASM card rendering should remain lightweight to avoid excessive DOM complexity.
- Sorting, selection, and pagination are explicitly left for v0.2 to keep the first release small.
