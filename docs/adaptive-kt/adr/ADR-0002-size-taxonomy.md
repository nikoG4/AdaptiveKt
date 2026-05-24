# ADR-0002: Size Taxonomy

## Context
AdaptiveKt requires a unified breakpoint taxonomy for responsive APIs.

## Decision
Use `Compact / Medium / Expanded / Large` as the official taxonomy for v0.1-alpha.

## Alternatives considered

- `Mobile / Tablet / Desktop / Wide`
- Compose-only `Compact / Medium / Expanded`

## Consequences

- The taxonomy is consistent across all modules.
- `Large` covers ultra-wide desktop surfaces beyond typical expanded layouts.
- The API remains compatible with Compose adaptive semantics while adding a fourth explicit wide breakpoint.
