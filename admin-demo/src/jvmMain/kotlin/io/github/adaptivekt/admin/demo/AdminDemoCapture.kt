package io.github.adaptivekt.admin.demo

import androidx.compose.ui.awt.ComposeWindow
import java.awt.EventQueue
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Window
import java.io.File
import javax.imageio.ImageIO

internal data class AdminDemoRunConfig(
    val captureMode: Boolean = false,
    val screen: AdminDemoScreen = AdminDemoScreen.Dashboard,
    val width: Int = 1200,
    val height: Int = 800,
    val output: String? = null,
    val delayMs: Long = 1200L,
    val accountMenuOpen: Boolean = false,
    val darkTheme: Boolean = false,
)

internal fun parseAdminDemoArgs(args: Array<String>): AdminDemoRunConfig {
    val tokens = args.flatMap { it.split(Regex("\\s+")) }.filter { it.isNotBlank() }
    var captureMode = false
    var screen: AdminDemoScreen = AdminDemoScreen.Dashboard
    var width = 1200
    var height = 800
    var output: String? = null
    var delayMs = 1200L
    var accountMenuOpen = false
    var darkTheme = false

    var index = 0
    while (index < tokens.size) {
        when (val token = tokens[index]) {
            "--capture" -> {
                captureMode = true
                index += 1
            }
            "--screen" -> {
                if (index + 1 >= tokens.size) {
                    throw IllegalArgumentException("Missing value for --screen")
                }
                screen = AdminDemoScreen.fromId(tokens[index + 1])
                index += 2
            }
            "--width" -> {
                if (index + 1 >= tokens.size) {
                    throw IllegalArgumentException("Missing value for --width")
                }
                width = tokens[index + 1].toIntOrNull() ?: throw IllegalArgumentException("Invalid width: ${tokens[index + 1]}")
                index += 2
            }
            "--height" -> {
                if (index + 1 >= tokens.size) {
                    throw IllegalArgumentException("Missing value for --height")
                }
                height = tokens[index + 1].toIntOrNull() ?: throw IllegalArgumentException("Invalid height: ${tokens[index + 1]}")
                index += 2
            }
            "--output" -> {
                if (index + 1 >= tokens.size) {
                    throw IllegalArgumentException("Missing value for --output")
                }
                output = tokens[index + 1]
                index += 2
            }
            "--delayMs" -> {
                if (index + 1 >= tokens.size) {
                    throw IllegalArgumentException("Missing value for --delayMs")
                }
                delayMs = tokens[index + 1].toLongOrNull() ?: throw IllegalArgumentException("Invalid delayMs: ${tokens[index + 1]}")
                index += 2
            }
            "--accountMenuOpen" -> {
                accountMenuOpen = true
                index += 1
            }
            "--theme" -> {
                if (index + 1 >= tokens.size) {
                    throw IllegalArgumentException("Missing value for --theme")
                }
                darkTheme = when (tokens[index + 1].lowercase()) {
                    "dark" -> true
                    "light" -> false
                    else -> throw IllegalArgumentException("Invalid theme: ${tokens[index + 1]}")
                }
                index += 2
            }
            "--help", "-h" -> {
                printAdminDemoHelp()
                kotlin.system.exitProcess(0)
            }
            else -> throw IllegalArgumentException("Unknown argument: $token")
        }
    }

    if (captureMode && output.isNullOrBlank()) {
        throw IllegalArgumentException("--output is required when --capture is enabled")
    }

    return AdminDemoRunConfig(
        captureMode = captureMode,
        screen = screen,
        width = width,
        height = height,
        output = output,
        delayMs = delayMs,
        accountMenuOpen = accountMenuOpen,
        darkTheme = darkTheme,
    )
}

private fun printAdminDemoHelp() {
    println("Admin Demo capture mode arguments:")
    println("  --capture")
    println("  --screen <dashboard|employees|products|invoices|settings|components|components-buttons|components-badges|components-avatars|components-cards|components-dropdowns|components-fields>")
    println("  --width <int>")
    println("  --height <int>")
    println("  --output <path>")
    println("  --delayMs <int optional, default 1200>")
    println("  --accountMenuOpen")
    println("  --theme <light|dark optional, default light>")
}

internal fun runAdminDemoCaptureMode(config: AdminDemoRunConfig): Nothing {
    val outputFile = File(requireNotNull(config.output))
    val title = "AdaptiveKt Admin Demo Capture - ${config.screen.id}"

    val windowHolder = arrayOfNulls<ComposeWindow>(1)
    EventQueue.invokeAndWait {
        windowHolder[0] = ComposeWindow().apply {
            this.title = title
            setSize(config.width, config.height)
            setLocation(100, 100)
        }
    }

    val window = requireNotNull(windowHolder[0])
    EventQueue.invokeAndWait {
        window.setContent {
            AdminDemoApp(
                initialScreen = config.screen,
                initialAccountMenuOpen = config.accountMenuOpen,
                initialDarkTheme = config.darkTheme,
            )
        }
        window.isAlwaysOnTop = true
        window.isVisible = true
        window.toFront()
        window.requestFocus()
    }

    try {
        println("captureMode=true")
        println("screen=${config.screen}")
        println("requested width=${config.width}, height=${config.height}")
        println("output=${outputFile.absolutePath}")
        println("theme=${if (config.darkTheme) "dark" else "light"}")
        println("window.title=${window.title}")
        println("window.isVisible=${window.isVisible}")
        println("window.isShowing=${window.isShowing}")
        println("window.locationOnScreen=${window.locationOnScreen}")
        println("window.size=${window.size}")
        println("window.bounds=${window.bounds}")

        if (!window.isVisible || !window.isShowing) {
            throw IllegalStateException("Capture window is not visible or not showing")
        }
        if (window.width <= 0 || window.height <= 0) {
            throw IllegalStateException("Capture window has invalid size: ${window.size}")
        }

        Thread.sleep(config.delayMs)
        EventQueue.invokeAndWait {
            window.isAlwaysOnTop = true
            window.toFront()
            window.requestFocus()
        }
        Thread.sleep(300)

        val location = window.locationOnScreen
        val size = window.size
        val captureBounds = Rectangle(location.x, location.y, size.width, size.height)
        println("capture bounds=$captureBounds")

        val robot = Robot()
        val image = robot.createScreenCapture(captureBounds)

        if (!outputFile.parentFile.exists()) {
            outputFile.parentFile.mkdirs()
        }
        ImageIO.write(image, "png", outputFile)

        if (!outputFile.exists()) {
            throw IllegalStateException("Capture file was not written: ${outputFile.absolutePath}")
        }

        println("Wrote capture to ${outputFile.absolutePath}")
        println("image.width=${image.width}, image.height=${image.height}")
        println("output.size=${outputFile.length()}")
        println("captured.window.title=${window.title}")
        println("captured.bounds=${captureBounds}")

        EventQueue.invokeAndWait {
            window.dispose()
        }
        kotlin.system.exitProcess(0)
    } catch (error: Throwable) {
        System.err.println("Capture failed: ${error.message}")
        error.printStackTrace(System.err)
        dumpVisibleWindows()
        EventQueue.invokeAndWait {
            window.dispose()
        }
        kotlin.system.exitProcess(1)
    }
}

private fun dumpVisibleWindows() {
    println("Visible windows diagnostic list:")
    Window.getWindows().forEach {
        val title = if (it is java.awt.Frame) it.title else it.name
        println("Window: title=${title}, visible=${it.isVisible}, showing=${it.isShowing}, bounds=${it.bounds}")
    }
}
