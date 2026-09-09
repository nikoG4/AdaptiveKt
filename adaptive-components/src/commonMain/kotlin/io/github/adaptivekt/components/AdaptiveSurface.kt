package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Neutral framed surface for grouped content.
 *
 * Surface content follows normal vertical flow, matching the grouped-panel use case and avoiding
 * accidental overlap when callers provide more than one child.
 */
@Composable
public fun AdaptiveSurface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = AdaptiveComponentDefaults.MediumShape
    Column(
        modifier = modifier
            .clip(shape)
            .background(AdaptiveComponentDefaults.Surface, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape)
            .padding(contentPadding),
        content = content,
    )
}
