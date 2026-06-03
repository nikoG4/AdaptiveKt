package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Foundation text input component with label, placeholder, disabled state and validation message.
 *
 * @param value Current text value of the field.
 * @param onValueChange Callback invoked when the text changes.
 * @param modifier Modifier applied to the root field container.
 * @param label Optional text label displayed above the input.
 * @param placeholder Optional text displayed when the input is empty.
 * @param enabled Disables input interaction and lowers contrast when false.
 * @param validationMessage Optional validation message; when present, field renders in error state.
 * @param leadingIcon Optional composable slot for a leading icon inside the field.
 * @param trailingIcon Optional composable slot for a trailing icon inside the field.
 */
@Composable
public fun AdaptiveTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    validationMessage: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    val hasError = !validationMessage.isNullOrBlank()
    val border = when {
        hasError -> AdaptiveComponentDefaults.Danger
        focused -> AdaptiveComponentDefaults.Primary
        else -> AdaptiveComponentDefaults.BorderStrong
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            BasicText(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AdaptiveComponentDefaults.Text,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = AdaptiveTokens.Sizes.ButtonHeight)
                .clip(shape)
                .background(if (enabled) AdaptiveComponentDefaults.Surface else AdaptiveComponentDefaults.DisabledSurface, shape)
                .border(1.dp, border, shape)
                .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                enabled = enabled,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = if (enabled) AdaptiveComponentDefaults.Text else AdaptiveComponentDefaults.DisabledText,
                ),
                cursorBrush = SolidColor(AdaptiveComponentDefaults.Primary),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && placeholder != null) {
                        BasicText(
                            text = placeholder,
                            style = TextStyle(fontSize = 14.sp, color = AdaptiveComponentDefaults.MutedText),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                },
            )

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                trailingIcon()
            }
        }

        if (hasError) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
            BasicText(
                text = validationMessage.orEmpty(),
                style = TextStyle(fontSize = 12.sp, color = AdaptiveComponentDefaults.Danger),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
