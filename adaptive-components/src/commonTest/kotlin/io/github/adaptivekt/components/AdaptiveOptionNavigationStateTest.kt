package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

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
        state = state.copy(highlightedKey = "D")
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next, wrap = true)
        assertEquals("A", state.highlightedKey)
    }

    @Test
    fun testNextWithoutWrapAtEnd() {
        var state = AdaptiveOptionNavigationState(highlightedKey = "D")
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next, wrap = false)
        assertEquals("D", state.highlightedKey) // Should remain D
    }

    @Test
    fun testPreviousWithoutWrapAtStart() {
        var state = AdaptiveOptionNavigationState(highlightedKey = "A")
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Previous, wrap = false)
        assertEquals("A", state.highlightedKey) // Should remain A
    }

    @Test
    fun testPreviousWithWrap() {
        var state = AdaptiveOptionNavigationState<String>()
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Previous, wrap = true)
        assertEquals("D", state.highlightedKey)
    }

    @Test
    fun testDisabledKeysSkipped() {
        var state = AdaptiveOptionNavigationState(disabledKeys = setOf("B", "C"))
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("A", state.highlightedKey)
        state = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("D", state.highlightedKey)
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
        assertEquals("B", first.highlightedKey)
        val last = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Last)
        assertEquals("C", last.highlightedKey)
    }

    @Test
    fun testClearHighlightWithExistingHighlight() {
        val state = AdaptiveOptionNavigationState(highlightedKey = "B")
        val cleared = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.ClearHighlight)
        assertNull(cleared.highlightedKey)
    }

    @Test
    fun testClearHighlightWithEmptyState() {
        val state = AdaptiveOptionNavigationState<String>()
        val cleared = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.ClearHighlight)
        assertNull(cleared.highlightedKey)
    }

    @Test
    fun testHighlightInexistentKey() {
        val state = AdaptiveOptionNavigationState(highlightedKey = "B")
        val next = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Highlight("Z"))
        assertEquals("B", next.highlightedKey) // Should remain B
    }

    @Test
    fun testHighlightDisabledKey() {
        val state = AdaptiveOptionNavigationState(highlightedKey = "B", disabledKeys = setOf("C"))
        val next = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Highlight("C"))
        assertEquals("B", next.highlightedKey) // Should remain B
    }

    @Test
    fun testEmptyList() {
        val state = AdaptiveOptionNavigationState(highlightedKey = "A")
        val next = resolveOptionNavigation(state, emptyList<String>(), keySelector, OptionNavigationOperation.Next)
        assertNull(next.highlightedKey)
    }

    @Test
    fun testPreviousHighlightDisappearsFromItems() {
        val state = AdaptiveOptionNavigationState(highlightedKey = "Z")
        // If "Z" is no longer in items, next should treat currentIndex as -1 and start over
        val next = resolveOptionNavigation(state, items, keySelector, OptionNavigationOperation.Next)
        assertEquals("A", next.highlightedKey)
    }

    @Test
    fun testReorderedList() {
        val state = AdaptiveOptionNavigationState(highlightedKey = "B")
        val reorderedItems = listOf("B", "A", "C", "D")
        val next = resolveOptionNavigation(state, reorderedItems, keySelector, OptionNavigationOperation.Next)
        // Next after B should be A in the new list
        assertEquals("A", next.highlightedKey)
    }

    @Test
    fun testDuplicateKeysInvalid() {
        val state = AdaptiveOptionNavigationState<String>()
        val invalidItems = listOf("A", "B", "A")
        assertFailsWith<IllegalArgumentException> {
            resolveOptionNavigation(state, invalidItems, keySelector, OptionNavigationOperation.Next)
        }
    }
}
