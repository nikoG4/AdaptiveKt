package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveSelectFilteringPolicyTest {
    private val options = listOf("Administration", "Billing", "Support")

    @Test
    fun localSearchFiltersCaseInsensitively() {
        assertEquals(
            listOf("Administration"),
            resolveVisibleSelectOptions(options, "ADMIN", searchable = true, externallyFiltered = false, optionLabel = { it }),
        )
    }

    @Test
    fun externalSearchUsesTheSuppliedOptionsAsIs() {
        assertEquals(
            options,
            resolveVisibleSelectOptions(options, "doesn't match", searchable = true, externallyFiltered = true, optionLabel = { it }),
        )
    }

    @Test
    fun disabledSearchNeverFilters() {
        assertEquals(
            options,
            resolveVisibleSelectOptions(options, "random", searchable = false, externallyFiltered = false, optionLabel = { it }),
        )
    }

    @Test
    fun emptyQueryPreservesAllItems() {
        assertEquals(
            options,
            resolveVisibleSelectOptions(options, "", searchable = true, externallyFiltered = false, optionLabel = { it }),
        )
    }

    @Test
    fun unmatchedQueryProducesEmptyResults() {
        assertEquals(
            emptyList(),
            resolveVisibleSelectOptions(options, "missing", searchable = true, externallyFiltered = false, optionLabel = { it }),
        )
    }
}
