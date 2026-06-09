# Release Commit 3: Demos and AI Workspace

## Status
- **Staged Files**: `examples/ecommerce-demo/*`, `examples/ai-workspace-demo/*`, `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteDemoPage.kt`, `README.md`.
- **Validation**:
  - `.\gradlew.bat build` (Root)
  - `.\gradlew.bat build` (AI Workspace)
  - `.\gradlew.bat desktopTest` (AI Workspace)
  - `.\gradlew.bat wasmJsBrowserDistribution` (AI Workspace)
  - `check-ai-workspace-layout-guards.ps1` (Passed)
- **Commit Message**: `feat(examples): add AI workspace demo`
- **Result**: Commits cleanly executed. Wasm targets compiled correctly without breaking memory limits.
