# ADR-0010: Navigation Selection State Owned by App

## Context
The adaptive navigation components must render selection state without owning route strings or navigation stack semantics.

## Decision
Keep navigation selection state in app-owned state, exposing `selectedItemId: String?` and `onItemSelected: (String) -> Unit` in `AdaptiveNavigationScaffold` and child navigation components.

## Alternatives considered

- Let the scaffold infer selection from route strings or destination metadata.
- Own a routing model inside the library.
- Expose a more complex navigation state object.

## Consequences

- Apps retain full control over navigation state and can integrate with their preferred routing solution.
- The library remains simple and routing-agnostic.
- Child navigation components become controlled components that require `selectedItemId`.
