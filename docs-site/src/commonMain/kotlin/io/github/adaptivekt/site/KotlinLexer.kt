package io.github.adaptivekt.site

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
        "infix", "tailrec", "external", "annotation", "expect", "actual", "lateinit", "const",
        "value"
    )

    fun tokenize(code: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        val length = code.length

        fun peek(offset: Int = 0): Char? = if (i + offset < length) code[i + offset] else null
        
        fun isWhitespace(c: Char) = c.isWhitespace()
        
        fun isIdentifierStart(c: Char) = c.isLetter() || c == '_'
        fun isIdentifierPart(c: Char) = c.isLetterOrDigit() || c == '_'

        while (i < length) {
            val c = peek()!!

            // Whitespace
            if (isWhitespace(c)) {
                val start = i
                while (peek()?.let { isWhitespace(it) } == true) i++
                tokens.add(Token(code.substring(start, i), TokenType.WHITESPACE))
                continue
            }

            // Line comment
            if (c == '/' && peek(1) == '/') {
                val start = i
                while (peek() != null && peek() != '\n') i++
                tokens.add(Token(code.substring(start, i), TokenType.COMMENT))
                continue
            }

            // Block comment (handles nesting)
            if (c == '/' && peek(1) == '*') {
                val start = i
                i += 2
                var nesting = 1
                while (peek() != null && nesting > 0) {
                    if (peek() == '/' && peek(1) == '*') {
                        nesting++
                        i += 2
                    } else if (peek() == '*' && peek(1) == '/') {
                        nesting--
                        i += 2
                    } else {
                        i++
                    }
                }
                tokens.add(Token(code.substring(start, i), TokenType.COMMENT))
                continue
            }

            // Triple quote string
            if (c == '"' && peek(1) == '"' && peek(2) == '"') {
                val start = i
                i += 3
                while (peek() != null) {
                    if (peek() == '"' && peek(1) == '"' && peek(2) == '"') {
                        i += 3
                        break
                    }
                    i++
                }
                tokens.add(Token(code.substring(start, i), TokenType.STRING))
                continue
            }

            // Single quote string
            if (c == '"') {
                val start = i
                i++
                while (peek() != null && peek() != '"' && peek() != '\n') {
                    if (peek() == '\\') i += 2 else i++
                }
                if (peek() == '"') i++
                tokens.add(Token(code.substring(start, i), TokenType.STRING))
                continue
            }

            // Character literal
            if (c == '\'') {
                val start = i
                i++
                while (peek() != null && peek() != '\'' && peek() != '\n') {
                    if (peek() == '\\') i += 2 else i++
                }
                if (peek() == '\'') i++
                tokens.add(Token(code.substring(start, i), TokenType.STRING))
                continue
            }

            // Annotation
            if (c == '@') {
                val start = i
                i++
                while (peek()?.let { isIdentifierPart(it) } == true) i++
                tokens.add(Token(code.substring(start, i), TokenType.ANNOTATION))
                continue
            }

            // Number (Decimal, Hex, Binary)
            if (c.isDigit()) {
                val start = i
                if (c == '0' && (peek(1) == 'x' || peek(1) == 'X')) {
                    i += 2
                    while (peek()?.let { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == '_' } == true) i++
                } else if (c == '0' && (peek(1) == 'b' || peek(1) == 'B')) {
                    i += 2
                    while (peek()?.let { it == '0' || it == '1' || it == '_' } == true) i++
                } else {
                    while (peek()?.let { it.isDigit() || it == '_' || it == '.' || it == 'e' || it == 'E' || it == 'f' || it == 'F' || it == 'L' } == true) {
                        // Prevent eating the dot of a function call if it's not a float e.g. 1.toString() vs 1.0
                        if (peek() == '.' && peek(1)?.isDigit() != true) break
                        i++
                    }
                }
                if (peek() == 'L' || peek() == 'f' || peek() == 'F') i++
                tokens.add(Token(code.substring(start, i), TokenType.NUMBER))
                continue
            }

            // Identifier
            if (isIdentifierStart(c) || c == '`') {
                val start = i
                if (c == '`') {
                    i++
                    while (peek() != null && peek() != '`' && peek() != '\n') i++
                    if (peek() == '`') i++
                } else {
                    while (peek()?.let { isIdentifierPart(it) } == true) i++
                }
                
                val text = code.substring(start, i)
                
                // Lookahead to see if function
                var isFunction = false
                var peekIndex = i
                while (peekIndex < length && code[peekIndex].isWhitespace()) peekIndex++
                if (peekIndex < length && (code[peekIndex] == '(' || code[peekIndex] == '{')) {
                    isFunction = true
                }
                
                val type = when {
                    text in keywords -> TokenType.KEYWORD
                    text.firstOrNull()?.isUpperCase() == true && text.all { isIdentifierPart(it) } -> TokenType.TYPE
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
