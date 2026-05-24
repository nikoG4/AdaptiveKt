package io.github.adaptivekt.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
public fun AdaptiveSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = title,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AdaptiveComponentDefaults.Text,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
                BasicText(
                    text = description,
                    style = TextStyle(fontSize = 14.sp, color = AdaptiveComponentDefaults.MutedText),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (action != null) {
            action()
        }
    }
}
