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
import io.github.adaptivekt.core.AdaptiveColorScheme
import io.github.adaptivekt.core.AdaptiveTheme
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
    val colors = badgeColors(tone, AdaptiveTheme.colors)
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

private fun badgeColors(tone: AdaptiveBadgeTone, colorScheme: AdaptiveColorScheme): BadgeColors = when (tone) {
    AdaptiveBadgeTone.Neutral -> BadgeColors(colorScheme.surfaceMuted, colorScheme.border, colorScheme.textSecondary)
    AdaptiveBadgeTone.Success -> BadgeColors(colorScheme.successSubtle, colorScheme.success, colorScheme.successText)
    AdaptiveBadgeTone.Warning -> BadgeColors(colorScheme.warningSubtle, colorScheme.warning, colorScheme.warningText)
    AdaptiveBadgeTone.Danger -> BadgeColors(colorScheme.dangerSubtle, colorScheme.danger, colorScheme.dangerText)
    AdaptiveBadgeTone.Info -> BadgeColors(colorScheme.infoSubtle, colorScheme.info, colorScheme.infoText)
}
