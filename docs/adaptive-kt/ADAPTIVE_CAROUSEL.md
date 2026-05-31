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
    transition: AdaptiveCarouselTransition = AdaptiveCarouselTransition.Slide,
    animationDurationMillis: Int = 240,
    emptyContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (item: T, index: Int) -> Unit,
)
```

`AdaptiveCarouselTransition` values:

- `Slide` - default. Moves forward from the right and backward from the left.
- `Fade` - crossfades between items.
- `Scale` - subtle fade/scale transition.
- `None` - disables item transition animation.

## Behavior

- `selectedIndex` is clamped safely when it is out of range.
- Empty lists render `emptyContent` or a neutral default message.
- A single item disables previous/next controls.
- `loop = false` keeps navigation at the first/last item.
- Controls use `AdaptiveIconButton` and embedded `AdaptiveIcons`.
- Colors come from `AdaptiveTheme`, including dark mode states.
- Transitions run in `commonMain` with Compose Multiplatform animation APIs and are safe for Desktop and Wasm.
- No autoplay is included yet; the component remains caller-controlled.

## Helpers

- `normalizeCarouselIndex(index, itemCount)`
- `nextCarouselIndex(index, itemCount, loop)`
- `previousCarouselIndex(index, itemCount, loop)`
- `carouselSlideDirection(previousIndex, nextIndex, itemCount, loop)`

These helpers are pure and covered by tests.
