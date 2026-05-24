package io.github.adaptivekt.forms

import io.github.adaptivekt.core.AdaptiveBreakpoint
import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveFormHelpersTest {
    @Test
    fun `compact returns compact columns`() {
        val config = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 3, large = 4)
        assertEquals(1, columnsForBreakpoint(AdaptiveBreakpoint.Compact, config))
    }

    @Test
    fun `medium returns medium columns`() {
        val config = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 3, large = 4)
        assertEquals(2, columnsForBreakpoint(AdaptiveBreakpoint.Medium, config))
    }

    @Test
    fun `expanded returns expanded columns`() {
        val config = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 3, large = 4)
        assertEquals(3, columnsForBreakpoint(AdaptiveBreakpoint.Expanded, config))
    }

    @Test
    fun `large returns large columns`() {
        val config = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 3, large = 4)
        assertEquals(4, columnsForBreakpoint(AdaptiveBreakpoint.Large, config))
    }

    @Test
    fun `columns forced to at least one`() {
        val config = AdaptiveFormColumns(compact = 0, medium = 0, expanded = 0, large = 0)
        assertEquals(1, columnsForBreakpoint(AdaptiveBreakpoint.Large, config))
    }

    @Test
    fun `resolve full span returns all columns`() {
        assertEquals(3, resolveFieldSpan(FieldSpan.Full, 3))
    }

    @Test
    fun `resolve half span returns one on three columns`() {
        assertEquals(1, resolveFieldSpan(FieldSpan.Half, 3))
    }

    @Test
    fun `resolve third span returns one on three columns`() {
        assertEquals(1, resolveFieldSpan(FieldSpan.Third, 3))
    }

    @Test
    fun `resolve two thirds span returns two on three columns`() {
        assertEquals(2, resolveFieldSpan(FieldSpan.TwoThirds, 3))
    }

    @Test
    fun `resolve columns count within bounds`() {
        assertEquals(2, resolveFieldSpan(FieldSpan.Columns(2), 3))
    }

    @Test
    fun `resolve columns count above bounds becomes max`() {
        assertEquals(3, resolveFieldSpan(FieldSpan.Columns(99), 3))
    }

    @Test
    fun `resolve columns count below bounds becomes minimum`() {
        assertEquals(1, resolveFieldSpan(FieldSpan.Columns(0), 3))
    }

    @Test
    fun `any span with one active column resolves to one`() {
        assertEquals(1, resolveFieldSpan(FieldSpan.Half, 1))
        assertEquals(1, resolveFieldSpan(FieldSpan.Third, 1))
        assertEquals(1, resolveFieldSpan(FieldSpan.TwoThirds, 1))
        assertEquals(1, resolveFieldSpan(FieldSpan.Columns(2), 1))
    }
}
