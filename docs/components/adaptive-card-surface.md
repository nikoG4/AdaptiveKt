# AdaptiveCard And AdaptiveSurface

Purpose: consistent containers for admin content.

Use `AdaptiveCard` for repeated framed items and `AdaptiveSurface` for neutral grouped panels.

Primary API: `AdaptiveCard(onClick, content)` and `AdaptiveSurface(content)`.

Simple example:

```kotlin
AdaptiveCard { /* content */ }
```

Advanced example: make a card clickable with `onClick`.

Responsive notes: do not nest cards unnecessarily; use grids for placement.

Multiplatform notes: common Compose surfaces.

Limitations: no elevation system yet.
