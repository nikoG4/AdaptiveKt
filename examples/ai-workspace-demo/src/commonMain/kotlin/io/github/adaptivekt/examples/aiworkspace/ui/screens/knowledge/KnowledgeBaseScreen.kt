package io.github.adaptivekt.examples.aiworkspace.ui.screens.knowledge

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
public fun KnowledgeBaseScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>, selectedId: String?) {
    val selectedItem = store.files.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.KnowledgeBase) },
        listPane = {
            AdaptivePage {
                AdaptiveActionBar(leadingContent = { Text("Knowledge Base", style = MaterialTheme.typography.titleLarge) })
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(store.files) { file ->
                        FileListItem(
                            file = file,
                            isSelected = file.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.FileDetail(file.id)) }
                        )
                    }
                }
            }
        },
        detailPane = { file ->
            AdaptiveScrollablePage {
                AdaptiveActionBar(leadingContent = { Text(file.name, style = MaterialTheme.typography.titleLarge) })

                AdaptiveSection(title = "Metadata") {
                    AdaptiveGrid() {
                        item { MetadataCard("Type", file.type) }
                        item { MetadataCard("Size", file.sizeLabel) }
                        item { MetadataCard("Chunks", file.chunks.toString()) }
                        item { MetadataCard("Updated", file.updatedAt) }
                    }
                }

                AdaptiveSection(title = "Status") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusBadge(
                            status = file.status.name,
                            isSuccess = file.status == FileIndexStatus.Ready
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = when (file.status) {
                                FileIndexStatus.Ready -> "File is indexed and ready for retrieval."
                                FileIndexStatus.Indexing -> "File is currently being processed..."
                                FileIndexStatus.Uploaded -> "File uploaded, waiting in queue."
                                FileIndexStatus.Failed -> "Failed to index file. Please check format."
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                AdaptiveSection(title = "Preview") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (file.status == FileIndexStatus.Ready) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AiGlyph("I")
                                Spacer(Modifier.height(8.dp))
                                Text("Preview available for indexed chunks.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        } else {
                            Text("Preview not available.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        },
        emptyDetail = {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Select a file to view details")
            }
        }
    )
}

@Composable
private fun MetadataCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun FileListItem(
    file: KnowledgeFile,
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
            Text(file.name, style = MaterialTheme.typography.titleMedium, color = contentColor)
            Spacer(Modifier.height(4.dp))
            Text(
                "${file.type} • ${file.sizeLabel} • ${file.updatedAt}",
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
        StatusBadge(
            status = file.status.name,
            isSuccess = file.status == FileIndexStatus.Ready
        )
    }
}

