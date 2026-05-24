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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import io.github.adaptivekt.core.AdaptiveTokens

private val NavigationSurfaceBackground = Color(0xFFF6F8FB)
private val NavigationItemBackground = Color(0xFFFFFFFF)
private val NavigationItemSelectedBackground = Color(0xFFE8F1FF)
private val NavigationItemBorder = Color(0xFFE1E7EF)
private val NavigationItemSelectedBorder = Color(0xFF2F7DF6)
private val NavigationGlyphBackground = Color(0xFFE9EEF6)
private val NavigationGlyphSelectedBackground = Color(0xFF2563EB)
private val NavigationTextColor = Color(0xFF334155)
private val NavigationTextSelectedColor = Color(0xFF0F172A)
private val NavigationGlyphTextColor = Color(0xFF334155)

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
            .background(NavigationSurfaceBackground)
            .padding(horizontal = AdaptiveTokens.Spacing.Large, vertical = AdaptiveTokens.Spacing.XLarge),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        BasicText(
            text = "AdaptiveKt Admin",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        BasicText(
            text = "Workspace overview",
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)),
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
            .background(Color.White)
            .border(width = 1.dp, color = NavigationItemBorder)
            .padding(horizontal = AdaptiveTokens.Spacing.Large, vertical = AdaptiveTokens.Spacing.XLarge),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        BasicText(
            text = "AdaptiveKt Admin Demo",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)),
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
            .background(Color.White)
            .border(width = 1.dp, color = NavigationItemBorder)
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
            .background(NavigationSurfaceBackground)
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
    val itemBackground = if (selected) NavigationItemSelectedBackground else NavigationItemBackground
    val itemBorder = if (selected) NavigationItemSelectedBorder else NavigationItemBorder
    val textColor = if (selected) NavigationTextSelectedColor else NavigationTextColor
    val glyphBackground = if (selected) NavigationGlyphSelectedBackground else NavigationGlyphBackground
    val glyphTextColor = if (selected) Color.White else NavigationGlyphTextColor
    val shape = RoundedCornerShape(AdaptiveTokens.Radius.Medium)

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
            .background(background, shape = RoundedCornerShape(AdaptiveTokens.Radius.Pill)),
        contentAlignment = Alignment.Center,
    ) {
        if (item.icon != null) {
            item.icon.invoke()
        } else {
            BasicText(
                text = item.label.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor),
            )
        }
    }
}

private fun compactNavigationLabel(label: String): String {
    val firstWord = label.trim().split(' ').firstOrNull().orEmpty()
    val source = firstWord.ifBlank { label.trim() }
    return if (source.length <= 6) source else source.take(6)
}
