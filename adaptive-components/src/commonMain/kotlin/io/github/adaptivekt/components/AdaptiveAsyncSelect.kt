package io.github.adaptivekt.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * An asynchronous wrapper around [AdaptiveSelect] that handles remote data states.
 * 
 * Displays a loading indicator or error message in the dropdown menu when no options are available.
 */
@Composable
public fun <T> AdaptiveAsyncSelect(
    state: AdaptiveOptionsState<T>,
    selectedOption: T?,
    onSelectedOptionChange: (T?) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select an option",
    enabled: Boolean = true,
    searchable: Boolean = false,
    clearable: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    maxMenuHeight: Dp = 320.dp,
    optionKey: ((T) -> Any)? = null,
) {
    val options = when (state) {
        is AdaptiveOptionsState.Success -> state.items
        else -> emptyList()
    }

    AdaptiveSelect(
        options = options,
        selectedOption = selectedOption,
        onSelectedOptionChange = onSelectedOptionChange,
        optionLabel = optionLabel,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled && state !is AdaptiveOptionsState.Loading,
        searchable = searchable,
        clearable = clearable,
        isError = isError || state is AdaptiveOptionsState.Error,
        supportingText = supportingText,
        maxMenuHeight = maxMenuHeight,
        optionKey = optionKey,
        emptyContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AdaptiveTokens.Spacing.Large),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    is AdaptiveOptionsState.Loading -> {
                        BasicText(
                            text = "Loading...",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = AdaptiveComponentDefaults.Primary,
                            ),
                            modifier = Modifier.padding(horizontal = AdaptiveTokens.Spacing.Medium)
                        )
                    }
                    is AdaptiveOptionsState.Error -> {
                        BasicText(
                            text = state.message,
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = AdaptiveComponentDefaults.Danger,
                            ),
                            modifier = Modifier.padding(horizontal = AdaptiveTokens.Spacing.Medium)
                        )
                    }
                    is AdaptiveOptionsState.Success -> {
                        BasicText(
                            text = "No options available",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = AdaptiveComponentDefaults.MutedText,
                            ),
                            modifier = Modifier.padding(horizontal = AdaptiveTokens.Spacing.Medium)
                        )
                    }
                }
            }
        }
    )
}
