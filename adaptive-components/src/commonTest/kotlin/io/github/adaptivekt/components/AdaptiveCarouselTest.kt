package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveCarouselTest {

    @Test
    fun normalizeCarouselIndex_emptyList_returnsZero() {
        assertEquals(0, normalizeCarouselIndex(index = 3, itemCount = 0))
    }

    @Test
    fun normalizeCarouselIndex_clampsOutOfRangeValues() {
        assertEquals(0, normalizeCarouselIndex(index = -2, itemCount = 4))
        assertEquals(3, normalizeCarouselIndex(index = 7, itemCount = 4))
    }

    @Test
    fun nextCarouselIndex_advancesAndLoops() {
        assertEquals(2, nextCarouselIndex(index = 1, itemCount = 4, loop = true))
        assertEquals(0, nextCarouselIndex(index = 3, itemCount = 4, loop = true))
    }

    @Test
    fun nextCarouselIndex_staysAtEndWhenLoopDisabled() {
        assertEquals(3, nextCarouselIndex(index = 3, itemCount = 4, loop = false))
    }

    @Test
    fun previousCarouselIndex_movesBackAndLoops() {
        assertEquals(1, previousCarouselIndex(index = 2, itemCount = 4, loop = true))
        assertEquals(3, previousCarouselIndex(index = 0, itemCount = 4, loop = true))
    }

    @Test
    fun previousCarouselIndex_staysAtStartWhenLoopDisabled() {
        assertEquals(0, previousCarouselIndex(index = 0, itemCount = 4, loop = false))
    }
}
