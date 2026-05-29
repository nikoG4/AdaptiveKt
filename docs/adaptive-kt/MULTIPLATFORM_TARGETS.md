# Multiplatform Targets

PR P0/P1 added JVM, Android, and iOS target declarations to the AdaptiveKt library modules. PR U0/U1 upgraded the toolchain and enabled Wasm targets. PR P2 added a basic Wasm browser target to `:admin-demo`. PR P3 adds Playwright-based visual smoke testing for the Wasm demo.

## Targets By Module

| Module | JVM/Desktop | Android | iOS X64 | iOS Arm64 | iOS Simulator Arm64 | Wasm |
|---|---:|---:|---:|---:|---:|---:|
| `:adaptive-core` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:adaptive-components` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:adaptive-layout` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:adaptive-feedback` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:adaptive-navigation` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:adaptive-forms` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:adaptive-data` | Yes | Yes | Declared | Declared | Declared | Yes |
| `:admin-demo` | Yes | No | No | No | No | Yes |

## Verification Tooling

Verification depends on the target platform:

- **Desktop (JVM)**: Uses AWT `Robot` via `tools/capture-admin-demo.ps1`.
- **Web (Wasm)**: Uses `Playwright` via `tools/capture-admin-demo-web.ps1`.
- **Docs Site (Wasm)**: Uses `Playwright` via `tools/capture-docs-site-web.ps1` against `site-dist/`.

## Source Set Policy

Library code remains in `commonMain` unless platform-specific code becomes necessary.

Expected source-set shape:

- `commonMain`: UI primitives, tokens, layout, data/forms/navigation/feedback behavior.
- `commonTest`: pure tests.
- `jvmTest`: existing JUnit test wiring.
- `androidMain`, `iosMain`, `wasmJsMain`: only when platform-specific behavior is required.

## Known Limitations

- **iOS**: Needs macOS CI or local macOS verification.
- **Android**: Requires a local Android SDK configured in `local.properties`.
- **Wasm**: Uses local image fallbacks; remote image loading via Kamel is currently JVM-only.
- **Visual Regression**: Tooling currently captures smoke screenshots for manual review; automated pixel-perfect diffing is not yet implemented.

## Docs Site

`:docs-site` is a Compose Multiplatform/Wasm application used for the public landing and live component catalog.

- It depends on AdaptiveKt library modules.
- It does not depend on `:admin-demo`.
- It does not use Kamel, Robot, AWT, Playwright, or Material 3.
- The GitHub Pages artifact places docs-site at the root and the admin demo at `/demo/app/`.
- Direct Pages routes are generated for `/components/`, `/docs/`, and `/demo/`.
