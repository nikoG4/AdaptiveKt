package io.github.adaptivekt.core

interface AdaptiveScope {
    val adaptiveInfo: AdaptiveInfo

    fun <T> adaptiveValue(
        compact: T,
        medium: T? = null,
        expanded: T? = null,
        large: T? = null,
    ): T = adaptiveValue(
        adaptiveInfo.breakpoint,
        compact,
        medium,
        expanded,
        large,
    )
}

internal class AdaptiveScopeImpl(
    override val adaptiveInfo: AdaptiveInfo,
) : AdaptiveScope
