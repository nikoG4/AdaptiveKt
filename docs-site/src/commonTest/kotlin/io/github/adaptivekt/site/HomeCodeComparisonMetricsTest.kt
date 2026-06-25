package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeCodeComparisonMetricsTest {

    @Test
    fun testCountMeaningfulCodeLines_excludesEmptyLines() {
        val code = """
            val a = 1
            
            val b = 2
        """.trimIndent()
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun testCountMeaningfulCodeLines_excludesImportsAndPackages() {
        val code = """
            package com.example
            import java.util.*
            import kotlin.math.*
            
            val a = 1
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun testCountMeaningfulCodeLines_excludesFullLineComments() {
        val code = """
            // This is a comment
            val a = 1 // inline comment
            /* multi line comment on single line */
            val b = 2
        """.trimIndent()
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun testCalculateCodeReduction_correctMetrics() {
        val adaptive = """
            val a = 1
        """.trimIndent() // 1 line
        
        val compose = """
            val a = 1
            val b = 2
            val c = 3
        """.trimIndent() // 3 lines

        val metrics = calculateCodeReduction(adaptive, compose)
        assertEquals(1, metrics.adaptiveLines)
        assertEquals(3, metrics.composeLines)
        assertEquals(2, metrics.savedLines)
        assertEquals(66, metrics.reductionPercent) // (2 * 100) / 3 = 66
    }

    @Test
    fun testCalculateCodeReduction_zeroComposeLines() {
        val adaptive = "val a = 1"
        val compose = ""

        val metrics = calculateCodeReduction(adaptive, compose)
        assertEquals(1, metrics.adaptiveLines)
        assertEquals(0, metrics.composeLines)
        assertEquals(0, metrics.savedLines) // max(0, 0 - 1)
        assertEquals(0, metrics.reductionPercent)
    }

    @Test
    fun testRealSnippetsProducePositiveReduction() {
        val metrics = calculateCodeReduction(
            AdaptiveDataViewComparisonCode,
            PlainComposeDataViewComparisonCode
        )
        
        assertTrue(metrics.adaptiveLines > 0, "Adaptive snippet should have meaningful lines")
        assertTrue(metrics.composeLines > metrics.adaptiveLines, "Compose snippet should be longer")
        assertTrue(metrics.reductionPercent in 1..99, "Reduction percent should be between 1 and 99")
    }
}
