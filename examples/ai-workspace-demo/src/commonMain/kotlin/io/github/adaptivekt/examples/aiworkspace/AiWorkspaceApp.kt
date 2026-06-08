package io.github.adaptivekt.examples.aiworkspace

import io.github.adaptivekt.examples.aiworkspace.ui.components.AiNavGlyph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
// removed icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.adaptivekt.core.AdaptiveApp
import io.github.adaptivekt.navigation.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRouteCodec
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.screens.assistants.AssistantsScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.chat.ChatWorkspaceScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.dashboard.DashboardScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.evaluations.EvaluationsScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.knowledge.KnowledgeBaseScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.playground.PlaygroundScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.prompts.PromptLibraryScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.settings.SettingsScreen
import io.github.adaptivekt.examples.aiworkspace.ui.screens.tools.ToolsScreen

@Composable
public fun AiWorkspaceApp(
    platformHistoryTracker: PlatformHistoryTracker? = null
) {
    val store = remember { AiWorkspaceStore() }

    AdaptiveApp {
        val navigator = rememberAdaptiveNavigator(
            initialRoute = AiRoute.Dashboard,
            codec = AiRouteCodec
        )

        AdaptiveNavigationScaffold(
            navItems = listOf(
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "dashboard",
                    label = "Dashboard",
                    icon = { AiNavGlyph("D") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "chats",
                    label = "Chats",
                    icon = { AiNavGlyph("C") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "prompts",
                    label = "Prompts",
                    icon = { AiNavGlyph("P") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "assistants",
                    label = "Assistants",
                    icon = { AiNavGlyph("A") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "playground",
                    label = "Playground",
                    icon = { AiNavGlyph("PG") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "knowledge",
                    label = "Knowledge Base",
                    icon = { AiNavGlyph("K") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "tools",
                    label = "Tools",
                    icon = { AiNavGlyph("T") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "evaluations",
                    label = "Evaluations",
                    icon = { AiNavGlyph("E") }
                ),
                io.github.adaptivekt.navigation.AdaptiveNavItem(
                    id = "settings",
                    label = "Settings",
                    icon = { AiNavGlyph("S") }
                )
            ),
            selectedItemId = when (navigator.currentRoute) {
                is AiRoute.Dashboard -> "dashboard"
                is AiRoute.Chats, is AiRoute.Chat -> "chats"
                is AiRoute.Prompts, is AiRoute.PromptDetail -> "prompts"
                is AiRoute.Assistants, is AiRoute.AssistantDetail -> "assistants"
                is AiRoute.Playground -> "playground"
                is AiRoute.KnowledgeBase, is AiRoute.FileDetail -> "knowledge"
                is AiRoute.Tools, is AiRoute.ToolDetail -> "tools"
                is AiRoute.Evaluations, is AiRoute.EvaluationDetail -> "evaluations"
                is AiRoute.Settings -> "settings"
                else -> "dashboard"
            },
            onItemSelected = { id ->
                when (id) {
                    "dashboard" -> navigator.navigate(AiRoute.Dashboard)
                    "chats" -> navigator.navigate(AiRoute.Chats)
                    "prompts" -> navigator.navigate(AiRoute.Prompts)
                    "assistants" -> navigator.navigate(AiRoute.Assistants)
                    "playground" -> navigator.navigate(AiRoute.Playground)
                    "knowledge" -> navigator.navigate(AiRoute.KnowledgeBase)
                    "tools" -> navigator.navigate(AiRoute.Tools)
                    "evaluations" -> navigator.navigate(AiRoute.Evaluations)
                    "settings" -> navigator.navigate(AiRoute.Settings)
                }
            },
            content = {
                when (val route = navigator.currentRoute) {
                    is AiRoute.Dashboard -> DashboardScreen(store, navigator)
                    is AiRoute.Chats -> ChatWorkspaceScreen(store, navigator, null)
                    is AiRoute.Chat -> ChatWorkspaceScreen(store, navigator, route.id)
                    is AiRoute.Prompts -> PromptLibraryScreen(store, navigator, null)
                    is AiRoute.PromptDetail -> PromptLibraryScreen(store, navigator, route.id)
                    is AiRoute.Assistants -> AssistantsScreen(store, navigator, null)
                    is AiRoute.AssistantDetail -> AssistantsScreen(store, navigator, route.id)
                    is AiRoute.Playground -> PlaygroundScreen(store, navigator)
                    is AiRoute.KnowledgeBase -> KnowledgeBaseScreen(store, navigator, null)
                    is AiRoute.FileDetail -> KnowledgeBaseScreen(store, navigator, route.id)
                    is AiRoute.Tools -> ToolsScreen(store, navigator, null)
                    is AiRoute.ToolDetail -> ToolsScreen(store, navigator, route.id)
                    is AiRoute.Evaluations -> EvaluationsScreen(store, navigator, null)
                    is AiRoute.EvaluationDetail -> EvaluationsScreen(store, navigator, route.id)
                    is AiRoute.Settings -> SettingsScreen(store, navigator)
                }
            }
        )
    }
}
