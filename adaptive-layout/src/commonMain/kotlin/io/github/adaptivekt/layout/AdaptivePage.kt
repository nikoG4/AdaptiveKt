package io.github.adaptivekt.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.AdaptiveTheme

public object AdaptivePaneDefaults {
    public fun contentPadding(): PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Small)

    public fun groupPadding(): PaddingValues = PaddingValues(AdaptiveTokens.Spacing.XSmall)

    public fun itemPadding(): PaddingValues = PaddingValues(
        horizontal = AdaptiveTokens.Spacing.Small,
        vertical = AdaptiveTokens.Spacing.Medium,
    )
}

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

/**
 * A pane-aware scroll container for list/detail side panes.
 *
 * Unlike [AdaptiveScrollablePage], this component intentionally avoids full-page padding. It is
 * designed for list panes where controls and rows should fill the useful pane width rather than
 * appearing as narrow page content inside a wider side pane.
 */
@Composable
public fun AdaptivePaneList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = AdaptivePaneDefaults.contentPadding(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

/**
 * A pane-aware detail layout with scrollable body content and an optional fixed footer.
 *
 * This is useful for chat, inspector, and editor panes where the main content should scroll while
 * actions or compose controls remain anchored to the bottom of the pane.
 */
@Composable
public fun AdaptivePaneDetail(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = AdaptivePaneDefaults.contentPadding(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(contentPadding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )

        if (footer != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                footer()
            }
        }
    }
}

/**
 * Full-width visual group for pane lists, useful for inbox-style rows with dividers.
 */
@Composable
public fun AdaptivePaneListGroup(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = AdaptivePaneDefaults.groupPadding(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AdaptiveTheme.shapes.medium)
            .background(AdaptiveTheme.colors.surface, AdaptiveTheme.shapes.medium)
            .border(1.dp, AdaptiveTheme.colors.border, AdaptiveTheme.shapes.medium)
            .padding(contentPadding),
        content = content,
    )
}

/**
 * Full-width pane list row. This lets demos and apps express "item inside a side pane" without
 * repeating width, selection, hover, or padding decisions in each screen.
 */
@Composable
public fun AdaptivePaneListItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = AdaptivePaneDefaults.itemPadding(),
    selectedBackground: Color = AdaptiveTheme.colors.primarySubtle,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = AdaptiveTheme.shapes.medium
    val clickModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) selectedBackground else Color.Transparent, shape)
            .then(clickModifier)
            .padding(contentPadding),
        contentAlignment = Alignment.CenterStart,
        content = content,
    )
}
