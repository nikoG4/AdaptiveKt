package io.github.adaptivekt.navigation

import kotlinx.browser.window
import org.w3c.dom.events.Event

actual fun getPlatformHistoryTracker(options: AdaptiveNavigationOptions): PlatformHistoryTracker? {
    return WasmPlatformHistoryTracker(options)
}

class WasmPlatformHistoryTracker(private val options: AdaptiveNavigationOptions) : PlatformHistoryTracker {
    
    private var isNavigatingInternally = false

    override val initialPath: String?
        get() {
            return if (options.webMode == AdaptiveWebNavigationMode.Hash) {
                val hash = window.location.hash
                if (hash.startsWith("#")) hash.substring(1).ifEmpty { "/" } else "/"
            } else {
                val path = window.location.pathname
                val basePath = options.basePath ?: ""
                if (basePath.isNotEmpty() && path.startsWith(basePath)) {
                    path.substring(basePath.length).ifEmpty { "/" }
                } else {
                    path.ifEmpty { "/" }
                }
            }
        }

    override fun push(path: String) {
        isNavigatingInternally = true
        try {
            if (options.webMode == AdaptiveWebNavigationMode.Hash) {
                // To avoid triggering onhashchange infinitely when setting hash manually
                val currentHash = window.location.hash
                val newHash = if (path.startsWith("/")) "#$path" else "#/$path"
                if (currentHash != newHash) {
                    window.location.hash = newHash
                }
            } else {
                val basePath = options.basePath ?: ""
                val newPath = if (basePath.endsWith("/") && path.startsWith("/")) {
                    basePath + path.substring(1)
                } else {
                    basePath + path
                }
                window.history.pushState(null, "", newPath)
            }
        } finally {
            // Some browsers trigger hashchange asynchronously. We might need a small delay,
            // but for now we just rely on comparing state in navigator.
            isNavigatingInternally = false
        }
    }

    override fun replace(path: String) {
        isNavigatingInternally = true
        try {
            val newUrl = if (options.webMode == AdaptiveWebNavigationMode.Hash) {
                val newHash = if (path.startsWith("/")) "#$path" else "#/$path"
                window.location.pathname + window.location.search + newHash
            } else {
                val basePath = options.basePath ?: ""
                if (basePath.endsWith("/") && path.startsWith("/")) {
                    basePath + path.substring(1)
                } else {
                    basePath + path
                }
            }
            window.history.replaceState(null, "", newUrl)
        } finally {
            isNavigatingInternally = false
        }
    }

    override fun goBack() {
        isNavigatingInternally = true
        try {
            window.history.back()
        } finally {
            isNavigatingInternally = false
        }
    }

    override fun onPopState(listener: (String) -> Unit) {
        if (options.webMode == AdaptiveWebNavigationMode.Hash) {
            window.onhashchange = {
                if (!isNavigatingInternally) {
                    val hash = window.location.hash
                    val path = if (hash.startsWith("#")) hash.substring(1).ifEmpty { "/" } else "/"
                    listener(path)
                }
                null
            }
        } else {
            window.onpopstate = {
                if (!isNavigatingInternally) {
                    val path = window.location.pathname
                    val basePath = options.basePath ?: ""
                    val relativePath = if (basePath.isNotEmpty() && path.startsWith(basePath)) {
                        path.substring(basePath.length).ifEmpty { "/" }
                    } else {
                        path.ifEmpty { "/" }
                    }
                    listener(relativePath)
                }
                null
            }
        }
    }
}
