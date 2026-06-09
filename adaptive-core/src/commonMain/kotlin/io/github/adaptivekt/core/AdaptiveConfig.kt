package io.github.adaptivekt.core

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Root configuration for the AdaptiveKt library layout system.
 */
public data class AdaptiveConfig(
    val breakpoints: AdaptiveBreakpoints = AdaptiveBreakpoints(),
    val navigation: AdaptiveNavigationPolicy = AdaptiveNavigationPolicy(),
    val layout: AdaptiveLayoutPolicy = AdaptiveLayoutPolicy(),
    val spacing: AdaptiveSpacingPolicy = AdaptiveSpacingPolicy(),
)

/**
 * Composition local holding the current [AdaptiveConfig].
 */
public val LocalAdaptiveConfig = staticCompositionLocalOf { AdaptiveConfig() }
