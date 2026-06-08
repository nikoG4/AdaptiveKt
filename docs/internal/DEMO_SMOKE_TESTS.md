# Demo Smoke Tests

## Overview
Because the AdaptiveKt demos compile purely to WebAssembly for public site consumption, traditional DOM-based test automation (like Playwright or Selenium) cannot reliably interact with the Canvas-rendered text without injecting specific accessibility semantic trees that vary heavily across Compose versions. 

Therefore, these smoke tests are conducted through manual verification of the local development distributions before major releases.

## Tools Used
- `.\gradlew.bat desktopRun` (JVM Target Verification)
- `.\gradlew.bat wasmJsBrowserDevelopmentRun` (Browser Target Verification)

## Routes Tested & Assertions
When the web server is active, a maintainer must load the following hash routes and verify the expected components render:

### AI Workspace Demo
- `/`: **Dashboard Screen** should display "AI Workspace" and render data grid tiles.
- `/#/chats`: **Chat Screen** should show "Conversations" in the list pane.
- `/#/prompts`: **Prompt Library** should list prompts in the `AdaptiveListDetailScaffold`.
- `/#/assistants`: **Assistants Screen** should show the Assistant configuration list.
- `/#/playground`: **Playground Screen** should open the interactive chat-tuning canvas.
- `/#/knowledge`: **Knowledge Base Screen** should show the knowledge documents table.
- `/#/tools`: **Tools Screen** should render configured functions.
- `/#/evaluations`: **Evaluations Screen** should show accuracy/latency metrics.
- `/#/settings`: **Settings Screen** should display "Settings".

### Ecommerce Demo
- `/`: **Storefront** should render a list of products.
- `/#/products/{id}`: **Product Detail** should render a "Add to Cart" block.
- `/#/cart`: **Cart** should render items and a "Checkout" button.

## Results
- **Automated Mode:** Skipped. Playwright is not configured for Wasm Canvas interaction in this repository.
- **Manual Mode:** `[PASS]`. Validated successfully on standard viewports via `desktopRun` and hot-reloaded Wasm development server prior to static packaging.
