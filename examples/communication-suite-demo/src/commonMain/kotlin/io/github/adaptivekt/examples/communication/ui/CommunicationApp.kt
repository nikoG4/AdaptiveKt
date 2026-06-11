package io.github.adaptivekt.examples.communication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.adaptivekt.core.AdaptiveApp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveThemeMode
import io.github.adaptivekt.navigation.AdaptiveNavItem
import io.github.adaptivekt.navigation.AdaptiveNavigationScaffold
import io.github.adaptivekt.examples.communication.state.AppArea
import io.github.adaptivekt.examples.communication.state.CommunicationState
import androidx.compose.foundation.layout.padding

@Composable
fun CommunicationApp() {
    val state = remember { CommunicationState() }
    
    val themeMode = if (state.isDarkMode == true) AdaptiveThemeMode.Dark else if (state.isDarkMode == false) AdaptiveThemeMode.Light else AdaptiveThemeMode.System
    
    AdaptiveApp {
        AdaptiveTheme(mode = themeMode) {
            CommunicationShell(state)
        }
    }
}

@Composable
private fun CommunicationShell(state: CommunicationState) {
    val navItems = listOf(
        AdaptiveNavItem(
            id = "chat",
            label = "Chat",
            icon = { /* Use AdaptiveIcons.Chat if available, else a placeholder */ }
        ),
        AdaptiveNavItem(
            id = "mail",
            label = "Mail",
            icon = { /* Use AdaptiveIcons.Mail if available, else a placeholder */ }
        ),
        AdaptiveNavItem(
            id = "settings",
            label = "Settings",
            icon = { /* Use AdaptiveIcons.Settings if available */ }
        )
    )

    AdaptiveNavigationScaffold(
        navItems = navItems,
        selectedItemId = when (state.activeArea) {
            AppArea.Chat -> "chat"
            AppArea.Mail -> "mail"
            AppArea.Settings -> "settings"
        },
        onItemSelected = { id ->
            when (id) {
                "chat" -> state.activeArea = AppArea.Chat
                "mail" -> state.activeArea = AppArea.Mail
                "settings" -> state.activeArea = AppArea.Settings
            }
        },
        topBar = {
            io.github.adaptivekt.components.AdaptiveBadge("CommSuite", tone = io.github.adaptivekt.components.AdaptiveBadgeTone.Info)
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.padding(padding)) {
            when (state.activeArea) {
                AppArea.Chat -> io.github.adaptivekt.examples.communication.ui.chat.ChatArea(state)
                AppArea.Mail -> io.github.adaptivekt.examples.communication.ui.mail.MailArea(state)
                AppArea.Settings -> io.github.adaptivekt.examples.communication.ui.settings.SettingsArea(state)
            }
        }
    }
}
