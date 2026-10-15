package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

@Composable
public fun AdaptiveMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    destructive: Boolean = false,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    val background = when {
        !enabled -> androidx.compose.ui.graphics.Color.Transparent
        destructive && (hovered || focused) -> AdaptiveTheme.colors.dangerSubtle
        destructive -> androidx.compose.ui.graphics.Color.Transparent
        hovered || focused -> AdaptiveComponentDefaults.SurfaceSubtle
        else -> androidx.compose.ui.graphics.Color.Transparent
    }
    val contentColor = if (destructive) AdaptiveComponentDefaults.Danger else AdaptiveComponentDefaults.Text
    val motionTokens = AdaptiveTheme.motion
    val transitionMs = motionTokens.durationMillis(motionTokens.fastMillis)
    val backgroundColor by animateColorAsState(
        targetValue = background,
        animationSpec = tween(durationMillis = transitionMs),
        label = "backgroundColor",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor, shape)
            .border(
                if (focused && enabled) AdaptiveTheme.states.focusBorderWidth else 0.dp,
                if (focused && enabled) AdaptiveTheme.colors.focusRing else androidx.compose.ui.graphics.Color.Transparent,
                shape,
            )
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small)
            .then(if (!enabled) Modifier.alpha(0.5f) else Modifier),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
        }
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
