# AdaptiveThumbnail

Purpose: product or object thumbnail with fallback label.

Use it for catalogs, media columns, and card previews.

Primary API: `AdaptiveThumbnail(label, modifier, size, tone, content)`.

Simple example:

```kotlin
AdaptiveThumbnail(label = "Invoice")
```

Advanced example: provide platform image content in the content slot.

Responsive notes: pair with `AdaptiveDataMobileRole.Media` for mobile cards.

Multiplatform notes: caller controls remote image loading.

Limitations: no network loading in the library.
