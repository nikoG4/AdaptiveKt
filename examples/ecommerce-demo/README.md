# Adaptive Store Ecommerce Demo

Adaptive Store is a complete, multiplatform ecommerce frontend demonstration built exclusively with Kotlin Multiplatform and **AdaptiveKt** (`0.1.0-alpha01`).

It showcases the core promise of AdaptiveKt: a single, unified UI codebase in `commonMain` that seamlessly adapts to Android, Desktop (JVM), Web (Wasm), and iOS, feeling native and responsive across all device sizes.

## Features & Implementation

- **Fully Multiplatform Frontend**: No platform-specific UI. 100% of the UI (screens, navigation, styling) resides in `commonMain`.
- **Adaptive Components**: Utilizes `AdaptiveNavigationScaffold`, `AdaptiveCard`, `AdaptiveGrid`, `AdaptiveButton`, and `AdaptiveEmptyState` directly from the AdaptiveKt Maven Central artifacts.
- **Responsive Layout**: Adjusts flawlessly from compact mobile screens to expanded desktop layouts using AdaptiveKt's built-in grid and container logic.
- **Demo State Management**: Includes localized mock state for Auth, Cart, Wishlist, Products, and Orders. No backend is required.

## Platforms Supported

- **Desktop (JVM)**: Full support
- **Web (Wasm)**: Full support
- **Android**: Full support
- **iOS**: Configured and supported (Requires macOS/Xcode host to compile and link the binary).

## Limitations

- **Frontend Only**: Contains mocked authentication and checkout flows. There is no real backend, database, or API integration.
- **Dependencies**: Resolves `io.github.nikog4.adaptivekt:*:0.1.0-alpha01` from Maven Central. (Note: A temporary fallback to `mavenLocal()` may be present if Maven Central indexing was delayed during development).

## Running the App

### Desktop
```bash
./gradlew -p examples/ecommerce-demo desktopRun
```
*Compiles and runs the standalone desktop JVM application.*

### Web / Wasm
```bash
./gradlew -p examples/ecommerce-demo wasmJsBrowserDistribution
```
*Generates the Wasm artifacts. You can then serve them using your preferred local server.*

### Android
```bash
./gradlew -p examples/ecommerce-demo assembleDebug
```
*Builds the Android debug APK. Alternatively, open the project in Android Studio and hit "Run".*

### iOS
```bash
./gradlew -p examples/ecommerce-demo linkDebugFrameworkIosSimulatorArm64
```
*Note: iOS target is fully configured. Full iOS binary verification requires a macOS host environment with Xcode installed.*

## AdaptiveKt Usage

This demo leverages the following AdaptiveKt packages:
- `adaptive-core`: Theming and tokens (`AdaptiveTheme`).
- `adaptive-components`: Reusable primitives (`AdaptiveButton`, `AdaptiveCard`).
- `adaptive-layout`: Grids and containers (`AdaptiveContainer`, `AdaptiveGrid`).
- `adaptive-navigation`: Responsive routing scaffolding (`AdaptiveNavigationScaffold`, `AdaptiveNavItem`).
- `adaptive-feedback`: Status UI elements (`AdaptiveEmptyState`).
