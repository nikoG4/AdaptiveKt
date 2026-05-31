# iOS and macOS Publishing Validation

AdaptiveKt library modules declare Apple targets:

- `iosX64`
- `iosArm64`
- `iosSimulatorArm64`

These targets must be validated on macOS. Windows can list some iOS tasks, but Kotlin/Native Apple targets are disabled there and cannot prove that iOS artifacts compile or publish correctly.

## What Windows Validates

Windows local publishing currently validates:

- Kotlin Multiplatform root metadata publications.
- JVM publications.
- Android release publications.
- Wasm JS publications.
- External consumer smoke tests for JVM and Wasm.

Windows does not validate iOS artifact generation.

## macOS Commands

Run these commands on macOS:

```bash
./gradlew build --console=plain --stacktrace
./gradlew publishAllPublicationsToLocalTestRepository --console=plain --stacktrace
```

The second command publishes to:

```text
build/local-maven/
```

This is a local dry-run repository only. It does not publish to Maven Central, does not use secrets, does not sign artifacts, does not create a release, and does not create a tag.

For PUBLISH-1B, conditional signing and a manual publish workflow are prepared, but remote publishing remains disabled until the Maven Central namespace and secrets are confirmed.

## Expected iOS Artifacts

After local publishing on macOS, inspect:

```bash
find build/local-maven/io/github/nikog4/adaptivekt -maxdepth 1 -type d | sort
```

Each publishable module should include iOS platform artifact directories, for example:

```text
adaptive-core-iosarm64
adaptive-core-iossimulatorarm64
adaptive-core-iosx64
```

The same pattern is expected for:

- `adaptive-components`
- `adaptive-layout`
- `adaptive-feedback`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`

## Manual GitHub Workflow

`.github/workflows/publishing-validation.yml` provides a safe manual validation workflow.

It:

- runs only through `workflow_dispatch`;
- uses `macos-latest`;
- uses JDK 17;
- runs the full build;
- publishes only to `build/local-maven`;
- verifies expected iOS artifact directories;
- uploads `build/local-maven` as a workflow artifact for inspection.

It does not:

- use secrets;
- publish to Maven Central;
- publish remote snapshots;
- sign artifacts;
- create a release;
- create a tag;
- run on `push`;
- run on `pull_request`.

## Maven Central Follow-Up

Real Maven Central publishing remains a later phase. It still needs:

- confirmed namespace ownership;
- conditional signing;
- secure GitHub Secrets;
- final release/tag strategy;
- a separate manual publishing workflow;
- Central Portal validation.
