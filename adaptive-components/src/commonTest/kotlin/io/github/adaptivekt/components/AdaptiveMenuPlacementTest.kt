package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class AdaptiveMenuPlacementTest {

    @Test
    fun testAutoPlacementFitsBelow() {
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        assertEquals(100, result.x)
        assertEquals(140, result.y)
        assertEquals(1000 - 140 - 8, result.maxHeight)
    }

    @Test
    fun testAutoPlacementFitsAboveWhenNoSpaceBelow() {
        val anchor = AdaptiveAnchorBounds(100, 800, 200, 840)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        assertEquals(100, result.x)
        assertEquals(800 - 300, result.y)
        assertEquals(800 - 8, result.maxHeight)
    }

    @Test
    fun testAutoPlacementFallbackToLargestSpace() {
        val anchor = AdaptiveAnchorBounds(100, 300, 200, 340)
        val viewport = AdaptiveViewportBounds(1000, 500)
        val menu = AdaptiveMenuSize(200, 400)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        // Above space = 292, Below space = 152. Should go above.
        assertEquals(100, result.x)
        assertEquals(8, result.y) // clamped to margin
        assertEquals(292, result.maxHeight)
    }

    @Test
    fun testMenuLargerThanViewport() {
        val anchor = AdaptiveAnchorBounds(10, 10, 20, 20)
        val viewport = AdaptiveViewportBounds(100, 100)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        assertEquals(8, result.x) // min margin clamp
        assertEquals(20, result.y) // below anchor
        assertEquals(100 - 20 - 8, result.maxHeight)
    }

    @Test
    fun testViewportSmallerThanMargins() {
        val anchor = AdaptiveAnchorBounds(0, 0, 10, 10)
        val viewport = AdaptiveViewportBounds(10, 10)
        val menu = AdaptiveMenuSize(50, 50)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.BelowStart, windowMarginPx = 8
        )
        
        // Window margin 8 on 10x10 means topLimit=8, bottomLimit=8
        assertEquals(8, result.x)
        assertEquals(8, result.y)
        assertEquals(0, result.maxHeight)
    }

    @Test
    fun testAnchorPartiallyOutside() {
        val anchor = AdaptiveAnchorBounds(-50, 50, 50, 90)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.BelowStart
        )
        
        assertEquals(8, result.x) // Clamped to left margin
        assertEquals(90, result.y)
    }

    @Test
    fun testAnchorCompletelyOutsideNegative() {
        val anchor = AdaptiveAnchorBounds(-200, -200, -100, -100)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.BelowStart
        )
        
        assertEquals(8, result.x) // Clamped
        assertEquals(8, result.y) // Clamped (Top limit is 8, 0 (clamped anchor bottom) + menu is clamped)
        assertEquals(1000 - 16, result.maxHeight) // clamped to safeHeight
    }

    @Test
    fun testAnchorInvertedBounds() {
        val anchor = AdaptiveAnchorBounds(200, 140, 100, 100) // inverted
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.BelowStart
        )
        
        // It normalizes to 100..200, 100..140
        assertEquals(100, result.x)
        assertEquals(140, result.y)
    }

    @Test
    fun testNegativeMarginThrows() {
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 300)
        
        assertFailsWith<IllegalArgumentException> {
            resolveAdaptiveMenuPlacement(anchor, viewport, menu, AdaptiveMenuPlacement.Auto, windowMarginPx = -1)
        }
    }

    @Test
    fun testAutoWithTie() {
        // Viewport 1000x1000. Anchor centered perfectly vertically 480..520.
        // Space above = 480 - 8 = 472. Space below = 1000 - 520 - 8 = 472.
        val anchor = AdaptiveAnchorBounds(100, 480, 200, 520)
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 600) // larger than space
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.Auto
        )
        
        // If tie and doesn't fit, Auto preferAbove = spaceAbove > spaceBelow (which is false, so it goes Below)
        assertEquals(100, result.x)
        assertEquals(520, result.y)
        assertEquals(472, result.maxHeight)
    }

    @Test
    fun testPlacementExplicitNoSpace() {
        val anchor = AdaptiveAnchorBounds(100, 100, 200, 140) // Near top
        val viewport = AdaptiveViewportBounds(1000, 1000)
        val menu = AdaptiveMenuSize(200, 600)
        
        val result = resolveAdaptiveMenuPlacement(
            anchor, viewport, menu, AdaptiveMenuPlacement.AboveStart
        )
        
        // Forced Above.
        assertEquals(100, result.x)
        assertEquals(8, result.y) // clamped to margin 8
        assertEquals(100 - 8, result.maxHeight) // maxHeight is 92
    }

    @Test
    fun testWidthMatchTrue() {
        val result = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = true,
            anchorWidth = 500,
            policyMinWidthPx = -1,
            policyMaxWidthPx = -1,
            safeViewportWidthPx = 1000
        )
        
        // Min should be anchorWidth
        assertEquals(500, result.minWidth)
        assertEquals(1000, result.maxWidth)
    }

    @Test
    fun testWidthMatchFalse() {
        val result = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = false,
            anchorWidth = 500,
            policyMinWidthPx = 100,
            policyMaxWidthPx = 600,
            safeViewportWidthPx = 1000
        )
        
        assertEquals(100, result.minWidth)
        assertEquals(600, result.maxWidth)
    }

    @Test
    fun testAnchorSmallerThanMin() {
        val result = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = true,
            anchorWidth = 50,
            policyMinWidthPx = 100,
            policyMaxWidthPx = 600,
            safeViewportWidthPx = 1000
        )
        
        assertEquals(100, result.minWidth) // clamped to minWidth
        assertEquals(600, result.maxWidth)
    }

    @Test
    fun testAnchorLargerThanMax() {
        val result = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = true,
            anchorWidth = 800,
            policyMinWidthPx = 100,
            policyMaxWidthPx = 600,
            safeViewportWidthPx = 1000
        )
        
        assertEquals(600, result.minWidth) // clamped to max
        assertEquals(600, result.maxWidth)
    }

    @Test
    fun testAnchorLargerThanViewport() {
        val result = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = true,
            anchorWidth = 1200,
            policyMinWidthPx = -1,
            policyMaxWidthPx = -1,
            safeViewportWidthPx = 1000
        )
        
        assertEquals(1000, result.minWidth) // clamped to viewport
        assertEquals(1000, result.maxWidth)
    }

    @Test
    fun testMinLargerThanMax() {
        val result = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = false,
            anchorWidth = 200,
            policyMinWidthPx = 800,
            policyMaxWidthPx = 600,
            safeViewportWidthPx = 1000
        )
        
        assertEquals(600, result.minWidth) // clamped to max
        assertEquals(600, result.maxWidth)
    }
}
