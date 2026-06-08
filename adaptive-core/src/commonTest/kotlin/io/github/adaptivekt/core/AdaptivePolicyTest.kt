package io.github.adaptivekt.core

import androidx.compose.ui.unit.dp
import io.github.adaptivekt.navigation.AdaptiveNavigationMode
import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptivePolicyTest {

    @Test
    fun testDefaultSpacingPolicyResolution() {
        val policy = AdaptiveSpacingPolicy()
        
        assertEquals(16.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Compact))
        assertEquals(24.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Medium))
        assertEquals(32.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Expanded))
        assertEquals(40.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Large))
    }

    @Test
    fun testCustomSpacingPolicyResolution() {
        val policy = AdaptiveSpacingPolicy(
            compactPagePadding = 8.dp,
            expandedPagePadding = 48.dp
        )
        
        assertEquals(8.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Compact))
        assertEquals(24.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Medium))
        assertEquals(48.dp, policy.pagePaddingFor(AdaptiveBreakpoint.Expanded))
    }

    @Test
    fun testDefaultNavigationPolicyResolution() {
        val policy = AdaptiveNavigationPolicy()
        
        assertEquals(AdaptiveNavigationMode.Drawer, policy.resolve(AdaptiveBreakpoint.Compact))
        assertEquals(AdaptiveNavigationMode.NavigationRail, policy.resolve(AdaptiveBreakpoint.Medium))
        assertEquals(AdaptiveNavigationMode.Sidebar, policy.resolve(AdaptiveBreakpoint.Expanded))
        assertEquals(AdaptiveNavigationMode.Sidebar, policy.resolve(AdaptiveBreakpoint.Large))
    }

    @Test
    fun testCustomNavigationPolicyResolution() {
        // Example: An app that always uses bottom bar on small devices, but jumps straight to Sidebar on Medium
        val policy = AdaptiveNavigationPolicy(
            compact = AdaptiveNavigationMode.BottomNavigation,
            medium = AdaptiveNavigationMode.Sidebar
        )
        
        assertEquals(AdaptiveNavigationMode.BottomNavigation, policy.resolve(AdaptiveBreakpoint.Compact))
        assertEquals(AdaptiveNavigationMode.Sidebar, policy.resolve(AdaptiveBreakpoint.Medium))
        assertEquals(AdaptiveNavigationMode.Sidebar, policy.resolve(AdaptiveBreakpoint.Expanded))
    }
}
