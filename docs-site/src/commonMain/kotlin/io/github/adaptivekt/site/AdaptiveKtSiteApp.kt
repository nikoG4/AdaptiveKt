package io.github.adaptivekt.site

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.adaptivekt.core.AdaptiveColorSchemes
import io.github.adaptivekt.core.AdaptiveApp
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
public fun AdaptiveKtSiteApp() {
    var route by remember { mutableStateOf(initialSiteRoute()) }
    var hash by remember { mutableStateOf(initialSiteHash()) }
    var darkTheme by remember { mutableStateOf(initialSiteDarkTheme()) }

    androidx.compose.runtime.DisposableEffect(Unit) {
        val cleanup = observeHistory { newRoute, newHash ->
            route = newRoute
            hash = newHash
        }
        onDispose { cleanup() }
    }

    AdaptiveApp {
        AdaptiveTheme(
            colorScheme = if (darkTheme) AdaptiveColorSchemes.defaultDark() else AdaptiveColorSchemes.defaultLight(),
        ) {
            val navigateTo: (SiteRoute, String) -> Unit = { newRoute, newHash ->
                route = newRoute
                hash = newHash
                pushSiteRouteAndHash(newRoute, newHash, darkTheme)
            }
            SiteLayout(
                route = route,
                darkTheme = darkTheme,
                onNavigate = {
                    navigateTo(it, "")
                },
                onThemeToggle = {
                    val nextDarkTheme = !darkTheme
                    darkTheme = nextDarkTheme
                    pushSiteRouteAndHash(route, hash, nextDarkTheme)
                },
            ) {
                when (route) {
                    SiteRoute.Home -> SiteHomePage(
                        onOpenComponents = { navigateTo(SiteRoute.Components, "") },
                        onOpenDocs = { navigateTo(SiteRoute.Docs, "") },
                        onOpenDemo = { navigateTo(SiteRoute.Demo, "") },
                    )
                    SiteRoute.Components -> SiteComponentsPage(
                        selectedHash = hash,
                        onSelectedHashChange = { navigateTo(SiteRoute.Components, it) },
                    )
                    SiteRoute.Docs -> SiteDocsPage(
                        selectedHash = hash,
                        onSelectedHashChange = { navigateTo(SiteRoute.Docs, it) },
                    )
                    SiteRoute.Demo -> SiteDemoPage()
                }
            }
        }
    }
}
