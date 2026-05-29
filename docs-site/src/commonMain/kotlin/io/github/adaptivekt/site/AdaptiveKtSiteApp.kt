package io.github.adaptivekt.site

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
public fun AdaptiveKtSiteApp() {
    var route by remember { mutableStateOf(initialSiteRoute()) }

    SiteLayout(
        route = route,
        onNavigate = {
            route = it
            pushSiteRoute(it)
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
