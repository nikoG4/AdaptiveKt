package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Modal dialog primitive for focused decisions, confirmations, and alerts.
 *
 * @param onDismissRequest Callback invoked when the user attempts to close the dialog.
 * @param confirmButton Composable slot for the primary confirm action.
 * @param modifier Modifier applied to the dialog content surface.
 * @param title Optional text title displayed at the top.
 * @param dismissButton Optional composable slot for the secondary dismiss action.
 * @param content Composable slot for the main dialog content.
 */
@Composable
public fun AdaptiveDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AdaptiveTheme.colors.overlay)
                .clickable(onClick = onDismissRequest),
        )
        Box(
            modifier = Modifier
                .widthIn(max = AdaptiveTokens.Widths.Form)
                .heightIn(max = 560.dp)
                .clip(AdaptiveComponentDefaults.MediumShape)
                .background(AdaptiveComponentDefaults.Surface)
                .border(1.dp, AdaptiveComponentDefaults.Border, AdaptiveComponentDefaults.MediumShape)
                .padding(AdaptiveTokens.Spacing.Large)
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
                content()
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
