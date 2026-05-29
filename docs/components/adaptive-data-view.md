# AdaptiveDataView

Purpose: responsive data presentation.

Use it for admin lists that should become cards on compact screens and tables on larger screens.

Primary API: `AdaptiveDataView(state, columns, filterSlot, actions, rowActions, onItemClick)`.

Simple example:

```kotlin
AdaptiveDataView(
    state = AdaptiveDataContent(items),
    columns = columns,
)
```

Advanced example: pass `AdaptiveDataAction` values for primary, secondary, and overflow row actions.

Responsive notes: compact and medium use cards; expanded and large use table layout.

Multiplatform notes: common Compose implementation.

Limitations: no virtualization or server-side sorting.
