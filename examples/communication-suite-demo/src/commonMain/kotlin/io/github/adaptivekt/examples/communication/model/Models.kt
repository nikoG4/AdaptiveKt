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
    val slug: String = id,
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

enum class MessageType {
    Text, Code, Image, File, Link, System, Event
}

data class Message(
    val id: String,
    val conversationId: String,
    val sender: UserProfile,
    val content: String,
    val timestamp: Instant,
    val type: MessageType = MessageType.Text,
    val deliveryStatus: MessageDeliveryStatus = MessageDeliveryStatus.Sent,
    val attachments: List<MessageAttachment> = emptyList(),
    val reactions: List<MessageReaction> = emptyList(),
    val isSystemMessage: Boolean = false
)

enum class CallType {
    Audio, Video
}

enum class CallDirection {
    Incoming, Outgoing, Missed
}

data class CallRecord(
    val id: String,
    val caller: UserProfile,
    val receiver: UserProfile,
    val timestamp: Instant,
    val durationSeconds: Int,
    val type: CallType,
    val direction: CallDirection
)
