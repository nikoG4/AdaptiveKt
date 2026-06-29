package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.AdaptiveTheme

internal class AdaptiveAnchoredPositionProvider(
    private val policy: AdaptiveAnchoredMenuPolicy,
    private val density: androidx.compose.ui.unit.Density,
    private val minWindowMargin: Int = 8
) : PopupPositionProvider {
    var lastResolvedPlacement: AdaptiveResolvedMenuPlacement? = null

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

        val placement = resolveAdaptiveMenuPlacement(
            anchor = anchor,
            viewport = viewport,
            menuSize = menu,
            placement = policy.placement,
            offsetX = offsetX,
            offsetY = offsetY,
            isRtl = isRtl,
            minWindowMargin = minWindowMargin
        )

        lastResolvedPlacement = placement
        return IntOffset(placement.x, placement.y)
    }
}

/**
 * A sophisticated anchored menu that supports virtualization, custom sizing policies, and robust placement.
 */
@Composable
public fun <T> AdaptiveAnchoredMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    modifier: Modifier = Modifier,
    policy: AdaptiveAnchoredMenuPolicy = AdaptiveAnchoredMenuPolicy(),
    lazyListState: LazyListState = rememberLazyListState(),
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    if (!expanded) return

    val density = LocalDensity.current
    val positionProvider = remember(policy, density) {
        AdaptiveAnchoredPositionProvider(policy, density)
    }

    Popup(
        onDismissRequest = onDismissRequest,
        popupPositionProvider = positionProvider,
        properties = PopupProperties(focusable = true)
    ) {
        val placement = positionProvider.lastResolvedPlacement
        val calculatedMaxHeight = placement?.maxHeight?.let { with(density) { it.toDp() } }
            ?: policy.maxHeight

        val finalMaxHeight = minOf(policy.maxHeight, calculatedMaxHeight)

        Box(
            modifier = modifier
                .widthIn(min = policy.minWidth, max = policy.maxWidth)
                .heightIn(max = finalMaxHeight)
                .background(
                    color = AdaptiveTheme.colors.surface,
                    shape = AdaptiveTheme.shapes.medium
                )
                .border(
                    width = 1.dp,
                    color = AdaptiveTheme.colors.outlineVariant,
                    shape = AdaptiveTheme.shapes.medium
                )
                .clip(AdaptiveTheme.shapes.medium)
        ) {
            LazyColumn(
                state = lazyListState
            ) {
                itemsIndexed(items) { index, item ->
                    itemContent(index, item)
                }
            }
        }
    }
}
