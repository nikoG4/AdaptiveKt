# Adaptive Data View

## Purpose

`AdaptiveDataView` displays structured records as tables or cards. `AdaptiveCollectionView` displays generic item collections as list, grid or cards.

## When To Use

Use `AdaptiveDataView` for invoices, users, orders and other column-oriented data.

Use `AdaptiveCollectionView` for product grids, content cards, media collections and storefront browsing.

## Display Modes

```kotlin
AdaptiveDataDisplayMode.Auto
AdaptiveDataDisplayMode.Table
AdaptiveDataDisplayMode.Cards
AdaptiveDataDisplayMode.List
AdaptiveDataDisplayMode.Grid
```

`Auto` preserves the default table-to-card behavior. Use `Table` or `Cards` to force one presentation.

```kotlin
AdaptiveCollectionDisplayMode.Auto
AdaptiveCollectionDisplayMode.List
AdaptiveCollectionDisplayMode.Grid
AdaptiveCollectionDisplayMode.Cards
AdaptiveCollectionDisplayMode.Table
```

## Query Controls

Shared query controls use `AdaptiveQueryState` and `onQueryChange`.

```kotlin
AdaptiveCollectionView(
    items = pageItems,
    displayMode = AdaptiveCollectionDisplayMode.Grid,
    queryState = queryState,
    onQueryChange = { queryState = it },
    pagination = AdaptivePaginationState(
        page = queryState.page,
        pageSize = queryState.pageSize,
        totalItems = totalItems,
    ),
    searchEnabled = true,
    filters = filters,
    sortOptions = sortOptions,
    listItemContent = { ProductCard(it) },
    gridItemContent = { ProductCard(it) },
)
```

## Data View Example

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(items),
    columns = columns,
    displayMode = AdaptiveDataDisplayMode.Auto,
    rowActions = actions,
)
```

## Multiplatform Notes

The implementation lives in `commonMain` and uses Compose Foundation plus AdaptiveKt components.

## Limitations

- Query controls are state-hoisted; apps own local filtering or server requests.
- `AdaptiveDataView` currently renders `List` and `Grid` requests through the card path.
- `AdaptiveCollectionView.Table` falls back to list because generic collections do not define columns.
