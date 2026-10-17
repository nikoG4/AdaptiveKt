package io.github.adaptivekt.admin.demo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PremiumDashboardLayoutTest {
    @Test
    fun metricsUseResponsiveColumns() {
        assertEquals(12, premiumDashboardMetricSpan(isCompact = true, isMedium = false))
        assertEquals(6, premiumDashboardMetricSpan(isCompact = false, isMedium = true))
        assertEquals(3, premiumDashboardMetricSpan(isCompact = false, isMedium = false))
    }

    @Test
    fun everyRevenueRangeHasBalancedNonNegativeData() {
        PremiumTrendRange.entries.forEach { range ->
            val data = premiumRevenueSeries(range)
            assertTrue(data.values.isNotEmpty())
            assertEquals(data.labels.size, data.values.size)
            assertTrue(data.values.all { it >= 0 })
        }
    }

    @Test
    fun chartHeightsRemainSafeWithEmptyOrUnexpectedValues() {
        assertEquals(0, premiumChartBarHeight(5, maxValue = 10, chartHeight = 0))
        assertEquals(4, premiumChartBarHeight(4, maxValue = 0))
        assertEquals(4, premiumChartBarHeight(-20, maxValue = 100))
        assertEquals(112, premiumChartBarHeight(300, maxValue = 100))
    }
}
