package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
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
import androidx.compose.foundation.ScrollState
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.layout.AdaptiveGrid
import kotlinx.coroutines.delay

/**
 * Internal docs-site code viewer with an editor-like appearance.
 *
 * Not public library API. Theme-aware (legible in dark and light), with optional
 * header, metadata, line-number gutter, horizontal scroll, light Kotlin syntax
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
    val colors = rememberCodeViewerColors()
    val syntax = colors.syntax

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(colors.elevation, shape, clip = false, ambientColor = colors.shadow, spotColor = colors.shadow)
            .clip(shape)
            .background(colors.background, shape)
            .border(1.dp, colors.border, shape)
    ) {
        if (title != null || badge != null) {
            CodeViewerHeader(
                title = title,
                badge = badge,
                copyEnabled = copyEnabled,
                code = code,
                colors = colors,
                collapsible = isCollapsible,
                expanded = isExpanded,
                expandTag = expandTag,
                onExpandedChange = resolvedOnExpandedChange,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            if (showLineNumbers) {
                BasicText(
                    text = remember(displayedCode, isCollapsible, isExpanded, syntax) {
                        renderLineNumbers(
                            lineCount = displayedCode.lineCount(),
                            totalLineCount = if (isCollapsible && !isExpanded) lines.size else null,
                        )
                    },
                    modifier = Modifier
                        .background(colors.gutter)
                        .border(1.dp, colors.gutterBorder)
                        .padding(start = 12.dp, end = 10.dp, top = 15.dp, bottom = 15.dp),
                    style = codeTextStyle(colors.gutterText),
                    softWrap = false,
                )
            }
            AdaptiveSelectionArea {
                val annotated = remember(displayedCode, showLineNumbers, syntax) {
                    renderCode(
                        code = displayedCode,
                        colors = syntax,
                    )
                }
                BasicText(
                    text = annotated,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp),
                    style = codeTextStyle(colors.codeText),
                    softWrap = false,
                )
            }
        }

        if (isCollapsible && !isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .background(
                        Brush.verticalGradient(listOf(Color.Transparent, colors.background)),
                    ),
            )
        }
    }
}

@Composable
private fun CodeViewerHeader(
    title: String?,
    badge: String?,
    copyEnabled: Boolean,
    code: String,
    colors: CodeViewerColors,
    collapsible: Boolean,
    expanded: Boolean,
    expandTag: String?,
    onExpandedChange: (Boolean) -> Unit,
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
            .background(colors.header)
            .border(1.dp, colors.headerBorder)
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        val compact = maxWidth < 220.dp
        if (compact && title != null) {
            Column {
                SiteText(title, fontWeight = FontWeight.Bold, maxLines = 1)
                if (badge != null) {
                    Spacer(modifier = Modifier.height(3.dp))
                    CodeViewerMetadata(badge, colors)
                }
                if (collapsible) {
                    Spacer(modifier = Modifier.height(4.dp))
                    CodeViewerExpandControl(
                        expanded = expanded,
                        colors = colors,
                        expandTag = expandTag,
                        onExpandedChange = onExpandedChange,
                    )
                }
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
                        CodeViewerMetadata(badge, colors)
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
                    if (collapsible) {
                        CodeViewerExpandControl(
                            expanded = expanded,
                            colors = colors,
                            expandTag = expandTag,
                            onExpandedChange = onExpandedChange,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeViewerExpandControl(
    expanded: Boolean,
    colors: CodeViewerColors,
    expandTag: String?,
    onExpandedChange: (Boolean) -> Unit,
) {
    SiteText(
        text = if (expanded) "Collapse" else "Expand",
        color = colors.controlText,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = (if (expandTag != null) Modifier.semantics { testTag = expandTag } else Modifier)
            .clip(AdaptiveTheme.shapes.pill)
            .clickable { onExpandedChange(!expanded) }
            .docsClickableCursor()
            .padding(horizontal = 8.dp, vertical = 6.dp),
    )
}

@Composable
private fun CodeViewerMetadata(text: String, colors: CodeViewerColors) {
    SiteText(
        text = text.replace(" meaningful", ""),
        color = colors.metadata,
        fontSize = 12.sp,
        maxLines = 1,
    )
}

@Composable
private fun rememberCodeViewerColors(): CodeViewerColors {
    val theme = AdaptiveTheme.colors
    return remember(theme) {
        val dark = theme.background.luminance() < 0.3f
        val syntax = if (dark) {
            SyntaxColors(
                keyword = Color(0xFF93C5FD), annotation = Color(0xFFFDE68A), string = Color(0xFF86EFAC),
                number = Color(0xFFC4B5FD), type = Color(0xFF67E8F9), function = Color(0xFFBFDBFE),
                literal = Color(0xFFFDA4AF), comment = Color(0xFF7F8EA3), punctuation = Color(0xFF94A3B8),
            )
        } else {
            SyntaxColors(
                keyword = Color(0xFF4338CA), annotation = Color(0xFF9A3412), string = Color(0xFF047857),
                number = Color(0xFF7C3AED), type = Color(0xFF0369A1), function = Color(0xFF1D4ED8),
                literal = Color(0xFFBE123C), comment = Color(0xFF64748B), punctuation = Color(0xFF475569),
            )
        }
        CodeViewerColors(
            background = if (dark) Color(0xFF101827) else Color(0xFFF6F8FC),
            header = if (dark) Color(0xFF172235) else Color(0xFFEEF3FA),
            gutter = if (dark) Color(0xFF0C1422) else Color(0xFFEDF2F8),
            border = if (dark) Color(0xFF334155) else Color(0xFFD6E0EC),
            headerBorder = if (dark) Color(0xFF2B3B52) else Color(0xFFDDE6F0),
            gutterBorder = if (dark) Color(0xFF243247) else Color(0xFFDCE5EF),
            codeText = theme.textPrimary,
            gutterText = if (dark) Color(0xFF74839A) else Color(0xFF718096),
            metadata = if (dark) Color(0xFFAAB9CC) else Color(0xFF64748B),
            controlText = if (dark) Color(0xFFBFDBFE) else Color(0xFF315FDC),
            shadow = if (dark) Color(0x99000000) else Color(0x180F172A),
            elevation = if (dark) 10.dp else 6.dp,
            syntax = syntax,
        )
    }
}

private data class CodeViewerColors(
    val background: Color,
    val header: Color,
    val gutter: Color,
    val border: Color,
    val headerBorder: Color,
    val gutterBorder: Color,
    val codeText: Color,
    val gutterText: Color,
    val metadata: Color,
    val controlText: Color,
    val shadow: Color,
    val elevation: androidx.compose.ui.unit.Dp,
    val syntax: SyntaxColors,
)

private data class SyntaxColors(
    val keyword: Color,
    val string: Color,
    val comment: Color,
    val number: Color,
    val annotation: Color,
    val type: Color,
    val function: Color,
    val literal: Color,
    val punctuation: Color,
)

private fun codeTextStyle(color: Color) = TextStyle(
    fontSize = 12.sp,
    lineHeight = 18.sp,
    color = color,
    fontFamily = FontFamily.Monospace,
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
    colors: SyntaxColors,
): AnnotatedString = buildAnnotatedString {
    val lines = code.split("\n")
    lines.forEachIndexed { index, line ->
        appendHighlightedLine(line, colors)
        if (index < lines.lastIndex) append("\n")
    }
}

private fun renderLineNumbers(lineCount: Int, totalLineCount: Int?): AnnotatedString = buildAnnotatedString {
    val width = (totalLineCount ?: lineCount).toString().length
    repeat(lineCount) { index ->
        append((index + 1).toString().padStart(width, ' '))
        if (index < lineCount - 1) append("\n")
    }
}

private fun String.lineCount(): Int = if (isEmpty()) 1 else count { it == '\n' } + 1

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
            } else if (word in KOTLIN_LITERALS) {
                withStyle(SpanStyle(color = colors.literal)) { append(word) }
            } else if (word.first().isUpperCase()) {
                withStyle(SpanStyle(color = colors.type)) { append(word) }
            } else if (line.drop(end).trimStart().startsWith("(")) {
                withStyle(SpanStyle(color = colors.function)) { append(word) }
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
        if (c in PUNCTUATION) withStyle(SpanStyle(color = colors.punctuation)) { append(c) } else append(c)
        i++
    }
}

private val KOTLIN_LITERALS = setOf("true", "false", "null")
private const val PUNCTUATION = "(){}[],:.=+-*/<>?!"

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
