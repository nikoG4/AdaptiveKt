package io.github.adaptivekt.core

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Regression guard for the minimum text contrast of the default semantic palettes.
 * This covers the primary command and ordinary surfaces; full color and visual
 * accessibility review remains a separate beta gate.
 */
class AdaptiveDefaultPaletteContrastTest {
    @Test
    fun defaultThemePrimaryTextMeetsFourPointFiveContrast() {
        for (scheme in listOf(AdaptiveColorSchemes.defaultLight(), AdaptiveColorSchemes.defaultDark())) {
            assertTrue(contrast(scheme.textPrimary, scheme.background) >= 4.5)
            assertTrue(contrast(scheme.textPrimary, scheme.surface) >= 4.5)
            assertTrue(contrast(scheme.textPrimary, scheme.surfaceRaised) >= 4.5)
            assertTrue(contrast(scheme.textInverse, scheme.primary) >= 4.5)
        }
    }

    private fun contrast(a: Color, b: Color): Double {
        val higher = maxOf(luminance(a), luminance(b))
        val lower = minOf(luminance(a), luminance(b))
        return (higher + 0.05) / (lower + 0.05)
    }

    private fun luminance(color: Color): Double =
        0.2126 * channel(color.red) + 0.7152 * channel(color.green) + 0.0722 * channel(color.blue)

    private fun channel(value: Float): Double {
        val x = value.toDouble()
        return if (x <= 0.04045) x / 12.92 else ((x + 0.055) / 1.055).pow(2.4)
    }
}
