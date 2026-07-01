package io.github.adaptivekt.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        
        // acción recibe Set<K> completo, selected count incluye keys off-page
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
        // Testing that the composable button would be disabled, but here we just check property.
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
    fun testPrimarySecondaryOverflowCategorization() {
        val actions = listOf(
            AdaptiveDataBulkAction<String>(id = "1", label = "P", priority = AdaptiveActionPriority.Primary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "2", label = "S", priority = AdaptiveActionPriority.Secondary, onClick = {}),
            AdaptiveDataBulkAction<String>(id = "3", label = "O", priority = AdaptiveActionPriority.Overflow, onClick = {})
        )
        
        val primary = actions.filter { it.priority == AdaptiveActionPriority.Primary }
        val secondary = actions.filter { it.priority == AdaptiveActionPriority.Secondary }
        val overflow = actions.filter { it.priority == AdaptiveActionPriority.Overflow }
        
        assertEquals(1, primary.size)
        assertEquals("P", primary[0].label)
        assertEquals(1, secondary.size)
        assertEquals(1, overflow.size)
    }
}
