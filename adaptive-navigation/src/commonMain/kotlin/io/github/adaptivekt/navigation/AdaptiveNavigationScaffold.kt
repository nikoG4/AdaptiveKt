package io.github.adaptivekt.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveContent
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo

private val NavigationSurfaceWidth: Dp = AdaptiveTokens.PaneWidths.Sidebar
private val NavigationRailWidth: Dp = AdaptiveTokens.PaneWidths.NavigationRail
private val TopBarHeight: Dp = AdaptiveTokens.Sizes.TopBarHeight
private val BottomNavigationHeight: Dp = AdaptiveTokens.Sizes.TopBarHeight + AdaptiveTokens.Spacing.Medium

/**
 * Responsive navigation shell that automatically switches between drawer, bottom bar, rail, and sidebar.
 *
 * @param navItems List of top-level navigation items.
 * @param selectedItemId The currently selected item ID.
 * @param onItemSelected Callback invoked when a navigation item is selected.
 * @param modifier Modifier applied to the root scaffold container.
 * @param preferBottomNavigationOnCompact Forces bottom navigation on compact screens if true.
 * @param navigationBehavior Optional responsive placement behavior. When null, the legacy
 * preferBottomNavigationOnCompact mapping is preserved.
 * @param navigationItemStyle Visual style for navigation items (Card, Pill, Minimal).
 * @param navigationDensity Padding density for navigation items.
 * @param topBar Optional composable slot for a top app bar.
 * @param content Screen content with applied scaffold padding.
 */
@Composable
public fun AdaptiveNavigationScaffold(
    navItems: List<AdaptiveNavItem>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    preferBottomNavigationOnCompact: Boolean = false,
    navigationBehavior: AdaptiveNavigationBehavior? = null,
    navigationItemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    navigationDensity: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    AdaptiveContent(modifier = modifier) {
        val adaptiveInfo = rememberAdaptiveInfo()
        val effectiveBehavior = navigationBehavior ?: if (preferBottomNavigationOnCompact) {
            AdaptiveNavigationDefaults.compactBottomBarBehavior()
        } else {
            AdaptiveNavigationDefaults.adminBehavior()
        }
        val placement = resolveAdaptiveNavigationPlacement(adaptiveInfo.breakpoint, effectiveBehavior)
        var drawerOpen by remember { mutableStateOf(false) }
        val showGlobalTopBar = placement == AdaptiveNavigationPlacement.Drawer ||
            (placement == AdaptiveNavigationPlacement.Hidden && topBar != null) ||
            (placement == AdaptiveNavigationPlacement.BottomBar && topBar != null)
        val showContentTopBar = topBar != null &&
            (placement == AdaptiveNavigationPlacement.Rail || placement == AdaptiveNavigationPlacement.Sidebar)
        val contentPadding = PaddingValues(
            top = if (showGlobalTopBar || showContentTopBar) TopBarHeight else 0.dp,
            bottom = if (placement == AdaptiveNavigationPlacement.BottomBar) BottomNavigationHeight else 0.dp,
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (placement) {
                    AdaptiveNavigationPlacement.Drawer -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                content(contentPadding)
                            }

                            if (drawerOpen) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(AdaptiveTheme.colors.overlay)
                                        .clickable { drawerOpen = false },
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(NavigationSurfaceWidth)
                                        .background(AdaptiveTheme.colors.surface)
                                ) {
                                    Drawer(
                                        items = navItems,
                                        selectedItemId = selectedItemId,
                                        modifier = Modifier.fillMaxSize(),
                                        itemStyle = navigationItemStyle,
                                        density = navigationDensity,
                                        onItemSelected = {
                                            onItemSelected(it)
                                            drawerOpen = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                    AdaptiveNavigationPlacement.BottomBar -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                content(contentPadding)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(AdaptiveTheme.colors.surfaceMuted),
                            ) {
                                BottomNavigation(
                                    items = navItems,
                                    selectedItemId = selectedItemId,
                                    modifier = Modifier.fillMaxWidth(),
                                    itemStyle = navigationItemStyle,
                                    density = navigationDensity,
                                    overflowBehavior = effectiveBehavior.overflowBehavior,
                                    visibleItemCount = effectiveBehavior.bottomBarVisibleItemCount,
                                    onItemSelected = onItemSelected,
                                )
                            }
                        }
                    }
                    AdaptiveNavigationPlacement.Rail -> {
                        Row(modifier = Modifier.fillMaxSize()) {
                            NavigationRail(
                                items = navItems,
                                selectedItemId = selectedItemId,
                                modifier = Modifier
                                    .width(NavigationRailWidth)
                                    .fillMaxHeight()
                                    .background(AdaptiveTheme.colors.surfaceMuted),
                                itemStyle = navigationItemStyle,
                                density = navigationDensity,
                                overflowBehavior = effectiveBehavior.overflowBehavior,
                                visibleItemCount = effectiveBehavior.railVisibleItemCount,
                                onItemSelected = onItemSelected,
                            )
                            Box(modifier = Modifier.fillMaxSize()) {
                                content(contentPadding)
                                if (showContentTopBar) {
                                    ContentTopBar(topBar = topBar)
                                }
                            }
                        }
                    }
                    AdaptiveNavigationPlacement.Sidebar -> {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Sidebar(
                                items = navItems,
                                selectedItemId = selectedItemId,
                                modifier = Modifier
                                    .width(NavigationSurfaceWidth)
                                    .fillMaxHeight()
                                    .background(AdaptiveTheme.colors.surfaceMuted),
                                itemStyle = navigationItemStyle,
                                density = navigationDensity,
                                onItemSelected = onItemSelected,
                            )
                            Box(modifier = Modifier.fillMaxSize()) {
                                content(contentPadding)
                                if (showContentTopBar) {
                                    ContentTopBar(topBar = topBar)
                                }
                            }
                        }
                    }
                    AdaptiveNavigationPlacement.Hidden -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            content(contentPadding)
                        }
                    }
                }
            }

            if (showGlobalTopBar) {
                CompactTopBar(
                    drawerOpen = drawerOpen,
                    showMenu = placement == AdaptiveNavigationPlacement.Drawer,
                    onMenuClick = { drawerOpen = !drawerOpen },
                    topBar = topBar,
                )
            }
        }
    }
}

@Composable
private fun ContentTopBar(
    topBar: (@Composable () -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopBarHeight)
            .background(AdaptiveTheme.colors.surface)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border)
            .padding(horizontal = AdaptiveTokens.Spacing.Large),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            topBar?.invoke()
        }
    }
}

@Composable
private fun CompactTopBar(
    drawerOpen: Boolean,
    showMenu: Boolean,
    onMenuClick: () -> Unit,
    topBar: (@Composable () -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopBarHeight)
            .background(AdaptiveTheme.colors.surface)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border)
            .padding(horizontal = AdaptiveTokens.Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showMenu) {
            AdaptiveIconButton(
                onClick = onMenuClick,
                size = AdaptiveTokens.Sizes.ButtonHeight,
            ) {
                if (drawerOpen) {
                    AdaptiveIcons.Close(tint = AdaptiveTheme.colors.primaryText, contentDescription = "Close navigation")
                } else {
                    AdaptiveIcons.Menu(tint = AdaptiveTheme.colors.primaryText, contentDescription = "Open navigation")
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(start = if (showMenu) AdaptiveTokens.Spacing.Medium else 0.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (topBar != null) {
                topBar()
            } else {
                BasicText(
                    text = "AdaptiveKt",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AdaptiveTheme.colors.textPrimary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
