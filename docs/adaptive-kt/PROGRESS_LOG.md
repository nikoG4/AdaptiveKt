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
