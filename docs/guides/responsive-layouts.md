# Responsive Layouts Guide

Learn how to build layouts that adapt across screen sizes and platforms using AdaptiveKt.

## Core Concepts

1. **Breakpoints** — Screen size categories (Compact, Medium, Expanded, Large)
2. **AdaptiveInfo** — Current breakpoint and window size
3. **Adaptive Value** — Values that vary by breakpoint
4. **Responsive Composition** — Layouts that recompose when breakpoint changes

## Getting Adaptive Context

Always use `rememberAdaptiveInfo()` at the composition root:

```kotlin
@Composable
fun MyScreen() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    // Use adaptiveInfo throughout this composable and children
    Content(adaptiveInfo = adaptiveInfo)
}
```

## Building Responsive Layouts

### Pattern 1: Conditional Layout

```kotlin
@Composable
fun ProductGrid() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    val columnCount = when (adaptiveInfo.breakpoint) {
        Compact -> 1
        Medium -> 2
        Expanded -> 3
        Large -> 4
    }
    
    AdaptiveGrid(columnCount = columnCount) {
        // Grid items
    }
}
```

### Pattern 2: Adaptive Values

```kotlin
@Composable
fun ResponsiveCard() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    val padding = when (adaptiveInfo.breakpoint) {
        Compact -> 8.dp
        Medium -> 12.dp
        Expanded -> 16.dp
        Large -> 20.dp
    }
    
    val fontSize = when (adaptiveInfo.breakpoint) {
        Compact -> 12.sp
        else -> 14.sp
    }
    
    Box(modifier = Modifier.padding(padding)) {
        Text("Title", fontSize = fontSize)
    }
}
```

### Pattern 3: Visibility Control

```kotlin
@Composable
fun Dashboard() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    Row {
        // Sidebar only on large screens
        if (adaptiveInfo.breakpoint >= Expanded) {
            Sidebar(modifier = Modifier.weight(0.2f))
        }
        
        // Main content fills available space
        MainContent(modifier = Modifier.weight(0.8f))
    }
}
```

### Pattern 4: Stacking Content

```kotlin
@Composable
fun TwoColumnLayout() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    if (adaptiveInfo.breakpoint <= Medium) {
        // Stack vertically on small screens
        Column {
            LeftPanel()
            RightPanel()
        }
    } else {
        // Side-by-side on large screens
        Row {
            LeftPanel(modifier = Modifier.weight(0.5f))
            RightPanel(modifier = Modifier.weight(0.5f))
        }
    }
}
```

## Common Responsive Patterns

### Navigation Switching

```kotlin
@Composable
fun MainApp() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    val navigationMode = when (adaptiveInfo.breakpoint) {
        Compact -> NavigationMode.Drawer
        Medium -> NavigationMode.BottomNav
        else -> NavigationMode.Sidebar
    }
    
    // Use navigationMode to render appropriate navigation
}
```

### Form Layouts

```kotlin
@Composable
fun ResponseForm() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    val columns = when (adaptiveInfo.breakpoint) {
        Compact, Medium -> 1
        else -> 2
    }
    
    val labelPosition = when (adaptiveInfo.breakpoint) {
        Compact -> LabelPosition.Top
        else -> LabelPosition.Left
    }
    
    AdaptiveFormLayout(
        columnCount = columns,
        labelPosition = labelPosition,
    ) {
        // Form fields
    }
}
```

### List vs Grid

```kotlin
@Composable
fun ContentView(items: List<Item>) {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    if (adaptiveInfo.breakpoint == Compact) {
        // List view for phones
        LazyColumn {
            items(items) { item ->
                ListItemView(item)
            }
        }
    } else {
        // Grid view for tablets and up
        AdaptiveGrid(columnCount = 3) {
            items.forEach { item ->
                GridItemView(item)
            }
        }
    }
}
```

## Performance Tips

✅ **Do:**
- Store `adaptiveInfo` at the top level
- Pass it down as a parameter rather than calling `rememberAdaptiveInfo()` multiple times
- Use `when` expressions for breakpoint decisions
- Memoize layout decisions if complex

```kotlin
@Composable
fun Screen() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    // Compute layout decisions once
    val columnCount = remember(adaptiveInfo.breakpoint) {
        when (adaptiveInfo.breakpoint) {
            Compact -> 1
            Medium -> 2
            Expanded -> 3
            Large -> 4
        }
    }
    
    // Use columnCount throughout
}
```

❌ **Don't:**
- Call `rememberAdaptiveInfo()` in every child composable
- Create new layout decision objects on every recomposition
- Hardcode breakpoint values in multiple places

## Testing Responsive Layouts

### Testing Locally

**Desktop:** Resize the window to trigger breakpoint changes.

**Android:** Use Android Studio emulator with different device presets or rotate the device.

**Wasm:** Open developer tools and use responsive design mode to test different screen sizes.

### Breakpoint Testing Checklist

- [ ] Compact (< 600 dp) — Phone portrait
- [ ] Medium (600-840 dp) — Tablet portrait, phone landscape
- [ ] Expanded (840-1200 dp) — Tablet landscape
- [ ] Large (≥ 1200 dp) — Desktop

For each breakpoint:
- [ ] Layout renders correctly
- [ ] Navigation adapts
- [ ] Content is readable
- [ ] Touch targets are adequately sized (48 dp min on touch devices)
- [ ] No overlapping elements

## Related Components

- [Adaptive Breakpoints](../components/core/adaptive-breakpoints.md)
- [Adaptive Container](../components/layout/adaptive-container.md)
- [Adaptive Grid](../components/layout/adaptive-grid.md)
- [Adaptive Navigation Scaffold](../components/navigation/adaptive-navigation-scaffold.md)
- [Adaptive Form Layout](../components/forms/adaptive-form-layout.md)

## See Also

- [Architecture](../../adaptive-kt/ARCHITECTURE.md)
- [Admin Demo](../../admin-demo/) — Real-world responsive layout examples
