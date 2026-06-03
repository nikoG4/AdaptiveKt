# AdaptiveKt Alpha Readiness

## Current status
- docs-site status: OK (Successfully built, web captures updated, parameter tables synced)
- admin-demo status: OK (Successfully built, web captures updated)
- local publishing status: OK (Dry-run publishing to local Maven repository succeeds)
- consumer smoke status: OK (Local consumer project successfully compiles against local artifacts)

## Public API naming
- feedback components prefixed: `AdaptiveEmptyState`, `AdaptiveErrorState`, `AdaptiveLoadingState`
- form validation types prefixed: `AdaptiveValidationMessage`, `AdaptiveValidationMessageType`
- remaining intentionally unprefixed DSL types:
  - `FieldSpan`
  - `LabelPosition`

## API/docs contract
- components checked: Prioritized components across core, components, feedback, layout, data, forms, and navigation were audited.
- docs-site tables synced: Parameter tables in `SiteComponentsPage.kt` have been updated to perfectly match real API signatures (including fixes for `AdaptiveDataView`, `AdaptiveNavigationScaffold`, `AdaptiveNavigationTree`, `AdaptiveAccordion`, and `AdaptiveDialog`).
- markdown docs synced: All `.md` files have been checked to ensure no un-prefixed legacy API names exist.

## Verification matrix
The following verification suite was executed successfully:
- `.\tools\check-dev-environment.ps1` -> Passed
- `.\gradlew.bat build` -> Passed
- `.\gradlew.bat publishAllPublicationsToLocalTestRepository` -> Passed
- `.\tools\verify-local-publishing-consumer.ps1` -> Passed
- `.\tools\prepare-pages-site.ps1` -> Passed
- `.\tools\check-site-links.ps1` -> Passed
- `.\tools\capture-docs-site-web.ps1 -SkipBuild` -> Passed
- `.\tools\capture-admin-demo-web.ps1` -> Passed

## Remaining before Maven Central
- Sonatype namespace verification
- Maven Central token
- PGP signing key
- GitHub Secrets
- manual publish-release only after explicit approval

## Known non-blocking items
- modifier placement audit pending
- deeper accessibility audit pending
- keyboard navigation improvements pending
- global docs search pending
