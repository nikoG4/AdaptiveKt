package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdaptiveOptionNavigationStateTest {

    private val items = listOf("A", "B", "C", "D")
    private val keySelector: (String) -> String = { it }

    @Test
    fun testNextWithWrap() {
        var state = AdaptiveOptionNavigationState<String>()
        
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("A", state.highlightedKey)
        
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("B", state.highlightedKey)

        // Skip to end
        state = state.copy(highlightedKey = "D")
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next, wrap = true)
        assertEquals("A", state.highlightedKey)
    }

    @Test
    fun testNextWithoutWrap() {
        var state = AdaptiveOptionNavigationState(highlightedKey = "D")
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next, wrap = false)
        // Remains D because it didn't wrap and there is no next
        assertEquals("D", state.highlightedKey)
    }

    @Test
    fun testPreviousWithWrap() {
        var state = AdaptiveOptionNavigationState<String>()
        // No current selection, wrap around to last
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Previous, wrap = true)
        assertEquals("D", state.highlightedKey)
    }

    @Test
    fun testDisabledKeysSkipped() {
        var state = AdaptiveOptionNavigationState(disabledKeys = setOf("B", "C"))
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("A", state.highlightedKey)
        
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("D", state.highlightedKey) // Skipped B and C
    }

    @Test
    fun testAllDisabled() {
        var state = AdaptiveOptionNavigationState(disabledKeys = setOf("A", "B", "C", "D"))
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertNull(state.highlightedKey)
    }

    @Test
    fun testFirstAndLast() {
        var state = AdaptiveOptionNavigationState(disabledKeys = setOf("A", "D"))
        
        val first = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.First)
        assertEquals("B", first.highlightedKey) // A is disabled
        
        val last = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Last)
        assertEquals("C", last.highlightedKey) // D is disabled
    }
}
