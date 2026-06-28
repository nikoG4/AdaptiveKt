package io.github.adaptivekt.site

import androidx.compose.runtime.compositionLocalOf

internal val LocalSiteLocation = compositionLocalOf<SiteLocation> { error("No SiteLocation provided") }

internal data class SiteLocation(
    val route: SiteRoute,
    val selectedItemId: String? = null,
    val sectionId: String? = null,
    val searchQuery: String? = null,
    val darkTheme: Boolean = false,
    val visualState: String? = null,
)

internal fun parseSiteLocation(
    path: String,
    hash: String,
    queryString: String,
    darkTheme: Boolean
): SiteLocation {
    val route = when {
        path.contains("/docs/") -> SiteRoute.Docs
        path.contains("/components/") -> SiteRoute.Components
        path.contains("/demo/") -> SiteRoute.Demo
        else -> SiteRoute.Home
    }

    // Parse query params
    val queryParams = parseQueryString(queryString)
    val q = queryParams["q"]?.takeIf { it.isNotBlank() }
    val section = queryParams["section"]?.takeIf { it.isNotBlank() }
    val themeOverride = queryParams["theme"]
    val captureState = queryParams["capture"]

    val finalDarkTheme = when (themeOverride) {
        "dark" -> true
        "light" -> false
        else -> darkTheme
    }

    val selectedId = hash.removePrefix("#").takeIf { it.isNotBlank() }

    return SiteLocation(
        route = route,
        selectedItemId = selectedId,
        sectionId = section,
        searchQuery = q,
        darkTheme = finalDarkTheme,
        visualState = captureState,
    )
}

internal fun serializeSiteLocation(location: SiteLocation): String {
    val routePath = location.route.path // This will be "/", "/docs/", "/components/"

    val queryParts = mutableListOf<String>()
    if (!location.searchQuery.isNullOrBlank()) {
        queryParts.add("q=${encodeURIComponent(location.searchQuery)}")
    }
    if (!location.sectionId.isNullOrBlank()) {
        queryParts.add("section=${encodeURIComponent(location.sectionId)}")
    }
    if (location.darkTheme) {
        queryParts.add("theme=dark")
    }
    if (!location.visualState.isNullOrBlank()) {
        queryParts.add("capture=${encodeURIComponent(location.visualState)}")
    }

    val queryString = if (queryParts.isNotEmpty()) "?" + queryParts.joinToString("&") else ""
    val hashString = if (!location.selectedItemId.isNullOrBlank()) "#${location.selectedItemId}" else ""

    return "$routePath$queryString$hashString"
}

internal fun canonicalizeSiteLocation(location: SiteLocation): SiteLocation {
    // Basic normalization of strings to prevent strange characters
    return location.copy(
        selectedItemId = location.selectedItemId?.let { normalizeDocsId(it) },
        sectionId = location.sectionId?.let { normalizeSectionId(it) },
        searchQuery = location.searchQuery?.let { sanitizeSearchQuery(it) }
    )
}

internal fun sanitizeSearchQuery(query: String): String {
    return query.trim().take(100)
}

internal fun normalizeDocsId(id: String): String {
    // Only lowercase alphanumeric and hyphens
    return id.trim().lowercase().replace(Regex("[^a-z0-9\\-]"), "-").replace(Regex("-+"), "-").trim('-')
}

internal fun normalizeSectionId(id: String): String {
    return normalizeDocsId(id)
}

// Simple pure query string parser since URLSearchParams isn't in commonMain
internal fun parseQueryString(query: String): Map<String, String> {
    val q = query.removePrefix("?")
    if (q.isBlank()) return emptyMap()

    val map = mutableMapOf<String, String>()
    q.split("&").forEach { pair ->
        val parts = pair.split("=", limit = 2)
        if (parts.isNotEmpty() && parts[0].isNotBlank()) {
            val key = decodeURIComponent(parts[0])
            val value = if (parts.size > 1) decodeURIComponent(parts[1]) else ""
            map[key] = value
        }
    }
    return map
}

internal fun encodeURIComponent(str: String): String {
    return PlatformInterop.encodeUrlComponent(str).replace("%20", "+")
}

internal fun decodeURIComponent(str: String): String {
    return PlatformInterop.decodeUrlComponent(str.replace("+", "%20"))
}

internal fun buildAbsoluteSiteUrl(location: SiteLocation): String {
    val origin = PlatformInterop.getWindowOrigin()
    val basePath = PlatformInterop.getWindowBasePath().trimEnd('/')
    return origin + basePath + serializeSiteLocation(location)
}


