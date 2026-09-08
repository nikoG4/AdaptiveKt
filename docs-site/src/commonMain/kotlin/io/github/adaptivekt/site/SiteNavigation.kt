package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveAnchoredDropdownMenu
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveDropdownPlacement
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveMenuItem
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo
import io.github.adaptivekt.layout.AdaptiveActionBar

@Composable
internal fun SiteNavigation(
    route: SiteRoute,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onNavigate: (SiteRoute) -> Unit,
    onSearchClick: () -> Unit,
) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    val compact = layoutInfo.isCompact
    val collapseRoutes = layoutInfo.isCompact || layoutInfo.isMedium
    var routeMenuOpen by remember { mutableStateOf(false) }

    AdaptiveActionBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdaptiveTheme.colors.surface)
            .border(1.dp, SiteLine)
            .padding(
                horizontal = when {
                    compact -> 16.dp
                    layoutInfo.isMedium -> 20.dp
                    else -> 24.dp
                },
                vertical = 12.dp,
            ),
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(AdaptiveTheme.shapes.medium)
                    .docsClickableCursor()
                    .clickable { onNavigate(SiteRoute.Home) },
            ) {
                AdaptiveKtLogo(symbolSize = 34.dp, wordmarkSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(if (compact) 10.dp else 20.dp))
            AdaptiveButton(
                text = if (collapseRoutes) "Search…" else "Search documentation…   Ctrl K",
                size = AdaptiveButtonSize.Small,
                variant = AdaptiveButtonVariant.Secondary,
                onClick = onSearchClick,
                modifier = when {
                    compact -> Modifier.weight(1f).docsClickableCursor()
                    layoutInfo.isMedium -> Modifier.widthIn(min = 220.dp, max = 280.dp).docsClickableCursor()
                    else -> Modifier.widthIn(min = 300.dp, max = 460.dp).docsClickableCursor()
                },
            )
        },
        secondaryActions = {
            if (collapseRoutes) {
                AdaptiveAnchoredDropdownMenu(
                    expanded = routeMenuOpen,
                    onExpandedChange = { routeMenuOpen = it },
                    placement = AdaptiveDropdownPlacement.BottomStart,
                    anchor = { _, toggle ->
                        AdaptiveButton(
                            text = route.label,
                            size = AdaptiveButtonSize.Small,
                            variant = AdaptiveButtonVariant.Secondary,
                            onClick = toggle,
                            modifier = Modifier.docsClickableCursor(),
                        )
                    },
                ) {
                    SiteRoute.entries.forEach { item ->
                        AdaptiveMenuItem(
                            text = item.label,
                            onClick = {
                                routeMenuOpen = false
                                onNavigate(item)
                            },
                            enabled = item != route,
                        )
                    }
                }
            } else {
                SiteRoute.entries.forEach { item ->
                    AdaptiveButton(
                        text = item.label,
                        size = AdaptiveButtonSize.Small,
                        variant = if (item == route) AdaptiveButtonVariant.Primary else AdaptiveButtonVariant.Ghost,
                        onClick = { onNavigate(item) },
                        modifier = Modifier.docsClickableCursor(),
                    )
                }
            }
        },
        primaryAction = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AdaptiveIconButton(
                    onClick = onThemeToggle,
                    size = 32.dp,
                    modifier = Modifier.docsClickableCursor(),
                    content = {
                        androidx.compose.foundation.Image(
                            imageVector = if (darkTheme) DocsIcons.Moon else DocsIcons.Sun,
                            contentDescription = if (darkTheme) "Switch to light theme" else "Switch to dark theme",
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AdaptiveTheme.colors.textPrimary),
                            modifier = Modifier.size(18.dp),
                        )
                    },
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
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
            }
        },
    )
}
