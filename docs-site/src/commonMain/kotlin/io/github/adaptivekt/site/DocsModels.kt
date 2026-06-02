package io.github.adaptivekt.site

import androidx.compose.runtime.Composable

internal data class DocsNavItem(
    val id: String,
    val label: String,
    val description: String,
)

internal data class DocsNavGroup(
    val title: String,
    val items: List<DocsNavItem>,
)

internal data class ComponentParameter(
    val name: String,
    val type: String,
    val defaultValue: String,
    val required: Boolean,
    val description: String,
)

internal data class DocsExample(
    val title: String,
    val description: String,
    val code: String,
    val preview: @Composable () -> Unit,
)

internal data class ComponentDoc(
    val id: String,
    val family: String,
    val title: String,
    val summary: String,
    val usage: String,
    val basicExample: DocsExample,
    val parameters: List<ComponentParameter>,
    val variants: List<DocsExample> = emptyList(),
    val themingNotes: List<String> = emptyList(),
    val responsiveNotes: List<String> = emptyList(),
    val accessibilityNotes: List<String> = emptyList(),
    val limitations: List<String> = emptyList(),
) {
    val tocItems: List<String> get() = buildList {
        add("Overview")
        add("Basic usage")
        if (parameters.isNotEmpty()) add("Parameters")
        if (variants.isNotEmpty()) add("Examples and variants")
        add("Behavior notes")
    }
}

internal data class DocsTopic(
    val id: String,
    val family: String,
    val title: String,
    val summary: String,
    val tocItems: List<String>? = null,
    val content: @Composable () -> Unit,
)
