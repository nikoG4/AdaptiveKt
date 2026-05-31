# Feedback States

Purpose: consistent loading, empty, and error states.

Use `LoadingState`, `EmptyState`, and `ErrorState` in data and workflow screens.

Primary API: `LoadingState`, `EmptyState`, `ErrorState`, and the internal shared `FeedbackStateLayout`.

Simple example:

```kotlin
EmptyState(title = "No invoices", description = "Create an invoice to get started.")
```

Animated loading example:

```kotlin
LoadingState(
    message = "Loading activity",
    indicatorStyle = AdaptiveLoadingIndicatorStyle.Dots,
)
```

Loading indicator styles:

- `Spinner` (default)
- `Dots`
- `Pulse`

Advanced example: use `ErrorState` with a retry action.

Responsive notes: keep titles short on compact screens.

Multiplatform notes: common Compose implementation. Loading animation uses Compose Multiplatform animation primitives and remains safe for Desktop and Wasm.

Limitations: app-specific retry and loading behavior stays outside the component.
