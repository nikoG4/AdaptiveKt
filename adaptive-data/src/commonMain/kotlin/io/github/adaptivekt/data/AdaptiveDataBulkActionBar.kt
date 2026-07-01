package io.github.adaptivekt.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.adaptivekt.components.*
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

@Composable
public fun <K : Any> AdaptiveDataBulkActionBar(
    scope: AdaptiveDataBulkActionScope<K>,
    actions: List<AdaptiveDataBulkAction<K>>,
    modifier: Modifier = Modifier,
) {
    if (scope.selectedCount == 0) return

    val isCompact = LocalAdaptiveLayoutInfo.current.isCompact
    
    val primaryActions = actions.filter { it.priority == AdaptiveActionPriority.Primary }
    val secondaryActions = actions.filter { it.priority == AdaptiveActionPriority.Secondary }
    val overflowActions = actions.filter { it.priority == AdaptiveActionPriority.Overflow }

    val visiblePrimary = primaryActions
    val visibleSecondary = if (isCompact) emptyList() else secondaryActions
    val allOverflow = (if (isCompact) secondaryActions else emptyList()) + overflowActions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AdaptiveTheme.colors.surfaceMuted, androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
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
                content = { AdaptiveIcons.ClearAll() }
            )
            
            BasicText(
                text = "${scope.selectedCount} selected",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp, 
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    color = AdaptiveTheme.colors.textMuted
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
        ) {
            visiblePrimary.forEach { action ->
                AdaptiveButton(
                    text = action.label,
                    onClick = { action.onClick(scope.selectedKeys) },
                    variant = if (action.destructive) AdaptiveButtonVariant.Danger else AdaptiveButtonVariant.Primary,
                    enabled = action.enabled
                )
            }
            
            visibleSecondary.forEach { action ->
                AdaptiveButton(
                    text = action.label,
                    onClick = { action.onClick(scope.selectedKeys) },
                    variant = if (action.destructive) AdaptiveButtonVariant.Danger else AdaptiveButtonVariant.Secondary,
                    enabled = action.enabled
                )
            }

            if (allOverflow.isNotEmpty()) {
                var menuExpanded by remember { mutableStateOf(false) }
                AdaptiveAnchoredDropdownMenu(
                    expanded = menuExpanded,
                    onExpandedChange = { menuExpanded = it },
                    anchor = { expanded, toggle ->
                        AdaptiveIconButton(
                            onClick = toggle,
                            content = { AdaptiveIcons.MoreVertical() }
                        )
                    }
                ) {
                    allOverflow.forEach { action ->
                        AdaptiveButton(
                            text = action.label,
                            onClick = {
                                menuExpanded = false
                                action.onClick(scope.selectedKeys)
                            },
                            variant = if (action.destructive) AdaptiveButtonVariant.Danger else AdaptiveButtonVariant.Ghost,
                            enabled = action.enabled,
                            modifier = Modifier.fillMaxWidth().padding(bottom = AdaptiveTokens.Spacing.XSmall)
                        )
                    }
                }
            }
        }
    }
}
