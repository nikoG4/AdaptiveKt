package io.github.adaptivekt.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
internal fun SimpleText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    color: Color = Color(0xFF0F172A),
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign? = null,
) {
    androidx.compose.foundation.text.BasicText(
        text = text,
        modifier = modifier,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = fontSize,
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
        ),
    )
}

/**
 * Displays an empty state with an optional icon, title, description, and action.
 *
 * @param title Main title text (required).
 * @param modifier Optional modifier for root container.
 * @param description Optional secondary description text.
 * @param icon Optional composable for a leading icon.
 * @param action Optional composable for a call-to-action button or similar.
 */
@Composable
fun EmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
) {
    FeedbackStateLayout(modifier = modifier) {
        if (icon != null) {
            icon()
        } else {
            DefaultFeedbackIcon(
                background = Color(0xFFEFF6FF),
            ) {
                AdaptiveIcons.Search(
                    size = 32.dp,
                    tint = Color(0xFF1D4ED8),
                    contentDescription = "Empty state",
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
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
            )
        }

        if (action != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            action()
        }
    }
}

@Composable
internal fun DefaultFeedbackIcon(
    background: Color,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(background, RoundedCornerShape(AdaptiveTokens.Radius.Large)),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
