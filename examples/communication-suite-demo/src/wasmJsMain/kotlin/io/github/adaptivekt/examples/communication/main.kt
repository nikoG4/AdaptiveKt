package io.github.adaptivekt.examples.communication

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.adaptivekt.examples.communication.ui.CommunicationApp
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        CommunicationApp()
    }
}
