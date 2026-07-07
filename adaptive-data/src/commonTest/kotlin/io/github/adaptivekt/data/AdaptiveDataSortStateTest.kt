package io.github.adaptivekt.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdaptiveDataSortStateTest {

    @Test
    fun normalizeRemovesDuplicateColumnIds() {
        val input = listOf(
            AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
            AdaptiveColumnSortState("a", AdaptiveSortDirection.Descending, AdaptiveSortPriority.Secondary),
            AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Tertiary),
        )
        val result = normalizeAdaptiveDataSortState(input)
        assertEquals(listOf("a", "b"), result.sortedColumns.map { it.columnId })
        assertEquals(AdaptiveSortDirection.Ascending, result.sortedColumns[0].direction)
    }

    @Test
    fun normalizeAssignsPrimarySecondaryTertiary() {
        val input = listOf(
            AdaptiveColumnSortState("c"),
            AdaptiveColumnSortState("a"),
            AdaptiveColumnSortState("b"),
        )
        val result = normalizeAdaptiveDataSortState(input)
        assertEquals(
            listOf(
                AdaptiveSortPriority.Primary,
                AdaptiveSortPriority.Secondary,
                AdaptiveSortPriority.Tertiary,
            ),
            result.sortedColumns.map { it.priority },
        )
    }

    @Test
    fun normalizeKeepsAtMostThreeColumns() {
        val input = listOf(
            AdaptiveColumnSortState("a"),
            AdaptiveColumnSortState("b"),
            AdaptiveColumnSortState("c"),
            AdaptiveColumnSortState("d"),
        )
        val result = normalizeAdaptiveDataSortState(input)
        assertEquals(3, result.sortedColumns.size)
        assertEquals(listOf("a", "b", "c"), result.sortedColumns.map { it.columnId })
    }

    @Test
    fun toggleNonSortableColumnIsNoOp() {
        val state = AdaptiveDataSortState()
        val result = state.toggleColumnSort("name", sortableColumnIds = emptySet())
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun toggleNewSortableColumnBecomesPrimaryAscending() {
        val state = AdaptiveDataSortState()
        val result = state.toggleColumnSort("name", sortableColumnIds = setOf("name", "status"))
        assertEquals(1, result.sortedColumns.size)
        assertEquals("name", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortDirection.Ascending, result.sortedColumns[0].direction)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
    }

    @Test
    fun toggleExistingAscendingBecomesDescending() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.toggleColumnSort("name", sortableColumnIds = setOf("name"))
        assertEquals(1, result.sortedColumns.size)
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[0].direction)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
    }

    @Test
    fun toggleExistingDescendingRemovesColumn() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Descending, AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.toggleColumnSort("name", sortableColumnIds = setOf("name"))
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun toggleNewColumnDemotesPreviousPrimaryToSecondary() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.toggleColumnSort("status", sortableColumnIds = setOf("name", "status"))
        assertEquals(2, result.sortedColumns.size)
        assertEquals("status", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals("name", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
    }

    @Test
    fun toggleNeverReturnsTwoPrimary() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Tertiary),
            ),
        )
        val result = state.toggleColumnSort("d", sortableColumnIds = setOf("a", "b", "c", "d"))
        assertEquals(3, result.sortedColumns.size)
        assertEquals(
            listOf(AdaptiveSortPriority.Primary, AdaptiveSortPriority.Secondary, AdaptiveSortPriority.Tertiary),
            result.sortedColumns.map { it.priority },
        )
    }

    @Test
    fun removeColumnSortRemovesAndReprioritizes() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Tertiary),
            ),
        )
        val result = state.removeColumnSort("a")
        assertEquals(2, result.sortedColumns.size)
        assertEquals("b", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals("c", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
    }

    @Test
    fun promoteColumnSortMovesExistingColumnToPrimary() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.promoteColumnSort("b")
        assertEquals(2, result.sortedColumns.size)
        assertEquals("b", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals("a", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
    }

    @Test
    fun promoteColumnSortUnknownColumnIsNoOp() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.promoteColumnSort("missing")
        assertEquals(listOf("a"), result.sortedColumns.map { it.columnId })
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
    }

    @Test
    fun toQuerySortReturnsNullAndAscendingWhenEmpty() {
        val (key, direction) = AdaptiveDataSortState().toQuerySort()
        assertNull(key)
        assertEquals(AdaptiveSortDirection.Ascending, direction)
    }

    @Test
    fun toQuerySortReturnsPrimaryColumnAndDirection() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Descending, AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("status", AdaptiveSortDirection.Ascending, AdaptiveSortPriority.Secondary),
            ),
        )
        val (key, direction) = state.toQuerySort()
        assertEquals("name", key)
        assertEquals(AdaptiveSortDirection.Descending, direction)
    }

    @Test
    fun toQuerySortReturnsAscendingFallbackWithoutPrimary() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Descending, AdaptiveSortPriority.Secondary),
            ),
        )
        val (key, direction) = state.toQuerySort()
        assertNull(key)
        assertEquals(AdaptiveSortDirection.Ascending, direction)
    }

    @Test
    fun toggleDescendsThroughCycleForSameColumn() {
        val ids = setOf("name")
        val first = AdaptiveDataSortState().toggleColumnSort("name", ids)
        assertEquals(AdaptiveSortDirection.Ascending, first.sortedColumns[0].direction)

        val second = first.toggleColumnSort("name", ids)
        assertEquals(AdaptiveSortDirection.Descending, second.sortedColumns[0].direction)

        val third = second.toggleColumnSort("name", ids)
        assertTrue(third.sortedColumns.isEmpty())
    }
}
