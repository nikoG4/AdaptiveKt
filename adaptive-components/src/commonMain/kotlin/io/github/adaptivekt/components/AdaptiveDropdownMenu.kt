package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
@Suppress("UNUSED_PARAMETER")
public fun AdaptiveDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return

    val shape = AdaptiveComponentDefaults.LargeShape
    Column(
        modifier = modifier
            .widthIn(min = 180.dp, max = 280.dp)
            .clip(shape)
            .background(AdaptiveComponentDefaults.Surface, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape)
            .padding(AdaptiveTokens.Spacing.Small),
        content = content,
    )
}
