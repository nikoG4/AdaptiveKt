package io.github.adaptivekt.examples.communication.data

import kotlin.test.Test
import kotlin.test.assertTrue

class MockCommunicationDataTest {

    @Test
    fun testMockDataMinimumCounts() {
        val conversations = MockCommunicationData.conversations
        val messages = MockCommunicationData.messages
        val threads = MockCommunicationData.mailThreads
        
        assertTrue(conversations.size >= 12, "Should have at least 12 chat conversations")
        assertTrue(messages.size >= 80, "Should have at least 80 chat messages")
        assertTrue(threads.size >= 20, "Should have at least 20 mail threads")
        
        var mailMessagesCount = 0
        threads.forEach { mailMessagesCount += it.messages.size }
        assertTrue(mailMessagesCount >= 60, "Should have at least 60 mail messages")
    }
}
