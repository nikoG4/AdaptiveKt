package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
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
 * The public [BoxScope] content API is retained for source compatibility, while direct sibling
 * content is hosted in a vertical flow so grouped text and controls do not accidentally overlap.
 */
@Composable
public fun AdaptiveSurface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = AdaptiveComponentDefaults.MediumShape
    Box(
        modifier = modifier
            .clip(shape)
            .background(AdaptiveComponentDefaults.Surface, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape),
    ) {
        val surfaceScope = this
        Column(modifier = Modifier.padding(contentPadding)) {
            content(surfaceScope)
        }
    }
}
