package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveSelectTest {

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
