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
    var inBlockComment = false
    return code.lines().count { line ->
        val trimmed = line.trim()
        
        if (inBlockComment) {
            if (trimmed.contains("*/")) {
                inBlockComment = false
                val codeAfterComment = trimmed.substringAfterLast("*/").trim()
                codeAfterComment.isNotEmpty() && !codeAfterComment.startsWith("/*") && !codeAfterComment.startsWith("//")
            } else {
                false
            }
        } else {
            if (trimmed.startsWith("/*")) {
                if (!trimmed.contains("*/")) {
                    inBlockComment = true
                    false
                } else {
                    val codeAfterComment = trimmed.substringAfterLast("*/").trim()
                    codeAfterComment.isNotEmpty() && !codeAfterComment.startsWith("/*") && !codeAfterComment.startsWith("//")
                }
            } else {
                trimmed.isNotEmpty() &&
                !trimmed.startsWith("import ") &&
                !trimmed.startsWith("package ") &&
                !trimmed.startsWith("//")
            }
        }
    }
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
