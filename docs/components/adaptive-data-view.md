# AdaptiveDataView And AdaptiveCollectionView

Purpose: responsive data and collection presentation.

Use `AdaptiveDataView` for structured records with columns and row actions. Use `AdaptiveCollectionView` for generic item collections such as product grids, media cards and searchable lists.

Primary APIs:

- `AdaptiveDataView(state, columns, displayMode, queryState, pagination, filters, sortOptions, rowActions, onItemClick)`
- `AdaptiveCollectionView(items, displayMode, queryState, pagination, filters, sortOptions, listItemContent, gridItemContent)`

Simple table/card example:

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(items),
    columns = columns,
    displayMode = AdaptiveDataDisplayMode.Auto,
)
```

Simple collection grid example:

```kotlin
AdaptiveCollectionView(
    items = products,
    displayMode = AdaptiveCollectionDisplayMode.Grid,
    searchEnabled = true,
    queryState = queryState,
    onQueryChange = { queryState = it },
    listItemContent = { ProductCard(it) },
    gridItemContent = { ProductCard(it) },
)
```

Responsive notes: `AdaptiveDataDisplayMode.Auto` preserves compact/medium cards and expanded/large tables. `AdaptiveCollectionDisplayMode.Auto` uses list on compact and grid on larger breakpoints.

Multiplatform notes: common Compose implementation.

Limitations: query controls are state-hoisted; apps own local filtering or server calls.
