package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

/**
 * A keyboard command for horizontal tabs. Only key-down events are dispatched.
 */
internal enum class AdaptiveTabNavigation { Next, Previous, First, Last }

internal fun resolveAdaptiveTabIndex(
    tabCount: Int,
    selectedIndex: Int,
    action: AdaptiveTabNavigation,
): Int? {
    if (tabCount <= 0) return null
    val current = selectedIndex.takeIf { it in 0 until tabCount } ?: 0
    return when (action) {
        AdaptiveTabNavigation.Next -> (current + 1) % tabCount
        AdaptiveTabNavigation.Previous -> (current + tabCount - 1) % tabCount
        AdaptiveTabNavigation.First -> 0
        AdaptiveTabNavigation.Last -> tabCount - 1
    }
}

/**
 * Segmented horizontal tabs component with scroll support for compact widths.
 *
 * @param tabs List of available tab models.
 * @param selectedTab The currently selected tab.
 * @param onTabSelected Callback invoked when a tab is selected.
 * @param tabLabel Function to resolve the visible text label for a given tab.
 * @param modifier Modifier applied to the root tabs container.
 */
@Composable
public fun <T> AdaptiveTabs(
    tabs: List<T>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    tabLabel: (T) -> String,
    modifier: Modifier = Modifier,
) {
    val focusRequesters = remember(tabs.size) { List(tabs.size) { FocusRequester() } }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val selectedIndex = tabs.indexOf(selectedTab)
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                val command = when (event.key) {
                    Key.DirectionRight -> if (rtl) AdaptiveTabNavigation.Previous else AdaptiveTabNavigation.Next
                    Key.DirectionLeft -> if (rtl) AdaptiveTabNavigation.Next else AdaptiveTabNavigation.Previous
                    Key.MoveHome -> AdaptiveTabNavigation.First
                    Key.MoveEnd -> AdaptiveTabNavigation.Last
                    else -> null
                }
                val next = command?.let { resolveAdaptiveTabIndex(tabs.size, selectedIndex, it) }
                if (next != null) {
                    onTabSelected(tabs[next])
                    focusRequesters[next].requestFocus()
                    true
                } else {
                    false
                }
            },
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = tab == selectedTab
            val background = if (selected) AdaptiveComponentDefaults.PrimarySubtle else AdaptiveComponentDefaults.Surface
            val borderColor = if (selected) AdaptiveComponentDefaults.Primary else AdaptiveComponentDefaults.Border
            val textColor = if (selected) AdaptiveTheme.colors.primaryText else AdaptiveTheme.colors.textPrimary
            val transitionMs = AdaptiveTheme.motion.durationMillis(AdaptiveTheme.motion.fastMillis)
            val selectedBackground by animateColorAsState(background, tween(durationMillis = transitionMs), label = "Tab background")
            val selectedBorder by animateColorAsState(borderColor, tween(durationMillis = transitionMs), label = "Tab border")
            val selectedText by animateColorAsState(textColor, tween(durationMillis = transitionMs), label = "Tab text")

            Box(
                modifier = Modifier
                    .focusRequester(focusRequesters[index])
                    .clip(AdaptiveComponentDefaults.PillShape)
                    .background(selectedBackground)
                    .border(1.dp, selectedBorder, AdaptiveComponentDefaults.PillShape)
                    .adaptiveInteractiveCursor()
                    .selectable(selected = selected, role = Role.Tab, onClick = { onTabSelected(tab) })
                    .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small)
                    .sizeIn(minWidth = 72.dp),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = tabLabel(tab),
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        color = selectedText,
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}
