package io.github.adaptivekt.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines the layout constraints across different breakpoints.
 */
public data class AdaptiveLayoutPolicy(
    val compactColumns: Int = 4,
    val mediumColumns: Int = 8,
    val expandedColumns: Int = 12,
    val largeColumns: Int = 12,
    val contentMaxWidth: Dp = 1440.dp,
)

/**
 * Resolves the number of columns for a specific breakpoint based on the layout policy.
 */
public fun AdaptiveLayoutPolicy.columnsFor(breakpoint: AdaptiveBreakpoint): Int = when (breakpoint) {
    AdaptiveBreakpoint.Compact -> compactColumns
    AdaptiveBreakpoint.Medium -> mediumColumns
    AdaptiveBreakpoint.Expanded -> expandedColumns
    AdaptiveBreakpoint.Large -> largeColumns
}
