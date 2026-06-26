package io.github.adaptivekt.site

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

internal enum class TokenType {
    KEYWORD,
    STRING,
    NUMBER,
    ANNOTATION,
    COMMENT,
    FUNCTION,
    TYPE,
    IDENTIFIER,
    SYMBOL,
    WHITESPACE
}

internal data class Token(val text: String, val type: TokenType)

internal object KotlinLexer {
    private val keywords = setOf(
        "package", "import", "class", "interface", "fun", "val", "var",
        "if", "else", "when", "for", "while", "do", "return", "break", "continue",
        "throw", "try", "catch", "finally", "true", "false", "null", "is", "in", "!in", "!is",
        "as", "as?", "object", "typealias", "enum", "data", "sealed", "open", "abstract",
        "override", "private", "protected", "internal", "public", "companion", "inline",
        "noinline", "crossinline", "reified", "out", "in", "vararg", "suspend", "operator",
        "infix", "tailrec", "external", "annotation", "expect", "actual", "lateinit", "const"
    )

    private val types = setOf(
        "String", "Int", "Boolean", "Float", "Double", "Long", "Short", "Byte", "Char",
        "Unit", "Any", "Nothing", "List", "Set", "Map", "Modifier", "Color", "Dp",
        "AdaptiveTheme", "AdaptiveCard", "AdaptiveButton", "AdaptiveBadge",
        "AdaptiveTextField", "AdaptiveSelect", "AdaptiveDataView", "AdaptiveAvatar",
        "AdaptiveSurface"
    )

    fun tokenize(code: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        val length = code.length

        while (i < length) {
            val c = code[i]
            
            // Whitespace
            if (c.isWhitespace()) {
                val start = i
                while (i < length && code[i].isWhitespace()) i++
                tokens.add(Token(code.substring(start, i), TokenType.WHITESPACE))
                continue
            }

            // Single line comment
            if (c == '/' && i + 1 < length && code[i + 1] == '/') {
                val start = i
                while (i < length && code[i] != '\n') i++
                tokens.add(Token(code.substring(start, i), TokenType.COMMENT))
                continue
            }

            // Multi line comment
            if (c == '/' && i + 1 < length && code[i + 1] == '*') {
                val start = i
                i += 2
                while (i < length) {
                    if (code[i] == '*' && i + 1 < length && code[i + 1] == '/') {
                        i += 2
                        break
                    }
                    i++
                }
                tokens.add(Token(code.substring(start, i), TokenType.COMMENT))
                continue
            }

            // String literals
            if (c == '"') {
                val start = i
                if (i + 2 < length && code[i + 1] == '"' && code[i + 2] == '"') {
                    // Triple quote string
                    i += 3
                    while (i < length) {
                        if (code[i] == '"' && i + 2 < length && code[i + 1] == '"' && code[i + 2] == '"') {
                            i += 3
                            break
                        }
                        i++
                    }
                } else {
                    // Single quote string
                    i++
                    while (i < length && code[i] != '"') {
                        if (code[i] == '\\') i++ // skip escaped char
                        i++
                    }
                    if (i < length) i++
                }
                tokens.add(Token(code.substring(start, i), TokenType.STRING))
                continue
            }

            // Annotation
            if (c == '@') {
                val start = i
                i++
                while (i < length && (code[i].isLetterOrDigit() || code[i] == '_')) i++
                tokens.add(Token(code.substring(start, i), TokenType.ANNOTATION))
                continue
            }

            // Number
            if (c.isDigit()) {
                val start = i
                while (i < length && (code[i].isLetterOrDigit() || code[i] == '.')) i++
                tokens.add(Token(code.substring(start, i), TokenType.NUMBER))
                continue
            }

            // Identifier / Keyword / Type / Function
            if (c.isLetter() || c == '_') {
                val start = i
                while (i < length && (code[i].isLetterOrDigit() || code[i] == '_')) i++
                val text = code.substring(start, i)
                
                // Peek ahead to see if it's a function call
                var isFunction = false
                var peek = i
                while (peek < length && code[peek].isWhitespace()) peek++
                if (peek < length && (code[peek] == '(' || code[peek] == '{')) {
                    isFunction = true
                }
                
                val type = when {
                    text in keywords -> TokenType.KEYWORD
                    text in types -> TokenType.TYPE
                    text.first().isUpperCase() -> TokenType.TYPE
                    isFunction -> TokenType.FUNCTION
                    else -> TokenType.IDENTIFIER
                }
                
                tokens.add(Token(text, type))
                continue
            }

            // Symbol
            val start = i
            i++
            tokens.add(Token(code.substring(start, i), TokenType.SYMBOL))
        }

        return tokens
    }
}
