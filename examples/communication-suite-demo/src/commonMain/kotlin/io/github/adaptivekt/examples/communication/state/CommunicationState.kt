package io.github.adaptivekt.examples.communication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.adaptivekt.examples.communication.data.MockCommunicationData
import io.github.adaptivekt.examples.communication.model.*
import kotlinx.datetime.Clock

enum class AppArea {
    Chat, Mail, Settings
}

class CommunicationState {
    var activeArea by mutableStateOf(AppArea.Chat)

    var conversations by mutableStateOf(MockCommunicationData.conversations)
    var messages by mutableStateOf(MockCommunicationData.messages)
    var mailThreads by mutableStateOf(MockCommunicationData.mailThreads)

    var selectedConversationId by mutableStateOf<String?>(null)
    var chatSearchQuery by mutableStateOf("")
    var isChatSearchActive by mutableStateOf(false)
    var chatDrafts = mutableMapOf<String, String>()

    var selectedMailFolder by mutableStateOf(MailFolder.Inbox)
    var selectedMailThreadId by mutableStateOf<String?>(null)
    var mailSearchQuery by mutableStateOf("")

    var isComposeMailOpen by mutableStateOf(false)
    var isNewConversationOpen by mutableStateOf(false)
    var selectedAttachment by mutableStateOf<MessageAttachment?>(null)

    var isDarkMode by mutableStateOf<Boolean?>(null)
    var useCompactDensity by mutableStateOf(false)

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

    fun selectMailFolder(folder: MailFolder) {
        isComposeMailOpen = false
        selectedMailFolder = folder
        selectedMailThreadId = null
    }

    fun selectMailThread(id: String?) {
        isComposeMailOpen = false
        selectedMailThreadId = id
        if (id != null) {
            mailThreads = mailThreads.map { thread ->
                if (thread.id == id && !thread.isRead) {
                    thread.copy(messages = thread.messages.map { it.copy(isRead = true) })
                } else thread
            }
        }
    }

    fun toggleMailStar(threadId: String) {
        mailThreads = mailThreads.map { thread ->
            if (thread.id == threadId) {
                val newMessages = thread.messages.toMutableList()
                if (newMessages.isNotEmpty()) {
                    newMessages[0] = newMessages[0].copy(isStarred = !newMessages[0].isStarred)
                }
                thread.copy(messages = newMessages)
            } else thread
        }
    }

    fun archiveMailThread(threadId: String) {
        mailThreads = mailThreads.map { thread ->
            if (thread.id == threadId) thread.copy(folder = MailFolder.Archive) else thread
        }
        if (selectedMailThreadId == threadId) selectedMailThreadId = null
    }

    val visibleConversations: List<Conversation>
        get() = if (chatSearchQuery.isBlank()) conversations else conversations.filter {
            it.title.contains(chatSearchQuery, ignoreCase = true)
        }

    val visibleMailThreads: List<MailThread>
        get() = mailThreads.filter { it.folder == selectedMailFolder }
            .filter { if (mailSearchQuery.isBlank()) true else it.subject.contains(mailSearchQuery, ignoreCase = true) }
            .sortedByDescending { it.latestMessage?.timestamp ?: Clock.System.now() }
}
