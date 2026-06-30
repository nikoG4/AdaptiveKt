package io.github.adaptivekt.data

/**
 * Defines the mode of selection allowed for rows in a data view.
 */
public enum class AdaptiveDataSelectionMode {
    /**
     * Selection is completely disabled.
     */
    None,

    /**
     * Only a single row can be selected at a time.
     */
    Single,

    /**
     * Multiple rows can be selected simultaneously.
     */
    Multiple,
}

/**
 * Represents the tri-state status of a "Select All" or bulk selection control.
 */
public enum class AdaptiveSelectAllState {
    /**
     * No visible and enabled rows are selected.
     */
    Unchecked,

    /**
     * All visible and enabled rows are selected.
     */
    Checked,

    /**
     * Some (but not all) visible and enabled rows are selected.
     */
    Indeterminate,

    /**
     * There are no visible and enabled rows to select, or the mode does not support multiple selection.
     */
    Disabled,
}

/**
 * Represents the pure selection state of a data view, independent of UI presentation.
 *
 * @param K The type of the row key. Must be non-nullable because a null anchorKey signifies no anchor.
 * @property selectedKeys The set of currently selected row keys.
 * @property anchorKey The key of the row where the last range selection originated.
 */
public data class AdaptiveDataSelectionState<K : Any>(
    val selectedKeys: Set<K> = emptySet(),
    val anchorKey: K? = null,
)

/**
 * Defines all possible pure operations that can modify an [AdaptiveDataSelectionState].
 */
public sealed interface AdaptiveDataSelectionOperation<out K : Any> {
    /** Selects a single key, making it the new anchor. */
    public data class Select<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>

    /** Deselects a single key. Clears the anchor if the deselected key was the anchor. */
    public data class Deselect<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>

    /** Toggles the selection of a single key. Updates anchor behavior appropriately. */
    public data class Toggle<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>

    /** Replaces the entire selection with a single key, making it the new anchor. */
    public data class Replace<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>

    /**
     * Selects a range from the current anchor to the [targetKey] within the currently visible keys.
     * @param additive If true, adds the range to the existing selection. If false, replaces the selection with the range.
     */
    public data class SelectRange<K : Any>(val targetKey: K, val additive: Boolean = true) : AdaptiveDataSelectionOperation<K>

    /** Selects all visible and enabled keys. */
    public data object SelectAllVisible : AdaptiveDataSelectionOperation<Nothing>

    /** Deselects only the currently visible and enabled keys. Preserves off-page selections. */
    public data object ClearVisible : AdaptiveDataSelectionOperation<Nothing>

    /** Deselects all keys globally, clearing the anchor. */
    public data object ClearAll : AdaptiveDataSelectionOperation<Nothing>

    /**
     * Retains only the keys that are present in the provided authoritative set.
     * WARNING: Do not pass only the current page keys, or you will lose off-page selections.
     * Use this when you receive the full authoritative dataset or know precisely which keys were deleted globally.
     */
    public data class RetainAvailableKeys<K : Any>(val availableKeys: Set<K>) : AdaptiveDataSelectionOperation<K>
}

/**
 * Pure reducer function to compute the new selection state based on an operation.
 *
 * @param state The current selection state.
 * @param operation The operation to apply.
 * @param mode The current selection mode.
 * @param visibleKeys The list of keys currently visible in the UI (determines range operations and visible select all).
 * @param disabledKeys The set of keys that cannot be selected.
 * @return The newly computed selection state.
 */
public fun <K : Any> resolveAdaptiveDataSelection(
    state: AdaptiveDataSelectionState<K>,
    operation: AdaptiveDataSelectionOperation<K>,
    mode: AdaptiveDataSelectionMode,
    visibleKeys: List<K>,
    disabledKeys: Set<K> = emptySet(),
): AdaptiveDataSelectionState<K> {
    // Mode None normalization: enforce empty state immediately.
    if (mode == AdaptiveDataSelectionMode.None) {
        return AdaptiveDataSelectionState()
    }

    // Safety cleanup: strip any selected keys that are now disabled.
    val baseSelectedKeys = if (disabledKeys.isNotEmpty() && state.selectedKeys.any { it in disabledKeys }) {
        state.selectedKeys - disabledKeys
    } else {
        state.selectedKeys
    }
    
    val baseAnchorKey = if (state.anchorKey in disabledKeys) null else state.anchorKey

    val baseState = state.copy(selectedKeys = baseSelectedKeys, anchorKey = baseAnchorKey)

    val newState = when (operation) {
        is AdaptiveDataSelectionOperation.Select -> {
            if (operation.key in disabledKeys) return baseState
            if (mode == AdaptiveDataSelectionMode.Single) {
                AdaptiveDataSelectionState(selectedKeys = setOf(operation.key), anchorKey = operation.key)
            } else {
                AdaptiveDataSelectionState(
                    selectedKeys = baseState.selectedKeys + operation.key,
                    anchorKey = operation.key
                )
            }
        }
        is AdaptiveDataSelectionOperation.Deselect -> {
            AdaptiveDataSelectionState(
                selectedKeys = baseState.selectedKeys - operation.key,
                anchorKey = if (baseState.anchorKey == operation.key) null else baseState.anchorKey
            )
        }
        is AdaptiveDataSelectionOperation.Toggle -> {
            if (operation.key in disabledKeys) return baseState
            val isSelected = operation.key in baseState.selectedKeys
            
            if (isSelected) {
                AdaptiveDataSelectionState(
                    selectedKeys = baseState.selectedKeys - operation.key,
                    anchorKey = if (baseState.anchorKey == operation.key) null else baseState.anchorKey
                )
            } else {
                if (mode == AdaptiveDataSelectionMode.Single) {
                    AdaptiveDataSelectionState(selectedKeys = setOf(operation.key), anchorKey = operation.key)
                } else {
                    AdaptiveDataSelectionState(
                        selectedKeys = baseState.selectedKeys + operation.key,
                        anchorKey = operation.key
                    )
                }
            }
        }
        is AdaptiveDataSelectionOperation.Replace -> {
            if (operation.key in disabledKeys) return baseState
            AdaptiveDataSelectionState(selectedKeys = setOf(operation.key), anchorKey = operation.key)
        }
        is AdaptiveDataSelectionOperation.SelectAllVisible -> {
            if (mode != AdaptiveDataSelectionMode.Multiple) return baseState
            val selectableVisible = visibleKeys.filter { it !in disabledKeys }
            AdaptiveDataSelectionState(
                selectedKeys = baseState.selectedKeys + selectableVisible,
                anchorKey = baseState.anchorKey
            )
        }
        is AdaptiveDataSelectionOperation.ClearVisible -> {
            val visibleSet = visibleKeys.toSet()
            // We only remove visible AND enabled keys. 
            // If a key is disabled, maybe it should also be removed? The specification says:
            // "ClearVisible elimina únicamente las visibles habilitadas".
            // So we remove keys that are in visibleSet AND NOT in disabledKeys.
            val keysToRemove = visibleSet - disabledKeys
            AdaptiveDataSelectionState(
                selectedKeys = baseState.selectedKeys - keysToRemove,
                anchorKey = if (baseState.anchorKey in keysToRemove) null else baseState.anchorKey
            )
        }
        is AdaptiveDataSelectionOperation.ClearAll -> {
            AdaptiveDataSelectionState()
        }
        is AdaptiveDataSelectionOperation.RetainAvailableKeys -> {
            val validSelected = baseState.selectedKeys.intersect(operation.availableKeys)
            AdaptiveDataSelectionState(
                selectedKeys = validSelected,
                anchorKey = if (baseState.anchorKey in operation.availableKeys) baseState.anchorKey else null
            )
        }
        is AdaptiveDataSelectionOperation.SelectRange -> {
            if (mode != AdaptiveDataSelectionMode.Multiple) {
                // In Single mode, SelectRange degrades to a normal Replace (or no-op). We'll treat it as Replace.
                if (operation.targetKey in disabledKeys) return baseState
                return AdaptiveDataSelectionState(selectedKeys = setOf(operation.targetKey), anchorKey = operation.targetKey)
            }

            if (operation.targetKey in disabledKeys) return baseState

            val anchorIndex = baseState.anchorKey?.let { visibleKeys.indexOf(it) } ?: -1
            val targetIndex = visibleKeys.indexOf(operation.targetKey)

            if (anchorIndex == -1 || targetIndex == -1) {
                // Anchor is missing/invisible/null, or target is not visible. Fallback to normal select/replace.
                if (operation.additive) {
                    return AdaptiveDataSelectionState(
                        selectedKeys = baseState.selectedKeys + operation.targetKey,
                        anchorKey = operation.targetKey
                    )
                } else {
                    return AdaptiveDataSelectionState(
                        selectedKeys = setOf(operation.targetKey),
                        anchorKey = operation.targetKey
                    )
                }
            }

            val minIndex = minOf(anchorIndex, targetIndex)
            val maxIndex = maxOf(anchorIndex, targetIndex)

            val rangeKeys = visibleKeys.subList(minIndex, maxIndex + 1).filter { it !in disabledKeys }

            if (operation.additive) {
                AdaptiveDataSelectionState(
                    selectedKeys = baseState.selectedKeys + rangeKeys,
                    anchorKey = baseState.anchorKey // Anchor remains the original one
                )
            } else {
                AdaptiveDataSelectionState(
                    selectedKeys = rangeKeys.toSet(),
                    anchorKey = baseState.anchorKey // Anchor remains the original one
                )
            }
        }
    }

    // Safety check for Single mode invariant
    return if (mode == AdaptiveDataSelectionMode.Single && newState.selectedKeys.size > 1) {
        // Enforce the invariant by just taking the first or anchor (which shouldn't happen naturally, but safety first)
        val fallbackKey = newState.anchorKey ?: newState.selectedKeys.first()
        AdaptiveDataSelectionState(selectedKeys = setOf(fallbackKey), anchorKey = fallbackKey)
    } else {
        newState
    }
}

/**
 * Determines the visual state of a "Select All" checkbox.
 *
 * @param selectedKeys The current set of selected keys.
 * @param visibleKeys The list of currently visible keys in the list/table.
 * @param disabledKeys The set of keys that cannot be selected.
 * @param mode The current selection mode.
 * @return The state of the select all control.
 */
public fun <K : Any> resolveAdaptiveSelectAllState(
    selectedKeys: Set<K>,
    visibleKeys: List<K>,
    disabledKeys: Set<K> = emptySet(),
    mode: AdaptiveDataSelectionMode = AdaptiveDataSelectionMode.Multiple,
): AdaptiveSelectAllState {
    if (mode != AdaptiveDataSelectionMode.Multiple) {
        return AdaptiveSelectAllState.Disabled
    }

    var selectableCount = 0
    var selectedCount = 0

    // O(N) iteration over visibleKeys to compute exactly what is visible and enabled
    for (key in visibleKeys) {
        if (key !in disabledKeys) {
            selectableCount++
            if (key in selectedKeys) {
                selectedCount++
            }
        }
    }

    return when {
        selectableCount == 0 -> AdaptiveSelectAllState.Disabled
        selectedCount == 0 -> AdaptiveSelectAllState.Unchecked
        selectedCount == selectableCount -> AdaptiveSelectAllState.Checked
        else -> AdaptiveSelectAllState.Indeterminate
    }
}

/**
 * Validates that the provided list of keys contains no duplicates.
 * Throws an [IllegalArgumentException] indicating the exact key that was duplicated.
 *
 * @param keys The list of keys to validate.
 */
public fun <K : Any> validateAdaptiveRowKeys(keys: List<K>) {
    val seen = HashSet<K>(keys.size)
    for (key in keys) {
        if (!seen.add(key)) {
            throw IllegalArgumentException("Duplicate row key detected: $key. Row keys must be completely unique to maintain selection identity.")
        }
    }
}
