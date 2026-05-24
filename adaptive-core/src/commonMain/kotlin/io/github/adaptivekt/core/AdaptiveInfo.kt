package io.github.adaptivekt.core

data class AdaptiveInfo(
    val windowSize: AdaptiveWindowSize,
    val breakpoint: AdaptiveBreakpoint,
) {
    val isCompact: Boolean get() = breakpoint == AdaptiveBreakpoint.Compact
    val isMedium: Boolean get() = breakpoint == AdaptiveBreakpoint.Medium
    val isExpanded: Boolean get() = breakpoint == AdaptiveBreakpoint.Expanded
    val isLarge: Boolean get() = breakpoint == AdaptiveBreakpoint.Large

    val isAtLeastMedium: Boolean get() = breakpoint != AdaptiveBreakpoint.Compact
    val isAtLeastExpanded: Boolean get() = breakpoint == AdaptiveBreakpoint.Expanded || breakpoint == AdaptiveBreakpoint.Large
    val isAtLeastLarge: Boolean get() = breakpoint == AdaptiveBreakpoint.Large
}
