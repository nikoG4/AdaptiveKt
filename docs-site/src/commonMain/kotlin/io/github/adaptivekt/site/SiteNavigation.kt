package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun SiteNavigation(
    route: SiteRoute,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onNavigate: (SiteRoute) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdaptiveTheme.colors.surface)
            .border(1.dp, SiteLine)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AdaptiveKtLogo(symbolSize = 34.dp, wordmarkSize = 18.sp)
        Spacer(modifier = Modifier.width(16.dp))
        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            SiteRoute.entries.forEach { item ->
                AdaptiveButton(
                    text = item.label,
                    size = AdaptiveButtonSize.Small,
                    variant = if (item == route) AdaptiveButtonVariant.Primary else AdaptiveButtonVariant.Ghost,
                    onClick = { onNavigate(item) },
                )
            }
            AdaptiveButton(
                text = if (darkTheme) "Light" else "Dark",
                size = AdaptiveButtonSize.Small,
                variant = AdaptiveButtonVariant.Secondary,
                onClick = onThemeToggle,
            )
            AdaptiveButton(
                text = "GitHub",
                size = AdaptiveButtonSize.Small,
                variant = AdaptiveButtonVariant.Secondary,
                onClick = { openSiteUrl("https://github.com/adaptivekt/adaptive-kt") },
            )
        }
    }
}
