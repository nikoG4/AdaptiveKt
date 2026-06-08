# Adaptive AI Workspace Demo

This is a complete frontend-only demo application for AdaptiveKt. 
It showcases AdaptiveKt as a serious cross-platform UI library for complex SaaS, admin, and internal tools.

## Features

- **Frontend-only**: No real backend, no API keys required, and no network dependencies.
- **Mock Data**: Uses local state to mock AI streaming, conversations, tools, and evaluations.
- **Adaptive Primitives Demonstrated**:
  - `AdaptiveApp` for global layout initialization.
  - `AdaptiveNavigationScaffold` for responsive navigation (bottom bar on compact, sidebar on expanded).
  - `AdaptiveNavigator` with hash routing.
  - `AdaptiveScrollablePage`, `AdaptiveColumnPage`, and `AdaptiveSection` for readable, bounded page contents.
  - `AdaptiveGrid` and `AdaptiveContainer` for content reflow without explicit column counting.
  - `AdaptiveListDetailScaffold` for chat views, prompt library, knowledge base, evaluations, and tools.

## Supported Targets

This demo relies strictly on `commonMain` UI code using Compose Multiplatform.

### Desktop

```bash
# Run the Desktop app
./gradlew desktopRun
```

### Web (Wasm)
To run the WebAssembly version in a local development server:
```bash
./gradlew wasmJsBrowserDevelopmentRun
```
To generate the production Wasm distribution:
```bash
./gradlew wasmJsBrowserDistribution
```

## Live Demo
The Wasm distribution of this demo is continuously deployed to GitHub Pages and can be accessed at:
[AI Workspace (Live Demo)](https://nikog4.github.io/AdaptiveKt/examples/ai-workspace/)

## Verification Scripts

To ensure the demo continues to compile cleanly and adheres to layout constraints, we provide the following project-level verification script. You can run this from the repository root:

```powershell
# Windows
.\scripts\verify-ai-workspace.ps1

# macOS/Linux
./scripts/verify-ai-workspace.sh
```

This script will:
1. Run a clean build (no Android SDK required).
2. Run `desktopTest` (which includes routing and layout component logic validation via `AiRouteCodecTest`).
3. Run the memory-intensive `wasmJsBrowserDistribution`.
4. Run layout constraint guards (`check-ai-workspace-layout-guards.ps1`).

## Target Limitations
- The Android target configuration has been deliberately removed from this demo to facilitate reliable and fast cross-platform compilation in standard CI environments. The demo focuses on Desktop and Web (Wasm) scenarios.
- The `wasmJsBrowserDistribution` compilation is memory-intensive. `gradle.properties` has been tuned to use `-Xmx4g` for Gradle and `-Xmx3g` for the Kotlin daemon. Ensure your machine or CI runner has adequate memory available.

## Known Limitations

- All interactions are mocked.
- Navigation history relies on hash routing.
- This is a UI showcase, not a production AI client. Do not use this codebase to handle real API keys or PII, as no secure storage mechanisms are implemented.
