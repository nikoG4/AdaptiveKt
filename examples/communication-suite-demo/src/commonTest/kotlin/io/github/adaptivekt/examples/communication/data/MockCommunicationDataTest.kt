package io.github.adaptivekt.examples.communication.data

import kotlin.test.Test
import kotlin.test.assertTrue

class MockCommunicationDataTest {

    @Test
    fun testMockDataMinimumCounts() {
        val conversations = MockCommunicationData.conversations
        val messages = MockCommunicationData.messages
        assertTrue(conversations.size >= 12, "Should have at least 12 chat conversations")
        assertTrue(messages.size >= 80, "Should have at least 80 chat messages")
    }
}
