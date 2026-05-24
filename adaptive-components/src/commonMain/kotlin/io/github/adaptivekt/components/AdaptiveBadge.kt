package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

public enum class AdaptiveBadgeTone {
    Neutral,
    Success,
    Warning,
    Danger,
    Info,
}

@Composable
public fun AdaptiveBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: AdaptiveBadgeTone = AdaptiveBadgeTone.Neutral,
) {
    val colors = badgeColors(tone)
    val shape = AdaptiveComponentDefaults.PillShape

    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.background, shape)
            .border(1.dp, colors.border, shape)
            .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.XSmall),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.content,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private data class BadgeColors(
    val background: Color,
    val border: Color,
    val content: Color,
)

private fun badgeColors(tone: AdaptiveBadgeTone): BadgeColors = when (tone) {
    AdaptiveBadgeTone.Neutral -> BadgeColors(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFF334155))
    AdaptiveBadgeTone.Success -> BadgeColors(Color(0xFFECFDF5), Color(0xFFA7F3D0), Color(0xFF047857))
    AdaptiveBadgeTone.Warning -> BadgeColors(Color(0xFFFFF7ED), Color(0xFFFED7AA), Color(0xFFB45309))
    AdaptiveBadgeTone.Danger -> BadgeColors(Color(0xFFFEF2F2), Color(0xFFFECACA), Color(0xFFB91C1C))
    AdaptiveBadgeTone.Info -> BadgeColors(Color(0xFFEFF6FF), Color(0xFFBFDBFE), Color(0xFF1D4ED8))
}
