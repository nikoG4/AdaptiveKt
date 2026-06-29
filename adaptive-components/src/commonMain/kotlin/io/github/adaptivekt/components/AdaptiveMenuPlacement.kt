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
    val windowMargin: Dp = 8.dp,
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
 * The calculated result for menu width constraints.
 */
public data class AdaptiveResolvedMenuWidth(
    val minWidth: Int,
    val maxWidth: Int
)

/**
 * Pure function to resolve the optimal placement of a menu relative to its anchor.
 * Ensures the menu fits within the viewport and respects window margins.
 * Negative margins are not permitted.
 */
public fun resolveAdaptiveMenuPlacement(
    anchor: AdaptiveAnchorBounds,
    viewport: AdaptiveViewportBounds,
    menuSize: AdaptiveMenuSize,
    placement: AdaptiveMenuPlacement,
    offsetX: Int = 0,
    offsetY: Int = 0,
    isRtl: Boolean = false,
    windowMarginPx: Int = 8
): AdaptiveResolvedMenuPlacement {
    require(windowMarginPx >= 0) { "windowMarginPx must be non-negative" }
    val margin = windowMarginPx
    
    val safeWidth = maxOf(0, viewport.width - margin * 2)
    val safeHeight = maxOf(0, viewport.height - margin * 2)

    val topLimit = margin
    val bottomLimit = maxOf(margin, viewport.height - margin)
    val leftLimit = margin
    val rightLimit = maxOf(margin, viewport.width - margin)

    // Normalize anchor so it's not inverted
    val normLeft = minOf(anchor.left, anchor.right)
    val normRight = maxOf(anchor.left, anchor.right)
    val normTop = minOf(anchor.top, anchor.bottom)
    val normBottom = maxOf(anchor.top, anchor.bottom)

    // Clamp anchor to viewport visually for available space calculations
    val anchorTop = normTop.coerceIn(0, viewport.height)
    val anchorBottom = normBottom.coerceIn(0, viewport.height)

    val spaceAbove = maxOf(0, anchorTop - margin)
    val spaceBelow = maxOf(0, bottomLimit - anchorBottom)

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
        normTop - menuSize.height - offsetY
    } else {
        normBottom + offsetY
    }
    
    // Calculate max height based on available space
    var maxHeight = if (preferAbove) {
        maxOf(0, normTop - offsetY - margin)
    } else {
        maxOf(0, bottomLimit - normBottom - offsetY)
    }
    maxHeight = minOf(maxHeight, safeHeight)
    
    // Determine horizontal placement
    val isStart = when (placement) {
        AdaptiveMenuPlacement.BelowStart, AdaptiveMenuPlacement.AboveStart, AdaptiveMenuPlacement.Auto -> true
        AdaptiveMenuPlacement.BelowEnd, AdaptiveMenuPlacement.AboveEnd -> false
    }
    
    val alignLeft = if (isRtl) !isStart else isStart
    
    var x = if (alignLeft) {
        normLeft + offsetX
    } else {
        normRight - menuSize.width - offsetX
    }
    
    // Strict clamp X to viewport bounds
    val actualWidth = minOf(menuSize.width, safeWidth)
    if (x + actualWidth > rightLimit) {
        x = rightLimit - actualWidth
    }
    if (x < leftLimit) {
        x = leftLimit
    }
    
    // Strict clamp Y to viewport bounds
    var finalY = y
    val actualHeight = minOf(menuSize.height, maxHeight)
    if (finalY + actualHeight > bottomLimit) {
        finalY = maxOf(topLimit, bottomLimit - actualHeight)
    }
    if (finalY < topLimit) {
        finalY = topLimit
    }
    
    return AdaptiveResolvedMenuPlacement(
        x = x,
        y = finalY,
        maxHeight = maxHeight
    )
}

/**
 * Pure function to resolve the sizing constraints of the anchored menu based on the policy and available space.
 */
public fun resolveAdaptiveAnchoredMenuWidth(
    matchAnchorWidth: Boolean,
    anchorWidth: Int,
    policyMinWidthPx: Int,
    policyMaxWidthPx: Int,
    safeViewportWidthPx: Int
): AdaptiveResolvedMenuWidth {
    require(safeViewportWidthPx >= 0) { "safeViewportWidthPx must be non-negative" }
    
    val baseMin = maxOf(0, policyMinWidthPx)
    val baseMax = if (policyMaxWidthPx > 0) policyMaxWidthPx else safeViewportWidthPx

    val finalMax = minOf(baseMax, safeViewportWidthPx)

    var finalMin = baseMin
    if (matchAnchorWidth) {
        val positiveAnchorWidth = maxOf(0, anchorWidth)
        finalMin = maxOf(baseMin, positiveAnchorWidth)
    }

    // Sanity clamp so min is never greater than max
    finalMin = minOf(finalMin, finalMax)

    return AdaptiveResolvedMenuWidth(
        minWidth = finalMin,
        maxWidth = finalMax
    )
}
