package io.github.adaptivekt.core

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveVisibilityTest {
    @Test
    fun `from creates visibility from the requested breakpoint including larger sizes`() {
        val visibility = AdaptiveVisibility.from(AdaptiveBreakpoint.Medium)
        assertFalse(visibility.isVisibleAt(AdaptiveBreakpoint.Compact))
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Medium))
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Expanded))
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Large))
    }

    @Test
    fun `until creates visibility until the requested breakpoint including smaller sizes`() {
        val visibility = AdaptiveVisibility.until(AdaptiveBreakpoint.Medium)
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Compact))
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Medium))
        assertFalse(visibility.isVisibleAt(AdaptiveBreakpoint.Expanded))
        assertFalse(visibility.isVisibleAt(AdaptiveBreakpoint.Large))
    }

    @Test
    fun `only creates visibility for exact breakpoint values`() {
        val visibility = AdaptiveVisibility.only(AdaptiveBreakpoint.Compact, AdaptiveBreakpoint.Large)
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Compact))
        assertFalse(visibility.isVisibleAt(AdaptiveBreakpoint.Medium))
        assertFalse(visibility.isVisibleAt(AdaptiveBreakpoint.Expanded))
        assertTrue(visibility.isVisibleAt(AdaptiveBreakpoint.Large))
    }
}
