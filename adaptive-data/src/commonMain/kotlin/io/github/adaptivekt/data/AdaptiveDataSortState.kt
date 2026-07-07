package io.github.adaptivekt.data

/**
 * Priority level for multi-column sorting.
 */
public enum class AdaptiveSortPriority {
    Primary,
    Secondary,
    Tertiary,
}

/**
 * Sort state for a single column.
 */
public data class AdaptiveColumnSortState(
    val columnId: String,
    val direction: AdaptiveSortDirection = AdaptiveSortDirection.Ascending,
    val priority: AdaptiveSortPriority = AdaptiveSortPriority.Primary,
)

/**
 * Hoisted multi-column sort state. Supports up to three columns.
 */
public data class AdaptiveDataSortState(
    val sortedColumns: List<AdaptiveColumnSortState> = emptyList(),
)

/**
 * Normalizes a list of column sort states into a valid [AdaptiveDataSortState].
 *
 * Rules:
 * - At most three columns are kept.
 * - Duplicates by [AdaptiveColumnSortState.columnId] are removed, preserving the first occurrence.
 * - Priorities are reassigned by position: index 0 = Primary, 1 = Secondary, 2 = Tertiary.
 */
public fun normalizeAdaptiveDataSortState(
    sortedColumns: List<AdaptiveColumnSortState>,
): AdaptiveDataSortState {
    val distinct = sortedColumns
        .distinctBy { it.columnId }
        .take(3)
    val normalized = distinct.mapIndexed { index, state ->
        val priority = when (index) {
            0 -> AdaptiveSortPriority.Primary
            1 -> AdaptiveSortPriority.Secondary
            2 -> AdaptiveSortPriority.Tertiary
            else -> AdaptiveSortPriority.Primary
        }
        state.copy(priority = priority)
    }
    return AdaptiveDataSortState(normalized)
}

/**
 * Toggles sorting for a column.
 *
 * Cycle: not sorted -> Primary Ascending -> Primary Descending -> removed.
 * Previous columns are demoted when a new column becomes Primary.
 *
 * If [columnId] is not in [sortableColumnIds], the current state is returned unchanged.
 */
public fun AdaptiveDataSortState.toggleColumnSort(
    columnId: String,
    sortableColumnIds: Set<String>,
): AdaptiveDataSortState {
    if (columnId !in sortableColumnIds) return this

    val existingIndex = sortedColumns.indexOfFirst { it.columnId == columnId }
    val mutable = sortedColumns.toMutableList()

    if (existingIndex >= 0) {
        val existing = mutable.removeAt(existingIndex)
        if (existing.direction == AdaptiveSortDirection.Ascending) {
            mutable.add(0, existing.copy(direction = AdaptiveSortDirection.Descending))
        }
        // If it was Descending, it is removed.
    } else {
        mutable.add(0, AdaptiveColumnSortState(columnId = columnId))
    }

    return normalizeAdaptiveDataSortState(mutable)
}

/**
 * Removes the sort state for [columnId] and reprioritizes the remaining columns.
 */
public fun AdaptiveDataSortState.removeColumnSort(
    columnId: String,
): AdaptiveDataSortState {
    val filtered = sortedColumns.filter { it.columnId != columnId }
    return normalizeAdaptiveDataSortState(filtered)
}

/**
 * Promotes [columnId] to Primary, keeping it in the sort state.
 * If the column is not currently sorted, the current state is returned unchanged.
 */
public fun AdaptiveDataSortState.promoteColumnSort(
    columnId: String,
): AdaptiveDataSortState {
    if (sortedColumns.none { it.columnId == columnId }) return this
    val reordered = buildList {
        sortedColumns.firstOrNull { it.columnId == columnId }?.let { add(it) }
        sortedColumns.filterTo(this) { it.columnId != columnId }
    }
    return normalizeAdaptiveDataSortState(reordered)
}

/**
 * Returns the primary sort key and direction for compatibility with [AdaptiveQueryState].
 *
 * When no sort is active, returns [Pair] of `null` and [AdaptiveSortDirection.Ascending].
 */
public fun AdaptiveDataSortState.toQuerySort(): Pair<String?, AdaptiveSortDirection> {
    val primary = sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Primary }
    return if (primary == null) {
        Pair(null, AdaptiveSortDirection.Ascending)
    } else {
        Pair(primary.columnId, primary.direction)
    }
}
