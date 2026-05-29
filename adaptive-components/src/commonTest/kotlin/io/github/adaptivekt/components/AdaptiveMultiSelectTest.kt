package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveMultiSelectTest {

    @Test
    fun filterMultiSelectOptions_emptyQuery_returnsAllOptions() {
        val options = listOf("Alpha", "Beta", "")

        assertEquals(options, filterMultiSelectOptions(options, "", optionLabel = { it }))
    }

    @Test
    fun filterMultiSelectOptions_matchesIgnoringCase() {
        val options = listOf("Operations", "Finance", "Customer Support")

        assertEquals(
            listOf("Customer Support"),
            filterMultiSelectOptions(options, "support", optionLabel = { it }),
        )
    }

    @Test
    fun filterMultiSelectOptions_emptyLabelsDoNotCrash() {
        val options = listOf("", "Admin")

        assertEquals(emptyList(), filterMultiSelectOptions(options, "missing", optionLabel = { it }))
    }

    @Test
    fun visibleMultiSelectChips_respectsMaxVisibleChips() {
        assertEquals(
            listOf("A", "B"),
            visibleMultiSelectChips(listOf("A", "B", "C"), maxVisibleChips = 2),
        )
    }

    @Test
    fun visibleMultiSelectChips_negativeMaxReturnsEmptyList() {
        assertEquals(
            emptyList(),
            visibleMultiSelectChips(listOf("A", "B"), maxVisibleChips = -1),
        )
    }

    @Test
    fun hiddenMultiSelectChipCount_returnsOverflowCount() {
        assertEquals(2, hiddenMultiSelectChipCount(selectedCount = 5, maxVisibleChips = 3))
    }

    @Test
    fun hiddenMultiSelectChipCount_noOverflowReturnsZero() {
        assertEquals(0, hiddenMultiSelectChipCount(selectedCount = 2, maxVisibleChips = 3))
    }

    @Test
    fun hiddenMultiSelectChipCount_negativeMaxTreatsMaxAsZero() {
        assertEquals(2, hiddenMultiSelectChipCount(selectedCount = 2, maxVisibleChips = -4))
    }
}
