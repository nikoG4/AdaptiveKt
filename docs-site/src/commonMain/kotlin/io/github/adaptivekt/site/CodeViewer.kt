package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import androidx.compose.foundation.ScrollState
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.layout.AdaptiveGrid
import kotlinx.coroutines.delay

/**
 * Internal docs-site code viewer with an editor-like appearance.
 *
 * Not public library API. Theme-aware (legible in dark and light), with optional
 * header, badge, line-number gutter, horizontal scroll, light Kotlin syntax
 * highlighting (no external dependency), optional copy button and optional
 * collapse/expand.
 */
@Composable
internal fun CodeViewer(
    code: String,
    title: String? = null,
    badge: String? = null,
    showLineNumbers: Boolean = false,
    copyEnabled: Boolean = false,
    collapsedMaxLines: Int? = null,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    expandTag: String? = null,
    scrollState: ScrollState = rememberScrollState(),
    modifier: Modifier = Modifier,
) {
    val lines = code.lines()
    val maxCollapsed = collapsedMaxLines ?: 0
    val isCollapsible = collapsedMaxLines != null && lines.size > maxCollapsed
    var internalExpanded by remember(code) { mutableStateOf(false) }
    val isExpanded = if (expanded != null) expanded else internalExpanded
    val resolvedOnExpandedChange: (Boolean) -> Unit = onExpandedChange ?: { internalExpanded = it }

    val displayedCode = if (isCollapsible && !isExpanded) {
        lines.take(maxCollapsed).joinToString("\n")
    } else {
        code
    }

    val shape = AdaptiveTheme.shapes.medium
    val editorBg = AdaptiveTheme.colors.surface
    val editorHeaderBg = AdaptiveTheme.colors.surfaceMuted
    val syntax = rememberSyntaxColors()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(editorBg, shape)
            .border(1.dp, SiteLine, shape)
    ) {
        if (title != null || badge != null) {
            CodeViewerHeader(
                title = title,
                badge = badge,
                copyEnabled = copyEnabled,
                code = code,
                headerBg = editorHeaderBg,
            )
            AdaptiveDivider()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(14.dp)
        ) {
            AdaptiveSelectionArea {
                val annotated = remember(displayedCode, showLineNumbers, syntax) {
                    renderCode(
                        code = displayedCode,
                        showLineNumbers = showLineNumbers,
                        totalLineCount = if (isCollapsible && !isExpanded) lines.size else null,
                        colors = syntax,
                    )
                }
                BasicText(
                    text = annotated,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontFamily = FontFamily.Monospace,
                    ),
                    softWrap = false,
                )
            }
        }

        if (isCollapsible && !isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(editorHeaderBg.copy(alpha = 0.5f))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                SiteText(
                    text = "// ... ${lines.size - maxCollapsed} more lines",
                    color = AdaptiveTheme.colors.textMuted,
                    fontSize = 12.sp,
                    maxLines = 1,
                )
            }
        }

        if (isCollapsible) {
            AdaptiveDivider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AdaptiveButton(
                    modifier = if (expandTag != null) Modifier.semantics { testTag = expandTag } else Modifier,
                    text = if (isExpanded) "Collapse" else "Show full code",
                    variant = AdaptiveButtonVariant.Ghost,
                    onClick = { resolvedOnExpandedChange(!isExpanded) }
                )
            }
        }
    }
}

@Composable
private fun CodeViewerHeader(
    title: String?,
    badge: String?,
    copyEnabled: Boolean,
    code: String,
    headerBg: Color,
) {
    var copied by remember { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(2000)
            copied = false
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBg)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        val compact = maxWidth < 220.dp
        if (compact && title != null && badge != null) {
            Column {
                SiteText(title, fontWeight = FontWeight.Bold, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                AdaptiveBadge(badge, tone = AdaptiveBadgeTone.Info)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (title != null) {
                    SiteText(title, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (badge != null) {
                        AdaptiveBadge(badge, tone = AdaptiveBadgeTone.Info)
                    }
                    if (copyEnabled) {
                        AdaptiveIconButton(
                            onClick = {
                                requestCopyToClipboard(code.trimIndent())
                                copied = true
                            },
                            size = 30.dp,
                            modifier = Modifier.docsClickableCursor(),
                            content = {
                                androidx.compose.foundation.Image(
                                    imageVector = if (copied) DocsIcons.Check else DocsIcons.Copy,
                                    contentDescription = if (copied) "Copied" else "Copy code",
                                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                                    modifier = Modifier.width(15.dp).height(15.dp),
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberSyntaxColors(): SyntaxColors {
    val theme = AdaptiveTheme.colors
    return remember(theme) {
        SyntaxColors(
            keyword = theme.primary,
            string = theme.success,
            comment = theme.textMuted,
            number = theme.info,
            annotation = theme.warning,
            gutter = theme.textMuted,
        )
    }
}

private data class SyntaxColors(
    val keyword: Color,
    val string: Color,
    val comment: Color,
    val number: Color,
    val annotation: Color,
    val gutter: Color,
)

private val KOTLIN_KEYWORDS = setOf(
    "fun", "val", "var", "class", "object", "interface", "enum", "sealed", "data", "abstract",
    "open", "final", "override", "private", "public", "protected", "internal", "companion",
    "import", "package", "return", "if", "else", "when", "for", "while", "do", "break",
    "continue", "is", "as", "out", "vararg", "noinline", "crossinline",
    "inline", "reified", "suspend", "operator", "infix", "tailrec", "lateinit", "const",
    "init", "this", "super", "null", "true", "false", "where", "by",
)

private fun renderCode(
    code: String,
    showLineNumbers: Boolean,
    totalLineCount: Int?,
    colors: SyntaxColors,
): AnnotatedString = buildAnnotatedString {
    val lines = code.split("\n")
    val gutterWidth = if (showLineNumbers) {
        val total = totalLineCount ?: lines.size
        total.toString().length
    } else 0
    lines.forEachIndexed { index, line ->
        if (showLineNumbers) {
            val lineNumber = (index + 1).toString().padStart(gutterWidth, ' ')
            withStyle(SpanStyle(color = colors.gutter)) { append("$lineNumber ") }
        }
        appendHighlightedLine(line, colors)
        if (index < lines.lastIndex) append("\n")
    }
}

private fun AnnotatedString.Builder.appendHighlightedLine(line: String, colors: SyntaxColors) {
    var i = 0
    val n = line.length
    while (i < n) {
        val c = line[i]
        val next = if (i + 1 < n) line[i + 1] else '\u0000'

        if (c == '/' && next == '/') {
            withStyle(SpanStyle(color = colors.comment)) { append(line.substring(i)) }
            return
        }
        if (c == '/' && next == '*') {
            val end = line.indexOf("*/", i + 2)
            val endIdx = if (end >= 0) end + 2 else n
            withStyle(SpanStyle(color = colors.comment)) { append(line.substring(i, endIdx)) }
            i = endIdx
            continue
        }
        if (c == '"') {
            val end = findStringEnd(line, i)
            val endIdx = if (end >= 0) end + 1 else n
            withStyle(SpanStyle(color = colors.string)) { append(line.substring(i, endIdx)) }
            i = endIdx
            continue
        }
        if (c == '@' && i + 1 < n && line[i + 1].isLetter()) {
            val end = identifierEnd(line, i + 1)
            withStyle(SpanStyle(color = colors.annotation)) { append(line.substring(i, end)) }
            i = end
            continue
        }
        if (c.isLetter()) {
            val end = identifierEnd(line, i)
            val word = line.substring(i, end)
            if (word in KOTLIN_KEYWORDS) {
                withStyle(SpanStyle(color = colors.keyword, fontWeight = FontWeight.Bold)) { append(word) }
            } else {
                append(word)
            }
            i = end
            continue
        }
        if (c.isDigit()) {
            val end = numberEnd(line, i)
            withStyle(SpanStyle(color = colors.number)) { append(line.substring(i, end)) }
            i = end
            continue
        }
        append(c)
        i++
    }
}

private fun findStringEnd(line: String, start: Int): Int {
    var i = start + 1
    while (i < line.length) {
        when (line[i]) {
            '\\' -> i += 2
            '"' -> return i
            else -> i++
        }
    }
    return -1
}

private fun identifierEnd(line: String, start: Int): Int {
    var i = start
    while (i < line.length && (line[i].isLetterOrDigit() || line[i] == '_')) i++
    return i
}

private fun numberEnd(line: String, start: Int): Int {
    var i = start
    while (i < line.length && (line[i].isLetterOrDigit() || line[i] == '_' || line[i] == '.')) i++
    return i
}

@Composable
internal fun CodeComparisonViewer(
    beforeTitle: String,
    beforeCode: String,
    beforeBadge: String? = null,
    beforeExpanded: Boolean,
    onBeforeExpandedChange: (Boolean) -> Unit,
    afterTitle: String,
    afterCode: String,
    afterBadge: String? = null,
    afterExpanded: Boolean,
    onAfterExpandedChange: (Boolean) -> Unit,
    beforePanelTag: String? = null,
    afterPanelTag: String? = null,
    beforeExpandTag: String? = null,
    afterExpandTag: String? = null,
    beforeScrollState: ScrollState = rememberScrollState(),
    afterScrollState: ScrollState = rememberScrollState(),
    modifier: Modifier = Modifier,
) {
    val collapsedMaxLines = 28
    AdaptiveGrid(columns = 12, horizontalGap = 16.dp, verticalGap = 16.dp, modifier = modifier) {
        item(span = 6) {
            CodeViewer(
                code = beforeCode,
                title = beforeTitle,
                badge = beforeBadge,
                showLineNumbers = true,
                collapsedMaxLines = collapsedMaxLines,
                expanded = beforeExpanded,
                onExpandedChange = onBeforeExpandedChange,
                expandTag = beforeExpandTag,
                scrollState = beforeScrollState,
                modifier = if (beforePanelTag != null) Modifier.semantics { testTag = beforePanelTag } else Modifier,
            )
        }
        item(span = 6) {
            CodeViewer(
                code = afterCode,
                title = afterTitle,
                badge = afterBadge,
                showLineNumbers = true,
                collapsedMaxLines = collapsedMaxLines,
                expanded = afterExpanded,
                onExpandedChange = onAfterExpandedChange,
                expandTag = afterExpandTag,
                scrollState = afterScrollState,
                modifier = if (afterPanelTag != null) Modifier.semantics { testTag = afterPanelTag } else Modifier,
            )
        }
    }
}
