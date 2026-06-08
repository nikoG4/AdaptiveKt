# AI Workspace Pages Deployment

## Overview
The AdaptiveKt library uses GitHub Pages to host its live interactive components and demo showcases (e.g. `docs-site`, `admin-demo`, `ecommerce-demo`). This document details how the newly developed `ai-workspace-demo` is integrated into this deployment flow.

## Deployment Preparation Script Update
The core deployment preparation script (`tools/prepare-pages-site.ps1`) has been successfully updated to include the AI Workspace Demo.

**Changes made:**
1. Included the demo target directory in the build sequence: `.\gradlew.bat -p examples/ai-workspace-demo wasmJsBrowserDistribution`.
2. Added the distribution path mapping: `$AiWorkspaceDemoDist = "examples/ai-workspace-demo/build/dist/wasmJs/productionExecutable"`.
3. Created a destination folder in the final `site-dist` payload at `examples/ai-workspace`.
4. Transferred the Wasm production build artifacts from the workspace target directly into the mapped routing payload.

## Deployed Links
Once the `pages.yml` GitHub Action successfully completes on `main`, the demo will be live alongside the others:
- Admin Demo: `[root_url]/demo/app/`
- Ecommerce Demo: `[root_url]/examples/ecommerce/`
- **AI Workspace Demo: `[root_url]/examples/ai-workspace/`**

## Verification Step
To ensure the deployment prep script functions correctly without accidentally committing the outputs, developers can run:
```powershell
.\tools\prepare-pages-site.ps1 -SkipBuild
```
(Assuming the Wasm binary was already built by `./gradlew wasmJsBrowserDistribution`). The script will populate the local `site-dist` directory where you can verify the `examples/ai-workspace` structure contains `index.html`, `ai-workspace-demo.js`, and `.wasm` files.

*Note: The generated `site-dist` and the legacy `tmp-gh-pages-root` directories are strictly excluded from version control.*
