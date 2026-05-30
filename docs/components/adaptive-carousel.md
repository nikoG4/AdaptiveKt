# AdaptiveCarousel

Use `AdaptiveCarousel` when a dashboard needs a small controlled sequence of cards without adding a pager dependency.

```kotlin
var selectedIndex by remember { mutableStateOf(0) }

AdaptiveCarousel(
    items = cards,
    selectedIndex = selectedIndex,
    onSelectedIndexChange = { selectedIndex = it },
) { card, index ->
    SummaryCard(card, index)
}
```

The component handles empty lists, one-item lists, looped navigation, indicators, and disabled controls.
