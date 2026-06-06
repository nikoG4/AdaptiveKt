package io.github.adaptivekt.core

public enum class AdaptiveThemeMode {
    System,
    Light,
    Dark,
}

public fun resolveAdaptiveThemeDarkMode(
    mode: AdaptiveThemeMode,
    systemIsDark: Boolean,
): Boolean = when (mode) {
    AdaptiveThemeMode.System -> systemIsDark
    AdaptiveThemeMode.Light -> false
    AdaptiveThemeMode.Dark -> true
}
