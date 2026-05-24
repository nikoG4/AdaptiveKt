package io.github.adaptivekt.layout

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveGridAlgorithmTest {
    @Test
    fun `coerce span below 1 becomes 1`() {
        assertEquals(1, coerceSpan(0, 12))
    }

    @Test
    fun `coerce span above columns becomes columns`() {
        assertEquals(12, coerceSpan(42, 12))
    }

    @Test
    fun `6 plus 6 remain in single row`() {
        val items = listOf(GridItem(6) { }, GridItem(6) { })
        val rows = groupGridItemsIntoRows(items, 12)
        assertEquals(1, rows.size)
        assertEquals(2, rows[0].size)
    }

    @Test
    fun `8 plus 4 remain in single row`() {
        val items = listOf(GridItem(8) { }, GridItem(4) { })
        val rows = groupGridItemsIntoRows(items, 12)
        assertEquals(1, rows.size)
    }

    @Test
    fun `8 plus 8 becomes two rows`() {
        val items = listOf(GridItem(8) { }, GridItem(8) { })
        val rows = groupGridItemsIntoRows(items, 12)
        assertEquals(2, rows.size)
    }

    @Test
    fun `4 plus 4 plus 4 in single row`() {
        val items = listOf(GridItem(4) { }, GridItem(4) { }, GridItem(4) { })
        val rows = groupGridItemsIntoRows(items, 12)
        assertEquals(1, rows.size)
    }

    @Test
    fun `4 plus 4 plus 6 becomes two rows`() {
        val items = listOf(GridItem(4) { }, GridItem(4) { }, GridItem(6) { })
        val rows = groupGridItemsIntoRows(items, 12)
        assertEquals(2, rows.size)
    }

    @Test
    fun `remaining columns for 6 plus 6 is 0`() {
        val row = listOf(GridItem(6) { }, GridItem(6) { })
        assertEquals(0, remainingColumns(row, 12))
    }

    @Test
    fun `remaining columns for 8 plus 4 is 0`() {
        val row = listOf(GridItem(8) { }, GridItem(4) { })
        assertEquals(0, remainingColumns(row, 12))
    }

    @Test
    fun `remaining columns for single 8 is 4`() {
        val row = listOf(GridItem(8) { })
        assertEquals(4, remainingColumns(row, 12))
    }

    @Test
    fun `remaining columns for 4 plus 4 is 4`() {
        val row = listOf(GridItem(4) { }, GridItem(4) { })
        assertEquals(4, remainingColumns(row, 12))
    }
}
