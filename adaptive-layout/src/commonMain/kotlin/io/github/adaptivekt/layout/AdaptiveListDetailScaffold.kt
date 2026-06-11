package io.github.adaptivekt.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveBreakpoint
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo
import io.github.adaptivekt.core.adaptiveInteractiveCursor

/**
 * Defines the sizing and weight constraints for a single pane within the [AdaptiveListDetailScaffold].
 *
 * @param weight The proportional layout weight when rendered side-by-side.
 * @param minWidth The minimum width constraint.
 * @param preferredWidth The exact width constraint, bypassing weight if set.
 * @param maxWidth The maximum width constraint.
 */
public data class AdaptivePaneSpec(
    val weight: Float = 1f,
    val minWidth: Dp = Dp.Unspecified,
    val preferredWidth: Dp = Dp.Unspecified,
    val maxWidth: Dp = Dp.Unspecified
)

/**
 * Sizing policy for [AdaptiveListDetailScaffold] when list and detail panes are rendered side-by-side.
 *
 * The list pane receives a resolved width between [listMinWidth], [listPreferredWidth], and
 * [listMaxWidth]. The detail pane is then given the remaining horizontal space, which prevents
 * the common desktop failure mode where a narrow list floats beside a detail pane with a large
 * empty gap between them.
 *
 * @param listMinWidth Minimum width for the list pane.
 * @param listPreferredWidth Preferred width for the list pane.
 * @param listMaxWidth Maximum width for the list pane.
 * @param detailMinWidth Minimum width reserved for the detail pane before the list can grow.
 * @param detailPreferredWidth Preferred detail width used when [fillAvailableWidth] is false.
 * @param detailMaxWidth Optional maximum width for detail content.
 * @param gap Space between the list and detail panes.
 * @param maxContentWidth Optional maximum width for the full side-by-side layout.
 * @param fillAvailableWidth Whether the full side-by-side layout should consume all available width.
 */
public data class AdaptiveListDetailPanePolicy(
    val listMinWidth: Dp = 280.dp,
    val listPreferredWidth: Dp = 360.dp,
    val listMaxWidth: Dp = 420.dp,
    val detailMinWidth: Dp = 420.dp,
    val detailPreferredWidth: Dp = 720.dp,
    val detailMaxWidth: Dp = Dp.Unspecified,
    val gap: Dp = AdaptiveTokens.Spacing.Large,
    val maxContentWidth: Dp = Dp.Unspecified,
    val fillAvailableWidth: Boolean = true,
) {
    public companion object {
        public fun fromPaneSpecs(
            listPaneSpec: AdaptivePaneSpec,
            detailPaneSpec: AdaptivePaneSpec,
            gap: Dp = AdaptiveTokens.Spacing.Large,
            maxContentWidth: Dp = Dp.Unspecified,
            fillAvailableWidth: Boolean = true,
        ): AdaptiveListDetailPanePolicy = AdaptiveListDetailPanePolicy(
            listMinWidth = listPaneSpec.minWidth.orDefault(280.dp),
            listPreferredWidth = listPaneSpec.preferredWidth.orDefault(360.dp),
            listMaxWidth = listPaneSpec.maxWidth.orDefault(420.dp),
            detailMinWidth = detailPaneSpec.minWidth.orDefault(420.dp),
            detailPreferredWidth = detailPaneSpec.preferredWidth.orDefault(720.dp),
            detailMaxWidth = detailPaneSpec.maxWidth,
            gap = gap,
            maxContentWidth = maxContentWidth,
            fillAvailableWidth = fillAvailableWidth,
        )
    }
}

/**
 * Defines the compact-screen routing behavior for the list and detail panes.
 */
public enum class AdaptiveListDetailCompactBehavior {
    ShowListUntilSelection,
    AlwaysShowList,
    AlwaysShowDetail
}

/**
 * Defines the pane visibility state for medium, expanded, and large viewports.
 */
public enum class AdaptiveListDetailPaneMode {
    ListOnly,
    DetailOnly,
    ListAndDetail
}

/**
 * The internally resolved mode dictating which panes are actively rendered.
 */
public enum class AdaptiveListDetailResolvedMode {
    ListOnly,
    DetailOnly,
    ListAndDetail
}

/**
 * Configuration policy for [AdaptiveListDetailScaffold] that defines how it responds across breakpoints.
 *
 * @param compact Behavior when the viewport is Compact.
 * @param medium Behavior when the viewport is Medium.
 * @param expanded Behavior when the viewport is Expanded.
 * @param large Behavior when the viewport is Large.
 * @param showBackButtonOnCompactDetail Whether to inject a default 'Back' header on compact detail views.
 */
public data class AdaptiveListDetailBehavior(
    val compact: AdaptiveListDetailCompactBehavior = AdaptiveListDetailCompactBehavior.ShowListUntilSelection,
    val medium: AdaptiveListDetailPaneMode = AdaptiveListDetailPaneMode.ListAndDetail,
    val expanded: AdaptiveListDetailPaneMode = AdaptiveListDetailPaneMode.ListAndDetail,
    val large: AdaptiveListDetailPaneMode = AdaptiveListDetailPaneMode.ListAndDetail,
    val showBackButtonOnCompactDetail: Boolean = true
)

/**
 * Evaluates the current breakpoint, selection state, and behavior policy to determine the active rendering mode.
 */
public fun resolveAdaptiveListDetailMode(
    breakpoint: AdaptiveBreakpoint,
    hasSelection: Boolean,
    behavior: AdaptiveListDetailBehavior
): AdaptiveListDetailResolvedMode {
    return when (breakpoint) {
        AdaptiveBreakpoint.Compact -> {
            when (behavior.compact) {
                AdaptiveListDetailCompactBehavior.ShowListUntilSelection -> {
                    if (hasSelection) AdaptiveListDetailResolvedMode.DetailOnly else AdaptiveListDetailResolvedMode.ListOnly
                }
                AdaptiveListDetailCompactBehavior.AlwaysShowList -> AdaptiveListDetailResolvedMode.ListOnly
                AdaptiveListDetailCompactBehavior.AlwaysShowDetail -> AdaptiveListDetailResolvedMode.DetailOnly
            }
        }
        AdaptiveBreakpoint.Medium -> behavior.medium.toResolvedMode()
        AdaptiveBreakpoint.Expanded -> behavior.expanded.toResolvedMode()
        AdaptiveBreakpoint.Large -> behavior.large.toResolvedMode()
    }
}

private fun AdaptiveListDetailPaneMode.toResolvedMode(): AdaptiveListDetailResolvedMode = when (this) {
    AdaptiveListDetailPaneMode.ListOnly -> AdaptiveListDetailResolvedMode.ListOnly
    AdaptiveListDetailPaneMode.DetailOnly -> AdaptiveListDetailResolvedMode.DetailOnly
    AdaptiveListDetailPaneMode.ListAndDetail -> AdaptiveListDetailResolvedMode.ListAndDetail
}

internal data class AdaptiveListDetailPaneWidths(
    val rowWidth: Dp,
    val listWidth: Dp,
)

internal fun resolveAdaptiveListDetailPaneWidths(
    containerWidth: Dp,
    policy: AdaptiveListDetailPanePolicy,
): AdaptiveListDetailPaneWidths {
    val safeContainerWidth = containerWidth.coerceAtLeastDp(0.dp)
    val listMinWidth = policy.listMinWidth.orDefault(280.dp).coerceAtLeastDp(0.dp)
    val listPreferredWidth = policy.listPreferredWidth.orDefault(360.dp).coerceAtLeastDp(listMinWidth)
    val listMaxWidth = policy.listMaxWidth.orDefault(420.dp).coerceAtLeastDp(listMinWidth)
    val detailMinWidth = policy.detailMinWidth.orDefault(420.dp).coerceAtLeastDp(0.dp)
    val detailPreferredWidth = policy.detailPreferredWidth.orDefault(720.dp).coerceAtLeastDp(detailMinWidth)
    val gap = policy.gap.coerceAtLeastDp(0.dp)
    val maxAllowedWidth = if (policy.maxContentWidth.isUsable()) {
        minDp(safeContainerWidth, policy.maxContentWidth.coerceAtLeastDp(0.dp))
    } else {
        safeContainerWidth
    }
    val preferredRowWidth = listPreferredWidth + gap + detailPreferredWidth
    val rowWidth = if (policy.fillAvailableWidth) {
        maxAllowedWidth
    } else {
        minDp(maxAllowedWidth, preferredRowWidth)
    }
    val availableForList = (rowWidth - gap - detailMinWidth).coerceAtLeastDp(listMinWidth)
    val listUpperBound = minDp(listMaxWidth, availableForList).coerceAtLeastDp(listMinWidth)
    val listWidth = listPreferredWidth.coerceInDp(listMinWidth, listUpperBound)

    return AdaptiveListDetailPaneWidths(
        rowWidth = rowWidth,
        listWidth = listWidth,
    )
}

internal fun Modifier.applyPaneSpec(spec: AdaptivePaneSpec): Modifier {
    var mod = this
    if (spec.minWidth != Dp.Unspecified || spec.maxWidth != Dp.Unspecified) {
        mod = mod.widthIn(min = spec.minWidth, max = spec.maxWidth)
    }
    if (spec.preferredWidth != Dp.Unspecified) {
        mod = mod.width(spec.preferredWidth)
    }
    return mod
}

private fun Modifier.applyDetailPolicy(policy: AdaptiveListDetailPanePolicy): Modifier {
    val detailMinWidth = policy.detailMinWidth.orDefault(0.dp).coerceAtLeastDp(0.dp)
    return if (policy.detailMaxWidth.isUsable()) {
        widthIn(min = detailMinWidth, max = policy.detailMaxWidth)
    } else if (detailMinWidth > 0.dp) {
        widthIn(min = detailMinWidth)
    } else {
        this
    }
}

private fun Dp.isUsable(): Boolean = this != Dp.Unspecified

private fun Dp.orDefault(default: Dp): Dp = if (isUsable()) this else default

private fun Dp.coerceAtLeastDp(minimumValue: Dp): Dp = if (this < minimumValue) minimumValue else this

private fun Dp.coerceInDp(minimumValue: Dp, maximumValue: Dp): Dp =
    maxDp(minimumValue, minDp(this, maximumValue))

private fun minDp(first: Dp, second: Dp): Dp = if (first < second) first else second

private fun maxDp(first: Dp, second: Dp): Dp = if (first > second) first else second

@Composable
internal fun DefaultEmptyDetail() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicText("Select an item", style = TextStyle(fontWeight = FontWeight.Bold))
            BasicText("Choose an item from the list to view its details.")
        }
    }
}

@Composable
private fun EmptyDetailPane(
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdaptiveTheme.colors.surface, shape = AdaptiveTheme.shapes.medium)
            .border(width = 1.dp, color = AdaptiveTheme.colors.border, shape = AdaptiveTheme.shapes.medium)
            .padding(AdaptiveTokens.Spacing.Large),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
internal fun DefaultCompactDetailHeader(onBackToList: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .adaptiveInteractiveCursor()
            .clickable { onBackToList() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText("← Back", style = TextStyle(fontWeight = FontWeight.Bold))
    }
}

/**
 * A highly opinionated scaffold for the Selectable List + Detail pattern (e.g. Email Inbox, Chat Workspace).
 * 
 * Automatically switches between a single-pane stacked navigation on compact screens, and a two-pane 
 * side-by-side layout on larger screens without requiring manual Breakpoint or BoxWithConstraints checks.
 *
 * @param selectedItem The currently selected item. If null, the empty state or list is shown.
 * @param onBackToList Callback triggered when the compact detail view needs to return to the list.
 * @param modifier Applied to the root container.
 * @param listPaneSpec Size and weight definitions for the list pane.
 * @param detailPaneSpec Size and weight definitions for the detail pane.
 * @param panePolicy Side-by-side pane sizing policy.
 * @param behavior Configuration policy determining pane modes per breakpoint.
 * @param listPane The composable slot for the list content.
 * @param detailPane The composable slot for the detail content.
 * @param emptyDetail The composable slot shown in the detail pane when no item is selected.
 * @param compactDetailHeader Optional custom header injected above the detail pane on compact screens.
 */
@Composable
public fun <T> AdaptiveListDetailScaffold(
    selectedItem: T?,
    onBackToList: () -> Unit,
    modifier: Modifier = Modifier,
    listPaneSpec: AdaptivePaneSpec = AdaptivePaneSpec(weight = 0.35f),
    detailPaneSpec: AdaptivePaneSpec = AdaptivePaneSpec(weight = 0.65f),
    panePolicy: AdaptiveListDetailPanePolicy = AdaptiveListDetailPanePolicy.fromPaneSpecs(
        listPaneSpec = listPaneSpec,
        detailPaneSpec = detailPaneSpec,
    ),
    behavior: AdaptiveListDetailBehavior = AdaptiveListDetailBehavior(),
    listPane: @Composable () -> Unit,
    detailPane: @Composable (T) -> Unit,
    emptyDetail: @Composable () -> Unit = { DefaultEmptyDetail() },
    compactDetailHeader: (@Composable (T) -> Unit)? = null
) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    val resolvedMode = resolveAdaptiveListDetailMode(
        breakpoint = layoutInfo.breakpoint,
        hasSelection = selectedItem != null,
        behavior = behavior
    )

    when (resolvedMode) {
        AdaptiveListDetailResolvedMode.ListOnly -> {
            Box(modifier = modifier.fillMaxSize()) {
                listPane()
            }
        }
        AdaptiveListDetailResolvedMode.DetailOnly -> {
            Box(modifier = modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (selectedItem != null) {
                        if (compactDetailHeader != null) {
                            compactDetailHeader(selectedItem)
                        } else if (behavior.showBackButtonOnCompactDetail) {
                            DefaultCompactDetailHeader(onBackToList)
                        }
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        if (selectedItem != null) {
                            detailPane(selectedItem)
                        } else {
                            EmptyDetailPane(emptyDetail)
                        }
                    }
                }
            }
        }
        AdaptiveListDetailResolvedMode.ListAndDetail -> {
            BoxWithConstraints(modifier = modifier.fillMaxSize()) {
                val paneWidths = resolveAdaptiveListDetailPaneWidths(maxWidth, panePolicy)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Row(
                        modifier = Modifier
                            .width(paneWidths.rowWidth)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(panePolicy.gap),
                    ) {
                        Box(
                            modifier = Modifier
                                .width(paneWidths.listWidth)
                                .fillMaxHeight(),
                        ) {
                            listPane()
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f, fill = true)
                                .fillMaxHeight()
                                .applyDetailPolicy(panePolicy),
                        ) {
                            if (selectedItem != null) {
                                detailPane(selectedItem)
                            } else {
                                EmptyDetailPane(emptyDetail)
                            }
                        }
                    }
                }
            }
        }
    }
}
