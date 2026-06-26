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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.core.AdaptiveTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.clip

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun SiteNavigation(
    route: SiteRoute,
    darkTheme: Boolean,
    searchQuery: String,
    onThemeToggle: () -> Unit,
    onNavigate: (SiteRoute) -> Unit,
    onSearchChange: (String) -> Unit,
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
        Box(
            modifier = Modifier
                .clip(AdaptiveTheme.shapes.medium)
                .docsClickableCursor()
                .clickable { onNavigate(SiteRoute.Home) }
        ) {
            AdaptiveKtLogo(symbolSize = 34.dp, wordmarkSize = 18.sp)
        }
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
                    modifier = Modifier.docsClickableCursor()
                )
            }
            AdaptiveSearchField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = "Search components ( / )",
                onClear = { onSearchChange("") },
                modifier = Modifier.width(200.dp)
            )
            AdaptiveIconButton(
                onClick = onThemeToggle,
                size = 32.dp,
                modifier = Modifier.docsClickableCursor(),
                content = {
                    androidx.compose.foundation.Image(
                        imageVector = if (darkTheme) DocsIcons.Moon else DocsIcons.Sun,
                        contentDescription = if (darkTheme) "Switch to light theme" else "Switch to dark theme",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
            AdaptiveIconButton(
                onClick = { openSiteUrl("https://github.com/nikoG4/AdaptiveKt") },
                size = 32.dp,
                modifier = Modifier.docsClickableCursor(),
                content = {
                    androidx.compose.foundation.Image(
                        imageVector = DocsIcons.GitHub,
                        contentDescription = "GitHub",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}
