package io.github.adaptivekt.core

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

public data class AdaptiveTypography(
    val title: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
) {
    public companion object {
        public fun default(): AdaptiveTypography = AdaptiveTypography(
            title = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            subtitle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            body = TextStyle(fontSize = 14.sp),
            bodySmall = TextStyle(fontSize = 13.sp),
            label = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
            caption = TextStyle(fontSize = 12.sp),
        )
    }
}
