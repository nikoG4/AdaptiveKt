package io.github.adaptivekt.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdaptiveDataColumnConfigStateTest {

    @Test
    fun normalizeRemovesDuplicateColumnIds() {
        val input = listOf(
            AdaptiveColumnConfig("a", order = 0),
            AdaptiveColumnConfig("a", order = 1),
            AdaptiveColumnConfig("b", order = 2),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(listOf("a", "b"), result.columns.map { it.columnId })
        assertEquals(0, result.columns[0].order)
        assertEquals(1, result.columns[1].order)
    }

    @Test
    fun normalizeVisibleOrdersAreCompact() {
        val input = listOf(
            AdaptiveColumnConfig("b", visible = true, order = 10),
            AdaptiveColumnConfig("a", visible = true, order = 5),
            AdaptiveColumnConfig("c", visible = true, order = 20),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        val visible = result.columns.filter { it.visible }
        assertEquals(listOf("a", "b", "c"), visible.map { it.columnId })
        assertEquals(listOf(0, 1, 2), visible.map { it.order })
    }

    @Test
    fun normalizeKeepsOnlyOneStartPin() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.Start, order = 0),
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.Start, order = 1),
            AdaptiveColumnConfig("c", visible = true, pinned = AdaptiveColumnPin.None, order = 2),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        val startPins = result.columns.filter { it.pinned == AdaptiveColumnPin.Start }
        assertEquals(1, startPins.size)
        assertEquals("a", startPins[0].columnId)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun normalizeKeepsOnlyOneEndPin() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.End, order = 0),
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.End, order = 1),
            AdaptiveColumnConfig("c", visible = true, pinned = AdaptiveColumnPin.None, order = 2),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        val endPins = result.columns.filter { it.pinned == AdaptiveColumnPin.End }
        assertEquals(1, endPins.size)
        assertEquals("a", endPins[0].columnId)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun setColumnVisibleHidesVisibleColumn() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, order = 1),
            ),
        )
        val result = state.setColumnVisible("b", visible = false)
        assertEquals(false, result.columns.first { it.columnId == "b" }.visible)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun setColumnVisibleCannotHideLastVisibleColumn() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
            ),
        )
        val result = state.setColumnVisible("a", visible = false)
        assertEquals(true, result.columns.first { it.columnId == "a" }.visible)
    }

    @Test
    fun hiddenColumnLosesPin() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.Start, order = 1),
            ),
        )
        val result = state.setColumnVisible("b", visible = false)
        val hidden = result.columns.first { it.columnId == "b" }
        assertEquals(false, hidden.visible)
        assertEquals(AdaptiveColumnPin.None, hidden.pinned)
    }

    @Test
    fun moveColumnMovesVisibleColumn() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, order = 1),
                AdaptiveColumnConfig("c", visible = true, order = 2),
            ),
        )
        val result = state.moveColumn("a", targetIndex = 2)
        val visible = result.columns.filter { it.visible }
        assertEquals(listOf("b", "c", "a"), visible.map { it.columnId })
        assertEquals(listOf(0, 1, 2), visible.map { it.order })
    }

    @Test
    fun moveColumnClampsNegativeIndex() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, order = 1),
            ),
        )
        val result = state.moveColumn("b", targetIndex = -10)
        val visible = result.columns.filter { it.visible }
        assertEquals(listOf("b", "a"), visible.map { it.columnId })
    }

    @Test
    fun moveColumnClampsLargeIndex() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, order = 1),
            ),
        )
        val result = state.moveColumn("a", targetIndex = 100)
        val visible = result.columns.filter { it.visible }
        assertEquals(listOf("b", "a"), visible.map { it.columnId })
    }

    @Test
    fun moveColumnHiddenColumnIsNoOp() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = false, order = 1),
            ),
        )
        val result = state.moveColumn("b", targetIndex = 0)
        assertEquals(state.columns, result.columns)
    }

    @Test
    fun setColumnPinStartClearsPreviousStart() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.Start, order = 0),
                AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.None, order = 1),
            ),
        )
        val result = state.setColumnPin("b", AdaptiveColumnPin.Start)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.Start, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun setColumnPinEndClearsPreviousEnd() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.End, order = 0),
                AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.None, order = 1),
            ),
        )
        val result = state.setColumnPin("b", AdaptiveColumnPin.End)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.End, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun setColumnPinNoneOnlyAffectsTarget() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.Start, order = 0),
                AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.End, order = 1),
            ),
        )
        val result = state.setColumnPin("a", AdaptiveColumnPin.None)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.End, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun setColumnPinHiddenColumnIsNoOp() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = false, order = Int.MAX_VALUE),
            ),
        )
        val result = state.setColumnPin("a", AdaptiveColumnPin.Start)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "a" }.pinned)
    }

    @Test
    fun setColumnVisibleShowsHiddenColumn() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = false, order = Int.MAX_VALUE),
            ),
        )
        val result = state.setColumnVisible("b", visible = true)
        assertEquals(true, result.columns.first { it.columnId == "b" }.visible)
    }

    @Test
    fun normalizeClearsPinFromHiddenColumns() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = false, pinned = AdaptiveColumnPin.Start, order = 0),
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.None, order = 1),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        val hidden = result.columns.first { it.columnId == "a" }
        assertEquals(false, hidden.visible)
        assertEquals(AdaptiveColumnPin.None, hidden.pinned)
    }

    @Test
    fun hiddenPinnedColumnDoesNotConsumeStartSlot() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = false, pinned = AdaptiveColumnPin.Start, order = 0),
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.Start, order = 1),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.Start, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun hiddenPinnedColumnDoesNotConsumeEndSlot() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = false, pinned = AdaptiveColumnPin.End, order = 0),
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.End, order = 1),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.End, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun firstStartByOrderWinsNotInputOrder() {
        val input = listOf(
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.Start, order = 1),
            AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.Start, order = 0),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(AdaptiveColumnPin.Start, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun firstEndByOrderWinsNotInputOrder() {
        val input = listOf(
            AdaptiveColumnConfig("b", visible = true, pinned = AdaptiveColumnPin.End, order = 1),
            AdaptiveColumnConfig("a", visible = true, pinned = AdaptiveColumnPin.End, order = 0),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(AdaptiveColumnPin.End, result.columns.first { it.columnId == "a" }.pinned)
        assertEquals(AdaptiveColumnPin.None, result.columns.first { it.columnId == "b" }.pinned)
    }

    @Test
    fun visibleColumnsAccessorReturnsVisibleSortedByOrder() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("c", visible = true, order = 2),
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = true, order = 1),
                AdaptiveColumnConfig("d", visible = false, order = 3),
            ),
        )
        val visible = state.visibleColumns
        assertEquals(listOf("a", "b", "c"), visible.map { it.columnId })
    }

    @Test
    fun hiddenColumnsAccessorReturnsHiddenColumns() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = false, order = 1),
                AdaptiveColumnConfig("c", visible = false, order = 2),
            ),
        )
        val hidden = state.hiddenColumns
        assertEquals(listOf("b", "c"), hidden.map { it.columnId })
    }

    @Test
    fun visibleColumnIdsAccessorReturnsSortedVisibleIds() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("b", visible = true, order = 1),
                AdaptiveColumnConfig("a", visible = true, order = 0),
            ),
        )
        assertEquals(listOf("a", "b"), state.visibleColumnIds)
    }

    @Test
    fun hiddenColumnIdsAccessorReturnsHiddenIds() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = false, order = 1),
            ),
        )
        assertEquals(listOf("b"), state.hiddenColumnIds)
    }

    @Test
    fun getConfigReturnsMatchingConfig() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0)),
        )
        assertEquals("a", state.getConfig("a")?.columnId)
    }

    @Test
    fun getConfigReturnsNullForMissingColumn() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0)),
        )
        assertNull(state.getConfig("ghost"))
    }

    @Test
    fun setColumnWidthUpdatesWidth() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0)),
        )
        val result = state.setColumnWidth("a", 120)
        assertEquals(120, result.columns.first { it.columnId == "a" }.width)
    }

    @Test
    fun setColumnWidthNullClearsWidth() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0, width = 120)),
        )
        val result = state.setColumnWidth("a", null)
        assertNull(result.columns.first { it.columnId == "a" }.width)
    }

    @Test
    fun setColumnWidthRejectsZero() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0, width = 120)),
        )
        val result = state.setColumnWidth("a", 0)
        assertEquals(120, result.columns.first { it.columnId == "a" }.width)
    }

    @Test
    fun setColumnWidthRejectsNegative() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0, width = 120)),
        )
        val result = state.setColumnWidth("a", -5)
        assertEquals(120, result.columns.first { it.columnId == "a" }.width)
    }

    @Test
    fun setColumnWidthMissingColumnIsNoOp() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(AdaptiveColumnConfig("a", visible = true, order = 0)),
        )
        val result = state.setColumnWidth("ghost", 120)
        assertEquals(state.columns, result.columns)
    }

    @Test
    fun resetAdaptiveDataColumnConfigStateNormalizesDefaults() {
        val defaults = listOf(
            AdaptiveColumnConfig("b", visible = true, order = 10),
            AdaptiveColumnConfig("a", visible = true, order = 5),
        )
        val result = resetAdaptiveDataColumnConfigState(defaults)
        assertEquals(listOf("a", "b"), result.columns.map { it.columnId })
        assertEquals(listOf(0, 1), result.columns.map { it.order })
    }

    @Test
    fun setColumnVisibleShowingHiddenColumnReturnsItVisibleAndUnpinned() {
        val state = AdaptiveDataColumnConfigState(
            columns = listOf(
                AdaptiveColumnConfig("a", visible = true, order = 0),
                AdaptiveColumnConfig("b", visible = false, pinned = AdaptiveColumnPin.Start, order = Int.MAX_VALUE),
            ),
        )
        val result = state.setColumnVisible("b", visible = true)
        val shown = result.columns.first { it.columnId == "b" }
        assertEquals(true, shown.visible)
        assertEquals(AdaptiveColumnPin.None, shown.pinned)
    }

    @Test
    fun normalizeDuplicateHiddenAndVisiblePreservesFirstOccurrence() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = true, order = 0),
            AdaptiveColumnConfig("a", visible = false, order = 1),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(1, result.columns.size)
        assertEquals(true, result.columns.first { it.columnId == "a" }.visible)
    }

    @Test
    fun normalizeDuplicateVisibleAndHiddenPreservesFirstOccurrence() {
        val input = listOf(
            AdaptiveColumnConfig("a", visible = false, order = 0),
            AdaptiveColumnConfig("a", visible = true, order = 1),
        )
        val result = normalizeAdaptiveDataColumnConfigState(input)
        assertEquals(1, result.columns.size)
        assertEquals(false, result.columns.first { it.columnId == "a" }.visible)
    }
}
