package io.github.adaptivekt.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
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
import io.github.adaptivekt.core.AdaptiveContent
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo

private val NavigationSurfaceWidth: Dp = AdaptiveTokens.PaneWidths.Sidebar
private val NavigationRailWidth: Dp = AdaptiveTokens.PaneWidths.NavigationRail
private val TopBarHeight: Dp = AdaptiveTokens.Sizes.TopBarHeight
private val BottomNavigationHeight: Dp = AdaptiveTokens.Sizes.TopBarHeight + AdaptiveTokens.Spacing.Medium

@Composable
public fun AdaptiveNavigationScaffold(
    navItems: List<AdaptiveNavItem>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    preferBottomNavigationOnCompact: Boolean = false,
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    AdaptiveContent(modifier = modifier) {
        val adaptiveInfo = rememberAdaptiveInfo()
        val mode = navigationModeForBreakpoint(adaptiveInfo.breakpoint, preferBottomNavigationOnCompact)
        var drawerOpen by remember { mutableStateOf(false) }
        val showGlobalTopBar = mode == AdaptiveNavigationMode.Drawer ||
            (mode == AdaptiveNavigationMode.BottomNavigation && topBar != null)
        val showContentTopBar = topBar != null &&
            (mode == AdaptiveNavigationMode.NavigationRail || mode == AdaptiveNavigationMode.Sidebar)
        val contentPadding = PaddingValues(
            top = if (showGlobalTopBar || showContentTopBar) TopBarHeight else 0.dp,
            bottom = if (mode == AdaptiveNavigationMode.BottomNavigation) BottomNavigationHeight else 0.dp,
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (mode) {
                    AdaptiveNavigationMode.Drawer -> {
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
                                        onItemSelected = {
                                            onItemSelected(it)
                                            drawerOpen = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                    AdaptiveNavigationMode.BottomNavigation -> {
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
                                    onItemSelected = onItemSelected,
                                )
                            }
                        }
                    }
                    AdaptiveNavigationMode.NavigationRail -> {
                        Row(modifier = Modifier.fillMaxSize()) {
                            NavigationRail(
                                items = navItems,
                                selectedItemId = selectedItemId,
                                modifier = Modifier
                                    .width(NavigationRailWidth)
                                    .fillMaxHeight()
                                    .background(AdaptiveTheme.colors.surfaceMuted),
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
                    AdaptiveNavigationMode.Sidebar -> {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Sidebar(
                                items = navItems,
                                selectedItemId = selectedItemId,
                                modifier = Modifier
                                    .width(NavigationSurfaceWidth)
                                    .fillMaxHeight()
                                    .background(AdaptiveTheme.colors.surfaceMuted),
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
                }
            }

            if (showGlobalTopBar) {
                CompactTopBar(
                    drawerOpen = drawerOpen,
                    showMenu = mode == AdaptiveNavigationMode.Drawer,
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
            Box(
                modifier = Modifier
                    .size(AdaptiveTokens.Sizes.ButtonHeight)
                    .clickable(onClick = onMenuClick)
                    .background(AdaptiveTheme.colors.surfaceMuted, AdaptiveTheme.shapes.pill)
                    .border(1.dp, AdaptiveTheme.colors.borderStrong, AdaptiveTheme.shapes.pill),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = if (drawerOpen) "x" else "\u2630",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AdaptiveTheme.colors.primaryText,
                    ),
                    maxLines = 1,
                )
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
