package io.github.adaptivekt.examples.aiworkspace.navigation

import io.github.adaptivekt.navigation.AdaptiveRouteCodec

public object AiRouteCodec : AdaptiveRouteCodec<AiRoute> {
    override fun encode(route: AiRoute): String {
        return when (route) {
            is AiRoute.Dashboard -> "/"
            is AiRoute.Chats -> "/chats"
            is AiRoute.Chat -> "/chats/${route.id}"
            is AiRoute.Prompts -> "/prompts"
            is AiRoute.PromptDetail -> "/prompts/${route.id}"
            is AiRoute.Assistants -> "/assistants"
            is AiRoute.AssistantDetail -> "/assistants/${route.id}"
            is AiRoute.Playground -> "/playground"
            is AiRoute.KnowledgeBase -> "/knowledge"
            is AiRoute.FileDetail -> "/knowledge/${route.id}"
            is AiRoute.Tools -> "/tools"
            is AiRoute.ToolDetail -> "/tools/${route.id}"
            is AiRoute.Evaluations -> "/evaluations"
            is AiRoute.EvaluationDetail -> "/evaluations/${route.id}"
            is AiRoute.Settings -> "/settings"
        }
    }

    override fun decode(path: String): AiRoute {
        val parts = path.trim('/').split('/').filter { it.isNotEmpty() }
        if (parts.isEmpty()) return AiRoute.Dashboard

        return when (parts[0]) {
            "chats" -> if (parts.size > 1) AiRoute.Chat(parts[1]) else AiRoute.Chats
            "prompts" -> if (parts.size > 1) AiRoute.PromptDetail(parts[1]) else AiRoute.Prompts
            "assistants" -> if (parts.size > 1) AiRoute.AssistantDetail(parts[1]) else AiRoute.Assistants
            "playground" -> AiRoute.Playground
            "knowledge" -> if (parts.size > 1) AiRoute.FileDetail(parts[1]) else AiRoute.KnowledgeBase
            "tools" -> if (parts.size > 1) AiRoute.ToolDetail(parts[1]) else AiRoute.Tools
            "evaluations" -> if (parts.size > 1) AiRoute.EvaluationDetail(parts[1]) else AiRoute.Evaluations
            "settings" -> AiRoute.Settings
            else -> AiRoute.Dashboard // Fallback for invalid routes
        }
    }
}
