# Maven Central Readiness

Status: local dry-run publishing is configured. Conditional signing and a guarded Central Portal workflow are prepared. No Maven Central upload, remote snapshot publishing, release, or tag has been executed.

## Strategy Chosen

AdaptiveKt uses Gradle's built-in `maven-publish` plugin for generating Kotlin Multiplatform Maven publications.

Reason:

- It avoids adding another external Gradle plugin before the first local publishing proof.
- It works well enough for a local Kotlin Multiplatform publishing dry-run.
- It keeps publication generation explicit and avoids switching to a larger publishing plugin before the first alpha.

For Central Portal, the release workflow prepares a Maven repository layout bundle from `build/local-maven` and uploads that bundle to the official Central Publisher API only when the manual guard is satisfied.

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

## Maven Central Requirements

Current status:

- Namespace confirmed: `io.github.nikog4`.
- Group ID configured: `io.github.nikog4.adaptivekt`.
- Version configured: `0.1.0-alpha01`.
- POM metadata configured for name, description, URL, license, developer, and SCM.
- Sources jars are generated.
- Placeholder javadoc jars are generated for the alpha release.
- Gradle generates checksums in the local Maven repository.
- Signing is conditional and activates when signing Gradle properties are supplied.
- GitHub Secrets are configured for signing and Central Portal credentials.

Remaining release gates:

- Validate signed artifacts in the manual release workflow.
- Validate iOS artifacts on `macos-latest`.
- Run the release workflow in dry-run mode.
- Run the guarded Central Portal upload only after explicit human approval.
- Create tags/releases only after the Central Portal deployment has been accepted and published.

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

## Signing

Signing is configured with Gradle's Signing plugin for publishable modules only.

The build reads:

```text
signingInMemoryKey
signingInMemoryKeyPassword
```

Local builds without those properties continue to work and produce unsigned local artifacts. The release workflow maps GitHub Secrets to the corresponding `ORG_GRADLE_PROJECT_...` environment variables so Gradle signs all publications during the guarded release pipeline.

No signing keys, passwords, tokens, or credentials should be committed to the repository.

## Central Portal Workflow

`.github/workflows/publish-release.yml` is manual-only and has two modes.

Dry run:

- `dryRunOnly=true`
- `confirm=DRY_RUN`
- Builds the project.
- Publishes signed artifacts to `build/local-maven`.
- Runs the consumer smoke test.
- Prepares and validates a Central Portal bundle.
- Uploads the bundle as a GitHub Actions artifact.
- Does not contact Maven Central or Central Portal.

Guarded Central Portal upload:

- `dryRunOnly=false`
- `confirm=PUBLISH`
- Uses the official Publisher API endpoint `https://central.sonatype.com/api/v1/publisher/upload`.
- Authenticates with a `Bearer` token derived from the Central Portal user token username and password.
- Uploads the generated bundle with `publishingType=USER_MANAGED`.
- Does not auto-publish to Maven Central; the deployment must be reviewed and published in Central Portal after validation.

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
- Local Windows publishing does not produce `.asc` files because signing secrets are intentionally not present locally.
- The alpha release uses placeholder javadoc jars instead of Dokka-generated API documentation.
- The Central Portal workflow uses `USER_MANAGED` deployments; it does not automatically publish after validation.

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
