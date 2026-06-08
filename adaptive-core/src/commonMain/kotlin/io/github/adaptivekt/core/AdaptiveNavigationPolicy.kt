package io.github.adaptivekt.core

import io.github.adaptivekt.navigation.AdaptiveNavigationMode

/**
 * Defines the navigation mode conventions across different breakpoints.
 */
public data class AdaptiveNavigationPolicy(
    val compact: AdaptiveNavigationMode = AdaptiveNavigationMode.Drawer,
    val medium: AdaptiveNavigationMode = AdaptiveNavigationMode.NavigationRail,
    val expanded: AdaptiveNavigationMode = AdaptiveNavigationMode.Sidebar,
    val large: AdaptiveNavigationMode = AdaptiveNavigationMode.Sidebar,
)

/**
 * Resolves the navigation mode for a specific breakpoint based on the policy.
 */
public fun AdaptiveNavigationPolicy.resolve(breakpoint: AdaptiveBreakpoint): AdaptiveNavigationMode = when (breakpoint) {
    AdaptiveBreakpoint.Compact -> compact
    AdaptiveBreakpoint.Medium -> medium
    AdaptiveBreakpoint.Expanded -> expanded
    AdaptiveBreakpoint.Large -> large
}
