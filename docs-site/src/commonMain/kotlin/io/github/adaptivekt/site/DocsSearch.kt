@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package io.github.adaptivekt.site

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDialog
import io.github.adaptivekt.components.AdaptiveSearchField

internal data class DocsSearchEntry(
    val route: SiteRoute,
    val id: String,
    val title: String,
    val family: String,
    val summary: String,
)

internal fun searchDocs(entries: List<DocsSearchEntry>, query: String): List<DocsSearchEntry> {
    val terms = query.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (terms.isEmpty()) return emptyList()
    return entries.filter { entry ->
        val text = "${entry.title} ${entry.family} ${entry.summary}".lowercase()
        terms.all { it in text }
    }.sortedByDescending { it.title.equals(query.trim(), ignoreCase = true) }
}

@Composable
internal fun DocsSearchDialog(
    onDismiss: () -> Unit,
    onNavigate: (SiteRoute, String) -> Unit,
) {
    val entries = remember {
        docsTopics().map { DocsSearchEntry(SiteRoute.Docs, it.id, it.title, it.family, it.summary) } +
            componentDocs().map { DocsSearchEntry(SiteRoute.Components, it.id, it.title, it.family, it.summary) }
    }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(0) }
    val results = remember(query) { searchDocs(entries, query) }

    AdaptiveDialog(
        onDismissRequest = onDismiss,
        title = "Search documentation",
        modifier = Modifier
            .widthIn(max = 640.dp)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) {
                    false
                } else {
                    when (event.key) {
                        Key.Escape -> {
                            onDismiss()
                            true
                        }
                        Key.DirectionDown -> {
                            active = (active + 1).coerceAtMost((results.size - 1).coerceAtLeast(0))
                            true
                        }
                        Key.DirectionUp -> {
                            active = (active - 1).coerceAtLeast(0)
                            true
                        }
                        Key.Enter -> {
                            results.getOrNull(active)?.let { onNavigate(it.route, it.id) }
                            true
                        }
                        else -> false
                    }
                }
            },
        dismissButton = {},
        confirmButton = {
            AdaptiveButton(
                text = "Close",
                onClick = onDismiss,
                variant = AdaptiveButtonVariant.Ghost,
            )
        },
    ) {
        AdaptiveSearchField(
            value = query,
            onValueChange = {
                query = it.replace('\n', ' ')
                active = 0
            },
            placeholder = "Search guides and components…",
            onClear = {
                query = ""
                active = 0
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))

        when {
            query.isBlank() -> SiteText("Type a name, topic or keyword.", color = SiteMuted)
            results.isEmpty() -> SiteText("No results. Try another keyword.", color = SiteMuted)
            else -> {
                SiteText("${results.size} results", color = SiteMuted)
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    results.forEachIndexed { index, entry ->
                        SearchResult(
                            entry = entry,
                            active = index == active,
                            onClick = { onNavigate(entry.route, entry.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResult(
    entry: DocsSearchEntry,
    active: Boolean,
    onClick: () -> Unit,
) {
    val requester = remember { androidx.compose.foundation.relocation.BringIntoViewRequester() }
    LaunchedEffect(active) {
        if (active) requester.bringIntoView()
    }

    AdaptiveCard(
        modifier = Modifier
            .fillMaxWidth()
            .bringIntoViewRequester(requester),
        contentPadding = PaddingValues(12.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SiteText(
                text = entry.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = Modifier.weight(1f),
            )
            if (active) {
                Spacer(Modifier.width(8.dp))
                SiteText("Selected", color = SiteMuted, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(4.dp))
        SiteText("${entry.route.label} / ${entry.family}", color = SiteMuted, maxLines = 2)
        Spacer(Modifier.height(4.dp))
        SiteText(entry.summary, color = SiteMuted, maxLines = 2)
    }
}
