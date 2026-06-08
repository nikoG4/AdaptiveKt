package io.github.adaptivekt.examples.aiworkspace

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Adaptive AI Workspace",
    ) {
        AiWorkspaceApp()
    }
}
