# CI Validation Plan

## Updates
Added a new `ai-workspace-demo` job to `.github/workflows/ci.yml`.

## Purpose
This dedicated job isolates the AI Workspace Demo validation from the core library matrix. It ensures the demo can compile and assemble across its key target platforms (JVM Desktop, WebAssembly) safely on typical GitHub Actions runners.

## Validation Steps Executed
1. **Checkout & JDK Setup:** Sets up Java 17 environment.
2. **`./gradlew build`:** Verifies common multiplatform compilation for the demo.
3. **`./gradlew desktopTest`:** Runs the desktop module tests, crucially executing `AiRouteCodecTest` to ensure navigation states serialize properly.
4. **`./gradlew wasmJsBrowserDistribution`:** Executes the full Wasm compiler and Webpack optimizer passes.

## What is Intentionally Not Validated
- **Android Target:** We intentionally bypassed configuring Android inside this job. Because we previously removed the Android plugin and SDK requirements from `ai-workspace-demo/build.gradle.kts` (to enhance cross-platform reliability and avoid `ANDROID_HOME` errors), this job runs successfully on generic Linux runners without downloading the massive Android SDK.
- **iOS Target Artifacts:** While `iosX64` and `iosArm64` source sets exist and compile, full XCFramework generation is not enforced in this Linux job since iOS binaries require a macOS runner.

## Re-Enabling Android in CI
If the Android target is later re-enabled in `build.gradle.kts`, the `ai-workspace-demo` CI job will need the `android-actions/setup-android@v3` step added before the Gradle commands, similar to the main `build` job.
