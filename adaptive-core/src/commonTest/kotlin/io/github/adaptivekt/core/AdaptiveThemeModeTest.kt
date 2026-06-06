package io.github.adaptivekt.core

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveThemeModeTest {
    @Test
    fun `light mode resolves to false regardless of system`() {
        assertFalse(resolveAdaptiveThemeDarkMode(AdaptiveThemeMode.Light, systemIsDark = false))
        assertFalse(resolveAdaptiveThemeDarkMode(AdaptiveThemeMode.Light, systemIsDark = true))
    }

    @Test
    fun `dark mode resolves to true regardless of system`() {
        assertTrue(resolveAdaptiveThemeDarkMode(AdaptiveThemeMode.Dark, systemIsDark = false))
        assertTrue(resolveAdaptiveThemeDarkMode(AdaptiveThemeMode.Dark, systemIsDark = true))
    }

    @Test
    fun `system mode follows provided platform state`() {
        assertFalse(resolveAdaptiveThemeDarkMode(AdaptiveThemeMode.System, systemIsDark = false))
        assertTrue(resolveAdaptiveThemeDarkMode(AdaptiveThemeMode.System, systemIsDark = true))
    }
}
