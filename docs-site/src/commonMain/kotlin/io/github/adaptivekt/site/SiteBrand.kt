package io.github.adaptivekt.site

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
internal fun AdaptiveKtLogo(
    modifier: Modifier = Modifier,
    symbolSize: Dp = 36.dp,
    wordmarkSize: TextUnit = 20.sp,
    compact: Boolean = false,
    tagline: Boolean = false,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AdaptiveKtSymbol(modifier = Modifier.size(symbolSize))
        if (!compact) {
            Spacer(modifier = Modifier.width(symbolSize * 0.28f))
            BasicText(
                text = "AdaptiveKt",
                style = TextStyle(
                    color = SiteInk,
                    fontSize = wordmarkSize,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                ),
                maxLines = 1,
            )
            if (tagline) {
                Spacer(modifier = Modifier.width(10.dp))
                BasicText(
                    text = "Compose Multiplatform UI",
                    style = TextStyle(
                        color = SiteMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.sp,
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun AdaptiveKtSymbol(modifier: Modifier = Modifier) {
    val primary = AdaptiveTheme.colors.primary
    val info = AdaptiveTheme.colors.info
    val success = AdaptiveTheme.colors.success

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val radius = w * 0.25f
        val strokeWidth = w * 0.088f

        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(primary, info, success),
                start = Offset(w * 0.12f, h * 0.10f),
                end = Offset(w * 0.88f, h * 0.90f),
            ),
            cornerRadius = CornerRadius(radius, radius),
        )
        drawRoundRect(
            color = Color.White.copy(alpha = 0.18f),
            topLeft = Offset(w * 0.22f, h * 0.22f),
            size = Size(w * 0.19f, h * 0.40f),
            cornerRadius = CornerRadius(w * 0.07f, w * 0.07f),
        )
        drawRoundRect(
            color = Color.White.copy(alpha = 0.26f),
            topLeft = Offset(w * 0.61f, h * 0.22f),
            size = Size(w * 0.18f, h * 0.18f),
            cornerRadius = CornerRadius(w * 0.06f, w * 0.06f),
        )
        drawRoundRect(
            color = Color.White.copy(alpha = 0.20f),
            topLeft = Offset(w * 0.61f, h * 0.61f),
            size = Size(w * 0.18f, h * 0.18f),
            cornerRadius = CornerRadius(w * 0.06f, w * 0.06f),
        )
        drawLine(
            color = Color.White,
            start = Offset(w * 0.29f, h * 0.76f),
            end = Offset(w * 0.50f, h * 0.25f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.White,
            start = Offset(w * 0.50f, h * 0.25f),
            end = Offset(w * 0.72f, h * 0.76f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.White,
            start = Offset(w * 0.39f, h * 0.54f),
            end = Offset(w * 0.61f, h * 0.54f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.92f),
            radius = w * 0.055f,
            center = Offset(w * 0.78f, h * 0.31f),
            style = Stroke(width = w * 0.018f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}
