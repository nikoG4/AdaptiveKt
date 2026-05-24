package io.github.adaptivekt.core

import androidx.compose.runtime.Composable

@Suppress("MemberVisibilityCanBePrivate")
data class AdaptiveVisibility(
    val visibleOn: Set<AdaptiveBreakpoint>,
) {
    fun isVisibleAt(breakpoint: AdaptiveBreakpoint): Boolean =
        visibleOn.contains(breakpoint)

    companion object {
        fun from(start: AdaptiveBreakpoint): AdaptiveVisibility =
            AdaptiveVisibility(
                AdaptiveBreakpoint.values()
                    .filter { it.ordinal >= start.ordinal }
                    .toSet(),
            )

        fun until(end: AdaptiveBreakpoint): AdaptiveVisibility =
            AdaptiveVisibility(
                AdaptiveBreakpoint.values()
                    .filter { it.ordinal <= end.ordinal }
                    .toSet(),
            )

        fun only(vararg breakpoints: AdaptiveBreakpoint): AdaptiveVisibility =
            AdaptiveVisibility(breakpoints.toSet())

        fun all(): AdaptiveVisibility =
            AdaptiveVisibility(AdaptiveBreakpoint.values().toSet())
    }
}

@Composable
fun AdaptiveScope.AdaptiveVisibility(
    visibility: AdaptiveVisibility,
    content: @Composable () -> Unit,
) {
    if (visibility.isVisibleAt(adaptiveInfo.breakpoint)) {
        content()
    }
}

@Composable
fun AdaptiveScope.AdaptiveVisibility(
    visibleOn: Set<AdaptiveBreakpoint>,
    content: @Composable () -> Unit,
) {
    AdaptiveVisibility(visibleOn).let { visibility ->
        if (visibility.isVisibleAt(adaptiveInfo.breakpoint)) {
            content()
        }
    }
}
