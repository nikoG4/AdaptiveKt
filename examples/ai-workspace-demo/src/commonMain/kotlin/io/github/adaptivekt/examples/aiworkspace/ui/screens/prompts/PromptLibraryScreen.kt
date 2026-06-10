package io.github.adaptivekt.examples.aiworkspace.ui.screens.prompts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveTabs
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.data.AdaptiveCollectionDisplayMode
import io.github.adaptivekt.data.AdaptiveCollectionView
import io.github.adaptivekt.examples.aiworkspace.model.PromptTemplate
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold
import io.github.adaptivekt.layout.AdaptivePaneList
import io.github.adaptivekt.layout.AdaptivePaneListItem
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.navigation.AdaptiveNavigator

private enum class PromptDetailTab {
    Overview,
    Variables,
    Runs,
    Notes,
}

@Composable
public fun PromptLibraryScreen(
    store: AiWorkspaceStore,
    navigator: AdaptiveNavigator<AiRoute>,
    selectedId: String?,
) {
    val categories = remember(store.prompts) {
        listOf("All") + store.prompts.map { it.category }.distinct().sorted()
    }
    var selectedCategory by remember { mutableStateOf("All") }
    val visiblePrompts = remember(store.prompts, selectedCategory) {
        if (selectedCategory == "All") store.prompts else store.prompts.filter { it.category == selectedCategory }
    }
    val selectedItem = store.prompts.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Prompts) },
        listPane = {
            PromptBrowserPane(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategoryChange = { selectedCategory = it ?: "All" },
                prompts = visiblePrompts,
                selectedId = selectedId,
                onOpenPrompt = { navigator.navigate(AiRoute.PromptDetail(it.id)) },
            )
        },
        detailPane = { prompt ->
            PromptDetailPane(
                prompt = prompt,
                onToggleFavorite = { store.togglePromptFavorite(prompt.id) },
                onOpenPlayground = { navigator.navigate(AiRoute.Playground) },
            )
        },
        emptyDetail = {
            AdaptiveEmptyState(
                title = "Choose a prompt",
                description = "Select a reusable workflow template to inspect variables, recent runs and notes.",
                icon = {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(AdaptiveTheme.shapes.large)
                            .background(AdaptiveTheme.colors.infoSubtle),
                        contentAlignment = Alignment.Center,
                    ) {
                        AiGlyph("P", modifier = Modifier.size(40.dp))
                    }
                },
                action = {
                    AdaptiveButton(
                        text = "Open playground",
                        onClick = { navigator.navigate(AiRoute.Playground) },
                        variant = AdaptiveButtonVariant.Secondary,
                    )
                },
            )
        },
        compactDetailHeader = { prompt ->
            AdaptiveActionBar(
                leadingContent = {
                    Column {
                        Text(prompt.title, style = MaterialTheme.typography.titleMedium, color = AdaptiveTheme.colors.textPrimary)
                        Text("Prompt detail", style = MaterialTheme.typography.labelSmall, color = AdaptiveTheme.colors.textMuted)
                    }
                },
                secondaryActions = {
                    AdaptiveButton(
                        text = "Back",
                        onClick = { navigator.navigate(AiRoute.Prompts) },
                        variant = AdaptiveButtonVariant.Secondary,
                        size = AdaptiveButtonSize.Small,
                    )
                },
            )
        },
    )
}

@Composable
private fun PromptBrowserPane(
    categories: List<String>,
    selectedCategory: String,
    onCategoryChange: (String?) -> Unit,
    prompts: List<PromptTemplate>,
    selectedId: String?,
    onOpenPrompt: (PromptTemplate) -> Unit,
) {
    AdaptivePaneList(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
    ) {
        AdaptiveActionBar(
            leadingContent = {
                Column {
                    Text(
                        text = "Prompt Library",
                        style = MaterialTheme.typography.titleLarge,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${prompts.size} reusable workflows",
                        style = MaterialTheme.typography.bodySmall,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
            },
            secondaryActions = {
                AdaptiveBadge(text = "Templates", tone = AdaptiveBadgeTone.Info)
                AdaptiveBadge(text = "Versioned", tone = AdaptiveBadgeTone.Success)
            },
        )

        AdaptiveSelect(
            options = categories,
            selectedOption = selectedCategory,
            onSelectedOptionChange = onCategoryChange,
            optionLabel = { it },
            label = "Category",
            placeholder = "All prompts",
            clearable = false,
        )

        AdaptiveCollectionView(
            items = prompts,
            displayMode = AdaptiveCollectionDisplayMode.List,
            emptyContent = {
                AdaptiveEmptyState(
                    title = "No prompts in this category",
                    description = "Try another category or clear the filter.",
                )
            },
            listItemContent = { prompt ->
                PromptListItem(
                    prompt = prompt,
                    selected = prompt.id == selectedId,
                    onClick = { onOpenPrompt(prompt) },
                )
            },
        )
    }
}

@Composable
private fun PromptListItem(
    prompt: PromptTemplate,
    selected: Boolean,
    onClick: () -> Unit,
) {
    AdaptivePaneListItem(
        selected = selected,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
        ) {
            AiGlyph(prompt.category.take(1), modifier = Modifier.size(36.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = prompt.title,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (prompt.favorite) {
                        AdaptiveBadge(text = "Saved", tone = AdaptiveBadgeTone.Warning)
                    } else if (selected) {
                        AdaptiveBadge(text = "Open", tone = AdaptiveBadgeTone.Info)
                    }
                }
                Text(
                    text = prompt.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AdaptiveTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
                Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall)) {
                    AdaptiveBadge(text = prompt.category, tone = AdaptiveBadgeTone.Info)
                    prompt.tags.take(2).forEach { tag ->
                        AdaptiveBadge(text = tag, tone = AdaptiveBadgeTone.Neutral)
                    }
                }
            }
        }
    }
}

@Composable
private fun PromptDetailPane(
    prompt: PromptTemplate,
    onToggleFavorite: () -> Unit,
    onOpenPlayground: () -> Unit,
) {
    var selectedTab by remember(prompt.id) { mutableStateOf(PromptDetailTab.Overview) }

    AdaptiveScrollablePage(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
    ) {
        AdaptiveCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
            ) {
                AiGlyph(prompt.category.take(1), modifier = Modifier.size(48.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prompt.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = prompt.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
                AdaptiveButton(
                    text = if (prompt.favorite) "Saved" else "Save",
                    onClick = onToggleFavorite,
                    variant = AdaptiveButtonVariant.Secondary,
                    size = AdaptiveButtonSize.Small,
                )
                AdaptiveButton(
                    text = "Run",
                    onClick = onOpenPlayground,
                    size = AdaptiveButtonSize.Small,
                    trailingIcon = { AdaptiveIcons.ChevronRight(size = 14.dp, tint = AdaptiveTheme.colors.textInverse) },
                )
            }
        }

        AdaptiveTabs(
            tabs = PromptDetailTab.entries,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabLabel = { it.name },
        )

        when (selectedTab) {
            PromptDetailTab.Overview -> PromptOverview(prompt)
            PromptDetailTab.Variables -> PromptVariables(prompt)
            PromptDetailTab.Runs -> PromptRuns(prompt)
            PromptDetailTab.Notes -> PromptNotes(prompt)
        }
    }
}

@Composable
private fun PromptOverview(prompt: PromptTemplate) {
    AdaptiveSection(title = "Template") {
        AdaptiveCard {
            Text(
                text = prompt.body,
                style = MaterialTheme.typography.bodyMedium,
                color = AdaptiveTheme.colors.textPrimary,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@Composable
private fun PromptVariables(prompt: PromptTemplate) {
    AdaptiveSection(title = "Variables", subtitle = "Inputs exposed by this prompt template.") {
        if (prompt.variables.isEmpty()) {
            AdaptiveEmptyState(
                title = "No variables",
                description = "This prompt can run without additional structured inputs.",
            )
        } else {
            AdaptiveGrid {
                prompt.variables.forEach { variable ->
                    item(span = 6) {
                        AdaptiveCard(contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium)) {
                            AdaptiveBadge(text = "Input", tone = AdaptiveBadgeTone.Info)
                            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
                            Text(
                                text = "{{$variable}}",
                                style = MaterialTheme.typography.titleMedium,
                                color = AdaptiveTheme.colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PromptRuns(prompt: PromptTemplate) {
    AdaptiveSection(title = "Recent runs", subtitle = "Mock execution history for visual coverage.") {
        AdaptiveGrid {
            listOf("Draft", "Review", "Ship").forEachIndexed { index, label ->
                item(span = 4) {
                    AdaptiveCard(contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium)) {
                        AdaptiveBadge(
                            text = if (index == 2) "Passed" else "Ready",
                            tone = if (index == 2) AdaptiveBadgeTone.Success else AdaptiveBadgeTone.Neutral,
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
                        Text(
                            text = "$label run",
                            style = MaterialTheme.typography.titleSmall,
                            color = AdaptiveTheme.colors.textPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "${prompt.category} workflow",
                            style = MaterialTheme.typography.bodySmall,
                            color = AdaptiveTheme.colors.textSecondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PromptNotes(prompt: PromptTemplate) {
    AdaptiveSection(title = "Notes") {
        AdaptiveCard {
            Text(
                text = "Use this template when a ${prompt.category.lowercase()} task needs a repeatable instruction contract. Keep variables explicit and attach source files when running in production.",
                style = MaterialTheme.typography.bodyMedium,
                color = AdaptiveTheme.colors.textSecondary,
            )
        }
    }
}
