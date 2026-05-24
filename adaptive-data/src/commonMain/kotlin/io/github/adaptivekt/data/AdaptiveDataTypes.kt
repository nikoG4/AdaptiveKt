package io.github.adaptivekt.data

import androidx.compose.runtime.Composable
import io.github.adaptivekt.core.AdaptiveBreakpoint

/** Slot type for filter controls shown above adaptive data content. */
public typealias AdaptiveFilterSlot = @Composable () -> Unit

/**
 * Determines how a column should appear in mobile card layout.
 */
public enum class AdaptiveDataMobileRole {
    Title,
    Subtitle,
    Metadata,
    Status,
    Media,
    Actions,
    Hidden,
}

public enum class AdaptiveActionPriority {
    Primary,
    Secondary,
    Overflow,
}

public data class AdaptiveDataAction<T>(
    val id: String,
    val label: String,
    val priority: AdaptiveActionPriority = AdaptiveActionPriority.Secondary,
    val destructive: Boolean = false,
    val onClick: (T) -> Unit,
)

/**
 * Represents a column in an adaptive data view.
 *
 * @param T The type of each row item.
 * @param id Unique column identifier.
 * @param header Column title text.
 * @param minBreakpoint Smallest breakpoint at which the column should be displayed.
 * @param weight Relative width weight for table layout.
 * @param mobileRole The role of this column when rendered as a mobile card.
 * @param mobilePriority Order of appearance in the mobile card.
 * @param showInMobileCard Whether this column should be shown by default in mobile cards.
 * @param cell Composable that renders the column value for a given row item.
 */
public data class AdaptiveDataColumn<T>(
    val id: String,
    val header: String,
    val minBreakpoint: AdaptiveBreakpoint = AdaptiveBreakpoint.Compact,
    val weight: Float = 1f,
    val mobileRole: AdaptiveDataMobileRole = AdaptiveDataMobileRole.Metadata,
    val mobilePriority: Int = 100,
    val showInMobileCard: Boolean = true,
    val cell: @Composable (T) -> Unit,
)

/**
 * Represents adaptive data loading and content states.
 */
public sealed interface AdaptiveDataState<out T>

public data object AdaptiveDataLoading : AdaptiveDataState<Nothing>

public data class AdaptiveDataError(
    val title: String,
    val description: String? = null,
) : AdaptiveDataState<Nothing>

public data class AdaptiveDataEmpty(
    val title: String,
    val description: String? = null,
) : AdaptiveDataState<Nothing>

public data class AdaptiveDataContent<T>(
    val items: List<T>,
) : AdaptiveDataState<T>

/** Determines whether the adaptive data view should render a table for the given breakpoint. */
public fun shouldUseTableLayout(breakpoint: AdaptiveBreakpoint): Boolean {
    return when (breakpoint) {
        AdaptiveBreakpoint.Compact, AdaptiveBreakpoint.Medium -> false
        AdaptiveBreakpoint.Expanded, AdaptiveBreakpoint.Large -> true
    }
}

/** Returns the subset of columns visible at the current breakpoint. */
public fun <T> visibleColumnsForBreakpoint(
    columns: List<AdaptiveDataColumn<T>>,
    breakpoint: AdaptiveBreakpoint,
): List<AdaptiveDataColumn<T>> {
    val visible = columns.filter { it.minBreakpoint.ordinal <= breakpoint.ordinal }
    return if (visible.isEmpty()) columns else visible
}

internal fun normalizeColumnWeight(weight: Float): Float = if (weight <= 0f) 1f else weight
