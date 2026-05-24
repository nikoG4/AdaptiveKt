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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
        !enabled -> Color(0xFFF8FAFC)
        pressed -> Color(0xFFE2E8F0)
        hovered -> Color(0xFFF1F5F9)
        else -> AdaptiveComponentDefaults.SurfaceSubtle
    }
    val border = if (hovered || pressed) AdaptiveComponentDefaults.BorderStrong else AdaptiveComponentDefaults.Border

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background, shape)
            .border(1.dp, border, shape)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
