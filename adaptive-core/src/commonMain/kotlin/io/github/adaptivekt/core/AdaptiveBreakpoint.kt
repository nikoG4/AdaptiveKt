package io.github.adaptivekt.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AdaptiveBreakpoint {
    Compact,
    Medium,
    Expanded,
    Large,
}

fun breakpointForWidth(width: Dp): AdaptiveBreakpoint =
    when {
        width < 600.dp -> AdaptiveBreakpoint.Compact
        width < 840.dp -> AdaptiveBreakpoint.Medium
        width < 1200.dp -> AdaptiveBreakpoint.Expanded
        else -> AdaptiveBreakpoint.Large
    }
