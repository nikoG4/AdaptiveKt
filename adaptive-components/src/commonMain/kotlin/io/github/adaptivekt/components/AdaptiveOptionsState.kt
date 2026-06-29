package io.github.adaptivekt.components

public sealed interface AdaptiveOptionsState<out T> {
    public data object Loading : AdaptiveOptionsState<Nothing>
    public data class Success<T>(val items: List<T>) : AdaptiveOptionsState<T>
    public data class Error(val message: String) : AdaptiveOptionsState<Nothing>
}
