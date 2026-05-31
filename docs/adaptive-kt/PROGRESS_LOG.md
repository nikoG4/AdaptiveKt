UX-FIX-1 - Mobile Demo Scroll and Sidebar Navigation Polish
===========================================================

Status: Completed

Date: 2026-05-31

Summary:
- Fixed mobile Wasm admin-demo scrolling by making the demo content vertically scrollable and relaxing the Wasm root overflow lock.
- Changed default navigation rows from large card-like items to a compact pill/list style.
- Added `AdaptiveNavigationItemStyle` with `Pill`, `Card`, and `Minimal` variants.
- Added `AdaptiveNavigationDensity` with `Comfortable` and `Compact` variants.
- Kept the previous card navigation treatment as an opt-in style.
- Updated the UI Kit navigation tree showcase with pill, card, and minimal examples.

Constraints preserved:
- No Maven Central publishing.
- No release, tag, or secrets.
- No Material 3 or external dependencies.
- No API removals or destructive changes.
- No generated outputs committed.

PUBLISH-1B - Conditional Signing and Manual Publish Workflow Prepared
==================================================================

Status: Completed

Date: 2026-05-31

Summary:
- Added conditional Gradle signing in library modules using `signingInMemoryKey` and `signingInMemoryKeyPassword`.
- Verified that local build and `publishAllPublicationsToLocalTestRepository` continue to pass without signing secrets.
- Added a manual GitHub Actions workflow at `.github/workflows/publish-release.yml` that is `workflow_dispatch` only and explicitly disables remote publishing.
- Documented future secrets, workflow behavior, and PUBLISH-2 requirements in `docs/publishing/SIGNING_AND_RELEASE_WORKFLOW.md`.
- Preserved no-release, no-tag, no-secrets, no-remote-publish constraints.

Constraints preserved:
- No Maven Central publishing.
- No release, tag, or secrets.
- No Material 3 or external dependencies.
- No API removals or destructive changes.
- No generated outputs committed.

UI-ANIM-1 - Carousel and Feedback Loading Animations
====================================================

Status: Completed

Date: 2026-05-31

Summary:
- Added animated transitions to `AdaptiveCarousel` with `Slide`, `Fade`, `Scale`, and `None` modes.
- Added `animationDurationMillis` while keeping existing carousel calls source-compatible.
- Added pure carousel direction helper coverage for normal and looped navigation.
- Upgraded `LoadingState` with animated `Spinner`, `Dots`, and `Pulse` indicator styles.
- Expanded admin-demo UI Kit and Web/Desktop capture routes with a focused `components-feedback` screen.
- Refreshed docs-site examples for animated carousel and loading states.

Constraints preserved:
- No Maven Central publishing.
- No release, tag, or secrets.
- No Material 3 or external dependencies.
- No autoplay.
- No Kamel, Robot, or Playwright in library modules.

UI-NAV-1 - Carousel and Hierarchical Navigation
===============================================

Status: Completed

Date: 2026-05-30

Summary:
- Confirmed PUBLISH-1A manual macOS publishing validation passed before starting UI work.
- Added `AdaptiveCarousel` to `:adaptive-components` with pure index helpers and tests.
- Added `AdaptiveNavigationTree` to `:adaptive-navigation` as a controlled hierarchy primitive.
- Expanded the admin-demo UI Kit and capture routes with carousel and navigation tree sections.
- Improved the MultiSelect showcase into a people picker using existing `optionContent` and `chipContent` slots, with no API change.
- Reworked several hover/selected colors to use `AdaptiveTheme` tokens instead of light-only interaction colors.

Constraints preserved:
- No Maven Central publishing.
- No release, tag, or secrets.
- No Material 3 or external dependencies.
- No Kamel in library modules.
- No public API breakage.

T3 - Dark Mode
==============

Status: Completed

Date: 2026-05-29

Summary:
- Added `AdaptiveColorSchemes.defaultDark()` to `:adaptive-core`.
- Kept `AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultDark())` as the public opt-in API.
- Added light/dark toggles to `:docs-site` and `:admin-demo`.
- Added Wasm query parameter support for deterministic dark captures.
- Extended Web capture tooling with docs-site and admin-demo dark smoke screenshots.

Constraints preserved:
- No platform presets.
- No Material 3 or external dependencies.
- No public API changes beyond adding the dark scheme.
- No broad visual redesign.

T2 - Module Theme Token Migration
=================================

Status: Completed

Date: 2026-05-29

Summary:
- Migrated `:adaptive-feedback` internals to `AdaptiveTheme` colors/shapes.
- Migrated `:adaptive-forms` section, label, validation, and compact action colors/typography to theme tokens.
- Migrated `:adaptive-data` table, mobile-card metadata, status badge shell, overflow menu shell, and affordance colors to theme tokens.
- Migrated `:adaptive-navigation` shell, selected state, top bar, drawer overlay, border, glyph, and text colors to theme tokens.

Constraints preserved:
- No public API changes.
- No dark mode or platform presets.
- No external dependencies or Material 3.
- No DataView/navigation behavior changes.

T1 - AdaptiveTheme Foundation
=============================

Status: Completed

Date: 2026-05-29

Summary:
- Added `AdaptiveTheme` to `:adaptive-core`.
- Added `AdaptiveColorScheme`, `AdaptiveColorSchemes.defaultLight()`, `AdaptiveShapeScheme`, `AdaptiveTypography`, and `AdaptiveStateScheme`.
- Migrated safe `:adaptive-components` base primitives to read theme colors/shapes while keeping the default light look close to the previous design.
- Wrapped `:admin-demo` and `:docs-site` roots with `AdaptiveTheme`.
- Documented the T2/T3 theme migration path.

Constraints preserved:
- No dark mode.
- No platform presets.
- No external dependencies or Material 3.
- No public component API changes beyond adding the theme API.

ENV-1 - Java/JDK Environment Guardrails
=======================================

Status: Completed

Date: 2026-05-29

Summary:
- Documented that AdaptiveKt local and CI builds are validated with JDK 17.
- Added `tools/check-dev-environment.ps1` to diagnose `JAVA_HOME`, `java -version`, local `local.properties` presence, and Android SDK configuration.
- Added `docs/development/setup.md` with Windows PowerShell JDK 17 setup guidance.
- Linked setup guidance from README, development docs, and GitHub Pages docs.

Notes:
- No local JDK path was committed as Gradle configuration.
- `org.gradle.java.home` was not added to `gradle.properties`.
- The script only reports environment state and does not modify the system.

PR C10 - AdaptiveMultiSelect
============================

Status: Completed

Date: 2026-05-29

Summary:
- Added `AdaptiveMultiSelect<T>` to `:adaptive-components`.
- Reused `AdaptiveAnchoredDropdownMenu`, `AdaptiveChip`, `AdaptiveSearchField`, `AdaptiveMenuItem`-style rows, and `AdaptiveIcons`.
- Added local filtering, selected chip overflow, clear-all, disabled state, validation text, custom option rows, and custom chip content.
- Added focused admin-demo routes `components-multiselects` and `components-multiselects-open`.
- Added a live `AdaptiveMultiSelect` example to `:docs-site`.
- Added component docs and API notes.

Verification:
- `:adaptive-components:build` passes locally with JDK 17.
- `:adaptive-components:compileKotlinWasmJs` passes.
- `:admin-demo:build` passes.
- `:admin-demo:wasmJsBrowserDistribution` passes.
- `:docs-site:wasmJsBrowserDistribution` passes.
- Full `build` passes.
- `tools/prepare-pages-site.ps1` passes.
- `tools/check-site-links.ps1` passes.
- Desktop component captures pass, including `components-multiselects` and `components-multiselects-open`.
- Admin-demo Web/Wasm captures pass, including `components-multiselects` and `components-multiselects-open`.
- Docs-site Web captures pass.

Constraints preserved:
- No external dependencies.
- No Material 3.
- No dark mode or theme foundation.
- No existing `AdaptiveSelect` API changes.
- No Maven publication or release.

R0/SITE-3 - Publication Audit, Docs-Site Captures, Link Check, Pages Hardening
=============================================================================

Status: Completed

Date: 2026-05-28

Summary:
- Added Playwright smoke screenshots for the Compose/Wasm docs-site.
- Added generated-site link checking for `site-dist/`.
- Added `tools/serve-site-dist.ps1` for local Pages preview.
- Hardened docs-site route handling for `/components/`, `/docs/`, and `/demo/`.
- Fixed docs-site Wasm viewport HTML so content renders correctly inside the Compose canvas.
- Hardened admin-demo web capture script to choose an available local port and fail on capture errors.
- Updated GitHub Pages and CI workflows with explicit Android SDK setup.
- Added pre-push publication audit documentation.

Verification:
- `:docs-site:wasmJsBrowserDistribution` passes.
- `:admin-demo:wasmJsBrowserDistribution` passes.
- `:adaptive-components:build` passes.
- `:adaptive-components:compileKotlinWasmJs` passes.
- `:admin-demo:build` passes.
- Full `build` passes.
- Docs-site Playwright captures pass.
- Admin-demo Playwright captures pass.
- `tools/check-site-links.ps1` passes.

Constraints preserved:
- No push, release, or Maven publication.
- No MultiSelect, dark mode, or theme foundation.
- No public API changes to AdaptiveKt components.
- Kamel/Ktor remain demo-only JVM dependencies.
- Robot/AWT and Playwright remain tooling/platform-specific.

PR C2.3 - Accessibility Semantics for AdaptiveIcons
============================================

Status: Completed

Objective:
- Expose contentDescription from each icon API to Modifier.semantics for accessibility.
- Maintain the existing Canvas implementation without visual changes.
- Preserve null as-is for decorative icons (no semantics needed).

Files modified:
- adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/icons/AdaptiveIcons.kt
- docs/adaptive-kt/ADAPTIVE_FUNCTIONAL_ICONS.md
- docs/adaptive-kt/PROGRESS_LOG.md

Implementation:
- Added imports: androidx.compose.ui.semantics.contentDescription, androidx.compose.ui.semantics.semantics
- Created internal helper logic in FunctionalIcon for applying semantics conditionally:
  - If contentDescription is null -> return modifier unchanged (decorative icon)
  - If contentDescription is not null -> apply modifier.semantics { this.contentDescription = contentDescription }
- All 8 icons (Close, ChevronDown, ChevronRight, ChevronLeft, Plus, MoreVertical, Search, Check) now support accessibility semantics via the shared FunctionalIcon implementation.

Code pattern applied:
```kotlin
val semanticsModifier = if (contentDescription == null) {
    modifier
} else {
    modifier.semantics {
        this.contentDescription = contentDescription
    }
}

Canvas(modifier = semanticsModifier.size(size)) { ... }
```

Commands executed:
- .\gradlew.bat :adaptive-components:jvmTest -> BUILD SUCCESSFUL
- .\gradlew.bat :adaptive-data:jvmTest -> BUILD SUCCESSFUL
- .\gradlew.bat :admin-demo:build -> BUILD SUCCESSFUL
- .\gradlew.bat build -> BUILD SUCCESSFUL

Build Results:
✓ adaptive-components compiles and passes its JVM tests
✓ adaptive-data compiles and passes its JVM tests
✓ admin-demo builds successfully
✓ Full project build passes
✓ No out-of-scope changes or dependencies added

Constraints preserved:
- No Material 3.
- No external dependencies.
- No icon pack.
- No Web, JS, or Wasm target changes.
- No AdaptiveSelect.
- No dark mode.
- No broad refactor.

Accessibility Impact:
- Screen readers now announce the purpose of icons that have contentDescription provided.
- Decorative icons (null contentDescription) remain without semantics to avoid redundant announcements.
- API surface remains unchanged - existing callers can now optionally pass contentDescription.

PR D1 Rescue - Remote Images In Admin Demo Only
===============================================

Status: Completed

Objective:
- Add remote employee avatars and product thumbnails to `:admin-demo` only.
- Keep AdaptiveKt library modules independent from image loading libraries.

Rescue findings:
- Git exists, but the working tree includes many generated build/capture changes from prior work.
- No `PLAN.md`, `ADMIN_DEMO_REMOTE_IMAGES.md`, or `AdminDemoRemoteImages.kt` was present at rescue start.
- Qwen/Gemma had already added `avatarUrl` to `Employee` and `thumbnailUrl` to `Product`.
- Mock data already contained the requested Random User and Picsum URLs.
- `:admin-demo:build` passed before D1 implementation continued.

Dependency:
- Added `media.kamel:kamel-image:0.7.3` to `admin-demo` `commonMain`.
- Added `io.ktor:ktor-client-cio:2.3.4` to `admin-demo` `jvmMain` so Kamel has a JVM HTTP engine at runtime.
- No other module depends on Kamel or Ktor image loading.

Implementation:
- Created `DemoRemoteAvatar` and `DemoRemoteThumbnail` in `admin-demo` UI helpers.
- Employees now use `DemoRemoteAvatar` in the media column.
- Products now use `DemoRemoteThumbnail` in the media column.
- Loading/failure/null URLs fall back to local generated avatar/thumbnail placeholders.
- Images use `ContentScale.Crop`, clipping, and subtle borders.

Files modified:
- admin-demo/build.gradle.kts
- admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoScreens.kt
- admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoRemoteImages.kt
- docs/adaptive-kt/ADMIN_DEMO.md
- docs/adaptive-kt/ADMIN_DEMO_REMOTE_IMAGES.md
- docs/adaptive-kt/NEXT_WORK_QUEUE.md
- docs/adaptive-kt/PROGRESS_LOG.md

Commands executed:
- git status --short -> repository exists; output heavily includes generated build/capture files.
- Get-ChildItem -Recurse -Filter PLAN.md -> no files found.
- Get-ChildItem -Recurse -Filter ADMIN_DEMO_REMOTE_IMAGES.md -> no files found before D1 docs creation.
- Get-ChildItem -Recurse -Filter AdminDemoRemoteImages.kt -> no files found before helper creation.
- .\gradlew.bat :admin-demo:build --console=plain --stacktrace -> BUILD SUCCESSFUL before implementation.
- .\gradlew.bat :admin-demo:build --console=plain --stacktrace -> BUILD SUCCESSFUL after implementation.
- .\gradlew.bat build --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\tools\capture-admin-demo.ps1 -> SUCCESS, 48 screenshots.

Visual outputs:
- Captures: build/visual-captures
- Report: build/visual-captures/visual-capture-report.md
- ZIP: build/adaptivekt-admin-demo-visual-captures.zip

Visual review:
- `employees-compact-420x900.png` and `employees-large-1440x900.png` show remote circular avatars.
- `products-compact-420x900.png` and `products-large-1440x900.png` show remote rounded thumbnails.
- Compact cards keep media placement and hierarchy.
- Desktop tables remain aligned.

Notes:
- Capture runs emit SLF4J no-binding warnings from Kamel/Ktor logging, but the process succeeds and screenshots are written.
- Remote image loading is demo-only in PR D1. AdaptiveKt core/components remain independent from image loading libraries.

Constraints preserved:
- No public API change in `adaptive-components`.
- No public API change in `adaptive-data`.
- No changes to `adaptive-navigation`, `adaptive-forms`, or `adaptive-feedback`.
- No Material 3.
- No Web target.
- No icon packs.
- No AdaptiveSelect.
- No dark mode.

PR C3 - Adaptive Forms Component Migration
==========================================

Status: Completed

Objective:
- Migrate safe duplicated visual primitives inside `:adaptive-forms` to `:adaptive-components`.
- Preserve the existing public forms API and responsive behavior.

Audit findings:
- `AdaptiveFormLayout` does not generate action buttons; actions are caller-provided composable slots.
- Form fields are caller-provided slots, so replacing them with `AdaptiveTextField` would change composition behavior.
- Sections use local heading/field layout; wrapping them in cards or surfaces would alter the current Settings layout.
- A local section divider was the safest duplicated primitive to migrate.

Dependency:
- Added `implementation(project(":adaptive-components"))` to `adaptive-forms`.
- Existing `:adaptive-layout` dependency remains because `AdaptiveFormLayout` uses `AdaptiveGrid`.
- No dependency cycle was introduced.

Implementation:
- Replaced the local divider `Box` in form sections with `AdaptiveDivider`.
- Kept action slots, field slots, validation rendering, section layout, and sticky compact actions unchanged.

Public API:
- No public API changed.
- Preserved `AdaptiveFormLayout`, `AdaptiveFormScope`, `AdaptiveFormSectionScope`, `AdaptiveFormActionsScope`, `FieldSpan`, `LabelPosition`, and `ValidationMessage`.

Files modified:
- adaptive-forms/build.gradle.kts
- adaptive-forms/src/commonMain/kotlin/io/github/adaptivekt/forms/AdaptiveFormLayout.kt
- docs/adaptive-kt/ADAPTIVE_COMPONENTS_API.md
- docs/adaptive-kt/ADAPTIVE_FORMS_COMPONENT_MIGRATION.md
- docs/adaptive-kt/NEXT_WORK_QUEUE.md
- docs/adaptive-kt/PROGRESS_LOG.md

Commands executed:
- .\gradlew.bat :adaptive-forms:jvmTest --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\gradlew.bat :admin-demo:build --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\gradlew.bat build --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\tools\capture-admin-demo.ps1 -> SUCCESS, 48 screenshots.

Visual outputs:
- Captures: build/visual-captures
- Report: build/visual-captures/visual-capture-report.md
- ZIP: build/adaptivekt-admin-demo-visual-captures.zip

Visual review:
- Settings compact keeps a single column and actions do not cover fields.
- Settings large preserves the current form width/layout.
- The shared divider remains subtle and consistent.
- Components showcase remains stable.

Notes:
- Capture runs still emit SLF4J no-binding warnings from the demo-only Kamel/Ktor image stack, but tasks succeed and screenshots are generated.

Constraints preserved:
- No public API change in `adaptive-forms`.
- No AdaptiveSelect.
- No dark mode.
- No changes to `adaptive-navigation` or `adaptive-feedback`.
- No image-loading changes.
- No Material 3.
- No external dependencies.

PR C4 - Adaptive Feedback Component Migration
=============================================

Status: Completed

Objective:
- Migrate safe duplicated visual primitives inside `:adaptive-feedback` to `:adaptive-components`.
- Preserve the existing public feedback API.

Audit findings:
- `EmptyState`, `LoadingState`, and `ErrorState` share an internal `FeedbackStateLayout`.
- `action` and `retryAction` are caller-provided composable slots, so they should not be replaced by `AdaptiveButton`.
- Empty and error states used raw text glyphs by default.
- Loading indicator is local, Foundation-only, and small enough to keep.

Dependency:
- Added `implementation(project(":adaptive-components"))` to `adaptive-feedback`.
- No dependency cycle was introduced.

Implementation:
- `FeedbackStateLayout` now uses `AdaptiveSurface` while preserving centered layout and max width.
- `EmptyState` default glyph now uses `AdaptiveIcons.Search`.
- `ErrorState` default glyph now uses `AdaptiveIcons.Close`.
- Loading indicator, text helper, and action slots remain local/unchanged.

Capture tooling:
- Added focused invoice state capture screens:
  - `invoices-empty`
  - `invoices-loading`
  - `invoices-error`
- The standard capture script now includes those screens for compact, medium, expanded, and large.

Public API:
- No public API changed.
- Preserved `EmptyState`, `LoadingState`, and `ErrorState`.
- `FeedbackStateLayout` remains internal.

Files modified:
- adaptive-feedback/build.gradle.kts
- adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/EmptyState.kt
- adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/ErrorState.kt
- adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/FeedbackStateLayout.kt
- admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoApp.kt
- admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoScreen.kt
- admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoScreens.kt
- tools/capture-admin-demo.ps1
- docs/adaptive-kt/ADAPTIVE_COMPONENTS_API.md
- docs/adaptive-kt/ADAPTIVE_FEEDBACK_COMPONENT_MIGRATION.md
- docs/adaptive-kt/NEXT_WORK_QUEUE.md
- docs/adaptive-kt/PROGRESS_LOG.md

Commands executed:
- .\gradlew.bat :adaptive-feedback:jvmTest --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\gradlew.bat :admin-demo:build --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\gradlew.bat build --console=plain --stacktrace -> BUILD SUCCESSFUL.
- .\tools\capture-admin-demo.ps1 -> SUCCESS, 60 screenshots.

Visual outputs:
- Captures: build/visual-captures
- Report: build/visual-captures/visual-capture-report.md
- ZIP: build/adaptivekt-admin-demo-visual-captures.zip

Visual review:
- `invoices-empty` compact/large shows a shared surface and Search icon.
- `invoices-loading` compact/large remains simple and centered.
- `invoices-error` compact/large shows a shared surface and Close icon.
- Feedback panels keep a reasonable max width on large screens.

Notes:
- Capture runs still emit SLF4J no-binding warnings from the demo-only Kamel/Ktor image stack, but tasks succeed and screenshots are generated.
- Error uses `AdaptiveIcons.Close` because the minimal icon set does not yet include a dedicated warning/error icon.

Constraints preserved:
- No public API change in `adaptive-feedback`.
- No AdaptiveSelect.
- No dark mode.
- No changes to `adaptive-navigation`, `adaptive-forms`, or `adaptive-data`.
- No image-loading changes.
- No Material 3.
- No external dependencies.
## SITE-2 - KMP/Wasm documentation site

- Added `:docs-site` as a Compose Multiplatform/Wasm module.
- Implemented the landing, component catalog, docs page, and demo page with real AdaptiveKt components.
- Kept `:admin-demo` as a separate Wasm application and linked it from the docs site.
- Updated `tools/prepare-pages-site.ps1` so `site-dist/` is rooted at the docs-site distribution and `admin-demo` is copied to `site-dist/demo/app/`.
- Updated GitHub Pages deployment to use the generated `site-dist` artifact.

## AUDIT-1 — Project Readiness Audit
===========================================

Status: Completed

Date: 2026-05-30

Objective:
- Execute a complete repository audit before Maven Central publishing.
- Generate a readiness report with findings, risks, and P0-P3 recommendations.
- Document API inventory, module publicability, and tooling verification.
- **NO code changes, no Gradle modifications, no commits, no push.**

Constraints:
- Do not touch source code of components.
- Do not refactor or fix bugs except minor documentation.
- Do not change APIs.
- Do not change Gradle.
- Do not touch captures.
- Do not create commits or push.
- Max 90 minutes duration.
- Stop any verification stuck >20 minutes.

Phases executed:
1. Estado inicial: `git status --short` (clean), `git diff --stat` (no changes).
2. Auditoría de estructura del repo: 9 módulos auditados (7 públicos, 2 no publicables: admin-demo, docs-site).
3. Auditoría de APIs públicas: 40+ APIs inventariadas (Core: 15, Components: 23, Otros: 10).
4. Auditoría de documentación: README, docs/components/, docs/adaptive-kt/, docs/development/*.md revisados (sin roturas).
5. Auditoría de publicación futura: Verificado que faltaban LICENSE, CHANGELOG y una estrategia única de versionado.
6. Auditoría visual/tooling: 8 scripts de PowerShell documentados y verificados.
7. Verificación ligera: JDK 17 confirmado, check-dev-environment.ps1 ejecutado sin errores.

Hallazgos principales:
- **P0 — Bloqueantes:**
  - ❌ LICENSE file no existía antes de PUBLISH-0A
  - ⚠️ Falta definir una estrategia única de versionado
  - ⚠️ admin-demo/docs-site deben excluirse solo de Maven publishing, no de `settings.gradle.kts`
- **P1 — Importantes:**
  - ✅ GitHub Actions CI/Pages existen
  - ❌ CHANGELOG.md no existía antes de PUBLISH-0A
  - ⚠️ Sin configuración de sources/javadocs
- **P2 — Mejoras:**
  - Documentar iOS targets requieren macOS
  - Agregar coverage thresholds a CI
  - Audit de dependencia de licencias
- **P3 — Nice to have:**
  - Agregar GitHub Actions badge en README
  - Agregar coverage badge

Maven readiness checklist (antes de PUBLISH-0):
- [x] LICENSE — creado en PUBLISH-0A
- [ ] Estrategia única de versionado — pendiente
- [ ] groupId — ⚠️ PENDIENTE (ej. io.github.adaptivekt)
- [ ] maven-publish plugin — ❌ NO CONFIGURADO (propósito de esta tarea)
- [ ] sources/javadocs — ⚠️ PENDIENTE verificar
- [ ] signing — ⚠️ OPCIONAL
- [ ] secrets cleanup — ✅ OK
- [ ] workflow manual — ⚠️ NO EXISTE (P2)
- [ ] dry-run local — ⚠️ NO IMPLEMENTADO
- [ ] release notes (CHANGELOG) — ⚠️ NO EXISTE (P2)
- [ ] tag strategy — ⚠️ NO DOCUMENTADO

Tooling audit:
- ✅ tools/check-dev-environment.ps1 — Validar JDK 17, JAVA_HOME, Android SDK
- ✅ tools/prepare-pages-site.ps1 — Build docs-site + admin-demo para GitHub Pages
- ✅ tools/check-site-links.ps1 — Verificar links internos de site-dist
- ✅ tools/capture-admin-demo.ps1 — Capturas Desktop de admin-demo
- ✅ tools/capture-admin-demo-web.ps1 — Capturas Web con Playwright
- ✅ tools/capture-docs-site-web.ps1 — Capturas Web de docs-site
- ✅ tools/serve-site-dist.ps1 — Servir site-dist localmente

Archivo de auditoría creado:
- docs/adaptive-kt/PROJECT_READINESS_AUDIT.md

Próximos pasos recomendados (orden P0→P3):
1. Crear LICENSE file (Apache 2.0). Hecho en PUBLISH-0A.
2. Crear CHANGELOG.md para release notes. Hecho en PUBLISH-0A.
3. Definir estrategia única de versionado.
4. Configurar Maven publishing solo para módulos publicables.
5. Mantener admin-demo/docs-site en `settings.gradle.kts` y fuera de publishing.

Verificaciones ejecutadas:
- git status/diff (clean)
- java -version (JDK 17.0.11 LTS)
- tools/check-dev-environment.ps1 (PASS)
- Revisión de 9 módulos Gradle (OK)
- Revisión de 40+ APIs públicas (consistentes)
- Revisión de documentación (*.md) (sin roturas)
- Revisión de 8 scripts PowerShell (válidos)

Veredicto: PENDIENTE (requiere correcciones P0 antes de PUBLISH-0)

Reporte completo: docs/adaptive-kt/PROJECT_READINESS_AUDIT.md

PUBLISH-0A - Corrected Maven Publishing Readiness Audit
=======================================================

Status: Completed

Date: 2026-05-30

Objective:
- Correct the AUDIT-1 report before Maven publishing work.
- Keep this as documentation/readiness only.
- Do not publish, release, tag, push, or change component APIs.

Corrections:
- Corrected the recommendation about `:admin-demo` and `:docs-site`.
  They must remain in `settings.gradle.kts` for demos, docs-site, captures, and GitHub Pages.
  They should be excluded only from future Maven publishing configuration.
- Corrected the versioning finding.
  A dedicated `version.gradle.kts` is optional; the real requirement is one documented version strategy.
- Corrected the GitHub Actions finding.
  `.github/workflows/ci.yml` and `.github/workflows/pages.yml` already exist.

Files created:
- `LICENSE`
- `CHANGELOG.md`
- `docs/publishing/MAVEN_CENTRAL_READINESS.md`

Files updated:
- `README.md`
- `docs/adaptive-kt/PROJECT_READINESS_AUDIT.md`
- `docs/adaptive-kt/NEXT_WORK_QUEUE.md`
- `docs/adaptive-kt/PROGRESS_LOG.md`

Constraints preserved:
- No Maven publishing.
- No release.
- No tag.
- No push.
- No component code changes.
- No public API changes.
- No `settings.gradle.kts` changes.

PUBLISH-0B - Local Maven Publishing Dry Run
===========================================

Status: Completed

Date: 2026-05-30

Objective:
- Define a single group/version strategy.
- Configure publishing only for AdaptiveKt library modules.
- Add a local Maven publishing dry-run without remote publication, secrets, release, tag, or push.

Version strategy:
- `GROUP=io.github.nikog4.adaptivekt`
- `VERSION_NAME=0.1.0-alpha01`
- Both are defined once in `gradle.properties`.

Publishing strategy:
- Uses Gradle's built-in `maven-publish` plugin.
- No external publishing plugin was added.
- Local dry-run repository: `build/local-maven`.
- Aggregate task: `publishAllPublicationsToLocalTestRepository`.

Publishable modules:
- `adaptive-core`
- `adaptive-components`
- `adaptive-layout`
- `adaptive-feedback`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`

Non-publishable modules:
- `admin-demo`
- `docs-site`

Notes:
- `admin-demo` and `docs-site` remain in `settings.gradle.kts`.
- They do not receive `maven-publish` configuration.
- Android release variants are enabled for publication on library modules.
- JVM, Android release, Wasm JS, and Kotlin Multiplatform root publications are generated locally on Windows.
- iOS targets remain declared, but Apple target validation/publication must run on macOS.

Files updated:
- `gradle.properties`
- `build.gradle.kts`
- `adaptive-core/build.gradle.kts`
- `adaptive-components/build.gradle.kts`
- `adaptive-layout/build.gradle.kts`
- `adaptive-feedback/build.gradle.kts`
- `adaptive-navigation/build.gradle.kts`
- `adaptive-forms/build.gradle.kts`
- `adaptive-data/build.gradle.kts`
- `README.md`
- `CHANGELOG.md`
- `docs/publishing/MAVEN_CENTRAL_READINESS.md`
- `docs/publishing/LOCAL_PUBLISHING.md`
- `docs/adaptive-kt/NEXT_WORK_QUEUE.md`
- `docs/adaptive-kt/PROGRESS_LOG.md`

Verification:
- `.\gradlew.bat tasks --all --console=plain` passed and showed publish tasks only for library modules.
- `.\gradlew.bat publishAllPublicationsToLocalTestRepository --console=plain --stacktrace` passed.
- `build/local-maven/io/github/nikog4/adaptivekt` contains library artifacts and no `admin-demo` or `docs-site` artifacts.
- Initial full `build` failed with Kotlin compiler OOM; `gradle.properties` was adjusted to `-Xmx4g` and `org.gradle.workers.max=2`.
- `.\gradlew.bat build --console=plain --stacktrace` passed after the memory/paralellism adjustment.

Constraints preserved:
- No Maven Central publication.
- No remote snapshot publication.
- No signing or secrets.
- No release.
- No tag.
- No push.
- No public API changes.
- No component UI changes.
- No `settings.gradle.kts` changes.

PUBLISH-0C - Local Publishing Consumer Smoke Test
=================================================

Status: Completed

Date: 2026-05-30

Objective:
- Verify that artifacts generated in `build/local-maven` can be consumed by an external project.
- Keep verification local only, with no Maven Central publication, remote snapshots, secrets, release, tag, or push.

Implementation:
- Added `tools/verify-local-publishing-consumer.ps1`.
- The script recreates `build/local-consumer-smoke`.
- The smoke project uses `build/local-maven` as a Maven repository.
- The smoke project uses Maven coordinates only, not `project(":...")` dependencies.
- The smoke project does not use `includeBuild`.

Coordinates consumed:
- `io.github.nikog4.adaptivekt:adaptive-core:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-layout:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-feedback:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-navigation:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-forms:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-data:0.1.0-alpha01`

Consumer coverage:
- JVM compile via `compileKotlinJvm`.
- Wasm JS compile via `compileKotlinWasmJs`.
- Representative imports from core, components, layout, feedback, navigation, forms, and data.

Verification:
- `.\gradlew.bat publishAllPublicationsToLocalTestRepository --console=plain --stacktrace` passed.
- `.\tools\verify-local-publishing-consumer.ps1` passed.
- Generated smoke project inspected to confirm it uses `build/local-maven` and Maven coordinates.

Limitations:
- Does not validate iOS consumption because that must run on macOS.
- Does not publish remotely.
- Does not validate signing or Maven Central release requirements.
- Does not run a rendered app; it is a compile smoke test.

Constraints preserved:
- No Maven Central publication.
- No remote snapshot publication.
- No secrets.
- No release.
- No tag.
- No push.
- No public API changes.
- No component code changes.

PUBLISH-1A - macOS/iOS Publishing Validation Plan
=================================================

Status: Completed

Date: 2026-05-30

Objective:
- Prepare safe macOS/iOS validation for local multiplatform publishing.
- Add a manual-only GitHub Actions validation workflow.
- Document signing and remote publishing plans without enabling remote publication.

Publishing task audit:
- Windows exposes local publish tasks for Kotlin Multiplatform metadata, JVM, Android release, and Wasm JS.
- iOS compile tasks are listed, but Kotlin/Native Apple targets are disabled on Windows.
- iOS publication artifacts must be validated on macOS.

Workflow:
- Added `.github/workflows/publishing-validation.yml`.
- The workflow runs only with `workflow_dispatch`.
- It uses `macos-latest` and JDK 17.
- It runs `./gradlew build --console=plain --stacktrace`.
- It runs `./gradlew publishAllPublicationsToLocalTestRepository --console=plain --stacktrace`.
- It lists and uploads `build/local-maven` as a workflow artifact.
- It verifies expected iOS artifact directories for all seven publishable modules.

Signing plan:
- Signing remains disabled for local publishing.
- Future remote publishing should use conditional in-memory signing properties.
- Builds without secrets must continue to pass.
- No keys, tokens, passwords, or credentials were added.

Files created:
- `.github/workflows/publishing-validation.yml`
- `docs/publishing/IOS_MACOS_VALIDATION.md`

Files updated:
- `docs/publishing/MAVEN_CENTRAL_READINESS.md`
- `docs/publishing/LOCAL_PUBLISHING.md`
- `docs/adaptive-kt/NEXT_WORK_QUEUE.md`
- `docs/adaptive-kt/PROGRESS_LOG.md`

Constraints preserved:
- No Maven Central publication.
- No remote snapshot publication.
- No signing.
- No secrets.
- No release.
- No tag.
- No push.
- No component code changes.
- No public API changes.
- No `settings.gradle.kts` changes.
