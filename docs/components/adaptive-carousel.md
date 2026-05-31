# AdaptiveCarousel

Use `AdaptiveCarousel` when a dashboard needs a small controlled sequence of cards without adding a pager dependency.

```kotlin
var selectedIndex by remember { mutableStateOf(0) }

AdaptiveCarousel(
    items = cards,
    selectedIndex = selectedIndex,
    onSelectedIndexChange = { selectedIndex = it },
    transition = AdaptiveCarouselTransition.Slide,
) { card, index ->
    SummaryCard(card, index)
}
```

The component handles empty lists, one-item lists, looped navigation, indicators, disabled controls, and animated item transitions.

Transition options:

- `Slide` (default)
- `Fade`
- `Scale`
- `None`

Use `transition = AdaptiveCarouselTransition.None` when a deterministic non-animated state is preferred. Autoplay is intentionally not part of the current API.
