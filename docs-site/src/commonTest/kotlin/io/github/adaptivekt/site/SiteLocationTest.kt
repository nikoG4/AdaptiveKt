package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals

class SiteLocationTest {

    @Test
    fun testRoundTripParseAndSerialize() {
        val locations = listOf(
            SiteLocation(SiteRoute.Home),
            SiteLocation(SiteRoute.Home, darkTheme = true),
            SiteLocation(SiteRoute.Docs),
            SiteLocation(SiteRoute.Docs, selectedItemId = "setup"),
            SiteLocation(SiteRoute.Docs, selectedItemId = "setup", searchQuery = "theme"),
            SiteLocation(SiteRoute.Components),
            SiteLocation(SiteRoute.Components, selectedItemId = "adaptive-button"),
            SiteLocation(SiteRoute.Components, selectedItemId = "adaptive-button", darkTheme = true),
            SiteLocation(SiteRoute.Components, selectedItemId = "adaptive-button", searchQuery = "button"),
            SiteLocation(SiteRoute.Components, selectedItemId = "adaptive-button", sectionId = "parameters"),
            SiteLocation(SiteRoute.Components, selectedItemId = "adaptive-data-view", searchQuery = "data", sectionId = "examples", darkTheme = true),
            SiteLocation(SiteRoute.Components, selectedItemId = "adaptive-select", darkTheme = true),
        )

        for (loc in locations) {
            val canonical = canonicalizeSiteLocation(loc)
            val url = serializeSiteLocation(canonical)
            
            // split URL into path, query and hash for parsing
            val hashIndex = url.indexOf('#')
            val hashPart = if (hashIndex != -1) url.substring(hashIndex) else ""
            val queryIndex = url.indexOf('?')
            val queryPart = if (queryIndex != -1) {
                if (hashIndex != -1) url.substring(queryIndex, hashIndex) else url.substring(queryIndex)
            } else ""
            val pathPart = if (queryIndex != -1) url.substring(0, queryIndex) else if (hashIndex != -1) url.substring(0, hashIndex) else url

            val parsed = parseSiteLocation(pathPart, hashPart, queryPart, false) // Default darkTheme = false
            assertEquals(canonical, parsed, "Failed round-trip for $url")
        }
    }
    
    @Test
    fun testParseQueryString() {
        // query empty
        assertEquals(emptyMap(), parseQueryString(""))
        assertEquals(emptyMap(), parseQueryString("?"))
        
        // spaces and encoding
        val q1 = parseQueryString("?q=adaptive+button")
        assertEquals("adaptive button", q1["q"])
        
        val q2 = parseQueryString("?q=special%20chars%26%3D")
        assertEquals("special chars&=", q2["q"])
    }
    
    @Test
    fun testCanonicalizationAndNormalization() {
        assertEquals("adaptive-button", normalizeDocsId("Adaptive-Button"))
        assertEquals("setup-guide", normalizeDocsId("Setup---Guide "))
        assertEquals("a-b", normalizeDocsId("A!@#B"))
        
        val dirty = SiteLocation(
            route = SiteRoute.Components,
            selectedItemId = " A!!B--c ",
            sectionId = "  Section_1 ",
            searchQuery = "  search  "
        )
        val clean = canonicalizeSiteLocation(dirty)
        assertEquals("a-b-c", clean.selectedItemId)
        assertEquals("section-1", clean.sectionId)
        assertEquals("search", clean.searchQuery)
    }
}
