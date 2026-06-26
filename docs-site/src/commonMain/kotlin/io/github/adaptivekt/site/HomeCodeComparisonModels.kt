package io.github.adaptivekt.site

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

internal data class CodeReductionMetrics(
    val adaptiveLines: Int,
    val composeLines: Int,
    val savedLines: Int,
    val reductionPercent: Int,
) {
    init {
        require(adaptiveLines >= 0) { "adaptiveLines cannot be negative" }
        require(composeLines >= 0) { "composeLines cannot be negative" }
        require(savedLines >= 0) { "savedLines cannot be negative" }
        require(reductionPercent in 0..100) { "reductionPercent must be in 0..100" }
    }
}

internal enum class HomeCodeComparisonLayout {
    Tabbed,
    SideBySide,
}

internal enum class ComparisonImplementation(val label: String) {
    AdaptiveKt("AdaptiveKt"),
    PlainCompose("Plain Compose"),
}

internal fun resolveHomeCodeComparisonLayout(width: Dp): HomeCodeComparisonLayout {
    return if (width < 960.dp) {
        HomeCodeComparisonLayout.Tabbed
    } else {
        HomeCodeComparisonLayout.SideBySide
    }
}

internal fun formatMeaningfulLineCount(count: Int): String {
    return if (count == 1) "1 meaningful line" else "$count meaningful lines"
}

internal fun countMeaningfulCodeLines(code: String): Int {
    var blockCommentDepth = 0
    var insideString = false
    var insideChar = false
    var escapeNext = false
    var meaningfulLines = 0

    val lines = code.lines()
    for (line in lines) {
        val trimmed = line.trimStart()
        if (blockCommentDepth == 0 && (trimmed.startsWith("import ") || trimmed.startsWith("package "))) {
            continue
        }

        var lineHasMeaningfulCode = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            val nextC = if (i + 1 < line.length) line[i + 1] else '\u0000'

            if (escapeNext) {
                escapeNext = false
                if (blockCommentDepth == 0) lineHasMeaningfulCode = true
                i++
                continue
            }

            if (c == '\\' && (insideString || insideChar)) {
                escapeNext = true
                i++
                continue
            }

            if (insideString) {
                if (c == '"') {
                    insideString = false
                }
                if (blockCommentDepth == 0) lineHasMeaningfulCode = true
                i++
                continue
            }

            if (insideChar) {
                if (c == '\'') {
                    insideChar = false
                }
                if (blockCommentDepth == 0) lineHasMeaningfulCode = true
                i++
                continue
            }

            if (blockCommentDepth > 0) {
                if (c == '*' && nextC == '/') {
                    blockCommentDepth--
                    i += 2
                    continue
                } else if (c == '/' && nextC == '*') {
                    blockCommentDepth++
                    i += 2
                    continue
                }
                i++
                continue
            }

            if (c == '/' && nextC == '/') {
                break
            }

            if (c == '/' && nextC == '*') {
                blockCommentDepth++
                i += 2
                continue
            }

            if (c == '"') {
                insideString = true
                lineHasMeaningfulCode = true
                i++
                continue
            }

            if (c == '\'') {
                insideChar = true
                lineHasMeaningfulCode = true
                i++
                continue
            }

            if (!c.isWhitespace()) {
                lineHasMeaningfulCode = true
            }
            i++
        }

        if (lineHasMeaningfulCode) {
            meaningfulLines++
        }
    }
    return meaningfulLines
}

internal fun calculateCodeReduction(adaptiveCode: String, composeCode: String): CodeReductionMetrics {
    val adaptiveLines = countMeaningfulCodeLines(adaptiveCode)
    val composeLines = countMeaningfulCodeLines(composeCode)
    val savedLines = max(0, composeLines - adaptiveLines)
    val reductionPercent = if (composeLines > 0) max(0, (savedLines * 100) / composeLines) else 0

    return CodeReductionMetrics(
        adaptiveLines = adaptiveLines,
        composeLines = composeLines,
        savedLines = savedLines,
        reductionPercent = reductionPercent,
    )
}
