package io.github.adaptivekt.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

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

/**
 * Returns 1 for forward movement, -1 for backward movement, and 0 when there is no movement.
 */
public fun carouselSlideDirection(
    previousIndex: Int,
    nextIndex: Int,
    itemCount: Int,
    loop: Boolean = true,
): Int {
    if (itemCount <= 1) return 0
    val previous = normalizeCarouselIndex(previousIndex, itemCount)
    val next = normalizeCarouselIndex(nextIndex, itemCount)
    if (previous == next) return 0
    if (loop && previous == itemCount - 1 && next == 0) return 1
    if (loop && previous == 0 && next == itemCount - 1) return -1
    return if (next > previous) 1 else -1
}

public enum class AdaptiveCarouselTransition {
    Slide,
    Fade,
    Scale,
    None,
}

@OptIn(ExperimentalAnimationApi::class)
/**
 * Controlled carousel component that displays a single item with optional transitions and indicators.
 *
 * @param items List of items to display.
 * @param selectedIndex The currently visible item index.
 * @param onSelectedIndexChange Callback invoked when the user navigates to a new index.
 * @param modifier Modifier applied to the root carousel container.
 * @param loop If true, navigation wraps around at the ends.
 * @param showControls If true, previous/next controls are rendered.
 * @param showIndicators If true, dot indicators are rendered.
 * @param transition Visual transition style between items.
 * @param emptyContent Optional composable rendered when the items list is empty.
 * @param itemContent Composable slot for rendering a single item.
 */
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
    transition: AdaptiveCarouselTransition = AdaptiveCarouselTransition.Slide,
    animationDurationMillis: Int = 240,
    emptyContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (item: T, index: Int) -> Unit,
) {
    val safeIndex = normalizeCarouselIndex(selectedIndex, items.size)
    val canNavigate = enabled && items.size > 1
    var previousSafeIndex by remember { mutableIntStateOf(safeIndex) }
    val slideDirection = carouselSlideDirection(previousSafeIndex, safeIndex, items.size, loop)

    LaunchedEffect(safeIndex) {
        previousSafeIndex = safeIndex
    }

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
                AnimatedContent(
                    targetState = safeIndex,
                    transitionSpec = {
                        carouselContentTransform(
                            transition = transition,
                            direction = slideDirection,
                            durationMillis = animationDurationMillis,
                        )
                    },
                    label = "AdaptiveCarouselContent",
                ) { index ->
                    itemContent(items[index], index)
                }
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

private fun carouselContentTransform(
    transition: AdaptiveCarouselTransition,
    direction: Int,
    durationMillis: Int,
): ContentTransform {
    val duration = durationMillis.coerceAtLeast(0)
    val animationSpec = tween<Float>(durationMillis = duration)
    return when (transition) {
        AdaptiveCarouselTransition.Slide -> {
            val resolvedDirection = if (direction == 0) 1 else direction
            slideInHorizontally(
                animationSpec = tween(durationMillis = duration),
                initialOffsetX = { width -> width * resolvedDirection },
            ) + fadeIn(animationSpec = animationSpec) togetherWith
                slideOutHorizontally(
                    animationSpec = tween(durationMillis = duration),
                    targetOffsetX = { width -> -width * resolvedDirection },
                ) + fadeOut(animationSpec = animationSpec)
        }
        AdaptiveCarouselTransition.Fade -> {
            fadeIn(animationSpec = animationSpec) togetherWith fadeOut(animationSpec = animationSpec)
        }
        AdaptiveCarouselTransition.Scale -> {
            scaleIn(initialScale = 0.96f, animationSpec = animationSpec) + fadeIn(animationSpec = animationSpec) togetherWith
                scaleOut(targetScale = 0.98f, animationSpec = animationSpec) + fadeOut(animationSpec = animationSpec)
        }
        AdaptiveCarouselTransition.None -> EnterTransition.None togetherWith ExitTransition.None
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
                    Modifier
                        .adaptiveInteractiveCursor()
                        .clickable(indication = null, interactionSource = null, onClick = onClick)
                } else {
                    Modifier
                },
            ),
    )
}
