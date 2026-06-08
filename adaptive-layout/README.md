# AdaptiveLayout

`adaptive-layout` provides the high-level structural primitives for building responsive interfaces without constantly writing `BoxWithConstraints`. These components read `LocalAdaptiveLayoutInfo.current` inherently.

## Core Components

- **`AdaptiveContainer`**: A centralized container holding content up to the `contentMaxWidth`.
- **`AdaptiveGrid`**: A flexible `LazyVerticalGrid` wrapper. Automatically pulls column counts from current configuration.
- **`AdaptivePage` / `AdaptiveColumnPage` / `AdaptiveScrollablePage`**: Wraps screens with safe `pagePadding`. Ideal for list or dashboard interfaces.
- **`AdaptiveSection`**: Renders headers and spaced content blocks cleanly.
- **`AdaptiveActionBar`**: An intelligent top action bar that wraps secondary action chips onto new rows on compact screens, or pushes them to the right on expanded displays.
- **`AdaptiveTwoPane`**: Eliminates manual `Row` vs `Column` checking when building detail panes. It implicitly uses the breakpoint state to switch orientations.
- **`AdaptiveListDetailScaffold`**: High-level scaffold for selectable list/detail patterns (like email or chat interfaces). Handles single-pane compact navigation and side-by-side expanded layouts seamlessly without manual breakpoints.

## Code Examples

### Simple Page Usage

```kotlin
AdaptiveScrollablePage {
    AdaptiveSection(
        title = "Products",
        subtitle = "Browse the catalog"
    ) {
        AdaptiveGrid {
            items(products) { product ->
                ProductCard(product)
            }
        }
    }
}
```

### Two Pane Setup

```kotlin
AdaptiveTwoPane(
    primary = { CartItems() },
    secondary = { OrderSummary() }
)
```

### Action Bar

```kotlin
AdaptiveActionBar(
    leadingContent = { SearchField(...) },
    secondaryActions = { FilterButton(...) },
    primaryAction = { AddButton(...) }
)
```

### AdaptiveListDetailScaffold

Use `AdaptiveListDetailScaffold` when the UI has a selectable list and a detail view. It automatically provides one-pane behavior on compact screens, two-pane behavior on larger screens, and manages default empty states and back navigation.

**Minimal Chat-like Usage:**
```kotlin
var selectedItem by remember { mutableStateOf<Item?>(null) }

AdaptiveListDetailScaffold(
    selectedItem = selectedItem,
    onBackToList = { selectedItem = null },
    listPane = {
        ItemList(
            selectedId = selectedItem?.id,
            onItemClick = { selectedItem = it }
        )
    },
    detailPane = { item ->
        ItemDetail(item)
    }
)
```

**Custom Pane Sizing:**
```kotlin
AdaptiveListDetailScaffold(
    selectedItem = selectedPrompt,
    listPaneSpec = AdaptivePaneSpec(
        weight = 0.32f,
        minWidth = 280.dp,
        maxWidth = 420.dp
    ),
    detailPaneSpec = AdaptivePaneSpec(
        weight = 0.68f,
        minWidth = 480.dp
    ),
    onBackToList = { selectedPrompt = null },
    listPane = { PromptList(...) },
    detailPane = { prompt -> PromptEditor(prompt) }
)
```

**Custom Empty Detail:**
```kotlin
AdaptiveListDetailScaffold(
    selectedItem = selectedFile,
    onBackToList = { selectedFile = null },
    listPane = { FileList(...) },
    detailPane = { file -> FilePreview(file) },
    emptyDetail = {
        AdaptiveEmptyState(
            title = "Select a file",
            message = "Choose a file to preview it."
        )
    }
)
```

**Custom Compact Behavior:**
```kotlin
AdaptiveListDetailScaffold(
    selectedItem = selectedTicket,
    behavior = AdaptiveListDetailBehavior(
        compact = AdaptiveListDetailCompactBehavior.ShowListUntilSelection,
        showBackButtonOnCompactDetail = true
    ),
    onBackToList = { selectedTicket = null },
    listPane = { TicketList(...) },
    detailPane = { ticket -> TicketDetail(ticket) }
)
```

**Disable default back header:**
```kotlin
AdaptiveListDetailScaffold(
    selectedItem = selectedConversation,
    behavior = AdaptiveListDetailBehavior(
        showBackButtonOnCompactDetail = false
    ),
    onBackToList = { selectedConversation = null },
    listPane = { ConversationList(...) },
    detailPane = { conversation -> ChatRoomWithOwnToolbar(conversation) }
)
```

> **Note**: Use `AdaptiveListDetailScaffold` when you have list-selection state. Use `AdaptiveTwoPane` when you simply need two static layout regions side-by-side.
