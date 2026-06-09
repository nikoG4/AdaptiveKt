# Release Commit 1: Core Adaptive Configuration

## Status
- **Staged Files**: `adaptive-core/*` (Config, LayoutInfo, Breakpoints, tests), `adaptive-core/README.md`, `AdaptiveNavigationMode.kt` (moved into core), `adaptive-navigation/build.gradle.kts`.
- **Validation**:
  - `.\gradlew.bat :adaptive-core:jvmTest` (Passed)
  - `.\gradlew.bat :adaptive-navigation:jvmTest` (Passed)
- **Commit Message**: `feat(core): add adaptive app configuration system`
- **Result**: Successfully committed. The core modules compile successfully, preserving source compatibility via package continuity for the `AdaptiveNavigationMode`.
