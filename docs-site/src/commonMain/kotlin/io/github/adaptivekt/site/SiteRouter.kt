package io.github.adaptivekt.site

internal enum class SiteRoute(val label: String, val path: String) {
    Home("Home", "/"),
    Components("Components", "/components/"),
    Docs("Docs", "/docs/"),
    Demo("Demo", "/demo/"),
}

internal expect fun openSiteUrl(url: String)

internal expect fun initialSiteRoute(): SiteRoute

internal expect fun pushSiteRoute(route: SiteRoute)
