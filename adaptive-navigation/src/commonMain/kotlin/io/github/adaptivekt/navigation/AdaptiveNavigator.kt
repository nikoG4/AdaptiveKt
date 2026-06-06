package io.github.adaptivekt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed interface AdaptiveRoute {
    val path: String
}

enum class AdaptiveWebNavigationMode {
    Hash,
    History
}

data class AdaptiveNavigationOptions(
    val webMode: AdaptiveWebNavigationMode = AdaptiveWebNavigationMode.Hash,
    val basePath: String? = null
)

interface AdaptiveRouteCodec<R> {
    fun encode(route: R): String
    fun decode(path: String): R?
}

interface AdaptiveNavigator<R> {
    val currentRoute: R
    val canGoBack: Boolean
    fun navigate(route: R)
    fun replace(route: R)
    fun goBack()
}

class DefaultAdaptiveNavigator<R>(
    initialRoute: R,
    private val codec: AdaptiveRouteCodec<R>,
    private val historyTracker: PlatformHistoryTracker?
) : AdaptiveNavigator<R> {

    private val backStack = mutableStateListOf<R>()
    
    override val currentRoute: R
        get() = backStack.last()

    override val canGoBack: Boolean
        get() = backStack.size > 1

    init {
        // Initialize from history tracker if available, otherwise use provided initialRoute
        val initialPath = historyTracker?.initialPath
        val routeToStart = if (!initialPath.isNullOrEmpty()) {
            codec.decode(initialPath) ?: initialRoute
        } else {
            initialRoute
        }
        backStack.add(routeToStart)
    }

    override fun navigate(route: R) {
        if (currentRoute != route) {
            backStack.add(route)
            historyTracker?.push(codec.encode(route))
        }
    }

    override fun replace(route: R) {
        if (backStack.isNotEmpty()) {
            backStack.removeLast()
        }
        backStack.add(route)
        historyTracker?.replace(codec.encode(route))
    }

    override fun goBack() {
        if (canGoBack) {
            backStack.removeLast()
            // The history tracker sync happens mostly onPopState, but if triggered programmatically
            // it doesn't automatically call the browser's back button. However, typically back()
            // is triggered BY the browser back button, which fires onPopState.
            // If called manually, we'd need historyTracker?.goBack(), but for simplicity
            // and avoiding infinite loops, we assume browser handles its own back state,
            // or we just reflect the new top state to the URL.
            historyTracker?.replace(codec.encode(currentRoute)) // Update URL to reflect new current state
        }
    }

    internal fun syncFromBrowser(path: String) {
        val decoded = codec.decode(path)
        if (decoded != null && decoded != currentRoute) {
            // Check if it's a back navigation
            if (backStack.size > 1 && backStack[backStack.size - 2] == decoded) {
                backStack.removeLast()
            } else {
                backStack.add(decoded)
            }
        }
    }
}

@Composable
fun <R> rememberAdaptiveNavigator(
    initialRoute: R,
    codec: AdaptiveRouteCodec<R>,
    options: AdaptiveNavigationOptions = AdaptiveNavigationOptions()
): AdaptiveNavigator<R> {
    val historyTracker = remember(options) { getPlatformHistoryTracker(options) }
    
    val navigator = remember(initialRoute, codec, historyTracker) {
        DefaultAdaptiveNavigator(initialRoute, codec, historyTracker)
    }

    DisposableEffect(historyTracker, navigator) {
        historyTracker?.onPopState { path ->
            navigator.syncFromBrowser(path)
        }
        onDispose { }
    }

    return navigator
}
