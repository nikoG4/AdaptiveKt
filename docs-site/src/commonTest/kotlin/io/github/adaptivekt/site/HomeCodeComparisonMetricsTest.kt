package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeCodeComparisonMetricsTest {

    @Test
    fun ignoresBlankLines() {
        val code = """
            val a = 1
            
            val b = 2
            
        """.trimIndent()
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun ignoresPackageDeclarations() {
        val code = """
            package com.example.test
            val a = 1
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun ignoresImports() {
        val code = """
            import java.util.*
            import kotlin.math.max
            val a = 1
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun ignoresFullLineComments() {
        val code = """
            // This is a comment
            val a = 1
            // Another comment
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun ignoresSingleLineBlockComments() {
        val code = """
            /* single line block */
            val a = 1
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun ignoresMultilineBlockComments() {
        val code = """
            /*
             * multi line
             * comment
             */
            val a = 1
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun ignoresDocComments() {
        val code = """
            /**
             * Documentation.
             */
            val a = 1
        """.trimIndent()
        assertEquals(1, countMeaningfulCodeLines(code))
    }

    @Test
    fun countsInlineCommentsAsCode() {
        val code = """
            val a = 1 // inline comment
            val b = 2 /* inline block comment */
        """.trimIndent()
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun countsCodeAfterClosingBlockComment() {
        val code = """
            /* explanation */ val a = 1
            /* 
             * multiline
             */ val b = 2
        """.trimIndent()
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun doesNotTreatCommentMarkersInsideStringsAsComments() {
        val code = """
            val url = "https://example.com"
            val marker = "/* not a comment */"
            val marker2 = "// neither is this"
        """.trimIndent()
        assertEquals(3, countMeaningfulCodeLines(code))
    }

    @Test
    fun supportsCrLf() {
        val code = "val a = 1\r\nval b = 2\r\n"
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun supportsMissingFinalNewline() {
        val code = "val a = 1\nval b = 2"
        assertEquals(2, countMeaningfulCodeLines(code))
    }

    @Test
    fun handlesEmptyComposeSnippet() {
        val metrics = calculateCodeReduction("val a = 1", "")
        assertEquals(1, metrics.adaptiveLines)
        assertEquals(0, metrics.composeLines)
        assertEquals(0, metrics.savedLines)
        assertEquals(0, metrics.reductionPercent)
    }

    @Test
    fun clampsNegativeSavings() {
        val metrics = calculateCodeReduction("val a = 1\nval b = 2", "val c = 3")
        assertEquals(2, metrics.adaptiveLines)
        assertEquals(1, metrics.composeLines)
        assertEquals(0, metrics.savedLines)
        assertEquals(0, metrics.reductionPercent)
    }

    @Test
    fun calculatesIntegerPercentage() {
        val metrics = calculateCodeReduction("val a = 1", "val a = 1\nval b = 2\nval c = 3")
        assertEquals(1, metrics.adaptiveLines)
        assertEquals(3, metrics.composeLines)
        assertEquals(2, metrics.savedLines)
        assertEquals(66, metrics.reductionPercent) // (2 * 100) / 3 = 66
    }

    @Test
    fun realSnippetsRemainComparable() {
        val metrics = calculateCodeReduction(
            AdaptiveDataViewComparisonCode,
            PlainComposeDataViewComparisonCode
        )
        
        assertTrue(metrics.adaptiveLines > 0, "Adaptive snippet should have meaningful lines")
        assertTrue(metrics.composeLines > metrics.adaptiveLines, "Compose snippet should be longer")
        assertTrue(metrics.reductionPercent in 1..99, "Reduction percent should be between 1 and 99")
    }
}
