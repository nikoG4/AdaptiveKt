# Docs Site

`:docs-site` is the Compose Multiplatform/Wasm public documentation experience for AdaptiveKt.

It contains:

- A product home page for framework positioning and current publishing status.
- A documentation page for setup, theming, layout, local publishing, and visual verification.
- A component catalog with rendered examples, copyable code blocks, API parameter tables, variants, accessibility notes, and limitations.
- Links into the bundled admin demo under `/demo/app/`.

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

## Source Structure

Primary docs-site UI files live under:

```text
docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/
```

Key files:

- `SiteHomePage.kt`: product home page and hero preview.
- `SiteDocsPage.kt`: rendered documentation topics.
- `SiteComponentsPage.kt`: live component catalog.
- `DocsModels.kt`: docs/catalog data models.
- `DocsUi.kt`: internal docs primitives such as docs shell, callouts, code blocks, parameter tables, and example panels.
- `SiteNavigation.kt`: top navigation and links.

Keep these primitives local to `:docs-site`; they are documentation surfaces, not AdaptiveKt public components.

## Notes

- The site uses real AdaptiveKt components.
- Component docs render real component examples, not static screenshots.
- Copy buttons currently provide visual feedback only in common code; browser clipboard integration can be added later behind a safe Wasm implementation.
- The site has a light/dark toggle backed by `AdaptiveColorSchemes.defaultLight()` and `AdaptiveColorSchemes.defaultDark()`.
- The Wasm entry point accepts `theme=dark`, for example `/?theme=dark` and `/components/?theme=dark`.
- It does not depend on `:admin-demo`.
- It does not use Kamel, Robot, AWT, Playwright, or Material 3 in app code.
- The older `site/` static landing remains as a fallback for now.

## Branding Assets

AdaptiveKt brand assets for the docs site live under:

```text
docs-site/src/wasmJsMain/resources/assets/brand/
```

Available SVG variants:

- `adaptivekt-symbol.svg`: primary colored symbol for favicon/app icon and compact UI.
- `adaptivekt-symbol-mono.svg`: neutral single-mark variant for constrained contexts.
- `adaptivekt-logo-light.svg`: wordmark for light backgrounds and README default rendering.
- `adaptivekt-logo-dark.svg`: wordmark for dark backgrounds and README dark rendering.

The docs-site navbar, hero, and footer render the same mark with Compose in `SiteBrand.kt` so the brand stays theme-aware in light and dark mode. Keep SVG files as the source asset format; generate PNG derivatives only when a platform explicitly requires raster icons.
