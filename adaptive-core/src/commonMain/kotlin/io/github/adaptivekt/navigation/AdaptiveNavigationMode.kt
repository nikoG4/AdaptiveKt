package io.github.adaptivekt.navigation

import io.github.adaptivekt.core.AdaptiveBreakpoint

public enum class AdaptiveNavigationMode {
    Drawer,
    BottomNavigation,
    NavigationRail,
    Sidebar,
}

public fun navigationModeForBreakpoint(
    breakpoint: AdaptiveBreakpoint,
    preferBottomNavigationOnCompact: Boolean = false,
): AdaptiveNavigationMode = when (breakpoint) {
    AdaptiveBreakpoint.Compact -> if (preferBottomNavigationOnCompact) {
        AdaptiveNavigationMode.BottomNavigation
    } else {
        AdaptiveNavigationMode.Drawer
    }
    AdaptiveBreakpoint.Medium -> AdaptiveNavigationMode.NavigationRail
    AdaptiveBreakpoint.Expanded,
    AdaptiveBreakpoint.Large -> AdaptiveNavigationMode.Sidebar
}
