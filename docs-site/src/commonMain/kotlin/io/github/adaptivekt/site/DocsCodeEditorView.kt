package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.core.AdaptiveTheme
import kotlinx.coroutines.delay

internal sealed interface ClipboardState {
    data object Idle : ClipboardState
    data object Copying : ClipboardState
    data object Success : ClipboardState
    data class Failure(val message: String) : ClipboardState
}

internal data class KotlinSyntaxColorScheme(
    val keyword: Color,
    val modifier: Color,
    val annotation: Color,
    val string: Color,
    val character: Color,
    val number: Color,
    val comment: Color,
    val type: Color,
    val function: Color,
    val property: Color,
    val operator: Color,
    val punctuation: Color,
    val plain: Color,
    val gutter: Color,
    val gutterText: Color,
    val background: Color,
    val border: Color,
    val toolbarBackground: Color,
    val toolbarText: Color
)

private val LightColorScheme = KotlinSyntaxColorScheme(
    keyword = Color(0xFF0033B3),
    modifier = Color(0xFF0033B3),
    annotation = Color(0xFF9E880D),
    string = Color(0xFF067D17),
    character = Color(0xFF067D17),
    number = Color(0xFF1750EB),
    comment = Color(0xFF8C8C8C),
    type = Color(0xFF000000),
    function = Color(0xFF00627A),
    property = Color(0xFF871094),
    operator = Color(0xFF000000),
    punctuation = Color(0xFF000000),
    plain = Color(0xFF000000),
    gutter = Color(0xFFF2F2F2),
    gutterText = Color(0xFFA0A0A0),
    background = Color(0xFFFAFAFA),
    border = Color(0xFFE2E8F0),
    toolbarBackground = Color(0xFFF1F5F9),
    toolbarText = Color(0xFF475569)
)

private val DarkColorScheme = KotlinSyntaxColorScheme(
    keyword = Color(0xFFCC7832),
    modifier = Color(0xFFCC7832),
    annotation = Color(0xFFBBB529),
    string = Color(0xFF6A8759),
    character = Color(0xFF6A8759),
    number = Color(0xFF6897BB),
    comment = Color(0xFF808080),
    type = Color(0xFFA9B7C6),
    function = Color(0xFFFFC66D),
    property = Color(0xFF9876AA),
    operator = Color(0xFFA9B7C6),
    punctuation = Color(0xFFA9B7C6),
    plain = Color(0xFFA9B7C6),
    gutter = Color(0xFF2B2B2B),
    gutterText = Color(0xFF606366),
    background = Color(0xFF0F172A),
    border = Color(0xFF1E293B),
    toolbarBackground = Color(0xFF1E293B),
    toolbarText = Color(0xFF94A3B8)
)

@Composable
internal fun DocsCodeEditorView(
    code: String,
    modifier: Modifier = Modifier,
    title: String = "Kotlin",
    showToolbar: Boolean = true,
    scrollState: ScrollState = rememberScrollState()
) {
    val tokens = remember(code) { KotlinLexer.tokenize(code) }
    val lineCount = remember(code) { code.lines().size }
    
    val isDark = AdaptiveTheme.colors.surface.luminance() < 0.5f
    val scheme = if (isDark) DarkColorScheme else LightColorScheme
    
    val annotatedCode = remember(tokens, scheme) {
        buildAnnotatedString {
            for (token in tokens) {
                val color = when (token.type) {
                    TokenType.KEYWORD -> scheme.keyword
                    TokenType.STRING -> scheme.string
                    TokenType.NUMBER -> scheme.number
                    TokenType.ANNOTATION -> scheme.annotation
                    TokenType.COMMENT -> scheme.comment
                    TokenType.FUNCTION -> scheme.function
                    TokenType.TYPE -> scheme.type
                    TokenType.IDENTIFIER -> scheme.plain
                    TokenType.SYMBOL -> scheme.punctuation
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

    var clipboardState by remember { mutableStateOf<ClipboardState>(ClipboardState.Idle) }

    LaunchedEffect(clipboardState) {
        when (clipboardState) {
            is ClipboardState.Success, is ClipboardState.Failure -> {
                delay(2000)
                clipboardState = ClipboardState.Idle
            }
            else -> {}
        }
    }

    val shape = RoundedCornerShape(10.dp)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(scheme.background)
            .border(1.dp, scheme.border, shape)
    ) {
        if (showToolbar) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(scheme.toolbarBackground)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicText(
                        text = title,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = scheme.toolbarText),
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(scheme.border, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        BasicText(
                            text = "$lineCount lines",
                            style = TextStyle(fontSize = 10.sp, color = scheme.toolbarText)
                        )
                    }
                }
                
                AdaptiveIconButton(
                    onClick = {
                        if (clipboardState == ClipboardState.Idle || clipboardState is ClipboardState.Failure) {
                            clipboardState = ClipboardState.Copying
                            PlatformInterop.copyToClipboard(code.trimIndent(), onSuccess = {
                                clipboardState = ClipboardState.Success
                            }, onError = {
                                clipboardState = ClipboardState.Failure(it)
                            })
                        }
                    },
                    size = 32.dp,
                    modifier = Modifier.docsClickableCursor(),
                    content = {
                        val icon = when (clipboardState) {
                            is ClipboardState.Success -> DocsIcons.Check
                            is ClipboardState.Failure -> DocsIcons.Menu // fallback icon for failure
                            else -> DocsIcons.Copy
                        }
                        val tint = when (clipboardState) {
                            is ClipboardState.Success -> AdaptiveTheme.colors.success
                            is ClipboardState.Failure -> AdaptiveTheme.colors.danger
                            else -> scheme.toolbarText
                        }
                        
                        androidx.compose.foundation.Image(
                            imageVector = icon,
                            contentDescription = "Copy state",
                            colorFilter = ColorFilter.tint(tint),
                            modifier = Modifier.size(16.dp).alpha(if (clipboardState == ClipboardState.Copying) 0.5f else 1f)
                        )
                    }
                )
            }
        }
        
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            val digits = lineCount.toString().length
            val gutterWidth = (16 + (digits * 8)).dp
            
            Box(
                modifier = Modifier
                    .width(gutterWidth)
                    .padding(end = 8.dp)
                    .border(width = 1.dp, color = scheme.border.copy(alpha = 0.5f), shape = RoundedCornerShape(0.dp))
            ) {
                val lineNumbersText = (1..lineCount).joinToString("\n")
                BasicText(
                    text = lineNumbersText,
                    style = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = scheme.gutterText,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Box(modifier = Modifier.weight(1f).horizontalScroll(scrollState).focusProperties { canFocus = false }) {
                AdaptiveSelectionArea {
                    BasicText(
                        text = annotatedCode,
                        style = TextStyle(
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = scheme.plain,
                            fontFamily = FontFamily.Monospace,
                        ),
                        softWrap = false,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        }
    }
}

// Luminance helper
private fun Color.luminance(): Float {
    return (0.299f * red + 0.587f * green + 0.114f * blue)
}
