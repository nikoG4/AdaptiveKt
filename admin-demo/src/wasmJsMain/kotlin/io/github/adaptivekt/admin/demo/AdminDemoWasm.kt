package io.github.adaptivekt.admin.demo

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    val params = window.location.search.removePrefix("?").split("&")
    val screenParam = params.find { it.startsWith("screen=") }?.substringAfter("=")
    val darkTheme = params.any { it == "theme=dark" }
    // The query parameter makes visual regression captures deterministic.
    val modernStyle = params.none { it == "style=classic" }
    val initialScreen = AdminDemoScreen.fromId(screenParam)

    ComposeViewport(viewportContainerId = "webApp") {
        AdminDemoApp(
            initialScreen = initialScreen,
            initialDarkTheme = darkTheme,
            contentScrollEnabled = true,
            initialModernStyle = modernStyle,
        )
    }
}
