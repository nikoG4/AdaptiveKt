package io.github.adaptivekt.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

@Suppress("DEPRECATION")
@Composable
internal fun <T> AdaptiveAsyncMenuEmptyContent(
    state: AdaptiveOptionsState<T>,
    query: String,
    onRetry: (() -> Unit)?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AdaptiveTokens.Spacing.Large),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is AdaptiveOptionsState.Idle -> {
                BasicText(
                    text = "Start typing to search",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = AdaptiveComponentDefaults.MutedText,
                    )
                )
            }
            is AdaptiveOptionsState.Loading -> {
                BasicText(
                    text = "Loading...",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = AdaptiveComponentDefaults.Primary,
                    )
                )
            }
            is AdaptiveOptionsState.Error -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)
                ) {
                    BasicText(
                        text = state.message,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = AdaptiveComponentDefaults.Danger,
                        )
                    )
                    if (onRetry != null) {
                        BasicText(
                            text = "Retry",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = AdaptiveComponentDefaults.Primary,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .adaptiveInteractiveCursor(true)
                                .clickable { onRetry() }
                        )
                    }
                }
            }
            is AdaptiveOptionsState.Content,
            is AdaptiveOptionsState.Success -> {
                BasicText(
                    text = if (query.isNotEmpty()) "No results for \"$query\"" else "No options available",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = AdaptiveComponentDefaults.MutedText,
                    )
                )
            }
        }
    }
}

@Composable
internal fun <T> AdaptiveAsyncMenuFooterContent(
    state: AdaptiveOptionsState<T>,
    onLoadMore: (() -> Unit)?,
) {
    if (state is AdaptiveOptionsState.Content) {
        if (state.loadingMore) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AdaptiveTokens.Spacing.Small),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Loading more...",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = AdaptiveComponentDefaults.Primary,
                    )
                )
            }
        } else if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AdaptiveTokens.Spacing.Small),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = state.error,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = AdaptiveComponentDefaults.Danger,
                    )
                )
            }
        } else if (state.canLoadMore && onLoadMore != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .adaptiveInteractiveCursor(true)
                    .clickable { onLoadMore() }
                    .padding(vertical = AdaptiveTokens.Spacing.Small),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Load more",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = AdaptiveComponentDefaults.Primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
