package io.github.adaptivekt.examples.communication.state


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommunicationStateTest {

    @Test
    fun testInitialState() {
        val state = CommunicationState()
        assertEquals(AppArea.Chat, state.activeArea)
        assertTrue(state.conversations.isNotEmpty())
        assertTrue(state.messages.isNotEmpty())
    }

    @Test
    fun testSelectConversationClearsUnread() {
        val state = CommunicationState()
        val cId = "c_1"
        
        // Ensure mock has unread initially
        val convBefore = state.conversations.find { it.id == cId }
        assertNotNull(convBefore)
        val initialUnread = convBefore.unreadCount
        assertTrue(initialUnread > 0, "Expected c_1 to have unread messages")

        state.selectConversation(cId)
        
        val convAfter = state.conversations.find { it.id == cId }
        assertNotNull(convAfter)
        assertEquals(0, convAfter.unreadCount)
        assertEquals(cId, state.selectedConversationId)
    }

    @Test
    fun testSendChatMessage() {
        val state = CommunicationState()
        val cId = "c_1"
        val initialMessagesCount = state.messages.size
        
        state.sendChatMessage(cId, "Hello test")
        
        assertEquals(initialMessagesCount + 1, state.messages.size)
        val newMsg = state.messages.last()
        assertEquals("Hello test", newMsg.content)
        assertEquals(cId, newMsg.conversationId)
    }


}
