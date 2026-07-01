package io.github.adaptivekt.data

import androidx.compose.runtime.Composable
import kotlin.test.Test

class AdaptiveDataViewCompileTest {

    private val dummyState = AdaptiveDataContent(listOf("A", "B", "C"))

    @Suppress("UNUSED")
    @Composable
    fun verifyOldOverload() {
        AdaptiveDataView(
            state = dummyState,
            columns = emptyList(),
            onItemClick = {},
            rowActions = emptyList(),
        )
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyNewSelectionOverload() {
        AdaptiveDataView(
            state = dummyState,
            rowKey = { it },
            columns = emptyList(),
            selectionMode = AdaptiveDataSelectionMode.Multiple,
            selectionState = AdaptiveDataSelectionState(emptySet(), null),
            onSelectionStateChange = {},
            rowEnabled = { true },
            rowClickBehavior = AdaptiveDataRowClickBehavior.SelectAndActivate,
        )
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyNewSelectionOverloadWithBulkActionSlot() {
        AdaptiveDataView(
            state = dummyState,
            rowKey = { it },
            columns = emptyList(),
            selectionMode = AdaptiveDataSelectionMode.Multiple,
            selectionState = AdaptiveDataSelectionState(setOf("A"), null),
            onSelectionStateChange = {},
            rowEnabled = { true },
            rowClickBehavior = AdaptiveDataRowClickBehavior.SelectAndActivate,
            bulkActionContent = {
                val keys = selectedKeys
                val count = selectedCount
                clearSelection()
            }
        )
    }

    @Test
    fun passesCompilation() {
        // This test solely checks if the above functions compile.
    }
}
