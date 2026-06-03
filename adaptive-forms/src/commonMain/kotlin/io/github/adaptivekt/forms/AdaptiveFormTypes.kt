package io.github.adaptivekt.forms

import io.github.adaptivekt.core.AdaptiveBreakpoint

public data class AdaptiveFormColumns(
    val compact: Int = 1,
    val medium: Int = 2,
    val expanded: Int = 3,
    val large: Int = 3,
)

public sealed interface FieldSpan {
    public data object Full : FieldSpan
    public data object Half : FieldSpan
    public data object Third : FieldSpan
    public data object TwoThirds : FieldSpan
    public data class Columns(public val count: Int) : FieldSpan
}

public enum class LabelPosition {
    Top,
    Start,
}

public data class AdaptiveValidationMessage(
    val message: String,
    val type: AdaptiveValidationMessageType = AdaptiveValidationMessageType.Error,
)

public enum class AdaptiveValidationMessageType {
    Error,
    Warning,
    Info,
}

public fun columnsForBreakpoint(
    breakpoint: AdaptiveBreakpoint,
    columns: AdaptiveFormColumns,
): Int = when (breakpoint) {
    AdaptiveBreakpoint.Compact -> columns.compact
    AdaptiveBreakpoint.Medium -> columns.medium
    AdaptiveBreakpoint.Expanded -> columns.expanded
    AdaptiveBreakpoint.Large -> columns.large
}.coerceAtLeast(1)

public fun resolveFieldSpan(
    span: FieldSpan,
    activeColumns: Int,
): Int = when (span) {
    FieldSpan.Full -> activeColumns
    FieldSpan.Half -> maxOf(1, activeColumns / 2)
    FieldSpan.Third -> maxOf(1, activeColumns / 3)
    FieldSpan.TwoThirds -> maxOf(1, (activeColumns * 2) / 3)
    is FieldSpan.Columns -> span.count.coerceIn(1, activeColumns)
}
