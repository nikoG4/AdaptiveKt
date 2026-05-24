# ADR-0004: Material 3 Complement, Not Replacement

## Context
AdaptiveKt should work alongside Material 3 rather than replacing it with its own visual system.

## Decision
Design components as structural and behavior-focused primitives that are theme-agnostic and can host Material 3 content.

## Alternatives considered

- Shipping opinionated AdaptiveKt-themed components.
- Implementing a parallel design system for visuals.

## Consequences

- The library remains compatible with existing Material 3 apps.
- Visual styling is delegated to the host application.
- Implementation complexity is reduced by avoiding custom theming.
