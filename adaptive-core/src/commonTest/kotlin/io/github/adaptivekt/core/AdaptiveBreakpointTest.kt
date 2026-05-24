package io.github.adaptivekt.core

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveBreakpointTest {
    @Test
    fun `breakpointForWidth returns Compact for widths below 600 dp`() {
        assertEquals(AdaptiveBreakpoint.Compact, breakpointForWidth(0.dp))
        assertEquals(AdaptiveBreakpoint.Compact, breakpointForWidth(599.dp))
    }

    @Test
    fun `breakpointForWidth returns Medium for widths from 600 to 839 dp`() {
        assertEquals(AdaptiveBreakpoint.Medium, breakpointForWidth(600.dp))
        assertEquals(AdaptiveBreakpoint.Medium, breakpointForWidth(839.dp))
    }

    @Test
    fun `breakpointForWidth returns Expanded for widths from 840 to 1199 dp`() {
        assertEquals(AdaptiveBreakpoint.Expanded, breakpointForWidth(840.dp))
        assertEquals(AdaptiveBreakpoint.Expanded, breakpointForWidth(1199.dp))
    }

    @Test
    fun `breakpointForWidth returns Large for widths of 1200 dp or more`() {
        assertEquals(AdaptiveBreakpoint.Large, breakpointForWidth(1200.dp))
        assertEquals(AdaptiveBreakpoint.Large, breakpointForWidth(1600.dp))
    }
}
