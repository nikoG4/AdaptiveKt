package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

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
    tabModifier: (T) -> Modifier = { Modifier },
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEach { tab ->
            val selected = tab == selectedTab
            val background = if (selected) AdaptiveComponentDefaults.PrimarySubtle else AdaptiveComponentDefaults.Surface
            val borderColor = if (selected) AdaptiveComponentDefaults.Primary else AdaptiveComponentDefaults.Border
            val textColor = if (selected) AdaptiveTheme.colors.primaryText else AdaptiveTheme.colors.textPrimary

            Box(
                modifier = Modifier
                    .clip(AdaptiveComponentDefaults.PillShape)
                    .background(background)
                    .border(1.dp, borderColor, AdaptiveComponentDefaults.PillShape)
                    .then(tabModifier(tab))
                    .adaptiveInteractiveCursor()
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small)
                    .sizeIn(minWidth = 72.dp),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = tabLabel(tab),
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        color = textColor,
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}
