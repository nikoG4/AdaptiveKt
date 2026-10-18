package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdaptiveTabsKeyboardTest {
    @Test
    fun rightArrowWrapsAtTheEnd() {
        assertEquals(0, resolveAdaptiveTabIndex(4, 3, AdaptiveTabNavigation.Next))
    }

    @Test
    fun leftArrowWrapsAtTheStart() {
        assertEquals(3, resolveAdaptiveTabIndex(4, 0, AdaptiveTabNavigation.Previous))
    }

    @Test
    fun homeAndEndChooseBoundaries() {
        assertEquals(0, resolveAdaptiveTabIndex(4, 2, AdaptiveTabNavigation.First))
        assertEquals(3, resolveAdaptiveTabIndex(4, 2, AdaptiveTabNavigation.Last))
    }

    @Test
    fun noTabsDoesNotCauseAnIndexError() {
        assertNull(resolveAdaptiveTabIndex(0, -1, AdaptiveTabNavigation.Next))
        assertNull(resolveAdaptiveTabIndex(0, -1, AdaptiveTabNavigation.Last))
    }

    @Test
    fun staleSelectionFallsBackToFirstIndexBeforeNavigation() {
        assertEquals(1, resolveAdaptiveTabIndex(3, -1, AdaptiveTabNavigation.Next))
        assertEquals(2, resolveAdaptiveTabIndex(3, -1, AdaptiveTabNavigation.Previous))
    }

    @Test
    fun singleTabAlwaysKeepsTheOnlyOption() {
        assertEquals(0, resolveAdaptiveTabIndex(1, 0, AdaptiveTabNavigation.Next))
        assertEquals(0, resolveAdaptiveTabIndex(1, 0, AdaptiveTabNavigation.Previous))
    }
}
