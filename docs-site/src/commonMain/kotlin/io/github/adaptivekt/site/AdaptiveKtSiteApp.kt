package io.github.adaptivekt.site

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.adaptivekt.core.AdaptiveColorSchemes
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
public fun AdaptiveKtSiteApp() {
    var route by remember { mutableStateOf(initialSiteRoute()) }
    var darkTheme by remember { mutableStateOf(initialSiteDarkTheme()) }

    AdaptiveTheme(
        colorScheme = if (darkTheme) AdaptiveColorSchemes.defaultDark() else AdaptiveColorSchemes.defaultLight(),
    ) {
        val navigateTo: (SiteRoute) -> Unit = {
            route = it
            pushSiteRoute(it, darkTheme)
        }
        SiteLayout(
            route = route,
            darkTheme = darkTheme,
            onNavigate = {
                navigateTo(it)
            },
            onThemeToggle = {
                val nextDarkTheme = !darkTheme
                darkTheme = nextDarkTheme
                pushSiteRoute(route, nextDarkTheme)
            },
        ) {
            when (route) {
                SiteRoute.Home -> SiteHomePage(
                    onOpenComponents = { navigateTo(SiteRoute.Components) },
                    onOpenDocs = { navigateTo(SiteRoute.Docs) },
                    onOpenDemo = { navigateTo(SiteRoute.Demo) },
                )
                SiteRoute.Components -> SiteComponentsPage()
                SiteRoute.Docs -> SiteDocsPage()
                SiteRoute.Demo -> SiteDemoPage()
            }
        }
    }
}
