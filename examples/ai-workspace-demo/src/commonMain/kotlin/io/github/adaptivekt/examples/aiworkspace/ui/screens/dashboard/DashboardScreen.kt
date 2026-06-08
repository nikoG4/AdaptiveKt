package io.github.adaptivekt.examples.aiworkspace.ui.screens.dashboard


// removed import
import androidx.compose.material3.*
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import androidx.compose.runtime.Composable
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore

import io.github.adaptivekt.examples.aiworkspace.ui.components.*
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun DashboardScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>) {
    AdaptiveScrollablePage {
        AdaptiveActionBar(leadingContent = { Text("AI Workspace", style = MaterialTheme.typography.titleLarge) },
            primaryAction = {
                Button(onClick = { navigator.navigate(AiRoute.Chats) }) {
                    AiGlyph("+")
                    Text("New chat")
                }
            }
        )

        AdaptiveSection(title = "Usage Overview") {
            AdaptiveGrid() {
                item { UsageMetricCard("Tokens This Week", "1.45M") }
                item { UsageMetricCard("Estimated Cost", "$14.50") }
                item { UsageMetricCard("Active Conversations", "124") }
                item { UsageMetricCard("Files Indexed", "45") }
                item { UsageMetricCard("Eval Pass Rate", "88%") }
                item { UsageMetricCard("Tools Enabled", "4") }
            }
        }

        AdaptiveSection(title = "Quick Actions") {
            AdaptiveGrid() {
                item { Card(onClick = { navigator.navigate(AiRoute.Playground) }) {
                    AiGlyph("PG")
                    Text("Open Playground", style = MaterialTheme.typography.titleMedium)
                    Text("Test models and prompts", style = MaterialTheme.typography.bodySmall)
                } }
                item { Card(onClick = { navigator.navigate(AiRoute.Prompts) }) {
                    AiGlyph("+")
                    Text("Create Prompt", style = MaterialTheme.typography.titleMedium)
                    Text("Add a new prompt template", style = MaterialTheme.typography.bodySmall)
                } }
            }
        }
    }
}
