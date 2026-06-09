package io.github.adaptivekt.examples.aiworkspace.ui.screens.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun PlaygroundScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>) {
    AdaptiveTwoPane(
        primary = {
            AdaptiveScrollablePage {
                AdaptiveActionBar(leadingContent = { Text("Playground", style = MaterialTheme.typography.titleLarge) })
                
                AdaptiveSection(title = "System Instruction") {
                    var system by remember { mutableStateOf("You are a helpful assistant.") }
                    OutlinedTextField(
                        value = system,
                        onValueChange = { system = it },
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                }

                AdaptiveSection(title = "User Prompt") {
                    var prompt by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text("Run Comparison")
                    }
                }
            }
        },
        secondary = {
            AdaptiveScrollablePage {
                AdaptiveActionBar(leadingContent = { Text("Results", style = MaterialTheme.typography.titleLarge) })

                AdaptiveGrid() {
                    item { ModelResultCard("Gemini 1.5 Pro", "Ready to process request.") }
                    item { ModelResultCard("GPT-4 Turbo", "Waiting for input...") }
                    item { ModelResultCard("Claude 3 Opus", "Standing by.") }
                }
            }
        }
    )
}

@Composable
private fun ModelResultCard(modelName: String, resultText: String) {
    AdaptiveSection(title = modelName) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Text(resultText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

