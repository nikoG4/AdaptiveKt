# AdaptiveKt

Compose Multiplatform admin UI toolkit for adaptive dashboards, data views, forms, navigation, feedback states, and shared UI components.

AdaptiveKt is source-first today. Maven publishing is not available yet, so consume it as included Gradle modules or a repository/submodule dependency.

## Platform Support

| Platform | Status |
| --- | --- |
| Desktop/JVM | Supported and used by `:admin-demo` |
| Android | Library targets enabled |
| Web/Wasm | Library targets and demo distribution enabled |
| iOS | Targets declared; validation requires macOS |

## Features

- Adaptive breakpoints and responsive helpers in `:adaptive-core`
- Layout primitives including `AdaptiveContainer` and `AdaptiveGrid`
- Navigation scaffold with breakpoint-driven navigation modes
- Data view that switches between mobile cards and wider table layouts
- Responsive form layout with sections, fields, validation messages, and actions
- Feedback states for loading, empty, and error content
- Components: buttons, icon buttons, badges, avatars, thumbnails, chips, cards, surfaces, text fields, search fields, menus, dropdowns, and select
- Wasm browser demo
- Compose Multiplatform/Wasm documentation site with live component examples
- Visual verification tooling for Desktop and Web

## Quick Start

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
AdaptiveGrid(columns = 12) {
    item(span = 6) { AdaptiveCard { /* content */ } }
    item(span = 6) { AdaptiveCard { /* content */ } }
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

The landing and component catalog are implemented in `:docs-site` with Compose Multiplatform/Wasm and render real AdaptiveKt components.

```powershell
.\gradlew.bat :docs-site:wasmJsBrowserDevelopmentRun
.\gradlew.bat :docs-site:wasmJsBrowserDistribution
```

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
- Add theming foundation and dark mode later
- MultiSelect is intentionally out of scope for this initial publication

## License

Apache License 2.0.
