package io.github.adaptivekt.data

/**
 * Priority level for multi-column sorting.
 */
public enum class AdaptiveSortPriority {
    Primary,
    Secondary,
    Tertiary,
    ;

    public companion object {
        public const val MAX_RANK: Int = 3

        public fun fromIndex(index: Int): AdaptiveSortPriority =
            when (index) {
                0 -> Primary
                1 -> Secondary
                2 -> Tertiary
                else -> throw IllegalArgumentException(
                    "AdaptiveSortPriority supports indices 0..2, got $index"
                )
            }
    }
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
 * Hoisted multi-column sort state. Supports up to [AdaptiveSortPriority.MAX_RANK] columns.
 *
 * Invariants enforced by the [init] block:
 * - At most [AdaptiveSortPriority.MAX_RANK] columns.
 * - Column ids are unique.
 * - Priorities are unique.
 *
 * A [Primary] column is **not** required to exist; callers may construct intermediate
 * states. Helpers like [normalizeAdaptiveDataSortState] and [toggleColumnSort] always
 * produce a normalized state.
 */
public data class AdaptiveDataSortState(
    val sortedColumns: List<AdaptiveColumnSortState> = emptyList(),
) {
    public val primarySort: AdaptiveColumnSortState?
        get() = sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Primary }

    public val secondarySort: AdaptiveColumnSortState?
        get() = sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Secondary }

    public val tertiarySort: AdaptiveColumnSortState?
        get() = sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Tertiary }

    public val isSorted: Boolean
        get() = sortedColumns.isNotEmpty()

    init {
        require(sortedColumns.size <= AdaptiveSortPriority.MAX_RANK) {
            "AdaptiveDataSortState supports at most ${AdaptiveSortPriority.MAX_RANK} sort columns, got ${sortedColumns.size}"
        }
        require(sortedColumns.map { it.columnId }.distinct().size == sortedColumns.size) {
            "AdaptiveDataSortState column ids must be unique."
        }
        require(sortedColumns.map { it.priority }.distinct().size == sortedColumns.size) {
            "AdaptiveDataSortState priorities must be unique."
        }
    }
}

/**
 * Normalizes a list of column sort states into a valid [AdaptiveDataSortState].
 *
 * Rules:
 * - Duplicates by [AdaptiveColumnSortState.columnId] are removed, preserving the first occurrence.
 * - At most [AdaptiveSortPriority.MAX_RANK] columns are kept.
 * - Priorities are reassigned by position: index 0 = Primary, 1 = Secondary, 2 = Tertiary.
 */
public fun normalizeAdaptiveDataSortState(
    sortedColumns: List<AdaptiveColumnSortState>,
): AdaptiveDataSortState {
    val distinct = sortedColumns
        .distinctBy { it.columnId }
        .take(AdaptiveSortPriority.MAX_RANK)
    val normalized = distinct.mapIndexed { index, state ->
        state.copy(priority = AdaptiveSortPriority.fromIndex(index))
    }
    return AdaptiveDataSortState(normalized)
}

/**
 * Toggles sorting for a column.
 *
 * Cycle: not sorted -> Primary Ascending -> Primary Descending -> removed.
 *
 * Behavior:
 * - If [columnId] is not in [sortableColumnIds], the current state is returned unchanged.
 * - If the column is not present, it is added at the front as `Primary Ascending` and
 *   existing columns are demoted by one priority slot (respecting [AdaptiveSortPriority.MAX_RANK]).
 * - If the column exists with [AdaptiveSortDirection.Ascending], only its direction flips to
 *   [AdaptiveSortDirection.Descending]; **its relative position and priority are preserved**.
 *   Use [promoteColumnSort] to move it to [AdaptiveSortPriority.Primary].
 * - If the column exists with [AdaptiveSortDirection.Descending], it is removed and the
 *   remaining columns are reprioritized.
 *
 * The result is always normalized via [normalizeAdaptiveDataSortState].
 */
public fun AdaptiveDataSortState.toggleColumnSort(
    columnId: String,
    sortableColumnIds: Set<String>,
): AdaptiveDataSortState {
    if (columnId !in sortableColumnIds) return this

    val normalized = normalizeAdaptiveDataSortState(sortedColumns)
    val existingIndex = normalized.sortedColumns.indexOfFirst { it.columnId == columnId }
    val mutable = normalized.sortedColumns.toMutableList()

    if (existingIndex >= 0) {
        val existing = mutable[existingIndex]
        if (existing.direction == AdaptiveSortDirection.Ascending) {
            mutable[existingIndex] = existing.copy(direction = AdaptiveSortDirection.Descending)
        } else {
            mutable.removeAt(existingIndex)
        }
    } else {
        mutable.add(0, AdaptiveColumnSortState(columnId = columnId))
    }

    return normalizeAdaptiveDataSortState(mutable)
}

/**
 * Removes the sort state for [columnId] and reprioritizes the remaining columns.
 * No-op if [columnId] is not currently sorted.
 */
public fun AdaptiveDataSortState.removeColumnSort(
    columnId: String,
): AdaptiveDataSortState {
    if (sortedColumns.none { it.columnId == columnId }) return this
    val filtered = sortedColumns.filter { it.columnId != columnId }
    return normalizeAdaptiveDataSortState(filtered)
}

/**
 * Promotes [columnId] to [AdaptiveSortPriority.Primary], moving it to the front of the sort
 * list. The column's direction is preserved. No-op if [columnId] is not currently sorted.
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
 * Updates the sort direction of [columnId] without changing its position or priority.
 * No-op if [columnId] is not currently sorted.
 */
public fun AdaptiveDataSortState.setColumnSortDirection(
    columnId: String,
    direction: AdaptiveSortDirection,
): AdaptiveDataSortState {
    if (sortedColumns.none { it.columnId == columnId }) return this
    val updated = sortedColumns.map { state ->
        if (state.columnId == columnId) state.copy(direction = direction) else state
    }
    return normalizeAdaptiveDataSortState(updated)
}

/**
 * Reorders the sort priority by moving the column at [fromIndex] to [toIndex].
 *
 * - If [fromIndex] is out of range, the state is returned unchanged.
 * - [toIndex] is clamped to `0..sortedColumns.lastIndex`.
 * - The resulting state is normalized; never exceeds [AdaptiveSortPriority.MAX_RANK].
 * - Never throws for out-of-range [toIndex].
 */
public fun AdaptiveDataSortState.reorderSortPriority(
    fromIndex: Int,
    toIndex: Int,
): AdaptiveDataSortState {
    val normalized = normalizeAdaptiveDataSortState(sortedColumns)
    if (fromIndex !in normalized.sortedColumns.indices) return normalized
    val list = normalized.sortedColumns.toMutableList()
    val clampedToIndex = toIndex.coerceIn(0, list.lastIndex)
    val item = list.removeAt(fromIndex)
    list.add(clampedToIndex, item)
    return normalizeAdaptiveDataSortState(list)
}

/**
 * Returns the primary sort key and direction for compatibility with [AdaptiveQueryState].
 *
 * When no [AdaptiveSortPriority.Primary] column is present, returns `Pair(null, Ascending)`.
 * Uses an explicit [Pair] constructor to avoid the precedence pitfall of `to` vs `?:`.
 */
public fun AdaptiveDataSortState.toQuerySort(): Pair<String?, AdaptiveSortDirection> {
    val primary = primarySort
    return if (primary == null) {
        Pair(null, AdaptiveSortDirection.Ascending)
    } else {
        Pair(primary.columnId, primary.direction)
    }
}

/**
 * Resolves the effective sort state by keeping only columns that exist, are visible and
 * are sortable in [columnConfigState]. The result is normalized.
 */
public fun resolveEffectiveSortState(
    sortState: AdaptiveDataSortState,
    columnConfigState: AdaptiveDataColumnConfigState,
): AdaptiveDataSortState {
    val allowedIds = columnConfigState.columns
        .asSequence()
        .filter { it.visible && it.sortable }
        .map { it.columnId }
        .toSet()
    val effective = sortState.sortedColumns.filter { it.columnId in allowedIds }
    return normalizeAdaptiveDataSortState(effective)
}

/**
 * Resolves the query sort pair (key, direction) from [sortState] filtered by
 * [columnConfigState]. Returns `Pair(null, Ascending)` when no primary effective sort exists.
 *
 * Uses an explicit [Pair] constructor to avoid the `to`/`?:` precedence bug.
 */
public fun resolveQuerySortFromState(
    sortState: AdaptiveDataSortState,
    columnConfigState: AdaptiveDataColumnConfigState,
): Pair<String?, AdaptiveSortDirection> {
    val effective = resolveEffectiveSortState(sortState, columnConfigState)
    val primary = effective.primarySort
    return if (primary == null) {
        Pair(null, AdaptiveSortDirection.Ascending)
    } else {
        Pair(primary.columnId, primary.direction)
    }
}

/**
 * Builds a single-column [AdaptiveDataSortState] from a query sort pair, validating the
 * target column against [columnConfigState]. Returns an empty state when [sortKey] is null,
 * missing, hidden or non-sortable.
 */
public fun resolveSortStateFromQuery(
    sortKey: String?,
    sortDirection: AdaptiveSortDirection,
    columnConfigState: AdaptiveDataColumnConfigState,
): AdaptiveDataSortState {
    if (sortKey == null) return AdaptiveDataSortState()
    val config = columnConfigState.getConfig(sortKey)
    if (config == null || !config.visible || !config.sortable) return AdaptiveDataSortState()
    return AdaptiveDataSortState(
        listOf(AdaptiveColumnSortState(sortKey, sortDirection, AdaptiveSortPriority.Primary))
    )
}

/**
 * Maps this [AdaptiveQueryState] to an [AdaptiveDataSortState] using [columnConfigState].
 */
public fun AdaptiveQueryState.toSortState(
    columnConfigState: AdaptiveDataColumnConfigState,
): AdaptiveDataSortState =
    resolveSortStateFromQuery(sortKey, sortDirection, columnConfigState)

/**
 * Writes the primary sort of [sortState] back into this [AdaptiveQueryState], delegating to
 * the existing [AdaptiveQueryState.withSort] extension. Does not modify [AdaptiveDataTypes.kt].
 */
public fun AdaptiveQueryState.withSortState(
    sortState: AdaptiveDataSortState,
    columnConfigState: AdaptiveDataColumnConfigState,
): AdaptiveQueryState {
    val (key, direction) = resolveQuerySortFromState(sortState, columnConfigState)
    return withSort(key, direction)
}
