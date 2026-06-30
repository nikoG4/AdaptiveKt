package io.github.adaptivekt.components

public sealed interface AdaptiveOptionsState<out T> {
    public data object Idle : AdaptiveOptionsState<Nothing>
    
    public data object Loading : AdaptiveOptionsState<Nothing>

    public data class Content<T>(
        val items: List<T>,
        val refreshing: Boolean = false,
        val loadingMore: Boolean = false,
        val canLoadMore: Boolean = false,
        val error: String? = null,
    ) : AdaptiveOptionsState<T>

    public data class Error(
        val message: String,
    ) : AdaptiveOptionsState<Nothing>
    
    @Deprecated(
        message = "Success is deprecated in favor of Content to support pagination and background refresh.",
        replaceWith = ReplaceWith("AdaptiveOptionsState.Content(items)")
    )
    public data class Success<T>(val items: List<T>) : AdaptiveOptionsState<T>
}

/**
 * Convenience extension to safely get the items out of any state that has them.
 */
@Suppress("DEPRECATION")
public val <T> AdaptiveOptionsState<T>.currentItems: List<T>
    get() = when (this) {
        is AdaptiveOptionsState.Content -> this.items
        is AdaptiveOptionsState.Success -> this.items
        else -> emptyList()
    }
