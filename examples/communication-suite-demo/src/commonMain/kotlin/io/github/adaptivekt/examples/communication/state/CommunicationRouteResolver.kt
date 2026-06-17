package io.github.adaptivekt.examples.communication.state

object CommunicationRouteResolver {

    fun resolve(hash: String, state: CommunicationState) {
        val path = hash.removePrefix("#").removePrefix("/")
        val segments = path.split("/")
        if (segments.isEmpty() || segments[0].isEmpty()) return

        when (segments[0]) {
            "chat" -> {
                state.activeArea = AppArea.Chat
                if (segments.size > 1) {
                    when (segments[1]) {
                        "inbox" -> state.selectedConversationId = null
                        "conversation" -> {
                            if (segments.size > 2) {
                                val slug = segments[2]
                                val conv = state.conversations.find { it.slug == slug }
                                if (conv != null) {
                                    state.selectConversation(conv.id)
                                } else {
                                    state.selectedConversationId = null // Unknown slug
                                }
                            }
                        }
                    }
                }
            }
            "mail" -> {
                state.activeArea = AppArea.Mail
                if (segments.size > 1) {
                    when (segments[1]) {
                        "inbox" -> state.selectedMailThreadId = null
                        "thread" -> {
                            if (segments.size > 2) {
                                val slug = segments[2]
                                val thread = state.mailThreads.find { it.slug == slug }
                                if (thread != null) {
                                    state.selectMailThread(thread.id)
                                } else {
                                    state.selectedMailThreadId = null // Unknown slug
                                }
                            }
                        }
                        "compose" -> {
                            state.isComposeMailOpen = true
                        }
                    }
                }
            }
            "settings" -> {
                state.activeArea = AppArea.Settings
            }
        }
    }

    fun generateHash(state: CommunicationState): String {
        return when (state.activeArea) {
            AppArea.Chat -> {
                val convId = state.selectedConversationId
                if (convId != null) {
                    val slug = state.conversations.find { it.id == convId }?.slug ?: convId
                    "#/chat/conversation/$slug"
                } else {
                    "#/chat/inbox"
                }
            }
            AppArea.Mail -> {
                if (state.isComposeMailOpen) {
                    "#/mail/compose"
                } else {
                    val threadId = state.selectedMailThreadId
                    if (threadId != null) {
                        val slug = state.mailThreads.find { it.id == threadId }?.slug ?: threadId
                        "#/mail/thread/$slug"
                    } else {
                        "#/mail/inbox"
                    }
                }
            }
            AppArea.Settings -> "#/settings"
        }
    }
}
