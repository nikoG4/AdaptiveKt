package io.github.adaptivekt.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.icons.AdaptiveIcons

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
            AdaptiveIcons.Search(size = 16.dp, tint = Color(0xFF64748B))
        },
        trailingIcon = if (onClear != null && value.isNotEmpty()) {
            {
                AdaptiveIconButton(
                    onClick = onClear,
                    size = 28.dp,
                ) {
                    AdaptiveIcons.Close(size = 18.dp)
                }
            }
        } else {
            null
        },
    )
}
