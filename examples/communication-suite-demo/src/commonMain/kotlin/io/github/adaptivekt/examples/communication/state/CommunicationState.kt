package io.github.adaptivekt.examples.communication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.adaptivekt.examples.communication.data.MockCommunicationData
import io.github.adaptivekt.examples.communication.model.*
import kotlinx.datetime.Clock

enum class AppArea {
    Chat, Contacts, Calls, Settings
}

class CommunicationState {
    var activeArea by mutableStateOf(AppArea.Chat)

    var conversations by mutableStateOf(MockCommunicationData.conversations)
    var messages by mutableStateOf(MockCommunicationData.messages)

    var selectedConversationId by mutableStateOf<String?>(null)
    var selectedContactId by mutableStateOf<String?>(null)
    var chatSearchQuery by mutableStateOf("")
    var isChatSearchActive by mutableStateOf(false)
    var chatDrafts = mutableMapOf<String, String>()

    var isNewConversationOpen by mutableStateOf(false)
    var selectedAttachment by mutableStateOf<MessageAttachment?>(null)

    var isDarkMode by mutableStateOf<Boolean?>(null)
    var useCompactDensity by mutableStateOf(false)
    var currentUser by mutableStateOf(MockCommunicationData.currentUser)

    fun selectConversation(id: String?) {
        isChatSearchActive = false
        selectedConversationId = id
        if (id != null) {
            conversations = conversations.map {
                if (it.id == id) it.copy(unreadCount = 0) else it
            }
        }
    }

    fun sendChatMessage(conversationId: String, content: String) {
        val newMessage = Message(
            id = "m_new_${Clock.System.now().toEpochMilliseconds()}",
            conversationId = conversationId,
            sender = MockCommunicationData.currentUser,
            content = content,
            timestamp = Clock.System.now(),
            deliveryStatus = MessageDeliveryStatus.Sent
        )
        messages = messages + newMessage
        conversations = conversations.map {
            if (it.id == conversationId) it.copy(lastMessageAt = newMessage.timestamp) else it
        }.sortedByDescending { it.lastMessageAt }
        chatDrafts.remove(conversationId)
    }



    val visibleConversations: List<Conversation>
        get() = if (chatSearchQuery.isBlank()) conversations else conversations.filter {
            it.title.contains(chatSearchQuery, ignoreCase = true)
        }
}
