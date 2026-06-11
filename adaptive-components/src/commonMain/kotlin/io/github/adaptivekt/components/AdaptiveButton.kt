package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveColorScheme
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

public enum class AdaptiveButtonVariant {
    Primary,
    Secondary,
    Ghost,
    Danger,
}

public enum class AdaptiveButtonSize {
    Small,
    Medium,
    Large,
}

/**
 * Renders a themed AdaptiveKt button for primary actions and secondary commands.
 *
 * @param text Visible button label.
 * @param onClick Command invoked when the button is pressed.
 * @param modifier Modifier applied to the root button container.
 * @param variant Visual treatment (Primary, Secondary, Ghost, or Danger).
 * @param size Button height and padding constraints (Small, Medium, Large).
 * @param enabled Disables click interaction and lowers contrast when false.
 * @param leadingIcon Optional composable slot for a leading icon.
 * @param trailingIcon Optional composable slot for a trailing icon.
 */
@Composable
public fun AdaptiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AdaptiveButtonVariant = AdaptiveButtonVariant.Primary,
    size: AdaptiveButtonSize = AdaptiveButtonSize.Medium,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = AdaptiveComponentDefaults.PillShape
    val colors = buttonColors(variant, enabled, hovered, pressed, AdaptiveTheme.colors)
    val metrics = buttonMetrics(size)

    Box(
        modifier = modifier
            .heightIn(min = metrics.minHeight)
            .clip(shape)
            .background(colors.background, shape)
            .border(colors.borderWidth, colors.border, shape)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = metrics.horizontalPadding, vertical = metrics.verticalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            }
            BasicText(
                text = text,
                style = TextStyle(
                    fontSize = metrics.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.content,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                trailingIcon()
            }
        }
    }
}

private data class ButtonColors(
    val background: Color,
    val content: Color,
    val border: Color,
    val borderWidth: Dp = 1.dp,
)

private data class ButtonMetrics(
    val minHeight: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val fontSize: androidx.compose.ui.unit.TextUnit,
)

private fun buttonMetrics(size: AdaptiveButtonSize): ButtonMetrics = when (size) {
    AdaptiveButtonSize.Small -> ButtonMetrics(36.dp, 12.dp, 6.dp, 13.sp)
    AdaptiveButtonSize.Medium -> ButtonMetrics(AdaptiveTokens.Sizes.ButtonHeight, 18.dp, 8.dp, 14.sp)
    AdaptiveButtonSize.Large -> ButtonMetrics(48.dp, 24.dp, 10.dp, 15.sp)
}

private fun buttonColors(
    variant: AdaptiveButtonVariant,
    enabled: Boolean,
    hovered: Boolean,
    pressed: Boolean,
    colorScheme: AdaptiveColorScheme,
): ButtonColors {
    if (!enabled) {
        return ButtonColors(
            background = colorScheme.disabledBackground,
            content = colorScheme.disabledText,
            border = colorScheme.border,
        )
    }

    return when (variant) {
        AdaptiveButtonVariant.Primary -> ButtonColors(
            background = when {
                pressed -> colorScheme.primary
                hovered -> colorScheme.primary
                else -> colorScheme.primary
            },
            content = colorScheme.textInverse,
            border = colorScheme.primary,
        )

        AdaptiveButtonVariant.Secondary -> ButtonColors(
            background = when {
                pressed -> colorScheme.surfaceRaised
                hovered -> colorScheme.surfaceRaised
                else -> colorScheme.surfaceMuted
            },
            content = colorScheme.textPrimary,
            border = if (hovered || pressed) colorScheme.borderStrong else colorScheme.border,
        )

        AdaptiveButtonVariant.Ghost -> ButtonColors(
            background = when {
                pressed -> colorScheme.surfaceMuted
                hovered -> colorScheme.surfaceMuted
                else -> Color.Transparent
            },
            content = colorScheme.textPrimary,
            border = Color.Transparent,
            borderWidth = 0.dp,
        )

        AdaptiveButtonVariant.Danger -> ButtonColors(
            background = when {
                pressed -> colorScheme.dangerSubtle
                hovered -> colorScheme.dangerSubtle
                else -> colorScheme.dangerSubtle
            },
            content = colorScheme.danger,
            border = if (hovered || pressed) colorScheme.danger else colorScheme.dangerText,
        )
    }
}
