# Release Commit Grouping

To ensure bisectability and logical progression, the repository changes will be bucketed into the following four distinct commits:

## Commit 1: Core Adaptive Configuration
- **Scope**: Foundational data structures and breakpoints.
- **Files**:
  - `adaptive-core/src/commonMain/kotlin/io/github/adaptivekt/core/*`
  - `adaptive-core/src/commonTest/kotlin/io/github/adaptivekt/core/*`
  - `adaptive-core/README.md`
  - *Move:* `adaptive-navigation/.../AdaptiveNavigationMode.kt` to `adaptive-core/.../AdaptiveNavigationMode.kt`
  - *Deps:* `adaptive-navigation/build.gradle.kts` adding dependency on `:adaptive-core`.
- **Validation**: Independent compilation of `:adaptive-core` and `:adaptive-navigation`.

## Commit 2: Layout Primitives
- **Scope**: Responsive UI wrappers and high-level list/detail scaffolding.
- **Files**:
  - `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/*`
  - `adaptive-layout/src/commonTest/kotlin/io/github/adaptivekt/layout/*`
  - `adaptive-layout/src/jvmTest/kotlin/io/github/adaptivekt/layout/*`
  - `adaptive-layout/README.md`
  - `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/AdaptiveNavigationScaffold.kt`
- **Validation**: Independent compilation of `:adaptive-layout`.

## Commit 3: Demos and AI Workspace
- **Scope**: Application implementation showing standard usage of primitives and elimination of `BoxWithConstraints`.
- **Files**:
  - `examples/ecommerce-demo/*`
  - `examples/ai-workspace-demo/*`
  - `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteDemoPage.kt`
- **Validation**: Successful Desktop and Wasm builds of `ai-workspace-demo` and `ecommerce-demo`.

## Commit 4: Build, CI, Pages, Audits
- **Scope**: CI/CD stability, Powershell validation logic, and extensive project audit notes.
- **Files**:
  - `.github/workflows/ci.yml`
  - `.gitignore`
  - `scripts/*`
  - `tools/prepare-pages-site.ps1`
  - `docs/internal/*`
  - `README.md`
- **Validation**: Validation script pipelines must run clean on root and examples.
