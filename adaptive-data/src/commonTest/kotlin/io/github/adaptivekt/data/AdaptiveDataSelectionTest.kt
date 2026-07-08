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
            AdaptiveDataSelectionOperation.Deselect("b"),
            AdaptiveDataSelectionOperation.Toggle("a"),
            AdaptiveDataSelectionOperation.Replace("d"),
            AdaptiveDataSelectionOperation.SelectRange("e"),
            AdaptiveDataSelectionOperation.SelectAllVisible,
            AdaptiveDataSelectionOperation.ClearAll,
            AdaptiveDataSelectionOperation.ClearVisible,
            AdaptiveDataSelectionOperation.RetainAvailableKeys(setOf("a"))
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
    fun testModeSingle_InitialStateWithMultipleKeys_Normalizes() {
        val state = AdaptiveDataSelectionState(setOf("a", "b", "c"), "b")
        val result = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.Select("b"), AdaptiveDataSelectionMode.Single, listOf("a", "b", "c")
        )
        // Anchor is 'b', so 'b' should be kept
        assertEquals(setOf("b"), result.selectedKeys)
        assertEquals("b", result.anchorKey)

        val stateNoAnchor = AdaptiveDataSelectionState(setOf("a", "b", "c"), null)
        val resultNoAnchor = resolveAdaptiveDataSelection(
            stateNoAnchor, AdaptiveDataSelectionOperation.Select("a"), AdaptiveDataSelectionMode.Single, listOf("a", "b", "c")
        )
        // Falls back to first if no valid anchor, then operation Select("a") runs
        assertEquals(setOf("a"), resultNoAnchor.selectedKeys)
    }

    @Test
    fun testAnchorNormalization() {
        // Anchor invalid (not in selectedKeys)
        val state1 = AdaptiveDataSelectionState(setOf("a", "b"), "c")
        val res1 = resolveAdaptiveDataSelection(state1, AdaptiveDataSelectionOperation.Toggle("a"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "c"))
        assertNull(res1.anchorKey, "Anchor should be normalized to null if it wasn't selected")

        // Anchor disabled
        val state2 = AdaptiveDataSelectionState(setOf("a", "b"), "b")
        val res2 = resolveAdaptiveDataSelection(state2, AdaptiveDataSelectionOperation.SelectAllVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b"), disabledKeys = setOf("b"))
        assertNull(res2.anchorKey, "Anchor should be null if disabled")
        assertEquals(setOf("a"), res2.selectedKeys, "Disabled selected key should be stripped")

        // Anchor removed by ClearVisible
        val state3 = AdaptiveDataSelectionState(setOf("a", "b"), "b")
        val res3 = resolveAdaptiveDataSelection(state3, AdaptiveDataSelectionOperation.ClearVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b"))
        assertNull(res3.anchorKey)
        
        // State empty with non-null anchor
        val state4 = AdaptiveDataSelectionState(emptySet(), "a")
        val res4 = resolveAdaptiveDataSelection(state4, AdaptiveDataSelectionOperation.Select("b"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b"))
        assertEquals("b", res4.anchorKey) // Overwritten by new select
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
    fun testRangeSelection_Scenarios() {
        val visible = listOf("a", "b", "c", "d", "e")
        
        // Forward range, additive = false
        var state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("b"), "b"),
            AdaptiveDataSelectionOperation.SelectRange("d", additive = false),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("b", "c", "d"), state.selectedKeys)
        assertEquals("b", state.anchorKey)

        // Backward range, additive = true
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("d"), "d"),
            AdaptiveDataSelectionOperation.SelectRange("b", additive = true),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("b", "c", "d"), state.selectedKeys)
        assertEquals("d", state.anchorKey)

        // Anchor null, target visible -> falls back to Replace/Select
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("a"), null),
            AdaptiveDataSelectionOperation.SelectRange("c", additive = false),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("c"), state.selectedKeys)
        assertEquals("c", state.anchorKey)

        // Anchor outside visible, target visible -> falls back to Select (additive=true)
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("off_page"), "off_page"),
            AdaptiveDataSelectionOperation.SelectRange("b", additive = true),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("off_page", "b"), state.selectedKeys)
        assertEquals("b", state.anchorKey)

        // Target not visible in Multiple -> no-op
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("b"), "b"),
            AdaptiveDataSelectionOperation.SelectRange("off_page", additive = true),
            AdaptiveDataSelectionMode.Multiple,
            visible
        )
        assertEquals(setOf("b"), state.selectedKeys)

        // Target not visible in Single -> no-op
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("b"), "b"),
            AdaptiveDataSelectionOperation.SelectRange("off_page", additive = true),
            AdaptiveDataSelectionMode.Single,
            visible
        )
        assertEquals(setOf("b"), state.selectedKeys)

        // Target disabled
        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("a"), "a"),
            AdaptiveDataSelectionOperation.SelectRange("c", additive = true),
            AdaptiveDataSelectionMode.Multiple,
            visible,
            disabledKeys = setOf("c")
        )
        assertEquals(setOf("a"), state.selectedKeys) // no-op
    }

    @Test
    fun testClearVisible_Vs_ClearAll() {
        val state = AdaptiveDataSelectionState(setOf("a", "b", "c", "off_page"), "c")
        
        val cvState = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.ClearVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "c")
        )
        assertEquals(setOf("off_page"), cvState.selectedKeys)
        assertNull(cvState.anchorKey)

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
        assertNull(newState.anchorKey) // anchor was deleted
    }

    @Test
    fun testSelectAllState() {
        // Mode Single
        assertEquals(
            AdaptiveSelectAllState.Disabled,
            resolveAdaptiveSelectAllState(setOf("a"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Single)
        )
        // All disabled
        assertEquals(
            AdaptiveSelectAllState.Disabled,
            resolveAdaptiveSelectAllState(setOf(), listOf("a"), disabledKeys = setOf("a"), mode = AdaptiveDataSelectionMode.Multiple)
        )
        // None selected
        assertEquals(
            AdaptiveSelectAllState.Unchecked,
            resolveAdaptiveSelectAllState(setOf(), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )
        // Indeterminate
        assertEquals(
            AdaptiveSelectAllState.Indeterminate,
            resolveAdaptiveSelectAllState(setOf("a"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )
        // Checked (off_page doesn't affect visible checked state)
        assertEquals(
            AdaptiveSelectAllState.Checked,
            resolveAdaptiveSelectAllState(setOf("a", "b", "off_page"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )
        // Selected keys outside visible don't affect unchecked
        assertEquals(
            AdaptiveSelectAllState.Unchecked,
            resolveAdaptiveSelectAllState(setOf("off_page"), listOf("a", "b"), mode = AdaptiveDataSelectionMode.Multiple)
        )
    }

    @Test
    fun testValidateRowKeysAndReducersDuplicateRejection() {
        // validateAdaptiveRowKeys directly
        val ex = assertFailsWith<IllegalArgumentException> {
            validateAdaptiveRowKeys(listOf("a", "b", "a"))
        }
        assertTrue(ex.message!!.contains("Duplicate row key detected"))

        // Duplicate in SelectRange
        assertFailsWith<IllegalArgumentException> {
            resolveAdaptiveDataSelection(
                AdaptiveDataSelectionState(), AdaptiveDataSelectionOperation.SelectRange("a"), AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "a")
            )
        }

        // Duplicate in SelectAllVisible
        assertFailsWith<IllegalArgumentException> {
            resolveAdaptiveDataSelection(
                AdaptiveDataSelectionState(), AdaptiveDataSelectionOperation.SelectAllVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "a")
            )
        }

        // Duplicate in ClearVisible
        assertFailsWith<IllegalArgumentException> {
            resolveAdaptiveDataSelection(
                AdaptiveDataSelectionState(), AdaptiveDataSelectionOperation.ClearVisible, AdaptiveDataSelectionMode.Multiple, listOf("a", "b", "a")
            )
        }

        // Duplicate in SelectAllState
        assertFailsWith<IllegalArgumentException> {
            resolveAdaptiveSelectAllState(
                setOf("a"), listOf("a", "b", "a"), mode = AdaptiveDataSelectionMode.Multiple
            )
        }
    }

    @Test
    fun testScale10000Keys() {
        val keys = (1..10000).map { it.toString() }
        var state = AdaptiveDataSelectionState<String>(emptySet(), null)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.SelectAllVisible, AdaptiveDataSelectionMode.Multiple, keys
        )
        assertEquals(10000, state.selectedKeys.size)

        state = resolveAdaptiveDataSelection(
            state, AdaptiveDataSelectionOperation.ClearAll, AdaptiveDataSelectionMode.Multiple, keys
        )
        assertEquals(0, state.selectedKeys.size)

        state = resolveAdaptiveDataSelection(
            AdaptiveDataSelectionState(setOf("100"), "100"),
            AdaptiveDataSelectionOperation.SelectRange("5100"),
            AdaptiveDataSelectionMode.Multiple,
            keys
        )
        assertEquals(5001, state.selectedKeys.size)
    }

    @Test
    fun testResolveAdaptiveRowSelectionOperation_CheckboxAlwaysToggle() {
        // Shift should not matter
        var op = resolveAdaptiveRowSelectionOperation("key", true, false, AdaptiveRowSelectionSource.Checkbox)
        assertTrue(op is AdaptiveDataSelectionOperation.Toggle)
        assertEquals("key", (op as AdaptiveDataSelectionOperation.Toggle).key)
        
        op = resolveAdaptiveRowSelectionOperation("key", false, false, AdaptiveRowSelectionSource.Checkbox)
        assertTrue(op is AdaptiveDataSelectionOperation.Toggle)
    }

    @Test
    fun testResolveAdaptiveRowSelectionOperation_RowNormalReplace() {
        val op = resolveAdaptiveRowSelectionOperation("key", false, false, AdaptiveRowSelectionSource.Row)
        assertTrue(op is AdaptiveDataSelectionOperation.Replace)
        assertEquals("key", (op as AdaptiveDataSelectionOperation.Replace).key)
    }

    @Test
    fun testResolveAdaptiveRowSelectionOperation_RowShiftSelectRange() {
        val op = resolveAdaptiveRowSelectionOperation("key", true, false, AdaptiveRowSelectionSource.Row)
        assertTrue(op is AdaptiveDataSelectionOperation.SelectRange)
        assertEquals("key", (op as AdaptiveDataSelectionOperation.SelectRange).targetKey)
        assertEquals(false, (op as AdaptiveDataSelectionOperation.SelectRange).additive)
        
        val opAdd = resolveAdaptiveRowSelectionOperation("key", true, true, AdaptiveRowSelectionSource.Row)
        assertTrue(opAdd is AdaptiveDataSelectionOperation.SelectRange)
        assertEquals(true, (opAdd as AdaptiveDataSelectionOperation.SelectRange).additive)
    }

    @Test
    fun testResolveAdaptiveRowSelectionOperation_RowCtrlMetaToggle() {
        val op = resolveAdaptiveRowSelectionOperation("key", false, true, AdaptiveRowSelectionSource.Row)
        assertTrue(op is AdaptiveDataSelectionOperation.Toggle)
        assertEquals("key", (op as AdaptiveDataSelectionOperation.Toggle).key)
    }

    @Test
    fun testResolveAdaptiveSelectAllState_HeaderChecked() {
        val state = resolveAdaptiveSelectAllState(
            selectedKeys = setOf("a", "b"),
            visibleKeys = listOf("a", "b"),
            disabledKeys = emptySet(),
            mode = AdaptiveDataSelectionMode.Multiple
        )
        assertEquals(AdaptiveSelectAllState.Checked, state)
    }

    @Test
    fun testResolveAdaptiveSelectAllState_HeaderPartial() {
        val state = resolveAdaptiveSelectAllState(
            selectedKeys = setOf("a"),
            visibleKeys = listOf("a", "b"),
            disabledKeys = emptySet(),
            mode = AdaptiveDataSelectionMode.Multiple
        )
        assertEquals(AdaptiveSelectAllState.Indeterminate, state)
    }

    @Test
    fun testResolveAdaptiveSelectAllState_DisabledNoOp() {
        val state = resolveAdaptiveSelectAllState(
            selectedKeys = setOf("a"),
            visibleKeys = listOf("a", "b"),
            disabledKeys = setOf("a", "b"),
            mode = AdaptiveDataSelectionMode.Multiple
        )
        assertEquals(AdaptiveSelectAllState.Disabled, state)
    }
}
