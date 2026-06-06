package io.github.adaptivekt.navigation

interface PlatformHistoryTracker {
    val initialPath: String?
    fun push(path: String)
    fun replace(path: String)
    fun onPopState(listener: (String) -> Unit)
}

expect fun getPlatformHistoryTracker(options: AdaptiveNavigationOptions): PlatformHistoryTracker?
