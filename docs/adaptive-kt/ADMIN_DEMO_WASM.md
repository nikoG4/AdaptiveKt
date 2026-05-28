# Admin Demo Wasm

PR P2 added a basic Wasm browser target to `:admin-demo`. PR P3 adds automated Playwright-based smoke screenshots.

## Goal

The goal is a runnable browser admin demo that reuses the existing shared demo UI and has automated visual verification.

## Source Sets

- `commonMain`: shared admin demo app, models, screens, UI helpers, and Components showcase.
- `jvmMain`: Desktop entry point, capture tooling, `ComposeWindow`/AWT `Robot`, and Kamel-backed remote image loading.
- `wasmJsMain`: browser entry point, `index.html`, query-param screen selection, and fallback-only image helpers.

`Robot`, AWT, and `ComposeWindow` remain outside `commonMain`.

## Running

Development server:

```powershell
.\gradlew.bat :admin-demo:wasmJsBrowserDevelopmentRun --console=plain
```

Gradle reports the local URL as:

```text
http://localhost:8080/
```

Production distribution:

```powershell
.\gradlew.bat :admin-demo:wasmJsBrowserDistribution --console=plain --stacktrace
```

Output:

```text
admin-demo/build/dist/wasmJs/productionExecutable
```

## Screen Selection (Wasm only)

In Wasm, you can open a specific screen by adding a `screen` query parameter:

```text
http://localhost:8080/?screen=employees
```

Supported IDs: `dashboard`, `employees`, `products`, `invoices`, `settings`, `components`, `components-thumbnails`, `components-chips`, `invoices-empty`, `invoices-loading`, `invoices-error`.

## Visual Verification (Playwright)

To capture screenshots of the Wasm demo across different breakpoints:

```powershell
.\tools\capture-admin-demo-web.ps1
```

See [WEB_SCREENSHOT_TOOLING.md](WEB_SCREENSHOT_TOOLING.md) for details.

## Remote Images

Remote image loading stays demo-only and JVM-only.

- JVM/Desktop uses Kamel in `admin-demo/src/jvmMain`.
- Wasm uses local fallback avatars and thumbnails via `AdaptiveAvatar` and `AdaptiveThumbnail`.
- AdaptiveKt library modules do not depend on Kamel or Ktor.

## Verification

Commands run in P3:

```powershell
.\gradlew.bat :admin-demo:wasmJsBrowserDistribution --console=plain --stacktrace
.\tools\capture-admin-demo-web.ps1
```

## Known Limitations

- Wasm uses local image fallbacks instead of remote avatars/thumbnails.
- The app renders through a canvas; Playwright interacts with the canvas container.
- Visual Regression: Currently smoke screenshots only, no pixel-perfect diffing yet.
- Webpack reports expected bundle-size warnings.
