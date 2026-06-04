# Governance

AdaptiveKt is currently a maintainer-led open-source project.

## Maintainer

- Niko Ovelar (`nikoG4`)

## Decision Making

The maintainer makes final decisions on scope, API shape, releases, and project direction. Community feedback is welcome through issues and pull requests.

For significant changes, the project should prefer an issue or proposal before implementation. Significant changes include:

- Breaking public API changes.
- New modules or artifact structure changes.
- New external dependencies.
- Platform target changes.
- Major visual or behavioral redesigns.

## Compatibility

AdaptiveKt is alpha-stage. APIs may change before stable releases, but changes should still be documented and justified. Once the project approaches beta/stable, breaking changes should use a clearer deprecation and migration process.

## Releases

Releases are expected to be published from `main` after validation. Tags and GitHub releases should be created only after Maven Central publication succeeds.
