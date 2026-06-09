# Final Public Site Validation Matrix

The following commands were executed locally to ensure the public demos compile, test cleanly, and route without 404s.

## 1. Core Library Verification
**Command:** `.\scripts\verify-layout.ps1`
- Includes: `.\gradlew.bat clean build`, `jvmTest` runs across core modules, and static layout guards.
- **Result:** [PASS]

## 2. AI Workspace Demo Verification
**Command:** `.\scripts\verify-ai-workspace.ps1`
- Includes: `desktopTest`, `wasmJsBrowserDistribution`, and specific routing/codec tests for the mock layout.
- **Result:** [PASS]

## 3. Pages Deployment Payload Generation
**Command:** `.\tools\prepare-pages-site.ps1`
- Includes: Parallel WebAssembly compilation across `docs-site`, `admin-demo`, `ecommerce-demo`, and `ai-workspace-demo`. Copies binary blobs into the ephemeral `site-dist/` payload.
- **Result:** [PASS] (Successfully bundled into `site-dist`)

## 4. Static Link Validation
**Command:** `.\scripts\check-pages-links.ps1`
- Verifies the directory payload mapping matches the frontend gallery links defined in `SiteDemoPage.kt`.
- **Result:** [PASS]
  - `[OK] index.html exists`
  - `[OK] demo/app/index.html exists`
  - `[OK] examples/ecommerce/index.html exists`
  - `[OK] examples/ai-workspace/index.html exists`

## 5. Visual/Smoke Validation
**Manual Inspection**
- Verified via Compose Desktop Previews and Wasm development server.
- Visual bounds respect `AdaptiveListDetailScaffold` behavior on compact vs. expanded viewports without causing infinite measurement loops.
- **Result:** [PASS]
