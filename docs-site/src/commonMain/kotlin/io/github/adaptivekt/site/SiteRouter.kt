package io.github.adaptivekt.site

internal enum class SiteRoute(val label: String, val path: String) {
    Home("Home", "/"),
    Docs("Docs", "/docs/"),
    Components("Components", "/components/"),
    Demo("Demo", "/demo/"),
}

internal expect fun openSiteUrl(url: String)

internal expect fun initialSiteRoute(): SiteRoute

internal expect fun initialSiteHash(): String

/** Reflects the last Compose-selected component into readable browser metadata for route smoke tests. */
internal expect fun publishRenderedComponentRoute(id: String, title: String)

internal expect fun initialSiteDarkTheme(): Boolean

internal expect fun pushSiteRouteAndHash(route: SiteRoute, hash: String, darkTheme: Boolean)

internal expect fun observeHistory(onHistoryChange: (SiteRoute, String) -> Unit): () -> Unit
