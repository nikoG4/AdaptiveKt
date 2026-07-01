package io.github.adaptivekt.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import io.github.adaptivekt.components.AdaptiveCheckbox

@Composable
internal fun <K : Any> AdaptiveDataSelectAllCheckbox(
    selectionState: AdaptiveDataSelectionState<K>,
    visibleKeys: List<K>,
    disabledKeys: Set<K>,
    selectionMode: AdaptiveDataSelectionMode,
    onSelectionStateChange: (AdaptiveDataSelectionState<K>) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Select all visible rows",
) {
    val state = if (selectionMode == AdaptiveDataSelectionMode.Multiple) {
        resolveAdaptiveSelectAllState(
            selectedKeys = selectionState.selectedKeys,
            visibleKeys = visibleKeys,
            disabledKeys = disabledKeys,
            mode = selectionMode
        )
    } else {
        AdaptiveSelectAllState.Disabled
    }

    AdaptiveCheckbox(
        state = when (state) {
            AdaptiveSelectAllState.Checked -> ToggleableState.On
            AdaptiveSelectAllState.Indeterminate -> ToggleableState.Indeterminate
            AdaptiveSelectAllState.Unchecked, AdaptiveSelectAllState.Disabled -> ToggleableState.Off
        },
        enabled = state != AdaptiveSelectAllState.Disabled,
        onClick = {
            val op = resolveAdaptiveSelectAllIntent(state)
            if (op != null) {
                onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
            }
        },
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
        }
    )
}

internal fun resolveAdaptiveSelectAllIntent(state: AdaptiveSelectAllState): AdaptiveDataSelectionOperation<Nothing>? {
    return when (state) {
        AdaptiveSelectAllState.Checked -> AdaptiveDataSelectionOperation.ClearVisible
        AdaptiveSelectAllState.Indeterminate, AdaptiveSelectAllState.Unchecked -> AdaptiveDataSelectionOperation.SelectAllVisible
        AdaptiveSelectAllState.Disabled -> null
    }
}
