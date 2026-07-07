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
)

/**
 * Hoisted configuration state for data view columns.
 */
public data class AdaptiveDataColumnConfigState(
    val columns: List<AdaptiveColumnConfig> = emptyList(),
)

/**
 * Normalizes a list of column configs into a valid [AdaptiveDataColumnConfigState].
 *
 * Rules:
 * - Duplicates by [AdaptiveColumnConfig.columnId] are removed, preserving the first occurrence.
 * - Visible columns receive compact `order` values starting at 0.
 * - Hidden columns are placed at the end with `order = [Int.MAX_VALUE]`.
 * - At most one visible column may be pinned [AdaptiveColumnPin.Start]; extras are reset to [AdaptiveColumnPin.None].
 * - At most one visible column may be pinned [AdaptiveColumnPin.End]; extras are reset to [AdaptiveColumnPin.None].
 */
public fun normalizeAdaptiveDataColumnConfigState(
    columns: List<AdaptiveColumnConfig>,
): AdaptiveDataColumnConfigState {
    val deduplicated = columns.distinctBy { it.columnId }

    var startSeen = false
    var endSeen = false
    val withNormalizedPins = deduplicated.map { config ->
        when (config.pinned) {
            AdaptiveColumnPin.Start -> {
                if (startSeen) {
                    config.copy(pinned = AdaptiveColumnPin.None)
                } else {
                    startSeen = true
                    config
                }
            }
            AdaptiveColumnPin.End -> {
                if (endSeen) {
                    config.copy(pinned = AdaptiveColumnPin.None)
                } else {
                    endSeen = true
                    config
                }
            }
            AdaptiveColumnPin.None -> config
        }
    }

    val visible = withNormalizedPins
        .filter { it.visible }
        .sortedBy { it.order }
        .mapIndexed { index, config -> config.copy(order = index) }

    val hidden = withNormalizedPins
        .filter { !it.visible }
        .map { config -> config.copy(order = Int.MAX_VALUE) }

    return AdaptiveDataColumnConfigState(columns = visible + hidden)
}

/**
 * Sets the visibility of [columnId].
 *
 * Hiding the last visible column is not allowed. When a column is hidden, its pin is cleared.
 */
public fun AdaptiveDataColumnConfigState.setColumnVisible(
    columnId: String,
    visible: Boolean,
): AdaptiveDataColumnConfigState {
    val config = columns.firstOrNull { it.columnId == columnId } ?: return this

    val currentlyVisible = columns.count { it.visible }
    if (!visible && currentlyVisible <= 1 && config.visible) return this

    val updated = columns.map { column ->
        if (column.columnId == columnId) {
            column.copy(
                visible = visible,
                pinned = if (!visible) AdaptiveColumnPin.None else column.pinned,
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
 * Hidden columns cannot be moved. Out-of-range indices are clamped.
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

    val reorderedVisible = visibleConfigs.mapIndexed { index, config -> config.copy(order = index) }
    val hiddenConfigs = columns.filter { !it.visible }

    return normalizeAdaptiveDataColumnConfigState(reorderedVisible + hiddenConfigs)
}

/**
 * Sets the pin position for [columnId].
 *
 * Only visible columns can be pinned. Setting [pin] to [AdaptiveColumnPin.Start] clears any other
 * Start pin; setting it to [AdaptiveColumnPin.End] clears any other End pin.
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
