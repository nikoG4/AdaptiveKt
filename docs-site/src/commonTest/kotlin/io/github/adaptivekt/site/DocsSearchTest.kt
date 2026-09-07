package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DocsSearchTest {
    private val entries = listOf(
        DocsSearchEntry(SiteRoute.Docs, "theme", "Theme guide", "Foundations", "Configure AdaptiveTheme colors"),
        DocsSearchEntry(SiteRoute.Components, "adaptive-theme", "AdaptiveTheme", "Foundations", "Shared colors"),
        DocsSearchEntry(SiteRoute.Components, "button", "AdaptiveButton", "Actions", "Primary action"),
    )

    @Test fun searchesAcrossRoutesAndRanksExactNamesFirst() {
        assertEquals(listOf("adaptive-theme", "theme"), searchDocs(entries, "ADAPTIVETHEME").map { it.id })
    }

    @Test fun requiresEveryWordAcrossTitleFamilyAndSummary() {
        assertEquals(listOf("adaptive-theme", "theme").toSet(), searchDocs(entries, " foundations   colors ").map { it.id }.toSet())
        assertTrue(searchDocs(entries, "button foundations").isEmpty())
    }

    @Test fun emptyAndMissingQueriesHaveNoResults() {
        assertTrue(searchDocs(entries, "  ").isEmpty())
        assertTrue(searchDocs(entries, "nonexistent").isEmpty())
    }
}
