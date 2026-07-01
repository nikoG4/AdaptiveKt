package io.github.adaptivekt.components.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * AdaptiveIcons provides minimal functional icons required by AdaptiveKt components.
 *
 * It is not a general-purpose icon pack. The set intentionally covers only component
 * affordances such as close, search, overflow, add, check, and chevron indicators.
 */
public object AdaptiveIcons {
    @Composable
    public fun Close(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.30f, side * 0.30f), Offset(side * 0.70f, side * 0.70f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.70f, side * 0.30f), Offset(side * 0.30f, side * 0.70f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun ChevronDown(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.28f, side * 0.40f), Offset(side * 0.50f, side * 0.62f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.50f, side * 0.62f), Offset(side * 0.72f, side * 0.40f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun ChevronRight(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.40f, side * 0.28f), Offset(side * 0.62f, side * 0.50f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.62f, side * 0.50f), Offset(side * 0.40f, side * 0.72f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun ChevronLeft(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.60f, side * 0.28f), Offset(side * 0.38f, side * 0.50f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.38f, side * 0.50f), Offset(side * 0.60f, side * 0.72f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun Plus(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.26f, side * 0.50f), Offset(side * 0.74f, side * 0.50f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.50f, side * 0.26f), Offset(side * 0.50f, side * 0.74f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun MoreVertical(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, _ ->
            val radius = side * 0.075f
            drawCircle(tint, radius, Offset(side * 0.50f, side * 0.30f))
            drawCircle(tint, radius, Offset(side * 0.50f, side * 0.50f))
            drawCircle(tint, radius, Offset(side * 0.50f, side * 0.70f))
        }
    }

    @Composable
    public fun Menu(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.24f, side * 0.32f), Offset(side * 0.76f, side * 0.32f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.24f, side * 0.50f), Offset(side * 0.76f, side * 0.50f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.24f, side * 0.68f), Offset(side * 0.76f, side * 0.68f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun Search(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawCircle(
                color = tint,
                radius = side * 0.22f,
                center = Offset(side * 0.44f, side * 0.44f),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            drawLine(tint, Offset(side * 0.60f, side * 0.60f), Offset(side * 0.76f, side * 0.76f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun Check(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            drawLine(tint, Offset(side * 0.26f, side * 0.52f), Offset(side * 0.42f, side * 0.68f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.42f, side * 0.68f), Offset(side * 0.75f, side * 0.32f), stroke, StrokeCap.Round)
        }
    }

    @Composable
    public fun ClearAll(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = Color(0xFF374151),
        contentDescription: String? = null,
    ) {
        FunctionalIcon(modifier, size, tint, contentDescription) { side, stroke ->
            // Simple X, similar to Close but distinct if needed, or just map to Close visual
            drawLine(tint, Offset(side * 0.30f, side * 0.30f), Offset(side * 0.70f, side * 0.70f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(side * 0.70f, side * 0.30f), Offset(side * 0.30f, side * 0.70f), stroke, StrokeCap.Round)
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun FunctionalIcon(
    modifier: Modifier,
    size: Dp,
    tint: Color,
    contentDescription: String?,
    draw: DrawScope.(side: Float, stroke: Float) -> Unit,
) {
    val semanticsModifier = if (contentDescription == null) {
        modifier
    } else {
        modifier.semantics {
            this.contentDescription = contentDescription
        }
    }

    Canvas(modifier = semanticsModifier.size(size)) {
        val side = min(this.size.width, this.size.height)
        val stroke = (side * 0.12f).coerceAtLeast(1.5f)
        draw(side, stroke)
    }
}
