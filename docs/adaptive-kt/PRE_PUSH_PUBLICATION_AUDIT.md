# Pre-Push Publication Audit

Date: 2026-05-28

## Build Status

- `:docs-site:wasmJsBrowserDistribution`: passing locally.
- `:admin-demo:wasmJsBrowserDistribution`: passing locally.
- `:adaptive-components:build`: passing locally.
- `:adaptive-components:compileKotlinWasmJs`: passing locally.
- `:admin-demo:build`: passing locally.
- Full `build`: passing locally.

## Docs Site

- `:docs-site` is a Compose Multiplatform/Wasm app.
- The site renders real AdaptiveKt components.
- Direct routes are prepared for `/`, `/components/`, `/docs/`, and `/demo/`.
- The root HTML uses a fixed-height Compose viewport with internal Compose scrolling.

## Admin Demo

- `:admin-demo` remains a separate Wasm app.
- Pages copies it to `site-dist/demo/app/`.
- Kamel/Ktor remain demo-only JVM dependencies.

## Captures

- Docs-site Playwright captures pass locally.
- Admin-demo Playwright captures pass locally.
- Desktop Robot capture remains local/manual and is not run in CI.

## Link Check

- `tools/check-site-links.ps1` passes locally.
- The checker confirms `site-dist/demo/app/index.html` exists.

## Workflows

- `ci.yml` configures Java 17, Gradle, and Android SDK.
- `pages.yml` uses `windows-latest`, Java 17, Gradle, Android SDK, `tools/prepare-pages-site.ps1`, and official Pages upload/deploy actions.
- No secrets are required.
- iOS is declared but not built on Linux/Windows runners.

## Ignored Generated Files

Ignored by `.gitignore`:

- `local.properties`
- `.gradle/`
- `build/`
- `**/build/`
- `site-dist/`
- `node_modules/`
- `**/node_modules/`
- `*.zip`
- `*.log`

## Limitations

- iOS validation still requires macOS.
- Wasm bundle size warnings are expected for Compose/Skiko.
- Visual verification is smoke/manual; no pixel diff gate yet.
- Maven publishing is not configured.
- MultiSelect, dark mode, and theme foundation remain future work.

## Manual GitHub Steps

1. Push the branch after reviewing the uncommitted changes.
2. Enable GitHub Pages with source `GitHub Actions`.
3. Run the Pages workflow manually or push to `main`/`master`.
4. Confirm `/`, `/components/`, `/docs/`, `/demo/`, and `/demo/app/` after deployment.

## Risk Summary

- Low publication risk for Pages after local build/capture/link checks.
- Main residual risk is hosted runner variance around Android SDK/Gradle; workflows now install Android SDK explicitly.
