package io.github.adaptivekt.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

@Composable
public fun <T> AdaptiveBreadcrumbs(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = {
        AdaptiveIcons.ChevronRight(size = 12.dp, tint = AdaptiveTheme.colors.textSecondary)
    },
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall),
    ) {
        items.forEachIndexed { index, item ->
            val selected = item == selectedItem
            BasicText(
                text = itemLabel(item),
                modifier = if (selected) {
                    Modifier
                } else {
                    Modifier
                        .adaptiveInteractiveCursor()
                        .clickable { onItemSelected(item) }
                },
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (selected) AdaptiveTheme.colors.textPrimary else AdaptiveTheme.colors.textSecondary,
                ),
                maxLines = 1,
            )
            if (index < items.lastIndex) {
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                separator()
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            }
        }
    }
}
