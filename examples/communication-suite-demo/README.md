# Adaptive Chat Showcase

Adaptive Chat is a frontend-only communication workspace built with **AdaptiveKt** and **Compose Multiplatform** for Android, Desktop/JVM, and Web/Wasm.

- [Open the live Web/Wasm demo](https://nikog4.github.io/AdaptiveKt/examples/communication-suite/)
- [Read the public architecture guide](../../docs/examples/adaptive-chat.md)

## What it demonstrates

- `AdaptiveNavigationScaffold` switching between compact bottom navigation and wider rail/sidebar surfaces.
- Contextual navigation visibility through `isNavigationVisible`, hiding the compact bottom bar while a conversation detail is open.
- `AdaptiveListDetailScaffold` switching between stacked compact navigation and side-by-side list/detail panes.
- Chat, contacts, calls, and settings flows backed by deterministic in-memory mock data.
- Theme-aware AdaptiveKt components across light and dark modes.

## Run locally

From the repository root:

```powershell
.\gradlew.bat -p examples/communication-suite-demo desktopRun
.\gradlew.bat -p examples/communication-suite-demo wasmJsBrowserDevelopmentRun
.\gradlew.bat -p examples/communication-suite-demo assembleDebug
```

## Limitations

This is a UI architecture showcase. Data is local and non-persistent, and there is no authentication, backend, push notification service, or real network transport.
