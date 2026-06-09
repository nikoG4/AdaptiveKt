package io.github.adaptivekt.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
fun AdaptiveContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = Dp.Unspecified,
    contentPadding: PaddingValues? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current
    val effectiveMaxWidth = if (maxWidth == Dp.Unspecified) layoutInfo.contentMaxWidth else maxWidth
    val effectivePadding = contentPadding ?: PaddingValues(layoutInfo.pagePadding)
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = effectiveMaxWidth)
                .fillMaxWidth()
                .padding(effectivePadding),
            content = content
        )
    }
}
