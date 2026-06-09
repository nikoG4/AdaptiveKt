package io.github.adaptivekt.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Provides standard adaptive page padding for scrollable or edge-to-edge content.
 * Unlike [AdaptiveContainer], which wraps content in a padded Box that can clip scrolling,
 * [AdaptivePage] provides [PaddingValues] that should be applied to the inner scrollable component
 * (e.g. LazyColumn's contentPadding).
 *
 * @param modifier applied to the root container if needed.
 * @param contentPadding optional override for page padding. If null, inherits from LocalAdaptiveLayoutInfo.
 * @param content the page content builder, receiving the effective padding values.
 */
@Composable
fun AdaptivePage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current
    val effectivePadding = contentPadding ?: PaddingValues(layoutInfo.pagePadding)
    
    // We do not wrap in a Box because AdaptivePage is explicitly designed to 
    // propagate padding values down to the consumer to avoid clipping scrollbars.
    content(effectivePadding)
}

/**
 * A simple vertical page layout that does not scroll by default.
 * Applies the effective adaptive page padding and standard vertical spacing.
 */
@Composable
fun AdaptiveColumnPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    AdaptivePage(modifier = modifier, contentPadding = contentPadding) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

/**
 * A standard scrollable vertical page layout.
 * Wraps content in a vertical scroll container and correctly applies internal padding.
 */
@Composable
fun AdaptiveScrollablePage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AdaptiveTokens.Spacing.Large),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    AdaptivePage(modifier = modifier, contentPadding = contentPadding) { padding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}
