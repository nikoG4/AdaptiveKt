# AdaptiveCarousel

`AdaptiveCarousel` is a controlled Foundation-only carousel for compact admin summaries, onboarding panels, feature cards, and dashboard highlights.

## API

```kotlin
@Composable
fun <T> AdaptiveCarousel(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loop: Boolean = true,
    showControls: Boolean = true,
    showIndicators: Boolean = true,
    emptyContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (item: T, index: Int) -> Unit,
)
```

## Behavior

- `selectedIndex` is clamped safely when it is out of range.
- Empty lists render `emptyContent` or a neutral default message.
- A single item disables previous/next controls.
- `loop = false` keeps navigation at the first/last item.
- Controls use `AdaptiveIconButton` and embedded `AdaptiveIcons`.
- Colors come from `AdaptiveTheme`, including dark mode states.

## Helpers

- `normalizeCarouselIndex(index, itemCount)`
- `nextCarouselIndex(index, itemCount, loop)`
- `previousCarouselIndex(index, itemCount, loop)`

These helpers are pure and covered by tests.
