package io.github.adaptivekt.core

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

private val LocalAdaptiveInfo = staticCompositionLocalOf<AdaptiveInfo> {
    error("AdaptiveInfo is not available. Use AdaptiveContent as the composition root.")
}

@Composable
fun AdaptiveContent(
    modifier: Modifier = Modifier,
    content: @Composable AdaptiveScope.() -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val info = remember(maxWidth, maxHeight) {
            AdaptiveInfo(
                windowSize = AdaptiveWindowSize(maxWidth, maxHeight),
                breakpoint = breakpointForWidth(maxWidth),
            )
        }
        val scope = remember(info) {
            AdaptiveScopeImpl(info)
        }

        CompositionLocalProvider(LocalAdaptiveInfo provides info) {
            scope.content()
        }
    }
}

@Composable
fun rememberAdaptiveInfo(): AdaptiveInfo = LocalAdaptiveInfo.current
