package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SiteSearchTest {

    @Test
    fun testSearchFunctionalityWithDocsCatalog() {
        // Search from fresh process using the real DocsCatalog
        val index = SiteSearchIndex()
        index.buildIndex() // Should use DocsCatalog default
        
        // Ensure index is populated
        assertTrue(index.search("Adaptive").isNotEmpty())
    }

    @Test
    fun testSearchFunctionality() {
        val mockComponents = listOf(
            ComponentDoc(id = "adaptive-button", family = "Forms", title = "AdaptiveButton", summary = "A simple button", usage = "val b = AdaptiveButton()", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-card", family = "Layout", title = "AdaptiveCard", summary = "A versatile card", usage = "val c = AdaptiveCard()", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-icon-button", family = "Forms", title = "AdaptiveIconButton", summary = "Icon button", usage = "AdaptiveIconButton()", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-data-view", family = "Data", title = "AdaptiveDataView", summary = "Table display", usage = "table", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-select", family = "Forms", title = "AdaptiveSelect", summary = "Dropdown", usage = "select", basicExample = DocsExample("", "", "", {}), parameters = listOf(ComponentParameter("onSelect", "() -> Unit", "Callback", true, "Callback description"))),
            ComponentDoc(id = "adaptive-multi-select", family = "Forms", title = "AdaptiveMultiSelect", summary = "Multi select", usage = "multi", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-navigation-scaffold", family = "Navigation", title = "AdaptiveNavigationScaffold", summary = "Sidebar layout", usage = "sidebar", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-navigation-tree", family = "Navigation", title = "AdaptiveNavigationTree", summary = "Tree view", usage = "tree", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "feedback-states", family = "Feedback", title = "FeedbackStates", summary = "Spinner and feedback", usage = "spinner", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-accordion-dialog", family = "Layout", title = "AdaptiveAccordionDialog", summary = "Modal dialog", usage = "modal", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "unicode-test", family = "Misc", title = "Español y 日本語", summary = "Prueba de caracteres", usage = "", basicExample = DocsExample("", "", "", {}), parameters = emptyList())
        )
        val mockTopics = listOf(
            DocsTopic(
                id = "getting-started",
                family = "Getting started",
                title = "Getting Started",
                summary = "How to start using AdaptiveKt",
                content = {}
            )
        )

        val index = SiteSearchIndex()
        index.buildIndex(mockComponents, mockTopics)

        // Empty query & spaces only
        assertTrue(index.search("").isEmpty())
        assertTrue(index.search("   ").isEmpty())

        // Exact title
        val exact = index.search("AdaptiveButton")
        assertEquals("adaptive-button", exact[0].id)

        // Short title without Adaptive
        val short = index.search("button")
        assertTrue(short.map { it.id }.contains("adaptive-button"))

        // Prefix
        assertTrue(index.search("Adapt").size > 1)

        // Family
        assertTrue(index.search("Forms").isNotEmpty())

        // Summary
        assertEquals("adaptive-card", index.search("versatile card")[0].id)

        // Usage
        assertEquals("adaptive-card", index.search("val c = AdaptiveCard()")[0].id)

        // Parameter name & type
        val selectRes = index.search("onSelect")
        assertEquals("adaptive-select", selectRes[0].id)
        val selectRes2 = index.search("() -> Unit")
        assertEquals("adaptive-select", selectRes2[0].id)

        // Multiple terms
        assertEquals("adaptive-button", index.search("adaptive simple")[0].id)

        // Unicode
        assertTrue(index.search("Español").isNotEmpty())
        assertTrue(index.search("日本語").isNotEmpty())

        // Special characters
        assertTrue(index.search("->").isNotEmpty())

        // Unknown query
        assertTrue(index.search("random_garbage_string").isEmpty())
        
        // Very long query
        assertTrue(index.search("this is a very long query that should just safely return nothing instead of crashing the regex engine").isEmpty())
        
        // Canonical queries check
        assertEquals("adaptive-button", index.search("button")[0].id)
        assertEquals("adaptive-icon-button", index.search("icon button")[0].id)
        assertEquals("adaptive-data-view", index.search("table")[0].id)
        assertEquals("adaptive-select", index.search("dropdown")[0].id)
        assertEquals("adaptive-multi-select", index.search("multi select")[0].id)
        assertEquals("adaptive-navigation-scaffold", index.search("sidebar")[0].id)
        assertEquals("adaptive-navigation-tree", index.search("tree")[0].id)
        assertEquals("feedback-states", index.search("spinner")[0].id)
        assertEquals("adaptive-accordion-dialog", index.search("modal")[0].id)
    }
}
