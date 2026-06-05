# Adaptive Store

A premium tech gear storefront built with [AdaptiveKt](https://github.com/nikoG4/AdaptiveKt) and Compose Multiplatform.

This demo showcases a modern, responsive e-commerce experience using AdaptiveKt's core components and layout system. It targets Web (Wasm), Android, Desktop, and iOS with a single shared codebase.

## Key Features

- **Premium UI:** A modern, visual-first design inspired by top-tier tech storefronts.
- **Rich Content:** 40+ products, multiple categories, and curated collections with realistic metadata.
- **Advanced Navigation:** Internal back stack management and browser history synchronization for a seamless Web experience.
- **Responsive Layout:** Adaptive grid and container systems that look great on both desktop monitors and mobile screens.
- **E-commerce Flow:** Full shopping experience from landing page to product detail, cart, and checkout.
- **Vector Icons:** High-quality SVG icons using Compose's `ImageVector` API to ensure crisp rendering on all platforms.

## Multiplatform Support

- **Web (Wasm):** High-performance rendering using Kotlin/Wasm and Skia.
- **Android:** Native performance with a modern Material 3 look and feel.
- **Desktop:** Standalone application for Windows, macOS, and Linux.
- **iOS:** Shared UI components running natively on iOS devices.

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

## Library Backlog

See [ADAPTIVEKT_LIBRARY_BACKLOG.md](./ADAPTIVEKT_LIBRARY_BACKLOG.md) for features identified during this demo that are planned for the core AdaptiveKt library.
