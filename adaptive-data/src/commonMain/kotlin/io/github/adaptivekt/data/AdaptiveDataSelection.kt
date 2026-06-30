package io.github.adaptivekt.data

public enum class AdaptiveDataSelectionMode {
    None,
    Single,
    Multiple,
}

public enum class AdaptiveSelectAllState {
    Unchecked,
    Checked,
    Indeterminate,
    Disabled,
}

public data class AdaptiveDataSelectionState<K : Any>(
    val selectedKeys: Set<K> = emptySet(),
    val anchorKey: K? = null,
)

public sealed interface AdaptiveDataSelectionOperation<out K : Any> {
    public data class Select<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>
    public data class Deselect<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>
    public data class Toggle<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>
    public data class Replace<K : Any>(val key: K) : AdaptiveDataSelectionOperation<K>
    public data class SelectRange<K : Any>(val targetKey: K, val additive: Boolean = true) : AdaptiveDataSelectionOperation<K>
    public data object SelectAllVisible : AdaptiveDataSelectionOperation<Nothing>
    public data object ClearVisible : AdaptiveDataSelectionOperation<Nothing>
    public data object ClearAll : AdaptiveDataSelectionOperation<Nothing>
    public data class RetainAvailableKeys<K : Any>(val availableKeys: Set<K>) : AdaptiveDataSelectionOperation<K>
}

/**
 * An optional optimized context to avoid O(N) recreations and duplicate validations.
 */
internal class AdaptiveDataSelectionContext<K : Any>(
    val visibleKeys: List<K>,
    val disabledKeys: Set<K> = emptySet(),
) {
    val visibleKeySet: Set<K>
    val indexByKey: Map<K, Int>

    init {
        val seen = HashSet<K>(visibleKeys.size)
        val idxMap = HashMap<K, Int>(visibleKeys.size)
        var i = 0
        for (key in visibleKeys) {
            if (!seen.add(key)) {
                throw IllegalArgumentException("Duplicate row key detected: $key. Row keys must be completely unique to maintain selection identity.")
            }
            idxMap[key] = i++
        }
        visibleKeySet = seen
        indexByKey = idxMap
    }
}

public fun <K : Any> resolveAdaptiveDataSelection(
    state: AdaptiveDataSelectionState<K>,
    operation: AdaptiveDataSelectionOperation<K>,
    mode: AdaptiveDataSelectionMode,
    visibleKeys: List<K>,
    disabledKeys: Set<K> = emptySet(),
): AdaptiveDataSelectionState<K> {
    return resolveAdaptiveDataSelection(
        state = state,
        operation = operation,
        mode = mode,
        context = AdaptiveDataSelectionContext(visibleKeys, disabledKeys)
    )
}

internal fun <K : Any> resolveAdaptiveDataSelection(
    state: AdaptiveDataSelectionState<K>,
    operation: AdaptiveDataSelectionOperation<K>,
    mode: AdaptiveDataSelectionMode,
    context: AdaptiveDataSelectionContext<K>,
): AdaptiveDataSelectionState<K> {
    if (mode == AdaptiveDataSelectionMode.None) {
        return AdaptiveDataSelectionState()
    }

    val disabledKeys = context.disabledKeys

    // Normalization logic: Clean selected keys.
    var currentSelectedKeys = state.selectedKeys
    if (disabledKeys.isNotEmpty() && currentSelectedKeys.any { it in disabledKeys }) {
        currentSelectedKeys = currentSelectedKeys - disabledKeys
    }

    // Single mode normalization
    if (mode == AdaptiveDataSelectionMode.Single && currentSelectedKeys.size > 1) {
        val keep = state.anchorKey?.takeIf { it in currentSelectedKeys } ?: currentSelectedKeys.first()
        currentSelectedKeys = setOf(keep)
    }

    // Normalize anchor
    var currentAnchorKey = state.anchorKey
    if (currentAnchorKey != null && currentAnchorKey !in currentSelectedKeys) {
        currentAnchorKey = null
    }

    var baseState = state.copy(selectedKeys = currentSelectedKeys, anchorKey = currentAnchorKey)

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
            val selectableVisible = context.visibleKeys.filter { it !in disabledKeys }
            AdaptiveDataSelectionState(
                selectedKeys = baseState.selectedKeys + selectableVisible,
                anchorKey = baseState.anchorKey
            )
        }
        is AdaptiveDataSelectionOperation.ClearVisible -> {
            val keysToRemove = context.visibleKeySet - disabledKeys
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
            if (operation.targetKey in disabledKeys || operation.targetKey !in context.visibleKeySet) {
                return baseState // no-op if target invisible or disabled
            }

            if (mode == AdaptiveDataSelectionMode.Single) {
                return AdaptiveDataSelectionState(selectedKeys = setOf(operation.targetKey), anchorKey = operation.targetKey)
            }

            val anchorIndex = baseState.anchorKey?.let { context.indexByKey[it] } ?: -1
            val targetIndex = context.indexByKey.getValue(operation.targetKey)

            if (anchorIndex == -1) {
                // target visible and anchor absent/not visible -> select target normally and make it anchor
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

            val rangeKeys = context.visibleKeys.subList(minIndex, maxIndex + 1).filter { it !in disabledKeys }

            if (operation.additive) {
                AdaptiveDataSelectionState(
                    selectedKeys = baseState.selectedKeys + rangeKeys,
                    anchorKey = baseState.anchorKey
                )
            } else {
                AdaptiveDataSelectionState(
                    selectedKeys = rangeKeys.toSet(),
                    anchorKey = baseState.anchorKey
                )
            }
        }
    }

    // Final pass anchor normalization for safety
    if (newState.anchorKey != null && newState.anchorKey !in newState.selectedKeys) {
        return newState.copy(anchorKey = null)
    }

    return newState
}

public fun <K : Any> resolveAdaptiveSelectAllState(
    selectedKeys: Set<K>,
    visibleKeys: List<K>,
    disabledKeys: Set<K> = emptySet(),
    mode: AdaptiveDataSelectionMode = AdaptiveDataSelectionMode.Multiple,
): AdaptiveSelectAllState {
    if (mode != AdaptiveDataSelectionMode.Multiple) {
        return AdaptiveSelectAllState.Disabled
    }

    val context = AdaptiveDataSelectionContext(visibleKeys, disabledKeys)
    var selectableCount = 0
    var selectedCount = 0

    for (key in context.visibleKeys) {
        if (key !in context.disabledKeys) {
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

public fun <K : Any> validateAdaptiveRowKeys(keys: List<K>) {
    AdaptiveDataSelectionContext(keys) // Implicitly validates via constructor
}

public enum class AdaptiveDataRowClickBehavior {
    Activate,
    Select,
    SelectAndActivate,
}

internal enum class AdaptiveRowSelectionSource {
    Row,
    Checkbox,
    Keyboard,
}

internal fun <K : Any> resolveAdaptiveRowSelectionOperation(
    key: K,
    shiftPressed: Boolean,
    togglePressed: Boolean,
    source: AdaptiveRowSelectionSource,
): AdaptiveDataSelectionOperation<K> {
    if (source == AdaptiveRowSelectionSource.Checkbox) {
        return AdaptiveDataSelectionOperation.Toggle(key)
    }
    
    if (shiftPressed) {
        return AdaptiveDataSelectionOperation.SelectRange(key, additive = togglePressed)
    }
    
    if (togglePressed) {
        return AdaptiveDataSelectionOperation.Toggle(key)
    }
    
    return AdaptiveDataSelectionOperation.Replace(key)
}
