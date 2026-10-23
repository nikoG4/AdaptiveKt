package io.github.adaptivekt.core

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Modern is AdaptiveKt's polished beta design language.
 * Classic restores the previous alpha defaults.
 */
public enum class AdaptiveVisualStyle { Modern, Classic }

/** Consistent, Compose-native palette, shape and type presets. */
public object AdaptiveVisualPresets {
    public fun modernLight(): AdaptiveColorScheme = AdaptiveColorSchemes.defaultLight().copy(
        background = Color(0xFFF5F7FB),
        surface = Color(0xFFFFFFFF),
        surfaceRaised = Color(0xFFFFFFFF),
        surfaceMuted = Color(0xFFF7F9FC),
        border = Color(0xFFE5EAF2),
        borderStrong = Color(0xFFC8D3E2),
        textPrimary = Color(0xFF111B2D),
        textSecondary = Color(0xFF34445C),
        textMuted = Color(0xFF63758F),
        primary = Color(0xFF245FEA),
        primarySubtle = Color(0xFFEAF1FF),
        primaryText = Color(0xFF1746B2),
        focusRing = Color(0xFF245FEA),
        disabledBackground = Color(0xFFF0F3F8),
        disabledText = Color(0xFF8795AC),
    )

    public fun modernDark(): AdaptiveColorScheme = AdaptiveColorSchemes.defaultDark().copy(
        background = Color(0xFF0B1220),
        surface = Color(0xFF131D2F),
        surfaceRaised = Color(0xFF1A2940),
        surfaceMuted = Color(0xFF172339),
        border = Color(0xFF2A3B53),
        borderStrong = Color(0xFF48617E),
        textPrimary = Color(0xFFF4F7FD),
        textSecondary = Color(0xFFD1DCF0),
        textMuted = Color(0xFF9BACCA),
        textInverse = Color(0xFF09162C),
        primary = Color(0xFF82B4FF),
        primarySubtle = Color(0xFF1A365B),
        primaryText = Color(0xFFC7DEFF),
        focusRing = Color(0xFF82B4FF),
        disabledBackground = Color(0xFF253044),
        disabledText = Color(0xFF91A1B9),
    )

    public fun modernShapes(): AdaptiveShapeScheme = AdaptiveShapeScheme(
        small = RoundedCornerShape(9.dp),
        medium = RoundedCornerShape(13.dp),
        large = RoundedCornerShape(18.dp),
        pill = RoundedCornerShape(999.dp),
        circle = RoundedCornerShape(999.dp),
    )

    public fun modernTypography(): AdaptiveTypography = AdaptiveTypography(
        title = TextStyle(fontSize = 27.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold),
        subtitle = TextStyle(fontSize = 17.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
        body = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
        bodySmall = TextStyle(fontSize = 13.sp, lineHeight = 19.sp),
        label = TextStyle(fontSize = 12.sp, lineHeight = 17.sp, fontWeight = FontWeight.SemiBold),
        caption = TextStyle(fontSize = 12.sp, lineHeight = 17.sp),
    )

    public fun modernStates(): AdaptiveStateScheme =
        AdaptiveStateScheme.default().copy(focusBorderWidth = 2.dp)
}
