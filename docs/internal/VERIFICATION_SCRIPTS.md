# Verification Scripts

To ensure continuous reproducibility and consistency without developers needing to memorize all exact Gradle paths and targets, we have introduced project-level verification scripts.

## Core Library Layout & Unit Verification

**Scripts:**
- `scripts/verify-layout.ps1` (Windows)
- `scripts/verify-layout.sh` (macOS/Linux)

**Purpose:** 
Runs a clean build of the core library, executes the JVM unit tests for all core modules, and runs the layout static analysis guards to ensure no demo apps or core modules contain forbidden legacy layout logic (e.g., `BoxWithConstraints`).

**Commands Executed:**
1. `./gradlew clean build`
2. `./gradlew :adaptive-core:jvmTest`
3. `./gradlew :adaptive-layout:jvmTest`
4. `./gradlew :adaptive-navigation:jvmTest`
5. PowerShell/Bash execution of `examples/ai-workspace-demo/scripts/check-ai-workspace-layout-guards.ps1` (or its equivalent shell script if available).

**Prerequisites:**
- JDK 17+
- PowerShell or Bash

## AI Workspace Demo Verification

**Scripts:**
- `scripts/verify-ai-workspace.ps1` (Windows)
- `scripts/verify-ai-workspace.sh` (macOS/Linux)

**Purpose:**
Strictly validates the `examples/ai-workspace-demo` project across its primary supported targets (JVM Desktop and Wasm). Validates that routing and navigation states are stable via `desktopTest` (which includes `AiRouteCodecTest`).

**Commands Executed:**
1. Navigates to `examples/ai-workspace-demo`
2. `./gradlew clean build` (Verifies basic common/desktop structure)
3. `./gradlew desktopTest` (Executes routing and layout component logic tests)
4. `./gradlew wasmJsBrowserDistribution` (Verifies WebAssembly bundle generation and optimization passes)
5. Navigates back and runs `scripts/check-ai-workspace-layout-guards.ps1` (or equivalent shell script) to ensure no manual breakpoints were introduced.

**Prerequisites:**
- JDK 17+
- Adequate memory allocation (minimum 4GB for Gradle daemon, handled automatically by `gradle.properties`).
- Node.js (handled automatically by Kotlin Gradle Plugin).
