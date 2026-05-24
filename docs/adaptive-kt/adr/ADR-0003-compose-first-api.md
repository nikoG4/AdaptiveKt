# ADR-0003: Compose-First API

## Context
API design must avoid CSS-like string classes and be idiomatic to Compose.

## Decision
Design all public APIs as composables, use `Modifier`, slots, typed parameters, and data classes rather than style strings.

## Alternatives considered

- Introducing a class-string based styling system.
- Using higher-level view models for layout decisions.

## Consequences

- The API feels natural for Compose developers.
- Clients can compose with existing Material components.
- Implementation stays declarative and extensible.
