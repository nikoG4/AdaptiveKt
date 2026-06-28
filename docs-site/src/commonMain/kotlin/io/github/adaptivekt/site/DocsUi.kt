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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import io.github.adaptivekt.site.LocalSiteLocation
import io.github.adaptivekt.site.serializeSiteLocation
import io.github.adaptivekt.site.normalizeSectionId
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

internal val LocalDocsVisualState = staticCompositionLocalOf { "" }

@Composable
internal fun DocsShell(
    eyebrow: String,
    title: String,
    description: String,
    navGroups: List<DocsNavGroup>,
    selectedId: String,
    onSelectedIdChange: (String) -> Unit,
    sectionId: String? = null,
    onThisPage: List<String>?,
    onTocItemClick: ((String) -> Unit)? = null,
    scrollState: androidx.compose.foundation.ScrollState = androidx.compose.foundation.rememberScrollState(),
    content: @Composable () -> Unit,
) {
    val registry = androidx.compose.runtime.remember { DocsSectionRegistry() }

    // Automatically scroll to section when it's available or selectedId/sectionId changes
    androidx.compose.runtime.LaunchedEffect(selectedId, sectionId) {
        if (sectionId != null) {
            registry.scrollToSection(sectionId)
        } else {
            // Scroll to top when changing page without a section
            scrollState.animateScrollTo(0)
        }
    }

    val activeSection = registry.getActiveSection(scrollState.value)

    androidx.compose.runtime.CompositionLocalProvider(LocalDocsSectionRegistry provides registry) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compact = maxWidth < 900.dp
            val showRightToc = maxWidth >= 1150.dp

        if (compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                DocsHeroHeader(eyebrow = eyebrow, title = title, description = description, compact = true)
                Spacer(modifier = Modifier.height(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    DocsCompactNav(navGroups = navGroups, selectedId = selectedId, onSelectedIdChange = onSelectedIdChange)
                    if (!onThisPage.isNullOrEmpty()) {
                        DocsOnThisPage(
                            items = onThisPage,
                            activeItem = activeSection,
                            compact = true,
                            onItemClick = { onTocItemClick?.invoke(normalizeSectionId(it)) }
                        )
                    }
                    content()
                }
                Spacer(modifier = Modifier.height(48.dp))
                SiteFooter()
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                DocsHeroHeader(eyebrow = eyebrow, title = title, description = description, compact = false)
                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(22.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    DocsSidebar(
                        navGroups = navGroups,
                        selectedId = selectedId,
                        onSelectedIdChange = onSelectedIdChange,
                        modifier = Modifier
                            .width(238.dp)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(scrollState),
                    ) {
                        content()
                        Spacer(modifier = Modifier.height(48.dp))
                        SiteFooter()
                    }
                    if (!onThisPage.isNullOrEmpty() && showRightToc) {
                        DocsOnThisPage(
                            items = onThisPage,
                            activeItem = activeSection,
                            compact = false,
                            onItemClick = { onTocItemClick?.invoke(normalizeSectionId(it)) },
                            modifier = Modifier
                                .width(210.dp)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                                .padding(start = 8.dp),
                        )
                    }
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
        
        var copied by remember { mutableStateOf(false) }
        LaunchedEffect(copied) {
            if (copied) {
                kotlinx.coroutines.delay(2000)
                copied = false
            }
        }
        val location = LocalSiteLocation.current
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = title,
                modifier = Modifier.semantics { heading() },
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
            Spacer(modifier = Modifier.width(8.dp))
            AdaptiveIconButton(
                onClick = {
                    val permalink = buildAbsoluteSiteUrl(location.copy(sectionId = null, searchQuery = null))
                    requestCopyToClipboard(permalink) {
                        copied = true
                    }
                },
                modifier = Modifier.docsClickableCursor().alpha(if (copied) 1f else 0.2f),
                size = 28.dp,
                content = {
                    androidx.compose.foundation.Image(
                        imageVector = if (copied) DocsIcons.Check else DocsIcons.Copy,
                        contentDescription = "Copy page link",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
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
    AdaptiveSurface(modifier = modifier, contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SiteText("Documentation", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            navGroups.forEach { group ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SiteText(group.title, fontWeight = FontWeight.Bold, color = SiteMuted, fontSize = 12.sp)
                    group.items.forEach { item ->
                        DocsNavButton(
                            label = item.label,
                            selected = item.id == selectedId,
                            onClick = { onSelectedIdChange(item.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DocsCompactNav(
    navGroups: List<DocsNavGroup>,
    selectedId: String,
    onSelectedIdChange: (String) -> Unit,
) {
    AdaptiveCard {
        SiteText("Browse documentation", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navGroups.flatMap { it.items }.forEach { item ->
                AdaptiveChip(
                    text = item.label,
                    selected = item.id == selectedId,
                    tone = if (item.id == selectedId) AdaptiveChipTone.Primary else AdaptiveChipTone.Neutral,
                    onClick = { onSelectedIdChange(item.id) },
                    modifier = Modifier.docsClickableCursor()
                )
            }
        }
    }
}

@Composable
private fun DocsNavButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = AdaptiveTheme.shapes.medium
    val background = if (selected) AdaptiveTheme.colors.primarySubtle else Color.Transparent
    val border = if (selected) AdaptiveTheme.colors.primary else Color.Transparent
    BasicText(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background, shape)
            .border(1.dp, border, shape)
            .docsClickableCursor()
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        style = TextStyle(
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) AdaptiveTheme.colors.primaryText else AdaptiveTheme.colors.textPrimary,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
internal fun DocsOnThisPage(
    items: List<String>,
    activeItem: String?,
    compact: Boolean,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AdaptiveSurface(modifier = modifier.fillMaxWidth(), contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SiteText("On this page", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (compact) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items.forEach { item ->
                        val isActive = normalizeSectionId(item) == activeItem
                        AdaptiveBadge(
                            text = item,
                            tone = if (isActive) AdaptiveBadgeTone.Info else AdaptiveBadgeTone.Neutral,
                            modifier = Modifier.clip(AdaptiveTheme.shapes.small).docsClickableCursor().clickable { onItemClick(item) },
                        )
                    }
                }
            } else {
                items.forEach { item ->
                    val isActive = normalizeSectionId(item) == activeItem
                    val color = if (isActive) AdaptiveTheme.colors.primary else SiteMuted
                    val dotColor = if (isActive) AdaptiveTheme.colors.primary else Color.Transparent
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(AdaptiveTheme.shapes.small)
                            .docsClickableCursor()
                            .clickable { onItemClick(item) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(6.dp)
                                .background(dotColor, AdaptiveTheme.shapes.pill),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SiteText(item, color = color, maxLines = 2, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@Composable
internal fun DocsSection(
    title: String,
    description: String? = null,
    content: @Composable () -> Unit,
) {
    DocsSectionAnchor(id = normalizeSectionId(title), modifier = Modifier.fillMaxWidth()) {
        var copied by remember { mutableStateOf(false) }
        LaunchedEffect(copied) {
            if (copied) {
                kotlinx.coroutines.delay(2000)
                copied = false
            }
        }
        val location = LocalSiteLocation.current
        val sectionId = normalizeSectionId(title)
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SiteText(title, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, maxLines = 3)
                Spacer(modifier = Modifier.width(8.dp))
                AdaptiveIconButton(
                    onClick = {
                        val permalink = buildAbsoluteSiteUrl(location.copy(sectionId = sectionId, searchQuery = null))
                        requestCopyToClipboard(permalink) {
                            copied = true
                        }
                    },
                    modifier = Modifier.docsClickableCursor().alpha(if (copied) 1f else 0.2f),
                    size = 28.dp,
                    content = {
                        androidx.compose.foundation.Image(
                            imageVector = if (copied) DocsIcons.Check else DocsIcons.Copy,
                            contentDescription = "Copy section link",
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                SiteText(description, color = SiteMuted, fontSize = 15.sp, maxLines = 6)
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
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
        DocsCodeEditorView(code = example.code, title = "Kotlin")
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
        DocsSectionAnchor(id = "overview", modifier = Modifier.fillMaxWidth()) {
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

internal fun requestCopyToClipboard(text: String, onSuccess: () -> Unit) {
    PlatformInterop.copyToClipboard(text, onSuccess = onSuccess, onError = {
        // Fallback or ignore for now
        println("Clipboard error: $it")
    })
}











