# Feedback States

Purpose: consistent loading, empty, and error states.

Use `AdaptiveLoadingState`, `AdaptiveEmptyState`, and `AdaptiveErrorState` in data and workflow screens.

Primary API: `AdaptiveLoadingState`, `AdaptiveEmptyState`, `AdaptiveErrorState`, and the internal shared `FeedbackStateLayout`.

Simple example:

```kotlin
AdaptiveEmptyState(title = "No invoices", description = "Create an invoice to get started.")
```

Animated loading example:

```kotlin
AdaptiveLoadingState(
    message = "Loading activity",
    indicatorStyle = AdaptiveLoadingIndicatorStyle.Dots,
)
```

Loading indicator styles:

- `Spinner` (default)
- `Dots`
- `Pulse`

Advanced example: use `AdaptiveErrorState` with a retry action.

Responsive notes: keep titles short on compact screens.

Multiplatform notes: common Compose implementation. Loading animation uses Compose Multiplatform animation primitives and remains safe for Desktop and Wasm.

Limitations: app-specific retry and loading behavior stays outside the component.
