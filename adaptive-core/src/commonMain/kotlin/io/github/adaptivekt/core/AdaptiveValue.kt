package io.github.adaptivekt.core

fun <T> adaptiveValue(
    breakpoint: AdaptiveBreakpoint,
    compact: T,
    medium: T? = null,
    expanded: T? = null,
    large: T? = null,
): T =
    when (breakpoint) {
        AdaptiveBreakpoint.Compact -> compact
        AdaptiveBreakpoint.Medium -> medium ?: compact
        AdaptiveBreakpoint.Expanded -> expanded ?: medium ?: compact
        AdaptiveBreakpoint.Large -> large ?: expanded ?: medium ?: compact
    }
