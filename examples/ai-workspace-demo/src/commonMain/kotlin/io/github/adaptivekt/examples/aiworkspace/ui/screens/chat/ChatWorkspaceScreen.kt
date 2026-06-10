package io.github.adaptivekt.examples.aiworkspace.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.examples.aiworkspace.model.ChatMessage
import io.github.adaptivekt.examples.aiworkspace.model.Conversation
import io.github.adaptivekt.examples.aiworkspace.model.MessagePart
import io.github.adaptivekt.examples.aiworkspace.model.MessageRole
import io.github.adaptivekt.examples.aiworkspace.model.MessageStatus
import io.github.adaptivekt.examples.aiworkspace.model.ToolCallStatus
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun ChatWorkspaceScreen(
    store: AiWorkspaceStore,
    navigator: AdaptiveNavigator<AiRoute>,
    selectedId: String?,
) {
    val selectedItem = store.conversations.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Chats) },
        listPane = {
            ConversationListPane(
                conversations = store.conversations,
                selectedId = selectedId,
                onNewChat = { navigator.navigate(AiRoute.Chats) },
                onOpenConversation = { navigator.navigate(AiRoute.Chat(it.id)) },
            )
        },
        detailPane = { conversation ->
            ChatDetailPane(
                conversation = conversation,
                onSendMessage = { text -> store.sendMessage(conversation.id, text) },
                onOpenPrompts = { navigator.navigate(AiRoute.Prompts) },
            )
        },
        emptyDetail = {
            AdaptiveEmptyState(
                title = "Select a conversation",
                description = "Open an active thread or start a new chat to review AdaptiveListDetailScaffold in action.",
                icon = {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(AdaptiveTheme.shapes.large)
                            .background(AdaptiveTheme.colors.primarySubtle),
                        contentAlignment = Alignment.Center,
                    ) {
                        AiGlyph("AI", modifier = Modifier.size(40.dp))
                    }
                },
                action = {
                    Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
                        AdaptiveButton(
                            text = "New chat",
                            onClick = { navigator.navigate(AiRoute.Chats) },
                            leadingIcon = { AdaptiveIcons.Plus(size = 16.dp, tint = AdaptiveTheme.colors.textInverse) },
                        )
                        AdaptiveButton(
                            text = "Prompts",
                            onClick = { navigator.navigate(AiRoute.Prompts) },
                            variant = AdaptiveButtonVariant.Secondary,
                        )
                    }
                },
            )
        },
        compactDetailHeader = { conversation ->
            AdaptiveActionBar(
                leadingContent = {
                    Column {
                        Text(
                            text = conversation.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = AdaptiveTheme.colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Tap back to return to conversations",
                            style = MaterialTheme.typography.labelSmall,
                            color = AdaptiveTheme.colors.textMuted,
                        )
                    }
                },
                secondaryActions = {
                    AdaptiveButton(
                        text = "Back",
                        onClick = { navigator.navigate(AiRoute.Chats) },
                        variant = AdaptiveButtonVariant.Secondary,
                        size = AdaptiveButtonSize.Small,
                    )
                },
            )
        },
    )
}

@Composable
private fun ConversationListPane(
    conversations: List<Conversation>,
    selectedId: String?,
    onNewChat: () -> Unit,
    onOpenConversation: (Conversation) -> Unit,
) {
    AdaptiveScrollablePage(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        AdaptiveActionBar(
            leadingContent = {
                Column {
                    Text(
                        text = "Conversations",
                        style = MaterialTheme.typography.titleLarge,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${conversations.size} active threads",
                        style = MaterialTheme.typography.bodySmall,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
            },
            primaryAction = {
                AdaptiveButton(
                    text = "New",
                    onClick = onNewChat,
                    size = AdaptiveButtonSize.Small,
                    leadingIcon = { AdaptiveIcons.Plus(size = 14.dp, tint = AdaptiveTheme.colors.textInverse) },
                )
            },
        )

        AdaptiveSurface(contentPadding = PaddingValues(AdaptiveTokens.Spacing.Small)) {
            Column {
                conversations.forEachIndexed { index, conversation ->
                    ConversationListRow(
                        conversation = conversation,
                        selected = conversation.id == selectedId,
                        onClick = { onOpenConversation(conversation) },
                    )
                    if (index < conversations.lastIndex) {
                        AdaptiveDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationListRow(
    conversation: Conversation,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val rowShape = AdaptiveTheme.shapes.medium
    val rowBackground = if (selected) AdaptiveTheme.colors.primarySubtle else AdaptiveTheme.colors.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(rowBackground, rowShape)
            .clickable(onClick = onClick)
            .padding(AdaptiveTokens.Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        AdaptiveAvatar(name = conversation.title, size = 36.dp)
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = conversation.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    color = AdaptiveTheme.colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (conversation.unread) {
                    AdaptiveBadge(text = "New", tone = AdaptiveBadgeTone.Warning)
                }
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
            Text(
                text = conversation.previewText(),
                style = MaterialTheme.typography.bodySmall,
                color = AdaptiveTheme.colors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall)) {
                conversation.tags.take(2).forEach { tag ->
                    AdaptiveBadge(text = tag, tone = AdaptiveBadgeTone.Neutral)
                }
                AdaptiveBadge(text = conversation.updatedAt, tone = AdaptiveBadgeTone.Info)
            }
        }
    }
}

@Composable
private fun ChatDetailPane(
    conversation: Conversation,
    onSendMessage: (String) -> Unit,
    onOpenPrompts: () -> Unit,
) {
    var inputText by remember(conversation.id) { mutableStateOf("") }

    AdaptiveScrollablePage(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
    ) {
        AdaptiveCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
            ) {
                AdaptiveAvatar(name = conversation.title, size = 44.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = conversation.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Model: Gemini 1.5 Pro | Updated ${conversation.updatedAt}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
                AdaptiveBadge(
                    text = if (conversation.unread) "Needs review" else "Synced",
                    tone = if (conversation.unread) AdaptiveBadgeTone.Warning else AdaptiveBadgeTone.Success,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium)) {
            conversation.messages.forEach { message ->
                MessageBubble(message)
            }
        }

        AdaptiveCard {
            Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
                AdaptiveBadge(text = "Summarize", tone = AdaptiveBadgeTone.Info)
                AdaptiveBadge(text = "Find sources", tone = AdaptiveBadgeTone.Neutral)
                AdaptiveBadge(text = "Draft next step", tone = AdaptiveBadgeTone.Neutral)
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            ) {
                AdaptiveTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = "Ask for a summary, source check or next action...",
                    modifier = Modifier.weight(1f),
                )
                AdaptiveButton(
                    text = "Send",
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    enabled = inputText.isNotBlank(),
                    trailingIcon = { AdaptiveIcons.ChevronRight(size = 16.dp, tint = AdaptiveTheme.colors.textInverse) },
                )
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            AdaptiveButton(
                text = "Browse reusable prompts",
                onClick = onOpenPrompts,
                variant = AdaptiveButtonVariant.Ghost,
                size = AdaptiveButtonSize.Small,
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == MessageRole.User
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val background = if (isUser) AdaptiveTheme.colors.primarySubtle else AdaptiveTheme.colors.surfaceRaised
    val label = when (message.role) {
        MessageRole.User -> "You"
        MessageRole.Assistant -> "Assistant"
        MessageRole.System -> "System"
        MessageRole.Tool -> "Tool"
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        AdaptiveCard(
            contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            ) {
                AdaptiveAvatar(name = label, size = 28.dp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = AdaptiveTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                )
                AdaptiveBadge(text = "${message.tokenCount} tok", tone = AdaptiveBadgeTone.Neutral)
                if (message.status == MessageStatus.Streaming) {
                    AdaptiveBadge(text = "Streaming", tone = AdaptiveBadgeTone.Info)
                }
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            Column(
                modifier = Modifier
                    .clip(AdaptiveTheme.shapes.medium)
                    .background(background)
                    .padding(AdaptiveTokens.Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            ) {
                message.parts.forEach { part ->
                    MessagePartView(part)
                }
            }
        }
    }
}

@Composable
private fun MessagePartView(part: MessagePart) {
    when (part) {
        is MessagePart.Text -> Text(
            text = part.value,
            style = MaterialTheme.typography.bodyMedium,
            color = AdaptiveTheme.colors.textPrimary,
        )
        is MessagePart.CodeBlock -> AdaptiveCard(
            contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        ) {
            AdaptiveBadge(text = part.language, tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            Text(
                text = part.code,
                style = MaterialTheme.typography.bodySmall,
                color = AdaptiveTheme.colors.textPrimary,
            )
        }
        is MessagePart.ToolCall -> AdaptiveCard(
            contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            ) {
                AiGlyph("T", modifier = Modifier.size(24.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(part.name, style = MaterialTheme.typography.titleSmall, color = AdaptiveTheme.colors.textPrimary)
                    Text(part.summary, style = MaterialTheme.typography.bodySmall, color = AdaptiveTheme.colors.textSecondary)
                }
                AdaptiveBadge(
                    text = part.status.name,
                    tone = when (part.status) {
                        ToolCallStatus.Pending -> AdaptiveBadgeTone.Warning
                        ToolCallStatus.Success -> AdaptiveBadgeTone.Success
                        ToolCallStatus.Failed -> AdaptiveBadgeTone.Danger
                    },
                )
            }
        }
        is MessagePart.Source -> AdaptiveCard(
            contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            ) {
                AiGlyph("K", modifier = Modifier.size(24.dp))
                Column {
                    Text(part.title, style = MaterialTheme.typography.titleSmall, color = AdaptiveTheme.colors.textPrimary)
                    Text(part.description, style = MaterialTheme.typography.bodySmall, color = AdaptiveTheme.colors.textSecondary)
                }
            }
        }
    }
}

private fun Conversation.previewText(): String {
    return messages.lastOrNull()?.parts?.firstOrNull()?.let { part ->
        when (part) {
            is MessagePart.Text -> part.value
            is MessagePart.CodeBlock -> "Code block: ${part.language}"
            is MessagePart.ToolCall -> "Tool call: ${part.name}"
            is MessagePart.Source -> "Source: ${part.title}"
        }
    } ?: "No messages yet"
}
