package io.github.adaptivekt.examples.communication.state

object CommunicationRouteResolver {

    private fun CommunicationState.enterChat() {
        activeArea = AppArea.Chat
        selectedContactId = null
        activeCallId = null
        incomingCallId = null
        if (settingsSection != "home") {
            settingsSection = "home"
        }
        demoState = null
    }

    private fun CommunicationState.enterContacts() {
        activeArea = AppArea.Contacts
        selectedConversationId = null
        isChatSearchActive = false
        activeCallId = null
        incomingCallId = null
        if (settingsSection != "home") {
            settingsSection = "home"
        }
        demoState = null
    }

    private fun CommunicationState.enterCalls() {
        activeArea = AppArea.Calls
        selectedConversationId = null
        selectedContactId = null
        isChatSearchActive = false
        if (settingsSection != "home") {
            settingsSection = "home"
        }
        demoState = null
    }

    private fun CommunicationState.enterSettings() {
        activeArea = AppArea.Settings
        selectedConversationId = null
        selectedContactId = null
        activeCallId = null
        incomingCallId = null
        isChatSearchActive = false
        demoState = null
    }

    private fun CommunicationState.enterDemoScenario(scenario: String) {
        activeArea = AppArea.Chat
        selectedConversationId = null
        selectedContactId = null
        activeCallId = null
        incomingCallId = null
        isChatSearchActive = false
        settingsSection = "home"
        demoState = scenario
    }

    fun resolve(hash: String, state: CommunicationState) {
        val path = hash.removePrefix("#").removePrefix("/")
        val segments = path.split("/")
        if (segments.isEmpty() || segments[0].isEmpty()) return

        when (segments[0]) {
            "chat" -> {
                state.enterChat()

                when (segments.getOrNull(1)) {
                    null, "", "inbox" -> state.selectedConversationId = null
                    "search" -> {
                        state.selectedConversationId = null
                        state.isChatSearchActive = true
                    }
                    "conversation" -> {
                        state.isChatSearchActive = false
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
                state.enterContacts()

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
                        val user = state.conversations.flatMap { it.participants }.find { it.slug == userId || it.id == userId }
                        state.contactsFilter = "all"
                        state.selectedContactId = user?.id ?: userId
                    }
                }
            }

            "calls" -> {
                state.enterCalls()

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
                state.enterSettings()

                val section = segments.getOrNull(1)
                if (section.isNullOrEmpty()) {
                    state.settingsSection = "home"
                } else {
                    state.settingsSection = section
                }
            }

            "demo" -> {
                val scenario = segments.getOrNull(1)
                if (scenario != null) {
                    state.enterDemoScenario(scenario)
                }
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
                    state.selectedContactId != null -> {
                        val contactId = state.selectedContactId!!
                        val slug = state.conversations.flatMap { it.participants }.find { it.id == contactId }?.slug ?: contactId
                        "#/contacts/$slug"
                    }
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
