package io.github.adaptivekt.examples.aiworkspace.model

public data class Conversation(
    val id: String,
    val title: String,
    val assistantId: String,
    val messages: List<ChatMessage>,
    val updatedAt: String,
    val pinned: Boolean,
    val unread: Boolean,
    val tags: List<String>
)

public data class ChatMessage(
    val id: String,
    val role: MessageRole,
    val parts: List<MessagePart>,
    val timestamp: String,
    val tokenCount: Int,
    val status: MessageStatus
)

public sealed interface MessagePart {
    public data class Text(val value: String) : MessagePart
    public data class CodeBlock(val language: String, val code: String) : MessagePart
    public data class ToolCall(val name: String, val status: ToolCallStatus, val summary: String) : MessagePart
    public data class Source(val title: String, val description: String) : MessagePart
}

public enum class ToolCallStatus {
    Pending,
    Success,
    Failed
}

public enum class MessageRole {
    User,
    Assistant,
    System,
    Tool
}

public enum class MessageStatus {
    Complete,
    Streaming,
    Failed
}

public data class AssistantProfile(
    val id: String,
    val name: String,
    val description: String,
    val avatarText: String,
    val model: String,
    val temperature: Float,
    val toolsEnabled: Int,
    val knowledgeFiles: Int,
    val tags: List<String>
)

public data class PromptTemplate(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val tags: List<String>,
    val body: String,
    val variables: List<String>,
    val favorite: Boolean
)

public data class KnowledgeFile(
    val id: String,
    val name: String,
    val type: String,
    val sizeLabel: String,
    val status: FileIndexStatus,
    val chunks: Int,
    val updatedAt: String
)

public enum class FileIndexStatus {
    Uploaded,
    Indexing,
    Ready,
    Failed
}

public data class AiTool(
    val id: String,
    val name: String,
    val description: String,
    val enabled: Boolean,
    val risk: ToolRisk,
    val lastRunStatus: String
)

public enum class ToolRisk {
    Low,
    Medium,
    High
}

public data class EvaluationRun(
    val id: String,
    val name: String,
    val dataset: String,
    val model: String,
    val passRate: Int,
    val avgLatency: String,
    val avgTokens: Int,
    val cases: List<EvaluationCase>
)

public data class EvaluationCase(
    val id: String,
    val prompt: String,
    val expected: String,
    val actual: String,
    val passed: Boolean,
    val score: Float
)

public data class UsageMetric(
    val tokensThisWeek: Int,
    val estimatedCost: Float,
    val activeConversations: Int,
    val filesIndexed: Int,
    val evaluationPassRate: Int,
    val toolsEnabledCount: Int
)

public data class ModelProfile(
    val id: String,
    val name: String,
    val provider: String,
    val contextWindow: Int,
    val supportsTools: Boolean,
    val supportsVision: Boolean
)

public data class AiSettings(
    val defaultModelId: String,
    val defaultTemperature: Float,
    val developerMode: Boolean,
    val saveHistory: Boolean
)
