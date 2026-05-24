# ADR-0008: Responsive Form Layout Contract

## Context
Forms must adapt to compact and expanded screens while preserving developer control over field spans.

## Decision
Provide a layout contract with `AdaptiveFormLayout`, `FormField`, `FieldSpan`, and `AdaptiveFormColumns`. Labels may default to `Top` on compact screens and allow `Inline` on expanded screens.

## Alternatives considered

- Using a purely automatic form layout with no explicit span control.
- Tying form layout directly to screen width values only.

## Consequences

- Apps can express both mobile-friendly one-column forms and desktop multi-column forms.
- The API remains declarative and predictable.
- The library can evolve to more advanced form features without breaking basic field layout.
