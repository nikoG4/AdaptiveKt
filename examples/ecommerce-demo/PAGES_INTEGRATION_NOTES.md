# GitHub Pages Integration Notes

This directory contains the E-commerce Demo built with AdaptiveKt and Compose Multiplatform for Web (Wasm).

## Future Integration Steps

To integrate this demo into the main GitHub Pages site (`docs-site`), you will need to perform the following steps in a future task:

1.  **Generate Wasm Distribution:**
    Ensure the `wasmJsBrowserDistribution` task is run. This generates the production-ready distribution.
    ```powershell
    .\gradlew.bat -p examples\ecommerce-demo wasmJsBrowserDistribution
    ```
    The output is located at: `examples/ecommerce-demo/build/dist/wasmJs/productionExecutable/`

2.  **Copy the Distribution to `site-dist`:**
    Modify the `tools/prepare-pages-site.ps1` script to copy the contents of the above output directory into a specific path within `site-dist`, such as `site-dist/examples/ecommerce/`.

    Example logic to add to `prepare-pages-site.ps1`:
    ```powershell
    $ecommerceSource = "examples/ecommerce-demo/build/dist/wasmJs/productionExecutable"
    $ecommerceTarget = "site-dist/examples/ecommerce"
    if (Test-Path $ecommerceSource) {
        New-Item -ItemType Directory -Force -Path $ecommerceTarget | Out-Null
        Copy-Item -Path "$ecommerceSource\*" -Destination $ecommerceTarget -Recurse -Force
        Write-Host "Copied ecommerce demo to $ecommerceTarget"
    } else {
        Write-Warning "Ecommerce demo distribution not found. Run wasmJsBrowserDistribution first."
    }
    ```

3.  **Suggested Route:**
    The suggested URL path for the demo on GitHub Pages is `/examples/ecommerce/`.

4.  **Local Validation:**
    This demo is currently validated standalone using Playwright. To verify it works independently:
    ```powershell
    npm install
    npm run test:e2e
    ```

**Note:** In this task, no files outside of `examples/ecommerce-demo/` were modified to adhere to strict directory boundaries.
