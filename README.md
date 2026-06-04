<picture>
  <source media="(prefers-color-scheme: dark)" srcset="docs-site/src/wasmJsMain/resources/assets/brand/adaptivekt-logo-dark.svg">
  <img alt="AdaptiveKt" src="docs-site/src/wasmJsMain/resources/assets/brand/adaptivekt-logo-light.svg" width="312">
</picture>

# AdaptiveKt

[![Maven Central](https://img.shields.io/maven-central/v/io.github.nikog4.adaptivekt/adaptive-components?label=Maven%20Central)](https://central.sonatype.com/search?q=io.github.nikog4.adaptivekt)
[![CI](https://github.com/nikoG4/AdaptiveKt/actions/workflows/ci.yml/badge.svg)](https://github.com/nikoG4/AdaptiveKt/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/nikoG4/AdaptiveKt)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.21-7F52FF)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-4285F4)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Docs](https://img.shields.io/badge/docs-GitHub%20Pages-2563EB)](https://nikog4.github.io/AdaptiveKt/)

Adaptive UI primitives for Compose Multiplatform.

AdaptiveKt is an alpha-stage Kotlin and Compose Multiplatform library for building responsive admin, productivity, and data-heavy interfaces from shared UI code. It focuses on the unglamorous but expensive parts of real app UI: layout breakpoints, dashboard shells, tables that become mobile cards, form sections, feedback states, navigation patterns, and reusable component defaults that look professional without requiring every call site to carry a wall of modifiers.

The project is published to Maven Central as `0.1.0-alpha01`. It is usable for experimentation, demos, internal tools, and early integration feedback, but it is not a stable 1.0 API yet. APIs may change before beta/stable releases as the library hardens.

## Why AdaptiveKt?

Compose Multiplatform makes it possible to share UI across platforms, but teams still need to solve responsive product UI patterns themselves. A simple admin screen often needs breakpoint rules, navigation mode changes, mobile-friendly data cards, clean action states, form layout decisions, empty/loading/error states, and consistent theme tokens.

AdaptiveKt exists to reduce that friction for Kotlin Multiplatform developers. Its direction is commonMain-first: shared primitives should work across Desktop/JVM, Android, Web/Wasm, and iOS where the underlying Compose Multiplatform tooling supports them.

This is not a replacement for Material, nor a claim to be a complete enterprise design system. AdaptiveKt is a focused toolkit for adaptive app patterns that can complement a product's own brand and component layer.

## What It Includes

| Module | Purpose |
| --- | --- |
| `adaptive-core` | Breakpoints, tokens, theme foundation, color schemes, typography, and state tokens. |
| `adaptive-components` | Buttons, icon buttons, badges, avatars, thumbnails, chips, cards, surfaces, fields, menus, select, multi-select, carousel, and embedded functional icons. |
| `adaptive-layout` | Responsive layout primitives such as `AdaptiveContainer` and `AdaptiveGrid`. |
| `adaptive-feedback` | Empty, loading, and error state components. |
| `adaptive-navigation` | Adaptive navigation scaffold, navigation modes, sidebar/rail/bottom navigation defaults, and navigation tree. |
| `adaptive-forms` | Responsive form sections, field layout, validation messages, and actions. |
| `adaptive-data` | Responsive data views that switch between desktop table layouts and compact mobile cards. |

The repository also contains a docs-site and admin demo used to exercise the library visually. Those demo modules are not Maven artifacts.

## Installation

AdaptiveKt is available from Maven Central.

```kotlin
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")
}
```

Use only the modules your app needs:

```kotlin
dependencies {
    implementation("io.github.nikog4.adaptivekt:adaptive-core:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-layout:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-feedback:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-navigation:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-forms:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-data:0.1.0-alpha01")
}
```

## Quick Start

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
fun DashboardSummary() {
    AdaptiveTheme {
        AdaptiveGrid(columns = 12) {
            item(span = 6) {
                AdaptiveCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BasicText("Revenue")
                        BasicText("\$42,800")
                        AdaptiveButton(
                            text = "View report",
                            onClick = { /* navigate */ },
                        )
                    }
                }
            }

            item(span = 6) {
                AdaptiveCard {
                    BasicText("Open invoices")
                    BasicText("18")
                }
            }
        }
    }
}
```

Select-style components expose controlled state:

```kotlin
var status by remember { mutableStateOf<String?>(null) }

AdaptiveSelect(
    options = listOf("Open", "Pending", "Closed"),
    selectedOption = status,
    onSelectedOptionChange = { status = it },
    optionLabel = { it },
    label = "Status",
    searchable = true,
    clearable = true,
)
```

## Platform Support

| Platform | Status |
| --- | --- |
| Desktop/JVM | Library and admin demo build/run on JVM. |
| Android | Library targets are enabled and built through the Android target. |
| Web/Wasm | Library targets and demo/docs distributions are enabled. |
| iOS | Library targets are declared; final iOS validation depends on macOS and Compose Multiplatform/iOS tooling maturity. |

AdaptiveKt is designed for Compose Multiplatform commonMain usage. Platform-specific screenshot tooling, Robot/AWT capture helpers, and demo image loading stay outside the library modules.

## Documentation

- Docs site: https://nikog4.github.io/AdaptiveKt/
- Component docs: [docs/components](docs/components)
- Development setup: [docs/development/setup.md](docs/development/setup.md)
- Local publishing notes: [docs/publishing/LOCAL_PUBLISHING.md](docs/publishing/LOCAL_PUBLISHING.md)
- Maven Central readiness notes: [docs/publishing/MAVEN_CENTRAL_READINESS.md](docs/publishing/MAVEN_CENTRAL_READINESS.md)

## Examples

- `admin-demo`: responsive admin dashboard demo for Desktop and Web/Wasm.
- `docs-site`: public Compose Multiplatform/Wasm documentation site and component catalog.
- `examples/`: example work may be developed separately from the core library and is not required to consume the published artifacts.

Run the admin demo:

```powershell
.\gradlew.bat :admin-demo:run
.\gradlew.bat :admin-demo:wasmJsBrowserDevelopmentRun
```

Run the docs site:

```powershell
.\gradlew.bat :docs-site:wasmJsBrowserDevelopmentRun
```

## Project Status

Current published version: `0.1.0-alpha01`.

AdaptiveKt is alpha software. The current focus is to validate the API surface, improve documentation and examples, harden accessibility and keyboard/focus behavior, expand visual regression coverage, and gather feedback before stabilizing a beta API.

## Why This Matters For The Kotlin Ecosystem

Kotlin Multiplatform adoption improves when developers can see credible paths for real user interfaces, not only small samples. AdaptiveKt targets a practical gap: shared responsive UI patterns for dashboards, internal tools, data workflows, and product admin screens.

The project can help:

- Kotlin Multiplatform developers prototype and ship shared UI faster.
- Compose Multiplatform adopters evaluate desktop, Android, Web/Wasm, and iOS UI from one codebase.
- Students, indie developers, and small teams learn commonMain-first UI architecture.
- Internal tool builders avoid rebuilding the same admin patterns from scratch.

## Roadmap

Short-term alpha hardening:

- Stabilize core public APIs and document migration rules.
- Improve accessibility, keyboard navigation, focus, and pointer behavior.
- Expand visual regression and screenshot testing across Desktop and Web/Wasm.
- Add richer examples for admin, productivity, and ecommerce-style workflows.
- Validate iOS artifacts and demos on macOS.
- Continue theme/token work for light and dark modes.
- Build a contributor feedback loop through issues, templates, and documented governance.

See [ROADMAP.md](ROADMAP.md) for the longer plan.

## Contributing

Contributions, bug reports, docs improvements, and API feedback are welcome. Start with [CONTRIBUTING.md](CONTRIBUTING.md), and please keep changes commonMain-first unless a platform-specific boundary is intentional and documented.

## Security

See [SECURITY.md](SECURITY.md) for supported versions and private vulnerability reporting guidance.

## Code Of Conduct

AdaptiveKt follows a contributor code of conduct. See [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

## Grant Readiness And Sustainability

AdaptiveKt is seeking support to mature from an alpha Compose Multiplatform UI toolkit into a more reliable, documented, and tested library for the Kotlin ecosystem. Grant funding would be directed toward API stabilization, accessibility work, cross-platform examples, visual regression coverage, iOS validation, and sustained maintenance.

See [docs/grants/GRANT_PROPOSAL_DRAFT.md](docs/grants/GRANT_PROPOSAL_DRAFT.md) for a draft proposal.

## License

AdaptiveKt is licensed under the Apache License 2.0. See [LICENSE](LICENSE).
