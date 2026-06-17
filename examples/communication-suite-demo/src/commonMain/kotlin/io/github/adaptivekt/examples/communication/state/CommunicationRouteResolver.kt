package io.github.adaptivekt.examples.communication.state

object CommunicationRouteResolver {

    fun resolve(hash: String, state: CommunicationState) {
        val path = hash.removePrefix("#").removePrefix("/")
        val segments = path.split("/")
        if (segments.isEmpty() || segments[0].isEmpty()) return

        when (segments[0]) {
            "chat" -> {
                state.activeArea = AppArea.Chat
                state.isComposeMailOpen = false
                state.isChatSearchActive = false

                when (segments.getOrNull(1)) {
                    null, "", "inbox" -> state.selectedConversationId = null
                    "search" -> {
                        state.selectedConversationId = null
                        state.isChatSearchActive = true
                    }
                    "conversation" -> {
                        val slug = segments.getOrNull(2)
                        val conversation = state.conversations.find { it.slug == slug }
                        if (conversation != null) {
                            state.selectConversation(conversation.id)
                        } else {
                            state.selectedConversationId = null
                        }
                    }
                }
            }

            "mail" -> {
                state.activeArea = AppArea.Mail
                state.isChatSearchActive = false
                state.isComposeMailOpen = false

                when (segments.getOrNull(1)) {
                    null, "", "inbox" -> state.selectedMailThreadId = null
                    "thread" -> {
                        val slug = segments.getOrNull(2)
                        val thread = state.mailThreads.find { it.slug == slug }
                        if (thread != null) {
                            state.selectMailThread(thread.id)
                        } else {
                            state.selectedMailThreadId = null
                        }
                    }
                    "compose" -> {
                        state.selectedMailThreadId = null
                        state.isComposeMailOpen = true
                    }
                }
            }

            "settings" -> {
                state.activeArea = AppArea.Settings
                state.isChatSearchActive = false
                state.isComposeMailOpen = false
            }
        }
    }

    fun generateHash(state: CommunicationState): String {
        return when (state.activeArea) {
            AppArea.Chat -> {
                when {
                    state.isChatSearchActive -> "#/chat/search"
                    state.selectedConversationId != null -> {
                        val conversationId = state.selectedConversationId!!
                        val slug = state.conversations.find { it.id == conversationId }?.slug ?: conversationId
                        "#/chat/conversation/$slug"
                    }
                    else -> "#/chat/inbox"
                }
            }

            AppArea.Mail -> {
                when {
                    state.isComposeMailOpen -> "#/mail/compose"
                    state.selectedMailThreadId != null -> {
                        val threadId = state.selectedMailThreadId!!
                        val slug = state.mailThreads.find { it.id == threadId }?.slug ?: threadId
                        "#/mail/thread/$slug"
                    }
                    else -> "#/mail/inbox"
                }
            }

            AppArea.Settings -> "#/settings"
        }
    }
}
