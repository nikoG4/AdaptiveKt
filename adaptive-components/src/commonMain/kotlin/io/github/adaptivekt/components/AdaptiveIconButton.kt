package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
 * @param contentDescription Accessible label, recommended for every icon-only action.
 * @param content Composable slot for the icon content, typically [AdaptiveIcons].
 */
@Composable
public fun AdaptiveIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 40.dp,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val pressed by interactionSource.collectIsPressedAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = AdaptiveComponentDefaults.PillShape
    val background = when {
        !enabled -> AdaptiveComponentDefaults.DisabledSurface
        pressed -> lerp(AdaptiveComponentDefaults.SurfaceSubtle, AdaptiveTheme.colors.primarySubtle, 0.6f)
        hovered -> AdaptiveComponentDefaults.Surface
        else -> AdaptiveComponentDefaults.SurfaceSubtle
    }
    val border = if (hovered || pressed) AdaptiveComponentDefaults.BorderStrong else AdaptiveComponentDefaults.Border

    val textColor = when {
        !enabled -> AdaptiveComponentDefaults.DisabledText
        pressed || hovered -> AdaptiveTheme.colors.textPrimary
        else -> AdaptiveTheme.colors.textSecondary
    }

    val motionTokens = AdaptiveTheme.motion
    val transitionMs = motionTokens.durationMillis(motionTokens.fastMillis)
    val backgroundColor by animateColorAsState(
        targetValue = background,
        animationSpec = tween(durationMillis = transitionMs),
        label = "backgroundColor",
    )
    val borderColor by animateColorAsState(
        targetValue = if (focused && enabled) AdaptiveTheme.colors.focusRing else border,
        animationSpec = tween(durationMillis = transitionMs),
        label = "borderColor",
    )
    val contentColor by animateColorAsState(
        targetValue = textColor,
        animationSpec = tween(durationMillis = transitionMs),
        label = "contentColor",
    )

    Box(
        modifier = modifier
            .size(size)
            .semantics { if (contentDescription != null) this.contentDescription = contentDescription }
            .clip(shape)
            .background(backgroundColor, shape)
            .border(if (focused && enabled) AdaptiveTheme.states.focusBorderWidth else 1.dp, borderColor, shape)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.runtime.CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides contentColor) {
            content()
        }
    }
}
