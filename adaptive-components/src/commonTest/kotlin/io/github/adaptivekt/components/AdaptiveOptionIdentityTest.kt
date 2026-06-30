package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveOptionIdentityTest {

    data class TestOption(val id: String, val label: String)

    @Test
    fun testOptionKeyOf_withKeySelector() {
        val option = TestOption("1", "A")
        assertEquals("1", optionKeyOf(option) { it.id })
    }

    @Test
    fun testOptionKeyOf_withoutKeySelector() {
        val option = TestOption("1", "A")
        assertEquals(option, optionKeyOf(option, null))
    }

    @Test
    fun testIsOptionSame_sameInstance() {
        val option = TestOption("1", "A")
        assertTrue(isOptionSame(option, option, null))
    }

    @Test
    fun testIsOptionSame_sameKeyDifferentInstance() {
        val option1 = TestOption("1", "A")
        val option2 = TestOption("1", "B")
        assertTrue(isOptionSame(option1, option2) { it.id })
    }

    @Test
    fun testIsOptionSame_differentKey() {
        val option1 = TestOption("1", "A")
        val option2 = TestOption("2", "A")
        assertFalse(isOptionSame(option1, option2) { it.id })
    }

    @Test
    fun testValidateOptionKeys_noDuplicates() {
        val options = listOf(TestOption("1", "A"), TestOption("2", "B"))
        validateOptionKeys(options) { it.id } // Should not throw
    }

    @Test
    fun testValidateOptionKeys_withDuplicates() {
        val options = listOf(TestOption("1", "A"), TestOption("1", "B"))
        assertFailsWith<IllegalArgumentException> {
            validateOptionKeys(options) { it.id }
        }
    }

    @Test
    fun testToggleOptionByKey() {
        val options = listOf(TestOption("1", "A"), TestOption("2", "B"))
        var selected = listOf(TestOption("1", "A"))

        // Add
        selected = toggleOptionByKey(selected, TestOption("2", "B")) { it.id }
        assertEquals(2, selected.size)
        assertEquals("1", selected[0].id)
        assertEquals("2", selected[1].id)

        // Remove
        selected = toggleOptionByKey(selected, TestOption("1", "A_updated")) { it.id }
        assertEquals(1, selected.size)
        assertEquals("2", selected[0].id)
    }
}
