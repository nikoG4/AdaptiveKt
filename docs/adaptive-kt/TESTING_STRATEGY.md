# AdaptiveKt — Testing Strategy

## Goals

Verify that adaptive behavior is correct, stable, and platform-agnostic, while acknowledging current Compose Multiplatform limitations.

### Canonical breakpoint thresholds

For v0.1-alpha the expected width mapping is:

- `Compact`: width < 600.dp
- `Medium`: 600.dp <= width < 840.dp
- `Expanded`: 840.dp <= width < 1200.dp
- `Large`: width >= 1200.dp

These thresholds are the source of truth for `breakpointForWidth(width: Dp)` tests.

## Test types

### Unit tests

- `adaptiveValue` logic
- adaptive breakpoint mapping from width values
- `AdaptiveVisibility` predicate behavior
- `AdaptiveFormColumns` layout decisions in scoped helpers
- `AdaptiveDataState` state mapping and content selection

### Component contract tests

- verify public API signatures and expected default parameter values
- ensure `AdaptiveGrid` item spans are interpreted correctly in layout metadata
- validate `AdaptiveNavigationScaffold` mode selection rules from breakpoint to navigation UI

### Manual / screenshot validation

Because Compose Multiplatform does not yet support fully reliable cross-platform screenshot testing in a single harness, use manual and platform-specific validation for:

- Desktop window resizing and adaptive mode transitions,
- Web/WASM viewport resizing,
- Android portrait and landscape,
- drawer and bottom navigation behaviors.

### Sample-based validation

- Use `admin-demo` as a living sample.
- Build the demo and verify that the desktop and mobile flows behave as designed.
- Use the demo for exploratory verification of `AdaptiveDataView`, `AdaptiveFormLayout`, and `AdaptiveNavigationScaffold`.

## What to automate in v0.1

- `rememberAdaptiveInfo()` breakpoint mapping from width values.
- `adaptiveValue(...)` resolution across all breakpoint combinations.
- `AdaptiveVisibility` with inclusive/exclusive sets.
- state-driven rendering of `EmptyState`, `LoadingState`, and `ErrorState`.

### adaptiveValue fallback expectations

Tests should cover the exact fallback rules:

- Compact resolves to `compact`
- Medium resolves to `medium` or falls back to `compact`
- Expanded resolves to `expanded` or falls back to `medium` then `compact`
- Large resolves to `large` or falls back to `expanded`, `medium`, then `compact`

## What to validate manually

- actual window measurement behavior on Android, Desktop, and Web/WASM.
- drawer open/close patterns on mobile-sized screens.
- card layout spacing on narrow viewports.
- form label behavior on compact vs expanded breakpoints.

## Command guidance

Suggested Gradle verification commands:

- `./gradlew :adaptive-core:test`
- `./gradlew :adaptive-layout:test`
- `./gradlew :adaptive-navigation:test`
- `./gradlew :adaptive-forms:test`
- `./gradlew :adaptive-data:test`
- `./gradlew :adaptive-feedback:test`
- `./gradlew :admin-demo:run` or platform-specific demo target

## Limitations

- Cross-platform screenshot tests are a future enhancement.
- Web/WASM resizing behavior should be validated manually until the Compose Web test ecosystem matures.
- Some layout assertions may be easier to verify through demo-driven exploratory testing than unit tests.
