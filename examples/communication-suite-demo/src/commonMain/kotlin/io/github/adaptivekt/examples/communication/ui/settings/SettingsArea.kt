package io.github.adaptivekt.examples.communication.ui.settings

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
import io.github.adaptivekt.layout.AdaptiveSection

@Composable
fun SettingsArea(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Settings", fontWeight = FontWeight.Bold) }
        )
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            item {
                AdaptiveSection(title = "Appearance") {
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
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                AdaptiveSection(title = "Notifications") {
                    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                AdaptiveText("Enable Notifications")
                                AdaptiveButton("On", variant = AdaptiveButtonVariant.Secondary, onClick = {})
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                AdaptiveText("Sound")
                                AdaptiveButton("Default", variant = AdaptiveButtonVariant.Secondary, onClick = {})
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                AdaptiveSection(title = "Account") {
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
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                AdaptiveSection(title = "Data & Debugging") {
                    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            AdaptiveButton("Reset Demo Data", onClick = { /* Reload mock data */ }, variant = AdaptiveButtonVariant.Danger)
                            AdaptiveButton("Simulate Error State", onClick = { /* Mock error */ }, variant = AdaptiveButtonVariant.Ghost)
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
                AdaptiveButton("Sign Out", onClick = { /* Handle sign out */ }, variant = AdaptiveButtonVariant.Danger, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
}
