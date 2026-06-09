package io.github.adaptivekt.examples.aiworkspace.navigation

public sealed interface AiRoute {
    public data object Dashboard : AiRoute
    public data object Chats : AiRoute
    public data class Chat(val id: String) : AiRoute
    public data object Prompts : AiRoute
    public data class PromptDetail(val id: String) : AiRoute
    public data object Assistants : AiRoute
    public data class AssistantDetail(val id: String) : AiRoute
    public data object Playground : AiRoute
    public data object KnowledgeBase : AiRoute
    public data class FileDetail(val id: String) : AiRoute
    public data object Tools : AiRoute
    public data class ToolDetail(val id: String) : AiRoute
    public data object Evaluations : AiRoute
    public data class EvaluationDetail(val id: String) : AiRoute
    public data object Settings : AiRoute
}
