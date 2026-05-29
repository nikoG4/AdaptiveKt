# AdaptiveBadge

Purpose: small status label.

Use it for state, priority, and categorization.

Primary API: `AdaptiveBadge(text, tone, modifier)`.

Simple example:

```kotlin
AdaptiveBadge("Active", tone = AdaptiveBadgeTone.Success)
```

Advanced example: place badges inside data-view status columns.

Responsive notes: keep badge text short on compact screens.

Multiplatform notes: common Compose text and surface code.

Limitations: badges are text-only.
