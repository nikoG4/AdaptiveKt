package io.github.adaptivekt.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * An asynchronous, caller-owned wrapper around [AdaptiveSelect].
 * 
 * Supports remote data fetching, pagination, and robust async states.
 * The `query` and network operations are controlled by the caller.
 */
@Composable
public fun <T> AdaptiveAsyncSelect(
    state: AdaptiveOptionsState<T>,
    query: String,
    onQueryChange: (String) -> Unit,
    selectedOption: T?,
    onSelectedOptionChange: (T?) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select an option",
    enabled: Boolean = true,
    clearable: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    maxMenuHeight: Dp = 320.dp,
    optionKey: ((T) -> Any)? = null,
    optionEnabled: (T) -> Boolean = { true },
    onRetry: (() -> Unit)? = null,
    onLoadMore: (() -> Unit)? = null,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
) {
    var internalExpanded by remember { mutableStateOf(false) }
    
    val effectiveExpanded = expanded ?: internalExpanded
    val setEffectiveExpanded: (Boolean) -> Unit = {
        if (onExpandedChange != null) onExpandedChange(it)
        else internalExpanded = it
    }

    val items = state.currentItems
    val isHardDisabled = !enabled || (state is AdaptiveOptionsState.Error && items.isEmpty())
    // Notice we do NOT disable the trigger just because it is Loading. 
    // Users can open the menu and see "Loading...".

    AdaptiveSelect(
        expanded = effectiveExpanded,
        onExpandedChange = setEffectiveExpanded,
        options = items,
        selectedOption = selectedOption,
        onSelectedOptionChange = onSelectedOptionChange,
        optionLabel = optionLabel,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = !isHardDisabled,
        searchable = true, 
        searchQuery = query,
        onSearchQueryChange = onQueryChange,
        clearable = clearable,
        isError = isError || state is AdaptiveOptionsState.Error,
        supportingText = supportingText,
        maxMenuHeight = maxMenuHeight,
        optionKey = optionKey,
        optionEnabled = optionEnabled,
        emptyContent = {
            AdaptiveAsyncMenuEmptyContent(state, query, onRetry)
        },
        footerContent = {
            AdaptiveAsyncMenuFooterContent(state, onLoadMore)
        }
    )
}
