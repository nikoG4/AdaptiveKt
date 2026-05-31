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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

public enum class AdaptiveNavigationItemStyle {
    Pill,
    Card,
    Minimal,
}

public enum class AdaptiveNavigationDensity {
    Compact,
    Comfortable,
}

@Composable
public fun Sidebar(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AdaptiveTheme.colors.surfaceMuted)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AdaptiveTokens.Spacing.Large, vertical = AdaptiveTokens.Spacing.XLarge),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        BasicText(
            text = "AdaptiveKt Admin",
            style = AdaptiveTheme.typography.subtitle.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AdaptiveTheme.colors.textPrimary,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        BasicText(
            text = "Workspace overview",
            style = AdaptiveTheme.typography.caption.copy(color = AdaptiveTheme.colors.textMuted),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XLarge))

        items.forEach { item ->
            NavigationItem(
                item = item,
                selected = item.id == selectedItemId,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = if (density == AdaptiveNavigationDensity.Compact) 2.dp else 4.dp),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.Sidebar,
                itemStyle = itemStyle,
                density = density,
            )
        }
    }
}

@Composable
public fun Drawer(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AdaptiveTheme.colors.surface)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AdaptiveTokens.Spacing.Large, vertical = AdaptiveTokens.Spacing.XLarge),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        BasicText(
            text = "AdaptiveKt Admin Demo",
            style = AdaptiveTheme.typography.subtitle.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AdaptiveTheme.colors.textPrimary,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))

        items.forEach { item ->
            NavigationItem(
                item = item,
                selected = item.id == selectedItemId,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = if (density == AdaptiveNavigationDensity.Compact) 2.dp else 4.dp),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.Sidebar,
                itemStyle = itemStyle,
                density = density,
            )
        }
    }
}

@Composable
public fun BottomNavigation(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AdaptiveTheme.colors.surface)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border)
            .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.Small),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (items.isEmpty()) return@Row

        items.forEach { item ->
            NavigationItem(
                item = item,
                selected = item.id == selectedItemId,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = AdaptiveTokens.Spacing.Small),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.BottomNavigation,
                itemStyle = itemStyle,
                density = density,
            )
        }
    }
}

@Composable
public fun NavigationRail(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AdaptiveTheme.colors.surfaceMuted)
            .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.Large),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEach { item ->
            NavigationItem(
                item = item,
                selected = item.id == selectedItemId,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = if (density == AdaptiveNavigationDensity.Compact) 2.dp else 4.dp),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.NavigationRail,
                itemStyle = itemStyle,
                density = density,
            )
        }
    }
}

private enum class NavigationItemLayout {
    Sidebar,
    NavigationRail,
    BottomNavigation,
}

@Composable
private fun NavigationItem(
    item: AdaptiveNavItem,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
    layout: NavigationItemLayout,
    itemStyle: AdaptiveNavigationItemStyle,
    density: AdaptiveNavigationDensity,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = when (itemStyle) {
        AdaptiveNavigationItemStyle.Card -> AdaptiveTheme.shapes.medium
        AdaptiveNavigationItemStyle.Pill,
        AdaptiveNavigationItemStyle.Minimal -> AdaptiveTheme.shapes.small
    }
    val itemBackground = when (itemStyle) {
        AdaptiveNavigationItemStyle.Card -> when {
            selected -> AdaptiveTheme.colors.primarySubtle
            hovered -> AdaptiveTheme.colors.surfaceRaised
            else -> AdaptiveTheme.colors.surface
        }
        AdaptiveNavigationItemStyle.Pill -> when {
            selected -> AdaptiveTheme.colors.primarySubtle
            hovered -> AdaptiveTheme.colors.surfaceRaised
            else -> Color.Transparent
        }
        AdaptiveNavigationItemStyle.Minimal -> when {
            selected -> AdaptiveTheme.colors.surfaceRaised
            hovered -> AdaptiveTheme.colors.surfaceMuted
            else -> Color.Transparent
        }
    }
    val itemBorder = when (itemStyle) {
        AdaptiveNavigationItemStyle.Card -> if (selected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.border
        AdaptiveNavigationItemStyle.Pill,
        AdaptiveNavigationItemStyle.Minimal -> Color.Transparent
    }
    val textColor = when {
        selected && itemStyle == AdaptiveNavigationItemStyle.Minimal -> AdaptiveTheme.colors.textPrimary
        selected -> AdaptiveTheme.colors.primaryText
        else -> AdaptiveTheme.colors.textSecondary
    }
    val glyphBackground = if (itemStyle == AdaptiveNavigationItemStyle.Card) {
        if (selected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.disabledBackground
    } else {
        Color.Transparent
    }
    val glyphTextColor = when {
        itemStyle == AdaptiveNavigationItemStyle.Card && selected -> AdaptiveTheme.colors.textInverse
        selected -> AdaptiveTheme.colors.primaryText
        else -> AdaptiveTheme.colors.textMuted
    }
    val minHeight = when (density) {
        AdaptiveNavigationDensity.Compact -> 40.dp
        AdaptiveNavigationDensity.Comfortable -> AdaptiveTokens.Sizes.NavItemHeight
    }
    val railHeight = when (density) {
        AdaptiveNavigationDensity.Compact -> 60.dp
        AdaptiveNavigationDensity.Comfortable -> 66.dp
    }

    val itemModifier = modifier
        .clip(shape)
        .background(itemBackground, shape = shape)
        .border(width = 1.dp, color = itemBorder, shape = shape)
        .hoverable(interactionSource = interactionSource)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { onItemSelected(item.id) },
        )
        .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = if (density == AdaptiveNavigationDensity.Compact) 6.dp else AdaptiveTokens.Spacing.Small)

    val labelText = compactNavigationLabel(item.label.ifBlank { item.id })

    when (layout) {
        NavigationItemLayout.Sidebar -> {
            Row(
                modifier = itemModifier
                    .heightIn(min = minHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                if (itemStyle == AdaptiveNavigationItemStyle.Pill) {
                    ActiveIndicator(selected)
                }
                NavigationGlyph(item, glyphBackground, glyphTextColor, itemStyle)
                BasicText(
                    text = item.label,
                    modifier = Modifier
                        .padding(start = AdaptiveTokens.Spacing.Small)
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        color = textColor,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        NavigationItemLayout.NavigationRail -> {
            Column(
                modifier = itemModifier.heightIn(min = railHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                NavigationGlyph(item, glyphBackground, glyphTextColor, itemStyle)
                Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Small))
                BasicText(
                    text = labelText,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = textColor,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                )
            }
        }
        NavigationItemLayout.BottomNavigation -> {
            Column(
                modifier = itemModifier.heightIn(min = 58.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                NavigationGlyph(item, glyphBackground, glyphTextColor, itemStyle)
                Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Small))
                BasicText(
                    text = labelText,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = textColor,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                )
            }
        }
    }
}

@Composable
private fun ActiveIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(end = AdaptiveTokens.Spacing.Small)
            .size(width = 3.dp, height = 22.dp)
            .background(
                color = if (selected) AdaptiveTheme.colors.primary else Color.Transparent,
                shape = AdaptiveTheme.shapes.pill,
            ),
    )
}

@Composable
private fun NavigationGlyph(
    item: AdaptiveNavItem,
    background: Color,
    textColor: Color,
    itemStyle: AdaptiveNavigationItemStyle,
) {
    val glyphSize = if (itemStyle == AdaptiveNavigationItemStyle.Card) AdaptiveTokens.Sizes.IconBox else 22.dp
    Box(
        modifier = Modifier
            .size(glyphSize)
            .background(background, shape = AdaptiveTheme.shapes.pill),
        contentAlignment = Alignment.Center,
    ) {
        if (item.icon != null) {
            item.icon.invoke()
        } else {
            BasicText(
                text = item.label.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                style = AdaptiveTheme.typography.label.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                ),
            )
        }
    }
}

private fun compactNavigationLabel(label: String): String {
    val firstWord = label.trim().split(' ').firstOrNull().orEmpty()
    val source = firstWord.ifBlank { label.trim() }
    return if (source.length <= 6) source else source.take(6)
}
