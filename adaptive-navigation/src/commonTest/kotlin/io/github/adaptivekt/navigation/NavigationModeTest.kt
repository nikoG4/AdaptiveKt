package io.github.adaptivekt.navigation

import io.github.adaptivekt.core.AdaptiveBreakpoint
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationModeTest {
    @Test
    fun `compact without bottom navigation returns drawer`() {
        assertEquals(
            AdaptiveNavigationMode.Drawer,
            navigationModeForBreakpoint(AdaptiveBreakpoint.Compact, preferBottomNavigationOnCompact = false),
        )
    }

    @Test
    fun `compact with bottom navigation returns bottom navigation`() {
        assertEquals(
            AdaptiveNavigationMode.BottomNavigation,
            navigationModeForBreakpoint(AdaptiveBreakpoint.Compact, preferBottomNavigationOnCompact = true),
        )
    }

    @Test
    fun `medium returns navigation rail`() {
        assertEquals(
            AdaptiveNavigationMode.NavigationRail,
            navigationModeForBreakpoint(AdaptiveBreakpoint.Medium),
        )
    }

    @Test
    fun `expanded returns sidebar`() {
        assertEquals(
            AdaptiveNavigationMode.Sidebar,
            navigationModeForBreakpoint(AdaptiveBreakpoint.Expanded),
        )
    }

    @Test
    fun `large returns sidebar`() {
        assertEquals(
            AdaptiveNavigationMode.Sidebar,
            navigationModeForBreakpoint(AdaptiveBreakpoint.Large),
        )
    }

    @Test
    fun `admin behavior resolves classic responsive placements`() {
        val behavior = AdaptiveNavigationDefaults.adminBehavior()

        assertEquals(AdaptiveNavigationPlacement.Drawer, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Compact, behavior))
        assertEquals(AdaptiveNavigationPlacement.Rail, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Medium, behavior))
        assertEquals(AdaptiveNavigationPlacement.Sidebar, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Expanded, behavior))
        assertEquals(AdaptiveNavigationPlacement.Sidebar, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Large, behavior))
    }

    @Test
    fun `storefront behavior keeps mobile and medium on bottom bar`() {
        val behavior = AdaptiveNavigationDefaults.storefrontBehavior()

        assertEquals(AdaptiveNavigationPlacement.BottomBar, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Compact, behavior))
        assertEquals(AdaptiveNavigationPlacement.BottomBar, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Medium, behavior))
        assertEquals(AdaptiveNavigationPlacement.Hidden, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Expanded, behavior))
        assertEquals(AdaptiveNavigationPlacement.Hidden, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Large, behavior))
        assertEquals(AdaptiveNavigationOverflowBehavior.MoreMenu, behavior.overflowBehavior)
        assertEquals(5, behavior.bottomBarVisibleItemCount)
    }

    @Test
    fun `custom behavior resolves configured placements`() {
        val behavior = AdaptiveNavigationBehavior(
            compact = AdaptiveNavigationPlacement.Hidden,
            medium = AdaptiveNavigationPlacement.BottomBar,
            expanded = AdaptiveNavigationPlacement.Drawer,
            large = AdaptiveNavigationPlacement.Rail,
            overflowBehavior = AdaptiveNavigationOverflowBehavior.Scroll,
        )

        assertEquals(AdaptiveNavigationPlacement.Hidden, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Compact, behavior))
        assertEquals(AdaptiveNavigationPlacement.BottomBar, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Medium, behavior))
        assertEquals(AdaptiveNavigationPlacement.Drawer, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Expanded, behavior))
        assertEquals(AdaptiveNavigationPlacement.Rail, resolveAdaptiveNavigationPlacement(AdaptiveBreakpoint.Large, behavior))
        assertEquals(AdaptiveNavigationOverflowBehavior.Scroll, behavior.overflowBehavior)
    }
}
