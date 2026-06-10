package io.github.adaptivekt.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo

/**
 * Shared state-hoisted controls for data and collection views.
 */
@Composable
public fun AdaptiveDataQueryControls(
    queryState: AdaptiveQueryState,
    onQueryChange: (AdaptiveQueryState) -> Unit,
    modifier: Modifier = Modifier,
    searchEnabled: Boolean = false,
    filters: List<AdaptiveFilterOption> = emptyList(),
    sortOptions: List<AdaptiveSortOption> = emptyList(),
    pagination: AdaptivePaginationState? = null,
    pageSizeSelectionEnabled: Boolean = pagination != null,
) {
    val adaptiveInfo = rememberAdaptiveInfo()
    val compact = adaptiveInfo.isCompact

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        if (searchEnabled || sortOptions.isNotEmpty() || pagination != null && pageSizeSelectionEnabled) {
            if (compact) {
                if (searchEnabled) {
                    AdaptiveSearchField(
                        value = queryState.search,
                        onValueChange = { onQueryChange(queryState.withSearch(it)) },
                        onClear = { onQueryChange(queryState.withSearch("")) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (sortOptions.isNotEmpty() || pagination != null && pageSizeSelectionEnabled) {
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SortSelect(
                        queryState = queryState,
                        onQueryChange = onQueryChange,
                        sortOptions = sortOptions,
                        modifier = Modifier.weight(1f),
                    )
                    PageSizeSelect(
                        queryState = queryState,
                        onQueryChange = onQueryChange,
                        pagination = pagination,
                        pageSizeSelectionEnabled = pageSizeSelectionEnabled,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (searchEnabled) {
                        AdaptiveSearchField(
                            value = queryState.search,
                            onValueChange = { onQueryChange(queryState.withSearch(it)) },
                            onClear = { onQueryChange(queryState.withSearch("")) },
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    SortSelect(
                        queryState = queryState,
                        onQueryChange = onQueryChange,
                        sortOptions = sortOptions,
                        modifier = Modifier.width(180.dp),
                    )
                    PageSizeSelect(
                        queryState = queryState,
                        onQueryChange = onQueryChange,
                        pagination = pagination,
                        pageSizeSelectionEnabled = pageSizeSelectionEnabled,
                        modifier = Modifier.width(140.dp),
                    )
                }
            }
        }

        filters.forEach { filter ->
            FilterRow(
                filter = filter,
                queryState = queryState,
                onQueryChange = onQueryChange,
            )
        }

        if (pagination != null) {
            PaginationRow(
                pagination = pagination,
                queryState = queryState,
                onQueryChange = onQueryChange,
            )
        }
    }
}

@Composable
private fun SortSelect(
    queryState: AdaptiveQueryState,
    onQueryChange: (AdaptiveQueryState) -> Unit,
    sortOptions: List<AdaptiveSortOption>,
    modifier: Modifier,
) {
    if (sortOptions.isEmpty()) return

    AdaptiveSelect(
        options = sortOptions,
        selectedOption = sortOptions.firstOrNull { it.key == queryState.sortKey },
        onSelectedOptionChange = { selected ->
            onQueryChange(queryState.withSort(selected?.key))
        },
        optionLabel = { it.label },
        placeholder = "Sort",
        modifier = modifier,
    )
}

@Composable
private fun PageSizeSelect(
    queryState: AdaptiveQueryState,
    onQueryChange: (AdaptiveQueryState) -> Unit,
    pagination: AdaptivePaginationState?,
    pageSizeSelectionEnabled: Boolean,
    modifier: Modifier,
) {
    if (pagination == null || !pageSizeSelectionEnabled) return

    AdaptiveSelect(
        options = pagination.pageSizeOptions,
        selectedOption = queryState.pageSize,
        onSelectedOptionChange = { selected ->
            if (selected != null) onQueryChange(queryState.withPageSize(selected))
        },
        optionLabel = { "$it / page" },
        placeholder = "Page size",
        clearable = false,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterRow(
    filter: AdaptiveFilterOption,
    queryState: AdaptiveQueryState,
    onQueryChange: (AdaptiveQueryState) -> Unit,
) {
    val selectedValues = queryState.filters[filter.key].orEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
        BasicText(
            text = filter.label,
            style = TextStyle(
                color = AdaptiveTheme.colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            filter.options.forEach { value ->
                val selected = value.value in selectedValues
                AdaptiveChip(
                    text = if (value.count != null) "${value.label} (${value.count})" else value.label,
                    selected = selected,
                    tone = if (selected) AdaptiveChipTone.Primary else AdaptiveChipTone.Neutral,
                    onClick = {
                        val next = if (selected) {
                            selectedValues - value.value
                        } else {
                            selectedValues + value.value
                        }
                        onQueryChange(queryState.withFilter(filter.key, next))
                    },
                )
            }
        }
    }
}

@Composable
private fun PaginationRow(
    pagination: AdaptivePaginationState,
    queryState: AdaptiveQueryState,
    onQueryChange: (AdaptiveQueryState) -> Unit,
) {
    val pageCount = adaptivePageCount(pagination.totalItems, pagination.pageSize)
    val currentPage = coerceAdaptivePage(queryState.page, pagination.totalItems, pagination.pageSize)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AdaptiveTokens.Spacing.XSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = "${pagination.totalItems} items",
            style = TextStyle(color = AdaptiveTheme.colors.textSecondary, fontSize = 13.sp),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AdaptiveButton(
                text = "Previous",
                onClick = { onQueryChange(queryState.withPage(currentPage - 1)) },
                enabled = currentPage > 1,
                variant = AdaptiveButtonVariant.Secondary,
                size = AdaptiveButtonSize.Small,
            )
            BasicText(
                text = "$currentPage / $pageCount",
                style = TextStyle(
                    color = AdaptiveTheme.colors.textSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            AdaptiveButton(
                text = "Next",
                onClick = { onQueryChange(queryState.withPage(currentPage + 1)) },
                enabled = currentPage < pageCount,
                variant = AdaptiveButtonVariant.Secondary,
                size = AdaptiveButtonSize.Small,
            )
        }
    }
}
