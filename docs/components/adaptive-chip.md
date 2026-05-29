# AdaptiveChip

Purpose: compact selectable or informational pill.

Use it for filters, toggles, and quick state selectors.

Primary API: `AdaptiveChip(text, selected, tone, onClick)`.

Simple example:

```kotlin
AdaptiveChip("Open", selected = true, onClick = {})
```

Advanced example: use a row of chips to switch `AdaptiveDataState`.

Responsive notes: wrap chip rows on compact screens.

Multiplatform notes: common Compose implementation.

Limitations: not a multi-select control by itself.
