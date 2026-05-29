package io.github.adaptivekt.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public data class AdaptiveStateScheme(
    val hoverAlpha: Float,
    val pressedAlpha: Float,
    val selectedAlpha: Float,
    val disabledAlpha: Float,
    val focusBorderWidth: Dp,
) {
    public companion object {
        public fun default(): AdaptiveStateScheme = AdaptiveStateScheme(
            hoverAlpha = 0.08f,
            pressedAlpha = 0.12f,
            selectedAlpha = 0.16f,
            disabledAlpha = 0.48f,
            focusBorderWidth = 1.dp,
        )
    }
}
