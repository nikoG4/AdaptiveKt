package io.github.adaptivekt.site

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    ComposeViewport(viewportContainerId = "webApp") {
        AdaptiveKtSiteApp()
    }
}

internal actual fun openSiteUrl(url: String) {
    window.open(url, target = "_self")
}

internal actual fun initialSiteRoute(): SiteRoute {
    val path = window.location.pathname.trimEnd('/') + "/"
    return when {
        path.endsWith("/components/") -> SiteRoute.Components
        path.endsWith("/docs/") -> SiteRoute.Docs
        path.endsWith("/demo/") -> SiteRoute.Demo
        else -> SiteRoute.Home
    }
}

internal actual fun pushSiteRoute(route: SiteRoute) {
    val base = siteBasePath()
    val nextPath = if (route == SiteRoute.Home) {
        base
    } else {
        base + route.path.trim('/')
    }
    window.history.pushState(null, route.label, nextPath)
}

private fun siteBasePath(): String {
    val path = window.location.pathname
    listOf("/components/", "/docs/", "/demo/").forEach { suffix ->
        if (path.endsWith(suffix)) return path.removeSuffix(suffix) + "/"
    }
    return if (path.endsWith("/")) path else path.substringBeforeLast('/', missingDelimiterValue = "") + "/"
}
