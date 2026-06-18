package io.github.adaptivekt.examples.communication.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.*
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.communication.state.CommunicationState
import io.github.adaptivekt.examples.communication.model.PresenceStatus
import io.github.adaptivekt.forms.*
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.examples.communication.ui.icons.DemoIcons
import androidx.compose.material3.Icon

@Composable
fun SettingsArea(state: CommunicationState) {
    Box(modifier = Modifier.fillMaxSize()) {
        AdaptiveListDetailScaffold(
            selectedItem = if (state.settingsSection == "home") null else state.settingsSection,
            onBackToList = { state.settingsSection = "home" },
            listPane = {
                SettingsList(state = state)
            },
            detailPane = { section ->
                SettingsDetail(state = state, section = section)
            }
        )
    }
}

@Composable
fun SettingsList(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Settings", fontWeight = FontWeight.Bold) }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                AdaptiveSection(title = "Account") {
                    SettingsRow(
                        title = "Profile",
                        icon = DemoIcons.Person,
                        isSelected = state.settingsSection == "profile",
                        onClick = { state.settingsSection = "profile" }
                    )
                }
            }

            item {
                AdaptiveSection(title = "Preferences") {
                    SettingsRow(
                        title = "Appearance",
                        icon = DemoIcons.Settings,
                        isSelected = state.settingsSection == "appearance",
                        onClick = { state.settingsSection = "appearance" }
                    )
                    SettingsRow(
                        title = "Notifications",
                        icon = DemoIcons.Notifications,
                        isSelected = state.settingsSection == "notifications",
                        onClick = { state.settingsSection = "notifications" }
                    )
                    SettingsRow(
                        title = "Privacy",
                        icon = DemoIcons.Lock,
                        isSelected = state.settingsSection == "privacy",
                        onClick = { state.settingsSection = "privacy" }
                    )
                    SettingsRow(
                        title = "Data & Storage",
                        icon = DemoIcons.Settings,
                        isSelected = state.settingsSection == "data",
                        onClick = { state.settingsSection = "data" }
                    )
                }
            }

            item {
                AdaptiveSection(title = "Advanced") {
                    SettingsRow(
                        title = "Developer Options",
                        icon = DemoIcons.Build,
                        isSelected = state.settingsSection == "developer",
                        onClick = { state.settingsSection = "developer" }
                    )
                    SettingsRow(
                        title = "Help & Support",
                        icon = DemoIcons.Info,
                        isSelected = state.settingsSection == "help",
                        onClick = { state.settingsSection = "help" }
                    )
                }
            }

            item {
                AdaptiveButton("Sign Out", onClick = { state.demoState = "offline" }, variant = AdaptiveButtonVariant.Danger, modifier = Modifier.fillMaxWidth().padding(16.dp))
            }
        }
    }
}

@Composable
fun SettingsRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.background
    Box(
        modifier = Modifier.fillMaxWidth().background(bgColor).clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = AdaptiveTheme.colors.textMuted)
            Spacer(modifier = Modifier.width(16.dp))
            AdaptiveText(
                text = title,
                fontWeight = FontWeight.Medium,
                color = AdaptiveTheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(DemoIcons.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(20.dp), tint = AdaptiveTheme.colors.textMuted)
        }
    }
}

@Composable
fun SettingsDetail(state: CommunicationState, section: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        val title = when (section) {
            "profile" -> "Profile"
            "appearance" -> "Appearance"
            "notifications" -> "Notifications"
            "privacy" -> "Privacy"
            "data" -> "Data & Storage"
            "developer" -> "Developer Options"
            "help" -> "Help & Support"
            else -> "Settings"
        }

        AdaptiveActionBar(
            leadingContent = { AdaptiveText(title, fontWeight = FontWeight.Bold) }
        )

        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (section) {
                "profile" -> ProfileSettings(state)
                "appearance" -> AppearanceSettings(state)
                "notifications" -> NotificationsSettings(state)
                "privacy" -> PrivacySettings(state)
                "data" -> DataSettings(state)
                "developer" -> DeveloperSettings(state)
                "help" -> HelpSettings(state)
            }
        }
    }
}

@Composable
fun ProfileSettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AdaptiveTextField(
                value = state.currentUser.name,
                onValueChange = { state.currentUser = state.currentUser.copy(name = it) },
                label = "Display Name",
                enabled = true
            )

            AdaptiveSelect(
                options = PresenceStatus.entries,
                selectedOption = state.currentUser.status ?: PresenceStatus.Offline,
                onSelectedOptionChange = { status ->
                    state.currentUser = state.currentUser.copy(status = status ?: PresenceStatus.Offline)
                },
                label = "Status",
                optionLabel = { it.name }
            )
        }
    }
}

@Composable
fun AppearanceSettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AdaptiveSelect(
                options = listOf("System", "Light", "Dark"),
                selectedOption = when (state.isDarkMode) {
                    true -> "Dark"
                    false -> "Light"
                    null -> "System"
                },
                onSelectedOptionChange = {
                    state.isDarkMode = when (it) {
                        "Dark" -> true
                        "Light" -> false
                        else -> null
                    }
                },
                label = "Theme",
                optionLabel = { it }
            )

            AdaptiveSelect(
                options = listOf("Comfortable", "Compact"),
                selectedOption = if (state.useCompactDensity) "Compact" else "Comfortable",
                onSelectedOptionChange = {
                    state.useCompactDensity = it == "Compact"
                },
                label = "Density",
                optionLabel = { it }
            )
        }
    }
}

@Composable
fun NotificationsSettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                AdaptiveText("Push Notifications")
                AdaptiveButton("On", variant = AdaptiveButtonVariant.Secondary, onClick = { state.demoState = "offline" })
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                AdaptiveText("Sound")
                AdaptiveButton("Default", variant = AdaptiveButtonVariant.Secondary, onClick = { state.demoState = "offline" })
            }
        }
    }
}

@Composable
fun PrivacySettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                AdaptiveText("Read Receipts")
                AdaptiveButton("Enabled", variant = AdaptiveButtonVariant.Secondary, onClick = { state.demoState = "offline" })
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                AdaptiveText("Blocked Contacts")
                AdaptiveButton("Manage", variant = AdaptiveButtonVariant.Secondary, onClick = { state.activeArea = io.github.adaptivekt.examples.communication.state.AppArea.Contacts; state.contactsFilter = "blocked" })
            }
        }
    }
}

@Composable
fun DataSettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                AdaptiveText("Media Auto-Download")
                AdaptiveButton("Wi-Fi Only", variant = AdaptiveButtonVariant.Secondary, onClick = { state.demoState = "offline" })
            }
            AdaptiveButton("Clear Cache", variant = AdaptiveButtonVariant.Danger, onClick = { state.demoState = "offline" })
        }
    }
}

@Composable
fun DeveloperSettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AdaptiveSelect(
                options = listOf("Bottom Bar on Compact", "Drawer on Compact"),
                selectedOption = if (state.navigationBehavior) "Bottom Bar on Compact" else "Drawer on Compact",
                onSelectedOptionChange = {
                    state.navigationBehavior = it == "Bottom Bar on Compact"
                },
                label = "Navigation Scaffold Behavior",
                optionLabel = { it }
            )
            AdaptiveButton("Reset Demo Data", onClick = { state.demoState = "empty-chats" }, variant = AdaptiveButtonVariant.Danger)
            AdaptiveButton("Simulate Error State", onClick = { state.demoState = "error" }, variant = AdaptiveButtonVariant.Ghost)
        }
    }
}

@Composable
fun HelpSettings(state: CommunicationState) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AdaptiveButton("View FAQs", variant = AdaptiveButtonVariant.Secondary, onClick = { state.demoState = "offline" })
            AdaptiveButton("Contact Support", variant = AdaptiveButtonVariant.Secondary, onClick = { state.demoState = "offline" })
            AdaptiveText("App Version: 1.0.0-demo", color = AdaptiveTheme.colors.textMuted)
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
}
