package io.github.adaptivekt.components

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdaptiveSelectKeyboardActionsTest {
    @Test
    fun onlyKeyDownActivatesSelectionAndNavigation() {
        assertTrue(isSelectCommandPress(KeyEventType.KeyDown))
        assertFalse(isSelectCommandPress(KeyEventType.KeyUp))
    }

    @Test
    fun collapsedInputOpensOnlyForExpectedKeys() {
        for (key in listOf(Key.Enter, Key.Spacebar, Key.DirectionDown)) {
            assertEquals(AdaptiveSelectKeyAction.Expand, resolveAdaptiveSelectKeyAction(key, false, false))
        }
        assertNull(resolveAdaptiveSelectKeyAction(Key.Escape, false, false))
    }

    @Test
    fun expandedMenuHandlesNavigationAndSelection() {
        assertEquals(AdaptiveSelectKeyAction.Next, resolveAdaptiveSelectKeyAction(Key.DirectionDown, true, false))
        assertEquals(AdaptiveSelectKeyAction.Previous, resolveAdaptiveSelectKeyAction(Key.DirectionUp, true, false))
        assertEquals(AdaptiveSelectKeyAction.First, resolveAdaptiveSelectKeyAction(Key.MoveHome, true, false))
        assertEquals(AdaptiveSelectKeyAction.Last, resolveAdaptiveSelectKeyAction(Key.MoveEnd, true, false))
        assertEquals(AdaptiveSelectKeyAction.Select, resolveAdaptiveSelectKeyAction(Key.Enter, true, false))
        assertEquals(AdaptiveSelectKeyAction.Collapse, resolveAdaptiveSelectKeyAction(Key.Escape, true, false))
    }

    @Test
    fun searchFocusDoesNotStealTypingOrCursorKeys() {
        assertNull(resolveAdaptiveSelectKeyAction(Key.Spacebar, true, true))
        assertNull(resolveAdaptiveSelectKeyAction(Key.MoveHome, true, true))
        assertNull(resolveAdaptiveSelectKeyAction(Key.MoveEnd, true, true))
        assertEquals(AdaptiveSelectKeyAction.Collapse, resolveAdaptiveSelectKeyAction(Key.Escape, true, true))
    }

    @Test
    fun unrelatedKeysBubbleToParent() {
        assertNull(resolveAdaptiveSelectKeyAction(Key.A, true, false))
        assertNull(resolveAdaptiveSelectKeyAction(Key.Tab, true, false))
    }
}
