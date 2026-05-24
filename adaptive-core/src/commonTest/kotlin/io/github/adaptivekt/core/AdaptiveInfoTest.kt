package io.github.adaptivekt.core

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveInfoTest {
    @Test
    fun `info helpers reflect breakpoint state correctly`() {
        val compactInfo = AdaptiveInfo(AdaptiveWindowSize(500.dp, 800.dp), AdaptiveBreakpoint.Compact)
        assertTrue(compactInfo.isCompact)
        assertFalse(compactInfo.isMedium)
        assertFalse(compactInfo.isExpanded)
        assertFalse(compactInfo.isLarge)
        assertFalse(compactInfo.isAtLeastMedium)
        assertFalse(compactInfo.isAtLeastExpanded)
        assertFalse(compactInfo.isAtLeastLarge)

        val mediumInfo = AdaptiveInfo(AdaptiveWindowSize(700.dp, 800.dp), AdaptiveBreakpoint.Medium)
        assertFalse(mediumInfo.isCompact)
        assertTrue(mediumInfo.isMedium)
        assertFalse(mediumInfo.isExpanded)
        assertFalse(mediumInfo.isLarge)
        assertTrue(mediumInfo.isAtLeastMedium)
        assertFalse(mediumInfo.isAtLeastExpanded)
        assertFalse(mediumInfo.isAtLeastLarge)

        val expandedInfo = AdaptiveInfo(AdaptiveWindowSize(900.dp, 800.dp), AdaptiveBreakpoint.Expanded)
        assertFalse(expandedInfo.isCompact)
        assertFalse(expandedInfo.isMedium)
        assertTrue(expandedInfo.isExpanded)
        assertFalse(expandedInfo.isLarge)
        assertTrue(expandedInfo.isAtLeastMedium)
        assertTrue(expandedInfo.isAtLeastExpanded)
        assertFalse(expandedInfo.isAtLeastLarge)

        val largeInfo = AdaptiveInfo(AdaptiveWindowSize(1300.dp, 900.dp), AdaptiveBreakpoint.Large)
        assertFalse(largeInfo.isCompact)
        assertFalse(largeInfo.isMedium)
        assertFalse(largeInfo.isExpanded)
        assertTrue(largeInfo.isLarge)
        assertTrue(largeInfo.isAtLeastMedium)
        assertTrue(largeInfo.isAtLeastExpanded)
        assertTrue(largeInfo.isAtLeastLarge)
    }
}
