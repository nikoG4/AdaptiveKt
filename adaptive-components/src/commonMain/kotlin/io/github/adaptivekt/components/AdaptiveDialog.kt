package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import io.github.adaptivekt.core.AdaptiveVisualStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveElevationLevel
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
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val enterDuration = AdaptiveTheme.motion.durationMillis(AdaptiveTheme.motion.emphasizedMillis)
    val dialogShape = if (AdaptiveTheme.visualStyle == AdaptiveVisualStyle.Modern) {
        AdaptiveComponentDefaults.LargeShape
    } else {
        AdaptiveComponentDefaults.MediumShape
    }

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
            AnimatedVisibility(
                visible = appeared,
                enter = fadeIn(animationSpec = tween(durationMillis = enterDuration)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = tween(durationMillis = enterDuration),
                    ),
            ) {
                Box(
                    modifier = modifier
                    .widthIn(max = AdaptiveOverlayDefaults.DialogMaxWidth)
                    .fillMaxWidth()
                    .heightIn(max = AdaptiveOverlayDefaults.DialogMaxHeight)
                    .shadow(
                        elevation = AdaptiveTheme.elevations.resolve(AdaptiveElevationLevel.Floating),
                        shape = dialogShape,
                        ambientColor = AdaptiveTheme.elevations.shadowColor,
                        spotColor = AdaptiveTheme.elevations.shadowColor,
                    )
                    .clip(dialogShape)
                    .background(AdaptiveComponentDefaults.Surface)
                    .border(1.dp, AdaptiveComponentDefaults.Border, dialogShape)
                    // Consume taps without creating an unnecessary accessible
                    // "button" for the noninteractive modal surface.
                    .pointerInput(Unit) { detectTapGestures(onTap = {}) }
                    .padding(AdaptiveOverlayDefaults.DialogContentPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                Column {
                    if (title != null) {
                        BasicText(
                            text = title,
                            style = AdaptiveTheme.typography.subtitle.copy(
                                color = AdaptiveTheme.colors.textPrimary,
                                fontWeight = FontWeight.SemiBold,
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
                    if (AdaptiveTheme.visualStyle == AdaptiveVisualStyle.Modern) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(AdaptiveTheme.colors.border),
                        )
                        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                    }
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
}
