@file:JvmName("AdminDemoKt")

package io.github.adaptivekt.admin.demo

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.unit.dp

fun main(args: Array<String>) {
    val config = try {
        parseAdminDemoArgs(args)
    } catch (error: IllegalArgumentException) {
        System.err.println(error.message)
        kotlin.system.exitProcess(1)
    }

    if (config.captureMode) {
        runAdminDemoCaptureMode(config)
    } else {
        application {
            val windowState = rememberWindowState(
                width = config.width.dp,
                height = config.height.dp,
                position = WindowPosition(Alignment.Center),
            )

            Window(
                onCloseRequest = ::exitApplication,
                title = "AdaptiveKt Admin Demo",
                state = windowState,
            ) {
                AdminDemoApp(initialScreen = config.screen, initialDarkTheme = config.darkTheme)
            }
        }
    }
}
