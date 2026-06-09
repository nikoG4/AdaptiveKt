package io.github.adaptivekt.layout

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveTwoPaneTest {

    @Test
    fun testTwoPaneOrientation() {
        // Stack on compact when collapse = true
        assertEquals(
            AdaptiveTwoPaneOrientation.Stacked,
            adaptiveTwoPaneOrientation(isCompact = true, collapseOnCompact = true)
        )

        // Side-by-side on compact when collapse = false
        assertEquals(
            AdaptiveTwoPaneOrientation.SideBySide,
            adaptiveTwoPaneOrientation(isCompact = true, collapseOnCompact = false)
        )

        // Side-by-side on non-compact regardless of collapse flag
        assertEquals(
            AdaptiveTwoPaneOrientation.SideBySide,
            adaptiveTwoPaneOrientation(isCompact = false, collapseOnCompact = true)
        )
        assertEquals(
            AdaptiveTwoPaneOrientation.SideBySide,
            adaptiveTwoPaneOrientation(isCompact = false, collapseOnCompact = false)
        )
    }
}
