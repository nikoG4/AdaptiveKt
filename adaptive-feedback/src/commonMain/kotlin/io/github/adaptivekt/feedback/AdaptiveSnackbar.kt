package io.github.adaptivekt.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.core.AdaptiveTheme

/**
 * Non-modal notification content. Place in a SnackbarHost/overlay owned by the
 * caller; lifetime, queueing and placement are deliberately not managed here.
 */
@Composable
public fun AdaptiveSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AdaptiveTheme.colors.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = message,
            modifier = Modifier.weight(1f),
            style = TextStyle(color = AdaptiveTheme.colors.textPrimary, fontSize = 14.sp),
        )
        if (actionLabel != null && onAction != null) {
            AdaptiveButton(
                text = actionLabel,
                onClick = onAction,
                variant = AdaptiveButtonVariant.Secondary,
                size = AdaptiveButtonSize.Small,
            )
        }
        if (onDismiss != null) {
            AdaptiveButton(
                text = "Dismiss",
                onClick = onDismiss,
                variant = AdaptiveButtonVariant.Ghost,
                size = AdaptiveButtonSize.Small,
            )
        }
    }
}

/** Short message notification without an action. */
@Composable
public fun AdaptiveToast(
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
) {
    AdaptiveSnackbar(message = message, modifier = modifier, onDismiss = onDismiss)
}
