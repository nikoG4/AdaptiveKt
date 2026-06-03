# Signing and Release Workflow

## Current state

- Local publishing is validated for AdaptiveKt library modules.
- Consumer smoke test is validated with `tools/verify-local-publishing-consumer.ps1`.
- macOS/iOS validation is documented and prepared in `docs/publishing/IOS_MACOS_VALIDATION.md`.
- Conditional signing is prepared in the Gradle build for publishable modules.
- The manual release workflow can now prepare a signed Central Portal bundle.
- Remote Central Portal upload is guarded by `dryRunOnly=false` and `confirm=PUBLISH`.
- Maven Central auto-publishing remains disabled; the workflow uploads with `publishingType=USER_MANAGED`.

## Signing and publishing secrets

GitHub Secrets expected by `.github/workflows/publish-release.yml`:

- `SIGNINGINMEMORYKEY`
- `SIGNINGINMEMORYKEYPASSWORD`
- `MAVENCENTRALUSERNAME`
- `MAVENCENTRALPASSWORD`

The workflow maps signing secrets to Gradle properties:

- `ORG_GRADLE_PROJECT_signingInMemoryKey`
- `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword`

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
- The Gradle build can also read `signingInMemoryKeyFile` and load the ASCII-armored key from that temporary file.
- Signing is enabled only when both properties are present.
- If signing keys are missing, local build and local publishing continue to work.
- The build does not require signing for local `publishAllPublicationsToLocalTestRepository`.
- The release workflow normalizes `SIGNINGINMEMORYKEY` before invoking Gradle. It accepts an ASCII-armored private key directly, base64-encoded ASCII armor, or a base64-encoded secret keyring that can be imported and re-exported as ASCII armor in the temporary runner environment.
- Gradle receives the normalized ASCII-armored key via `signingInMemoryKeyFile`, avoiding multi-line secret values in `$GITHUB_ENV`.

## Manual publish workflow

The manual workflow file exists at `.github/workflows/publish-release.yml`.

It is:

- manual-only (`workflow_dispatch` only)
- run on `macos-latest`
- configured with JDK 17
- validating a project build
- publishing all library publications to `build/local-maven`
- running the consumer smoke verification script
- preparing a Central Portal upload bundle
- verifying required signatures, checksums, sources jars, and javadoc jars in that bundle
- uploading `build/local-maven` and the Central Portal bundle as inspection artifacts
- contacting Central Portal only when `dryRunOnly=false` and `confirm=PUBLISH`

### Workflow inputs

- `version`: requested version to publish
- `confirm`: `DRY_RUN` for dry runs, `PUBLISH` for guarded remote upload
- `dryRunOnly`: when `true`, no request is sent to Maven Central or Central Portal

### What the workflow does not do

- It does not publish to Maven Central automatically.
- It does not create a GitHub release.
- It does not create a Git tag.
- It does not run on push or pull request.
- In dry-run mode, it does not call Central Portal.

When explicitly run with `dryRunOnly=false` and `confirm=PUBLISH`, it uploads the signed bundle to the Central Publisher API with `publishingType=USER_MANAGED`. That creates a Central Portal deployment that must still be reviewed and published manually after validation.

## Checklist before first real remote publish

- Confirm the Maven Central/Sonatype namespace.
- Confirm the final `groupId`.
- Validate signing secrets in the dry-run workflow.
- Validate Maven Central credentials through a guarded Central Portal upload.
- Set the final release version.
- Update `CHANGELOG.md`.
- Define the tag and release strategy.
- Confirm `tools/verify-local-publishing-consumer.ps1` passes.
- Confirm `.github/workflows/publishing-validation.yml` passes.
- Confirm `.github/workflows/publish-release.yml` passes in dry-run mode.
