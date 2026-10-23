package io.github.adaptivekt.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Masked input with accessible reveal control and password IME. */
@Composable
public fun AdaptivePasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    validationMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done,
) {
    var revealed by remember { mutableStateOf(false) }
    AdaptiveTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        validationMessage = validationMessage,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (revealed) KeyboardType.Text else KeyboardType.Password,
            imeAction = imeAction,
            autoCorrectEnabled = false,
        ),
        visualTransformation = if (revealed) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            AdaptiveIconButton(
                onClick = { revealed = !revealed },
                enabled = enabled,
                size = 40.dp,
                contentDescription = if (revealed) "Hide password" else "Show password",
            ) {
                BasicText(
                    text = if (revealed) "Hide" else "Show",
                    style = TextStyle(fontSize = 12.sp, color = AdaptiveComponentDefaults.Text),
                )
            }
        },
    )
}
