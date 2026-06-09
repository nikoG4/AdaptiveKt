# Adaptive Store

A premium tech gear storefront built with [AdaptiveKt](https://github.com/nikoG4/AdaptiveKt) and Compose Multiplatform.

This demo showcases a modern, responsive e-commerce experience using AdaptiveKt's core components and layout system. It targets Web (Wasm), Android, Desktop, and iOS with a single shared codebase.

## Key Features

- **Premium UI:** A modern, visual-first design inspired by top-tier tech storefronts.
- **Reference Implementation:** This application serves as the primary showcase for `AdaptiveKt` layout primitives. It strictly adheres to library principles by avoiding manual `BoxWithConstraints` entirely. It demonstrates `AdaptiveApp` initialization, responsive `AdaptivePage` variants, `AdaptiveTwoPane` collapsing structures, and intelligent `AdaptiveGrid` defaults.
- **Rich Content:** 40+ products, multiple categories, and curated collections with realistic metadata.
- **Advanced Navigation:** Internal back stack management and browser history synchronization for a seamless Web experience using Hash routing.
- **Responsive Layout:** Adaptive grid/container systems plus `AdaptiveNavigationDefaults.storefrontBehavior()` for bottom navigation on compact and medium screens.
- **System Theme:** The demo uses `AdaptiveTheme(mode = AdaptiveThemeMode.System)` by default, with a mock System/Light/Dark selector in Settings.
- **E-commerce Flow:** Full shopping experience from landing page to product detail, cart, and checkout.
- **Vector Visuals:** Product cards use Compose vector/gradient visuals so the demo remains polished on Wasm without remote image loading.

## Multiplatform Support

- **Web (Wasm):** High-performance rendering using Kotlin/Wasm and Skia.
- **Android:** Native performance with a modern Material 3 look and feel.
- **Desktop:** Standalone application for Windows, macOS, and Linux.
- **iOS:** Shared UI components running natively on iOS devices.

## Live Demo
The Wasm distribution of this demo is continuously deployed to GitHub Pages and can be accessed at:
[Adaptive Store (Live Demo)](https://nikog4.github.io/AdaptiveKt/examples/ecommerce/)

## Getting Started

### Prerequisites

- Kotlin 2.1.21+
- Java 17+
- Android Studio / IntelliJ IDEA
- Node.js (for E2E tests)

### Build and Run

To run the Wasm version locally:

```powershell
# From project root
.\gradlew.bat -p examples\ecommerce-demo wasmJsBrowserRun
```

To build the Wasm distribution:

```powershell
.\gradlew.bat -p examples\ecommerce-demo wasmJsBrowserDistribution
```

## Testing

This project uses [Playwright](https://playwright.dev/) for end-to-end testing of the Wasm version.

```powershell
cd examples/ecommerce-demo
npm install
npx playwright install chromium
npm run test:e2e
```

To generate visual screenshots of the current build:

```powershell
npm run screenshots
```

## Attributions

See [ATTRIBUTIONS.md](./ATTRIBUTIONS.md) for details on icons and images used in this demo.

Remote product image URLs remain in mock data for future loader experiments, but the current UI renders local Compose visuals as stable fallbacks across Web, Android, Desktop and iOS.

## Library Backlog

See [ADAPTIVEKT_LIBRARY_BACKLOG.md](./ADAPTIVEKT_LIBRARY_BACKLOG.md) for features identified during this demo that are planned for the core AdaptiveKt library.
