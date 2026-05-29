package io.github.adaptivekt.core

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape

public data class AdaptiveShapeScheme(
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val pill: Shape,
    val circle: Shape,
) {
    public companion object {
        public fun default(): AdaptiveShapeScheme = AdaptiveShapeScheme(
            small = RoundedCornerShape(AdaptiveTokens.Radius.Small),
            medium = RoundedCornerShape(AdaptiveTokens.Radius.Medium),
            large = RoundedCornerShape(AdaptiveTokens.Radius.Large),
            pill = RoundedCornerShape(AdaptiveTokens.Radius.Pill),
            circle = RoundedCornerShape(AdaptiveTokens.Radius.Pill),
        )
    }
}
