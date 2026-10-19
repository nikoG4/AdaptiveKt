# Roadmap

AdaptiveKt is alpha-stage. This roadmap is intentionally realistic: the project is published, but not yet API-stable.

## Next milestone: 0.2.0-beta01

Prioritize the [beta readiness gates](docs/adaptive-kt/BETA01_READINESS.md), and evolve the
[visual foundations](docs/adaptive-kt/VISUAL_FOUNDATIONS.md) without broadening scope.
Beta is an API and reliability milestone, not a component-count target.

### Visual beta work in progress

A cohesive interaction and elevation pass is being developed on a feature branch.
It must pass component compilation, accessible interaction review, visual captures
and consumer verification before a beta release.

### Beta01 interaction checkpoint

Keyboard and focus reliability for selects/multi-selects is tracked in the
[interaction audit](docs/adaptive-kt/BETA01_INTERACTION_AUDIT.md). The stacked
branch intentionally defers CI until the visual branch is reviewed.

## 0.1 Alpha Hardening

- Document public APIs and current module boundaries.
- Improve accessibility, focus, keyboard, and pointer behavior.
- Expand Desktop and Web/Wasm visual verification.
- Validate Maven Central consumption and release process.
- Collect feedback from real examples and early adopters.

## 0.2 API Stabilization

- Review naming consistency across components, forms, feedback, data, and navigation.
- Add migration notes for any breaking changes.
- Strengthen KDoc coverage for public APIs.
- Improve theme and token documentation.
- Validate iOS artifacts on macOS.

## 0.3 Examples, Docs, And Testing

- Expand admin, productivity, and ecommerce-style examples.
- Improve docs-site component pages with more complete snippets.
- Add more pure tests for behavior helpers.
- Improve screenshot review workflow.
- Add guidance for app-level theming and product branding.

## 1.0 Criteria

AdaptiveKt should not be called stable until:

- Core APIs have documented compatibility expectations.
- Major components have accessibility and keyboard behavior reviewed.
- Supported targets have repeatable validation paths.
- Published artifacts are consistently consumable from Maven Central.
- Examples and docs cover common workflows.
- The project has clear contribution, security, and governance processes.
