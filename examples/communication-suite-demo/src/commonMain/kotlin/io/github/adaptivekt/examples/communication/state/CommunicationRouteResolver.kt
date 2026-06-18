package io.github.adaptivekt.examples.communication.state

object CommunicationRouteResolver {

    fun resolve(hash: String, state: CommunicationState) {
        val path = hash.removePrefix("#").removePrefix("/")
        val segments = path.split("/")
        if (segments.isEmpty() || segments[0].isEmpty()) return

        when (segments[0]) {
            "chat" -> {
                state.activeArea = AppArea.Chat
                state.isChatSearchActive = false

                when (segments.getOrNull(1)) {
                    null, "", "inbox" -> state.selectedConversationId = null
                    "search" -> {
                        state.selectedConversationId = null
                        state.isChatSearchActive = true
                    }
                    "conversation" -> {
                        val slug = segments.getOrNull(2)
                        val conversation = state.conversations.find { it.slug == slug || it.id == slug }
                        if (conversation != null) {
                            state.selectConversation(conversation.id)
                        } else {
                            state.selectedConversationId = null
                        }
                    }
                }
            }

            "contacts" -> {
                state.activeArea = AppArea.Contacts
                state.isChatSearchActive = false

                when (segments.getOrNull(1)) {
                    null, "", "all" -> {
                        state.contactsFilter = "all"
                        state.selectedContactId = null
                    }
                    "favorites" -> {
                        state.contactsFilter = "favorites"
                        state.selectedContactId = null
                    }
                    "pending" -> {
                        state.contactsFilter = "pending"
                        state.selectedContactId = null
                    }
                    "blocked" -> {
                        state.contactsFilter = "blocked"
                        state.selectedContactId = null
                    }
                    else -> {
                        val userId = segments[1]
                        state.contactsFilter = "all"
                        state.selectedContactId = userId
                    }
                }
            }

            "calls" -> {
                state.activeArea = AppArea.Calls
                state.isChatSearchActive = false

                when (segments.getOrNull(1)) {
                    null, "", "history" -> {
                        state.callsFilter = "history"
                        state.activeCallId = null
                        state.incomingCallId = null
                    }
                    "missed" -> {
                        state.callsFilter = "missed"
                        state.activeCallId = null
                        state.incomingCallId = null
                    }
                    "incoming" -> {
                        val id = segments.getOrNull(2)
                        state.incomingCallId = id
                        state.activeCallId = null
                    }
                    "outgoing", "active" -> {
                        val id = segments.getOrNull(2)
                        state.activeCallId = id
                        state.incomingCallId = null
                    }
                }
            }

            "settings" -> {
                state.activeArea = AppArea.Settings
                state.isChatSearchActive = false

                val section = segments.getOrNull(1)
                if (section.isNullOrEmpty()) {
                    state.settingsSection = "home"
                } else {
                    state.settingsSection = section
                }
            }

            "demo" -> {
                state.demoState = segments.getOrNull(1)
            }
        }
    }

    fun generateHash(state: CommunicationState): String {
        if (state.demoState != null) {
            return "#/demo/${state.demoState}"
        }

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

            AppArea.Contacts -> {
                when {
                    state.selectedContactId != null -> "#/contacts/${state.selectedContactId}"
                    state.contactsFilter != "all" -> "#/contacts/${state.contactsFilter}"
                    else -> "#/contacts"
                }
            }

            AppArea.Calls -> {
                when {
                    state.activeCallId != null -> "#/calls/active/${state.activeCallId}"
                    state.incomingCallId != null -> "#/calls/incoming/${state.incomingCallId}"
                    state.callsFilter == "missed" -> "#/calls/missed"
                    else -> "#/calls"
                }
            }

            AppArea.Settings -> {
                if (state.settingsSection == "home") "#/settings" else "#/settings/${state.settingsSection}"
            }
        }
    }
}
