package io.github.adaptivekt.examples.aiworkspace.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.examples.aiworkspace.mock.AiMockData
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.navigation.AdaptiveNavigator
import kotlin.math.roundToInt

@Composable
public fun SettingsScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>) {
    val selectedModel = AiMockData.models.firstOrNull { it.id == store.settings.defaultModelId } ?: AiMockData.models.first()
    var monthlyLimit by remember { mutableStateOf("$120") }

    AdaptiveScrollablePage(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XLarge),
    ) {
        AdaptiveActionBar(
            leadingContent = {
                Column {
                    Text(
                        text = "Workspace settings",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Configure providers, model defaults, billing guardrails and team safety.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
            },
            secondaryActions = {
                AdaptiveBadge(text = "System theme", tone = AdaptiveBadgeTone.Info)
                AdaptiveBadge(text = "Alpha demo", tone = AdaptiveBadgeTone.Neutral)
            },
            primaryAction = {
                AdaptiveButton(
                    text = "Save changes",
                    onClick = {},
                    trailingIcon = { AdaptiveIcons.Check(size = 16.dp, tint = AdaptiveTheme.colors.textInverse) },
                )
            },
        )

        AdaptiveGrid(
            horizontalGap = AdaptiveTokens.Spacing.Large,
            verticalGap = AdaptiveTokens.Spacing.Large,
        ) {
            item(span = 5) {
                AdaptiveSection(title = "Providers", subtitle = "Demo-only connection states.") {
                    Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium)) {
                        ProviderCard("OpenAI", "Configured", true, "GPT-4 Turbo")
                        ProviderCard("Google Gemini", "Configured", true, "Gemini 1.5 Pro")
                        ProviderCard("Anthropic", "API key required", false, "Claude 3 Opus")
                        ProviderCard("Local Ollama", "Not running", false, "Local models")
                    }
                }
            }

            item(span = 7) {
                AdaptiveSection(title = "Model defaults", subtitle = "Fields use AdaptiveSelect and AdaptiveTextField.") {
                    AdaptiveCard {
                        AdaptiveSelect(
                            options = AiMockData.models,
                            selectedOption = selectedModel,
                            onSelectedOptionChange = { model ->
                                if (model != null) {
                                    store.updateSettings(store.settings.copy(defaultModelId = model.id))
                                }
                            },
                            optionLabel = { "${it.name} - ${it.provider}" },
                            label = "Default model",
                            clearable = false,
                            searchable = true,
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                        AdaptiveTextField(
                            value = temperatureLabel(store.settings.defaultTemperature),
                            onValueChange = { next ->
                                next.toFloatOrNull()?.let { temp ->
                                    store.updateSettings(store.settings.copy(defaultTemperature = temp))
                                }
                            },
                            label = "Default temperature",
                            placeholder = "0.7",
                            validationMessage = if (store.settings.defaultTemperature !in 0f..1f) "Use a value from 0.0 to 1.0" else null,
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                        AdaptiveTextField(
                            value = monthlyLimit,
                            onValueChange = { monthlyLimit = it },
                            label = "Monthly budget limit",
                            placeholder = "$120",
                        )
                    }
                }
            }

            item(span = 7) {
                AdaptiveSection(title = "Security and history", subtitle = "Responsive cards with controlled settings state.") {
                    AdaptiveCard {
                        SettingSwitchRow(
                            title = "Developer mode",
                            description = "Show advanced logs, tool traces and evaluation diagnostics.",
                            checked = store.settings.developerMode,
                            onCheckedChange = { store.updateSettings(store.settings.copy(developerMode = it)) },
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                        SettingSwitchRow(
                            title = "Save conversation history",
                            description = "Store mock chat logs locally for future sessions.",
                            checked = store.settings.saveHistory,
                            onCheckedChange = { store.updateSettings(store.settings.copy(saveHistory = it)) },
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                        SettingSwitchRow(
                            title = "Require tool approval",
                            description = "Preview of a future safety control for high-risk tools.",
                            checked = true,
                            onCheckedChange = {},
                        )
                    }
                }
            }

            item(span = 5) {
                AdaptiveSection(title = "Integrations", subtitle = "Status overview for AI workspace services.") {
                    AdaptiveCard {
                        IntegrationRow("Slack", "Notifications enabled", AdaptiveBadgeTone.Success)
                        IntegrationRow("GitHub", "Workflow tools available", AdaptiveBadgeTone.Success)
                        IntegrationRow("Jira", "Read-only mode", AdaptiveBadgeTone.Warning)
                        IntegrationRow("Warehouse", "Requires review", AdaptiveBadgeTone.Danger)
                    }
                }
            }

            item(span = 12) {
                AdaptiveSection(title = "Danger zone") {
                    AdaptiveCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
                        ) {
                            AiGlyph("!", modifier = Modifier)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Clear all demo data",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AdaptiveTheme.colors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "Disabled in this mock workspace, but styled through AdaptiveButton.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AdaptiveTheme.colors.textSecondary,
                                )
                            }
                            AdaptiveButton(
                                text = "Clear data",
                                onClick = {},
                                variant = AdaptiveButtonVariant.Danger,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun temperatureLabel(value: Float): String {
    val rounded = (value * 100).roundToInt() / 100f
    return rounded.toString()
}

@Composable
private fun ProviderCard(
    name: String,
    status: String,
    connected: Boolean,
    model: String,
) {
    AdaptiveCard(contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
        ) {
            AiGlyph(name.take(1), modifier = Modifier)
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, color = AdaptiveTheme.colors.textPrimary)
                Text(model, style = MaterialTheme.typography.bodySmall, color = AdaptiveTheme.colors.textSecondary)
            }
            AdaptiveBadge(
                text = status,
                tone = if (connected) AdaptiveBadgeTone.Success else AdaptiveBadgeTone.Warning,
            )
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = AdaptiveTheme.colors.textPrimary)
            Text(description, style = MaterialTheme.typography.bodySmall, color = AdaptiveTheme.colors.textSecondary)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun IntegrationRow(
    name: String,
    status: String,
    tone: AdaptiveBadgeTone,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = AdaptiveTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        AdaptiveBadge(text = name.take(2).uppercase(), tone = AdaptiveBadgeTone.Neutral)
        Column(modifier = Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.titleSmall, color = AdaptiveTheme.colors.textPrimary)
            Text(status, style = MaterialTheme.typography.bodySmall, color = AdaptiveTheme.colors.textSecondary)
        }
        AdaptiveBadge(text = status.substringBefore(" "), tone = tone)
    }
}
