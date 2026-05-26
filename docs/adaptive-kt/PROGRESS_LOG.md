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
