package io.github.adaptivekt.examples.aiworkspace.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.model.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.*
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun ToolsScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>, selectedId: String?) {
    val selectedItem = store.tools.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Tools) },
        listPane = {
            AdaptivePage {
                AdaptiveActionBar(leadingContent = { Text("Tools", style = MaterialTheme.typography.titleLarge) })
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(store.tools) { tool ->
                        ToolListItem(
                            tool = tool,
                            isSelected = tool.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.ToolDetail(tool.id)) }
                        )
                    }
                }
            }
        },
        detailPane = { tool ->
            AdaptiveScrollablePage {
                AdaptiveActionBar(
                    leadingContent = { Text(tool.name, style = MaterialTheme.typography.titleLarge) },
                    primaryAction = {
                        AdaptiveButton(
                            text = if (tool.enabled) "Disable" else "Enable",
                            onClick = { store.toggleToolEnabled(tool.id) },
                            size = AdaptiveButtonSize.Small,
                            variant = if (tool.enabled) AdaptiveButtonVariant.Secondary else AdaptiveButtonVariant.Primary,
                        )
                    }
                )

                AdaptiveSection {
                    Text(tool.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Row {
                        StatusBadge(
                            status = if (tool.enabled) "Enabled" else "Disabled",
                            isSuccess = tool.enabled,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        StatusBadge(
                            status = "${tool.risk.name} Risk",
                            isSuccess = tool.risk == ToolRisk.Low,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        StatusBadge(
                            status = "Last Run: ${tool.lastRunStatus}",
                            isSuccess = tool.lastRunStatus == "Success"
                        )
                    }
                }

                AdaptiveSection(title = "Input Schema") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                    ) {
                        Text(
                            "{\n  \"type\": \"object\",\n  \"properties\": {\n    \"query\": { \"type\": \"string\" }\n  }\n}",
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                AdaptiveSection(title = "Output Schema") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                    ) {
                        Text(
                            "{\n  \"type\": \"object\",\n  \"properties\": {\n    \"results\": { \"type\": \"array\" }\n  }\n}",
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        emptyDetail = {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Select a tool to view details")
            }
        }
    )
}

@Composable
private fun ToolListItem(
    tool: AiTool,
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
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(tool.name, style = MaterialTheme.typography.titleMedium, color = contentColor)
                Spacer(Modifier.width(8.dp))
                if (!tool.enabled) {
                    Text("(Disabled)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                tool.description,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
        StatusBadge(
            status = tool.risk.name,
            isSuccess = tool.risk == ToolRisk.Low
        )
    }
}

