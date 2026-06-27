package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
internal fun DocsCodeEditorView(code: String, modifier: Modifier = Modifier, scrollState: ScrollState = rememberScrollState()) {
    val tokens = remember(code) { KotlinLexer.tokenize(code) }

    // We will build one AnnotatedString for all the lines but we need to split it by lines to render line numbers.
    // However, splitting by lines after tokenizing is complex if tokens span multiple lines (like multi-line strings or comments).
    // Let's render the lines and the code side-by-side using two Text blocks inside a Row.

    val lineCount = remember(code) { code.lines().size }

    // Determine colors based on dark mode. AdaptiveTheme doesn't expose an explicit isDark flag easily,
    // but AdaptiveTheme.colors.background being dark or light can be used, or we just rely on AdaptiveTheme colors.
    val isDark = AdaptiveTheme.colors.surface.luminance() < 0.5f

    // Colors inspired by IDEs (like IntelliJ or VS Code)
    val keywordColor = if (isDark) Color(0xFFCC7832) else Color(0xFF0033B3)
    val stringColor = if (isDark) Color(0xFF6A8759) else Color(0xFF067D17)
    val numberColor = if (isDark) Color(0xFF6897BB) else Color(0xFF1750EB)
    val annotationColor = if (isDark) Color(0xFFBBB529) else Color(0xFF9E880D)
    val commentColor = if (isDark) Color(0xFF808080) else Color(0xFF8C8C8C)
    val functionColor = if (isDark) Color(0xFFFFC66D) else Color(0xFF00627A)
    val typeColor = if (isDark) Color(0xFFA9B7C6) else Color(0xFF000000)
    val identifierColor = if (isDark) Color(0xFFA9B7C6) else Color(0xFF000000)
    val symbolColor = if (isDark) Color(0xFFA9B7C6) else Color(0xFF000000)

    val annotatedCode = remember(tokens, isDark) {
        buildAnnotatedString {
            for (token in tokens) {
                val color = when (token.type) {
                    TokenType.KEYWORD -> keywordColor
                    TokenType.STRING -> stringColor
                    TokenType.NUMBER -> numberColor
                    TokenType.ANNOTATION -> annotationColor
                    TokenType.COMMENT -> commentColor
                    TokenType.FUNCTION -> functionColor
                    TokenType.TYPE -> typeColor
                    TokenType.IDENTIFIER -> identifierColor
                    TokenType.SYMBOL -> symbolColor
                    TokenType.WHITESPACE -> Color.Unspecified
                }

                if (color != Color.Unspecified) {
                    withStyle(SpanStyle(color = color)) {
                        append(token.text)
                    }
                } else {
                    append(token.text)
                }
            }
        }
    }

    Row(modifier = modifier.fillMaxWidth().background(AdaptiveTheme.colors.surfaceMuted).padding(12.dp)) {
        // Line numbers
        val lineNumbersText = (1..lineCount).joinToString("\n")
        BasicText(
            text = lineNumbersText,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = AdaptiveTheme.colors.textMuted.copy(alpha = 0.5f),
                fontFamily = FontFamily.Monospace,
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            ),
            modifier = Modifier.width(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Code
        Box(modifier = Modifier.weight(1f).horizontalScroll(scrollState)) {
            io.github.adaptivekt.components.AdaptiveSelectionArea {
                BasicText(
                    text = annotatedCode,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = identifierColor, // default fallback
                        fontFamily = FontFamily.Monospace,
                    ),
                    softWrap = false,
                )
            }
        }
    }
}

// Luminance helper
private fun Color.luminance(): Float {
    return (0.299f * red + 0.587f * green + 0.114f * blue)
}
