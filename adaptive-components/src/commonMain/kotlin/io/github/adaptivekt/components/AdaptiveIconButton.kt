package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.adaptiveInteractiveCursor

/**
 * Renders a compact, rounded icon button for toolbars and concise actions.
 *
 * @param onClick Command invoked when the button is pressed.
 * @param modifier Modifier applied to the root button container.
 * @param enabled Disables click interaction when false.
 * @param size Size of the square hit area (default 40.dp).
 * @param content Composable slot for the icon content, typically [AdaptiveIcons].
 */
@Composable
public fun AdaptiveIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 40.dp,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = AdaptiveComponentDefaults.PillShape
    val background = when {
        !enabled -> AdaptiveComponentDefaults.DisabledSurface
        pressed -> AdaptiveComponentDefaults.Surface
        hovered -> AdaptiveComponentDefaults.Surface
        else -> AdaptiveComponentDefaults.SurfaceSubtle
    }
    val border = if (hovered || pressed) AdaptiveComponentDefaults.BorderStrong else AdaptiveComponentDefaults.Border

    val textColor = when {
        !enabled -> AdaptiveComponentDefaults.DisabledText
        pressed || hovered -> AdaptiveTheme.colors.textPrimary
        else -> AdaptiveTheme.colors.textSecondary
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background, shape)
            .border(1.dp, border, shape)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.runtime.CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides textColor) {
            content()
        }
    }
}
