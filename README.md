# AdaptiveKt

[![Kotlin](https://img.shields.io/badge/kotlin-2.1.21-blue?logo=kotlin)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/compose_multiplatform-1.8.2-blue)](https://www.jetbrains.com/help/compose-multiplatform/)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](LICENSE)

**Compose Multiplatform Admin UI Toolkit**

Build adaptive, professional admin dashboards, forms, data views, and navigation for desktop and web.

[Live Demo](https://adaptivekt.io/demo) • [Documentation](https://adaptivekt.io/docs) • [GitHub](https://github.com/adaptivekt/adaptive-kt)

---

## Features

### 🎯 Adaptive Layout System
- **Breakpoint-aware** components that respond to screen size (Compact, Medium, Expanded, Large)
- **Adaptive Grid** with proportional column distribution
- **Flexible Container** with responsive padding and content layout
- Works seamlessly across desktop, tablet, and mobile viewports

### 📊 Enterprise Data Views
- **AdaptiveDataView** — responsive table/card hybrid
- Column filtering, sorting, and custom actions
- Loading, empty, and error states
- Row selection and bulk actions

### 📋 Professional Forms
- **AdaptiveFormLayout** — responsive form structure
- Label positioning (top, left, inline) based on available space
- Validation states and error messaging
- Multi-column layouts for large screens

### 🧭 Smart Navigation
- **AdaptiveNavigationScaffold** — layout container for admin UIs
- Adaptive surfaces: Drawer, Sidebar, Bottom Navigation, Rail
- Drawer collapses on small screens; Rail appears on tablets
- Sidebar for large desktop displays

### 🎨 Component Library
- **AdaptiveButton** — variants (Primary, Secondary, Ghost, Danger) and sizes
- **AdaptiveCard** — surface containers for content
- **AdaptiveAvatar** — user images with intelligent initials fallback
- **AdaptiveBadge** — status indicators with tones
- **AdaptiveTextField** — form input with icons and validation
- **AdaptiveSearchField** — specialized search input
- **AdaptiveDropdownMenu** — inline menus and actions
- **AdaptiveSelect** — dropdown selection (where enabled)
- More coming in 1.0

### 🎭 Feedback States
- **EmptyState** — friendly messaging when no data exists
- **LoadingState** — animated loading indicator
- **ErrorState** — error messages with retry actions

### 🌐 Multiplatform Ready
- **JVM/Desktop** — via Compose Desktop
- **Android** — target enabled (validation pending)
- **iOS** — target declared (requires macOS)
- **Web/Wasm** — live browser demo included

---

## Platform Status

| Platform | Status | Notes |
|----------|--------|-------|
| JVM/Desktop | ✅ Stable | Compose Desktop, full feature set |
| Android | ⚠️ Build target | Ready to use; iOS validation pending |
| iOS | ⚠️ Declared | Requires macOS to build/test |
| Web/Wasm | ✅ Demo | Production builds work; library usage TBD |

---

## Quick Start

> **Note:** Maven publishing coming soon. For now, use AdaptiveKt as a source dependency by cloning this repository or adding it as a Git submodule.

### Adding to Your Project

```gradle
// settings.gradle.kts
include(":adaptive-kt:adaptive-core")
include(":adaptive-kt:adaptive-layout")
include(":adaptive-kt:adaptive-components")
include(":adaptive-kt:adaptive-forms")
include(":adaptive-kt:adaptive-data")
include(":adaptive-kt:adaptive-navigation")
include(":adaptive-kt:adaptive-feedback")

// build.gradle.kts
dependencies {
    implementation(project(":adaptive-core"))
    implementation(project(":adaptive-components"))
    implementation("org.jetbrains.compose.runtime:runtime:1.8.2")
    implementation("org.jetbrains.compose.foundation:foundation:1.8.2")
}
```

---

## Usage Examples

### Adaptive Layout

```kotlin
@Composable
fun MyAdminScreen() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    AdaptiveContent(
        adaptiveInfo = adaptiveInfo,
        minWidth = 320.dp,
        preferredWidth = 1200.dp,
    ) {
        // Your content here
    }
}
```

### Responsive Grid

```kotlin
@Composable
fun ProductGrid() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    AdaptiveGrid(
        adaptiveInfo = adaptiveInfo,
        columnCount = when (adaptiveInfo.breakpoint) {
            Compact -> 1
            Medium -> 2
            Expanded -> 3
            Large -> 4
        }
    ) {
        repeat(12) { i ->
            AdaptiveCard {
                Text("Product ${i + 1}")
            }
        }
    }
}
```

### Admin Navigation

```kotlin
@Composable
fun AdminLayout() {
    val adaptiveInfo = rememberAdaptiveInfo()
    var selectedNav by remember { mutableStateOf("dashboard") }
    
    AdaptiveNavigationScaffold(
        adaptiveInfo = adaptiveInfo,
        navigationItems = listOf(
            AdaptiveNavItem("dashboard", "Dashboard", Icons.Filled.Dashboard),
            AdaptiveNavItem("users", "Users", Icons.Filled.People),
            AdaptiveNavItem("settings", "Settings", Icons.Filled.Settings),
        ),
        selectedId = selectedNav,
        onNavigate = { selectedNav = it },
    ) { navigationBarHeight ->
        // Your screen content
    }
}
```

### Data View with Filtering

```kotlin
@Composable
fun UsersList() {
    var users by remember { mutableStateOf(listOf(...)) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    
    AdaptiveDataView(
        items = users,
        columns = listOf(
            DataColumn("Name", { it.name }),
            DataColumn("Email", { it.email }),
            DataColumn("Status", { it.status }),
        ),
        selectedIds = selectedIds,
        onSelectionChange = { selectedIds = it },
        onAction = { action, itemId ->
            when (action) {
                "edit" -> navigateToEdit(itemId)
                "delete" -> deleteUser(itemId)
            }
        },
        state = DataState.Content(users),
    )
}
```

### Responsive Form

```kotlin
@Composable
fun UserForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    
    AdaptiveFormLayout(
        labelPosition = LabelPosition.Auto, // Adapts to breakpoint
        columnCount = when (rememberAdaptiveInfo().breakpoint) {
            Compact, Medium -> 1
            else -> 2
        }
    ) {
        AdaptiveFormField(label = "Name") {
            AdaptiveTextField(
                value = name,
                onValueChange = { name = it },
            )
        }
        AdaptiveFormField(label = "Email") {
            AdaptiveTextField(
                value = email,
                onValueChange = { email = it },
            )
        }
        AdaptiveFormField(label = "Bio", span = 2) {
            AdaptiveTextField(
                value = bio,
                onValueChange = { bio = it },
                maxLines = 4,
            )
        }
    }
}
```

### Component Showcase

```kotlin
@Composable
fun ButtonShowcase() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Primary buttons
        AdaptiveButton(text = "Save", onClick = {}, variant = Primary)
        
        // Secondary button with icon
        AdaptiveButton(
            text = "Download",
            onClick = {},
            variant = Secondary,
            leadingIcon = { Icon(Icons.Filled.Download, "Download") },
        )
        
        // Danger button
        AdaptiveButton(text = "Delete", onClick = {}, variant = Danger)
        
        // Badge states
        AdaptiveBadge("Active", tone = Success)
        AdaptiveBadge("Inactive", tone = Neutral)
        
        // Avatar with fallback
        AdaptiveAvatar(name = "Alice Romero")
    }
}
```

---

## Running the Demo

### Desktop (JVM)

```bash
./gradlew :admin-demo:run
```

Opens a desktop window with a complete admin UI demo featuring all components and layouts.

### Web/Wasm Browser

**Development server:**
```bash
./gradlew :admin-demo:wasmJsBrowserDevelopmentRun
```

Open `http://localhost:8080` in your browser.

**Select a specific screen:**
```
http://localhost:8080/?screen=employees
http://localhost:8080/?screen=products
http://localhost:8080/?screen=dashboard
```

Available screens: `dashboard`, `employees`, `products`, `invoices`, `settings`, `components`

**Production build:**
```bash
./gradlew :admin-demo:wasmJsBrowserDistribution
```

Output: `admin-demo/build/dist/wasmJs/productionExecutable/`

### Visual Verification

**Desktop screenshots:**
```powershell
.\tools\capture-admin-demo.ps1
```

**Web screenshots (Playwright):**
```powershell
.\tools\capture-admin-demo-web.ps1
```

---

## Project Structure

```
adaptive-kt/
├── adaptive-core/           # Breakpoints, tokens, scopes
├── adaptive-layout/         # Grid, container components
├── adaptive-components/     # Button, badge, avatar, etc.
├── adaptive-navigation/     # Navigation scaffold, surfaces
├── adaptive-forms/          # Form layout and validation
├── adaptive-data/           # Data view, columns, filtering
├── adaptive-feedback/       # Empty, loading, error states
├── admin-demo/              # Demo app (desktop + Wasm)
├── docs/                    # Documentation
├── tools/                   # Build and capture scripts
└── build.gradle.kts         # Root build configuration
```

---

## Component API Reference

### Breakpoints
- `Compact` — < 600 dp (phone portrait)
- `Medium` — 600-840 dp (tablet portrait, phone landscape)
- `Expanded` — 840-1200 dp (tablet landscape)
- `Large` — ≥ 1200 dp (desktop)

### Core Types
- `AdaptiveInfo` — current breakpoint, window size, orientation
- `AdaptiveValue<T>` — adaptive values that change by breakpoint
- `AdaptiveBreakpoint` — current size category
- `AdaptiveTokens` — spacing, colors, radius, widths

### Navigation Modes
- `Drawer` — Compact/Medium screens (slides in)
- `BottomNavigation` — Mobile phones, navigation at bottom
- `Rail` — Expanded tablets, narrow navigation rail
- `Sidebar` — Large desktop, always-visible sidebar

For detailed API documentation, see [Component Guides](docs/components/README.md).

---

## Roadmap

### 1.0 (Next)
- ✅ Core layout system
- ✅ Navigation scaffold
- ✅ Component library
- ✅ Form layout
- ✅ Data view
- 🔄 Component documentation
- 🔄 GitHub Pages site
- 🔄 Wasm demo integration

### 1.1 (Future)
- 📋 Maven Central publishing
- 🎨 Dark mode / theme system
- 📱 Platform-specific presets (iOS design patterns)
- 🔍 MultiSelect component
- 📸 Visual regression testing
- 📚 Migration guides

### Beyond
- iOS native validation
- Figma design system export
- Storybook-like component browser
- Design tokens library

---

## Contributing

This is the initial publication of AdaptiveKt. Feedback, issues, and PRs are welcome.

- **Bugs:** File an issue with reproduction steps
- **Features:** Discuss in issues before starting work
- **Docs:** Corrections and examples appreciated

---

## License

AdaptiveKt is licensed under the [Apache License 2.0](LICENSE).

Free to use in commercial projects.

---

## Resources

- **Docs:** [Component Guides](docs/components/) | [Architecture](docs/adaptive-kt/ARCHITECTURE.md)
- **Demo:** [Live Wasm](https://adaptivekt.io/demo) | [Desktop Screenshots](build/visual-captures/)
- **Chat:** [GitHub Discussions](https://github.com/adaptivekt/adaptive-kt/discussions)

---

Built with ❤️ for admin UI developers.
