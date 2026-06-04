# Contributing To AdaptiveKt

Thanks for considering a contribution. AdaptiveKt is alpha-stage, so feedback about API shape, documentation clarity, multiplatform behavior, and real usage friction is especially valuable.

## Development Requirements

- JDK 17.
- Git.
- Gradle wrapper from this repository.
- A Kotlin/Compose Multiplatform-capable IDE is recommended.
- Android SDK if you want to build Android targets.
- macOS is required to fully validate iOS targets.

Use the Gradle wrapper instead of a globally installed Gradle.

```powershell
.\tools\check-dev-environment.ps1
.\gradlew.bat build --console=plain --stacktrace
```

On Unix-like systems:

```bash
./gradlew build --console=plain --stacktrace
```

## Project Shape

Library modules:

- `adaptive-core`
- `adaptive-components`
- `adaptive-layout`
- `adaptive-feedback`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`

Demo and documentation modules:

- `admin-demo`
- `docs-site`

The demo and docs modules are not published Maven artifacts.

## Contribution Guardrails

- Prefer commonMain-first APIs.
- Do not introduce platform-specific UI unless the platform boundary is intentional and documented.
- Do not add dependencies without a clear reason.
- Do not introduce breaking public API changes without discussion.
- Update docs and examples when adding or changing public APIs.
- Add focused tests for pure helpers and behavior that can be tested without visual tooling.
- For UI changes, include screenshots or describe visual verification.
- Keep generated outputs out of commits.

## API Changes

AdaptiveKt is alpha, but public API still needs care. For changes that affect names, behavior, artifact structure, or migration paths:

1. Open an issue or discussion first.
2. Explain the problem, alternatives, and migration impact.
3. Prefer additive APIs before breaking changes.
4. Document any migration path in README/docs/changelog.

## Running Useful Checks

Common checks:

```powershell
.\gradlew.bat build --console=plain --stacktrace
.\gradlew.bat :adaptive-components:build --console=plain --stacktrace
.\gradlew.bat :admin-demo:wasmJsBrowserDistribution --console=plain --stacktrace
.\gradlew.bat :docs-site:wasmJsBrowserDistribution --console=plain --stacktrace
```

Local publishing smoke test:

```powershell
.\gradlew.bat publishAllPublicationsToLocalTestRepository --console=plain --stacktrace
.\tools\verify-local-publishing-consumer.ps1
```

Docs site:

```powershell
.\tools\prepare-pages-site.ps1
.\tools\check-site-links.ps1
```

## Commit Style

Use concise, conventional-style commit messages when possible:

- `feat: add adaptive component`
- `fix: correct focus state contrast`
- `docs: improve setup guide`
- `ci: adjust validation workflow`

## Reporting Bugs

Please include:

- AdaptiveKt version.
- Module.
- Platform.
- Kotlin and Compose Multiplatform versions.
- Minimal reproduction.
- Expected and actual behavior.

Use the bug report issue template when available.
