package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import kotlin.test.assertContains

class DocsRegistryTest {

    @Test
    fun testResolveComponentId() {
        assertEquals(DocsRegistry.ID_BUTTON, DocsRegistry.resolveComponentId(DocsRegistry.ID_BUTTON))
        assertEquals(DocsRegistry.ID_BUTTON, DocsRegistry.resolveComponentId("${DocsRegistry.ID_BUTTON}-primary"))
        assertEquals(DocsRegistry.ID_ACCORDION_DIALOG, DocsRegistry.resolveComponentId("adaptive-accordion-dialog"))
        assertEquals(DocsRegistry.ID_THEME, DocsRegistry.resolveComponentId("unknown-id-123"))
    }

    @Test
    fun testResolveTopicId() {
        assertEquals(DocsRegistry.TOPIC_THEME, DocsRegistry.resolveTopicId(DocsRegistry.TOPIC_THEME))
        assertEquals(DocsRegistry.TOPIC_GETTING_STARTED, DocsRegistry.resolveTopicId("random"))
    }

    @Test
    fun duplicate_ID_fails() {
        val e = assertFailsWith<IllegalArgumentException> {
            DocsRegistry.requireValidComponentDocs(listOf(
                createMockDoc("test-1"),
                createMockDoc("test-1")
            ))
        }
        assertContains(e.message ?: "", "test-1")
    }

    @Test
    fun empty_ID_fails() {
        val e = assertFailsWith<IllegalArgumentException> {
            DocsRegistry.requireValidComponentDocs(listOf(createMockDoc("")))
        }
        assertContains(e.message ?: "", "cannot be empty")
    }

    @Test
    fun invalid_ID_fails() {
        val e = assertFailsWith<IllegalArgumentException> {
            DocsRegistry.requireValidComponentDocs(listOf(createMockDoc("Invalid_ID")))
        }
        assertContains(e.message ?: "", "not kebab-case")
    }

    @Test
    fun empty_catalog_fails() {
        assertFailsWith<IllegalArgumentException> {
            DocsRegistry.requireValidComponentDocs(emptyList())
        }
    }

    @Test
    fun valid_catalog_passes() {
        DocsRegistry.requireValidComponentDocs(listOf(createMockDoc("test-1"), createMockDoc("test-2")))
    }

    @Test
    fun registry_contains_every_rendered_component() {
        val docs = componentDocs()
        val renderedIds = docs.map { it.id }.toSet()
        val registryIds = DocsRegistry.allComponentIds

        renderedIds.forEach { id ->
            assertTrue(id in registryIds, "Rendered ID $id is missing in DocsRegistry.allComponentIds")
        }
    }

    @Test
    fun rendered_catalog_contains_no_unknown_registry_ID() {
        val docs = componentDocs()
        val renderedIds = docs.map { it.id }.toSet()
        val registryIds = DocsRegistry.allComponentIds

        registryIds.forEach { id ->
            assertTrue(id in renderedIds, "Registry ID $id is not rendered in componentDocs()")
        }
    }

    private fun createMockDoc(id: String) = ComponentDoc(
        id = id,
        family = "family",
        title = "title",
        summary = "summary",
        usage = "usage",
        basicExample = DocsExample("title", "desc", "code", {}),
        parameters = emptyList()
    )
}

