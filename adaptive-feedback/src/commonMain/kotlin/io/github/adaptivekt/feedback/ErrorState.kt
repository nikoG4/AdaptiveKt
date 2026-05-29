package io.github.adaptivekt.feedback

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Displays an error state with an optional icon, title, description, and retry action.
 *
 * @param title Main error title text (required).
 * @param modifier Optional modifier for root container.
 * @param description Optional error description or details.
 * @param icon Optional composable for a leading icon (e.g., error indicator).
 * @param retryAction Optional composable for a retry button or similar.
 */
@Composable
fun ErrorState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    retryAction: (@Composable () -> Unit)? = null,
) {
    FeedbackStateLayout(modifier = modifier) {
        if (icon != null) {
            icon()
        } else {
            DefaultFeedbackIcon(
                background = AdaptiveTheme.colors.dangerSubtle,
            ) {
                AdaptiveIcons.Close(
                    size = 32.dp,
                    tint = AdaptiveTheme.colors.danger,
                    contentDescription = "Error state",
                )
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))

        SimpleText(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        if (description != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            SimpleText(
                text = description,
                fontSize = 14.sp,
                color = AdaptiveTheme.colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }

        if (retryAction != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            retryAction()
        }
    }
}
