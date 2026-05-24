package io.github.adaptivekt.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        trailingIcon = if (onClear != null && value.isNotEmpty()) {
            {
                AdaptiveIconButton(
                    onClick = onClear,
                    size = 28.dp,
                ) {
                    Box(modifier = Modifier.size(16.dp), contentAlignment = Alignment.Center) {
                        BasicText(
                            text = "x",
                            style = TextStyle(fontSize = 14.sp, color = Color(0xFF475569)),
                        )
                    }
                }
            }
        } else {
            null
        },
    )
}
