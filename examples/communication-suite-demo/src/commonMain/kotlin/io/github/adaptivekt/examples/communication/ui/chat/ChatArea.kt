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
import kotlinx.datetime.Instant
import kotlin.time.Duration
import io.github.adaptivekt.examples.communication.ui.icons.DemoIcons
import androidx.compose.material3.Icon

fun formatRelativeTime(instant: Instant): String {
    val now = Clock.System.now()
    val diff = now - instant
    return when {
        diff.inWholeMinutes < 1 -> "Just now"
        diff.inWholeHours < 1 -> "${diff.inWholeMinutes}m"
        diff.inWholeDays < 1 -> "${diff.inWholeHours}h"
        diff.inWholeDays < 7 -> "${diff.inWholeDays}d"
        else -> "Older"
    }
}

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
                    AdaptiveEmptyState(
                        title = "No conversation selected",
                        description = "Choose a conversation to start chatting."
                    )
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
                    content = { Icon(DemoIcons.Add, contentDescription = "Add") },
                    onClick = { state.isNewConversationOpen = true }
                )
            }
        )

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            AdaptiveTextField(
                value = state.chatSearchQuery,
                onValueChange = {
                    state.chatSearchQuery = it
                    state.isChatSearchActive = true
                },
                placeholder = "Search messages...",
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (state.conversations.isEmpty()) {
                item {
                    AdaptiveEmptyState(
                        title = "No conversations yet",
                        description = "Start a new chat to connect with others.",
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else if (state.isChatSearchActive || state.chatSearchQuery.isNotEmpty()) {
                val query = state.chatSearchQuery
                val searchResults = if (query.isBlank()) {
                    emptyList()
                } else {
                    state.messages.filter { it.content.contains(query, ignoreCase = true) }
                }

                item {
                    AdaptiveSection(title = if (query.isBlank()) "Type to search" else "Search Results") {
                        if (query.isNotBlank() && searchResults.isEmpty()) {
                            AdaptiveText("No messages found.", modifier = Modifier.padding(16.dp), color = AdaptiveTheme.colors.textMuted)
                        } else {
                            searchResults.forEach { msg ->
                                val conv = state.conversations.find { it.id == msg.conversationId }
                                if (conv != null) {
                                    ConversationRow(
                                        conversation = conv,
                                        lastMessage = msg,
                                        isSelected = conv.id == state.selectedConversationId
                                    ) {
                                        state.selectConversation(conv.id)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                val pinned = state.visibleConversations.filter { it.isPinned }
                val recent = state.visibleConversations.filter { !it.isPinned }

                if (pinned.isNotEmpty()) {
                    item {
                        AdaptiveSection(title = "Pinned") {
                            pinned.forEach { conv ->
                                val lastMessage = state.messages.filter { it.conversationId == conv.id }.maxByOrNull { it.timestamp }
                                ConversationRow(
                                    conversation = conv,
                                    lastMessage = lastMessage,
                                    isSelected = conv.id == state.selectedConversationId
                                ) {
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
                                val lastMessage = state.messages.filter { it.conversationId == conv.id }.maxByOrNull { it.timestamp }
                                ConversationRow(
                                    conversation = conv,
                                    lastMessage = lastMessage,
                                    isSelected = conv.id == state.selectedConversationId
                                ) {
                                    state.selectConversation(conv.id)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.isNewConversationOpen) {
            AdaptiveDialog(
                onDismissRequest = { state.isNewConversationOpen = false },
                title = "New Message",
                confirmButton = {
                    AdaptiveButton("Cancel", onClick = { state.isNewConversationOpen = false })
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    MockCommunicationData.allUsers.filter { it.id != MockCommunicationData.currentUser.id }.forEach { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Normally we would add to state, but for mock, let's just close dialog
                                    // and select the user's direct message if it exists.
                                    val existing = state.conversations.find {
                                        it.type == ConversationType.Direct && it.participants.any { p -> p.id == user.id }
                                    }
                                    if (existing != null) {
                                        state.selectConversation(existing.id)
                                        state.isNewConversationOpen = false
                                    } else {
                                        state.createConversation(user)
                                    }
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AdaptiveAvatar(name = user.name, size = 32.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            AdaptiveText(user.name, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationRow(conversation: Conversation, lastMessage: Message?, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.background
    Box(
        modifier = Modifier.fillMaxWidth().background(bgColor).clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdaptiveAvatar(name = conversation.title, size = 48.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    AdaptiveText(
                        text = conversation.title,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        color = AdaptiveTheme.colors.textPrimary,
                        maxLines = 1
                    )
                    if (lastMessage != null) {
                        AdaptiveText(
                            text = formatRelativeTime(lastMessage.timestamp),
                            color = if (conversation.unreadCount > 0) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.textMuted,
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    val messagePreview = if (lastMessage != null) {
                        if (lastMessage.sender.id == MockCommunicationData.currentUser.id) "You: ${lastMessage.content}" else lastMessage.content
                    } else "No messages yet"

                    AdaptiveText(
                        text = messagePreview,
                        color = if (conversation.unreadCount > 0) AdaptiveTheme.colors.textPrimary else AdaptiveTheme.colors.textMuted,
                        maxLines = 1,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AdaptiveBadge(conversation.unreadCount.toString(), tone = AdaptiveBadgeTone.Info)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatDetail(state: CommunicationState, conversation: Conversation) {
    var isInfoPanelOpen by remember { mutableStateOf(false) }

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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AdaptiveIconButton(
                        content = { Icon(DemoIcons.Call, contentDescription = "Start Audio Call") },
                        onClick = { println("Action") }
                    )
                    AdaptiveIconButton(
                        content = { Icon(DemoIcons.Face, contentDescription = "Start Video Call") },
                        onClick = { println("Action") }
                    )
                    AdaptiveIconButton(
                        content = { Icon(DemoIcons.Info, contentDescription = "Conversation Info") },
                        onClick = { isInfoPanelOpen = true }
                    )
                }
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

        if (isInfoPanelOpen) {
            AdaptiveDialog(
                onDismissRequest = { isInfoPanelOpen = false },
                title = "Conversation Details",
                confirmButton = {
                    AdaptiveButton("Close", onClick = { isInfoPanelOpen = false })
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AdaptiveSection(title = "Members") {
                        conversation.participants.forEach { user ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                                AdaptiveAvatar(name = user.name, size = 32.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                AdaptiveText(user.name, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    AdaptiveSection(title = "Shared Files") {
                        val allFiles = state.messages.filter { it.conversationId == conversation.id }.flatMap { it.attachments }
                        if (allFiles.isEmpty()) {
                            AdaptiveText("No shared files", color = AdaptiveTheme.colors.textMuted)
                        } else {
                            allFiles.forEach { file ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                    AdaptiveText("📄", modifier = Modifier.padding(end = 8.dp))
                                    AdaptiveText(file.name, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageRow(message: Message) {
    val isMe = message.sender.id == MockCommunicationData.currentUser.id
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val bgColor = if (isMe) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.surfaceMuted
    val textColor = if (isMe) AdaptiveTheme.colors.textInverse else AdaptiveTheme.colors.textPrimary

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        if (!isMe) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AdaptiveAvatar(name = message.sender.name, size = 24.dp)
                Spacer(modifier = Modifier.width(8.dp))
                AdaptiveText(message.sender.name, style = androidx.compose.material3.MaterialTheme.typography.labelMedium, color = AdaptiveTheme.colors.textMuted)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Row(horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .background(bgColor, AdaptiveTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                if (message.content.startsWith("Check out this snippet:\n```kotlin")) {
                    Column {
                        AdaptiveText("Check out this snippet:", color = textColor, modifier = Modifier.padding(bottom = 8.dp))
                        Box(modifier = Modifier.fillMaxWidth().background(androidx.compose.ui.graphics.Color(0xFF2B2B2B), AdaptiveTheme.shapes.small).padding(8.dp)) {
                            AdaptiveText("fun hello() {\n    println(\"World\")\n}", color = androidx.compose.ui.graphics.Color(0xFFE0E0E0), style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    AdaptiveText(message.content, color = textColor)
                }
            }
        }
        if (message.reactions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start) {
                message.reactions.forEach { reaction ->
                    AdaptiveChip("${reaction.emoji} ${reaction.count}", onClick = { println("Action") })
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
        if (message.attachments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                message.attachments.forEach { att ->
                    AdaptiveChip(text = att.name, onClick = { println("Action") })
                }
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
                content = { Icon(DemoIcons.AddCircle, contentDescription = "Attach File") },
                onClick = { println("Action") }
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
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE, style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = style)
}
