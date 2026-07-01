package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.adaptivekt.core.AdaptiveTheme
import kotlin.math.roundToInt

internal class AdaptivePureAnchoredPositionProvider(
    private val policy: AdaptiveAnchoredMenuPolicy,
    private val density: androidx.compose.ui.unit.Density,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val anchor = AdaptiveAnchorBounds(
            left = anchorBounds.left,
            top = anchorBounds.top,
            right = anchorBounds.right,
            bottom = anchorBounds.bottom
        )
        val viewport = AdaptiveViewportBounds(
            width = windowSize.width,
            height = windowSize.height
        )
        val menu = AdaptiveMenuSize(
            width = popupContentSize.width,
            height = popupContentSize.height
        )
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val offsetX = with(density) { policy.offset.x.roundToPx() }
        val offsetY = with(density) { policy.offset.y.roundToPx() }
        val windowMarginPx = with(density) { policy.windowMargin.roundToPx() }

        val placement = resolveAdaptiveMenuPlacement(
            anchor = anchor,
            viewport = viewport,
            menuSize = menu,
            placement = policy.placement,
            offsetX = offsetX,
            offsetY = offsetY,
            isRtl = isRtl,
            windowMarginPx = windowMarginPx
        )

        return IntOffset(placement.x, placement.y)
    }
}

/**
 * A sophisticated anchored menu that supports virtualization, custom sizing policies, stable keys, and robust placement.
 * Manages the anchor positioning automatically.
 *
 * @param expanded Whether the menu is currently visible.
 * @param onDismissRequest Called when the user dismisses the menu (e.g. clicking outside).
 * @param items The list of items to display.
 * @param anchor The composable that acts as the anchor for the menu placement.
 * @param itemKey Optional stable key provider for list items.
 * @param policy Sizing and placement policy.
 * @param lazyListState State of the internal LazyColumn.
 */
@Composable
public fun <T> AdaptiveAnchoredMenuBox(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    anchor: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    itemKey: ((T) -> Any)? = null,
    policy: AdaptiveAnchoredMenuPolicy = AdaptiveAnchoredMenuPolicy(),
    lazyListState: LazyListState = rememberLazyListState(),
    headerContent: (@Composable () -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    var anchorBounds by remember { mutableStateOf(IntRect.Zero) }

    Box(
        modifier = Modifier.onGloballyPositioned { coords ->
            val bounds = coords.boundsInWindow()
            val newBounds = IntRect(
                bounds.left.roundToInt(), 
                bounds.top.roundToInt(), 
                bounds.right.roundToInt(), 
                bounds.bottom.roundToInt()
            )
            if (anchorBounds != newBounds) {
                anchorBounds = newBounds
            }
        }
    ) {
        anchor()

        if (expanded) {
            val density = LocalDensity.current
            val windowInfo = LocalWindowInfo.current
            val positionProvider = remember(policy, density) {
                AdaptivePureAnchoredPositionProvider(policy, density)
            }

            val windowSize = windowInfo.containerSize
            
            // Calculate constraints purely based on state before Popup layout
            val windowMarginPx = with(density) { policy.windowMargin.roundToPx() }
            val offsetX = with(density) { policy.offset.x.roundToPx() }
            val offsetY = with(density) { policy.offset.y.roundToPx() }
            
            // We use a dummy menu size just to compute maxHeight available
            val placement = resolveAdaptiveMenuPlacement(
                anchor = AdaptiveAnchorBounds(anchorBounds.left, anchorBounds.top, anchorBounds.right, anchorBounds.bottom),
                viewport = AdaptiveViewportBounds(windowSize.width, windowSize.height),
                menuSize = AdaptiveMenuSize(0, 0),
                placement = policy.placement,
                offsetX = offsetX,
                offsetY = offsetY,
                isRtl = false, // Doesn't affect height available
                windowMarginPx = windowMarginPx
            )

            val policyMinWidthPx = if (policy.minWidth.isSpecified) with(density) { policy.minWidth.roundToPx() } else -1
            val policyMaxWidthPx = if (policy.maxWidth.isSpecified) with(density) { policy.maxWidth.roundToPx() } else -1
            val safeViewportWidthPx = maxOf(0, windowSize.width - windowMarginPx * 2)

            val widthConstraints = resolveAdaptiveAnchoredMenuWidth(
                matchAnchorWidth = policy.matchAnchorWidth,
                anchorWidth = anchorBounds.width,
                policyMinWidthPx = policyMinWidthPx,
                policyMaxWidthPx = policyMaxWidthPx,
                safeViewportWidthPx = safeViewportWidthPx
            )

            val finalMaxHeight = if (placement.maxHeight >= 0) {
                val calcDp = with(density) { placement.maxHeight.toDp() }
                minOf(policy.maxHeight, calcDp)
            } else {
                policy.maxHeight
            }
            
            val finalMinWidth = if (widthConstraints.minWidth >= 0) {
                with(density) { widthConstraints.minWidth.toDp() }
            } else {
                policy.minWidth
            }
            
            val finalMaxWidth = if (widthConstraints.maxWidth >= 0) {
                with(density) { widthConstraints.maxWidth.toDp() }
            } else {
                policy.maxWidth
            }

            Popup(
                onDismissRequest = onDismissRequest,
                popupPositionProvider = positionProvider,
                properties = PopupProperties(focusable = true)
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = modifier
                        .widthIn(min = finalMinWidth, max = finalMaxWidth)
                        .heightIn(max = finalMaxHeight)
                        .background(AdaptiveTheme.colors.surface, AdaptiveTheme.shapes.medium)
                        .border(1.dp, AdaptiveTheme.colors.border, AdaptiveTheme.shapes.medium)
                        .clip(AdaptiveTheme.shapes.medium)
                ) {
                    if (headerContent != null) {
                        headerContent()
                    }
                    
                    if (items.isEmpty() && emptyContent != null) {
                        emptyContent()
                    } else {
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            if (itemKey != null) {
                                itemsIndexed(
                                    items = items,
                                    key = { _, item -> itemKey(item) }
                                ) { index, item ->
                                    itemContent(index, item)
                                }
                            } else {
                                itemsIndexed(items) { index, item ->
                                    itemContent(index, item)
                                }
                            }
                        }
                    }
                    
                    if (footerContent != null) {
                        footerContent()
                    }
                }
            }
        }
    }
}
