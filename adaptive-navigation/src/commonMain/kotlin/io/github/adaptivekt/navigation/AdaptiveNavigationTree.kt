package io.github.adaptivekt.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

public data class AdaptiveNavigationTreeItem(
    val id: String,
    val label: String,
    val children: List<AdaptiveNavigationTreeItem> = emptyList(),
    val enabled: Boolean = true,
    val badge: String? = null,
)

/**
 * Controlled hierarchical navigation tree for nested sidebars and settings panels.
 *
 * @param items List of root navigation tree items.
 * @param selectedItemId The currently selected item ID.
 * @param onItemSelected Callback invoked when an item is selected.
 * @param expandedItemIds Set of currently expanded parent item IDs.
 * @param onExpandedItemIdsChange Callback invoked when the expansion state changes.
 * @param modifier Modifier applied to the root tree container.
 * @param maxDepth Maximum rendering depth for nested children.
 * @param itemStyle Visual style for the tree items.
 * @param density Padding density for the tree items.
 */
@Composable
public fun AdaptiveNavigationTree(
    items: List<AdaptiveNavigationTreeItem>,
    selectedItemId: String?,
    onItemSelected: (AdaptiveNavigationTreeItem) -> Unit,
    expandedItemIds: Set<String>,
    onExpandedItemIdsChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    maxDepth: Int = 6,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall),
    ) {
        items.forEach { item ->
            NavigationTreeNode(
                item = item,
                depth = 0,
                maxDepth = maxDepth.coerceAtLeast(0),
                selectedItemId = selectedItemId,
                expandedItemIds = expandedItemIds,
                onExpandedItemIdsChange = onExpandedItemIdsChange,
                onItemSelected = onItemSelected,
                itemStyle = itemStyle,
                density = density,
            )
        }
    }
}

@Composable
private fun NavigationTreeNode(
    item: AdaptiveNavigationTreeItem,
    depth: Int,
    maxDepth: Int,
    selectedItemId: String?,
    expandedItemIds: Set<String>,
    onExpandedItemIdsChange: (Set<String>) -> Unit,
    onItemSelected: (AdaptiveNavigationTreeItem) -> Unit,
    itemStyle: AdaptiveNavigationItemStyle,
    density: AdaptiveNavigationDensity,
) {
    val hasChildren = item.children.isNotEmpty() && depth < maxDepth
    val expanded = item.id in expandedItemIds

    NavigationTreeRow(
        item = item,
        depth = depth,
        selected = item.id == selectedItemId,
        expanded = expanded,
        hasChildren = hasChildren,
        onClick = {
            if (!item.enabled) return@NavigationTreeRow
            if (hasChildren) {
                onExpandedItemIdsChange(
                    if (expanded) expandedItemIds - item.id else expandedItemIds + item.id,
                )
            }
            onItemSelected(item)
        },
        itemStyle = itemStyle,
        density = density,
    )

    if (hasChildren && expanded) {
        item.children.forEach { child ->
            NavigationTreeNode(
                item = child,
                depth = depth + 1,
                maxDepth = maxDepth,
                selectedItemId = selectedItemId,
                expandedItemIds = expandedItemIds,
                onExpandedItemIdsChange = onExpandedItemIdsChange,
                onItemSelected = onItemSelected,
                itemStyle = itemStyle,
                density = density,
            )
        }
    }
}

@Composable
private fun NavigationTreeRow(
    item: AdaptiveNavigationTreeItem,
    depth: Int,
    selected: Boolean,
    expanded: Boolean,
    hasChildren: Boolean,
    onClick: () -> Unit,
    itemStyle: AdaptiveNavigationItemStyle,
    density: AdaptiveNavigationDensity,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = if (itemStyle == AdaptiveNavigationItemStyle.Card) AdaptiveTheme.shapes.medium else AdaptiveTheme.shapes.small
    val enabled = item.enabled
    val background = when {
        !enabled -> Color.Transparent
        selected && itemStyle == AdaptiveNavigationItemStyle.Minimal -> AdaptiveTheme.colors.surfaceRaised
        selected -> AdaptiveTheme.colors.primarySubtle
        hovered && itemStyle == AdaptiveNavigationItemStyle.Minimal -> AdaptiveTheme.colors.surfaceMuted
        hovered -> AdaptiveTheme.colors.surfaceRaised
        else -> Color.Transparent
    }
    val border = if (itemStyle == AdaptiveNavigationItemStyle.Card && selected) AdaptiveTheme.colors.primary else Color.Transparent
    val textColor = when {
        !enabled -> AdaptiveTheme.colors.disabledText
        selected && itemStyle == AdaptiveNavigationItemStyle.Minimal -> AdaptiveTheme.colors.textPrimary
        selected -> AdaptiveTheme.colors.primaryText
        else -> AdaptiveTheme.colors.textSecondary
    }
    val minHeight = when (density) {
        AdaptiveNavigationDensity.Compact -> 40.dp
        AdaptiveNavigationDensity.Comfortable -> AdaptiveTokens.Sizes.NavItemHeight
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .clip(shape)
            .background(background, shape)
            .border(1.dp, border, shape)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(
                start = AdaptiveTokens.Spacing.Small + (depth * 16).dp,
                end = AdaptiveTokens.Spacing.Small,
                top = if (density == AdaptiveNavigationDensity.Compact) 6.dp else AdaptiveTokens.Spacing.Small,
                bottom = if (density == AdaptiveNavigationDensity.Compact) 6.dp else AdaptiveTokens.Spacing.Small,
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.size(18.dp), contentAlignment = Alignment.Center) {
            if (hasChildren) {
                if (expanded) {
                    AdaptiveIcons.ChevronDown(size = 15.dp, tint = textColor)
                } else {
                    AdaptiveIcons.ChevronRight(size = 15.dp, tint = textColor)
                }
            }
        }
        Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
        BasicText(
            text = item.label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (!item.badge.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            AdaptiveBadge(
                text = item.badge,
                tone = if (selected) AdaptiveBadgeTone.Info else AdaptiveBadgeTone.Neutral,
            )
        }
    }
}
