package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.adaptivekt.core.AdaptiveTokens

public enum class AdaptiveDropdownPlacement {
    BottomStart,
    BottomEnd,
    TopStart,
    TopEnd
}

internal class AdaptiveDropdownPositionProvider(
    private val placement: AdaptiveDropdownPlacement
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        var x = when (placement) {
            AdaptiveDropdownPlacement.BottomStart, AdaptiveDropdownPlacement.TopStart -> anchorBounds.left
            AdaptiveDropdownPlacement.BottomEnd, AdaptiveDropdownPlacement.TopEnd -> anchorBounds.right - popupContentSize.width
        }

        var y = when (placement) {
            AdaptiveDropdownPlacement.BottomStart, AdaptiveDropdownPlacement.BottomEnd -> anchorBounds.bottom
            AdaptiveDropdownPlacement.TopStart, AdaptiveDropdownPlacement.TopEnd -> anchorBounds.top - popupContentSize.height
        }

        // Avoid horizontal overflow
        if (x + popupContentSize.width > windowSize.width) {
            x = windowSize.width - popupContentSize.width
        }
        if (x < 0) {
            x = 0
        }

        // Avoid vertical overflow
        if ((placement == AdaptiveDropdownPlacement.BottomStart || placement == AdaptiveDropdownPlacement.BottomEnd) &&
            y + popupContentSize.height > windowSize.height) {
            val spaceAbove = anchorBounds.top
            val spaceBelow = windowSize.height - anchorBounds.bottom
            if (spaceAbove > spaceBelow || y + popupContentSize.height > windowSize.height) {
                y = anchorBounds.top - popupContentSize.height
            }
        }

        if ((placement == AdaptiveDropdownPlacement.TopStart || placement == AdaptiveDropdownPlacement.TopEnd) &&
            y < 0) {
            val spaceAbove = anchorBounds.top
            val spaceBelow = windowSize.height - anchorBounds.bottom
            if (spaceBelow > spaceAbove || y < 0) {
                y = anchorBounds.bottom
            }
        }

        // Hard constraints
        if (y + popupContentSize.height > windowSize.height) {
            y = windowSize.height - popupContentSize.height
        }
        if (y < 0) {
            y = 0
        }

        return IntOffset(x, y)
    }
}

@Composable
@Suppress("UNUSED_PARAMETER")
public fun AdaptiveDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return

    val shape = AdaptiveComponentDefaults.LargeShape
    Column(
        modifier = modifier
            .widthIn(min = 180.dp, max = 280.dp)
            .clip(shape)
            .background(AdaptiveComponentDefaults.Surface, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape)
            .padding(AdaptiveTokens.Spacing.Small),
        content = content,
    )
}

/**
 * A popup-based dropdown menu that anchors to a provided trigger.
 */
@Composable
public fun AdaptiveAnchoredDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    matchAnchorWidth: Boolean = false,
    maxHeight: Dp = 320.dp,
    placement: AdaptiveDropdownPlacement = AdaptiveDropdownPlacement.BottomStart,
    anchor: @Composable (
        expanded: Boolean,
        toggle: () -> Unit
    ) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    var anchorSizeState by remember { mutableStateOf(IntSize.Zero) }
    val anchorSize = anchorSizeState

    Box(
        modifier = modifier.onGloballyPositioned {
            anchorSizeState = it.size
        }
    ) {
        anchor(expanded) {
            if (enabled) onExpandedChange(!expanded)
        }

        if (expanded) {
            Popup(
                popupPositionProvider = AdaptiveDropdownPositionProvider(placement),
                onDismissRequest = { onExpandedChange(false) },
                properties = PopupProperties(focusable = true)
            ) {
                val shape = AdaptiveComponentDefaults.LargeShape
                val menuModifier = if (matchAnchorWidth && anchorSize.width > 0) {
                    Modifier.width(with(density) { anchorSize.width.toDp() })
                } else {
                    Modifier.widthIn(min = 180.dp, max = 280.dp)
                }

                Column(
                    modifier = menuModifier
                        .heightIn(max = maxHeight)
                        .clip(shape)
                        .background(AdaptiveComponentDefaults.Surface, shape)
                        .border(1.dp, AdaptiveComponentDefaults.Border, shape)
                        .padding(AdaptiveTokens.Spacing.Small)
                        .verticalScroll(rememberScrollState()),
                    content = content
                )
            }
        }
    }
}
