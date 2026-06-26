package io.github.adaptivekt.site

internal enum class SiteRoute(val label: String, val path: String) {
    Home("Home", "/"),
    Docs("Docs", "/docs/"),
    Components("Components", "/components/"),
    Demo("Demo", "/demo/"),
}

internal expect fun openSiteUrl(url: String)

internal expect fun initialSiteLocation(): SiteLocation

internal expect fun pushSiteLocation(location: SiteLocation)

internal expect fun observeSiteLocation(onLocationChange: (SiteLocation) -> Unit): () -> Unit
