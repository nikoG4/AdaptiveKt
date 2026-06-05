package io.github.adaptivekt.site

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    ComposeViewport(viewportContainerId = "webApp") {
        AdaptiveKtSiteApp()
    }
}

internal actual fun openSiteUrl(url: String) {
    if (url.startsWith("http")) {
        window.open(url, target = "_self")
    } else {
        window.open(siteBasePath() + url.removePrefix("/"), target = "_self")
    }
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

internal actual fun initialSiteDarkTheme(): Boolean {
    return window.location.search
        .removePrefix("?")
        .split("&")
        .any { it == "theme=dark" }
}

internal actual fun initialSiteHash(): String {
    return window.location.hash.removePrefix("#")
}

internal actual fun pushSiteRouteAndHash(route: SiteRoute, hash: String, darkTheme: Boolean) {
    val base = siteBasePath()
    val nextPathBase = if (route == SiteRoute.Home) {
        base
    } else {
        base + route.path.trim('/') + "/"
    }
    val query = if (darkTheme) "?theme=dark" else ""
    val hashPart = if (hash.isNotEmpty()) "#$hash" else ""
    val nextPath = "$nextPathBase$query$hashPart"
    
    // Only push if the path actually changed to avoid stacking identical states
    if (window.location.pathname + window.location.search + window.location.hash != nextPath) {
        window.history.pushState(null, route.label, nextPath)
    }
}

internal actual fun observeHistory(onHistoryChange: (SiteRoute, String) -> Unit): () -> Unit {
    val listener: (org.w3c.dom.events.Event) -> Unit = {
        onHistoryChange(initialSiteRoute(), initialSiteHash())
    }
    window.addEventListener("popstate", listener)
    return {
        window.removeEventListener("popstate", listener)
    }
}

internal actual fun Modifier.docsClickableCursor(enabled: Boolean): Modifier {
    return if (enabled) this.pointerHoverIcon(PointerIcon.Hand) else this
}

private fun siteBasePath(): String {
    val path = window.location.pathname.trimEnd('/') + "/"
    listOf("/components/", "/docs/", "/demo/").forEach { suffix ->
        if (path.endsWith(suffix)) return path.removeSuffix(suffix) + "/"
    }
    return path
}
