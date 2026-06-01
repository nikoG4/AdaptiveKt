<picture>
  <source media="(prefers-color-scheme: dark)" srcset="docs-site/src/wasmJsMain/resources/assets/brand/adaptivekt-logo-dark.svg">
  <img alt="AdaptiveKt" src="docs-site/src/wasmJsMain/resources/assets/brand/adaptivekt-logo-light.svg" width="312">
</picture>

# AdaptiveKt

Compose Multiplatform admin UI toolkit for adaptive dashboards, data views, forms, navigation, feedback states, and shared UI components.

AdaptiveKt is source-first today. Maven Central publishing is not available yet, so consume it as included Gradle modules or a repository/submodule dependency. A local Maven publishing dry-run is available for release preparation.

## Platform Support

| Platform | Status |
| --- | --- |
| Desktop/JVM | Supported and used by `:admin-demo` |
| Android | Library targets enabled |
| Web/Wasm | Library targets and demo distribution enabled |
| iOS | Targets declared; validation requires macOS |

## Features

- Adaptive breakpoints and responsive helpers in `:adaptive-core`
- Theme foundation with `AdaptiveTheme`, light/dark color schemes, shapes, typography, and state tokens used across core components and shared modules
- Layout primitives including `AdaptiveContainer` and `AdaptiveGrid`
- Navigation scaffold with breakpoint-driven navigation modes and professional pill-style sidebar defaults
- Data view that switches between mobile cards and wider table layouts
- Responsive form layout with sections, fields, validation messages, and actions
- Feedback states for animated loading, empty, and error content
- Components: buttons, icon buttons, badges, avatars, thumbnails, chips, cards, surfaces, text fields, search fields, menus, dropdowns, select, multi-select, and carousel
- Hierarchical navigation tree for nested admin sidebars and settings panels
- Wasm browser demo
- Public Compose Multiplatform/Wasm documentation site with a product home page, rendered docs pages, live component examples, code snippets, and API tables
- Visual verification tooling for Desktop and Web

## Quick Start

Use JDK 17 for local development and Gradle builds. AdaptiveKt is validated with Java 17 in CI; newer JDKs can fail in Kotlin/Gradle before project compilation starts.

Windows PowerShell session setup:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
.\tools\check-dev-environment.ps1
.\gradlew.bat build --console=plain --stacktrace
```

Add the modules you need as source dependencies:

```kotlin
// settings.gradle.kts
include(":adaptive-core")
include(":adaptive-components")
include(":adaptive-layout")
include(":adaptive-navigation")
include(":adaptive-forms")
include(":adaptive-data")
include(":adaptive-feedback")
```

Then depend on the modules from your Compose Multiplatform target:

```kotlin
dependencies {
    implementation(project(":adaptive-core"))
    implementation(project(":adaptive-components"))
    implementation(project(":adaptive-layout"))
}
```

## Examples

```kotlin
AdaptiveTheme {
    AdaptiveGrid(columns = 12) {
        item(span = 6) { AdaptiveCard { /* content */ } }
        item(span = 6) { AdaptiveCard { /* content */ } }
    }
}
```

```kotlin
AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultDark()) {
    AdminApp()
}
```

```kotlin
var selected by remember { mutableStateOf<String?>(null) }

AdaptiveSelect(
    options = listOf("Open", "Pending", "Closed"),
    selectedOption = selected,
    onOptionSelected = { selected = it },
    optionLabel = { it },
    label = "Status",
    searchable = true,
    clearable = true,
)
```

```kotlin
var teams by remember { mutableStateOf(listOf("Operations", "Finance")) }

AdaptiveMultiSelect(
    options = listOf("Operations", "Finance", "Support", "Sales"),
    selectedOptions = teams,
    onSelectedOptionsChange = { teams = it },
    optionLabel = { it },
    label = "Teams",
    searchable = true,
    maxVisibleChips = 2,
)
```

```kotlin
var slide by remember { mutableStateOf(0) }

AdaptiveCarousel(
    items = dashboardCards,
    selectedIndex = slide,
    onSelectedIndexChange = { slide = it },
    transition = AdaptiveCarouselTransition.Slide,
) { card, index ->
    SummaryPanel(card, index)
}
```

## Run The Demo

Desktop:

```powershell
.\gradlew.bat :admin-demo:run
```

Wasm development:

```powershell
.\gradlew.bat :admin-demo:wasmJsBrowserDevelopmentRun
```

Wasm production distribution:

```powershell
.\gradlew.bat :admin-demo:wasmJsBrowserDistribution
```

Output: `admin-demo/build/dist/wasmJs/productionExecutable/`

## Run The Documentation Site

The public documentation site is implemented in `:docs-site` with Compose Multiplatform/Wasm. It includes a product landing page, a documentation overview, and a live component catalog rendered with real AdaptiveKt APIs.

```powershell
.\gradlew.bat :docs-site:wasmJsBrowserDevelopmentRun
.\gradlew.bat :docs-site:wasmJsBrowserDistribution
```

The generated Pages artifact also includes the admin demo under `/demo/app/`.

## Visual Capture

Desktop screenshots use Robot/AWT and require an active graphical session:

```powershell
.\tools\capture-admin-demo.ps1
```

Web screenshots use Playwright against the Wasm distribution:

```powershell
.\tools\capture-admin-demo-web.ps1
```

Docs-site screenshots use Playwright against `site-dist/`:

```powershell
.\tools\capture-docs-site-web.ps1
```

Basic generated-site link check:

```powershell
.\tools\check-site-links.ps1
```

## Documentation

- Component docs: `docs/components/`
- Guides: `docs/guides/`
- Development notes: `docs/development/`
- Local setup: `docs/development/setup.md`
- Local publishing dry-run: `docs/publishing/LOCAL_PUBLISHING.md`
- Signing and release workflow preparation: `docs/publishing/SIGNING_AND_RELEASE_WORKFLOW.md`
- Roadmap: `docs/roadmap/`
- Historical project docs: `docs/adaptive-kt/`

## GitHub Pages

The Pages artifact is prepared by:

```powershell
.\tools\prepare-pages-site.ps1
```

This builds `:docs-site` as the site root and copies `:admin-demo` under `site-dist/demo/app/`. The older `site/` static HTML is kept as a temporary fallback and is not the primary Pages output.

Serve the generated site locally:

```powershell
.\tools\serve-site-dist.ps1
```

## Roadmap

- Validate iOS targets on macOS
- Publish artifacts to Maven Central
- Expand visual regression coverage
- Evaluate platform/brand presets
- Add async/server search patterns for select-style components

## License

Apache License 2.0. See [LICENSE](LICENSE).
