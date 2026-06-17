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
fun CommunicationApp(state: CommunicationState = remember { CommunicationState() }) {
    
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
            icon = { io.github.adaptivekt.examples.communication.ui.icons.DemoIcon(io.github.adaptivekt.examples.communication.ui.icons.DemoIcons.Chat) }
        ),
        AdaptiveNavItem(
            id = "contacts",
            label = "Contacts",
            icon = { io.github.adaptivekt.examples.communication.ui.icons.DemoIcon(io.github.adaptivekt.examples.communication.ui.icons.DemoIcons.Contacts) }
        ),
        AdaptiveNavItem(
            id = "calls",
            label = "Calls",
            icon = { io.github.adaptivekt.examples.communication.ui.icons.DemoIcon(io.github.adaptivekt.examples.communication.ui.icons.DemoIcons.Call) }
        ),        AdaptiveNavItem(
            id = "settings",
            label = "Settings",
            icon = { io.github.adaptivekt.examples.communication.ui.icons.DemoIcon(io.github.adaptivekt.examples.communication.ui.icons.DemoIcons.Settings) }
        )
    )

    AdaptiveNavigationScaffold(
        navItems = navItems,
        preferBottomNavigationOnCompact = true,
        selectedItemId = when (state.activeArea) {
            AppArea.Chat -> "chat"
            AppArea.Contacts -> "contacts"
            AppArea.Calls -> "calls"
            AppArea.Settings -> "settings"
        },
        onItemSelected = { id ->
            when (id) {
                "chat" -> state.activeArea = AppArea.Chat
                "contacts" -> state.activeArea = AppArea.Contacts
                "calls" -> state.activeArea = AppArea.Calls
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
                AppArea.Contacts -> io.github.adaptivekt.examples.communication.ui.contacts.ContactsArea(state)
                AppArea.Calls -> io.github.adaptivekt.examples.communication.ui.calls.CallsArea(state)
                AppArea.Settings -> io.github.adaptivekt.examples.communication.ui.settings.SettingsArea(state)
            }
        }
    }
}
