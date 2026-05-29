# AdaptiveGrid

Purpose: place content in a 12-column responsive grid.

Use it for dashboards, form sections, and card layouts.

Primary API: `AdaptiveGrid(columns, horizontalGap, verticalGap) { item(span) { ... } }`.

Simple example:

```kotlin
AdaptiveGrid(columns = 12) {
    item(span = 6) { AdaptiveCard { } }
    item(span = 6) { AdaptiveCard { } }
}
```

Advanced example: choose spans from `rememberAdaptiveInfo()`.

Responsive notes: spans are coerced between 1 and `columns`.

Multiplatform notes: common Compose layout code.

Limitations: it is a layout primitive, not a virtualized grid.
