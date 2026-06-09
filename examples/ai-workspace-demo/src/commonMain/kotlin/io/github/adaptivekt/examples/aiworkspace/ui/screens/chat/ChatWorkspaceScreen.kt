package io.github.adaptivekt.examples.aiworkspace.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
// removed icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.model.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun ChatWorkspaceScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>, selectedId: String?) {
    val selectedItem = store.conversations.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Chats) },
        listPane = {
            AdaptivePage {
                AdaptiveActionBar(leadingContent = { Text("Conversations", style = MaterialTheme.typography.titleLarge) })
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(store.conversations) { conv ->
                        ConversationListItem(
                            conversation = conv,
                            isSelected = conv.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.Chat(conv.id)) }
                        )
                    }
                }
            }
        },
        detailPane = { conv ->
            ChatRoom(
                conversation = conv,
                onSendMessage = { text ->
                    store.sendMessage(conv.id, text)
                }
            )
        },
        emptyDetail = {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Select a conversation to start chatting")
            }
        }
    )
}

@Composable
private fun ConversationListItem(
    conversation: Conversation,
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
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(conversation.title, style = MaterialTheme.typography.titleMedium, color = contentColor, modifier = Modifier.weight(1f, fill = false))
                if (conversation.unread) {
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.primary))
                }
                if (conversation.pinned) {
                    Spacer(Modifier.width(8.dp))
                    AiGlyph("P", modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.height(4.dp))
            val lastMsg = conversation.messages.lastOrNull()?.parts?.firstOrNull() as? MessagePart.Text
            Text(
                lastMsg?.value ?: "No messages",
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ChatRoom(
    conversation: Conversation,
    onSendMessage: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    
    Column(Modifier.fillMaxSize()) {
        AdaptiveActionBar(leadingContent = { Text(conversation.title, style = MaterialTheme.typography.titleLarge) })
        
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(conversation.messages) { msg ->
                MessageBubble(msg)
                Spacer(Modifier.height(16.dp))
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message...") }
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { 
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                AiGlyph("→")
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == MessageRole.User
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val bgColor = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = RoundedCornerShape(16.dp)
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .clip(shape)
                .background(bgColor)
                .padding(16.dp)
        ) {
            Column {
                message.parts.forEach { part ->
                    when (part) {
                        is MessagePart.Text -> Text(part.value, color = contentColor)
                        is MessagePart.CodeBlock -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(8.dp)
                            ) {
                                Text(part.code, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        is MessagePart.ToolCall -> {
                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    AiGlyph("T", modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("${part.name} - ${part.summary}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                        is MessagePart.Source -> {
                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    AiGlyph("K", modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(part.title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (message.status == MessageStatus.Streaming) {
            Text("Typing...", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

