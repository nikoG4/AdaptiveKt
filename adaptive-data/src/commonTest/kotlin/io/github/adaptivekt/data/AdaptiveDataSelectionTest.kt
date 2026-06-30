package io.github.adaptivekt.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdaptiveDataSelectionTest {

    @Test
    fun testModeNone_AlwaysNormalizesToEmpty() {
        val initialState = AdaptiveDataSelectionState(setOf("a", "b"), "b")
        val ops = listOf(
            AdaptiveDataSelectionOperation.Select("c"),
            AdaptiveDataSelectionOperation.Toggle("a"),
            AdaptiveDataSelectionOperation.Replace("d"),
            AdaptiveDataSelectionOperation.SelectRange("e"),
            AdaptiveDataSelectionOperation.SelectAllVisible,
            AdaptiveDataSelectionOperation.ClearAll,
            AdaptiveDataSelectionOperation.ClearVisible
        )

        for (op in ops) {
            val result = resolveAdaptiveDataSelection(
                state = initialState,
                operation = op,
                mode = AdaptiveDataSelectionMode.None,
                visibleKeys = listOf("a", "b", "c", "d", "e")
            )
            assertTrue(result.selectedKeys.isEmpty(), "Mode None should empty selection on $op")
            assertNull(result.anchorKey, "Mode None should empty anchor on $op")
        }
    }

    @Test
    fun testModeSingle_SelectAndReplace() {
        var state = AdaptiveDataSelectionState<String>()

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Select("a"), AdaptiveDataSelectionMode.Single, listOf("a")
        )
        assertEquals(setOf("a"), state.selectedKeys)
        assertEquals("a", state.anchorKey)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Select("b"), AdaptiveDataSelectionMode.Single, listOf("a", "b")
        )
        assertEquals(setOf("b"), state.selectedKeys)
        assertEquals("b", state.anchorKey)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Replace("c"), AdaptiveDataSelectionMode.Single, listOf("a", "b", "c")
        )
        assertEquals(setOf("c"), state.selectedKeys)
        assertEquals("c", state.anchorKey)
    }

    @Test
    fun testModeSingle_Toggle() {
        var state = AdaptiveDataSelectionState(setOf("a"), "a")

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Toggle("a"), AdaptiveDataSelectionMode.Single, listOf("a", "b")
        )
        assertTrue(state.selectedKeys.isEmpty())
        assertNull(state.anchorKey)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Toggle("b"), AdaptiveDataSelectionMode.Single, listOf("a", "b")
        )
        assertEquals(setOf("b"), state.selectedKeys)
        assertEquals("b", state.anchorKey)
    }

    @Test
    fun testModeMultiple_SelectDeselectToggle() {
        var state = AdaptiveDataSelectionState<String>()

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Select("a"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b")
        )
        assertEquals(setOf("a"), state.selectedKeys)
        assertEquals("a", state.anchorKey)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Select("b"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b")
        )
        assertEquals(setOf("a", "b"), state.selectedKeys)
        assertEquals("b", state.anchorKey)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Deselect("b"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b")
        )
        assertEquals(setOf("a"), state.selectedKeys)
        assertNull(state.anchorKey) // anchor was 'b', it got deselected

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Toggle("a"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b")
        )
        assertTrue(state.selectedKeys.isEmpty())
        assertNull(state.anchorKey)
    }

    @Test
    fun testDisabledKeys_CannotBeSelected() {
        val disabled = setOf("b")
        var state = AdaptiveDataSelectionState<String>()

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Select("b"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b"), disabledKeys = disabled
        )
        assertTrue(state.selectedKeys.isEmpty())

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Toggle("b"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b"), disabledKeys = disabled
        )
        assertTrue(state.selectedKeys.isEmpty())

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.SelectAllVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b"), disabledKeys = disabled
        )
        assertEquals(setOf("a"), state.selectedKeys)
    }

    @Test
    fun testRangeSelection_ForwardAndBackward() {
        val visible = listOf("a", "b", "c", "d", "e")
        
        // Forward range
        var state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("b"), "b"),
            AdaptiveDataSelectionOperation.SelectRange("d", additive = false),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("b", "c", "d"), state.selectedKeys)
        assertEquals("b", state.anchorKey) // anchor remains

        // Backward range
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("d"), "d"),
            AdaptiveDataSelectionOperation.SelectRange("a", additive = true),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("d", "a", "b", "c"), state.selectedKeys)
        assertEquals("d", state.anchorKey)
    }

    @Test
    fun testRangeSelection_WithDisabledInside() {
        val visible = listOf("a", "b", "c", "d", "e")
        val disabled = setOf("c")

        val state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("b"), "b"),
            AdaptiveDataSelectionOperation.SelectRange("e", additive = false),
            AdaptiveDataSelectionMode.Multiple,
            visible,
            disabledKeys = disabled
        )
        assertEquals(setOf("b", "d", "e"), state.selectedKeys) // 'c' is skipped
    }

    @Test
    fun testClearVisible_Vs_ClearAll() {
        val state = AdaptiveDataSelectionState(setOf("a", "b", "c", "off_page"), "c")
        
        // ClearVisible
        val cvState = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.ClearVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "c")
        )
        assertEquals(setOf("off_page"), cvState.selectedKeys)
        assertNull(cvState.anchorKey)

        // ClearAll
        val caState = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.ClearAll, AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "c")
        )
        assertTrue(caState.selectedKeys.isEmpty())
        assertNull(caState.anchorKey)
    }

    @Test
    fun testRetainAvailableKeys() {
        val state = AdaptiveDataSelectionState(setOf("a", "b", "deleted_row"), "deleted_row")
        val newState = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.RetainAvailableKeys(setOf("a", "b", "c")), AdaptiveDataSelectionMode.Multiple, emptyList()
        )
        assertEquals(setOf("a", "b"), newState.selectedKeys)
        assertNull(newState.anchorKey)
    }

    @Test
    fun testSelectAllState() {
        assertEquals(
            AdaptiveSelectAllState.Disabled,
            resolveAdaptiveSelectAllState(setOf("a"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Single)
        )

        assertEquals(
            AdaptiveSelectAllState.Disabled,
            resolveAdaptiveSelectAllState(setOf(), listOf("a"), disabledKeys = setOf("a"), mode = AdaptiveDataSelectionMode.Multiple)
        )

        assertEquals(
            AdaptiveSelectAllState.Unchecked,
            resolveAdaptiveSelectAllState(setOf(), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )

        assertEquals(
            AdaptiveSelectAllState.Indeterminate,
            resolveAdaptiveSelectAllState(setOf("a"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )

        assertEquals(
            AdaptiveSelectAllState.Checked,
            resolveAdaptiveSelectAllState(setOf("a", "b", "off_page"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )
    }

    @Test
    fun testValidateRowKeys() {
        validateAdaptiveRowKeys(listOf("a", "b", "c"))
        
        val ex = assertFailsWith<IllegalArgumentException> {
            validateAdaptiveRowKeys(listOf("a", "b", "a"))
        }
        assertTrue(ex.message!!.contains("Duplicate row key detected: a"))
    }

    @Test
    fun testScale10000Keys() {
        val keys = (1..10000).map { it.toString() }
        var state = AdaptiveDataSelectionState<String>(emptySet(), null)

        // Select all
        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.SelectAllVisible, AdaptiveDataSelectionMode.Multiple, keys
        )
        assertEquals(10000, state.selectedKeys.size)

        // Clear all
        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.ClearAll, AdaptiveDataSelectionMode.Multiple, keys
        )
        assertEquals(0, state.selectedKeys.size)

        // Range select 5000 keys
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("100"), "100"),
            AdaptiveDataSelectionOperation.SelectRange("5100"),
            AdaptiveDataSelectionMode.Multiple,
            keys
        )
        assertEquals(5001, state.selectedKeys.size)
    }
}
