package io.github.adaptivekt.examples.communication.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommunicationRouteResolverTest {

    @Test
    fun testValidSlugResolution() {
        val state = CommunicationState()
        CommunicationRouteResolver.resolve("#/chat/conversation/team-alpha", state)
        assertEquals(AppArea.Chat, state.activeArea)
        assertEquals("c_1", state.selectedConversationId)

        CommunicationRouteResolver.resolve("#/mail/thread/product-launch", state)
        assertEquals(AppArea.Mail, state.activeArea)
        assertEquals("t_1", state.selectedMailThreadId)
    }

    @Test
    fun testUnknownSlugBehavior() {
        val state = CommunicationState()
        state.selectConversation("c_1")
        
        // Navigate to unknown slug should clear selection
        CommunicationRouteResolver.resolve("#/chat/conversation/unknown-slug", state)
        assertEquals(AppArea.Chat, state.activeArea)
        assertNull(state.selectedConversationId)
    }

    @Test
    fun testRouteDecodingAndEncoding() {
        val state = CommunicationState()
        
        // Decoding
        CommunicationRouteResolver.resolve("#/chat/conversation/support-desk", state)
        assertEquals("c_2", state.selectedConversationId)
        
        // Encoding
        val hash = CommunicationRouteResolver.generateHash(state)
        assertEquals("#/chat/conversation/support-desk", hash)
    }

    @Test
    fun testStableInternalSelectionAfterSlugNavigation() {
        val state = CommunicationState()
        
        // Navigate by slug
        CommunicationRouteResolver.resolve("#/mail/thread/security-review", state)
        assertEquals("t_2", state.selectedMailThreadId)
        
        // Internal state is stable, hash is generated correctly
        assertEquals("#/mail/thread/security-review", CommunicationRouteResolver.generateHash(state))
    }
}
