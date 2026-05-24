package io.github.adaptivekt.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.github.adaptivekt.core.AdaptiveTokens

internal object AdaptiveComponentDefaults {
    val Surface = Color.White
    val SurfaceSubtle = Color(0xFFF8FAFC)
    val Border = Color(0xFFE2E8F0)
    val BorderStrong = Color(0xFFCBD5E1)
    val Text = Color(0xFF0F172A)
    val MutedText = Color(0xFF64748B)
    val Primary = Color(0xFF2563EB)
    val PrimaryHover = Color(0xFF315FDC)
    val PrimaryPressed = Color(0xFF1D4ED8)
    val Danger = Color(0xFFB91C1C)

    val MediumShape: Shape = RoundedCornerShape(AdaptiveTokens.Radius.Medium)
    val LargeShape: Shape = RoundedCornerShape(AdaptiveTokens.Radius.Large)
    val PillShape: Shape = RoundedCornerShape(AdaptiveTokens.Radius.Pill)
}
