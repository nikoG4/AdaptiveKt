# Feedback States

Purpose: consistent loading, empty, and error states.

Use `LoadingState`, `EmptyState`, and `ErrorState` in data and workflow screens.

Primary API: `LoadingState`, `EmptyState`, `ErrorState`, and `FeedbackStateLayout`.

Simple example:

```kotlin
EmptyState(title = "No invoices", description = "Create an invoice to get started.")
```

Advanced example: use `ErrorState` with a retry action.

Responsive notes: keep titles short on compact screens.

Multiplatform notes: common Compose implementation.

Limitations: app-specific retry and loading behavior stays outside the component.
