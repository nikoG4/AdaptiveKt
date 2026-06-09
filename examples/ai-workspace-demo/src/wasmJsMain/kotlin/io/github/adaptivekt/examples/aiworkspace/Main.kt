package io.github.adaptivekt.examples.aiworkspace

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.adaptivekt.navigation.PlatformHistoryTracker
import kotlinx.browser.window
import org.w3c.dom.events.Event

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val historyTracker = object : PlatformHistoryTracker {
        override val initialPath: String?
            get() = window.location.hash

        override fun push(path: String) {
            window.location.hash = path
        }

        override fun replace(path: String) {
            window.history.replaceState(null, "", path)
        }

        override fun goBack() {
            window.history.back()
        }

        

        override fun onPopState(listener: (String) -> Unit) {
            val listener: (Event) -> Unit = {
                listener(window.location.hash)
            }
            window.addEventListener("hashchange", listener)
            
        }
    }

    ComposeViewport(viewportContainerId = "webApp") {
        AiWorkspaceApp(platformHistoryTracker = historyTracker)
    }
}
