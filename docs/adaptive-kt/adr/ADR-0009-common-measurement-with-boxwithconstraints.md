# ADR-0009: Common Measurement Strategy with BoxWithConstraints

## Context
AdaptiveKt needs a reliable commonMain measurement strategy for v0.1-alpha without introducing platform-specific dependencies.

## Decision
Use `BoxWithConstraints` in `commonMain` to derive available width and height for adaptive calculations.

## Alternatives considered

- Platform-specific measurement helpers in Android, Desktop, and Web/WASM.
- A separate common interop layer for window metrics.
- Not providing a measurement strategy in v0.1-alpha.

## Consequences

- The adaptive API remains implementable in common code and avoids platform-only build complexity.
- Width breakpoints can be derived consistently across Compose Multiplatform targets.
- Platform-specific improvement is deferred to later versions, while v0.1-alpha remains focused on API stability.
