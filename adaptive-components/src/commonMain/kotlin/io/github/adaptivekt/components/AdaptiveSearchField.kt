package io.github.adaptivekt.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme

/**
 * Search input component with built-in search icon and optional clear action.
 *
 * @param value Current text value of the search field.
 * @param onValueChange Callback invoked when the text changes.
 * @param modifier Modifier applied to the root container.
 * @param placeholder Optional placeholder text (default "Search").
 * @param onClear Optional callback for the clear button.
 */
@Composable
public fun AdaptiveSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true,
    onClear: (() -> Unit)? = null,
) {
    AdaptiveTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        leadingIcon = {
            AdaptiveIcons.Search(size = 16.dp, tint = AdaptiveTheme.colors.textMuted)
        },
        trailingIcon = if (onClear != null && value.isNotEmpty()) {
            {
                AdaptiveIconButton(
                    onClick = onClear,
                    size = 28.dp,
                ) {
                    AdaptiveIcons.Close(size = 18.dp, tint = AdaptiveTheme.colors.textMuted)
                }
            }
        } else {
            null
        },
    )
}
