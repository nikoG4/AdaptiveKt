# Adaptive Collection And Data View Design

## Existing API Audit

`AdaptiveDataView` was focused on structured tabular data:

- `AdaptiveDataState<T>` handled loading/content/error/empty.
- `AdaptiveDataColumn<T>` described table columns and mobile card roles.
- `AdaptiveDataAction<T>` handled row actions.
- Table-to-card behavior existed, but was implicit: compact and medium rendered cards, expanded and large rendered tables.
- Filtering and actions were possible through custom slots, but common search/sort/filter/pagination controls were rebuilt by demos.

## Decision

Keep `AdaptiveDataView` as the structured data/table component, and add `AdaptiveCollectionView` for generic storefront/list/grid/card collections.

This avoids forcing ecommerce/product grids into a table-first API while still sharing query state and controls.

## Added Architecture

- `AdaptiveDataDisplayMode`: `Auto`, `Table`, `Cards`, `List`, `Grid`.
- `AdaptiveCollectionDisplayMode`: `Auto`, `List`, `Grid`, `Cards`, `Table`.
- `AdaptiveQueryState`: search, filters, sort, page and page size.
- `AdaptivePaginationState`: current page, page size, total item count and size options.
- `AdaptiveSortOption`, `AdaptiveFilterOption`, `AdaptiveFilterValue`, `AdaptiveSortDirection`.
- `AdaptiveDataQueryControls`: shared optional state-hoisted controls.
- `AdaptiveCollectionView`: generic item collection rendering with optional query controls.

## Compatibility Plan

Existing `AdaptiveDataView` calls remain source-compatible:

- `displayMode` defaults to `Auto`.
- `autoSwitchToCards` defaults to `true`, preserving the original compact/medium card conversion.
- Query controls render only when `queryState` and `onQueryChange` are provided.

## Examples

```kotlin
AdaptiveCollectionView(
    items = products,
    displayMode = AdaptiveCollectionDisplayMode.Grid,
    queryState = queryState,
    onQueryChange = { queryState = it },
    pagination = AdaptivePaginationState(page = queryState.page, pageSize = queryState.pageSize, totalItems = total),
    searchEnabled = true,
    filters = productFilters,
    sortOptions = sortOptions,
    listItemContent = { ProductCard(it) },
    gridItemContent = { ProductCard(it) },
)
```

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(rows),
    columns = columns,
    displayMode = AdaptiveDataDisplayMode.Cards,
    cardContent = { row -> RowCard(row) },
)
```

## Current Limitations

- `AdaptiveDataView` accepts `List` and `Grid` display modes for source-compatible future growth, but currently renders non-table modes through the card path.
- `AdaptiveCollectionView.Table` falls back to list rendering because generic collections do not define columns.
- Data fetching is intentionally external and state-hoisted.
