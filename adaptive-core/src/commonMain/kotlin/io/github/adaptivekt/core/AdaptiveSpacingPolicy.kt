package io.github.adaptivekt.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines the spacing conventions across different breakpoints.
 */
public data class AdaptiveSpacingPolicy(
    val compactPagePadding: Dp = 16.dp,
    val mediumPagePadding: Dp = 24.dp,
    val expandedPagePadding: Dp = 32.dp,
    val largePagePadding: Dp = 40.dp,
)

/**
 * Resolves the page padding for a specific breakpoint based on the spacing policy.
 */
public fun AdaptiveSpacingPolicy.pagePaddingFor(breakpoint: AdaptiveBreakpoint): Dp = when (breakpoint) {
    AdaptiveBreakpoint.Compact -> compactPagePadding
    AdaptiveBreakpoint.Medium -> mediumPagePadding
    AdaptiveBreakpoint.Expanded -> expandedPagePadding
    AdaptiveBreakpoint.Large -> largePagePadding
}
