# ADR-0001: Project Scope

## Context
AdaptiveKt must start as a small, implementable Compose Multiplatform toolkit rather than a broad UI framework.

## Decision
Limit v0.1-alpha to the following modules:

- `adaptive-core`
- `adaptive-layout`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`
- `adaptive-feedback`
- `admin-demo`

Exclude more advanced modules such as `adaptive-actions` and `adaptive-overlays` from the first release.

## Alternatives considered

- Starting with a larger scope including overlays and action surfaces.
- Deferring adaptive navigation to a future release.

## Consequences

- The initial release remains focused, achievable, and testable.
- Future expansion is preserved but not required for v0.1-alpha.
- The architecture and public API can be validated with a small set of core behaviors.
