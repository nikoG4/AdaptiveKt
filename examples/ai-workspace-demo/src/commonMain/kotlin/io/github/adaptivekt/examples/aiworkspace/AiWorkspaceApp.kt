package io.github.adaptivekt.examples.aiworkspace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveApp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveThemeMode
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.navigation.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRouteCodec
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiNavGlyph
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
        AdaptiveTheme(mode = AdaptiveThemeMode.System) {
            val navigator = rememberAdaptiveNavigator(
                initialRoute = AiRoute.Dashboard,
                codec = AiRouteCodec
            )
            val selectedItemId = when (navigator.currentRoute) {
                is AiRoute.Dashboard -> "dashboard"
                is AiRoute.Chats, is AiRoute.Chat -> "chats"
                is AiRoute.Prompts, is AiRoute.PromptDetail -> "prompts"
                is AiRoute.Assistants, is AiRoute.AssistantDetail -> "assistants"
                is AiRoute.Playground -> "playground"
                is AiRoute.KnowledgeBase, is AiRoute.FileDetail -> "knowledge"
                is AiRoute.Tools, is AiRoute.ToolDetail -> "tools"
                is AiRoute.Evaluations, is AiRoute.EvaluationDetail -> "evaluations"
                is AiRoute.Settings -> "settings"
            }

            AdaptiveNavigationScaffold(
                navItems = aiWorkspaceNavItems(),
                selectedItemId = selectedItemId,
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
                navigationBehavior = AdaptiveNavigationBehavior(
                    compact = AdaptiveNavigationPlacement.BottomBar,
                    medium = AdaptiveNavigationPlacement.Rail,
                    expanded = AdaptiveNavigationPlacement.Sidebar,
                    large = AdaptiveNavigationPlacement.Sidebar,
                    overflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu,
                    bottomBarVisibleItemCount = 5,
                    railVisibleItemCount = 6,
                ),
                navigationTitle = "AI Workspace",
                navigationSubtitle = "Adaptive AI workspace",
                navigationItemStyle = AdaptiveNavigationItemStyle.Pill,
                navigationDensity = AdaptiveNavigationDensity.Compact,
                topBar = {
                    AiWorkspaceTopBar(
                        selectedItemId = selectedItemId,
                        onNewChat = { navigator.navigate(AiRoute.Chats) },
                    )
                },
                content = { scaffoldPadding ->
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding)
                    ) {
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
                }
            )
        }
    }
}

private fun aiWorkspaceNavItems(): List<AdaptiveNavItem> = listOf(
    AdaptiveNavItem(id = "dashboard", label = "Home", icon = { AiNavGlyph("D") }),
    AdaptiveNavItem(id = "chats", label = "Chats", icon = { AiNavGlyph("C") }),
    AdaptiveNavItem(id = "prompts", label = "Prompts", icon = { AiNavGlyph("P") }),
    AdaptiveNavItem(id = "assistants", label = "Agents", icon = { AiNavGlyph("A") }),
    AdaptiveNavItem(id = "playground", label = "Lab", icon = { AiNavGlyph("PG") }),
    AdaptiveNavItem(id = "knowledge", label = "Files", icon = { AiNavGlyph("K") }),
    AdaptiveNavItem(id = "tools", label = "Tools", icon = { AiNavGlyph("T") }),
    AdaptiveNavItem(id = "evaluations", label = "Evals", icon = { AiNavGlyph("E") }),
    AdaptiveNavItem(id = "settings", label = "Config", icon = { AiNavGlyph("S") }),
)

@Composable
private fun AiWorkspaceTopBar(
    selectedItemId: String?,
    onNewChat: () -> Unit,
) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    val compact = layoutInfo.isCompact
    val title = when (selectedItemId) {
        "dashboard" -> "AI Workspace"
        "chats" -> "Conversations"
        "prompts" -> "Prompt Library"
        "assistants" -> "Agents"
        "playground" -> "Playground"
        "knowledge" -> "Knowledge Base"
        "tools" -> "Tools"
        "evaluations" -> "Evaluations"
        "settings" -> "Settings"
        else -> "AI Workspace"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = AdaptiveTheme.colors.textPrimary,
            )
            if (!compact) {
                Text(
                    text = "AdaptiveKt showcase workspace",
                    style = MaterialTheme.typography.labelSmall,
                    color = AdaptiveTheme.colors.textMuted,
                )
            }
        }

        if (!compact) {
            Spacer(modifier = Modifier.weight(1f))
            AdaptiveSearchField(
                value = "",
                onValueChange = {},
                placeholder = "Search chats, prompts, files...",
                enabled = false,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Medium))
            AdaptiveBadge(text = "Gemini 1.5 Pro", tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Small))
            AdaptiveBadge(text = "Healthy", tone = AdaptiveBadgeTone.Success)
            Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Medium))
            AdaptiveButton(
                text = "New chat",
                onClick = onNewChat,
                size = AdaptiveButtonSize.Small,
                leadingIcon = {
                    AdaptiveIcons.Plus(
                        size = AdaptiveTokens.Spacing.Medium,
                        tint = AdaptiveTheme.colors.textInverse,
                    )
                },
            )
            Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Medium))
            AdaptiveAvatar(name = "Niko Ovelar", size = 32.dp)
        } else {
            Spacer(modifier = Modifier.weight(1f))
            AdaptiveBadge(text = "Online", tone = AdaptiveBadgeTone.Success)
            Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Small))
            AdaptiveButton(
                text = "New",
                onClick = onNewChat,
                size = AdaptiveButtonSize.Small,
                variant = AdaptiveButtonVariant.Secondary,
            )
        }
    }
}
