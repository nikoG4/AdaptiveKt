package io.github.adaptivekt.examples.communication

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.adaptivekt.examples.communication.state.CommunicationState
import io.github.adaptivekt.examples.communication.state.CommunicationRouteResolver
import io.github.adaptivekt.examples.communication.ui.CommunicationApp
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        val state = remember { CommunicationState() }

        LaunchedEffect(Unit) {
            // Initial load
            CommunicationRouteResolver.resolve(window.location.hash, state)
            
            // Listen for external hash changes
            window.addEventListener("hashchange", {
                CommunicationRouteResolver.resolve(window.location.hash, state)
            })
            
            // Sync state to hash and validation bridge
            snapshotFlow { CommunicationRouteResolver.generateHash(state) }.collect { hash ->
                if (window.location.hash != hash) {
                    window.location.hash = hash
                }
                
                // Expose validation bridge
                updateValidationBridge(hash)
            }
        }

        CommunicationApp(state)
    }
}

private fun updateValidationBridge(hash: String) {
    js("window.__adaptiveKtCommunicationRoute = hash")
}
