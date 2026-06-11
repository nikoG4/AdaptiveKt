package io.github.adaptivekt.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun <R> AdaptiveNavigationBackHandler(
    navigator: AdaptiveNavigator<R>,
    enabled: Boolean = true,
)
