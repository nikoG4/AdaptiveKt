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

internal actual fun initialSiteLocation(): SiteLocation {
    val path = window.location.pathname.trimEnd('/') + "/"
    val hash = window.location.hash
    val search = window.location.search
    return parseSiteLocation(path, hash, search, false)
}

internal actual fun pushSiteLocation(location: SiteLocation) {
    val base = siteBasePath()
    val serialized = serializeSiteLocation(location)

    val nextPath = if (location.route == SiteRoute.Home) {
        base + serialized.removePrefix("/")
    } else {
        base + serialized.removePrefix("/")
    }

    if (window.location.pathname + window.location.search + window.location.hash != nextPath) {
        window.history.pushState(null, location.route.label, nextPath)
    }
}

internal actual fun observeSiteLocation(onLocationChange: (SiteLocation) -> Unit): () -> Unit {
    val listener: (org.w3c.dom.events.Event) -> Unit = {
        onLocationChange(initialSiteLocation())
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
