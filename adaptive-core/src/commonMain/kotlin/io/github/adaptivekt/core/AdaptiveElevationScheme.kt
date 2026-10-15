package io.github.adaptivekt.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Semantic elevation levels for Compose Multiplatform surfaces. */
public enum class AdaptiveElevationLevel { None, Low, Medium, High, Floating }

/**
 * Shared elevation and shadow tokens. Override through AdaptiveTheme to match your brand.
 * The shadow color includes alpha; it is not the surface background color.
 */
public data class AdaptiveElevationScheme(
    val low: Dp,
    val medium: Dp,
    val high: Dp,
    val floating: Dp,
    val shadowColor: Color,
) {
    public fun resolve(level: AdaptiveElevationLevel): Dp = when (level) {
        AdaptiveElevationLevel.None -> 0.dp
        AdaptiveElevationLevel.Low -> low
        AdaptiveElevationLevel.Medium -> medium
        AdaptiveElevationLevel.High -> high
        AdaptiveElevationLevel.Floating -> floating
    }

    public companion object {
        public fun defaultLight(): AdaptiveElevationScheme = AdaptiveElevationScheme(
            low = 1.dp, medium = 4.dp, high = 10.dp, floating = 18.dp,
            shadowColor = Color(0x260F172A),
        )

        public fun defaultDark(): AdaptiveElevationScheme = AdaptiveElevationScheme(
            low = 1.dp, medium = 3.dp, high = 8.dp, floating = 16.dp,
            shadowColor = Color(0x80000000),
        )
    }
}
