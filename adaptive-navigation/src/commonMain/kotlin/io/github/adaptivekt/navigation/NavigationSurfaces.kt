package io.github.adaptivekt.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
public fun Sidebar(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AdaptiveTheme.colors.surfaceMuted)
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
                    .padding(vertical = AdaptiveTokens.Spacing.Small),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.Sidebar,
            )
        }
    }
}

@Composable
public fun Drawer(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AdaptiveTheme.colors.surface)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border)
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
                    .padding(vertical = AdaptiveTokens.Spacing.Small),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.Sidebar,
            )
        }
    }
}

@Composable
public fun BottomNavigation(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
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
            )
        }
    }
}

@Composable
public fun NavigationRail(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
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
                    .padding(vertical = AdaptiveTokens.Spacing.Small),
                onItemSelected = onItemSelected,
                layout = NavigationItemLayout.NavigationRail,
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
) {
    val itemBackground = if (selected) AdaptiveTheme.colors.primarySubtle else AdaptiveTheme.colors.surface
    val itemBorder = if (selected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.border
    val textColor = if (selected) AdaptiveTheme.colors.textPrimary else AdaptiveTheme.colors.textSecondary
    val glyphBackground = if (selected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.disabledBackground
    val glyphTextColor = if (selected) AdaptiveTheme.colors.textInverse else AdaptiveTheme.colors.textSecondary
    val shape = AdaptiveTheme.shapes.medium

    val itemModifier = modifier
        .clickable { onItemSelected(item.id) }
        .background(itemBackground, shape = shape)
        .border(width = 1.dp, color = itemBorder, shape = shape)
        .padding(AdaptiveTokens.Spacing.Small)

    val labelText = compactNavigationLabel(item.label.ifBlank { item.id })

    when (layout) {
        NavigationItemLayout.Sidebar -> {
            Row(
                modifier = itemModifier
                    .heightIn(min = AdaptiveTokens.Sizes.NavItemHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                NavigationGlyph(item, glyphBackground, glyphTextColor)
                BasicText(
                    text = item.label,
                    modifier = Modifier
                        .padding(start = AdaptiveTokens.Spacing.Medium)
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                        color = textColor,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        NavigationItemLayout.NavigationRail -> {
            Column(
                modifier = itemModifier.heightIn(min = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                NavigationGlyph(item, glyphBackground, glyphTextColor)
                Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Small))
                BasicText(
                    text = labelText,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
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
                NavigationGlyph(item, glyphBackground, glyphTextColor)
                Spacer(modifier = Modifier.size(AdaptiveTokens.Spacing.Small))
                BasicText(
                    text = labelText,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
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
private fun NavigationGlyph(
    item: AdaptiveNavItem,
    background: Color,
    textColor: Color,
) {
    Box(
        modifier = Modifier
            .size(AdaptiveTokens.Sizes.IconBox)
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
