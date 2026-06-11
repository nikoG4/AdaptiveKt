package io.github.adaptivekt.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun <R> AdaptiveNavigationBackHandler(
    navigator: AdaptiveNavigator<R>,
    enabled: Boolean,
) {
    // No-op for Wasm JS (Handled by PlatformHistoryTracker)
}
