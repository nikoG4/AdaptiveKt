package io.github.adaptivekt.data

import io.github.adaptivekt.core.AdaptiveBreakpoint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveDataHelpersTest {
    @Test
    fun `should use table layout for expanded and large breakpoints`() {
        assertFalse(shouldUseTableLayout(AdaptiveBreakpoint.Compact))
        assertFalse(shouldUseTableLayout(AdaptiveBreakpoint.Medium))
        assertTrue(shouldUseTableLayout(AdaptiveBreakpoint.Expanded))
        assertTrue(shouldUseTableLayout(AdaptiveBreakpoint.Large))
    }

    @Test
    fun `data display auto mode preserves table to cards behavior`() {
        assertEquals(
            AdaptiveDataDisplayMode.Cards,
            resolveAdaptiveDataDisplayMode(AdaptiveBreakpoint.Compact),
        )
        assertEquals(
            AdaptiveDataDisplayMode.Table,
            resolveAdaptiveDataDisplayMode(AdaptiveBreakpoint.Large),
        )
        assertEquals(
            AdaptiveDataDisplayMode.Table,
            resolveAdaptiveDataDisplayMode(
                breakpoint = AdaptiveBreakpoint.Compact,
                autoSwitchToCards = false,
            ),
        )
        assertEquals(
            AdaptiveDataDisplayMode.Cards,
            resolveAdaptiveDataDisplayMode(
                breakpoint = AdaptiveBreakpoint.Large,
                displayMode = AdaptiveDataDisplayMode.Cards,
            ),
        )
    }

    @Test
    fun `collection auto mode chooses list on compact and grid otherwise`() {
        assertEquals(
            AdaptiveCollectionDisplayMode.List,
            resolveAdaptiveCollectionDisplayMode(AdaptiveBreakpoint.Compact),
        )
        assertEquals(
            AdaptiveCollectionDisplayMode.Grid,
            resolveAdaptiveCollectionDisplayMode(AdaptiveBreakpoint.Medium),
        )
        assertEquals(
            AdaptiveCollectionDisplayMode.Cards,
            resolveAdaptiveCollectionDisplayMode(
                breakpoint = AdaptiveBreakpoint.Compact,
                displayMode = AdaptiveCollectionDisplayMode.Cards,
            ),
        )
    }

    @Test
    fun `pagination helpers clamp bounds`() {
        assertEquals(1, adaptivePageCount(totalItems = 0, pageSize = 20))
        assertEquals(3, adaptivePageCount(totalItems = 41, pageSize = 20))
        assertEquals(1, coerceAdaptivePage(page = -2, totalItems = 41, pageSize = 20))
        assertEquals(3, coerceAdaptivePage(page = 99, totalItems = 41, pageSize = 20))
    }

    @Test
    fun `query helpers reset page when criteria change`() {
        val query = AdaptiveQueryState(
            search = "old",
            filters = mapOf("category" to setOf("audio")),
            sortKey = "popular",
            page = 4,
            pageSize = 20,
        )

        assertEquals(1, query.withSearch("new").page)
        assertEquals("new", query.withSearch("new").search)
        assertEquals(1, query.withFilter("category", setOf("laptops")).page)
        assertEquals(setOf("laptops"), query.withFilter("category", setOf("laptops")).filters["category"])
        assertEquals(1, query.withSort("price").page)
        assertEquals("price", query.withSort("price").sortKey)
        assertEquals(2, query.withPage(2).page)
        assertEquals(50, query.withPageSize(50).pageSize)
        assertEquals(1, query.withPageSize(50).page)
    }

    @Test
    fun `visible columns respects min breakpoint and falls back when none visible`() {
        val columns = listOf(
            AdaptiveDataColumn<String>(
                id = "first",
                header = "First",
                minBreakpoint = AdaptiveBreakpoint.Expanded,
                cell = { }
            ),
            AdaptiveDataColumn<String>(
                id = "second",
                header = "Second",
                minBreakpoint = AdaptiveBreakpoint.Large,
                cell = { }
            ),
        )

        val compactVisible = visibleColumnsForBreakpoint(columns, AdaptiveBreakpoint.Compact)
        assertEquals(columns, compactVisible)

        val expandedVisible = visibleColumnsForBreakpoint(columns, AdaptiveBreakpoint.Expanded)
        assertEquals(1, expandedVisible.size)
        assertEquals("first", expandedVisible.first().id)

        val largeVisible = visibleColumnsForBreakpoint(columns, AdaptiveBreakpoint.Large)
        assertEquals(2, largeVisible.size)
    }

    @Test
    fun `mobile column heuristics infer title subtitle status and limited metadata`() {
        val columns = listOf(
            AdaptiveDataColumn<String>(id = "name", header = "Name", cell = { }),
            AdaptiveDataColumn<String>(id = "role", header = "Role", cell = { }),
            AdaptiveDataColumn<String>(id = "department", header = "Department", cell = { }),
            AdaptiveDataColumn<String>(id = "status", header = "Status", cell = { }),
            AdaptiveDataColumn<String>(id = "email", header = "Email", cell = { }),
            AdaptiveDataColumn<String>(id = "notes", header = "Notes", cell = { }),
        )

        val resolved = resolveMobileColumns(columns)

        assertEquals(5, resolved.size)
        assertEquals(AdaptiveDataMobileRole.Title, resolved.first { it.column.id == "name" }.role)
        assertEquals(AdaptiveDataMobileRole.Subtitle, resolved.first { it.column.id == "role" }.role)
        assertEquals(AdaptiveDataMobileRole.Status, resolved.first { it.column.id == "status" }.role)
        assertEquals(AdaptiveDataMobileRole.Metadata, resolved.first { it.column.id == "department" }.role)
        assertTrue(resolved.none { it.column.id == "notes" })
    }
}
