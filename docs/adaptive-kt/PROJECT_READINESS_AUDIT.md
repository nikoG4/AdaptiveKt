# Project Readiness Audit

Date: 2026-05-30
Task: PUBLISH-0A correction of AUDIT-1
Status: Corrected

## Summary

This document corrects the previous AUDIT-1 report before Maven publishing work begins. AdaptiveKt is in good source-first shape, and PUBLISH-0B has added local dry-run publishing. It is not ready for Maven Central publishing yet.

The most important correction is architectural:

`admin-demo` and `docs-site` must remain included in `settings.gradle.kts`. They are needed for demo builds, docs-site generation, visual verification, and GitHub Pages. They should be excluded only from future Maven publishing configuration.

## Current Repository State

Confirmed in PUBLISH-0A:

- `.github/workflows/ci.yml` exists.
- `.github/workflows/pages.yml` exists.
- `settings.gradle.kts` includes all library modules plus `:admin-demo` and `:docs-site`.
- `docs/adaptive-kt/PROJECT_READINESS_AUDIT.md` exists and was corrected by this task.
- `LICENSE` was missing before PUBLISH-0A.
- `CHANGELOG.md` was missing before PUBLISH-0A.
- PUBLISH-0B defines `GROUP` and `VERSION_NAME` in `gradle.properties`.
- PUBLISH-0B configures local `maven-publish` dry-run only for library modules.

## Module Classification

### Maven-Publishable Library Modules

These modules are intended candidates for future Maven publication:

- `:adaptive-core`
- `:adaptive-components`
- `:adaptive-layout`
- `:adaptive-feedback`
- `:adaptive-navigation`
- `:adaptive-forms`
- `:adaptive-data`

### Non-Publishable Project Modules

These modules must stay in the Gradle build but should not receive Maven publishing configuration:

- `:admin-demo`
- `:docs-site`

Do not remove `:admin-demo` or `:docs-site` from `settings.gradle.kts` as part of publishing readiness.

## Corrected Findings

### 1. admin-demo/docs-site Exclusion

Incorrect previous recommendation:

> Exclude `admin-demo` and `docs-site` from `settings.gradle.kts`.

Correct recommendation:

> Keep `admin-demo` and `docs-site` in `settings.gradle.kts`, but do not apply `maven-publish`, signing, or Maven Central release configuration to those modules.

Reason:

- `:admin-demo` validates desktop and Wasm demo behavior.
- `:docs-site` produces the GitHub Pages artifact.
- Capture and link-check tooling depend on these modules.

### 2. Version Strategy

Incorrect previous framing:

> Missing `version.gradle.kts` is P0.

Correct framing:

> AdaptiveKt needs one documented version source before publishing.

Acceptable strategies include:

- `gradle.properties`
- root `build.gradle.kts`
- convention plugin
- dedicated `version.gradle.kts`

`version.gradle.kts` is one valid option, not a requirement by itself.

### 3. GitHub Actions

Incorrect previous finding:

> No GitHub Actions workflow exists.

Correct finding:

> GitHub Actions workflows exist: `.github/workflows/ci.yml` and `.github/workflows/pages.yml`.

A future publishing workflow is still missing, but basic CI/Pages automation exists.

## Maven Readiness Status

AdaptiveKt is not ready for Maven Central publishing until signing, final artifact validation, and a manual release workflow are implemented and verified.

Completed locally:

- License file and README license alignment.
- Single version strategy in `gradle.properties`.
- Dry-run coordinates for library modules.
- Publishing configuration only for publishable library modules.
- Basic POM metadata.
- Local dry-run verification to `build/local-maven`.

Remaining blockers:

- Confirm final Maven Central namespace and version.
- Validate iOS publications on macOS.
- Confirm source/documentation artifacts required by the chosen release path.
- Configure signing.
- Configure secrets in GitHub Actions or local release process.
- Add manual release workflow only after local publishing dry-run passes.

## Proposed Coordinates

These are active local dry-run coordinates, not published Maven Central coordinates:

- groupId: `io.github.nikog4.adaptivekt`
- version: `0.1.0-alpha01`

Proposed artifacts:

- `adaptive-core`
- `adaptive-components`
- `adaptive-layout`
- `adaptive-feedback`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`

## Publishing Guardrails

Future publishing configuration should:

- Apply only to library modules.
- Never publish `:admin-demo`.
- Never publish `:docs-site`.
- Avoid local machine paths.
- Avoid hardcoded secrets.
- Keep Kamel/Ktor demo-only.
- Keep Robot/AWT/Playwright tooling out of library artifacts.
- Avoid creating a release or tag until dry-run publishing is verified.

## Verification In PUBLISH-0A/PUBLISH-0B

Required verification commands:

```powershell
.\tools\check-dev-environment.ps1
.\gradlew.bat build --console=plain --stacktrace
.\tools\prepare-pages-site.ps1
.\tools\check-site-links.ps1
.\gradlew.bat publishAllPublicationsToLocalTestRepository --console=plain --stacktrace
```

Results are recorded in `PROGRESS_LOG.md`.

## Verdict

PUBLISH-0B adds local publishing readiness only. No Maven Central publishing, remote snapshot publishing, signing, release, tag, or push was performed.
