# Platform Readiness Audit

PR P0/P1 audited AdaptiveKt for full Compose Multiplatform readiness and added JVM/Android/iOS target declarations to the library modules. PR U0/U1 upgrades the toolchain and enables Wasm for those library modules.

## Architecture Rule

- AdaptiveKt library modules should be full multiplatform.
- Demo applications may have platform-specific entry points.
- Automatic screenshots are verification tooling, not public API.
- Desktop screenshot tooling may use AWT `Robot` and `ComposeWindow`.
- Future Web/Wasm screenshots should use Playwright or an equivalent browser runner.
- No screenshot tooling belongs in library `commonMain`.

## A. AdaptiveKt Library

All library modules were JVM-only before P0/P1. They now declare:

- `jvm()`
- `androidTarget()`
- `iosX64()`
- `iosArm64()`
- `iosSimulatorArm64()`

`wasmJs { browser() }` is enabled after upgrading to Kotlin `2.1.21` and Compose Multiplatform `1.8.2`.

### Module Audit

| Module | Targets after P0/P1 | Source sets | Dependencies | Portability notes |
|---|---:|---|---|---|
| `:adaptive-core` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | Compose Foundation | No JVM-only API found in `commonMain`. |
| `:adaptive-components` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | `:adaptive-core`, Compose Foundation | Uses Foundation primitives, `BasicTextField`, `Canvas`, and hover interactions. Wasm compiles/tests. |
| `:adaptive-layout` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | `:adaptive-core`, Compose Foundation | Wasm compiles/tests. |
| `:adaptive-feedback` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | `:adaptive-core`, `:adaptive-components`, Compose Foundation | One internal `TextAlign` nullability fix was needed for newer Compose. Wasm compiles. |
| `:adaptive-navigation` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | `:adaptive-core`, `:adaptive-components`, Compose Foundation | Wasm compiles/tests. |
| `:adaptive-forms` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | `:adaptive-core`, `:adaptive-layout`, `:adaptive-components`, Compose Foundation | Wasm compiles/tests. |
| `:adaptive-data` | JVM, Android, iOS declared, Wasm | `commonMain`, `commonTest`, `jvmTest` | `:adaptive-core`, `:adaptive-layout`, `:adaptive-feedback`, `:adaptive-components`, Compose Foundation | Uses Compose `Popup` in `commonMain`; Wasm compiles/tests, but runtime browser behavior still needs P2/P3 validation. |

### Android Readiness

Android library plugin support was added to the library modules with:

- `com.android.library`
- `compileSdk = 34`
- `minSdk = 23`
- Java source/target compatibility 17
- `android.useAndroidX=true`

The local machine needed an ignored `local.properties` with the Android SDK path. This is local environment setup and should not be versioned.

### iOS Readiness

iOS targets are declared for every library module. Compose UIKit support required:

```properties
org.jetbrains.compose.experimental.uikit.enabled=true
```

On Windows, Kotlin/Native disables iOS compilation tasks because Apple targets require a macOS host. Gradle reports this as skipped, not as source incompatibility.

### Web/Wasm Readiness

P0/P1 found Wasm blocked with Kotlin `1.9.10` and Compose `1.5.1`. Attempting to add:

```kotlin
@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
wasmJs { browser() }
```

failed during Gradle script compilation with unresolved `ExperimentalWasmDsl`, `wasmJs`, and `browser` references.

U0/U1 resolves that blocker by upgrading to Kotlin `2.1.21`, Compose Multiplatform `1.8.2`, and applying the Kotlin Compose compiler plugin. Wasm targets now compile and run Wasm test tasks for all library modules.

## B. Admin Demo

`:admin-demo` remains JVM/Desktop-only in P0/P1. PR P2 adds a basic Wasm browser target without converting capture tooling or remote image loading into shared/library code.

Current demo source sets:

- `commonMain`
- `commonTest`
- `jvmMain`
- `jvmTest`
- `wasmJsMain`

Demo-only dependencies:

- `media.kamel:kamel-image:0.7.3`
- `io.ktor:ktor-client-cio:2.3.4`

Remote images remain demo-only. Kamel/Ktor were not added to any library module.

In P2, Kamel/Ktor moved to `jvmMain` for the demo. Wasm uses fallback-only avatar and thumbnail helpers so the browser demo can compile without image-loading dependencies.

## C. Capture Tooling

Desktop capture tooling remains under:

- `admin-demo/src/jvmMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoCapture.kt`
- `tools/capture-admin-demo.ps1`

The JVM capture code imports:

- `androidx.compose.ui.awt.ComposeWindow`
- `java.awt.Robot`
- `java.awt.EventQueue`
- `java.awt.Rectangle`
- `java.io.File`

These are correctly scoped to `admin-demo` JVM tooling and do not contaminate library `commonMain`.

## Risks And Recommendations

- iOS should be verified on macOS because Windows skips Apple target compilation.
- `Popup` behavior in `adaptive-data` should be validated in a real browser when admin-demo Wasm starts.
- Demo remote images should stay out of library modules unless a future media abstraction is designed.
- Playwright/browser screenshots should be a later PR, not part of platform target setup.
- P2 validates that the browser target starts and renders a Compose canvas. P3 should add real browser navigation/screenshot coverage.
