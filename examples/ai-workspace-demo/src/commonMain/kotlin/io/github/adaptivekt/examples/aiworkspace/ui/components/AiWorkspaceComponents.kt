package io.github.adaptivekt.examples.aiworkspace.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
public fun UsageMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    AdaptiveCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.bodyMedium, color = AdaptiveTheme.colors.textSecondary)
        Spacer(Modifier.height(AdaptiveTokens.Spacing.Small))
        Text(
            value,
            style = MaterialTheme.typography.headlineMedium,
            color = AdaptiveTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
public fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier,
    isSuccess: Boolean = true
) {
    AdaptiveBadge(
        text = status,
        modifier = modifier,
        tone = if (isSuccess) AdaptiveBadgeTone.Success else AdaptiveBadgeTone.Danger,
    )
}
