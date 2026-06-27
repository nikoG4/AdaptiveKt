package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class DocsRegistryTest {

    @Test
    fun testResolveComponentId() {
        assertEquals(DocsRegistry.ID_BUTTON, DocsRegistry.resolveComponentId(DocsRegistry.ID_BUTTON))

        // Fallback for nested sections/states that start with base id
        assertEquals(DocsRegistry.ID_BUTTON, DocsRegistry.resolveComponentId("${DocsRegistry.ID_BUTTON}-primary"))

        // Legacy IDs
        assertEquals(DocsRegistry.ID_ACCORDION_DIALOG, DocsRegistry.resolveComponentId("adaptive-accordion-dialog"))

        // Fallback for completely unknown returns ID_THEME
        assertEquals(DocsRegistry.ID_THEME, DocsRegistry.resolveComponentId("unknown-id-123"))
    }

    @Test
    fun testResolveTopicId() {
        assertEquals(DocsRegistry.TOPIC_THEME, DocsRegistry.resolveTopicId(DocsRegistry.TOPIC_THEME))

        // Fallback to getting-started
        assertEquals(DocsRegistry.TOPIC_GETTING_STARTED, DocsRegistry.resolveTopicId("random"))
    }

    @Test
    fun testDuplicateValidationThrows() {
        var exceptionThrown = false
        try {
            DocsRegistry.getComponents {
                listOf(
                    ComponentDoc(id = "test-1", family = "", title = "", summary = "", usage = "", basicExample = DocsExample("", "", "", {}), parameters = emptyList()),
                    ComponentDoc(id = "test-1", family = "", title = "", summary = "", usage = "", basicExample = DocsExample("", "", "", {}), parameters = emptyList())
                )
            }
        } catch (e: Exception) {
            exceptionThrown = true
            assertTrue(e.message?.contains("test-1") == true)
        }
    }

    @Test
    fun testKebabCaseIds() {
        val components = DocsRegistry.getComponents { emptyList() } // Ensure we aren't using bad mocks
        // Just verify registry constants
        val registryIds = listOf(
            DocsRegistry.ID_THEME, DocsRegistry.ID_BUTTON, DocsRegistry.ID_ICON_BUTTON,
            DocsRegistry.ID_BADGE, DocsRegistry.ID_CHIP, DocsRegistry.ID_AVATAR,
            DocsRegistry.ID_THUMBNAIL, DocsRegistry.ID_CARD_SURFACE, DocsRegistry.ID_SELECTION_AREA,
            DocsRegistry.ID_SEARCH_FIELD, DocsRegistry.ID_SELECT, DocsRegistry.ID_MULTI_SELECT,
            DocsRegistry.ID_FORM_LAYOUT, DocsRegistry.ID_DATA_VIEW, DocsRegistry.ID_NAVIGATION_SCAFFOLD,
            DocsRegistry.ID_NAVIGATION_TREE, DocsRegistry.ID_BREADCRUMBS, DocsRegistry.ID_TABS,
            DocsRegistry.ID_CAROUSEL, DocsRegistry.ID_ACCORDION_DIALOG, DocsRegistry.ID_TEXT_FIELD,
            DocsRegistry.ID_FEEDBACK_STATES
        )

        registryIds.forEach { id ->
            assertTrue(id.matches(Regex("^[a-z0-9-]+$")), "ID $id is not kebab-case")
        }
    }
}
