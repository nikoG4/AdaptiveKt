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

    @Suppress("UNUSED")
    @Composable
    fun verifyBulkActionBarWithKeys() {
        AdaptiveDataBulkActionBar(
            selectedKeys = setOf("1"),
            actions = emptyList(),
            onClearSelection = {}
        )
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyBulkActionBarWithScope() {
        val scope = object : AdaptiveDataBulkActionScope<String> {
            override val selectedKeys = setOf("1")
            override val selectedCount = 1
            override fun clearSelection() {}
        }
        AdaptiveDataBulkActionBar(
            scope = scope,
            actions = emptyList()
        )
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyNewSelectionOverloadSingleMode() {
        AdaptiveDataView(
            state = dummyState,
            rowKey = { it },
            columns = emptyList(),
            selectionMode = AdaptiveDataSelectionMode.Single,
            selectionState = AdaptiveDataSelectionState(emptySet(), null),
            onSelectionStateChange = {},
        )
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyBulkActionBarWithCustomDescriptions() {
        AdaptiveDataBulkActionBar(
            selectedKeys = setOf("1"),
            actions = emptyList(),
            onClearSelection = {},
            clearSelectionContentDescription = "Limpiar todo",
            overflowContentDescription = "Más opciones"
        )
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyAdaptiveMenuItemSignatures() {
        io.github.adaptivekt.components.AdaptiveMenuItem(
            text = "Menu 1",
            onClick = {},
        )
        io.github.adaptivekt.components.AdaptiveMenuItem(
            text = "Menu 2",
            onClick = {},
            enabled = false
        )
    }

    private fun compileColumnStateTypes() {
        val sort = AdaptiveDataSortState().toggleColumnSort(
            columnId = "name",
            sortableColumnIds = setOf("name", "status"),
        )

        val querySort = sort.toQuerySort()

        val columns = normalizeAdaptiveDataColumnConfigState(
            listOf(
                AdaptiveColumnConfig("name"),
                AdaptiveColumnConfig("status"),
            )
        ).setColumnPin("name", AdaptiveColumnPin.Start)

        check(querySort.first == "name")
        check(columns.columns.isNotEmpty())
    }

    @Test
    fun passesCompilation() {
        // This test solely checks if the above functions compile.
    }
}
