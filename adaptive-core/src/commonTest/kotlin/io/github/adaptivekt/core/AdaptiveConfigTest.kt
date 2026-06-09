package io.github.adaptivekt.core

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveConfigTest {

    @Test
    fun testDefaultBreakpoints() {
        val config = AdaptiveConfig()
        
        // Test breakpoint ranges
        assertEquals(AdaptiveBreakpoint.Compact, config.breakpoints.resolve(300.dp))
        assertEquals(AdaptiveBreakpoint.Medium, config.breakpoints.resolve(700.dp))
        assertEquals(AdaptiveBreakpoint.Expanded, config.breakpoints.resolve(1000.dp))
        assertEquals(AdaptiveBreakpoint.Large, config.breakpoints.resolve(1300.dp))
        assertEquals(AdaptiveBreakpoint.Large, config.breakpoints.resolve(1600.dp))
    }

    @Test
    fun testLayoutInfoCalculations() {
        val config = AdaptiveConfig()
        
        // Compact window
        val compactInfo = AdaptiveLayoutInfo.resolve(
            width = 400.dp,
            height = 800.dp,
            config = config
        )
        
        assertTrue(compactInfo.isCompact)
        assertFalse(compactInfo.isMedium)
        assertFalse(compactInfo.isExpanded)
        assertEquals(4, compactInfo.columns)
        
        // Medium window
        val mediumInfo = AdaptiveLayoutInfo.resolve(
            width = 700.dp,
            height = 800.dp,
            config = config
        )
        
        assertFalse(mediumInfo.isCompact)
        assertTrue(mediumInfo.isMedium)
        assertFalse(mediumInfo.isExpanded)
        assertEquals(8, mediumInfo.columns)
        
        // Expanded window
        val expandedInfo = AdaptiveLayoutInfo.resolve(
            width = 1000.dp,
            height = 800.dp,
            config = config
        )
        
        assertFalse(expandedInfo.isCompact)
        assertFalse(expandedInfo.isMedium)
        assertTrue(expandedInfo.isExpanded)
        assertEquals(12, expandedInfo.columns)
    }

    @Test
    fun testCustomConfigOverrides() {
        val customBreakpoints = AdaptiveBreakpoints(
            compactMax = 500.dp,
            mediumMax = 800.dp,
            expandedMax = 1100.dp
        )
        
        val customLayoutPolicy = AdaptiveLayoutPolicy(
            compactColumns = 2,
            mediumColumns = 6,
            expandedColumns = 10,
            largeColumns = 14
        )
        
        val config = AdaptiveConfig(
            breakpoints = customBreakpoints,
            layout = customLayoutPolicy
        )
        
        // Custom breakpoints
        assertEquals(AdaptiveBreakpoint.Compact, config.breakpoints.resolve(450.dp))
        assertEquals(AdaptiveBreakpoint.Medium, config.breakpoints.resolve(550.dp))
        
        // Custom layout columns
        val compactInfo = AdaptiveLayoutInfo.resolve(width = 400.dp, height = 800.dp, config = config)
        assertEquals(2, compactInfo.columns)
        
        val mediumInfo = AdaptiveLayoutInfo.resolve(width = 700.dp, height = 800.dp, config = config)
        assertEquals(6, mediumInfo.columns)
        
        val expandedInfo = AdaptiveLayoutInfo.resolve(width = 1000.dp, height = 800.dp, config = config)
        assertEquals(10, expandedInfo.columns)
    }
}
