package io.github.adaptivekt.examples.aiworkspace.ui.screens.assistants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.model.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.*
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun AssistantsScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>, selectedId: String?) {
    val selectedItem = store.assistants.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Assistants) },
        listPane = {
            AdaptivePage {
                AdaptiveActionBar(leadingContent = { Text("Assistants", style = MaterialTheme.typography.titleLarge) })
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(store.assistants) { assistant ->
                        AssistantListItem(
                            assistant = assistant,
                            isSelected = assistant.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.AssistantDetail(assistant.id)) }
                        )
                    }
                }
            }
        },
        detailPane = { assistant ->
            AdaptiveScrollablePage {
                AdaptiveActionBar(leadingContent = { Text("Configuration", style = MaterialTheme.typography.titleLarge) })

                AdaptiveSection {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(assistant.avatarText, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(assistant.name, style = MaterialTheme.typography.headlineSmall)
                            Text(assistant.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                AdaptiveSection(title = "Model Settings") {
                    AdaptiveGrid() {
                        item {
                            OutlinedTextField(
                                value = assistant.model,
                                onValueChange = {},
                                label = { Text("Model") },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            var temp by remember(assistant.temperature) { mutableStateOf(assistant.temperature.toString()) }
                            OutlinedTextField(
                                value = temp,
                                onValueChange = { temp = it },
                                label = { Text("Temperature") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                AdaptiveSection(title = "Capabilities") {
                    AdaptiveGrid() {
                        item { CapabilityCard("Tools Enabled", assistant.toolsEnabled.toString()) }
                        item { CapabilityCard("Knowledge Files", assistant.knowledgeFiles.toString()) }
                    }
                }
                
                AdaptiveSection(title = "Tags") {
                    Row {
                        assistant.tags.forEach { tag ->
                            StatusBadge(tag, Modifier.padding(end = 8.dp), isSuccess = true)
                        }
                    }
                }
            }
        },
        emptyDetail = {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Select an assistant to configure")
            }
        }
    )
}

@Composable
private fun CapabilityCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun AssistantListItem(
    assistant: AssistantProfile,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(assistant.avatarText, color = contentColor, style = MaterialTheme.typography.labelLarge)
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(assistant.name, style = MaterialTheme.typography.titleMedium, color = contentColor)
            Text(
                assistant.description,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
    }
}

