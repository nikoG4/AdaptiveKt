package io.github.adaptivekt.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdaptiveDataSortStateTest {

    @Test
    fun normalizeRemovesDuplicateColumnIds() {
        val input = listOf(
            AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
            AdaptiveColumnSortState("a", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
            AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Tertiary),
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
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
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
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.toggleColumnSort("name", sortableColumnIds = setOf("name"))
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun toggleNewColumnDemotesPreviousPrimaryToSecondary() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
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
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Tertiary),
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
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Tertiary),
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
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
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
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
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
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("status", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Secondary),
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
                AdaptiveColumnSortState("name", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
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

    @Test
    fun constructorRejectsMoreThanThreeSortColumns() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveDataSortState(
                sortedColumns = listOf(
                    AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                    AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
                    AdaptiveColumnSortState("c", priority = AdaptiveSortPriority.Tertiary),
                    AdaptiveColumnSortState("d", priority = AdaptiveSortPriority.Primary),
                ),
            )
        }
    }

    @Test
    fun constructorRejectsDuplicateColumnIds() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveDataSortState(
                sortedColumns = listOf(
                    AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                    AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Secondary),
                ),
            )
        }
    }

    @Test
    fun constructorRejectsDuplicatePriorities() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveDataSortState(
                sortedColumns = listOf(
                    AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                    AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Primary),
                ),
            )
        }
    }

    @Test
    fun primarySortReturnsPrimaryColumn() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Secondary),
            ),
        )
        assertEquals("a", state.primarySort?.columnId)
        assertEquals(AdaptiveSortDirection.Descending, state.primarySort?.direction)
    }

    @Test
    fun secondarySortReturnsSecondaryColumn() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
            ),
        )
        assertEquals("b", state.secondarySort?.columnId)
        assertEquals(AdaptiveSortDirection.Descending, state.secondarySort?.direction)
    }

    @Test
    fun tertiarySortReturnsTertiaryColumn() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Tertiary),
            ),
        )
        assertEquals("c", state.tertiarySort?.columnId)
        assertEquals(AdaptiveSortDirection.Descending, state.tertiarySort?.direction)
    }

    @Test
    fun isSortedIsFalseForEmptyState() {
        assertEquals(false, AdaptiveDataSortState().isSorted)
    }

    @Test
    fun isSortedIsTrueForNonEmptyState() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary)),
        )
        assertEquals(true, state.isSorted)
    }

    @Test
    fun toggleSecondaryAscendingKeepsSecondaryPriorityAndChangesToDescending() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.toggleColumnSort("b", sortableColumnIds = setOf("a", "b"))
        assertEquals(2, result.sortedColumns.size)
        assertEquals("a", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals(AdaptiveSortDirection.Ascending, result.sortedColumns[0].direction)
        assertEquals("b", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[1].direction)
    }

    @Test
    fun toggleTertiaryAscendingKeepsTertiaryPriorityAndChangesToDescending() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Tertiary),
            ),
        )
        val result = state.toggleColumnSort("c", sortableColumnIds = setOf("a", "b", "c"))
        assertEquals(3, result.sortedColumns.size)
        assertEquals("c", result.sortedColumns[2].columnId)
        assertEquals(AdaptiveSortPriority.Tertiary, result.sortedColumns[2].priority)
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[2].direction)
    }

    @Test
    fun toggleSecondaryDescendingRemovesItAndReprioritizesRemainingColumns() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Tertiary),
            ),
        )
        val result = state.toggleColumnSort("b", sortableColumnIds = setOf("a", "b", "c"))
        assertEquals(2, result.sortedColumns.size)
        assertEquals("a", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals("c", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
    }

    @Test
    fun promoteColumnSortMovesSecondaryToPrimary() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.promoteColumnSort("b")
        assertEquals("b", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals("a", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
    }

    @Test
    fun promoteColumnSortPreservesDirection() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.promoteColumnSort("b")
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[0].direction)
    }

    @Test
    fun setColumnSortDirectionUpdatesExistingColumn() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.setColumnSortDirection("b", AdaptiveSortDirection.Descending)
        assertEquals(2, result.sortedColumns.size)
        assertEquals("b", result.sortedColumns[1].columnId)
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[1].direction)
        assertEquals(AdaptiveSortPriority.Secondary, result.sortedColumns[1].priority)
        assertEquals(AdaptiveSortDirection.Ascending, result.sortedColumns[0].direction)
    }

    @Test
    fun setColumnSortDirectionMissingColumnIsNoOp() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Ascending, priority = AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.setColumnSortDirection("missing", AdaptiveSortDirection.Descending)
        assertEquals(state.sortedColumns, result.sortedColumns)
    }

    @Test
    fun reorderSortPriorityMovesItemToStart() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", priority = AdaptiveSortPriority.Tertiary),
            ),
        )
        val result = state.reorderSortPriority(fromIndex = 2, toIndex = 0)
        assertEquals(listOf("c", "a", "b"), result.sortedColumns.map { it.columnId })
        assertEquals(
            listOf(AdaptiveSortPriority.Primary, AdaptiveSortPriority.Secondary, AdaptiveSortPriority.Tertiary),
            result.sortedColumns.map { it.priority },
        )
    }

    @Test
    fun reorderSortPriorityMovesItemToEnd() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
                AdaptiveColumnSortState("c", priority = AdaptiveSortPriority.Tertiary),
            ),
        )
        val result = state.reorderSortPriority(fromIndex = 0, toIndex = 2)
        assertEquals(listOf("b", "c", "a"), result.sortedColumns.map { it.columnId })
        assertEquals(
            listOf(AdaptiveSortPriority.Primary, AdaptiveSortPriority.Secondary, AdaptiveSortPriority.Tertiary),
            result.sortedColumns.map { it.priority },
        )
    }

    @Test
    fun reorderSortPriorityClampsNegativeTarget() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.reorderSortPriority(fromIndex = 1, toIndex = -10)
        assertEquals(listOf("b", "a"), result.sortedColumns.map { it.columnId })
    }

    @Test
    fun reorderSortPriorityClampsLargeTarget() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val result = state.reorderSortPriority(fromIndex = 0, toIndex = 100)
        assertEquals(listOf("b", "a"), result.sortedColumns.map { it.columnId })
    }

    @Test
    fun reorderSortPriorityInvalidFromIndexIsNoOp() {
        val state = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
            ),
        )
        val result = state.reorderSortPriority(fromIndex = 5, toIndex = 0)
        assertEquals(state.sortedColumns, result.sortedColumns)
    }

    @Test
    fun resolveEffectiveSortStateDropsMissingColumns() {
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("ghost", priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, sortable = true, order = 1),
            ),
        )
        val result = resolveEffectiveSortState(sortState, config)
        assertEquals(listOf("a"), result.sortedColumns.map { it.columnId })
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
    }

    @Test
    fun resolveEffectiveSortStateDropsHiddenColumns() {
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0),
                AdaptiveColumnConfig("b", visible = false, sortable = true, order = 1),
            ),
        )
        val result = resolveEffectiveSortState(sortState, config)
        assertEquals(listOf("a"), result.sortedColumns.map { it.columnId })
    }

    @Test
    fun resolveEffectiveSortStateDropsNonSortableColumns() {
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", priority = AdaptiveSortPriority.Primary),
                AdaptiveColumnSortState("b", priority = AdaptiveSortPriority.Secondary),
            ),
        )
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, sortable = false, order = 1),
            ),
        )
        val result = resolveEffectiveSortState(sortState, config)
        assertEquals(listOf("a"), result.sortedColumns.map { it.columnId })
    }

    @Test
    fun resolveQuerySortFromStateReturnsFallbackWhenEffectiveStateEmpty() {
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("ghost", priority = AdaptiveSortPriority.Primary),
            ),
        )
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val (key, direction) = resolveQuerySortFromState(sortState, config)
        assertNull(key)
        assertEquals(AdaptiveSortDirection.Ascending, direction)
    }

    @Test
    fun resolveQuerySortFromStateReturnsPrimaryEffectiveSort() {
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("a", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Primary),
            ),
        )
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val (key, direction) = resolveQuerySortFromState(sortState, config)
        assertEquals("a", key)
        assertEquals(AdaptiveSortDirection.Descending, direction)
    }

    @Test
    fun resolveSortStateFromQueryReturnsEmptyForNullKey() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val result = resolveSortStateFromQuery(null, AdaptiveSortDirection.Ascending, config)
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun resolveSortStateFromQueryReturnsEmptyForMissingKey() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val result = resolveSortStateFromQuery("ghost", AdaptiveSortDirection.Ascending, config)
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun resolveSortStateFromQueryReturnsEmptyForHiddenKey() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = false, sortable = true, order = 0)),
        )
        val result = resolveSortStateFromQuery("a", AdaptiveSortDirection.Ascending, config)
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun resolveSortStateFromQueryReturnsEmptyForNonSortableKey() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = false, order = 0)),
        )
        val result = resolveSortStateFromQuery("a", AdaptiveSortDirection.Ascending, config)
        assertEquals(0, result.sortedColumns.size)
    }

    @Test
    fun resolveSortStateFromQueryReturnsPrimaryForVisibleSortableKey() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val result = resolveSortStateFromQuery("a", AdaptiveSortDirection.Descending, config)
        assertEquals(1, result.sortedColumns.size)
        assertEquals("a", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortPriority.Primary, result.sortedColumns[0].priority)
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[0].direction)
    }

    @Test
    fun adaptiveQueryStateToSortStateMapsQuerySort() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val query = AdaptiveQueryState(sortKey = "a", sortDirection = AdaptiveSortDirection.Descending)
        val result = query.toSortState(config)
        assertEquals(1, result.sortedColumns.size)
        assertEquals("a", result.sortedColumns[0].columnId)
        assertEquals(AdaptiveSortDirection.Descending, result.sortedColumns[0].direction)
    }

    @Test
    fun adaptiveQueryStateWithSortStateWritesPrimarySortBackToQuery() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, sortable = true, order = 1),
            ),
        )
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("b", AdaptiveSortDirection.Descending, priority = AdaptiveSortPriority.Primary),
            ),
        )
        val query = AdaptiveQueryState()
        val result = query.withSortState(sortState, config)
        assertEquals("b", result.sortKey)
        assertEquals(AdaptiveSortDirection.Descending, result.sortDirection)
    }

    @Test
    fun adaptiveQueryStateWithSortStateClearsQuerySortWhenEffectiveStateEmpty() {
        val config = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, sortable = true, order = 0)),
        )
        val sortState = AdaptiveDataSortState(
            sortedColumns = listOf(
                AdaptiveColumnSortState("ghost", priority = AdaptiveSortPriority.Primary),
            ),
        )
        val query = AdaptiveQueryState(sortKey = "previous", sortDirection = AdaptiveSortDirection.Descending)
        val result = query.withSortState(sortState, config)
        assertNull(result.sortKey)
        assertEquals(AdaptiveSortDirection.Ascending, result.sortDirection)
    }
}
