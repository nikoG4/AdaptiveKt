package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
public fun AdaptiveSurface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    content: @Composable BoxScope.() -> Unit,
) {
    AdaptiveSurface(
        modifier = modifier,
        contentPadding = contentPadding,
        style = AdaptiveSurfaceDefaults.solid(),
        content = content,
    )
}

/**
 * Surface with an explicit reusable [style]. Glass styles are visual-only and work without
 * requiring platform-specific backdrop blur support.
 */
@Composable
public fun AdaptiveSurface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    style: AdaptiveSurfaceStyle,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = AdaptiveComponentDefaults.MediumShape
    Box(
        modifier = modifier
            .adaptiveSurfaceShadow(style.shadow, shape)
            .clip(shape)
            .adaptiveSurfaceBackground(style.background, shape)
            .then(if (style.border != null) Modifier.border(style.border.width, style.border.color, shape) else Modifier),
        contentAlignment = Alignment.TopStart,
    ) {
        style.glow?.let { glow ->
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Brush.radialGradient(listOf(glow.color.copy(alpha = glow.alpha), Color.Transparent)), shape),
            )
        }
        if (style.overlay != Color.Transparent) {
            Box(modifier = Modifier.matchParentSize().background(style.overlay, shape))
        }
        if (style.highlight != Color.Transparent) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Brush.verticalGradient(listOf(style.highlight, Color.Transparent)), shape),
            )
        }
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}
