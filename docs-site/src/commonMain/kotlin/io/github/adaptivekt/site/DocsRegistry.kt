package io.github.adaptivekt.site

internal object DocsRegistry {

    // Component IDs
    const val ID_THEME = "adaptive-theme"
    const val ID_BUTTON = "adaptive-button"
    const val ID_ICON_BUTTON = "adaptive-icon-button"
    const val ID_BADGE = "adaptive-badge"
    const val ID_CHIP = "adaptive-chip"
    const val ID_AVATAR = "adaptive-avatar"
    const val ID_THUMBNAIL = "adaptive-thumbnail"
    const val ID_CARD_SURFACE = "adaptive-card-surface"
    const val ID_SELECTION_AREA = "adaptive-selection-area"
    const val ID_SEARCH_FIELD = "adaptive-search-field"
    const val ID_SELECT = "adaptive-select"
    const val ID_MULTI_SELECT = "adaptive-multi-select"
    const val ID_FORM_LAYOUT = "adaptive-form-layout"
    const val ID_DATA_VIEW = "adaptive-data-view"
    const val ID_NAVIGATION_SCAFFOLD = "adaptive-navigation-scaffold"
    const val ID_NAVIGATION_TREE = "adaptive-navigation-tree"
    const val ID_BREADCRUMBS = "adaptive-breadcrumbs"
    const val ID_TABS = "adaptive-tabs"
    const val ID_CAROUSEL = "adaptive-carousel"
    const val ID_ACCORDION_DIALOG = "adaptive-accordion-dialog"
    const val ID_TEXT_FIELD = "adaptive-text-field"
    const val ID_FEEDBACK_STATES = "feedback-states"

    // Topic IDs
    const val TOPIC_GETTING_STARTED = "getting-started"
    const val TOPIC_THEME = "theme"
    const val TOPIC_RESPONSIVE_NAV = "responsive-navigation-behavior"
    const val TOPIC_LAYOUT_SYSTEM = "layout-system"
    const val TOPIC_PUBLISHING = "publishing"
    const val TOPIC_VISUAL_VERIFICATION = "visual-verification"
    const val TOPIC_ROADMAP = "roadmap"

    // Component Registration
    private var cachedComponents: List<ComponentDoc>? = null
    private var cachedTopics: List<DocsTopic>? = null

    val allComponentIds = setOf(
        ID_THEME, ID_BUTTON, ID_ICON_BUTTON, ID_BADGE, ID_CHIP, ID_AVATAR, ID_THUMBNAIL,
        ID_CARD_SURFACE, ID_SELECTION_AREA, ID_SEARCH_FIELD, ID_SELECT, ID_MULTI_SELECT,
        ID_FORM_LAYOUT, ID_DATA_VIEW, ID_NAVIGATION_SCAFFOLD, ID_NAVIGATION_TREE,
        ID_BREADCRUMBS, ID_TABS, ID_CAROUSEL, ID_ACCORDION_DIALOG, ID_TEXT_FIELD,
        ID_FEEDBACK_STATES
    )

    val allTopicIds = setOf(
        TOPIC_GETTING_STARTED, TOPIC_THEME, TOPIC_RESPONSIVE_NAV, TOPIC_LAYOUT_SYSTEM,
        TOPIC_PUBLISHING, TOPIC_VISUAL_VERIFICATION, TOPIC_ROADMAP
    )

    fun getComponents(factory: () -> List<ComponentDoc>): List<ComponentDoc> {
        return cachedComponents ?: factory().also { docs ->
            validateIds(docs.map { it.id }, "Components")
            cachedComponents = docs
        }
    }

    fun getTopics(factory: () -> List<DocsTopic>): List<DocsTopic> {
        return cachedTopics ?: factory().also { topics ->
            validateIds(topics.map { it.id }, "Topics")
            cachedTopics = topics
        }
    }

    private fun validateIds(ids: List<String>, context: String) {
        val duplicates = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
        if (duplicates.isNotEmpty()) {
            error("Duplicate IDs found in $context: ${duplicates.keys}")
        }
    }

    fun resolveComponentId(hash: String): String {
        // Fallback a componente base si la sub-secciÃ³n es invÃ¡lida (ej. "adaptive-button-icon" -> "adaptive-button")
        // O si no existe, devuelve el primer componente.
        if (hash in allComponentIds) return hash

        // Simple fallback matcher
        val baseId = allComponentIds.find { hash.startsWith("$it-") }
        if (baseId != null) return baseId

        return ID_THEME // Default
    }

    fun resolveTopicId(hash: String): String {
        if (hash in allTopicIds) return hash
        return TOPIC_GETTING_STARTED // Default
    }
}
