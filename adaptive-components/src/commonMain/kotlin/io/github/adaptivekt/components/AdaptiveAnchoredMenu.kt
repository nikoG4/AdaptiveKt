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

internal class AdaptiveAnchoredPositionProvider(
    private val policy: AdaptiveAnchoredMenuPolicy,
    private val density: androidx.compose.ui.unit.Density,
) : PopupPositionProvider {

    var resolvedMaxHeightPx by mutableStateOf(-1)
    var resolvedMinWidthPx by mutableStateOf(-1)
    var resolvedMaxWidthPx by mutableStateOf(-1)

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

        val policyMinWidthPx = if (policy.minWidth.isSpecified) with(density) { policy.minWidth.roundToPx() } else -1
        val policyMaxWidthPx = if (policy.maxWidth.isSpecified) with(density) { policy.maxWidth.roundToPx() } else -1
        val safeViewportWidthPx = maxOf(0, windowSize.width - windowMarginPx * 2)

        val widthConstraints = resolveAdaptiveAnchoredMenuWidth(
            matchAnchorWidth = policy.matchAnchorWidth,
            anchorWidth = anchor.width,
            policyMinWidthPx = policyMinWidthPx,
            policyMaxWidthPx = policyMaxWidthPx,
            safeViewportWidthPx = safeViewportWidthPx
        )

        // Update state to trigger recomposition if constraints change
        if (resolvedMaxHeightPx != placement.maxHeight) {
            resolvedMaxHeightPx = placement.maxHeight
        }
        if (resolvedMinWidthPx != widthConstraints.minWidth) {
            resolvedMinWidthPx = widthConstraints.minWidth
        }
        if (resolvedMaxWidthPx != widthConstraints.maxWidth) {
            resolvedMaxWidthPx = widthConstraints.maxWidth
        }

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
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    // We wrap the anchor in a Box just to attach `onGloballyPositioned` if needed, 
    // but actually Popup doesn't need us to pass anchorBounds manually. 
    // Popup in Compose attaches to its parent in the composition tree!
    Box {
        anchor()

        if (expanded) {
            val density = LocalDensity.current
            val positionProvider = remember(policy, density) {
                AdaptiveAnchoredPositionProvider(policy, density)
            }

            Popup(
                onDismissRequest = onDismissRequest,
                popupPositionProvider = positionProvider,
                properties = PopupProperties(focusable = true)
            ) {
                // Read resolved constraints. Initial pass may be -1, meaning we use defaults.
                val finalMaxHeight = if (positionProvider.resolvedMaxHeightPx >= 0) {
                    val calcDp = with(density) { positionProvider.resolvedMaxHeightPx.toDp() }
                    minOf(policy.maxHeight, calcDp)
                } else {
                    policy.maxHeight
                }

                val finalMinWidth = if (positionProvider.resolvedMinWidthPx >= 0) {
                    with(density) { positionProvider.resolvedMinWidthPx.toDp() }
                } else {
                    policy.minWidth
                }

                val finalMaxWidth = if (positionProvider.resolvedMaxWidthPx >= 0) {
                    with(density) { positionProvider.resolvedMaxWidthPx.toDp() }
                } else {
                    policy.maxWidth
                }

                Box(
                    modifier = modifier
                        .widthIn(min = finalMinWidth, max = finalMaxWidth)
                        .heightIn(max = finalMaxHeight)
                        .background(
                            color = AdaptiveTheme.colors.surface,
                            shape = AdaptiveTheme.shapes.medium
                        )
                        .border(
                            width = 1.dp,
                            color = AdaptiveTheme.colors.border,
                            shape = AdaptiveTheme.shapes.medium
                        )
                        .clip(AdaptiveTheme.shapes.medium)
                ) {
                    LazyColumn(
                        state = lazyListState
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
            }
        }
    }
}
