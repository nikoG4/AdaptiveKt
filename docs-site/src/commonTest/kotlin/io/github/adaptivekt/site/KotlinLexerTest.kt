package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KotlinLexerTest {

    @Test
    fun testExactReconstruction() {
        val corpus = """
            package io.github.adaptivekt.site

            import kotlin.test.Test

            // This is a single line comment
            /* This is a
               multi-line comment */

            @Composable
            fun MyComponent(
                text: String = "Hello",
                modifier: Modifier = Modifier
            ) {
                val num = 42
                val hex = 0xFF
                val bin = 0b1010
                val dec = 3.14f
                val esc = "Escaped \" quotes\n and newlines"
                val char = 'c'
                val escapedChar = '\n'

                AdaptiveCard {
                    AdaptiveText(text = text)
                }
            }
        """.trimIndent()

        val tokens = KotlinLexer.tokenize(corpus)
        val reconstructed = tokens.joinToString("") { it.text }
        assertEquals(corpus, reconstructed, "Tokens must perfectly reconstruct original source")
    }

    @Test
    fun testKeywordsAndModifiers() {
        val tokens = KotlinLexer.tokenize("private val public var suspend fun inner class open override")
        val keywords = tokens.filter { it.type == TokenType.KEYWORD }.map { it.text }
        assertEquals(listOf("private", "val", "public", "var", "suspend", "fun", "class", "open", "override"), keywords)
    }

    @Test
    fun testAnnotations() {
        val tokens = KotlinLexer.tokenize("@Composable @OptIn(Experimental::class)")
        val annotations = tokens.filter { it.type == TokenType.ANNOTATION }.map { it.text }
        assertEquals(listOf("@Composable", "@OptIn"), annotations)
    }

    @Test
    fun testStrings() {
        val tokens = KotlinLexer.tokenize("\"Simple string\" \"Escaped \\\" string\" \"\"\"Triple quoted\"\"\"")
        val strings = tokens.filter { it.type == TokenType.STRING }.map { it.text }
        assertEquals(listOf("\"Simple string\"", "\"Escaped \\\" string\"", "\"\"\"Triple quoted\"\"\""), strings)
    }

    @Test
    fun testComments() {
        val code = """
            // line comment
            /* block comment */
            /* nested /* block */ comment */ // The lexer doesn't fully parse nesting, but shouldn't crash.
        """.trimIndent()
        val tokens = KotlinLexer.tokenize(code)
        val comments = tokens.filter { it.type == TokenType.COMMENT }
        assertTrue(comments.isNotEmpty())
    }

    @Test
    fun testTypes() {
        val tokens = KotlinLexer.tokenize("String Int CustomType Boolean AdaptiveButton")
        val types = tokens.filter { it.type == TokenType.TYPE }.map { it.text }
        assertEquals(listOf("String", "Int", "CustomType", "Boolean", "AdaptiveButton"), types)
    }

    @Test
    fun testFunctions() {
        val tokens = KotlinLexer.tokenize("myFunction() execute { }")
        val functions = tokens.filter { it.type == TokenType.FUNCTION }.map { it.text }
        assertEquals(listOf("myFunction", "execute"), functions)
    }

    @Test
    fun testPropertyAccess() {
        val tokens = KotlinLexer.tokenize("object.property.method()")
        val identifiers = tokens.filter { it.type == TokenType.IDENTIFIER || it.type == TokenType.FUNCTION }.map { it.text }
        // "object" is a keyword. "property" is identifier, "method" is function.
        assertTrue("property" in identifiers)
        assertTrue("method" in identifiers)
    }

    @Test
    fun testIncompleteCode() {
        // Should not crash
        KotlinLexer.tokenize("\"Unclosed string")
        KotlinLexer.tokenize("/* Unclosed comment")
        KotlinLexer.tokenize("val x = 1 + ")
        assertTrue(true)
    }
}
