package io.github.adaptivekt.feedback

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

public enum class AdaptiveLoadingIndicatorStyle {
    Spinner,
    Dots,
    Pulse,
}

/**
 * Animated loading indicator with Foundation-only primitives.
 */
@Composable
internal fun SimpleLoadingIndicator(
    modifier: Modifier = Modifier,
    style: AdaptiveLoadingIndicatorStyle = AdaptiveLoadingIndicatorStyle.Spinner,
) {
    when (style) {
        AdaptiveLoadingIndicatorStyle.Spinner -> SpinnerLoadingIndicator(modifier)
        AdaptiveLoadingIndicatorStyle.Dots -> DotsLoadingIndicator(modifier)
        AdaptiveLoadingIndicatorStyle.Pulse -> PulseLoadingIndicator(modifier)
    }
}

@Composable
private fun SpinnerLoadingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "AdaptiveLoadingSpinner")
    val rotation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 900, easing = FastOutSlowInEasing)),
        label = "AdaptiveLoadingSpinnerRotation",
    )
    val trackColor = AdaptiveTheme.colors.primarySubtle
    val indicatorColor = AdaptiveTheme.colors.primary

    Canvas(modifier = modifier.size(52.dp)) {
        val strokeWidth = 4.dp.toPx()
        val inset = strokeWidth / 2f
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        drawArc(
            color = indicatorColor,
            startAngle = rotation.value,
            sweepAngle = 92f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
    }
}

@Composable
private fun DotsLoadingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(52.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(3) { index ->
            DotLoadingIndicator(delayMillis = index * 140)
        }
    }
}

@Composable
private fun DotLoadingIndicator(delayMillis: Int) {
    val transition = rememberInfiniteTransition(label = "AdaptiveLoadingDot")
    val scale = transition.animateFloat(
        initialValue = 0.64f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 520, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delayMillis),
        ),
        label = "AdaptiveLoadingDotScale",
    )
    Box(
        modifier = Modifier
            .size((10 * scale.value).dp)
            .background(AdaptiveTheme.colors.primary, CircleShape),
    )
}

@Composable
private fun PulseLoadingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "AdaptiveLoadingPulse")
    val pulse = transition.animateFloat(
        initialValue = 0.42f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 820, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "AdaptiveLoadingPulseScale",
    )
    val primary = AdaptiveTheme.colors.primary
    val subtle = AdaptiveTheme.colors.primarySubtle

    Box(
        modifier = modifier
            .size(52.dp)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size((36 * pulse.value).dp)
                .background(subtle, CircleShape),
        )
        Box(
            modifier = Modifier
                .size(13.dp)
                .background(primary, CircleShape),
        )
    }
}

/**
 * Displays a loading state with a progress indicator and optional message.
 *
 * @param modifier Optional modifier for root container.
 * @param message Optional text message to display below the progress indicator.
 * @param indicatorStyle Visual style for the animated default indicator.
 */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    message: String? = null,
    indicatorStyle: AdaptiveLoadingIndicatorStyle = AdaptiveLoadingIndicatorStyle.Spinner,
) {
    FeedbackStateLayout(modifier = modifier) {
        SimpleLoadingIndicator(style = indicatorStyle)

        if (message != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            SimpleText(
                text = message,
                fontSize = 14.sp,
                color = AdaptiveTheme.colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }
    }
}
