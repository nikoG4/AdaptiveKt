# ADR-0007: Data View Table/Card Pattern

## Context
Adaptive data views must display the same dataset as desktop tables and mobile cards.

## Decision
Use a single `AdaptiveDataView` wrapper with separate desktop table and mobile card render paths, driven by the current breakpoint.

## Alternatives considered

- Requiring applications to choose between separate `AdaptiveTable` and `AdaptiveCardList` components.
- Building a generic list API with manual switching.

## Consequences

- The public API is simpler for common list/detail use cases.
- The component can manage state-driven rendering and content slots.
- Future advanced features can be layered on top without changing the basic contract.
