package io.github.adaptivekt.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun <R> AdaptiveNavigationBackHandler(
    navigator: AdaptiveNavigator<R>,
    enabled: Boolean,
) {
    BackHandler(enabled = enabled && navigator.canGoBack) {
        navigator.goBack()
    }
}
