package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTheme

/** A color or gradient background used by [AdaptiveSurfaceStyle]. */
public sealed interface AdaptiveSurfaceBackground {
    public data class Solid(val color: Color) : AdaptiveSurfaceBackground
    public data class Gradient(val brush: Brush) : AdaptiveSurfaceBackground
}

/** A low-contrast border for a surface style. */
public data class AdaptiveSurfaceBorder(
    val color: Color,
    val width: Dp = 1.dp,
)

/** Cross-platform shadow settings. Color is used where the Compose target supports it. */
public data class AdaptiveSurfaceShadow(
    val elevation: Dp = 0.dp,
    val color: Color = Color.Transparent,
)

/** Optional ambient radial light drawn behind surface content. */
public data class AdaptiveSurfaceGlow(
    val color: Color,
    val alpha: Float = 0.14f,
)

/**
 * Immutable visual treatment for [AdaptiveSurface].
 *
 * Glass is a portable visual effect made from translucency, gradients, borders and shadows.
 * It intentionally does not imply platform backdrop blur.
 */
public data class AdaptiveSurfaceStyle(
    val background: AdaptiveSurfaceBackground,
    val border: AdaptiveSurfaceBorder? = null,
    val shadow: AdaptiveSurfaceShadow? = null,
    val glow: AdaptiveSurfaceGlow? = null,
    val overlay: Color = Color.Transparent,
    val highlight: Color = Color.Transparent,
)

/** Theme-aware starting points for solid, elevated, gradient, and glass surfaces. */
public object AdaptiveSurfaceDefaults {
    @Composable
    public fun solid(): AdaptiveSurfaceStyle = AdaptiveSurfaceStyle(
        background = AdaptiveSurfaceBackground.Solid(AdaptiveTheme.colors.surface),
        border = AdaptiveSurfaceBorder(AdaptiveTheme.colors.border),
    )

    @Composable
    public fun elevated(): AdaptiveSurfaceStyle = AdaptiveSurfaceStyle(
        background = AdaptiveSurfaceBackground.Solid(AdaptiveTheme.colors.surfaceRaised),
        border = AdaptiveSurfaceBorder(AdaptiveTheme.colors.borderStrong.copy(alpha = 0.72f)),
        shadow = AdaptiveSurfaceShadow(8.dp, AdaptiveTheme.colors.overlay.copy(alpha = 0.22f)),
        highlight = AdaptiveTheme.colors.textInverse.copy(alpha = 0.035f),
    )

    @Composable
    public fun gradient(): AdaptiveSurfaceStyle = AdaptiveSurfaceStyle(
        background = AdaptiveSurfaceBackground.Gradient(
            Brush.linearGradient(
                listOf(AdaptiveTheme.colors.primarySubtle, AdaptiveTheme.colors.surfaceRaised),
            ),
        ),
        border = AdaptiveSurfaceBorder(AdaptiveTheme.colors.borderStrong.copy(alpha = 0.7f)),
        shadow = AdaptiveSurfaceShadow(6.dp, AdaptiveTheme.colors.overlay.copy(alpha = 0.18f)),
    )

    @Composable
    public fun glass(): AdaptiveSurfaceStyle = AdaptiveSurfaceStyle(
        background = AdaptiveSurfaceBackground.Gradient(
            Brush.linearGradient(
                listOf(
                    AdaptiveTheme.colors.surfaceRaised.copy(alpha = 0.84f),
                    AdaptiveTheme.colors.surface.copy(alpha = 0.68f),
                ),
            ),
        ),
        border = AdaptiveSurfaceBorder(AdaptiveTheme.colors.textInverse.copy(alpha = 0.18f)),
        shadow = AdaptiveSurfaceShadow(12.dp, AdaptiveTheme.colors.overlay.copy(alpha = 0.28f)),
        glow = AdaptiveSurfaceGlow(AdaptiveTheme.colors.primary, alpha = 0.10f),
        overlay = AdaptiveTheme.colors.primary.copy(alpha = 0.025f),
        highlight = AdaptiveTheme.colors.textInverse.copy(alpha = 0.055f),
    )
}

internal fun Modifier.adaptiveSurfaceBackground(background: AdaptiveSurfaceBackground, shape: Shape): Modifier = when (background) {
    is AdaptiveSurfaceBackground.Solid -> background(background.color, shape)
    is AdaptiveSurfaceBackground.Gradient -> background(background.brush, shape)
}

internal fun Modifier.adaptiveSurfaceShadow(shadow: AdaptiveSurfaceShadow?, shape: Shape): Modifier =
    if (shadow == null || shadow.elevation <= 0.dp) this else shadow(
        elevation = shadow.elevation,
        shape = shape,
        clip = false,
        ambientColor = shadow.color,
        spotColor = shadow.color,
    )
