package io.github.adaptivekt.examples.communication.ui.mail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.*
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.communication.model.*
import io.github.adaptivekt.examples.communication.state.CommunicationState
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.feedback.AdaptiveEmptyState

@Composable
fun MailArea(state: CommunicationState) {
    AdaptiveListDetailScaffold(
        listPane = {
            MailThreadList(state)
        },
        detailPane = {
            if (state.selectedMailThreadId != null) {
                val thread = state.mailThreads.find { it.id == state.selectedMailThreadId }
                if (thread != null) {
                    MailReadingPane(state, thread)
                } else {
                    AdaptiveEmptyState("Mail not found", description = "It may have been deleted.")
                }
            } else {
                AdaptiveEmptyState("No mail selected", description = "Choose a mail from the list.")
            }
        },
        selectedItem = state.selectedMailThreadId,
        onBackToList = { state.selectMailThread(null) }
    )
}

@Composable
fun MailThreadList(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText(state.selectedMailFolder.name, fontWeight = FontWeight.Bold) },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { AdaptiveBadge("✏️") },
                    onClick = { state.isComposeMailOpen = true }
                )
            }
        )

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            AdaptiveTextField(
                value = state.mailSearchQuery,
                onValueChange = { state.mailSearchQuery = it },
                placeholder = "Search mail...",
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Folders switcher mockup
            MailFolder.values().forEach { folder ->
                if (folder == MailFolder.Inbox || folder == MailFolder.Starred) {
                    AdaptiveChip(
                        text = folder.name,
                        onClick = { state.selectMailFolder(folder) },
                        tone = if (state.selectedMailFolder == folder) io.github.adaptivekt.components.AdaptiveChipTone.Primary else io.github.adaptivekt.components.AdaptiveChipTone.Neutral
                    )
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val threads = state.visibleMailThreads

            if (threads.isEmpty()) {
                item {
                    AdaptiveEmptyState("Nothing here", description = "You're all caught up!")
                }
            } else {
                items(threads) { thread ->
                    MailThreadRow(thread, isSelected = thread.id == state.selectedMailThreadId) {
                        state.selectMailThread(thread.id)
                    }
                }
            }
        }
    }
}

@Composable
fun MailThreadRow(thread: MailThread, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.surface
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).background(bgColor, AdaptiveTheme.shapes.medium).clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AdaptiveText(
                    text = thread.senderNames,
                    fontWeight = if (thread.isRead) FontWeight.Normal else FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (thread.isStarred) {
                    AdaptiveText("⭐", color = AdaptiveTheme.colors.warning)
                }
                Spacer(modifier = Modifier.width(8.dp))
                AdaptiveText("Date", color = AdaptiveTheme.colors.textMuted) // placeholder
            }
            AdaptiveText(
                text = thread.subject,
                fontWeight = if (thread.isRead) FontWeight.Normal else FontWeight.Bold,
                maxLines = 1
            )
            AdaptiveText(
                text = thread.latestMessage?.body ?: "",
                color = AdaptiveTheme.colors.textMuted,
                maxLines = 1
            )
            if (thread.labels.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    thread.labels.forEach { label ->
                        AdaptiveBadge(label.name, tone = AdaptiveBadgeTone.Neutral)
                    }
                }
            }
        }
    }
}

@Composable
fun MailReadingPane(state: CommunicationState, thread: MailThread) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText(thread.subject, fontWeight = FontWeight.Bold) },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { AdaptiveText("⭐") },
                    onClick = { state.toggleMailStar(thread.id) }
                )
                AdaptiveIconButton(
                    content = { AdaptiveText("🗑️") },
                    onClick = { state.archiveMailThread(thread.id) }
                )
            }
        )

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(thread.messages) { message ->
                AdaptiveCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AdaptiveAvatar(name = message.sender.name, size = 40.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                AdaptiveText(message.sender.name, fontWeight = FontWeight.Bold)
                                AdaptiveText("to me", color = AdaptiveTheme.colors.textMuted)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AdaptiveSelectionArea {
                            AdaptiveText(message.body)
                        }
                    }
                }
            }
            item {
                AdaptiveButton("Reply", onClick = { /* open reply compose */ }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
}
