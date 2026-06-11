package io.github.adaptivekt.examples.communication.model

import kotlinx.datetime.Instant

enum class PresenceStatus {
    Online, Offline, Away, Busy
}

data class UserProfile(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val status: PresenceStatus = PresenceStatus.Offline
)

enum class ConversationType {
    Direct, Channel
}

data class Conversation(
    val id: String,
    val type: ConversationType,
    val title: String,
    val participants: List<UserProfile>,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val lastMessageAt: Instant
)

enum class MessageDeliveryStatus {
    Sending, Sent, Delivered, Read, Failed
}

data class MessageAttachment(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val mimeType: String,
    val url: String? = null
)

data class MessageReaction(
    val emoji: String,
    val count: Int,
    val reactedByMe: Boolean
)

data class Message(
    val id: String,
    val conversationId: String,
    val sender: UserProfile,
    val content: String,
    val timestamp: Instant,
    val deliveryStatus: MessageDeliveryStatus = MessageDeliveryStatus.Sent,
    val attachments: List<MessageAttachment> = emptyList(),
    val reactions: List<MessageReaction> = emptyList(),
    val isSystemMessage: Boolean = false
)

enum class MailFolder {
    Inbox, Starred, Sent, Drafts, Archive, Spam, Trash, Snoozed
}

data class MailLabel(
    val id: String,
    val name: String,
    val colorHex: String
)

data class MailContact(
    val name: String,
    val email: String,
    val avatarUrl: String? = null
)

data class MailAttachment(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val mimeType: String
)

enum class MailPriority {
    Low, Normal, High
}

data class MailMessage(
    val id: String,
    val threadId: String,
    val sender: MailContact,
    val recipients: List<MailContact>,
    val cc: List<MailContact> = emptyList(),
    val bcc: List<MailContact> = emptyList(),
    val timestamp: Instant,
    val body: String,
    val attachments: List<MailAttachment> = emptyList(),
    val isRead: Boolean = false,
    val isStarred: Boolean = false
)

data class MailThread(
    val id: String,
    val subject: String,
    val messages: List<MailMessage>,
    val labels: List<MailLabel> = emptyList(),
    val folder: MailFolder = MailFolder.Inbox,
    val priority: MailPriority = MailPriority.Normal
) {
    val latestMessage: MailMessage? get() = messages.maxByOrNull { it.timestamp }
    val isRead: Boolean get() = messages.all { it.isRead }
    val isStarred: Boolean get() = messages.any { it.isStarred }
    val senderNames: String get() = messages.map { it.sender.name }.distinct().joinToString(", ")
}
