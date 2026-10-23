package io.github.adaptivekt.components

import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.Key
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdaptiveSelectTest {
    @Test
    fun keyboardNavigation_ignoredKeyUpsCannotRepeatActions() {
        val keys = listOf(Key.DirectionDown, Key.DirectionUp, Key.Enter, Key.Spacebar, Key.Escape)
        for (key in keys) {
            assertNull(resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyUp, key, isExpanded = false))
            assertNull(resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyUp, key, isExpanded = true))
        }
        assertNull(resolveAdaptiveSelectKeyboardAction(KeyEventType.Unknown, Key.Enter, isExpanded = true))
    }

    @Test
    fun keyboardNavigation_closedAndExpandedKeys() {
        for (key in listOf(Key.DirectionDown, Key.Enter, Key.Spacebar)) {
            assertEquals(AdaptiveSelectKeyboardAction.Expand,
                resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyDown, key, isExpanded = false))
        }
        val cases = listOf(
            Key.DirectionDown to AdaptiveSelectKeyboardAction.Next,
            Key.DirectionUp to AdaptiveSelectKeyboardAction.Previous,
            Key.MoveHome to AdaptiveSelectKeyboardAction.First,
            Key.MoveEnd to AdaptiveSelectKeyboardAction.Last,
            Key.Enter to AdaptiveSelectKeyboardAction.Select,
            Key.Spacebar to AdaptiveSelectKeyboardAction.Select,
            Key.Escape to AdaptiveSelectKeyboardAction.Collapse,
        )
        for ((key, action) in cases) {
            assertEquals(action, resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyDown, key, isExpanded = true))
        }
        assertNull(resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyDown, Key.Tab, isExpanded = true))
        assertNull(resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyDown, Key.Escape, isExpanded = false))
    }

    @Test
    fun keyboardNavigation_searchFocusDoesNotConsumeSpace() {
        assertNull(resolveAdaptiveSelectKeyboardAction(
            KeyEventType.KeyDown, Key.Spacebar, isExpanded = true, isSearchFocused = true))
        assertEquals(AdaptiveSelectKeyboardAction.Select,
            resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyDown, Key.Enter, isExpanded = true, isSearchFocused = true))
        assertEquals(AdaptiveSelectKeyboardAction.Collapse,
            resolveAdaptiveSelectKeyboardAction(KeyEventType.KeyDown, Key.Escape, isExpanded = true, isSearchFocused = true))
    }


    @Test
    fun keyboardNavigation_acceptsOnlyKeyDown() {
        assertTrue(shouldHandleAdaptiveSelectKeyEvent(KeyEventType.KeyDown))
        assertFalse(shouldHandleAdaptiveSelectKeyEvent(KeyEventType.KeyUp))
        assertFalse(shouldHandleAdaptiveSelectKeyEvent(KeyEventType.Unknown))
    }

    @Test
    fun selectMatchesQuery_emptyQuery_alwaysMatches() {
        assertTrue(selectMatchesQuery("Option A", ""))
        assertTrue(selectMatchesQuery("", ""))
        assertTrue(selectMatchesQuery("anything here", ""))
    }

    @Test
    fun selectMatchesQuery_exactMatch_returnsTrue() {
        assertTrue(selectMatchesQuery("Option A", "Option A"))
    }

    @Test
    fun selectMatchesQuery_caseInsensitive_returnsTrue() {
        assertTrue(selectMatchesQuery("New York", "new york"))
        assertTrue(selectMatchesQuery("new york", "New York"))
        assertTrue(selectMatchesQuery("BRAZIL", "brazil"))
    }

    @Test
    fun selectMatchesQuery_partialMatch_returnsTrue() {
        assertTrue(selectMatchesQuery("United States", "United"))
        assertTrue(selectMatchesQuery("United States", "states"))
        assertTrue(selectMatchesQuery("United States", "ed St"))
    }

    @Test
    fun selectMatchesQuery_noMatch_returnsFalse() {
        assertFalse(selectMatchesQuery("Canada", "Brazil"))
        assertFalse(selectMatchesQuery("Option A", "Option B"))
    }

    @Test
    fun selectMatchesQuery_withSpaces_works() {
        assertTrue(selectMatchesQuery("New York City", "york city"))
        assertFalse(selectMatchesQuery("New York", "New  York"))
    }

    @Test
    fun selectMatchesQuery_emptyLabel_emptyQuery_matches() {
        assertTrue(selectMatchesQuery("", ""))
    }

    @Test
    fun selectMatchesQuery_emptyLabel_nonEmptyQuery_doesNotMatch() {
        assertFalse(selectMatchesQuery("", "abc"))
    }
}
