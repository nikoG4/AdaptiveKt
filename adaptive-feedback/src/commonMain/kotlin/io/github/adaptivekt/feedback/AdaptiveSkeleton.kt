package io.github.adaptivekt.feedback

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTheme

/**
 * Animated neutral placeholder, suitable for any loading layout.
 *
 * Animation affects opacity only and can be disabled for deterministic tests.
 */
@Composable
public fun AdaptiveSkeleton(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    animated: Boolean = true,
) {
    val transition = rememberInfiniteTransition(label = "AdaptiveSkeleton")
    val pulse = transition.animateFloat(
        initialValue = 0.48f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "AdaptiveSkeletonOpacity",
    )
    Box(
        modifier = modifier
            .height(height)
            .alpha(if (animated) pulse.value else 0.7f)
            .background(AdaptiveTheme.colors.borderStrong, RoundedCornerShape(8.dp)),
    )
}

/** Ready-to-use skeleton for list rows; no consumer-specific business rules. */
@Composable
public fun AdaptiveSkeletonRow(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        AdaptiveSkeleton(modifier = Modifier.width(144.dp), height = 18.dp)
        Spacer(modifier = Modifier.height(10.dp))
        AdaptiveSkeleton(modifier = Modifier.fillMaxWidth(0.72f), height = 12.dp)
    }
}
