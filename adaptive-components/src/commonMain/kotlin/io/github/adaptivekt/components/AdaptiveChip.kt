package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Chip tone enum for AdaptiveChip.
 *
 * Defines the visual tone and state of chips for filters, tags, and selections.
 */
public enum class AdaptiveChipTone {
    Neutral,
    Primary,
    Success,
    Warning,
    Danger,
    Info,
}

/**
 * A chip component for filters, tags, and selection states.
 *
 * Supports:
 * - Neutral, Primary, Success, Warning, Danger, Info tones
 * - Selected and disabled states
 * - Leading and trailing icon slots
 * - Clickable or non-clickable based on onClick parameter
 *
 * Designed as a pill-shaped component without Material 3 dependencies.
 * Prepares the foundation for future MultiSelect implementation.
 */
@Composable
public fun AdaptiveChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    tone: AdaptiveChipTone = AdaptiveChipTone.Neutral,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val chipColor = chipBackgroundColor(selected, enabled, tone)
    val chipBorderColor = chipBorderColor(selected, enabled, tone)
    val chipTextColor = chipTextColor(selected, enabled, tone)
    val shape = RoundedCornerShape(AdaptiveTokens.Radius.Pill)

    Row(
        modifier = modifier
            .heightIn(min = 32.dp)
            .clip(shape)
            .background(chipColor, shape)
            .border(1.dp, chipBorderColor, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled = enabled, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.XSmall))
        }
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = chipTextColor,
            ),
            maxLines = 1,
        )
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.XSmall))
            trailingIcon()
        }
    }
}

private fun chipBackgroundColor(selected: Boolean, enabled: Boolean, tone: AdaptiveChipTone): Color {
    return when (tone) {
        AdaptiveChipTone.Primary -> if (selected && enabled) Color(0xFF2563EB) else if (enabled) Color(0xFFEBF4FF) else Color(0xFFE5E7EB)
        AdaptiveChipTone.Success -> if (selected && enabled) Color(0xFF047857) else if (enabled) Color(0xFFF0FDF4) else Color(0xFFE5E7EB)
        AdaptiveChipTone.Warning -> if (selected && enabled) Color(0xFFB45309) else if (enabled) Color(0xFFFDF6E7) else Color(0xFFE5E7EB)
        AdaptiveChipTone.Danger -> if (selected && enabled) Color(0xFFB91C1C) else if (enabled) Color(0xFFFFF5F5) else Color(0xFFE5E7EB)
        AdaptiveChipTone.Info -> if (selected && enabled) Color(0xFF1D4ED8) else if (enabled) Color(0xFFEEF2FF) else Color(0xFFE5E7EB)
        AdaptiveChipTone.Neutral -> if (selected && enabled) Color(0xFF334155) else if (enabled) Color(0xFFF8FAFC) else Color(0xFFE5E7EB)
    }
}

private fun chipBorderColor(selected: Boolean, enabled: Boolean, tone: AdaptiveChipTone): Color {
    return when {
        !enabled -> Color(0xFFE5E7EB)
        selected -> when (tone) {
            AdaptiveChipTone.Primary -> Color(0xFF2563EB)
            AdaptiveChipTone.Success -> Color(0xFF047857)
            AdaptiveChipTone.Warning -> Color(0xFFB45309)
            AdaptiveChipTone.Danger -> Color(0xFFB91C1C)
            AdaptiveChipTone.Info -> Color(0xFF1D4ED8)
            AdaptiveChipTone.Neutral -> Color(0xFF334155)
        }
        else -> when (tone) {
            AdaptiveChipTone.Primary -> Color(0xFF315FDC)
            AdaptiveChipTone.Success -> Color(0xFF059669)
            AdaptiveChipTone.Warning -> Color(0xFFF59E0B)
            AdaptiveChipTone.Danger -> Color(0xFFF87171)
            AdaptiveChipTone.Info -> Color(0xFF6366F1)
            AdaptiveChipTone.Neutral -> Color(0xFF94A3B8)
        }
    }
}

private fun chipTextColor(selected: Boolean, enabled: Boolean, tone: AdaptiveChipTone): Color {
    return when {
        !enabled -> Color(0xFF9CA3AF)
        selected -> Color.White
        else -> when (tone) {
            AdaptiveChipTone.Primary -> Color(0xFF1E3A8A)
            AdaptiveChipTone.Success -> Color(0xFF065F46)
            AdaptiveChipTone.Warning -> Color(0xFF92400E)
            AdaptiveChipTone.Danger -> Color(0xFF991B1B)
            AdaptiveChipTone.Info -> Color(0xFF3730A3)
            AdaptiveChipTone.Neutral -> Color(0xFF334155)
        }
    }
}
