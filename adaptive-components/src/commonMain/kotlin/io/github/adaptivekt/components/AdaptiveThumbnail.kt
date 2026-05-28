package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * A small thumbnail component for products, documents, and media items.
 *
 * Shows initial letters from a label when no image is provided,
 * or renders a custom image slot when provided.
 *
 * Designed for non-personal items (products, files, media) with
 * muted tones and professional styling.
 */
@Composable
public fun AdaptiveThumbnail(
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    shape: Shape = RoundedCornerShape(AdaptiveTokens.Radius.Medium),
    image: (@Composable () -> Unit)? = null,
    tone: Color = Color(0xFF64748B),
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(tone, shape)
            .border(1.dp, Color(0xFFE2E8F0), shape),
        contentAlignment = Alignment.Center,
    ) {
        if (image != null) {
            image()
        } else {
            BasicText(
                text = thumbnailLabelFor(label),
                style = TextStyle(
                    fontSize = thumbnailFontSize(size),
                    fontWeight = FontWeight.Bold,
                color = thumbnailTextColor(tone),
                ),
                maxLines = 1,
            )
        }
    }
}

/**
 * Extracts a 2-character label from a text string for thumbnails.
 *
 * Rules:
 * - Takes the first character from the first two readable words
 * - Falls back to "?" for empty or whitespace-only strings
 * - Handles multiple spaces and unusual characters gracefully
 * - Filters out symbols to get meaningful initials
 *
 * Examples:
 * - "AirPods Pro" -> "AP"
 * - "Router Gigabit" -> "RG"
 * - "Router" -> "R"
 * - "" -> "?"
 * - "   " -> "?"
 */
public fun thumbnailLabelFor(text: String): String {
    val parts = text
        .trim()
        .split(Regex("[\\s\\-_]+"))
        .map { part -> part.filter { it.isLetterOrDigit() } }
        .filter { it.isNotBlank() }

    val label = when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}"
        parts.size == 1 -> parts[0].take(1)
        else -> text.filter { it.isLetterOrDigit() }.take(2)
    }

    return label.ifBlank { "?" }.uppercase()
}

private fun thumbnailFontSize(size: Dp) = when {
    size < 32.dp -> 11.sp
    size < 44.dp -> 13.sp
    else -> 15.sp
}

private fun thumbnailTextColor(background: Color): Color {
    val luminance = (0.299f * background.red) + (0.587f * background.green) + (0.114f * background.blue)
    return if (luminance < 0.55f) Color.White else Color(0xFF0F172A)
}
