package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdaptiveMenuPlacementTest {

    @Test
    fun testAutoPlacementFitsBelow() {
        // Viewport 1000x1000, Anchor 100x40 at (100, 100). Space below is ~860.
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        // Should place below start
        assertEquals(100, result.x) // Start aligned
        assertEquals(140, result.y) // Just below anchor
        assertTrue(result.maxHeight >= 300)
    }

    @Test
    fun testAutoPlacementFitsAboveWhenNoSpaceBelow() {
        // Viewport 1000x1000, Anchor at bottom.
        val anchor = AdaptiveAnchorBounds(100, 800, 200, 840)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        // Should place above start
        assertEquals(100, result.x)
        assertEquals(800 - 300, result.y)
        assertTrue(result.maxHeight >= 300)
    }

    @Test
    fun testAutoPlacementFallbackToLargestSpace() {
        // Viewport 1000x500. Anchor at 300. Above space = 292, Below space = 500 - 340 = 160.
        val anchor = AdaptiveAnchorBounds(100, 300, 200, 340)
        val viewport = AdaptiveViewportBounds(1000, 500)
        val menu = AdaptiveMenuSize(200, 400) // Does not fit fully above or below
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        // Should fallback to above because 292 > 160
        assertEquals(100, result.x)
        assertEquals(300 - 400, result.y) // y = -100, but clamped later or max height used
        assertEquals(292, result.maxHeight) // maxHeight is 292 (300 - 8 margin)
    }

    @Test
    fun testAlignEnd() {
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(300, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.BelowEnd
        )
        
        // Right bound of anchor is 200. Menu width 300. X should be 200 - 300 = -100.
        // But clamped to minWindowMargin (8).
        assertEquals(8, result.x)
    }

    @Test
    fun testRtlAuto() {
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(300, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto, isRtl = true
        )
        
        // RTL Auto means it aligns End instead of Start.
        // X = 200 - 300 = -100. Clamped to 8.
        assertEquals(8, result.x)
    }

    @Test
    fun testOffset() {
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.BelowStart, offsetX = 10, offsetY = 20
        )
        
        assertEquals(110, result.x)
        assertEquals(160, result.y) // 140 + 20
    }
}
