package io.github.adaptivekt.components

/**
 * Pure state machine representing keyboard/focus navigation within a list of options.
 * 
 * @param K Type of the option key.
 */
public data class AdaptiveOptionNavigationState<K>(
    val highlightedKey: K? = null,
    val disabledKeys: Set<K> = emptySet()
)

public sealed interface OptionNavigationOperation {
    public data object Next : OptionNavigationOperation
    public data object Previous : OptionNavigationOperation
    public data object First : OptionNavigationOperation
    public data object Last : OptionNavigationOperation
    public data object ClearHighlight : OptionNavigationOperation

    public data class Highlight<K>(val key: K) : OptionNavigationOperation
}

/**
 * Operations that can be performed on the option navigation state.
 * Throws [IllegalArgumentException] if `items` contains duplicate keys.
 */
public fun <T, K> resolveOptionNavigation(
    state: AdaptiveOptionNavigationState<K>,
    items: List<T>,
    keySelector: (T) -> K,
    operation: OptionNavigationOperation,
    wrap: Boolean = true
): AdaptiveOptionNavigationState<K> {
    if (items.isEmpty()) return state.copy(highlightedKey = null)

    val allKeys = items.map(keySelector)
    if (allKeys.size != allKeys.toSet().size) {
        throw IllegalArgumentException("Duplicate keys are not allowed in AdaptiveOptionNavigationState.")
    }

    val currentIndex = state.highlightedKey?.let { allKeys.indexOf(it) } ?: -1

    return when (operation) {
        OptionNavigationOperation.Next -> {
            val nextKey = findNextEnabled(currentIndex, allKeys, state.disabledKeys, 1, wrap)
            state.copy(highlightedKey = nextKey ?: state.highlightedKey)
        }
        OptionNavigationOperation.Previous -> {
            val nextKey = findNextEnabled(currentIndex, allKeys, state.disabledKeys, -1, wrap)
            state.copy(highlightedKey = nextKey ?: state.highlightedKey)
        }
        OptionNavigationOperation.First -> {
            val nextKey = findNextEnabled(-1, allKeys, state.disabledKeys, 1, false)
            state.copy(highlightedKey = nextKey ?: state.highlightedKey)
        }
        OptionNavigationOperation.Last -> {
            val nextKey = findNextEnabled(allKeys.size, allKeys, state.disabledKeys, -1, false)
            state.copy(highlightedKey = nextKey ?: state.highlightedKey)
        }
        is OptionNavigationOperation.Highlight<*> -> {
            @Suppress("UNCHECKED_CAST")
            val highlightOp = operation as OptionNavigationOperation.Highlight<K>
            val opKey = highlightOp.key
            if (opKey !in state.disabledKeys && opKey in allKeys) {
                state.copy(highlightedKey = opKey)
            } else {
                state
            }
        }
        OptionNavigationOperation.ClearHighlight -> state.copy(highlightedKey = null)
    }
}

private fun <K> findNextEnabled(
    startIndex: Int,
    keys: List<K>,
    disabledKeys: Set<K>,
    step: Int,
    wrap: Boolean
): K? {
    val size = keys.size
    var index = startIndex

    // We do at most `size` iterations to prevent infinite loops if all are disabled
    for (i in 0 until size) {
        index += step
        if (wrap) {
            if (index < 0) index = size - 1
            if (index >= size) index = 0
        } else {
            if (index < 0 || index >= size) return null
        }

        val candidate = keys[index]
        if (candidate !in disabledKeys) {
            return candidate
        }
    }
    return null
}
