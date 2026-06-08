package io.github.adaptivekt.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

/**
 * A responsive action bar designed to hold search fields, filters, and page-level actions.
 * 
 * On compact screens, it stacks the leading content above the actions.
 * On larger screens, it places the leading content on the left and actions on the right.
 *
 * @param modifier The modifier to be applied to the action bar.
 * @param primaryAction The primary action button or content.
 * @param secondaryActions Secondary actions placed alongside the primary action.
 * @param leadingContent Content placed on the leading edge (e.g. search fields).
 */
@Composable
public fun AdaptiveActionBar(
    modifier: Modifier = Modifier,
    primaryAction: (@Composable () -> Unit)? = null,
    secondaryActions: (@Composable RowScope.() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    val compact = layoutInfo.isCompact

    if (compact) {
        Column(modifier = modifier.fillMaxWidth()) {
            if (leadingContent != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
                ) {
                    leadingContent()
                }
                Spacer(Modifier.height(AdaptiveTokens.Spacing.Medium))
            }
            if (primaryAction != null || secondaryActions != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
                ) {
                    if (secondaryActions != null) {
                        secondaryActions()
                    }
                    Spacer(Modifier.weight(1f))
                    if (primaryAction != null) {
                        primaryAction()
                    }
                }
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
            ) {
                if (leadingContent != null) {
                    leadingContent()
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
            ) {
                if (secondaryActions != null) {
                    secondaryActions()
                }
                if (primaryAction != null) {
                    primaryAction()
                }
            }
        }
    }
}
