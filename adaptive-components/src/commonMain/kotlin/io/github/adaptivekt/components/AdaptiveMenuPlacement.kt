package io.github.adaptivekt.components

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Defines the placement strategy for an anchored menu relative to its anchor.
 */
public enum class AdaptiveMenuPlacement {
    BelowStart,
    BelowEnd,
    AboveStart,
    AboveEnd,
    Auto,
}

/**
 * Policy for configuring the dimensions and positioning of an anchored menu.
 */
public data class AdaptiveAnchoredMenuPolicy(
    val placement: AdaptiveMenuPlacement = AdaptiveMenuPlacement.Auto,
    val matchAnchorWidth: Boolean = true,
    val minWidth: Dp = Dp.Unspecified,
    val maxWidth: Dp = Dp.Unspecified,
    val maxHeight: Dp = 320.dp,
    val offset: DpOffset = DpOffset.Zero,
)

/**
 * Bounds of an anchor in window coordinates.
 */
public data class AdaptiveAnchorBounds(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    val width: Int get() = right - left
    val height: Int get() = bottom - top
}

/**
 * Dimensions of the viewport/window.
 */
public data class AdaptiveViewportBounds(
    val width: Int,
    val height: Int
)

/**
 * Dimensions of the menu to be placed.
 */
public data class AdaptiveMenuSize(
    val width: Int,
    val height: Int
)

/**
 * The calculated result for menu placement.
 */
public data class AdaptiveResolvedMenuPlacement(
    val x: Int,
    val y: Int,
    val maxHeight: Int
)

/**
 * Pure function to resolve the optimal placement of a menu relative to its anchor.
 * Ensures the menu fits within the viewport.
 */
public fun resolveAdaptiveMenuPlacement(
    anchor: AdaptiveAnchorBounds,
    viewport: AdaptiveViewportBounds,
    menuSize: AdaptiveMenuSize,
    placement: AdaptiveMenuPlacement,
    offsetX: Int = 0,
    offsetY: Int = 0,
    isRtl: Boolean = false,
    minWindowMargin: Int = 8
): AdaptiveResolvedMenuPlacement {
    val spaceAbove = anchor.top - minWindowMargin
    val spaceBelow = viewport.height - anchor.bottom - minWindowMargin
    
    // Determine vertical placement
    val preferAbove = when (placement) {
        AdaptiveMenuPlacement.AboveStart, AdaptiveMenuPlacement.AboveEnd -> true
        AdaptiveMenuPlacement.BelowStart, AdaptiveMenuPlacement.BelowEnd -> false
        AdaptiveMenuPlacement.Auto -> {
            if (spaceBelow >= menuSize.height) false // Fits below
            else if (spaceAbove >= menuSize.height) true // Fits above
            else spaceAbove > spaceBelow // Fallback to wherever there is more space
        }
    }
    
    val y = if (preferAbove) {
        anchor.top - menuSize.height - offsetY
    } else {
        anchor.bottom + offsetY
    }
    
    // Calculate max height based on available space
    val maxHeight = if (preferAbove) {
        maxOf(0, spaceAbove - offsetY)
    } else {
        maxOf(0, spaceBelow - offsetY)
    }
    
    // Determine horizontal placement
    val isStart = when (placement) {
        AdaptiveMenuPlacement.BelowStart, AdaptiveMenuPlacement.AboveStart -> true
        AdaptiveMenuPlacement.BelowEnd, AdaptiveMenuPlacement.AboveEnd -> false
        AdaptiveMenuPlacement.Auto -> true
    }
    
    val alignLeft = if (isRtl) !isStart else isStart
    
    var x = if (alignLeft) {
        anchor.left + offsetX
    } else {
        anchor.right - menuSize.width - offsetX
    }
    
    // Clamp X to viewport horizontally
    if (x < minWindowMargin) {
        x = minWindowMargin
    } else if (x + menuSize.width > viewport.width - minWindowMargin) {
        x = maxOf(minWindowMargin, viewport.width - minWindowMargin - menuSize.width)
    }
    
    var finalY = y
    // If above and it exceeds the top
    if (preferAbove && finalY < minWindowMargin) {
        finalY = minWindowMargin
    }
    // If below and it exceeds the bottom
    if (!preferAbove && finalY + menuSize.height > viewport.height - minWindowMargin) {
        // Just let it be, the maxHeight truncation handles the sizing
        // But we shouldn't push the popup offscreen. Actually popup positioning usually relies on its size.
        // We will return the top-left y.
    }
    
    return AdaptiveResolvedMenuPlacement(
        x = x,
        y = finalY,
        maxHeight = maxHeight
    )
}
