package io.github.adaptivekt.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines the maximum widths for each adaptive breakpoint.
 */
public data class AdaptiveBreakpoints(
    val compactMax: Dp = 599.dp,
    val mediumMax: Dp = 839.dp,
    val expandedMax: Dp = 1199.dp,
)

/**
 * Resolves the given width into an AdaptiveBreakpoint using the provided breakpoints configuration.
 */
public fun AdaptiveBreakpoints.resolve(width: Dp): AdaptiveBreakpoint = when {
    width <= compactMax -> AdaptiveBreakpoint.Compact
    width <= mediumMax -> AdaptiveBreakpoint.Medium
    width <= expandedMax -> AdaptiveBreakpoint.Expanded
    else -> AdaptiveBreakpoint.Large
}
