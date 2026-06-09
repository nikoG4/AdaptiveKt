# AdaptiveKt - Adaptive Data And Collection Views

## Purpose

`adaptive-data` provides reusable Compose Multiplatform primitives for structured data and generic item collections.

- `AdaptiveDataView` is best for table-like records with columns, row actions and mobile card roles.
- `AdaptiveCollectionView` is best for storefronts, product grids, media lists and generic card/list/grid collections.

Both components can use shared state-hoisted query controls.

## Display Modes

```kotlin
enum class AdaptiveDataDisplayMode {
    Auto,
    Table,
    Cards,
    List,
    Grid,
}

enum class AdaptiveCollectionDisplayMode {
    Auto,
    List,
    Grid,
    Cards,
    Table,
}
```

`AdaptiveDataView(displayMode = Auto, autoSwitchToCards = true)` preserves the original behavior:

- Compact and Medium -> cards.
- Expanded and Large -> table.

Set `autoSwitchToCards = false` to keep auto mode table-only. Set `displayMode = Cards` to force cards at every breakpoint.

## Query State

```kotlin
data class AdaptiveQueryState(
    val search: String = "",
    val filters: Map<String, Set<String>> = emptyMap(),
    val sortKey: String? = null,
    val sortDirection: AdaptiveSortDirection = AdaptiveSortDirection.Ascending,
    val page: Int = 1,
    val pageSize: Int = 20,
)
```

Related types:

- `AdaptivePaginationState`
- `AdaptiveSortOption`
- `AdaptiveFilterOption`
- `AdaptiveFilterValue`
- `AdaptiveSortDirection`

The data module does not fetch data. Apps own loading and server callbacks through `onQueryChange`.

## AdaptiveCollectionView

```kotlin
AdaptiveCollectionView(
    items = products,
    displayMode = AdaptiveCollectionDisplayMode.Grid,
    gridColumns = 4,
    queryState = queryState,
    onQueryChange = { queryState = it },
    pagination = AdaptivePaginationState(
        page = queryState.page,
        pageSize = queryState.pageSize,
        totalItems = totalCount,
    ),
    searchEnabled = true,
    filters = productFilters,
    sortOptions = sortOptions,
    listItemContent = { ProductCard(it) },
    gridItemContent = { ProductCard(it) },
)
```

Use it for:

- product grids;
- collection cards;
- search/filter/sort/pagination surfaces;
- list/grid/cards mode switching.

## AdaptiveDataView

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(rows),
    columns = columns,
    displayMode = AdaptiveDataDisplayMode.Auto,
    autoSwitchToCards = true,
    queryState = queryState,
    onQueryChange = { queryState = it },
    pagination = pagination,
    searchEnabled = true,
    sortOptions = sortOptions,
    filters = filters,
    rowActions = rowActions,
)
```

Force cards only:

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(rows),
    columns = columns,
    displayMode = AdaptiveDataDisplayMode.Cards,
    cardContent = { row -> RowCard(row) },
)
```

Force table only:

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(rows),
    columns = columns,
    displayMode = AdaptiveDataDisplayMode.Table,
)
```

## Compatibility Notes

- Existing `AdaptiveDataView` callers continue to compile.
- Query controls render only when `queryState` and `onQueryChange` are provided.
- `List` and `Grid` display modes on `AdaptiveDataView` currently render through the card path; they are reserved for future structured-data renderers.
- `AdaptiveCollectionView.Table` falls back to list rendering because generic collections do not provide columns.
