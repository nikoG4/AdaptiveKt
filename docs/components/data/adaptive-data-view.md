# Adaptive Data View

## Purpose

`AdaptiveDataView` is a responsive component that displays tabular data, adapting its layout between table mode (large screens) and card mode (mobile screens). It supports filtering, sorting, row selection, and custom actions.

## When to Use

Use `AdaptiveDataView` when:
- Displaying lists of structured data (users, products, orders, etc.)
- You need responsive table/card switching
- Supporting filtering, sorting, or bulk actions
- Handling loading, empty, and error states
- You want row selection and actions

## API Signature

```kotlin
@Composable
fun <T> AdaptiveDataView(
    items: List<T>,
    columns: List<DataColumn<T>>,
    modifier: Modifier = Modifier,
    selectedIds: Set<String> = emptySet(),
    onSelectionChange: (Set<String>) -> Unit = {},
    onAction: (action: String, itemId: String) -> Unit = { _, _ -> },
    state: DataState<T> = DataState.Content(items),
    filterSlot: (@Composable () -> Unit)? = null,
    actionSlot: (@Composable (T) -> Unit)? = null,
)
```

## Simple Example

```kotlin
data class User(val id: String, val name: String, val email: String)

@Composable
fun UsersDataView() {
    val users by remember { mutableStateOf(listOf(
        User("1", "Alice", "alice@example.com"),
        User("2", "Bob", "bob@example.com"),
    )) }
    
    AdaptiveDataView(
        items = users,
        columns = listOf(
            DataColumn("Name") { user -> user.name },
            DataColumn("Email") { user -> user.email },
        ),
        state = DataState.Content(users),
    )
}
```

## Advanced Example

```kotlin
@Composable
fun ProductsScreen() {
    var products by remember { mutableStateOf(listOf(...)) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    AdaptiveDataView(
        items = products.filter { it.name.contains(searchQuery) },
        columns = listOf(
            DataColumn("SKU") { product -> product.sku },
            DataColumn("Name") { product -> product.name },
            DataColumn("Price") { product -> "$${product.price}" },
            DataColumn("Stock") { product -> "${product.stock} units" },
        ),
        selectedIds = selectedIds,
        onSelectionChange = { selectedIds = it },
        onAction = { action, productId ->
            when (action) {
                "edit" -> navigateToEdit(productId)
                "delete" -> deleteProduct(productId)
                "duplicate" -> duplicateProduct(productId)
            }
        },
        state = when {
            isLoading -> DataState.Loading("Fetching products...")
            products.isEmpty() -> DataState.Empty(
                title = "No products",
                description = "Create your first product to get started.",
                action = "New Product",
                onAction = { navigateToCreate() },
            )
            else -> DataState.Content(products)
        },
        filterSlot = {
            AdaptiveSearchField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search products...",
            )
        },
    )
}
```

## Data States

### Loading State

```kotlin
AdaptiveDataView(
    items = emptyList(),
    columns = columns,
    state = DataState.Loading("Fetching data..."),
)
```

### Empty State

```kotlin
AdaptiveDataView(
    items = emptyList(),
    columns = columns,
    state = DataState.Empty(
        title = "No items",
        description = "Create your first item to get started.",
        icon = Icons.Filled.Inbox,
        action = "Create Item",
        onAction = { navigate() },
    ),
)
```

### Error State

```kotlin
AdaptiveDataView(
    items = emptyList(),
    columns = columns,
    state = DataState.Error(
        title = "Failed to load",
        description = "Please check your connection and try again.",
        action = "Retry",
        onAction = { retry() },
    ),
)
```

### Content State

```kotlin
AdaptiveDataView(
    items = myItems,
    columns = columns,
    state = DataState.Content(myItems),
)
```

## Responsive Behavior

On **Compact** and **Medium** screens, the view switches to card layout:

```
┌─────────────────────┐
│ Name: Alice         │
│ Email: alice@ex...  │
│ [Edit] [Delete]     │
└─────────────────────┘
```

On **Expanded** and **Large** screens, it displays as a table:

```
┌──────────────┬──────────────────┐
│ Name         │ Email            │
├──────────────┼──────────────────┤
│ Alice        │ alice@example... │
│ Bob          │ bob@example...   │
└──────────────┴──────────────────┘
```

## Multiplatform Notes

| Platform | Notes |
|----------|-------|
| **JVM/Desktop** | Full table and card modes |
| **Android** | Card mode by default due to narrow viewports |
| **iOS** | Target declared; needs macOS validation |
| **Wasm** | Full support; table on desktop, cards on mobile |

## Column Definition

```kotlin
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
)

val columns = listOf(
    DataColumn("Product Name") { product -> product.name },
    DataColumn("Category") { product -> product.category },
    DataColumn("Price") { product -> String.format("$%.2f", product.price) },
)
```

## Selection and Actions

```kotlin
var selectedIds by remember { mutableStateOf(setOf<String>()) }

AdaptiveDataView(
    items = items,
    columns = columns,
    selectedIds = selectedIds,
    onSelectionChange = { newSelection ->
        selectedIds = newSelection
    },
    onAction = { action, itemId ->
        when (action) {
            "view" -> viewItem(itemId)
            "edit" -> editItem(itemId)
            "delete" -> deleteItem(itemId)
        }
    },
)
```

## Filter Integration

```kotlin
AdaptiveDataView(
    items = filteredItems,
    columns = columns,
    filterSlot = {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            AdaptiveTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search",
                modifier = Modifier.weight(1f),
            )
            AdaptiveButton(
                text = "Filter",
                onClick = { applyFilters() },
                variant = Secondary,
            )
        }
    },
)
```

## Known Limitations

- ⚠️ Server-side pagination not yet supported
- ⚠️ Sorting API in development
- ⚠️ Column resizing not supported
- ⚠️ Horizontal scroll on mobile not fully optimized
- ⚠️ Inline editing cells not yet available

## Related Components

- [`AdaptiveCard`](./adaptive-card.md) — Individual item card display
- [`AdaptiveButton`](./adaptive-button.md) — Action buttons
- [`AdaptiveFormLayout`](../forms/adaptive-form-layout.md) — Create/edit forms
- [`EmptyState`, `LoadingState`, `ErrorState`](../feedback/adaptive-feedback-states.md)

## See Also

- [Admin Demo Products Screen](../../admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/screens/ProductsScreen.kt)
- [Data View Design](../../adaptive-kt/ADAPTIVE_DATA_VIEW.md)
- [Responsive Tables Guide](../../guides/responsive-data.md)
