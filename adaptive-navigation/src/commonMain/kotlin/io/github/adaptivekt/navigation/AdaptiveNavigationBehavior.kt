package io.github.adaptivekt.navigation

import io.github.adaptivekt.core.AdaptiveBreakpoint

public enum class AdaptiveNavigationPlacement {
    Hidden,
    Sidebar,
    Rail,
    BottomBar,
    Drawer,
}

public enum class AdaptiveNavigationOverflowBehavior {
    Scroll,
    MoreMenu,
    Wrap,
    Clip,
}

public data class AdaptiveNavigationBehavior(
    val compact: AdaptiveNavigationPlacement = AdaptiveNavigationPlacement.Drawer,
    val medium: AdaptiveNavigationPlacement = AdaptiveNavigationPlacement.Rail,
    val expanded: AdaptiveNavigationPlacement = AdaptiveNavigationPlacement.Sidebar,
    val large: AdaptiveNavigationPlacement = AdaptiveNavigationPlacement.Sidebar,
    val overflowBehavior: AdaptiveNavigationOverflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu,
    val drawerGesturesEnabled: Boolean = true,
    val bottomBarVisibleItemCount: Int = 4,
    val railVisibleItemCount: Int = 6,
)

public object AdaptiveNavigationDefaults {
    public fun adminBehavior(): AdaptiveNavigationBehavior = AdaptiveNavigationBehavior()

    public fun storefrontBehavior(): AdaptiveNavigationBehavior = AdaptiveNavigationBehavior(
        compact = AdaptiveNavigationPlacement.BottomBar,
        medium = AdaptiveNavigationPlacement.BottomBar,
        expanded = AdaptiveNavigationPlacement.Hidden,
        large = AdaptiveNavigationPlacement.Hidden,
        overflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu,
        bottomBarVisibleItemCount = 5,
    )

    public fun compactBottomBarBehavior(): AdaptiveNavigationBehavior = AdaptiveNavigationBehavior(
        compact = AdaptiveNavigationPlacement.BottomBar,
        medium = AdaptiveNavigationPlacement.Rail,
        expanded = AdaptiveNavigationPlacement.Sidebar,
        large = AdaptiveNavigationPlacement.Sidebar,
        overflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu,
    )
}

public fun resolveAdaptiveNavigationPlacement(
    breakpoint: AdaptiveBreakpoint,
    behavior: AdaptiveNavigationBehavior,
): AdaptiveNavigationPlacement = when (breakpoint) {
    AdaptiveBreakpoint.Compact -> behavior.compact
    AdaptiveBreakpoint.Medium -> behavior.medium
    AdaptiveBreakpoint.Expanded -> behavior.expanded
    AdaptiveBreakpoint.Large -> behavior.large
}
