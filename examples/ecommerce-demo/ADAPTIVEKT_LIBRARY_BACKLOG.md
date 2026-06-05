# AdaptiveKt Library Backlog

This document tracks features and improvements identified during the development of the E-commerce Demo that should be considered for inclusion in the core `AdaptiveKt` library.

## Navigation & Routing

- **AdaptiveBackStackNavigator:** A high-level component to manage a stack of screens with built-in state preservation.
- **Browser History Sync:** Automatic synchronization between internal app state and the browser's history API (`pushState`, `popstate`) for Wasm/JS.
- **Android BackHandler Integration:** Out-of-the-box support for the Android system back button.
- **Desktop Keyboard Shortcuts:** Default handling for `ESC` and back navigation keys.
- **Deep Linking:** A declarative way to map URLs to application states across all platforms.
- **Route Serialization:** Mechanisms to save and restore the navigation stack (e.g., during process death on Android).

## UI Components & UX

- **Image convenience components:** Better handling of remote images with built-in loading placeholders and error fallbacks.
- **SVG Icon System:** A more robust way to include and tint SVG icons without relying on font-based emojis which can be unreliable.
- **Improved Scaffolding:** More storefront-oriented layouts (e.g., `AdaptiveStorefrontScaffold`) that differ from the typical `AdaptiveNavigationScaffold` (which can feel more like an admin dashboard).
- **Form Validation:** Integrated validation logic for `AdaptiveFormLayout` fields.
- **Skeleton Loaders:** Primitive components to easily build "shimmer" loading states.

## Multiplatform Assets

- **Resource Management:** Simplification of multiplatform resource access for images, fonts, and SVGs across Android, Desktop, iOS, and Wasm.
