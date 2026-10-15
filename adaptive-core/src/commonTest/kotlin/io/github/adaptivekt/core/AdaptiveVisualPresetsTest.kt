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
    }
}
