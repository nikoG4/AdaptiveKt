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
    var location by remember { mutableStateOf(initialSiteLocation()) }

    androidx.compose.runtime.DisposableEffect(Unit) {
        val cleanup = observeSiteLocation { newLocation ->
            location = newLocation
        }
        onDispose { cleanup() }
    }

    AdaptiveApp {
        AdaptiveTheme(
            colorScheme = if (location.darkTheme) AdaptiveColorSchemes.defaultDark() else AdaptiveColorSchemes.defaultLight(),
        ) {
            androidx.compose.runtime.CompositionLocalProvider(LocalSiteLocation provides location) {
            val updateLocation: (SiteLocation) -> Unit = { newLocation ->
                location = newLocation
                pushSiteLocation(newLocation)
            }
            SiteLayout(
                route = location.route,
                darkTheme = location.darkTheme,
                searchQuery = location.searchQuery ?: "",
                onNavigate = {
                    updateLocation(location.copy(route = it, selectedItemId = null, sectionId = null, searchQuery = null))
                },
                onNavigateLocation = { newLocation ->
                    updateLocation(newLocation.copy(darkTheme = location.darkTheme))
                },
                onThemeToggle = {
                    updateLocation(location.copy(darkTheme = !location.darkTheme))
                },
                onSearchChange = { newQuery ->
                    updateLocation(location.copy(searchQuery = newQuery))
                }
            ) {
                when (location.route) {
                    SiteRoute.Home -> SiteHomePage(
                        onOpenComponents = { updateLocation(location.copy(route = SiteRoute.Components, selectedItemId = null)) },
                        onOpenDocs = { updateLocation(location.copy(route = SiteRoute.Docs, selectedItemId = null)) },
                        onOpenDemo = { updateLocation(location.copy(route = SiteRoute.Demo, selectedItemId = null)) },
                    )
                    SiteRoute.Components -> SiteComponentsPage(
                        selectedHash = location.selectedItemId ?: "",
                        onSelectedHashChange = { updateLocation(location.copy(route = SiteRoute.Components, selectedItemId = it)) },
                        sectionId = location.sectionId,
                        onSectionChange = { updateLocation(location.copy(sectionId = it)) }
                    )
                    SiteRoute.Docs -> SiteDocsPage(
                        selectedHash = location.selectedItemId ?: "",
                        onSelectedHashChange = { updateLocation(location.copy(route = SiteRoute.Docs, selectedItemId = it)) },
                        sectionId = location.sectionId,
                        onSectionChange = { updateLocation(location.copy(sectionId = it)) }
                    )
                    SiteRoute.Demo -> SiteDemoPage()
                }
            }
            }
        }
    }
}

