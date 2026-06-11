# Android System Back Navigation Audit

## Current Navigation Model
AdaptiveKt uses `AdaptiveNavigator` for its core routing state. It tracks `currentRoute` and `backStack`. `canGoBack` is true when the backStack has more than 1 entry. Navigation is performed by modifying this stack and optionally syncing it with a `PlatformHistoryTracker`.

## Current Web History Behavior
On WasmJs, `PlatformHistoryTracker.wasmJs.kt` uses the standard browser History API. `historyTracker?.push(...)` calls `window.history.pushState()`, and the app listens to `window.addEventListener("popstate")` to trigger `navigator.syncFromBrowser()`. This provides a fully native experience on Web.

## Current Android Behavior
Android currently uses `actual fun getPlatformHistoryTracker(options: AdaptiveNavigationOptions): PlatformHistoryTracker? = null`. Because it provides no tracker, the `AdaptiveNavigator` only maintains local Compose state (`mutableStateListOf<R>`). It does NOT integrate with Android's system back button or gestures.

## Why Android system back is currently not handled
The `PlatformHistoryTracker` is purely a string-based path synchronization mechanism designed around browser history APIs. Android's system back navigation requires integrating with `androidx.activity.compose.BackHandler` (or the underlying `OnBackPressedDispatcher`). 
Since `AdaptiveNavigator` is pure state and decoupled from Activity contexts, it has no automatic way to intercept system back presses.

## Risks
- A user pressing the hardware or gesture back button on Android expects to go back within the app's internal navigation stack. Currently, pressing back will finish the Activity immediately, ignoring the internal backStack, resulting in immediate app exit.

## Proposed Solution
Introduce a multiplatform composable `AdaptiveNavigationBackHandler<R>(navigator: AdaptiveNavigator<R>, enabled: Boolean = true)`.
- **commonMain**: Expect declaration.
- **androidMain**: Actual declaration using `androidx.activity.compose.BackHandler(enabled = enabled && navigator.canGoBack) { navigator.goBack() }`.
- **jvmMain / wasmJsMain / iosMain**: Actual declaration as a no-op empty composable.

This allows applications to selectively opt-in to Android back handling by placing this composable at the root of their `AdaptiveNavigator` usages, keeping common state cleanly separated from AndroidX dependencies.
