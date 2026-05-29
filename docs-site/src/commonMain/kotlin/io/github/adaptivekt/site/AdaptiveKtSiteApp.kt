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
        SiteLayout(
            route = route,
            darkTheme = darkTheme,
            onNavigate = {
                route = it
                pushSiteRoute(it, darkTheme)
            },
            onThemeToggle = {
                val nextDarkTheme = !darkTheme
                darkTheme = nextDarkTheme
                pushSiteRoute(route, nextDarkTheme)
            },
        ) {
            when (route) {
                SiteRoute.Home -> SiteHomePage(
                    onOpenComponents = { route = SiteRoute.Components },
                    onOpenDocs = { route = SiteRoute.Docs },
                    onOpenDemo = { route = SiteRoute.Demo },
                )
                SiteRoute.Components -> SiteComponentsPage()
                SiteRoute.Docs -> SiteDocsPage()
                SiteRoute.Demo -> SiteDemoPage()
            }
        }
    }
}
