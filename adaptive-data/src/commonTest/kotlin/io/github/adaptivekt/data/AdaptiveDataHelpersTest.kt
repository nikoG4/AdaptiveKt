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
