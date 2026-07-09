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
        val config = normalizeAdaptiveDataColumnConfigState(
            listOf(
                AdaptiveColumnConfig("name"),
                AdaptiveColumnConfig("status"),
            )
        )

        val sort = AdaptiveDataSortState()
            .toggleColumnSort(
                columnId = "name",
                sortableColumnIds = setOf("name", "status"),
            )
            .setColumnSortDirection("name", AdaptiveSortDirection.Descending)

        val promoted = sort.promoteColumnSort("name")
        val reordered = promoted.reorderSortPriority(fromIndex = 0, toIndex = 0)

        val primary = reordered.primarySort
        val sorted = reordered.isSorted
        check(primary != null && sorted)

        val querySort = reordered.toQuerySort()
        val resolvedQuery = resolveQuerySortFromState(reordered, config)
        check(querySort == resolvedQuery)

        val queryState = AdaptiveQueryState(sortKey = "name", sortDirection = AdaptiveSortDirection.Ascending)
        val fromQuery = queryState.toSortState(config)
        check(fromQuery.isSorted)

        val writtenBack = queryState.withSortState(reordered, config)
        check(writtenBack.sortKey == "name")

        val emptyQuery = AdaptiveQueryState().withSortState(AdaptiveDataSortState(), config)
        check(emptyQuery.sortKey == null)

        val pinned = config.setColumnPin("name", AdaptiveColumnPin.Start)
        check(pinned.visibleColumnIds.contains("name"))

        val widened = pinned.setColumnWidth("name", 200)
        check(widened.getConfig("name")?.width == 200)

        val reset = resetAdaptiveDataColumnConfigState(
            listOf(AdaptiveColumnConfig("name"), AdaptiveColumnConfig("status"))
        )
        check(reset.columns.isNotEmpty())
    }

    @Test
    fun passesCompilation() {
        compileColumnStateTypes()
        // This test solely checks if the above functions compile.
    }
}
