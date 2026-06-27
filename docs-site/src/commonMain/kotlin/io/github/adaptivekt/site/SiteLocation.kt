package io.github.adaptivekt.site

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
    // Basic JS encodeURIComponent equivalent for commonMain, handling common cases.
    val allowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()"
    val sb = StringBuilder()
    for (char in str) {
        if (char in allowed) {
            sb.append(char)
        } else if (char == ' ') {
            sb.append("+")
        } else {
            val bytes = char.toString().encodeToByteArray()
            for (b in bytes) {
                val hex = b.toUByte().toString(16).uppercase()
                sb.append("%").append(if (hex.length == 1) "0$hex" else hex)
            }
        }
    }
    return sb.toString()
}

internal fun decodeURIComponent(str: String): String {
    // Best effort decode for commonMain
    var i = 0
    val sb = StringBuilder()
    while (i < str.length) {
        val c = str[i]
        if (c == '+') {
            sb.append(' ')
            i++
        } else if (c == '%' && i + 2 < str.length) {
            val hex = str.substring(i + 1, i + 3)
            try {
                val byte = hex.toInt(16).toByte()
                sb.append(byte.toInt().toChar())
                i += 3
            } catch (e: Exception) {
                sb.append(c)
                i++
            }
        } else {
            sb.append(c)
            i++
        }
    }
    return sb.toString()
}
