package io.github.adaptivekt.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.state.ToggleableState
import io.github.adaptivekt.components.AdaptiveCheckbox

@Composable
internal fun <K : Any> AdaptiveDataSelectAllCheckbox(
    selectionState: AdaptiveDataSelectionState<K>,
    visibleKeys: List<K>,
    disabledKeys: Set<K>,
    selectionMode: AdaptiveDataSelectionMode,
    onSelectionStateChange: (AdaptiveDataSelectionState<K>) -> Unit,
) {
    if (selectionMode != AdaptiveDataSelectionMode.Multiple) {
        return
    }

    val state = resolveAdaptiveSelectAllState(
        selectedKeys = selectionState.selectedKeys,
        visibleKeys = visibleKeys,
        disabledKeys = disabledKeys,
        mode = selectionMode
    )

    AdaptiveCheckbox(
        state = when (state) {
            AdaptiveSelectAllState.Checked -> ToggleableState.On
            AdaptiveSelectAllState.Indeterminate -> ToggleableState.Indeterminate
            AdaptiveSelectAllState.Unchecked, AdaptiveSelectAllState.Disabled -> ToggleableState.Off
        },
        enabled = state != AdaptiveSelectAllState.Disabled,
        onClick = {
            val op = if (state == AdaptiveSelectAllState.Checked) {
                AdaptiveDataSelectionOperation.ClearVisible
            } else {
                AdaptiveDataSelectionOperation.SelectAllVisible
            }
            onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
        }
    )
}
