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
import io.github.adaptivekt.core.AdaptiveTokens

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
    val colors = buttonColors(variant, enabled, hovered, pressed)
    val metrics = buttonMetrics(size)

    Box(
        modifier = modifier
            .heightIn(min = metrics.minHeight)
            .clip(shape)
            .background(colors.background, shape)
            .border(colors.borderWidth, colors.border, shape)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
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
): ButtonColors {
    if (!enabled) {
        return ButtonColors(
            background = Color(0xFFF1F5F9),
            content = Color(0xFF94A3B8),
            border = Color(0xFFE2E8F0),
        )
    }

    return when (variant) {
        AdaptiveButtonVariant.Primary -> ButtonColors(
            background = when {
                pressed -> AdaptiveComponentDefaults.PrimaryPressed
                hovered -> AdaptiveComponentDefaults.PrimaryHover
                else -> AdaptiveComponentDefaults.Primary
            },
            content = Color.White,
            border = when {
                pressed -> AdaptiveComponentDefaults.PrimaryPressed
                else -> AdaptiveComponentDefaults.Primary
            },
        )

        AdaptiveButtonVariant.Secondary -> ButtonColors(
            background = when {
                pressed -> Color(0xFFE2E8F0)
                hovered -> Color(0xFFF1F5F9)
                else -> AdaptiveComponentDefaults.SurfaceSubtle
            },
            content = AdaptiveComponentDefaults.Text,
            border = if (hovered || pressed) AdaptiveComponentDefaults.BorderStrong else AdaptiveComponentDefaults.Border,
        )

        AdaptiveButtonVariant.Ghost -> ButtonColors(
            background = when {
                pressed -> Color(0xFFE2E8F0)
                hovered -> Color(0xFFF8FAFC)
                else -> Color.Transparent
            },
            content = AdaptiveComponentDefaults.Text,
            border = Color.Transparent,
            borderWidth = 0.dp,
        )

        AdaptiveButtonVariant.Danger -> ButtonColors(
            background = when {
                pressed -> Color(0xFFFEE2E2)
                hovered -> Color(0xFFFFEEEE)
                else -> Color(0xFFFEF2F2)
            },
            content = AdaptiveComponentDefaults.Danger,
            border = if (hovered || pressed) Color(0xFFFCA5A5) else Color(0xFFFECACA),
        )
    }
}
