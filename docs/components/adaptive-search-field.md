# AdaptiveSearchField

Purpose: search-specialized text field with clear behavior.

Use it above data views or inside searchable dropdowns.

Primary API: `AdaptiveSearchField(value, onValueChange, placeholder, onClear, modifier)`.

Simple example:

```kotlin
AdaptiveSearchField(value = query, onValueChange = { query = it }, onClear = { query = "" })
```

Advanced example: filter local lists before passing them to `AdaptiveDataView`.

Responsive notes: place full-width on compact screens.

Multiplatform notes: common Compose code.

Limitations: filtering logic belongs to the caller.
