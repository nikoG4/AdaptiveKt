# Maven Central Readiness

Status: local dry-run publishing is configured. No Maven Central publishing, remote snapshot publishing, release, tag, signing, or secrets are configured.

## Strategy Chosen

PUBLISH-0B uses Gradle's built-in `maven-publish` plugin.

Reason:

- It avoids adding another external Gradle plugin before the first local publishing proof.
- It works well enough for a local Kotlin Multiplatform publishing dry-run.
- It keeps Maven Central-specific signing/secrets/workflow setup for a later dedicated publishing PR.

Versioning is centralized in `gradle.properties`:

```properties
GROUP=io.github.nikog4.adaptivekt
VERSION_NAME=0.1.0-alpha01
```

## Publishable Modules

Maven publishing applies only to AdaptiveKt library modules:

- `adaptive-core`
- `adaptive-components`
- `adaptive-layout`
- `adaptive-feedback`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`

## Non-Publishable Modules

These modules must remain in `settings.gradle.kts`, but do not receive Maven publishing configuration:

- `admin-demo`
- `docs-site`

Reason:

- `admin-demo` is the visual/demo app and verification target.
- `docs-site` builds the GitHub Pages documentation artifact.
- Both are part of the repository build and release verification, but neither is a library artifact.

## Proposed Coordinates

These coordinates are configured for local dry-run publishing:

| Module | Proposed groupId | Proposed artifactId |
|---|---|---|
| `:adaptive-core` | `io.github.nikog4.adaptivekt` | `adaptive-core` |
| `:adaptive-components` | `io.github.nikog4.adaptivekt` | `adaptive-components` |
| `:adaptive-layout` | `io.github.nikog4.adaptivekt` | `adaptive-layout` |
| `:adaptive-feedback` | `io.github.nikog4.adaptivekt` | `adaptive-feedback` |
| `:adaptive-navigation` | `io.github.nikog4.adaptivekt` | `adaptive-navigation` |
| `:adaptive-forms` | `io.github.nikog4.adaptivekt` | `adaptive-forms` |
| `:adaptive-data` | `io.github.nikog4.adaptivekt` | `adaptive-data` |

Initial version:

```text
0.1.0-alpha01
```

## Real Blockers

- Confirm whether `io.github.nikog4.adaptivekt` is the final Maven Central namespace.
- Confirm final release version before tagging.
- Confirm generated artifacts on macOS for iOS publications.
- Decide whether documentation artifacts need Dokka or another docs jar strategy.
- Configure signing.
- Configure required secrets.
- Add a manual GitHub Actions publishing workflow after local dry run succeeds.
- Perform Sonatype Central Portal validation.

## Platform Publishing Validation

Windows currently validates local Maven publishing for:

- Kotlin Multiplatform root metadata.
- JVM.
- Android release.
- Wasm JS.

iOS publications require macOS. Apple targets are declared, but Windows disables Kotlin/Native Apple targets, so Windows cannot prove `iosX64`, `iosArm64`, or `iosSimulatorArm64` artifact generation.

Manual macOS validation is documented in `docs/publishing/IOS_MACOS_VALIDATION.md`.

The repository also contains `.github/workflows/publishing-validation.yml`, a safe manual-only validation workflow. It runs on `macos-latest`, performs a full build, publishes only to `build/local-maven`, verifies expected iOS artifact directories, and uploads the local Maven repository as an inspection artifact.

That workflow does not use secrets, sign artifacts, publish remotely, create tags, or create releases.

## Signing Plan

Signing is not configured or required for local publishing.

PUBLISH-1B prepares conditional signing in the Gradle build so local builds continue to pass when signing secrets are absent.
Local publishing to `build/local-maven` remains valid without signing, and publication tasks do not require signing unless `signingInMemoryKey` and `signingInMemoryKeyPassword` are provided.

Future secret-backed properties:

```text
ORG_GRADLE_PROJECT_signingInMemoryKey
ORG_GRADLE_PROJECT_signingInMemoryKeyPassword
```

Future Maven Central or Central Portal credentials should also be supplied through GitHub Secrets, for example:

```text
ORG_GRADLE_PROJECT_mavenCentralUsername
ORG_GRADLE_PROJECT_mavenCentralPassword
```

No signing keys, passwords, tokens, or credentials should be committed to the repository.

## Future Remote Publishing Workflow

Remote publishing should be added in a later PUBLISH-1B/PUBLISH-2 phase.

It should:

- be manual-only;
- require a confirmed Maven Central namespace;
- require signing secrets;
- require publishing credentials;
- use an explicit release/tag strategy;
- avoid running automatically on push.

This PUBLISH-1A phase intentionally does not configure an active remote Maven repository.

## Local Dry Run

Run:

```powershell
.\gradlew.bat publishAllPublicationsToLocalTestRepository --console=plain --stacktrace
```

Output:

```text
build/local-maven/
```

The aggregate task publishes all library module publications to the local `LocalTest` Maven repository.

Observed local artifacts include KMP root publications plus platform publications for JVM, Android release, and Wasm JS, for example:

- `io.github.nikog4.adaptivekt:adaptive-core:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-core-jvm:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-core-android:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-core-wasm-js:0.1.0-alpha01`

The same pattern is generated for the other publishable modules.

## Current Limitations

- iOS targets are declared, but local Windows builds disable Apple targets. iOS publication validation must run on macOS.
- No signing is configured yet.
- No remote Maven Central repository is configured yet.
- No remote publish workflow is configured yet.
- No Dokka/docs jar strategy is configured yet.

## Consumer Smoke Test

PUBLISH-0C adds a reproducible local consumer smoke test:

```powershell
.\tools\verify-local-publishing-consumer.ps1
```

The script creates `build/local-consumer-smoke`, configures `build/local-maven` as a Maven repository, and compiles a standalone Kotlin Multiplatform consumer against the published Maven coordinates.

It validates:

- dependency resolution from `build/local-maven`;
- Maven coordinates for all seven library modules;
- JVM compilation;
- Wasm JS compilation;
- representative imports from core, components, layout, feedback, navigation, forms, and data.

It does not validate:

- Maven Central upload;
- signing;
- secrets;
- iOS consumption on macOS;
- runtime rendering of the consumer app.

## Guardrails

- Do not remove `:admin-demo` or `:docs-site` from `settings.gradle.kts`.
- Do not publish demo or docs-site artifacts.
- Do not hardcode secrets.
- Do not configure remote publishing until signing and release workflow guardrails are ready.
- Do not tag or release until a dry run passes.
- Keep Kamel/Ktor demo-only.
- Keep Robot/AWT/Playwright verification tooling out of library artifacts.
