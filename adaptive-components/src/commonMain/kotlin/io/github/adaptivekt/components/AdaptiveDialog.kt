package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

public object AdaptiveOverlayDefaults {
    public val DialogMaxWidth = AdaptiveTokens.Widths.Form
    public val DialogMaxHeight = 560.dp
    public val DialogCompactMargin = AdaptiveTokens.Spacing.Medium
    public val DialogContentPadding = PaddingValues(AdaptiveTokens.Spacing.Large)
}

/**
 * Modal dialog primitive for focused decisions, confirmations, and alerts.
 *
 * @param onDismissRequest Callback invoked when the user attempts to close the dialog.
 * @param confirmButton Composable slot for the primary confirm action.
 * @param modifier Modifier applied to the dialog content surface.
 * @param title Optional text title displayed at the top.
 * @param dismissButton Optional composable slot for the secondary dismiss action.
 * @param dismissOnBackdropClick Whether clicking outside the dialog surface requests dismissal.
 * @param contentSelectionEnabled Whether the dialog body should allow text selection.
 * @param content Composable slot for the main dialog content.
 */
@Composable
public fun AdaptiveDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissButton: @Composable () -> Unit = {},
    dismissOnBackdropClick: Boolean = true,
    contentSelectionEnabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val surfaceInteractionSource = remember { MutableInteractionSource() }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = dismissOnBackdropClick,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AdaptiveTheme.colors.overlay)
                .padding(AdaptiveOverlayDefaults.DialogCompactMargin)
                .then(
                    if (dismissOnBackdropClick) {
                        Modifier
                            .adaptiveInteractiveCursor()
                            .clickable(onClick = onDismissRequest)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .widthIn(max = AdaptiveOverlayDefaults.DialogMaxWidth)
                    .heightIn(max = AdaptiveOverlayDefaults.DialogMaxHeight)
                    .clip(AdaptiveComponentDefaults.MediumShape)
                    .background(AdaptiveComponentDefaults.Surface)
                    .border(1.dp, AdaptiveComponentDefaults.Border, AdaptiveComponentDefaults.MediumShape)
                    .adaptiveInteractiveCursor(false)
                    .clickable(
                        interactionSource = surfaceInteractionSource,
                        indication = null,
                        onClick = {},
                    )
                    .padding(AdaptiveOverlayDefaults.DialogContentPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                Column {
                    if (title != null) {
                        BasicText(
                            text = title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AdaptiveTheme.colors.textPrimary,
                            ),
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                    }
                    AdaptiveSelectionArea(enabled = contentSelectionEnabled) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            content()
                        }
                    }
                    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        dismissButton()
                        Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                        confirmButton()
                    }
                }
            }
        }
    }
}
