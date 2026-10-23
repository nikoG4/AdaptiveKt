package io.github.adaptivekt.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

/** Multiline field with bounded visible lines and optional character limit. */
@Composable
public fun AdaptiveMultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    validationMessage: String? = null,
    minLines: Int = 2,
    maxLines: Int = 6,
    maxLength: Int? = null,
) {
    require(minLines >= 1 && maxLines >= minLines) { "Invalid multiline line limits" }
    require(maxLength == null || maxLength >= 0) { "maxLength must be nonnegative" }
    AdaptiveTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        validationMessage = validationMessage,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
        maxLength = maxLength,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
    )
}
