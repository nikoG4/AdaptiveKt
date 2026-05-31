# Signing and Release Workflow

## Current state

- Local publishing is validated for AdaptiveKt library modules.
- Consumer smoke test is validated with `tools/verify-local-publishing-consumer.ps1`.
- macOS/iOS validation is documented and prepared in `docs/publishing/IOS_MACOS_VALIDATION.md`.
- Conditional signing is prepared in the Gradle build for publishable modules.
- Remote Maven Central publishing is intentionally disabled for PUBLISH-1B.

## Signing and publishing secrets planned

Future secrets expected for real remote publishing:

- `ORG_GRADLE_PROJECT_signingInMemoryKey`
- `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword`
- `ORG_GRADLE_PROJECT_mavenCentralUsername`
- `ORG_GRADLE_PROJECT_mavenCentralPassword`

These values must not be committed to the repository.

## What must not be done

- Do not commit private keys or `.asc` signature files.
- Do not commit secrets or credentials.
- Do not print secret values in logs.
- Do not publish from automatic push events.
- Do not enable remote Maven Central publishing until PUBLISH-2.

## How the PGP key is generated and used

1. Generate the PGP key outside the repository on a secure machine.
2. Export it in a format compatible with Gradle Signing `useInMemoryPgpKeys`.
3. Store the exported key material in a GitHub Secret, not in source control.
4. Provide the key material through `ORG_GRADLE_PROJECT_signingInMemoryKey`.
5. Provide the key password through `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword`.
6. Never version the key material or passwords in the repository.

## How signing is activated

- The Gradle build reads `signingInMemoryKey` and `signingInMemoryKeyPassword`.
- Signing is enabled only when both properties are present.
- If signing keys are missing, local build and local publishing continue to work.
- The build does not require signing for local `publishAllPublicationsToLocalTestRepository`.

## Manual publish workflow

A new workflow file exists at `.github/workflows/publish-release.yml`.

It is:

- manual-only (`workflow_dispatch` only)
- run on `macos-latest`
- configured with JDK 17
- validating a project build
- publishing all library publications to `build/local-maven`
- running the consumer smoke verification script
- listing generated artifacts
- uploading `build/local-maven` as an inspection artifact
- explicitly disabled for remote publishing

### Workflow inputs

- `version`: requested version to publish
- `confirm`: must be set to `PUBLISH`
- `dryRunOnly`: keeps remote publishing disabled

### What the workflow does not do

- It does not publish to Maven Central.
- It does not create a GitHub release.
- It does not create a Git tag.
- It does not use secrets to publish.
- It does not call a remote publication repository.

## Checklist before first real remote publish

- Confirm the Maven Central/Sonatype namespace.
- Confirm the final `groupId`.
- Configure and validate signing secrets.
- Configure and validate Maven Central credentials.
- Set the final release version.
- Update `CHANGELOG.md`.
- Define the tag and release strategy.
- Confirm `tools/verify-local-publishing-consumer.ps1` passes.
- Confirm `.github/workflows/publishing-validation.yml` passes.
