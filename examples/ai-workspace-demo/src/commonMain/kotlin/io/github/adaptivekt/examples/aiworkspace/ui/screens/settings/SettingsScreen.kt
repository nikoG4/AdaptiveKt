package io.github.adaptivekt.examples.aiworkspace.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun SettingsScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>) {
    AdaptiveScrollablePage {
        AdaptiveActionBar(leadingContent = { Text("Settings", style = MaterialTheme.typography.titleLarge) })

        AdaptiveSection(title = "AI Providers") {
            AdaptiveGrid() {
                item { ProviderCard("OpenAI", "Configured", true) }
                item { ProviderCard("Anthropic", "API Key Required", false) }
                item { ProviderCard("Google (Gemini)", "Configured", true) }
                item { ProviderCard("Local Ollama", "Not Running", false) }
            }
        }

        AdaptiveSection(title = "Default Settings") {
            AdaptiveGrid() {
                item { OutlinedTextField(
                    value = store.settings.defaultModelId,
                    onValueChange = {},
                    label = { Text("Default Model") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                ) }
                item { OutlinedTextField(
                    value = store.settings.defaultTemperature.toString(),
                    onValueChange = {},
                    label = { Text("Default Temperature") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                ) }
            }
        }

        AdaptiveSection(title = "Preferences") {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Developer Mode", style = MaterialTheme.typography.titleMedium)
                    Text("Enable advanced logs and debugging tools.", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = store.settings.developerMode, onCheckedChange = { 
                    store.updateSettings(store.settings.copy(developerMode = it)) 
                })
            }
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Save Conversation History", style = MaterialTheme.typography.titleMedium)
                    Text("Store chat logs locally for future reference.", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = store.settings.saveHistory, onCheckedChange = { 
                    store.updateSettings(store.settings.copy(saveHistory = it)) 
                })
            }
        }
        
        AdaptiveSection(title = "Danger Zone") {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                AiGlyph("!")
                Spacer(Modifier.width(8.dp))
                Text("Clear All Data")
            }
        }
    }
}

@Composable
private fun ProviderCard(name: String, status: String, connected: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (connected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(status, style = MaterialTheme.typography.bodySmall)
        }
    }
}

