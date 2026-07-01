package io.github.adaptivekt.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdaptiveDataBulkActionBarLogicTest {

    @Test
    fun testBulkActionExecution() {
        val selectedKeys = setOf("1", "2", "off_page_key")
        var executedKeys: Set<String>? = null
        
        val action = AdaptiveDataBulkAction<String>(
            id = "delete",
            label = "Delete",
            onClick = { keys -> executedKeys = keys }
        )
        
        action.onClick(selectedKeys)
        
        assertEquals(3, executedKeys?.size)
        assertTrue(executedKeys!!.contains("off_page_key"))
    }
    
    @Test
    fun testBulkActionDisabled() {
        var executed = false
        val action = AdaptiveDataBulkAction<String>(
            id = "disabled_action",
            label = "Disabled",
            enabled = false,
            onClick = { executed = true }
        )
        assertFalse(action.enabled)
    }

    @Test
    fun testBulkActionScopeClearUsesClearAll() {
        val state = AdaptiveDataSelectionState(setOf("1", "2"), null)
        var resultingState = state
        
        val scope = object : AdaptiveDataBulkActionScope<String> {
            override val selectedKeys = state.selectedKeys
            override val selectedCount = state.selectedKeys.size
            override fun clearSelection() {
                resultingState = resolveAdaptiveDataSelection(
                    state = state,
                    operation = AdaptiveDataSelectionOperation.ClearAll,
                    mode = AdaptiveDataSelectionMode.Multiple,
                    visibleKeys = emptyList(),
                    disabledKeys = emptySet()
                )
            }
        }
        
        scope.clearSelection()
        assertTrue(resultingState.selectedKeys.isEmpty())
    }
    
    @Test
    fun testBulkActionLayoutCompact() {
        val actions = listOf(
            AdaptiveDataBulkAction<String>(id = "p1", label = "P1", priority = AdaptiveActionPriority.Primary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "p2", label = "P2", priority = AdaptiveActionPriority.Primary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "s1", label = "S1", priority = AdaptiveActionPriority.Secondary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "o1", label = "O1", priority = AdaptiveActionPriority.Overflow, onClick = {})
        )
        
        val layout = resolveAdaptiveBulkActionLayout(actions, compact = true)
        
        assertEquals(1, layout.visiblePrimary.size)
        assertEquals("P1", layout.visiblePrimary[0].label)
        
        assertEquals(0, layout.visibleSecondary.size)
        
        assertEquals(3, layout.overflow.size)
        assertEquals("P2", layout.overflow[0].label)
        assertEquals("S1", layout.overflow[1].label)
        assertEquals("O1", layout.overflow[2].label)
    }

    @Test
    fun testBulkActionLayoutExpanded() {
        val actions = listOf(
            AdaptiveDataBulkAction<String>(id = "p1", label = "P1", priority = AdaptiveActionPriority.Primary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "p2", label = "P2", priority = AdaptiveActionPriority.Primary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "s1", label = "S1", priority = AdaptiveActionPriority.Secondary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "o1", label = "O1", priority = AdaptiveActionPriority.Overflow, onClick = {})
        )
        
        val layout = resolveAdaptiveBulkActionLayout(actions, compact = false)
        
        assertEquals(2, layout.visiblePrimary.size)
        assertEquals("P1", layout.visiblePrimary[0].label)
        assertEquals("P2", layout.visiblePrimary[1].label)
        
        assertEquals(1, layout.visibleSecondary.size)
        assertEquals("S1", layout.visibleSecondary[0].label)
        
        assertEquals(1, layout.overflow.size)
        assertEquals("O1", layout.overflow[0].label)
    }

    @Test
    fun testSelectAllIntentResolution() {
        assertEquals(
            AdaptiveDataSelectionOperation.ClearVisible,
            resolveAdaptiveSelectAllIntent(AdaptiveSelectAllState.Checked)
        )
        assertEquals(
            AdaptiveDataSelectionOperation.SelectAllVisible,
            resolveAdaptiveSelectAllIntent(AdaptiveSelectAllState.Unchecked)
        )
        assertEquals(
            AdaptiveDataSelectionOperation.SelectAllVisible,
            resolveAdaptiveSelectAllIntent(AdaptiveSelectAllState.Indeterminate)
        )
        assertNull(resolveAdaptiveSelectAllIntent(AdaptiveSelectAllState.Disabled))
    }
}
