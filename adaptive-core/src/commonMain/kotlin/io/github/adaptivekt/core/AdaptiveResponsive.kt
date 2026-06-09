package io.github.adaptivekt.core

import androidx.compose.runtime.Composable

/**
 * A declarative utility for rendering different composables based on the current adaptive breakpoint.
 * It uses a cascading fallback mechanism:
 * - If a specific breakpoint slot is not provided, it falls back to the next smallest slot.
 * - [compact] is the only required slot, as it serves as the ultimate fallback for all sizes.
 */
@Composable
public fun AdaptiveResponsive(
    compact: @Composable () -> Unit,
    medium: @Composable (() -> Unit)? = null,
    expanded: @Composable (() -> Unit)? = null,
    large: @Composable (() -> Unit)? = null
) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current

    when (layoutInfo.breakpoint) {
        AdaptiveBreakpoint.Large -> {
            if (large != null) large()
            else if (expanded != null) expanded()
            else if (medium != null) medium()
            else compact()
        }
        AdaptiveBreakpoint.Expanded -> {
            if (expanded != null) expanded()
            else if (medium != null) medium()
            else compact()
        }
        AdaptiveBreakpoint.Medium -> {
            if (medium != null) medium()
            else compact()
        }
        AdaptiveBreakpoint.Compact -> {
            compact()
        }
    }
}
