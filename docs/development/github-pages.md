# GitHub Pages

AdaptiveKt Pages is generated from Compose Multiplatform/Wasm.

## Layout

```text
site-dist/
  index.html
  components/
    index.html
  docs/
    index.html
  demo/
    index.html
    app/
      index.html
      admin-demo.js
      *.wasm
  docs-site.js
  *.wasm
  .nojekyll
```

## Local Build

```powershell
.\tools\prepare-pages-site.ps1
```

The script builds:

- `:docs-site:wasmJsBrowserDistribution`
- `:admin-demo:wasmJsBrowserDistribution`

Then it copies the docs site to the root of `site-dist/` and the admin demo to `site-dist/demo/app/`.

The script also writes route entrypoints for `/components/`, `/docs/`, and `/demo/` so direct navigation works on GitHub Pages.

Serve locally with:

```powershell
.\tools\serve-site-dist.ps1
```

Open:

- `http://localhost:8090/`
- `http://localhost:8090/components/`
- `http://localhost:8090/docs/`
- `http://localhost:8090/demo/`
- `http://localhost:8090/demo/app/`

## Workflow

`.github/workflows/pages.yml` runs on `windows-latest` and uploads `site-dist` with `actions/upload-pages-artifact`.

The workflow configures Java 17, Gradle, and the Android SDK, then runs:

```powershell
.\gradlew.bat build --console=plain --stacktrace
.\tools\prepare-pages-site.ps1
```

## Notes

- `site/` is retained as the old static fallback for now.
- Admin demo remains a separate Wasm application.
- Playwright capture for docs-site is available locally via `tools/capture-docs-site-web.ps1`.
- Robot/AWT desktop capture is not run in CI.
- Playwright visual captures are local/manual for now.
