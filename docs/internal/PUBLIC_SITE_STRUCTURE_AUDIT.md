# Public Site Structure Audit

## Current Docs-Site Structure
The documentation site is a Compose Multiplatform WebAssembly application located in the `docs-site` module. It uses `AdaptiveKt` primitives directly to render its pages. The source is located at `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/`. Key files include:
- `SiteRouter.kt` for top-level navigation.
- `SiteDemoPage.kt` for rendering the "Demos" section.
- `SiteDocsPage.kt` for rendering documentation components.

## Current Demos Included in Pages
Based on `tools/prepare-pages-site.ps1`:
1. **docs-site** (Main Docs app)
2. **admin-demo**
3. **ecommerce-demo**
4. **ai-workspace-demo** (recently integrated in the prep script)

## Current Output Directory Convention
- `tools/prepare-pages-site.ps1` builds the Wasm distribution for the above modules.
- It aggregates them into the `site-dist/` directory.
- The `.github/workflows/pages.yml` Action uploads `site-dist` as the artifact and deploys it to GitHub Pages.

## Directory Mapping
- **Docs Site:** Root of `site-dist/`
- **Admin Demo:** Copied from `admin-demo/build/dist/wasmJs/productionExecutable` to `site-dist/demo/app/`
- **Ecommerce Demo:** Copied from `examples/ecommerce-demo/build/dist/wasmJs/productionExecutable` to `site-dist/examples/ecommerce/`
- **AI Workspace Demo:** Copied from `examples/ai-workspace-demo/build/dist/wasmJs/productionExecutable` to `site-dist/examples/ai-workspace/`

## Version Control Rules
- **Must be committed:** All source code changes (`SiteDemoPage.kt`), scripts (`prepare-pages-site.ps1`), and audit documents (`docs/internal/*.md`).
- **Must remain generated/ignored:** The actual build artifacts (`site-dist/`, `tmp-gh-pages-root/`, and any `.wasm`/`.js` binary outputs) must never be committed to the repository. They are built and deployed ephemerally by GitHub Actions.
