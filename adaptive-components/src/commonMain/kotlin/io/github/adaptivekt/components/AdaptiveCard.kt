package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

@Composable
public fun AdaptiveCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    onClick: (() -> Unit)? = null,
    contentSelectionEnabled: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = AdaptiveComponentDefaults.MediumShape
    val clickableModifier = if (onClick != null) {
        Modifier
            .adaptiveInteractiveCursor()
            .clickable(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AdaptiveTokens.Sizes.CardMinHeight)
            .clip(shape)
            .background(AdaptiveComponentDefaults.Surface, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape)
            .then(clickableModifier)
            .padding(contentPadding),
    ) {
        if (contentSelectionEnabled && onClick == null) {
            AdaptiveSelectionArea {
                content()
            }
        } else {
            content()
        }
    }
}
