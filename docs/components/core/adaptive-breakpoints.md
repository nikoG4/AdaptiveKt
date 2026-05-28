# Adaptive Breakpoints

## Purpose

Breakpoints categorize screen sizes into responsive design categories. AdaptiveKt uses four standard breakpoints that map to device types.

## When to Use

Always use breakpoints when:
- Building layouts that need to adapt to different screen sizes
- Deciding whether to show/hide UI elements
- Choosing between layout strategies (single column vs multi-column)
- Selecting navigation modes (drawer vs sidebar vs rail)

## Breakpoint Categories

| Breakpoint | Size Range | Device Type | Columns | Navigation |
|------------|-----------|------------|---------|-----------|
| **Compact** | < 600 dp | Phone (portrait) | 1 | Drawer |
| **Medium** | 600-840 dp | Tablet (portrait) / Phone (landscape) | 2 | Bottom Nav |
| **Expanded** | 840-1200 dp | Tablet (landscape) | 3 | Rail |
| **Large** | ≥ 1200 dp | Desktop | 4+ | Sidebar |

## API

### Accessing Current Breakpoint

```kotlin
val adaptiveInfo = rememberAdaptiveInfo()
val currentBreakpoint = adaptiveInfo.breakpoint

when (currentBreakpoint) {
    Compact -> println("Phone")
    Medium -> println("Tablet Portrait")
    Expanded -> println("Tablet Landscape")
    Large -> println("Desktop")
}
```

### Using in Layouts

```kotlin
@Composable
fun ResponsiveColumn() {
    val columnCount = when (rememberAdaptiveInfo().breakpoint) {
        Compact -> 1
        Medium -> 2
        Expanded -> 3
        Large -> 4
    }
    
    AdaptiveGrid(
        columnCount = columnCount,
    ) {
        // Grid items
    }
}
```

### Responsive Padding

```kotlin
@Composable
fun ResponsivePadding() {
    val padding = when (rememberAdaptiveInfo().breakpoint) {
        Compact -> 8.dp
        Medium -> 12.dp
        Expanded -> 16.dp
        Large -> 20.dp
    }
    
    Box(modifier = Modifier.padding(padding)) {
        // Content
    }
}
```

## Behavior Across Platforms

| Platform | Breakpoint Detection | Notes |
|----------|---------------------|-------|
| **JVM/Desktop** | Window width | Resizable; breakpoint updates on resize |
| **Android** | Screen width (dp) | Responsive to orientation changes |
| **iOS** | Screen width (dp) | Declared target; iOS validation pending |
| **Wasm** | Window width | Resizable in browser |

## Best Practices

✅ **Do:**
- Check breakpoint once at composition and pass values down
- Use `rememberAdaptiveInfo()` at the top level of your screen
- Create reusable layout decision logic
- Test at each breakpoint during development

❌ **Don't:**
- Check breakpoint multiple times in a single composition
- Hardcode values without considering breakpoint context
- Assume desktop sizes in mobile-first layout

## Advanced Usage

### Storing Breakpoint Decisions

```kotlin
@Composable
fun MyScreen() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    // Make layout decisions once
    val isCompact = adaptiveInfo.breakpoint <= Compact
    val isLargeScreen = adaptiveInfo.breakpoint >= Expanded
    
    // Pass them down
    Content(isCompact = isCompact, isLargeScreen = isLargeScreen)
}
```

### Responsive Value Type

For complex responsive behavior, use `AdaptiveValue<T>`:

```kotlin
val columnCount = AdaptiveValue(
    compact = 1,
    medium = 2,
    expanded = 3,
    large = 4,
)

val columns = columnCount.valueAt(adaptiveInfo.breakpoint)
```

## Related Components

- [`AdaptiveInfo`](adaptive-window-info.md) — Full responsive context
- [`AdaptiveGrid`](../layout/adaptive-grid.md) — Breakpoint-aware grid
- [`AdaptiveNavigationScaffold`](../navigation/adaptive-navigation-scaffold.md) — Breakpoint-driven navigation

## Limitations

- Breakpoints are fixed; custom ranges not yet supported
- Orientation detection is basic (width-only, not device rotation events)
- iOS sizing requires native validation

## See Also

- [Responsive Layout Guide](../../guides/responsive-layouts.md)
- [Architecture](../../adaptive-kt/ARCHITECTURE.md)
