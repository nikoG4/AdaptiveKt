package io.github.adaptivekt.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.core.AdaptiveBreakpoint
import io.github.adaptivekt.core.AdaptiveContent
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.feedback.AdaptiveEmptyState

/**
 * Displays a generic item collection as a list, grid, or cards with optional query controls.
 *
 * The component is state-hoisted: search, filters, sorting and pagination are emitted through
 * [onQueryChange], leaving local or server-side data loading to the application.
 */
@Composable
public fun <T> AdaptiveCollectionView(
    items: List<T>,
    listItemContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    displayMode: AdaptiveCollectionDisplayMode = AdaptiveCollectionDisplayMode.Auto,
    gridColumns: Int? = null,
    queryState: AdaptiveQueryState? = null,
    onQueryChange: ((AdaptiveQueryState) -> Unit)? = null,
    pagination: AdaptivePaginationState? = null,
    searchEnabled: Boolean = false,
    filters: List<AdaptiveFilterOption> = emptyList(),
    sortOptions: List<AdaptiveSortOption> = emptyList(),
    emptyContent: (@Composable () -> Unit)? = null,
    gridItemContent: (@Composable (T) -> Unit)? = null,
    cardItemContent: (@Composable (T) -> Unit)? = null,
) {
    AdaptiveContent(modifier = modifier) {
        val adaptiveInfo = rememberAdaptiveInfo()
        val resolvedMode = resolveAdaptiveCollectionDisplayMode(
            breakpoint = adaptiveInfo.breakpoint,
            displayMode = displayMode,
        )
        val resolvedColumns = gridColumns ?: collectionColumnsForBreakpoint(adaptiveInfo.breakpoint)

        Column(modifier = Modifier.fillMaxWidth()) {
            if (queryState != null && onQueryChange != null) {
                AdaptiveDataQueryControls(
                    queryState = queryState,
                    onQueryChange = onQueryChange,
                    searchEnabled = searchEnabled,
                    filters = filters,
                    sortOptions = sortOptions,
                    pagination = pagination,
                )
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
            }

            if (items.isEmpty()) {
                if (emptyContent != null) {
                    emptyContent()
                } else {
                    AdaptiveEmptyState(
                        title = "No items available",
                        description = "Try adjusting search or filters.",
                    )
                }
                return@Column
            }

            when (resolvedMode) {
                AdaptiveCollectionDisplayMode.Grid -> CollectionGrid(
                    items = items,
                    columns = resolvedColumns,
                    itemContent = gridItemContent ?: cardItemContent ?: listItemContent,
                )
                AdaptiveCollectionDisplayMode.Cards -> CollectionCards(
                    items = items,
                    itemContent = cardItemContent ?: listItemContent,
                )
                AdaptiveCollectionDisplayMode.Table,
                AdaptiveCollectionDisplayMode.List,
                AdaptiveCollectionDisplayMode.Auto -> CollectionList(
                    items = items,
                    itemContent = listItemContent,
                )
            }
        }
    }
}

public fun collectionColumnsForBreakpoint(breakpoint: AdaptiveBreakpoint): Int {
    return when (breakpoint) {
        AdaptiveBreakpoint.Compact -> 2
        AdaptiveBreakpoint.Medium -> 3
        AdaptiveBreakpoint.Expanded -> 4
        AdaptiveBreakpoint.Large -> 5
    }
}

@Composable
private fun <T> CollectionList(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        items.forEach { item ->
            itemContent(item)
        }
    }
}

@Composable
private fun <T> CollectionCards(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        items.forEach { item ->
            AdaptiveCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
            ) {
                itemContent(item)
            }
        }
    }
}

@Composable
private fun <T> CollectionGrid(
    items: List<T>,
    columns: Int,
    itemContent: @Composable (T) -> Unit,
) {
    val safeColumns = columns.coerceAtLeast(1)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        items.chunked(safeColumns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        itemContent(item)
                    }
                }
                repeat(safeColumns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
