package io.github.adaptivekt.examples.aiworkspace.ui.screens.prompts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.model.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.*
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun PromptLibraryScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>, selectedId: String?) {
    val selectedItem = store.prompts.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Prompts) },
        listPane = {
            AdaptivePage {
                AdaptiveActionBar(leadingContent = { Text("Prompt Library", style = MaterialTheme.typography.titleLarge) })
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(store.prompts) { prompt ->
                        PromptListItem(
                            prompt = prompt,
                            isSelected = prompt.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.PromptDetail(prompt.id)) }
                        )
                    }
                }
            }
        },
        detailPane = { prompt ->
            AdaptiveScrollablePage {
                AdaptiveActionBar(
                    leadingContent = { Text(prompt.title, style = MaterialTheme.typography.titleLarge) },
                    primaryAction = {
                        IconButton(onClick = { store.togglePromptFavorite(prompt.id) }) {
                            AiGlyph(if (prompt.favorite) "★" else "☆")
                        }
                    }
                )

                AdaptiveSection {
                    Text(prompt.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Row {
                        prompt.tags.forEach { tag ->
                            StatusBadge(tag, Modifier.padding(end = 8.dp), isSuccess = false)
                        }
                    }
                }

                AdaptiveSection(title = "Template") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                    ) {
                        Text(prompt.body, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (prompt.variables.isNotEmpty()) {
                    AdaptiveSection(title = "Variables") {
                        AdaptiveGrid() {
                            prompt.variables.forEach { variable -> item {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(8.dp)
                                ) {
                                    Text("{{$variable}}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                } }
                            }
                        }
                    }
                }
            }
        },
        emptyDetail = {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Select a prompt to view details")
            }
        }
    )
}

@Composable
private fun PromptListItem(
    prompt: PromptTemplate,
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
            Text(prompt.title, style = MaterialTheme.typography.titleMedium, color = contentColor)
            Spacer(Modifier.height(4.dp))
            Text(
                prompt.description,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                maxLines = 2
            )
        }
        if (prompt.favorite) {
            AiGlyph("*")
        }
    }
}

