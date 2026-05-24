package io.github.adaptivekt.navigation

import androidx.compose.runtime.Composable

public data class AdaptiveNavItem(
    val id: String,
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
)
