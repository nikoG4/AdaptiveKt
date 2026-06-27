package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SiteSearchTest {

    @kotlin.test.Ignore
    @Test
    fun testSearchFunctionality() {
        val mockComponents = listOf(
            ComponentDoc(id = "adaptive-button", family = "Forms", title = "AdaptiveButton", summary = "A simple button", usage = "val b = AdaptiveButton()", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
            ComponentDoc(id = "adaptive-card", family = "Layout", title = "AdaptiveCard", summary = "A versatile card", usage = "val c = AdaptiveCard()", basicExample = DocsExample("", "", "", {}), parameters = emptyList())
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

        // Exact match
        val res1 = index.search("AdaptiveButton")
        assertEquals(1, res1.size)
        assertEquals("adaptive-button", res1[0].id)

        // Prefix match
        val res2 = index.search("Adaptive")
        assertEquals(2, res2.size) // both button and card

        // Match in description
        val res3 = index.search("versatile")
        assertEquals(1, res3.size)
        assertEquals("adaptive-card", res3[0].id)

        // Empty
        assertTrue(index.search("").isEmpty())

        // Topic search
        val res4 = index.search("started")
        assertEquals(1, res4.size)
        assertEquals("getting-started", res4[0].id)

        // Multiple terms
        val res5 = index.search("adaptive simple")
        assertEquals(1, res5.size)
        assertEquals("adaptive-button", res5[0].id)

        // No match
        assertTrue(index.search("random_garbage_string").isEmpty())
    }
}
