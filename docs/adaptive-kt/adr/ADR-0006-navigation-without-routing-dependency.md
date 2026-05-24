# ADR-0006: Navigation Without Routing Dependency

## Context
AdaptiveNavigationScaffold must support app navigation patterns without coupling to any routing library.

## Decision
Expose navigation items and selection callbacks only. The library does not own navigation state or routing.

## Alternatives considered

- Integrating with a specific navigation library.
- Exposing route strings and path-based navigation helpers.

## Consequences

- AdaptiveKt remains framework-agnostic.
- App authors keep full control of routing.
- The navigation scaffold is easier to adopt in multiple app architectures.
