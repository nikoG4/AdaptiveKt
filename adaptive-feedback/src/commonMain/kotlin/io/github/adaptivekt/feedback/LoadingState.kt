package io.github.adaptivekt.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Simple loading indicator with Foundation-only primitives.
 */
@Composable
internal fun SimpleLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .border(width = 3.dp, color = Color(0xFFBFDBFE), shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(Color(0xFF2563EB), CircleShape),
        )
    }
}

/**
 * Displays a loading state with a progress indicator and optional message.
 *
 * @param modifier Optional modifier for root container.
 * @param message Optional text message to display below the progress indicator.
 */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    FeedbackStateLayout(modifier = modifier) {
        SimpleLoadingIndicator()

        if (message != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            SimpleText(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
            )
        }
    }
}
