@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package io.github.adaptivekt.site

import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.core.AdaptiveTheme

internal data class DocsSearchEntry(val route: SiteRoute, val id: String, val title: String, val family: String, val summary: String)

internal fun searchDocs(entries: List<DocsSearchEntry>, query: String): List<DocsSearchEntry> {
    val terms = query.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (terms.isEmpty()) return emptyList()
    return entries.filter { entry ->
        val text = "${entry.title} ${entry.family} ${entry.summary}".lowercase()
        terms.all { it in text }
    }.sortedByDescending { it.title.equals(query.trim(), ignoreCase = true) }
}

@Composable
internal fun DocsSearchDialog(onDismiss: () -> Unit, onNavigate: (SiteRoute, String) -> Unit) {
    val entries = remember {
        docsTopics().map { DocsSearchEntry(SiteRoute.Docs, it.id, it.title, it.family, it.summary) } +
            componentDocs().map { DocsSearchEntry(SiteRoute.Components, it.id, it.title, it.family, it.summary) }
    }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(0) }
    val results = remember(query) { searchDocs(entries, query) }
    val focus = remember { FocusRequester() }
    Dialog(onDismissRequest = onDismiss) {
        Column(Modifier.widthIn(max = 640.dp).fillMaxWidth()
            .background(AdaptiveTheme.colors.surface, AdaptiveTheme.shapes.large).padding(20.dp)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) false else when (event.key) {
                    Key.Escape -> { onDismiss(); true }
                    Key.DirectionDown -> { active = (active + 1).coerceAtMost((results.size - 1).coerceAtLeast(0)); true }
                    Key.DirectionUp -> { active = (active - 1).coerceAtLeast(0); true }
                    Key.Enter -> { results.getOrNull(active)?.let { onNavigate(it.route, it.id) }; true }
                    else -> false
                }
            }) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SiteText("Search documentation", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f), maxLines = 2)
                AdaptiveButton("Close", onClick = onDismiss, variant = AdaptiveButtonVariant.Ghost)
            }
            Spacer(Modifier.height(12.dp))
            BasicTextField(
                value = query,
                onValueChange = { query = it; active = 0 },
                singleLine = true,
                textStyle = TextStyle(color = AdaptiveTheme.colors.textPrimary, fontSize = 16.sp),
                cursorBrush = SolidColor(AdaptiveTheme.colors.primary),
                modifier = Modifier.fillMaxWidth().focusRequester(focus)
                    .background(AdaptiveTheme.colors.surfaceRaised, AdaptiveTheme.shapes.medium).padding(16.dp),
                decorationBox = { input ->
                    Box {
                        if (query.isEmpty()) SiteText("Search guides and components…", color = SiteMuted)
                        input()
                    }
                },
            )
            LaunchedEffect(Unit) { focus.requestFocus() }
            Spacer(Modifier.height(12.dp))
            if (query.isBlank()) SiteText("Type a name, topic or keyword.", color = SiteMuted)
            else if (results.isEmpty()) SiteText("No results. Try another keyword.", color = SiteMuted)
            else {
                SiteText("${results.size} results", color = SiteMuted)
                Column(Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState())) {
                    results.forEachIndexed { index, entry ->
                        SearchResult(entry, index == active) { onNavigate(entry.route, entry.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResult(entry: DocsSearchEntry, active: Boolean, onClick: () -> Unit) {
    val requester = remember { androidx.compose.foundation.relocation.BringIntoViewRequester() }
    LaunchedEffect(active) { if (active) requester.bringIntoView() }
    Column(Modifier.fillMaxWidth()
        .bringIntoViewRequester(requester)
        .background(if (active) AdaptiveTheme.colors.primarySubtle else AdaptiveTheme.colors.surface)
        .docsClickableCursor().clickable(onClick = onClick).padding(12.dp)) {
        SiteText(entry.title, fontWeight = FontWeight.Bold, maxLines = 2)
        SiteText("${entry.route.label} / ${entry.family}", color = SiteMuted, maxLines = 2)
        SiteText(entry.summary, color = SiteMuted, maxLines = 2)
    }
}
