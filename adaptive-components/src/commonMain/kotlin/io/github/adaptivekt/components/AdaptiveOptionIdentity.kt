package io.github.adaptivekt.components

/**
 * Resolves the key for an option.
 * If [optionKey] is provided, uses it. Otherwise, defaults to the option itself.
 */
public fun <T> optionKeyOf(option: T, optionKey: ((T) -> Any)?): Any {
    return optionKey?.invoke(option) ?: (option as Any)
}

/**
 * Checks if two options are identical based on their resolved keys.
 */
public fun <T> isOptionSame(a: T, b: T, optionKey: ((T) -> Any)?): Boolean {
    return optionKeyOf(a, optionKey) == optionKeyOf(b, optionKey)
}

/**
 * Validates that all options have unique keys.
 * Throws [IllegalArgumentException] if duplicates are found.
 */
public fun <T> validateOptionKeys(options: List<T>, optionKey: ((T) -> Any)?) {
    val keys = mutableSetOf<Any>()
    for (option in options) {
        val key = optionKeyOf(option, optionKey)
        if (!keys.add(key)) {
            throw IllegalArgumentException("Duplicate option key found: $key")
        }
    }
}

/**
 * Toggles an option in a selection list based on its key.
 */
public fun <T> toggleOptionByKey(
    selectedOptions: List<T>,
    option: T,
    optionKey: ((T) -> Any)?
): List<T> {
    val key = optionKeyOf(option, optionKey)
    val isSelected = selectedOptions.any { optionKeyOf(it, optionKey) == key }
    return if (isSelected) {
        selectedOptions.filterNot { optionKeyOf(it, optionKey) == key }
    } else {
        selectedOptions + option
    }
}
