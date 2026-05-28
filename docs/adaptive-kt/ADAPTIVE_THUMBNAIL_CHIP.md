# Adaptive Thumbnail And Chip

PR C7 adds two small shared primitives to `:adaptive-components`:

- `AdaptiveThumbnail`
- `AdaptiveChip`

The goal is to replace demo-only media and filter helpers without adding image loading, Material 3, icon packs, or platform target changes.

## AdaptiveThumbnail

`AdaptiveThumbnail` is a Foundation-only thumbnail for products, documents, and media-like items.

It does not load remote URLs. Remote image loading remains demo-only in `:admin-demo`.

```kotlin
AdaptiveThumbnail(label = "AirPods Pro")

AdaptiveThumbnail(
    label = "Router Gigabit",
    image = {
        ProductImage()
    },
)
```

API:

```kotlin
@Composable
fun AdaptiveThumbnail(
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    shape: Shape = RoundedCornerShape(AdaptiveTokens.Radius.Medium),
    image: (@Composable () -> Unit)? = null,
    tone: Color = Color(0xFF64748B),
)
```

The originally requested default shape was `Shape = AdaptiveTokens.Radius.Medium`, but `AdaptiveTokens.Radius.Medium` is a `Dp` token. PR C7 uses `RoundedCornerShape(AdaptiveTokens.Radius.Medium)` so the public parameter is still a real `Shape`.

Fallback label helper:

```kotlin
fun thumbnailLabelFor(text: String): String
```

Rules:

- `"AirPods Pro"` -> `"AP"`
- `"Router Gigabit"` -> `"RG"`
- `"Router"` -> `"R"`
- blank input -> `"?"`
- maximum two characters
- symbols and unusual input should not crash

## AdaptiveChip

`AdaptiveChip` is a pill-shaped primitive for filters, tags, and selected states.

```kotlin
AdaptiveChip(
    text = "In stock",
    selected = true,
    tone = AdaptiveChipTone.Success,
    onClick = ::toggleInStock,
)
```

API:

```kotlin
enum class AdaptiveChipTone {
    Neutral,
    Primary,
    Success,
    Warning,
    Danger,
    Info,
}

@Composable
fun AdaptiveChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    tone: AdaptiveChipTone = AdaptiveChipTone.Neutral,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
)
```

Behavior:

- `onClick == null` renders as a static tag.
- `enabled = false` lowers visual emphasis.
- `selected = true` uses a filled tone.
- icon slots are optional and caller-owned.

## Demo Adoption

PR C7 keeps the demo migration small:

- `DemoThumbnail` delegates to `AdaptiveThumbnail`.
- `DemoToggleChip` delegates to `AdaptiveChip`.
- `DemoRemoteThumbnail` remains demo-only and uses `AdaptiveThumbnail` as fallback.

Kamel remains scoped to `:admin-demo`.

## Showcase And Captures

New focused showcase screens:

- `components-thumbnails`
- `components-chips`

These are included in both the full capture matrix and the components-only capture matrix.

## Not Included

- URL loading in `:adaptive-components`
- Kamel outside `:admin-demo`
- `AdaptiveSelect`
- segmented control API
- dark mode
- Web/Wasm targets
- external dependencies
