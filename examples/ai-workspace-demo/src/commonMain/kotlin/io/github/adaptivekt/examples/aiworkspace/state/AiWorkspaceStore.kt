package io.github.adaptivekt.examples.aiworkspace.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.adaptivekt.examples.aiworkspace.mock.AiMockData
import io.github.adaptivekt.examples.aiworkspace.model.*
import kotlinx.coroutines.*

public class AiWorkspaceStore {
    public val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    public var conversations: List<Conversation> by mutableStateOf(AiMockData.conversations)
        private set
        
    public var prompts: List<PromptTemplate> by mutableStateOf(AiMockData.prompts)
        private set
        
    public var assistants: List<AssistantProfile> by mutableStateOf(AiMockData.assistants)
        private set
        
    public var tools: List<AiTool> by mutableStateOf(AiMockData.tools)
        private set
        
    public var files: List<KnowledgeFile> by mutableStateOf(AiMockData.files)
        private set
        
    public var evaluations: List<EvaluationRun> by mutableStateOf(AiMockData.evaluations)
        private set
        
    public var settings: AiSettings by mutableStateOf(AiMockData.defaultSettings)
        private set
        
    public var metrics: UsageMetric by mutableStateOf(AiMockData.defaultMetrics)
        private set

    public fun sendMessage(conversationId: String, text: String, role: MessageRole = MessageRole.User) {
        val convIndex = conversations.indexOfFirst { it.id == conversationId }
        if (convIndex == -1) return
        val conv = conversations[convIndex]
        
        val userMsg = ChatMessage(
            id = "msg_${kotlin.random.Random.nextInt()}",
            role = role,
            parts = listOf(MessagePart.Text(text)),
            timestamp = "Just now",
            tokenCount = text.length / 4,
            status = MessageStatus.Complete
        )
        
        // Simulating immediate response
        val assistantMsgId = "msg_ast_${kotlin.random.Random.nextInt()}"
        val assistantMsg = ChatMessage(
            id = assistantMsgId,
            role = MessageRole.Assistant,
            parts = listOf(MessagePart.Text("...")), // Initial typing
            timestamp = "Just now",
            tokenCount = 0,
            status = MessageStatus.Streaming
        )
        
        val updatedConv = conv.copy(
            messages = conv.messages + userMsg + assistantMsg,
            updatedAt = "Just now"
        )
        
        val newConversations = conversations.toMutableList()
        newConversations[convIndex] = updatedConv
        conversations = newConversations

        // Simulate network delay and response update
        scope.launch {
            delay(1500)
            updateMessage(conversationId, assistantMsgId) { msg ->
                msg.copy(
                    parts = listOf(MessagePart.Text("I am a mock assistant. I received your message: \"$text\".")),
                    status = MessageStatus.Complete,
                    tokenCount = 42
                )
            }
        }
    }
    
    private fun updateMessage(conversationId: String, messageId: String, update: (ChatMessage) -> ChatMessage) {
        val convIndex = conversations.indexOfFirst { it.id == conversationId }
        if (convIndex == -1) return
        val conv = conversations[convIndex]
        
        val msgIndex = conv.messages.indexOfFirst { it.id == messageId }
        if (msgIndex == -1) return
        
        val newMessages = conv.messages.toMutableList()
        newMessages[msgIndex] = update(newMessages[msgIndex])
        
        val newConversations = conversations.toMutableList()
        newConversations[convIndex] = conv.copy(messages = newMessages)
        conversations = newConversations
    }

    public fun togglePromptFavorite(promptId: String) {
        prompts = prompts.map { 
            if (it.id == promptId) it.copy(favorite = !it.favorite) else it 
        }
    }

    public fun toggleToolEnabled(toolId: String) {
        tools = tools.map { 
            if (it.id == toolId) it.copy(enabled = !it.enabled) else it 
        }
    }

    public fun updateSettings(newSettings: AiSettings) {
        settings = newSettings
    }

    public fun updateAssistantTemperature(assistantId: String, temperature: Float) {
        assistants = assistants.map {
            if (it.id == assistantId) it.copy(temperature = temperature) else it
        }
    }
}
