package io.github.adaptivekt.examples.communication.ui.chat

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
import io.github.adaptivekt.examples.communication.data.MockCommunicationData
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ChatArea(state: CommunicationState) {
    Box(modifier = Modifier.fillMaxSize()) {
        AdaptiveListDetailScaffold(
            selectedItem = state.selectedConversationId,
            onBackToList = { state.selectedConversationId = null },
            listPane = {
                ChatInbox(
                    state = state
                )
            },
            detailPane = { selectedId ->
                val conversation = state.conversations.find { it.id == selectedId }
                if (conversation != null) {
                    ChatDetail(
                        state = state,
                        conversation = conversation
                    )
                } else {
                    AdaptiveEmptyState(title = "Not found", description = "Conversation not found.")
                }
            }
        )
    }
}

@Composable
fun ChatInbox(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Chat", fontWeight = FontWeight.Bold) },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { AdaptiveBadge("+") }, // Mock icon
                    onClick = { state.isNewConversationOpen = true }
                )
            }
        )
        
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            AdaptiveTextField(
                value = state.chatSearchQuery,
                onValueChange = { state.chatSearchQuery = it },
                placeholder = "Search messages...",
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val pinned = state.visibleConversations.filter { it.isPinned }
            val recent = state.visibleConversations.filter { !it.isPinned }

            if (pinned.isNotEmpty()) {
                item {
                    AdaptiveSection(title = "Pinned") {
                        pinned.forEach { conv ->
                            ConversationRow(conv, isSelected = conv.id == state.selectedConversationId) {
                                state.selectConversation(conv.id)
                            }
                        }
                    }
                }
            }

            if (recent.isNotEmpty()) {
                item {
                    AdaptiveSection(title = "Recent") {
                        recent.forEach { conv ->
                            ConversationRow(conv, isSelected = conv.id == state.selectedConversationId) {
                                state.selectConversation(conv.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationRow(conversation: Conversation, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.surface
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).background(bgColor, AdaptiveTheme.shapes.medium).clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdaptiveAvatar(name = conversation.title, size = 40.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                AdaptiveText(conversation.title, fontWeight = FontWeight.Bold)
                val statusText = if (conversation.type == ConversationType.Channel) "${conversation.participants.size} members" else "Direct Message"
                AdaptiveText(statusText, color = AdaptiveTheme.colors.textMuted, maxLines = 1)
            }
            if (conversation.unreadCount > 0) {
                AdaptiveBadge(conversation.unreadCount.toString(), tone = AdaptiveBadgeTone.Info)
            }
        }
    }
}

@Composable
fun ChatDetail(state: CommunicationState, conversation: Conversation) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = {
                Column {
                    AdaptiveText(conversation.title, fontWeight = FontWeight.Bold)
                    val statusText = if (conversation.type == ConversationType.Channel) "${conversation.participants.size} members" else "Direct Message"
                    AdaptiveText(statusText, color = AdaptiveTheme.colors.textMuted)
                }
            },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { AdaptiveText("ℹ️") },
                    onClick = { /* Open info panel */ }
                )
            }
        )

        val messages = state.messages.filter { it.conversationId == conversation.id }.sortedBy { it.timestamp }
        
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msg ->
                MessageRow(msg)
            }
        }

        ChatComposer(state, conversation)
    }
}

@Composable
fun MessageRow(message: Message) {
    val isMe = message.sender.id == MockCommunicationData.currentUser.id
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val bgColor = if (isMe) AdaptiveTheme.colors.primary.copy(alpha = 0.1f) else AdaptiveTheme.colors.surfaceMuted

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!isMe) {
                AdaptiveAvatar(name = message.sender.name, size = 24.dp)
                Spacer(modifier = Modifier.width(8.dp))
            }
            AdaptiveText(message.sender.name, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textMuted)
        }
        Box(modifier = Modifier.padding(top = 4.dp).background(bgColor, AdaptiveTheme.shapes.medium)) {
            AdaptiveText(message.content, modifier = Modifier.padding(12.dp))
        }
        if (message.attachments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            message.attachments.forEach { att ->
                AdaptiveChip(text = att.name, onClick = {})
            }
        }
    }
}

@Composable
fun ChatComposer(state: CommunicationState, conversation: Conversation) {
    var text by remember { mutableStateOf(state.chatDrafts[conversation.id] ?: "") }
    
    AdaptiveCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdaptiveIconButton(
                content = { AdaptiveText("📎") },
                onClick = { /* Open attachment */ }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                AdaptiveTextField(
                    value = text,
                    onValueChange = { 
                        text = it
                        state.chatDrafts[conversation.id] = it
                    },
                    placeholder = "Type a message...",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            AdaptiveButton(
                "Send",
                onClick = {
                    if (text.isNotBlank()) {
                        state.sendChatMessage(conversation.id, text)
                        text = ""
                    }
                },
                enabled = text.isNotBlank()
            )
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
}
