package io.github.adaptivekt.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.*
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

internal data class AdaptiveBulkActionLayout<K : Any>(
    val visiblePrimary: List<AdaptiveDataBulkAction<K>>,
    val visibleSecondary: List<AdaptiveDataBulkAction<K>>,
    val overflow: List<AdaptiveDataBulkAction<K>>
)

internal fun <K : Any> resolveAdaptiveBulkActionLayout(
    actions: List<AdaptiveDataBulkAction<K>>,
    compact: Boolean
): AdaptiveBulkActionLayout<K> {
    val primaryActions = actions.filter { it.priority == AdaptiveActionPriority.Primary }
    val secondaryActions = actions.filter { it.priority == AdaptiveActionPriority.Secondary }
    val overflowActions = actions.filter { it.priority == AdaptiveActionPriority.Overflow }

    return if (compact) {
        val firstPrimary = primaryActions.take(1)
        val overflowPrimary = primaryActions.drop(1)
        AdaptiveBulkActionLayout(
            visiblePrimary = firstPrimary,
            visibleSecondary = emptyList(),
            overflow = overflowPrimary + secondaryActions + overflowActions
        )
    } else {
        AdaptiveBulkActionLayout(
            visiblePrimary = primaryActions,
            visibleSecondary = secondaryActions,
            overflow = overflowActions
        )
    }
}

@Composable
public fun <K : Any> AdaptiveDataBulkActionBar(
    selectedKeys: Set<K>,
    actions: List<AdaptiveDataBulkAction<K>>,
    onClearSelection: () -> Unit,
    modifier: Modifier = Modifier,
    selectedCountLabel: (Int) -> String = { count -> "$count selected" },
) {
    val scope = remember(selectedKeys, onClearSelection) {
        object : AdaptiveDataBulkActionScope<K> {
            override val selectedKeys: Set<K> = selectedKeys
            override val selectedCount: Int = selectedKeys.size
            override fun clearSelection() = onClearSelection()
        }
    }

    AdaptiveDataBulkActionBar(
        scope = scope,
        actions = actions,
        modifier = modifier,
        selectedCountLabel = selectedCountLabel,
    )
}

@Composable
public fun <K : Any> AdaptiveDataBulkActionBar(
    scope: AdaptiveDataBulkActionScope<K>,
    actions: List<AdaptiveDataBulkAction<K>>,
    modifier: Modifier = Modifier,
    selectedCountLabel: (Int) -> String = { count -> "$count selected" },
) {
    if (scope.selectedCount == 0) return

    val isCompact = LocalAdaptiveLayoutInfo.current.isCompact
    val layout = resolveAdaptiveBulkActionLayout(actions, isCompact)

    val clearSelectionContentDescription = "Clear selection"
    val overflowContentDescription = "More bulk actions"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AdaptiveTheme.colors.surfaceMuted, AdaptiveTheme.shapes.small)
            .padding(
                horizontal = AdaptiveTokens.Spacing.Medium,
                vertical = AdaptiveTokens.Spacing.Small
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium)
        ) {
            AdaptiveIconButton(
                onClick = { scope.clearSelection() },
                modifier = Modifier.semantics {
                    this.contentDescription = clearSelectionContentDescription
                },
                content = { AdaptiveIcons.Close() }
            )

            BasicText(
                text = selectedCountLabel(scope.selectedCount),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AdaptiveTheme.colors.textMuted
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
        ) {
            layout.visiblePrimary.forEach { action ->
                AdaptiveButton(
                    text = action.label,
                    onClick = { action.onClick(scope.selectedKeys) },
                    variant = if (action.destructive) AdaptiveButtonVariant.Danger else AdaptiveButtonVariant.Primary,
                    enabled = action.enabled
                )
            }

            layout.visibleSecondary.forEach { action ->
                AdaptiveButton(
                    text = action.label,
                    onClick = { action.onClick(scope.selectedKeys) },
                    variant = if (action.destructive) AdaptiveButtonVariant.Danger else AdaptiveButtonVariant.Secondary,
                    enabled = action.enabled
                )
            }

            if (layout.overflow.isNotEmpty()) {
                var menuExpanded by remember { mutableStateOf(false) }
                AdaptiveAnchoredDropdownMenu(
                    expanded = menuExpanded,
                    onExpandedChange = { menuExpanded = it },
                    anchor = { expanded, toggle ->
                        AdaptiveIconButton(
                            onClick = toggle,
                            modifier = Modifier.semantics {
                                this.contentDescription = overflowContentDescription
                            },
                            content = { AdaptiveIcons.MoreVertical() }
                        )
                    }
                ) {
                    layout.overflow.forEach { action ->
                        AdaptiveMenuItem(
                            text = action.label,
                            onClick = {
                                if (action.enabled) {
                                    menuExpanded = false
                                    action.onClick(scope.selectedKeys)
                                }
                            },
                            destructive = action.destructive,
                            modifier = if (action.enabled) Modifier else Modifier.alpha(0.5f)
                        )
                    }
                }
            }
        }
    }
}
