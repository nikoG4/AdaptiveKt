package io.github.adaptivekt.examples.aiworkspace.ui.screens.assistants

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.feedback.AdaptiveEmptyState
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
            AdaptivePaneList {
                AdaptiveActionBar(
                    leadingContent = {
                        Text(
                            text = "Assistants",
                            style = MaterialTheme.typography.titleLarge,
                            color = AdaptiveTheme.colors.textPrimary,
                        )
                    }
                )

                AdaptivePaneListGroup {
                    store.assistants.forEachIndexed { index, assistant ->
                        AssistantListItem(
                            assistant = assistant,
                            isSelected = assistant.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.AssistantDetail(assistant.id)) }
                        )
                        if (index < store.assistants.lastIndex) {
                            AdaptiveDivider()
                        }
                    }
                }
            }
        },
        detailPane = { assistant ->
            AdaptiveScrollablePage {
                AdaptiveActionBar(leadingContent = { Text("Configuration", style = MaterialTheme.typography.titleLarge) })

                AdaptiveSection {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AdaptiveAvatar(name = assistant.name, size = 64.dp)
                        Spacer(Modifier.width(AdaptiveTokens.Spacing.Medium))
                        Column {
                            Text(assistant.name, style = MaterialTheme.typography.headlineSmall, color = AdaptiveTheme.colors.textPrimary)
                            Text(assistant.description, style = MaterialTheme.typography.bodyMedium, color = AdaptiveTheme.colors.textSecondary)
                        }
                    }
                }

                AdaptiveSection(title = "Model Settings") {
                    AdaptiveGrid() {
                        item {
                            AdaptiveTextField(
                                value = assistant.model,
                                onValueChange = {},
                                label = "Model",
                                enabled = false,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            var temp by remember(assistant.temperature) { mutableStateOf(assistant.temperature.toString()) }
                            AdaptiveTextField(
                                value = temp,
                                onValueChange = { temp = it },
                                label = "Temperature",
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
            AdaptiveEmptyState(
                title = "Select an assistant",
                description = "Choose an agent from the list to review model settings, capabilities and tags.",
                modifier = Modifier.fillMaxSize(),
            )
        }
    )
}

@Composable
private fun CapabilityCard(title: String, value: String) {
    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = AdaptiveTheme.colors.textSecondary)
        Text(value, style = MaterialTheme.typography.titleLarge, color = AdaptiveTheme.colors.textPrimary)
    }
}

@Composable
private fun AssistantListItem(
    assistant: AssistantProfile,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    AdaptivePaneListItem(
        selected = isSelected,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AdaptiveAvatar(name = assistant.name, size = 40.dp)
            Spacer(Modifier.width(AdaptiveTokens.Spacing.Medium))
            Column(modifier = Modifier.weight(1f)) {
                Text(assistant.name, style = MaterialTheme.typography.titleMedium, color = AdaptiveTheme.colors.textPrimary)
                Text(
                    assistant.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AdaptiveTheme.colors.textSecondary,
                    maxLines = 1
                )
            }
            AdaptiveBadge(
                text = if (isSelected) "Open" else assistant.model.substringBefore("-"),
                tone = if (isSelected) AdaptiveBadgeTone.Info else AdaptiveBadgeTone.Neutral,
            )
        }
    }
}

