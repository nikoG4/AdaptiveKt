package io.github.adaptivekt.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

public enum class AdaptiveTwoPaneOrientation {
    Stacked,
    SideBySide
}

public fun adaptiveTwoPaneOrientation(
    isCompact: Boolean,
    collapseOnCompact: Boolean
): AdaptiveTwoPaneOrientation {
    return if (collapseOnCompact && isCompact) {
        AdaptiveTwoPaneOrientation.Stacked
    } else {
        AdaptiveTwoPaneOrientation.SideBySide
    }
}

/**
 * A standard two-pane layout that automatically switches between a side-by-side row
 * and a stacked column based on the adaptive breakpoint.
 *
 * @param modifier The modifier to be applied to the container.
 * @param primaryWeight The weight applied to the primary pane when in Row mode.
 * @param secondaryWeight The weight applied to the secondary pane when in Row mode.
 * @param gap The spacing between the two panes.
 * @param collapseOnCompact If true, the panes stack vertically on compact screens.
 * @param primary The content for the primary pane.
 * @param secondary The content for the secondary pane.
 */
@Composable
public fun AdaptiveTwoPane(
    modifier: Modifier = Modifier,
    primaryWeight: Float = 1f,
    secondaryWeight: Float = 1f,
    gap: Dp = AdaptiveTokens.Spacing.Large,
    collapseOnCompact: Boolean = true,
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit
) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    val orientation = adaptiveTwoPaneOrientation(layoutInfo.isCompact, collapseOnCompact)

    if (orientation == AdaptiveTwoPaneOrientation.Stacked) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            primary()
            secondary()
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(gap)
        ) {
            androidx.compose.foundation.layout.Box(modifier = Modifier.weight(primaryWeight)) {
                primary()
            }
            androidx.compose.foundation.layout.Box(modifier = Modifier.weight(secondaryWeight)) {
                secondary()
            }
        }
    }
}
