package io.github.adaptivekt.site

internal data class SearchResult(
    val id: String,
    val title: String,
    val description: String,
    val route: SiteRoute,
    val score: Int
)

internal class SiteSearchIndex {
    private val records = mutableListOf<SearchRecord>()

    fun buildIndex(
        components: List<ComponentDoc> = DocsRegistry.getComponents { emptyList() },
        topics: List<DocsTopic> = DocsRegistry.getTopics { emptyList() }
    ) {
        if (records.isNotEmpty()) return

        // Index components
        components.forEach { doc ->
            records.add(
                SearchRecord(
                    id = doc.id,
                    title = doc.title,
                    description = doc.summary,
                    content = listOf(doc.usage) + doc.themingNotes + doc.accessibilityNotes,
                    route = SiteRoute.Components
                )
            )
        }

        // Index docs
        topics.forEach { topic ->
            records.add(
                SearchRecord(
                    id = topic.id,
                    title = topic.title,
                    description = topic.summary,
                    content = emptyList(),
                    route = SiteRoute.Docs
                )
            )
        }
    }

    fun search(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()

        val tokens = query.lowercase().split(" ", "\t", "\n", "\r").filter { it.isNotBlank() }
        if (tokens.isEmpty()) return emptyList()

        return records.mapNotNull { record ->
            var score = 0
            val titleLower = record.title.lowercase()
            val descLower = record.description.lowercase()
            val contentLower = record.content.joinToString(" ").lowercase()

            for (token in tokens) {
                if (titleLower.contains(token)) {
                    score += if (titleLower.startsWith(token)) 100 else 50
                } else if (descLower.contains(token)) {
                    score += 20
                } else if (contentLower.contains(token)) {
                    score += 5
                } else if (record.id.contains(token)) {
                    score += 10
                } else {
                    // Token not found anywhere, reject or penalize? Let's penalize heavily, require all tokens to match something
                    return@mapNotNull null
                }
            }

            if (score > 0) {
                SearchResult(record.id, record.title, record.description, record.route, score)
            } else null
        }.sortedByDescending { it.score }.take(10)
    }

    private data class SearchRecord(
        val id: String,
        val title: String,
        val description: String,
        val content: List<String>,
        val route: SiteRoute
    )
}
