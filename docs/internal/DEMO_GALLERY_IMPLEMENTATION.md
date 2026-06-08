# Demo Gallery Implementation

## Overview
The demo gallery exists as a Compose UI screen (`SiteDemoPage.kt`) within the `docs-site` WebAssembly application. Visitors landing on the documentation site's "Demos" tab can launch the individual demo modules from there.

## Files Changed
- `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteDemoPage.kt`

## Links Added
- Added an `AdaptiveCard` for the **AI Workspace Demo**.
- The "Open AI Workspace" button calls `openSiteUrl("examples/ai-workspace/")`.

## Directory Mappings for Pages
- **Docs-site route**: `[site-root]/index.html` (or simply `[site-root]/`)
- **Admin Demo route**: `[site-root]/demo/app/`
- **Ecommerce Demo route**: `[site-root]/examples/ecommerce/`
- **AI Workspace Demo route**: `[site-root]/examples/ai-workspace/`

## Generation
The `tools/prepare-pages-site.ps1` script creates the routing structure (`examples/ai-workspace/` etc.) by copying the Wasm binaries (`*.wasm`, `*.js`, `index.html`) produced by each demo's `wasmJsBrowserDistribution` Gradle task directly into a unified `site-dist/` folder. This is completely transparent to the user since they use standard relative links.
