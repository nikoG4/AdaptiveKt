# Build Architecture Audit

## Project Structure

**Root Project (`adaptive-kt`)**
Includes the following modules:
- `:adaptive-core`
- `:adaptive-components`
- `:adaptive-layout`
- `:adaptive-feedback`
- `:adaptive-navigation`
- `:adaptive-forms`
- `:adaptive-data`
- `:admin-demo`
- `:docs-site`

**Independent Gradle Roots**
There are two independent project roots under the `examples/` directory:
- `examples/ecommerce-demo` (uses `includeBuild("../..")` to depend on root)
- `examples/ai-workspace-demo` (uses `includeBuild("../..")` to depend on root)

## Configured Targets (per Demo)
The independent examples (`ecommerce-demo`, `ai-workspace-demo`) configure the following targets:
- `jvm("desktop")`
- `wasmJs`
- `androidTarget`
- iOS targets (`iosX64`, `iosArm64`, `iosSimulatorArm64`)

## Task Requirements and Safety

**Tasks Requiring Android SDK:**
- Any Android-specific compilation tasks (`compileDebugJavaWithJavac`, `assembleDebug`, etc.).
- The generic `build` lifecycle task on the demo projects, because it attempts to build all configured targets, including `androidTarget`.

**Tasks Requiring Node/Wasm/Webpack:**
- `:wasmJsBrowserDistribution`
- `:wasmJsBrowserDevelopmentRun`
- The Kotlin plugin automatically handles Node/Yarn setup in standard environments, but these are computationally and memory intensive.

**Tasks Safe for Generic CI:**
- Target-specific JVM tasks: `desktopTest`, `:adaptive-core:jvmTest`, `:adaptive-layout:jvmTest`, `:adaptive-navigation:jvmTest`.
- These bypass the `androidTarget` requirement.

**Heavy/Expensive Tasks:**
- `wasmJsBrowserDistribution` (requires significant memory for Wasm compilation and Webpack processing).

## Current Build Failure Causes

1. **Android SDK Location Missing:**
   Running `./gradlew build` in `examples/ai-workspace-demo` fails because `com.android.application` is applied, but the environment (e.g., standard CI or non-Android dev machines) lacks `ANDROID_HOME`.
2. **Wasm Out of Memory (OOM):**
   Running `./gradlew wasmJsBrowserDistribution` in `examples/ai-workspace-demo` runs out of memory (`GC overhead limit exceeded`). The `ai-workspace-demo` root lacks explicit memory allocations in `gradle.properties` (e.g., `org.gradle.jvmargs` and `kotlin.daemon.jvmargs`).
