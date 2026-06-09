package io.github.adaptivekt.data

import androidx.compose.runtime.Composable
import io.github.adaptivekt.core.AdaptiveBreakpoint

/** Slot type for filter controls shown above adaptive data content. */
public typealias AdaptiveFilterSlot = @Composable () -> Unit

/**
 * Determines how a column should appear in mobile card layout.
 */
public enum class AdaptiveDataMobileRole {
    Title,
    Subtitle,
    Metadata,
    Status,
    Media,
    Actions,
    Hidden,
}

public enum class AdaptiveActionPriority {
    Primary,
    Secondary,
    Overflow,
}

/**
 * Display mode preference for [AdaptiveDataView].
 *
 * [Auto] keeps the original responsive table-to-card behavior unless
 * `autoSwitchToCards` is disabled.
 */
public enum class AdaptiveDataDisplayMode {
    Auto,
    Table,
    Cards,
    List,
    Grid,
}

/**
 * Display mode preference for generic item collections.
 */
public enum class AdaptiveCollectionDisplayMode {
    Auto,
    List,
    Grid,
    Cards,
    Table,
}

public enum class AdaptiveSortDirection {
    Ascending,
    Descending,
}

public data class AdaptiveQueryState(
    val search: String = "",
    val filters: Map<String, Set<String>> = emptyMap(),
    val sortKey: String? = null,
    val sortDirection: AdaptiveSortDirection = AdaptiveSortDirection.Ascending,
    val page: Int = 1,
    val pageSize: Int = 20,
)

public data class AdaptivePaginationState(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
)

public data class AdaptiveSortOption(
    val key: String,
    val label: String,
)

public data class AdaptiveFilterValue(
    val value: String,
    val label: String,
    val count: Int? = null,
)

public data class AdaptiveFilterOption(
    val key: String,
    val label: String,
    val options: List<AdaptiveFilterValue>,
)

public data class AdaptiveDataAction<T>(
    val id: String,
    val label: String,
    val priority: AdaptiveActionPriority = AdaptiveActionPriority.Secondary,
    val destructive: Boolean = false,
    val onClick: (T) -> Unit,
)

/**
 * Represents a column in an adaptive data view.
 *
 * @param T The type of each row item.
 * @param id Unique column identifier.
 * @param header Column title text.
 * @param minBreakpoint Smallest breakpoint at which the column should be displayed.
 * @param weight Relative width weight for table layout.
 * @param mobileRole The role of this column when rendered as a mobile card.
 * @param mobilePriority Order of appearance in the mobile card.
 * @param showInMobileCard Whether this column should be shown by default in mobile cards.
 * @param cell Composable that renders the column value for a given row item.
 */
public data class AdaptiveDataColumn<T>(
    val id: String,
    val header: String,
    val minBreakpoint: AdaptiveBreakpoint = AdaptiveBreakpoint.Compact,
    val weight: Float = 1f,
    val mobileRole: AdaptiveDataMobileRole = AdaptiveDataMobileRole.Metadata,
    val mobilePriority: Int = 100,
    val showInMobileCard: Boolean = true,
    val cell: @Composable (T) -> Unit,
)

/**
 * Represents adaptive data loading and content states.
 */
public sealed interface AdaptiveDataState<out T>

public data object AdaptiveDataLoading : AdaptiveDataState<Nothing>

public data class AdaptiveDataError(
    val title: String,
    val description: String? = null,
) : AdaptiveDataState<Nothing>

public data class AdaptiveDataEmpty(
    val title: String,
    val description: String? = null,
) : AdaptiveDataState<Nothing>

public data class AdaptiveDataContent<T>(
    val items: List<T>,
) : AdaptiveDataState<T>

/** Determines whether the adaptive data view should render a table for the given breakpoint. */
public fun shouldUseTableLayout(breakpoint: AdaptiveBreakpoint): Boolean {
    return when (breakpoint) {
        AdaptiveBreakpoint.Compact, AdaptiveBreakpoint.Medium -> false
        AdaptiveBreakpoint.Expanded, AdaptiveBreakpoint.Large -> true
    }
}

public fun resolveAdaptiveDataDisplayMode(
    breakpoint: AdaptiveBreakpoint,
    displayMode: AdaptiveDataDisplayMode = AdaptiveDataDisplayMode.Auto,
    autoSwitchToCards: Boolean = true,
): AdaptiveDataDisplayMode {
    return when (displayMode) {
        AdaptiveDataDisplayMode.Auto -> when {
            !autoSwitchToCards -> AdaptiveDataDisplayMode.Table
            shouldUseTableLayout(breakpoint) -> AdaptiveDataDisplayMode.Table
            else -> AdaptiveDataDisplayMode.Cards
        }
        else -> displayMode
    }
}

public fun resolveAdaptiveCollectionDisplayMode(
    breakpoint: AdaptiveBreakpoint,
    displayMode: AdaptiveCollectionDisplayMode = AdaptiveCollectionDisplayMode.Auto,
): AdaptiveCollectionDisplayMode {
    return when (displayMode) {
        AdaptiveCollectionDisplayMode.Auto -> when (breakpoint) {
            AdaptiveBreakpoint.Compact -> AdaptiveCollectionDisplayMode.List
            AdaptiveBreakpoint.Medium,
            AdaptiveBreakpoint.Expanded,
            AdaptiveBreakpoint.Large -> AdaptiveCollectionDisplayMode.Grid
        }
        else -> displayMode
    }
}

public fun adaptivePageCount(totalItems: Int, pageSize: Int): Int {
    if (totalItems <= 0) return 1
    val safePageSize = pageSize.coerceAtLeast(1)
    return ((totalItems + safePageSize - 1) / safePageSize).coerceAtLeast(1)
}

public fun coerceAdaptivePage(page: Int, totalItems: Int, pageSize: Int): Int {
    return page.coerceIn(1, adaptivePageCount(totalItems = totalItems, pageSize = pageSize))
}

public fun AdaptiveQueryState.withSearch(search: String): AdaptiveQueryState {
    return copy(search = search, page = 1)
}

public fun AdaptiveQueryState.withFilter(key: String, selectedValues: Set<String>): AdaptiveQueryState {
    val nextFilters = if (selectedValues.isEmpty()) {
        filters - key
    } else {
        filters + (key to selectedValues)
    }
    return copy(filters = nextFilters, page = 1)
}

public fun AdaptiveQueryState.withSort(
    key: String?,
    direction: AdaptiveSortDirection = sortDirection,
): AdaptiveQueryState {
    return copy(sortKey = key, sortDirection = direction, page = 1)
}

public fun AdaptiveQueryState.withPage(page: Int): AdaptiveQueryState {
    return copy(page = page.coerceAtLeast(1))
}

public fun AdaptiveQueryState.withPageSize(pageSize: Int): AdaptiveQueryState {
    return copy(pageSize = pageSize.coerceAtLeast(1), page = 1)
}

/** Returns the subset of columns visible at the current breakpoint. */
public fun <T> visibleColumnsForBreakpoint(
    columns: List<AdaptiveDataColumn<T>>,
    breakpoint: AdaptiveBreakpoint,
): List<AdaptiveDataColumn<T>> {
    val visible = columns.filter { it.minBreakpoint.ordinal <= breakpoint.ordinal }
    return if (visible.isEmpty()) columns else visible
}

internal fun normalizeColumnWeight(weight: Float): Float = if (weight <= 0f) 1f else weight
