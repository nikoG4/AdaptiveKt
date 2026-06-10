package io.github.adaptivekt.examples.aiworkspace.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveBreakpoint
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo
import io.github.adaptivekt.data.AdaptiveCollectionDisplayMode
import io.github.adaptivekt.data.AdaptiveCollectionView
import io.github.adaptivekt.examples.aiworkspace.model.Conversation
import io.github.adaptivekt.examples.aiworkspace.model.FileIndexStatus
import io.github.adaptivekt.examples.aiworkspace.model.PromptTemplate
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.AiGlyph
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun DashboardScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    if (layoutInfo.isCompact) {
        CompactDashboardScreen(store, navigator)
        return
    }

    val metricSpan = when (layoutInfo.breakpoint) {
        AdaptiveBreakpoint.Compact -> 12
        AdaptiveBreakpoint.Medium -> 6
        AdaptiveBreakpoint.Expanded,
        AdaptiveBreakpoint.Large -> 3
    }
    val mainSpan = if (layoutInfo.isCompact) 12 else 7
    val sideSpan = if (layoutInfo.isCompact) 12 else 5

    AdaptiveScrollablePage(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XLarge),
    ) {
        AdaptiveActionBar(
            leadingContent = {
                Column {
                    Text(
                        text = "AI Workspace",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AdaptiveTheme.colors.textPrimary,
                    )
                    Text(
                        text = "Compose-ready operations console for prompts, chats, tools and evaluations.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
            },
            secondaryActions = {
                AdaptiveBadge(text = "System theme", tone = AdaptiveBadgeTone.Info)
                AdaptiveBadge(text = "All systems normal", tone = AdaptiveBadgeTone.Success)
            },
            primaryAction = {
                AdaptiveButton(
                    text = "New chat",
                    onClick = { navigator.navigate(AiRoute.Chats) },
                    leadingIcon = {
                        AdaptiveIcons.Plus(size = 16.dp, tint = AdaptiveTheme.colors.textInverse)
                    },
                )
            },
        )

        WorkspaceHero(navigator)

        AdaptiveSection(
            title = "Usage Overview",
            subtitle = "A wide responsive metric grid driven by AdaptiveGrid.",
        ) {
            AdaptiveGrid(columns = 12) {
                item(span = metricSpan) {
                    MetricCard("Conversations", store.metrics.activeConversations.toString(), "+18 this week", "C", AdaptiveBadgeTone.Success)
                }
                item(span = metricSpan) {
                    MetricCard("Tokens today", "1.45M", "68% of planned budget", "T", AdaptiveBadgeTone.Info)
                }
                item(span = metricSpan) {
                    MetricCard("Monthly cost", "$14.50", "Projected $42.80", "$", AdaptiveBadgeTone.Neutral)
                }
                item(span = metricSpan) {
                    MetricCard("Files indexed", store.metrics.filesIndexed.toString(), "8 sources refreshed", "K", AdaptiveBadgeTone.Success)
                }
                item(span = metricSpan) {
                    MetricCard("Eval pass rate", "${store.metrics.evaluationPassRate}%", "Nightly benchmark", "E", AdaptiveBadgeTone.Success)
                }
                item(span = metricSpan) {
                    MetricCard("Active tools", store.metrics.toolsEnabledCount.toString(), "2 need review", "T", AdaptiveBadgeTone.Warning)
                }
                item(span = metricSpan) {
                    MetricCard("Saved prompts", store.prompts.count { it.favorite }.toString(), "Favorites ready", "P", AdaptiveBadgeTone.Info)
                }
                item(span = metricSpan) {
                    MetricCard("Model health", "99.9%", "Gemini 1.5 Pro", "M", AdaptiveBadgeTone.Success)
                }
            }
        }

        AdaptiveGrid(
            columns = 12,
            horizontalGap = AdaptiveTokens.Spacing.Large,
            verticalGap = AdaptiveTokens.Spacing.Large,
        ) {
            item(span = mainSpan) {
                RecentConversationsCard(
                    conversations = store.conversations.take(4),
                    onOpen = { navigator.navigate(AiRoute.Chat(it.id)) },
                )
            }
            item(span = sideSpan) {
                QuickActionsCard(navigator)
            }
            item(span = mainSpan) {
                PromptRecommendationsCard(
                    prompts = store.prompts.filter { it.favorite }.take(4),
                    onOpen = { navigator.navigate(AiRoute.PromptDetail(it.id)) },
                )
            }
            item(span = sideSpan) {
                WorkspaceStatusCard(store)
            }
        }
    }
}

@Composable
private fun CompactDashboardScreen(
    store: AiWorkspaceStore,
    navigator: AdaptiveNavigator<AiRoute>,
) {
    AdaptiveScrollablePage(
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
    ) {
        AdaptiveActionBar(
            leadingContent = {
                Column {
                    Text(
                        text = "AI Workspace",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = AdaptiveTheme.colors.textPrimary,
                    )
                    Text(
                        text = "Adaptive AI operations on mobile.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AdaptiveTheme.colors.textSecondary,
                    )
                }
            },
        )

        AdaptiveCard {
            HeroCopy(navigator)
        }

        AdaptiveSection(title = "Usage Overview") {
            Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium)) {
                MetricCard("Conversations", store.metrics.activeConversations.toString(), "+18 this week", "C", AdaptiveBadgeTone.Success)
                MetricCard("Tokens today", "1.45M", "68% of planned budget", "T", AdaptiveBadgeTone.Info)
                MetricCard("Eval pass rate", "${store.metrics.evaluationPassRate}%", "Nightly benchmark", "E", AdaptiveBadgeTone.Success)
                MetricCard("Active tools", store.metrics.toolsEnabledCount.toString(), "2 need review", "T", AdaptiveBadgeTone.Warning)
            }
        }

        RecentConversationsCard(
            conversations = store.conversations.take(3),
            onOpen = { navigator.navigate(AiRoute.Chat(it.id)) },
        )

        QuickActionsCard(navigator)
    }
}

@Composable
private fun WorkspaceHero(navigator: AdaptiveNavigator<AiRoute>) {
    val compact = LocalAdaptiveLayoutInfo.current.isCompact

    AdaptiveCard(
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.XLarge),
    ) {
        if (compact) {
            HeroCopy(navigator)
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
            ) {
                HeroCopy(navigator, modifier = Modifier.weight(1f))
                HeroStatusPanel()
            }
        }
    }
}

@Composable
private fun HeroCopy(
    navigator: AdaptiveNavigator<AiRoute>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
            AdaptiveBadge(text = "AdaptiveKt Showcase", tone = AdaptiveBadgeTone.Info)
            AdaptiveBadge(text = "Mock data", tone = AdaptiveBadgeTone.Neutral)
        }
        Text(
            text = "Operate AI work from one adaptive command center.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AdaptiveTheme.colors.textPrimary,
        )
        Text(
            text = "A premium demo for multi-pane chat, prompt libraries, knowledge status, model settings and evaluation workflows.",
            style = MaterialTheme.typography.bodyMedium,
            color = AdaptiveTheme.colors.textSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
            AdaptiveButton(
                text = "Start chat",
                onClick = { navigator.navigate(AiRoute.Chats) },
                size = AdaptiveButtonSize.Large,
                leadingIcon = { AdaptiveIcons.Plus(size = 16.dp, tint = AdaptiveTheme.colors.textInverse) },
            )
            AdaptiveButton(
                text = "Prompts",
                onClick = { navigator.navigate(AiRoute.Prompts) },
                size = AdaptiveButtonSize.Large,
                variant = AdaptiveButtonVariant.Secondary,
            )
        }
    }
}

@Composable
private fun HeroStatusPanel() {
    Column(
        modifier = Modifier
            .clip(AdaptiveTheme.shapes.large)
            .background(AdaptiveTheme.colors.primarySubtle)
            .padding(AdaptiveTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
    ) {
        AiGlyph("AI", modifier = Modifier.size(56.dp))
        Text(
            text = "Live ops",
            style = MaterialTheme.typography.titleMedium,
            color = AdaptiveTheme.colors.primaryText,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "8 tools | 45 files | 5 models",
            style = MaterialTheme.typography.labelMedium,
            color = AdaptiveTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    helper: String,
    glyph: String,
    tone: AdaptiveBadgeTone,
) {
    AdaptiveCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AiGlyph(glyph, modifier = Modifier.size(32.dp))
            AdaptiveBadge(text = helper.take(14), tone = tone)
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = AdaptiveTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = AdaptiveTheme.colors.textPrimary,
        )
        Text(
            text = helper,
            style = MaterialTheme.typography.bodySmall,
            color = AdaptiveTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RecentConversationsCard(
    conversations: List<Conversation>,
    onOpen: (Conversation) -> Unit,
) {
    AdaptiveCard {
        SectionCardHeader("Recent conversations", "Open active threads without leaving the dashboard.")
        Column {
            conversations.forEachIndexed { index, conversation ->
                ConversationSummaryRow(conversation = conversation, onClick = { onOpen(conversation) })
                if (index < conversations.lastIndex) {
                    AdaptiveDivider()
                }
            }
        }
    }
}

@Composable
private fun ConversationSummaryRow(
    conversation: Conversation,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdaptiveTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(vertical = AdaptiveTokens.Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        AiGlyph(conversation.title.take(1), modifier = Modifier.size(32.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                conversation.title,
                style = MaterialTheme.typography.titleSmall,
                color = AdaptiveTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                conversation.tags.joinToString(" / "),
                style = MaterialTheme.typography.labelSmall,
                color = AdaptiveTheme.colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        AdaptiveBadge(
            text = if (conversation.unread) "Unread" else conversation.updatedAt,
            tone = if (conversation.unread) AdaptiveBadgeTone.Warning else AdaptiveBadgeTone.Neutral,
        )
    }
}

@Composable
private fun QuickActionsCard(navigator: AdaptiveNavigator<AiRoute>) {
    AdaptiveCard {
        SectionCardHeader("Quick actions", "Common AI workspace tasks.")
        Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
            ActionTile("Open playground", "Compare model outputs", "PG") { navigator.navigate(AiRoute.Playground) }
            ActionTile("Create prompt", "Start from reusable templates", "P") { navigator.navigate(AiRoute.Prompts) }
            ActionTile("Review evaluations", "Inspect latest quality runs", "E") { navigator.navigate(AiRoute.Evaluations) }
            ActionTile("Upload knowledge", "Refresh indexed files", "K") { navigator.navigate(AiRoute.KnowledgeBase) }
        }
    }
}

@Composable
private fun PromptRecommendationsCard(
    prompts: List<PromptTemplate>,
    onOpen: (PromptTemplate) -> Unit,
) {
    AdaptiveCard {
        SectionCardHeader("Recommended prompts", "Favorites shown through AdaptiveCollectionView.")
        AdaptiveCollectionView(
            items = prompts,
            displayMode = AdaptiveCollectionDisplayMode.Grid,
            gridColumns = 2,
            listItemContent = { prompt ->
                PromptRecommendation(prompt, onOpen)
            },
            gridItemContent = { prompt ->
                PromptRecommendation(prompt, onOpen)
            },
        )
    }
}

@Composable
private fun PromptRecommendation(
    prompt: PromptTemplate,
    onOpen: (PromptTemplate) -> Unit,
) {
    AdaptiveCard(
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        onClick = { onOpen(prompt) },
    ) {
        Text(prompt.title, style = MaterialTheme.typography.titleSmall, color = AdaptiveTheme.colors.textPrimary)
        Text(
            prompt.description,
            style = MaterialTheme.typography.bodySmall,
            color = AdaptiveTheme.colors.textSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveBadge(text = prompt.category, tone = AdaptiveBadgeTone.Info)
    }
}

@Composable
private fun WorkspaceStatusCard(store: AiWorkspaceStore) {
    AdaptiveCard {
        SectionCardHeader("Workspace status", "Operational state across tools and knowledge.")
        Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium)) {
            StatusRow("Connected tools", "${store.tools.count { it.enabled }} enabled", AdaptiveBadgeTone.Success)
            StatusRow("Knowledge ready", "${store.files.count { it.status == FileIndexStatus.Ready }} indexed", AdaptiveBadgeTone.Info)
            StatusRow("Risk review", "${store.tools.count { it.risk.name != "Low" }} tools", AdaptiveBadgeTone.Warning)
            StatusRow("Evaluations", "${store.evaluations.firstOrNull()?.passRate ?: 0}% latest", AdaptiveBadgeTone.Success)
        }
    }
}

@Composable
private fun SectionCardHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AdaptiveTheme.colors.textPrimary,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = AdaptiveTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
    }
}

@Composable
private fun ActionTile(title: String, subtitle: String, glyph: String, onClick: () -> Unit) {
    AdaptiveCard(
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
        ) {
            AiGlyph(glyph, modifier = Modifier.size(32.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = AdaptiveTheme.colors.textPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = AdaptiveTheme.colors.textSecondary)
            }
            AdaptiveIcons.ChevronRight(size = 16.dp, tint = AdaptiveTheme.colors.textMuted)
        }
    }
}

@Composable
private fun StatusRow(label: String, value: String, tone: AdaptiveBadgeTone) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = AdaptiveTheme.colors.textPrimary)
        AdaptiveBadge(text = value, tone = tone)
    }
}
