package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

public fun normalizeCarouselIndex(index: Int, itemCount: Int): Int {
    if (itemCount <= 0) return 0
    return index.coerceIn(0, itemCount - 1)
}

public fun nextCarouselIndex(index: Int, itemCount: Int, loop: Boolean = true): Int {
    if (itemCount <= 0) return 0
    val normalized = normalizeCarouselIndex(index, itemCount)
    return when {
        normalized < itemCount - 1 -> normalized + 1
        loop -> 0
        else -> normalized
    }
}

public fun previousCarouselIndex(index: Int, itemCount: Int, loop: Boolean = true): Int {
    if (itemCount <= 0) return 0
    val normalized = normalizeCarouselIndex(index, itemCount)
    return when {
        normalized > 0 -> normalized - 1
        loop -> itemCount - 1
        else -> normalized
    }
}

@Composable
public fun <T> AdaptiveCarousel(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loop: Boolean = true,
    showControls: Boolean = true,
    showIndicators: Boolean = true,
    emptyContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (item: T, index: Int) -> Unit,
) {
    val safeIndex = normalizeCarouselIndex(selectedIndex, items.size)
    val canNavigate = enabled && items.size > 1

    AdaptiveCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(AdaptiveTokens.Spacing.Large),
    ) {
        if (items.isEmpty()) {
            if (emptyContent != null) {
                emptyContent()
            } else {
                BasicText(
                    text = "No items available",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AdaptiveTheme.colors.textMuted,
                    ),
                )
            }
            return@AdaptiveCard
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showControls) {
                AdaptiveIconButton(
                    onClick = {
                        onSelectedIndexChange(previousCarouselIndex(safeIndex, items.size, loop))
                    },
                    enabled = canNavigate && (loop || safeIndex > 0),
                ) {
                    AdaptiveIcons.ChevronLeft(
                        tint = if (canNavigate) AdaptiveTheme.colors.textSecondary else AdaptiveTheme.colors.disabledText,
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                itemContent(items[safeIndex], safeIndex)
            }

            if (showControls) {
                AdaptiveIconButton(
                    onClick = {
                        onSelectedIndexChange(nextCarouselIndex(safeIndex, items.size, loop))
                    },
                    enabled = canNavigate && (loop || safeIndex < items.lastIndex),
                ) {
                    AdaptiveIcons.ChevronRight(
                        tint = if (canNavigate) AdaptiveTheme.colors.textSecondary else AdaptiveTheme.colors.disabledText,
                    )
                }
            }
        }

        if (showIndicators && items.size > 1) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.indices.forEach { index ->
                    CarouselIndicator(
                        selected = index == safeIndex,
                        enabled = enabled,
                        onClick = { onSelectedIndexChange(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CarouselIndicator(
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = AdaptiveTheme.shapes.pill
    val background = if (selected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.surfaceMuted
    val border = if (selected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.borderStrong

    Box(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .size(width = if (selected) 18.dp else 8.dp, height = 8.dp)
            .clip(shape)
            .background(background, shape)
            .border(1.dp, border, shape)
            .then(
                if (enabled) {
                    Modifier.clickable(indication = null, interactionSource = null, onClick = onClick)
                } else {
                    Modifier
                },
            ),
    )
}
