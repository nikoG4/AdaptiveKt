package io.github.adaptivekt.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdaptiveSurfaceStyleTest {
    @Test
    fun `solid style preserves value object settings`() {
        val style = AdaptiveSurfaceStyle(
            background = AdaptiveSurfaceBackground.Solid(Color(0xFF102030)),
            border = AdaptiveSurfaceBorder(Color.White, 2.dp),
            shadow = AdaptiveSurfaceShadow(6.dp, Color.Black),
        )

        assertEquals(AdaptiveSurfaceBackground.Solid(Color(0xFF102030)), style.background)
        assertEquals(AdaptiveSurfaceBorder(Color.White, 2.dp), style.border)
        assertEquals(AdaptiveSurfaceShadow(6.dp, Color.Black), style.shadow)
    }

    @Test
    fun `effects remain opt in by default`() {
        val style = AdaptiveSurfaceStyle(AdaptiveSurfaceBackground.Solid(Color.White))

        assertNull(style.border)
        assertNull(style.shadow)
        assertNull(style.glow)
        assertEquals(Color.Transparent, style.overlay)
        assertEquals(Color.Transparent, style.highlight)
    }
}
