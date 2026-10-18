package io.github.adaptivekt.core

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdaptiveVisualPresetsTest {
    @Test
    fun modernFocusAndCornerTokensDifferFromClassic() {
        assertTrue(AdaptiveVisualPresets.modernShapes().medium != AdaptiveShapeScheme.default().medium)
        assertEquals(2.dp, AdaptiveVisualPresets.modernStates().focusBorderWidth)
        assertEquals(1.dp, AdaptiveStateScheme.default().focusBorderWidth)
        assertEquals(2.dp, AdaptiveVisualPresets.modernLightElevations().low)
        assertEquals(2.dp, AdaptiveVisualPresets.modernDarkElevations().low)
        assertEquals(1.dp, AdaptiveElevationScheme.defaultLight().low)
    }

    @Test
    fun modernLightAndDarkUseDistinctSemanticSurfaces() {
        val light = AdaptiveVisualPresets.modernLight()
        val dark = AdaptiveVisualPresets.modernDark()
        assertTrue(light.background != dark.background)
        assertTrue(light.textPrimary != dark.textPrimary)
        assertTrue(light.surface != light.background)
        assertTrue(dark.surface != dark.background)
        assertTrue(light.primary != dark.primary)
        assertTrue(light.overlay != dark.overlay)
        assertTrue(light.overlay.alpha > 0f && light.overlay.alpha < 1f)
        assertTrue(dark.overlay.alpha > 0f && dark.overlay.alpha < 1f)
    }
}
