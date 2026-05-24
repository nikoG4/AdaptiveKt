package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveAvatarInitialsTest {

    @Test
    fun initials_for_two_name_parts_returns_two_letters() {
        assertEquals("AR", initialsForName("Alicia Romero"))
        assertEquals("NK", initialsForName("Noah Kim"))
        assertEquals("JD", initialsForName("John Doe"))
    }

    @Test
    fun initials_for_single_name_returns_first_letter() {
        assertEquals("D", initialsForName("David"))
        assertEquals("A", initialsForName("Alice"))
        assertEquals("M", initialsForName("Mike"))
    }

    @Test
    fun initials_for_empty_name_returns_question_mark() {
        assertEquals("?", initialsForName(""))
        assertEquals("?", initialsForName("   "))
    }

    @Test
    fun initials_for_name_with_multiple_spaces_returns_correct_initials() {
        assertEquals("AR", initialsForName("  Alicia   Romero  "))
        assertEquals("JD", initialsForName("John    Doe"))
    }

    @Test
    fun initials_for_name_with_leading_trailing_spaces_returns_correct_initials() {
        assertEquals("AR", initialsForName("  Alicia Romero "))
    }

    @Test
    fun initials_for_single_letter_name_returns_that_letter() {
        assertEquals("X", initialsForName("X"))
        assertEquals("A", initialsForName("a"))
    }

    @Test
    fun initials_for_names_with_hyphens_or_symbols_do_not_crash() {
        assertEquals("AS", initialsForName("Anne-Marie Smith"))
        assertEquals("?", initialsForName("--- !!!"))
    }
}
