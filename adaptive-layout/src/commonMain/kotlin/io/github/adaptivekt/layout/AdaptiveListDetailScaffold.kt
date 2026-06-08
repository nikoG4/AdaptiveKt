package io.github.adaptivekt.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

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
internal fun DefaultCompactDetailHeader(onBackToList: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                            emptyDetail()
                        }
                    }
                }
            }
        }
        AdaptiveListDetailResolvedMode.ListAndDetail -> {
            AdaptiveTwoPane(
                modifier = modifier,
                primaryWeight = listPaneSpec.weight,
                secondaryWeight = detailPaneSpec.weight,
                collapseOnCompact = false, // The list/detail specific compact logic overrides standard stacking
                primary = {
                    Box(modifier = Modifier.fillMaxSize().applyPaneSpec(listPaneSpec)) {
                        listPane()
                    }
                },
                secondary = {
                    Box(modifier = Modifier.fillMaxSize().applyPaneSpec(detailPaneSpec)) {
                        if (selectedItem != null) {
                            detailPane(selectedItem)
                        } else {
                            emptyDetail()
                        }
                    }
                }
            )
        }
    }
}
