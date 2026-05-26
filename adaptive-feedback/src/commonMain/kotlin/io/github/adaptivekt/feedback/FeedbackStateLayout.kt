package io.github.adaptivekt.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Shared layout for feedback states (empty, loading, error).
 * Provides vertical centering with consistent spacing from AdaptiveTokens.
 */
@Composable
internal fun FeedbackStateLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 220.dp),
        contentAlignment = Alignment.Center,
    ) {
        AdaptiveSurface(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(AdaptiveTokens.Spacing.Large),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }
    }
}
