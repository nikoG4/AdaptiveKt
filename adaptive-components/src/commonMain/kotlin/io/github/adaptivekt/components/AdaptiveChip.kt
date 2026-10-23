package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveColorScheme
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

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
    val colorScheme = AdaptiveTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val chipColor = chipBackgroundColor(selected, enabled, tone, colorScheme)
    val chipBorderColor = chipBorderColor(selected, enabled, tone, colorScheme)
    val chipTextColor = chipTextColor(selected, enabled, tone, colorScheme)
    val shape = RoundedCornerShape(AdaptiveTokens.Radius.Pill)
    val transitionMs = AdaptiveTheme.motion.durationMillis(AdaptiveTheme.motion.fastMillis)
    val targetBackground = if (onClick != null && enabled && (hovered || focused)) {
        lerp(chipColor, colorScheme.surfaceRaised, 0.2f)
    } else {
        chipColor
    }
    val background by animateColorAsState(targetBackground, tween(durationMillis = transitionMs), label = "Chip background")
    val border by animateColorAsState(
        if (focused && enabled) colorScheme.focusRing else chipBorderColor,
        tween(durationMillis = transitionMs),
        label = "Chip border",
    )
    val contentColor by animateColorAsState(chipTextColor, tween(durationMillis = transitionMs), label = "Chip text")

    Row(
        modifier = modifier
            .heightIn(min = 32.dp)
            .clip(shape)
            .background(background, shape)
            .border(if (focused && enabled) AdaptiveTheme.states.focusBorderWidth else 1.dp, border, shape)
            .then(
                if (onClick != null) {
                    Modifier
                        .hoverable(interactionSource = interactionSource, enabled = enabled)
                        .adaptiveInteractiveCursor(enabled)
                        .semantics { this.selected = selected }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = enabled,
                            role = Role.Button,
                            onClick = onClick,
                        )
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
                color = contentColor,
            ),
            maxLines = 1,
        )
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.XSmall))
            trailingIcon()
        }
    }
}

private fun chipBackgroundColor(
    selected: Boolean,
    enabled: Boolean,
    tone: AdaptiveChipTone,
    colorScheme: AdaptiveColorScheme,
): Color {
    return when (tone) {
        AdaptiveChipTone.Primary -> if (selected && enabled) colorScheme.primary else if (enabled) colorScheme.primarySubtle else colorScheme.disabledBackground
        AdaptiveChipTone.Success -> if (selected && enabled) colorScheme.success else if (enabled) colorScheme.successSubtle else colorScheme.disabledBackground
        AdaptiveChipTone.Warning -> if (selected && enabled) colorScheme.warning else if (enabled) colorScheme.warningSubtle else colorScheme.disabledBackground
        AdaptiveChipTone.Danger -> if (selected && enabled) colorScheme.danger else if (enabled) colorScheme.dangerSubtle else colorScheme.disabledBackground
        AdaptiveChipTone.Info -> if (selected && enabled) colorScheme.info else if (enabled) colorScheme.infoSubtle else colorScheme.disabledBackground
        AdaptiveChipTone.Neutral -> if (selected && enabled) colorScheme.textSecondary else if (enabled) colorScheme.surfaceMuted else colorScheme.disabledBackground
    }
}

private fun chipBorderColor(
    selected: Boolean,
    enabled: Boolean,
    tone: AdaptiveChipTone,
    colorScheme: AdaptiveColorScheme,
): Color {
    return when {
        !enabled -> colorScheme.border
        selected -> when (tone) {
            AdaptiveChipTone.Primary -> colorScheme.primary
            AdaptiveChipTone.Success -> colorScheme.success
            AdaptiveChipTone.Warning -> colorScheme.warning
            AdaptiveChipTone.Danger -> colorScheme.danger
            AdaptiveChipTone.Info -> colorScheme.info
            AdaptiveChipTone.Neutral -> colorScheme.textSecondary
        }
        else -> when (tone) {
            AdaptiveChipTone.Primary -> colorScheme.primary
            AdaptiveChipTone.Success -> colorScheme.success
            AdaptiveChipTone.Warning -> colorScheme.warning
            AdaptiveChipTone.Danger -> colorScheme.danger
            AdaptiveChipTone.Info -> colorScheme.info
            AdaptiveChipTone.Neutral -> colorScheme.disabledText
        }
    }
}

private fun chipTextColor(
    selected: Boolean,
    enabled: Boolean,
    tone: AdaptiveChipTone,
    colorScheme: AdaptiveColorScheme,
): Color {
    return when {
        !enabled -> colorScheme.disabledText
        selected -> colorScheme.textInverse
        else -> when (tone) {
            AdaptiveChipTone.Primary -> colorScheme.primaryText
            AdaptiveChipTone.Success -> colorScheme.successText
            AdaptiveChipTone.Warning -> colorScheme.warningText
            AdaptiveChipTone.Danger -> colorScheme.dangerText
            AdaptiveChipTone.Info -> colorScheme.infoText
            AdaptiveChipTone.Neutral -> colorScheme.textSecondary
        }
    }
}
