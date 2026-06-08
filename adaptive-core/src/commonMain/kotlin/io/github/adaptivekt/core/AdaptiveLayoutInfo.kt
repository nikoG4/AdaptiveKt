package io.github.adaptivekt.core

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import io.github.adaptivekt.navigation.AdaptiveNavigationMode

/**
 * Holds the resolved layout parameters for the current screen size.
 */
public data class AdaptiveLayoutInfo(
    val width: Dp,
    val height: Dp,
    val breakpoint: AdaptiveBreakpoint,
    val navigationMode: AdaptiveNavigationMode,
    val columns: Int,
    val pagePadding: Dp,
    val contentMaxWidth: Dp,
) {
    public val isCompact: Boolean get() = breakpoint == AdaptiveBreakpoint.Compact
    public val isMedium: Boolean get() = breakpoint == AdaptiveBreakpoint.Medium
    public val isExpanded: Boolean get() = breakpoint == AdaptiveBreakpoint.Expanded
    public val isLarge: Boolean get() = breakpoint == AdaptiveBreakpoint.Large

    public companion object {
        /**
         * Resolves the layout info given the current container constraints and configuration.
         */
        public fun resolve(width: Dp, height: Dp, config: AdaptiveConfig): AdaptiveLayoutInfo {
            val breakpoint = config.breakpoints.resolve(width)
            return AdaptiveLayoutInfo(
                width = width,
                height = height,
                breakpoint = breakpoint,
                navigationMode = config.navigation.resolve(breakpoint),
                columns = config.layout.columnsFor(breakpoint),
                pagePadding = config.spacing.pagePaddingFor(breakpoint),
                contentMaxWidth = config.layout.contentMaxWidth,
            )
        }
    }
}

/**
 * Composition local holding the resolved [AdaptiveLayoutInfo].
 * Fails if accessed outside an AdaptiveApp or AdaptiveContainer provider.
 */
public val LocalAdaptiveLayoutInfo = staticCompositionLocalOf<AdaptiveLayoutInfo> {
    error("No AdaptiveLayoutInfo provided. Make sure to wrap your content in AdaptiveApp.")
}
