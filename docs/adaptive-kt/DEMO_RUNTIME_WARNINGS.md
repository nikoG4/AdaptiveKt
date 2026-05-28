# Demo Runtime Warnings

PR C6 documents runtime warnings observed during admin-demo capture runs. This is informational only; no logging dependencies were added.

## SLF4J No-Binding Warning

The admin demo can print SLF4J no-binding warnings during capture runs because PR D1 uses Kamel/Ktor for demo-only remote images.

Impact:

- Does not fail Gradle builds.
- Does not fail capture generation.
- Does not affect `adaptive-core`, `adaptive-components`, `adaptive-data`, `adaptive-forms`, `adaptive-feedback`, or `adaptive-navigation`.
- Applies only to the `:admin-demo` runtime path.

Current decision:

- Do not add an SLF4J binding in PR C6.
- Keep image loading and any logging cleanup demo-only.
- Revisit in a future D1.1 cleanup if the warnings become disruptive in CI or local capture output.

Related rule:

Remote image loading is demo-only in PR D1. AdaptiveKt core/components remain independent from image loading libraries.

## Wasm Bundle Warnings

PR P2 adds a basic Wasm target to `:admin-demo`. Webpack can report bundle-size warnings for:

- `admin-demo.js`
- the demo Wasm artifact
- `skiko.wasm`

Impact:

- Does not fail `:admin-demo:build`.
- Does not fail `:admin-demo:wasmJsBrowserDistribution`.
- Expected for the first browser smoke target before code splitting or optimization work.

Current decision:

- Do not tune bundle size in P2.
- Keep P2 focused on a working browser entry point.
- Revisit size and browser-performance work after P3 adds Playwright coverage.
