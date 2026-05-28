# Web Screenshot Tooling

This tool allows for automated visual smoke testing of the `admin-demo` Wasm target using Playwright.

## Prerequisites

- **Node.js**: Required to run Playwright and the local server.
- **npm**: Required to install dependencies.
- **Playwright**: Installed automatically by the script.

## How to Run

To generate web screenshots, run the following PowerShell script from the project root:

```powershell
.\tools\capture-admin-demo-web.ps1
```

### Parameters

- `-OutputDir`: (Default: `build\web-visual-captures`) Directory where PNGs and the report will be saved.
- `-ZipPath`: (Default: `build\adaptivekt-admin-demo-web-visual-captures.zip`) Path for the generated ZIP file.
- `-BaseUrl`: (Default: `http://localhost:8080`) The URL where the Wasm app is served.
- `-SkipBuild`: Switch to skip the `:admin-demo:wasmJsBrowserDistribution` Gradle task if already built.

## What it does

1.  **Builds the Wasm target**: Executes `./gradlew :admin-demo:wasmJsBrowserDistribution`.
2.  **Serves the distribution**: Starts a local `http-server` pointing to the Wasm output.
3.  **Launches Playwright**: Opens a headless Chromium browser.
4.  **Navigates to screens**: Uses query parameters (e.g., `?screen=dashboard`) to open specific screens.
5.  **Captures Screenshots**: Saves PNGs for three breakpoints:
    - **Compact**: 420x900
    - **Medium**: 720x900
    - **Large**: 1440x900
6.  **Generates Report**: Creates a `web-visual-capture-report.md` with a gallery of all captures.
7.  **Zips Output**: Compresses all captures into a single ZIP file.
8.  **Cleans up**: Stops the local server.

## Screens Captured

- dashboard
- employees
- products
- invoices
- settings
- components
- components-thumbnails
- components-chips
- components-dropdowns-open
- components-dropdowns-edge
- invoices-empty
- invoices-loading
- invoices-error

## Comparison with Desktop Tooling

- **Desktop**: Uses `Robot` and `AWT` for captures via `capture-admin-demo.ps1`.
- **Web/Wasm**: Uses `Playwright` via `capture-admin-demo-web.ps1`.
- **Images**: Wasm uses local fallback for images since Kamel is not yet supported in Wasm for this project.

## Limitations

- This is a **smoke visual test**. It verifies that the canvas renders and the app doesn't crash on specific screens.
- **Visual Regression**: Pixel-perfect diffing is not implemented yet.
