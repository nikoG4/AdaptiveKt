# Docs Site

`:docs-site` is the Compose Multiplatform/Wasm landing and live component catalog for AdaptiveKt.

## Run Locally

```powershell
.\gradlew.bat :docs-site:wasmJsBrowserDevelopmentRun
```

## Production Distribution

```powershell
.\gradlew.bat :docs-site:wasmJsBrowserDistribution --console=plain --stacktrace
```

Output:

```text
docs-site/build/dist/wasmJs/productionExecutable/
```

## Pages Artifact

```powershell
.\tools\prepare-pages-site.ps1
```

This places docs-site at the root of `site-dist/` and keeps the admin demo as a separate Wasm app at `site-dist/demo/app/`.

## Notes

- The site uses real AdaptiveKt components.
- The site has a light/dark toggle backed by `AdaptiveColorSchemes.defaultLight()` and `AdaptiveColorSchemes.defaultDark()`.
- The Wasm entry point accepts `theme=dark`, for example `/?theme=dark` and `/components/?theme=dark`.
- It does not depend on `:admin-demo`.
- It does not use Kamel, Robot, AWT, Playwright, or Material 3 in app code.
- The older `site/` static landing remains as a fallback for now.
