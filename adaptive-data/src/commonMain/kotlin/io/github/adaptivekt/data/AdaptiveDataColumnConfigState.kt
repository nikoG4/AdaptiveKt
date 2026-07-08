package io.github.adaptivekt.data

/**
 * Pin position for a column.
 */
public enum class AdaptiveColumnPin {
    None,
    Start,
    End,
}

/**
 * Configuration for a single data column.
 */
public data class AdaptiveColumnConfig(
    val columnId: String,
    val visible: Boolean = true,
    val pinned: AdaptiveColumnPin = AdaptiveColumnPin.None,
    val order: Int = Int.MAX_VALUE,
    val sortable: Boolean = true,
    val width: Int? = null,
)

/**
 * Hoisted configuration state for data view columns.
 */
public data class AdaptiveDataColumnConfigState(
    val columns: List<AdaptiveColumnConfig> = emptyList(),
) {
    public val visibleColumns: List<AdaptiveColumnConfig>
        get() = columns.filter { it.visible }.sortedBy { it.order }

    public val hiddenColumns: List<AdaptiveColumnConfig>
        get() = columns.filter { !it.visible }

    public val visibleColumnIds: List<String>
        get() = visibleColumns.map { it.columnId }

    public val hiddenColumnIds: List<String>
        get() = hiddenColumns.map { it.columnId }

    public fun getConfig(columnId: String): AdaptiveColumnConfig? =
        columns.firstOrNull { it.columnId == columnId }
}

/**
 * Normalizes a list of column configs into a valid [AdaptiveDataColumnConfigState].
 *
 * Rules:
 * - Duplicates by [AdaptiveColumnConfig.columnId] are removed, preserving the first occurrence.
 * - Visible columns receive compact `order` values `0..n-1`, sorted by their previous `order`
 *   (stable when `order` ties).
 * - Hidden columns are placed at the end with `order = [Int.MAX_VALUE]`, `visible = false`
 *   and `pinned = [AdaptiveColumnPin.None]`.
 * - Pin resolution happens **only among visible columns**, in ascending `order`.
 *   The first visible column with [AdaptiveColumnPin.Start] keeps it; any other visible
 *   Start is reset to [AdaptiveColumnPin.None]. Same rule for [AdaptiveColumnPin.End].
 * - Hidden columns never consume a Start/End slot and never steal a pin from a visible one.
 */
public fun normalizeAdaptiveDataColumnConfigState(
    columns: List<AdaptiveColumnConfig>,
): AdaptiveDataColumnConfigState {
    val deduplicated = columns.distinctBy { it.columnId }

    val visibleSorted = deduplicated.filter { it.visible }.sortedBy { it.order }

    var startSeen = false
    var endSeen = false
    val normalizedVisible = visibleSorted.mapIndexed { index, config ->
        val normalizedPin = when (config.pinned) {
            AdaptiveColumnPin.Start -> if (!startSeen) {
                startSeen = true
                AdaptiveColumnPin.Start
            } else {
                AdaptiveColumnPin.None
            }
            AdaptiveColumnPin.End -> if (!endSeen) {
                endSeen = true
                AdaptiveColumnPin.End
            } else {
                AdaptiveColumnPin.None
            }
            AdaptiveColumnPin.None -> AdaptiveColumnPin.None
        }
        config.copy(order = index, pinned = normalizedPin)
    }

    val hidden = deduplicated
        .filter { !it.visible }
        .map { it.copy(order = Int.MAX_VALUE, pinned = AdaptiveColumnPin.None) }

    return AdaptiveDataColumnConfigState(columns = normalizedVisible + hidden)
}

/**
 * Sets the visibility of [columnId].
 *
 * - No-op if [columnId] does not exist.
 * - Hiding the last visible column is not allowed.
 * - When a column is hidden, its pin is cleared.
 * - When a previously hidden column is shown, it returns visible and unpinned (hidden
 *   columns always have `pinned = None` after normalization).
 * - Result is normalized.
 */
public fun AdaptiveDataColumnConfigState.setColumnVisible(
    columnId: String,
    visible: Boolean,
): AdaptiveDataColumnConfigState {
    val config = columns.firstOrNull { it.columnId == columnId } ?: return this

    val currentlyVisible = columns.count { it.visible }
    if (!visible && currentlyVisible <= 1 && config.visible) return this

    val wasHidden = !config.visible
    val updated = columns.map { column ->
        if (column.columnId == columnId) {
            column.copy(
                visible = visible,
                pinned = when {
                    !visible -> AdaptiveColumnPin.None
                    wasHidden -> AdaptiveColumnPin.None
                    else -> column.pinned
                },
            )
        } else {
            column
        }
    }

    return normalizeAdaptiveDataColumnConfigState(updated)
}

/**
 * Moves [columnId] to [targetIndex] among visible columns.
 *
 * - Hidden columns cannot be moved.
 * - [targetIndex] is clamped to `0..visibleCount - 1`.
 * - Hidden columns are preserved at the end.
 * - Result is normalized.
 */
public fun AdaptiveDataColumnConfigState.moveColumn(
    columnId: String,
    targetIndex: Int,
): AdaptiveDataColumnConfigState {
    val config = columns.firstOrNull { it.columnId == columnId } ?: return this
    if (!config.visible) return this

    val visibleConfigs = columns.filter { it.visible }.sortedBy { it.order }.toMutableList()
    val currentIndex = visibleConfigs.indexOfFirst { it.columnId == columnId }
    if (currentIndex < 0) return this

    val item = visibleConfigs.removeAt(currentIndex)
    val clampedTarget = targetIndex.coerceIn(0, visibleConfigs.size)
    visibleConfigs.add(clampedTarget, item)

    val reorderedVisible = visibleConfigs.mapIndexed { index, c -> c.copy(order = index) }
    val hiddenConfigs = columns.filter { !it.visible }

    return normalizeAdaptiveDataColumnConfigState(reorderedVisible + hiddenConfigs)
}

/**
 * Sets the pin position for [columnId].
 *
 * - No-op if [columnId] does not exist or is hidden.
 * - Setting [AdaptiveColumnPin.Start] clears any other visible Start pin.
 * - Setting [AdaptiveColumnPin.End] clears any other visible End pin.
 * - Setting [AdaptiveColumnPin.None] only affects the target column.
 * - Result is normalized.
 */
public fun AdaptiveDataColumnConfigState.setColumnPin(
    columnId: String,
    pin: AdaptiveColumnPin,
): AdaptiveDataColumnConfigState {
    val config = columns.firstOrNull { it.columnId == columnId } ?: return this
    if (!config.visible) return this

    val updated = columns.map { column ->
        when {
            column.columnId == columnId -> column.copy(pinned = pin)
            pin == AdaptiveColumnPin.Start && column.pinned == AdaptiveColumnPin.Start ->
                column.copy(pinned = AdaptiveColumnPin.None)
            pin == AdaptiveColumnPin.End && column.pinned == AdaptiveColumnPin.End ->
                column.copy(pinned = AdaptiveColumnPin.None)
            else -> column
        }
    }

    return normalizeAdaptiveDataColumnConfigState(updated)
}

/**
 * Sets the width of [columnId].
 *
 * - No-op if [columnId] does not exist.
 * - No-op if [width] is not null and `<= 0`.
 * - Does not change `visible`, `pinned` or relative `order` (beyond normalization).
 * - Result is normalized.
 */
public fun AdaptiveDataColumnConfigState.setColumnWidth(
    columnId: String,
    width: Int?,
): AdaptiveDataColumnConfigState {
    val config = columns.firstOrNull { it.columnId == columnId } ?: return this
    if (width != null && width <= 0) return this
    if (config.width == width) return this

    val updated = columns.map { column ->
        if (column.columnId == columnId) column.copy(width = width) else column
    }

    return normalizeAdaptiveDataColumnConfigState(updated)
}

/**
 * Builds a fresh [AdaptiveDataColumnConfigState] from [defaults], normalizing them.
 * Useful for resetting a configuration to its baseline.
 */
public fun resetAdaptiveDataColumnConfigState(
    defaults: List<AdaptiveColumnConfig>,
): AdaptiveDataColumnConfigState =
    normalizeAdaptiveDataColumnConfigState(defaults)
