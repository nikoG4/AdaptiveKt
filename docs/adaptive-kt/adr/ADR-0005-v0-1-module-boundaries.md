# ADR-0005: v0.1 Module Boundaries

## Context
The implementation plan needs clear module boundaries to avoid scope creep.

## Decision
Define separate modules for the minimum viable adaptive stack:

- `adaptive-core`
- `adaptive-layout`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`
- `adaptive-feedback`
- `admin-demo`

## Alternatives considered

- Merging navigation and layout into a single module.
- Including actions and overlays in v0.1.

## Consequences

- Responsibilities are clear for implementation and dependency management.
- The initial product is smaller and easier to verify.
- Future modules can be added without affecting core contracts.
