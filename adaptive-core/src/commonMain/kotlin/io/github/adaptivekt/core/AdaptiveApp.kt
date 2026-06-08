package io.github.adaptivekt.core

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

/**
 * Root wrapper for AdaptiveKt applications.
 *
 * This composable measures the available screen space via an internal [BoxWithConstraints]
 * and computes the [AdaptiveLayoutInfo] using the provided [AdaptiveConfig]. It then provides
 * both the configuration and the resolved layout info to the composition tree.
 *
 * Components down the tree should consume [LocalAdaptiveLayoutInfo] to adapt their
 * layout instead of performing their own measurement.
 */
@Composable
public fun AdaptiveApp(
    modifier: Modifier = Modifier,
    config: AdaptiveConfig = AdaptiveConfig(),
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val layoutInfo = AdaptiveLayoutInfo.resolve(
            width = maxWidth,
            height = maxHeight,
            config = config
        )

        CompositionLocalProvider(
            LocalAdaptiveConfig provides config,
            LocalAdaptiveLayoutInfo provides layoutInfo,
            content = content
        )
    }
}
