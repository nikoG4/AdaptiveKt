# AdaptiveAnchoredDropdownMenu

The `AdaptiveAnchoredDropdownMenu` provides a popup-based menu that anchors itself to a trigger (e.g., a button). It is designed to be multiplatform-ready and serves as the foundation for future components like `AdaptiveSelect`.

## API

```kotlin
@Composable
fun AdaptiveAnchoredDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    matchAnchorWidth: Boolean = false,
    maxHeight: Dp = 320.dp,
    placement: AdaptiveDropdownPlacement = AdaptiveDropdownPlacement.BottomStart,
    anchor: @Composable (
        expanded: Boolean,
        toggle: () -> Unit
    ) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
)
```

### Parameters

- **expanded**: Whether the menu is currently visible.
- **onExpandedChange**: Callback when the expanded state should change (e.g., on dismiss or toggle).
- **enabled**: If false, the toggle action provided to the anchor will be a no-op.
- **matchAnchorWidth**: If true, the menu width will match the measured width of the anchor.
- **maxHeight**: Limits the height of the menu; content becomes scrollable if it exceeds this.
- **placement**: Where to place the menu relative to the anchor (default: `BottomStart`).
- **anchor**: Composable slot for the trigger element. It receives the current state and a `toggle` function.
- **content**: The menu items (usually `AdaptiveMenuItem`).

## Examples

### Basic Anchored Menu

```kotlin
var expanded by remember { mutableStateOf(false) }

AdaptiveAnchoredDropdownMenu(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    anchor = { isExpanded, toggle ->
        AdaptiveButton(
            text = if (isExpanded) "Close" else "Open Menu",
            onClick = toggle
        )
    }
) {
    AdaptiveMenuItem("Option 1", onClick = { expanded = false })
    AdaptiveMenuItem("Option 2", onClick = { expanded = false })
}
```

### Match Anchor Width

Useful for "Select" style dropdowns where the menu should span the same width as the input field.

```kotlin
AdaptiveAnchoredDropdownMenu(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    matchAnchorWidth = true,
    anchor = { _, toggle ->
        AdaptiveButton("Full Width Trigger", modifier = Modifier.fillMaxWidth(), onClick = toggle)
    }
) {
    AdaptiveMenuItem("Item A", onClick = { ... })
}
```

## Multiplatform Support

- **JVM/Desktop**: Uses standard `Popup` with `focusable = true`.
- **Wasm/Web**: Uses Compose `Popup`. Compiled and verified via smoke screenshots.
- **Android/iOS**: Uses standard `Popup` behavior.

## Implementation Details

- **Positioning**: Uses `PopupPositionProvider` (via internal `AdaptiveDropdownPositionProvider`) to calculate the optimal position, avoiding horizontal and vertical screen overflow. The anchor width is also measured via `onGloballyPositioned` to support `matchAnchorWidth`.
- **Styling**: Uses `AdaptiveSurface` defaults (White background, 1dp border, `AdaptiveTokens.Radius.Large`).
- **Scrolling**: The menu content is wrapped in a `Column` with `verticalScroll` enabled by default if it exceeds `maxHeight`.

## Difference from AdaptiveDropdownMenu

`AdaptiveDropdownMenu` is a **simple inline panel**. It does not use `Popup` and must be placed manually in the layout. It is mostly used for simple "expandable" sections or when a real popup is not desired.

`AdaptiveAnchoredDropdownMenu` is a **real popup**. It overlays other content and is automatically positioned relative to its anchor.
