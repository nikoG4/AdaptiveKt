package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveThumbnailTest {

    @Test
    fun thumbnailLabelFor_twoNameParts_returnsTwoLetters() {
        assertEquals("AP", thumbnailLabelFor("AirPods Pro"))
        assertEquals("RG", thumbnailLabelFor("Router Gigabit"))
        assertEquals("JK", thumbnailLabelFor("John Kim"))
    }

    @Test
    fun thumbnailLabelFor_singleName_returnsFirstLetter() {
        assertEquals("R", thumbnailLabelFor("Router"))
        assertEquals("D", thumbnailLabelFor("D"))
    }

    @Test
    fun thumbnailLabelFor_emptyName_returnsQuestionMark() {
        assertEquals("?", thumbnailLabelFor(""))
        assertEquals("?", thumbnailLabelFor("   "))
    }

    @Test
    fun thumbnailLabelFor_nameWithMultipleSpaces_returnsCorrectInitials() {
        assertEquals("AP", thumbnailLabelFor("  AirPods   Pro  "))
        assertEquals("JK", thumbnailLabelFor("John    Kim"))
    }

    @Test
    fun thumbnailLabelFor_namesWithHyphensOrSymbolsDoNotCrash() {
        assertEquals("AM", thumbnailLabelFor("Anne-Marie Smith"))
        assertEquals("?", thumbnailLabelFor("--- !!!"))
        assertEquals("V", thumbnailLabelFor("v2"))
    }

    @Test
    fun thumbnailLabelFor_neverReturnsMoreThanTwoCharacters() {
        assertEquals(2, thumbnailLabelFor("Responsive Grid Kit").length)
        assertEquals(1, thumbnailLabelFor("Router").length)
    }
}
