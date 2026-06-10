package io.github.adaptivekt.examples.aiworkspace.ui.screens.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.layout.AdaptiveTwoPane
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun PlaygroundScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>) {
    AdaptiveTwoPane(
        primaryWeight = 0.48f,
        secondaryWeight = 0.52f,
        primary = {
            AdaptiveScrollablePage(
                verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
            ) {
                AdaptiveActionBar(
                    leadingContent = {
                        Column {
                            Text(
                                text = "Playground",
                                style = MaterialTheme.typography.titleLarge,
                                color = AdaptiveTheme.colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Compare mock model responses through AdaptiveTwoPane.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AdaptiveTheme.colors.textSecondary,
                            )
                        }
                    },
                    secondaryActions = {
                        AdaptiveBadge(text = "${store.tools.count { it.enabled }} tools", tone = AdaptiveBadgeTone.Success)
                    },
                )

                var system by remember { mutableStateOf("You are a concise assistant for product teams.") }
                var prompt by remember { mutableStateOf("Summarize today's risk review and propose next actions.") }

                AdaptiveSection(title = "System instruction") {
                    AdaptiveTextField(
                        value = system,
                        onValueChange = { system = it },
                        placeholder = "Describe assistant behavior",
                    )
                }

                AdaptiveSection(title = "User prompt") {
                    AdaptiveTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        placeholder = "Ask the models to complete a task",
                    )
                    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                    AdaptiveButton(
                        text = "Run comparison",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        secondary = {
            AdaptiveScrollablePage(
                verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
            ) {
                AdaptiveActionBar(
                    leadingContent = {
                        Text(
                            text = "Results",
                            style = MaterialTheme.typography.titleLarge,
                            color = AdaptiveTheme.colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                )

                AdaptiveGrid(
                    horizontalGap = AdaptiveTokens.Spacing.Medium,
                    verticalGap = AdaptiveTokens.Spacing.Medium,
                ) {
                    item(span = 12) { ModelResultCard("Gemini 1.5 Pro", "Ready to process request.", AdaptiveBadgeTone.Success) }
                    item(span = 12) { ModelResultCard("GPT-4 Turbo", "Waiting for input...", AdaptiveBadgeTone.Info) }
                    item(span = 12) { ModelResultCard("Claude 3 Opus", "Standing by.", AdaptiveBadgeTone.Neutral) }
                }
            }
        },
    )
}

@Composable
private fun ModelResultCard(
    modelName: String,
    resultText: String,
    tone: AdaptiveBadgeTone,
) {
    AdaptiveCard(contentPadding = PaddingValues(AdaptiveTokens.Spacing.Large)) {
        AdaptiveBadge(text = "Mock", tone = tone)
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        Text(modelName, style = MaterialTheme.typography.titleMedium, color = AdaptiveTheme.colors.textPrimary)
        Text(resultText, style = MaterialTheme.typography.bodyMedium, color = AdaptiveTheme.colors.textSecondary)
    }
}
