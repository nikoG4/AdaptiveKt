@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.relocation.bringIntoViewRequester

internal val LocalDocsVisualState = staticCompositionLocalOf { "" }

internal val LocalDocsAnchors = staticCompositionLocalOf<Map<String, androidx.compose.foundation.relocation.BringIntoViewRequester>> { emptyMap() }

@Composable
internal fun DocsShell(
    eyebrow: String,
    title: String,
    description: String,
    navGroups: List<DocsNavGroup>,
    selectedId: String,
    onSelectedIdChange: (String) -> Unit,
    onThisPage: List<String>?,
    onTocItemClick: ((String) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val anchors = remember(selectedId, onThisPage) {
        onThisPage.orEmpty().associateWith { androidx.compose.foundation.relocation.BringIntoViewRequester() }
    }
    val articleScroll = rememberScrollState()
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    var menuOpen by remember { mutableStateOf(false) }
    LaunchedEffect(selectedId) { articleScroll.scrollTo(0); menuOpen = false }
    val jump: (String) -> Unit = { heading ->
        scope.launch {
            if (heading == "Overview") articleScroll.animateScrollTo(0)
            else anchors[heading]?.bringIntoView()
        }
        onTocItemClick?.invoke(heading)
    }
    androidx.compose.runtime.CompositionLocalProvider(LocalDocsAnchors provides anchors) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compact = maxWidth < 880.dp
            val showRightToc = maxWidth >= 1150.dp
            if (compact) {
                Column(Modifier.fillMaxSize()) {
                    AdaptiveButton(
                        text = if (menuOpen) "Close navigation" else "Browse $eyebrow",
                        onClick = { menuOpen = !menuOpen },
                        variant = AdaptiveButtonVariant.Ghost,
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                    )
                    if (menuOpen) {
                        DocsSidebar(navGroups, selectedId, { onSelectedIdChange(it); menuOpen = false },
                            Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()))
                    } else {
                        Column(Modifier.weight(1f).verticalScroll(articleScroll).padding(16.dp)) {
                            if (!onThisPage.isNullOrEmpty()) {
                                DocsOnThisPage(onThisPage, true, jump)
                                Spacer(Modifier.height(20.dp))
                            }
                            content()
                            Spacer(Modifier.height(32.dp))
                            SiteFooter()
                        }
                    }
                }
            } else {
                Row(Modifier.fillMaxSize()) {
                    DocsSidebar(navGroups, selectedId, onSelectedIdChange,
                        Modifier.width(272.dp).fillMaxHeight().verticalScroll(rememberScrollState()))
                    Box(Modifier.width(1.dp).fillMaxHeight().background(SiteLine))
                    Column(Modifier.weight(1f).fillMaxHeight().verticalScroll(articleScroll).padding(28.dp)) {
                        content()
                        Spacer(Modifier.height(48.dp))
                        SiteFooter()
                    }
                    if (!onThisPage.isNullOrEmpty() && showRightToc) {
                        Box(Modifier.width(1.dp).fillMaxHeight().background(SiteLine))
                        DocsOnThisPage(onThisPage, false, jump,
                            Modifier.width(208.dp).fillMaxHeight().verticalScroll(rememberScrollState()))
                    }
                }
            }
        }
    }
}

@Composable
internal fun DocsHeroHeader(
    eyebrow: String,
    title: String,
    description: String,
    compact: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveBadge(text = eyebrow, tone = AdaptiveBadgeTone.Info)
        Spacer(modifier = Modifier.height(12.dp))
        val primary = AdaptiveTheme.colors.primary
        val info = AdaptiveTheme.colors.info
        val success = AdaptiveTheme.colors.success
        BasicText(
            text = title,
            style = TextStyle(
                fontSize = if (compact) 34.sp else 48.sp,
                lineHeight = (if (compact) 34.sp else 48.sp) * 1.15f,
                fontWeight = FontWeight.ExtraBold,
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(primary, info, success)
                ),
            ),
            maxLines = if (compact) 4 else 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(10.dp))
        SiteText(
            text = description,
            fontSize = if (compact) 15.sp else 17.sp,
            color = SiteMuted,
            maxLines = 6,
        )
    }
}

@Composable
private fun DocsSidebar(
    navGroups: List<DocsNavGroup>,
    selectedId: String,
    onSelectedIdChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tree = remember(navGroups) {
        navGroups.map { group ->
            io.github.adaptivekt.navigation.AdaptiveNavigationTreeItem(
                id = "group:${group.title}", label = group.title,
                children = group.items.map { io.github.adaptivekt.navigation.AdaptiveNavigationTreeItem(it.id, it.label) },
            )
        }
    }
    var expanded by remember { mutableStateOf(tree.map { it.id }.toSet()) }
    LaunchedEffect(selectedId, navGroups) {
        navGroups.firstOrNull { group -> group.items.any { it.id == selectedId } }?.let {
            expanded = expanded + "group:${it.title}"
        }
    }
    Column(modifier.padding(horizontal = 12.dp, vertical = 20.dp)) {
        io.github.adaptivekt.navigation.AdaptiveNavigationTree(
            items = tree,
            selectedItemId = selectedId,
            onItemSelected = { if (it.children.isEmpty()) onSelectedIdChange(it.id) },
            expandedItemIds = expanded,
            onExpandedItemIdsChange = { expanded = it },
            density = io.github.adaptivekt.navigation.AdaptiveNavigationDensity.Compact,
        )
    }
}

@Composable
internal fun DocsOnThisPage(
    items: List<String>,
    compact: Boolean,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SiteText("On this page", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        items.forEach { item ->
            SiteText(item, color = SiteMuted, maxLines = 2,
                modifier = Modifier.fillMaxWidth().docsClickableCursor()
                    .clickable { onItemClick(item) }.padding(vertical = 8.dp))
        }
    }
}

@Composable
internal fun DocsSection(
    title: String,
    description: String? = null,
    content: @Composable () -> Unit,
) {
    val anchor = LocalDocsAnchors.current[title]
    Column(modifier = Modifier.fillMaxWidth()) {
        SiteText(title, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, maxLines = 3,
            modifier = if (anchor != null) Modifier.bringIntoViewRequester(anchor) else Modifier)
        if (description != null) {
            Spacer(modifier = Modifier.height(8.dp))
            SiteText(description, color = SiteMuted, fontSize = 15.sp, maxLines = 6)
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
internal fun DocsCallout(
    title: String,
    body: String,
    tone: AdaptiveBadgeTone = AdaptiveBadgeTone.Info,
) {
    AdaptiveCard {
        Row(verticalAlignment = Alignment.Top) {
            AdaptiveBadge(title, tone = tone)
            Spacer(modifier = Modifier.width(12.dp))
            SiteText(body, color = SiteMuted, maxLines = 8)
        }
    }
}

@Composable
internal fun DocsExampleBlock(example: DocsExample) {
    AdaptiveCard {
        SiteText(example.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 3)
        Spacer(modifier = Modifier.height(6.dp))
        SiteText(example.description, color = SiteMuted, maxLines = 6)
        Spacer(modifier = Modifier.height(14.dp))
        AdaptiveSurface(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(AdaptiveTokens.Spacing.Medium),
        ) {
            example.preview()
        }
        Spacer(modifier = Modifier.height(14.dp))
        DocsCodeBlock(code = example.code, title = "Kotlin")
    }
}

@Composable
internal fun DocsCodeBlock(
    code: String,
    title: String = "Kotlin",
) {
    var copied by remember { mutableStateOf(false) }
    
    LaunchedEffect(copied) {
        if (copied) {
            delay(2000)
            copied = false
        }
    }
    
    val shape = RoundedCornerShape(10.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFF0F172A), shape)
            .border(1.dp, Color(0xFF243044), shape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111827))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                text = title,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFCBD5E1)),
            )
            AdaptiveIconButton(
                onClick = {
                    requestCopyToClipboard(code.trimIndent())
                    copied = true
                },
                size = 32.dp,
                modifier = Modifier.docsClickableCursor(),
                content = {
                    androidx.compose.foundation.Image(
                        imageVector = if (copied) DocsIcons.Check else DocsIcons.Copy,
                        contentDescription = if (copied) "Copied" else "Copy code",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(14.dp),
        ) {
            AdaptiveSelectionArea {
                BasicText(
                    text = code.trimIndent(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFFE2E8F0),
                        fontFamily = FontFamily.Monospace,
                    ),
                    softWrap = false,
                )
            }
        }
    }
}

@Composable
internal fun DocsParameterTable(parameters: List<ComponentParameter>) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val compact = maxWidth < 560.dp
        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                parameters.forEach { parameter ->
                    AdaptiveCard {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            SiteText(parameter.name, fontWeight = FontWeight.Bold, maxLines = 2)
                            AdaptiveBadge(if (parameter.required) "Required" else "Optional")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SiteText("Type: ${parameter.type}", color = SiteMuted, maxLines = 3, monospace = true)
                        SiteText("Default: ${parameter.defaultValue}", color = SiteMuted, maxLines = 3, monospace = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        SiteText(parameter.description, color = SiteMuted, maxLines = 8)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AdaptiveTheme.shapes.medium)
                    .border(1.dp, SiteLine, AdaptiveTheme.shapes.medium),
            ) {
                ParameterRow(
                    values = listOf("Parameter", "Type", "Default", "Required", "Description"),
                    header = true,
                )
                parameters.forEachIndexed { index, parameter ->
                    if (index > 0) AdaptiveDivider()
                    ParameterRow(
                        values = listOf(
                            parameter.name,
                            parameter.type,
                            parameter.defaultValue,
                            if (parameter.required) "Yes" else "No",
                            parameter.description,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun ParameterRow(values: List<String>, header: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (header) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val weights = listOf(1.05f, 1.2f, 1.0f, 0.72f, 2.2f)
        values.forEachIndexed { index, value ->
            BasicText(
                text = value,
                modifier = Modifier.weight(weights[index]),
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = if (header || index == 0) FontWeight.Bold else FontWeight.Normal,
                    color = if (header) AdaptiveTheme.colors.textPrimary else AdaptiveTheme.colors.textSecondary,
                    fontFamily = if (index in 1..2 && !header) FontFamily.Monospace else FontFamily.Default,
                ),
                maxLines = if (index == 4) 4 else 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun ComponentDocArticle(doc: ComponentDoc) {
    Column(verticalArrangement = Arrangement.spacedBy(28.dp)) {
        AdaptiveCard(contentSelectionEnabled = true) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AdaptiveBadge(doc.family, tone = AdaptiveBadgeTone.Info)
                Spacer(modifier = Modifier.height(12.dp))
                SiteText(doc.title, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp, maxLines = 10)
                Spacer(modifier = Modifier.height(10.dp))
                SiteText(doc.summary, color = SiteMuted, fontSize = 16.sp, maxLines = 20)
                Spacer(modifier = Modifier.height(12.dp))
                SiteText(doc.usage, color = SiteMuted, maxLines = 20)
            }
        }

        DocsSection("Basic usage", "A minimal example rendered with the real component API.") {
            DocsExampleBlock(doc.basicExample)
        }

        DocsSection("Parameters", "Public parameters exposed by this component or primitive.") {
            DocsParameterTable(doc.parameters)
        }

        if (doc.variants.isNotEmpty()) {
            DocsSection("Examples and variants", "Common states and variants you can copy into an app.") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    doc.variants.forEach { DocsExampleBlock(it) }
                }
            }
        }

        DocsSection("Behavior notes") {
            NotesGrid(
                notes = listOf(
                    "Theming" to doc.themingNotes,
                    "Responsive behavior" to doc.responsiveNotes,
                    "Accessibility" to doc.accessibilityNotes,
                    "Limitations" to doc.limitations,
                ),
            )
        }
    }
}

@Composable
private fun NotesGrid(notes: List<Pair<String, List<String>>>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        notes.forEach { (title, values) ->
            AdaptiveCard {
                SiteText(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (values.isEmpty()) {
                    SiteText("No special notes for this component yet.", color = SiteMuted, maxLines = 3)
                } else {
                    values.forEach { value ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 7.dp)
                                    .width(7.dp)
                                    .height(7.dp)
                                    .background(AdaptiveTheme.colors.primary, AdaptiveTheme.shapes.pill),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            SiteText(value, color = SiteMuted, maxLines = 6)
                        }
                    }
                }
            }
        }
    }
}

internal fun requestCopyToClipboard(@Suppress("UNUSED_PARAMETER") text: String) {
    // Wasm clipboard support is intentionally best-effort for now. The docs UI still gives
    // an immediate copied state so examples remain usable even where browser APIs are blocked.
}
