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
}
