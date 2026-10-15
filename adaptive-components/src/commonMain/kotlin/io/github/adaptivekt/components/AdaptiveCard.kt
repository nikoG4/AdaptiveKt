package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveElevationLevel
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

@Composable
public fun AdaptiveCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AdaptiveTokens.Spacing.Large),
    onClick: (() -> Unit)? = null,
    contentSelectionEnabled: Boolean = false,
    elevation: AdaptiveElevationLevel = AdaptiveElevationLevel.Low,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = AdaptiveComponentDefaults.MediumShape
    val elevationTokens = AdaptiveTheme.elevations
    val surfaceColor = if (elevation == AdaptiveElevationLevel.None) {
        AdaptiveComponentDefaults.Surface
    } else {
        AdaptiveTheme.colors.surfaceRaised
    }
    val clickableModifier = if (onClick != null) {
        Modifier
            .adaptiveInteractiveCursor()
            .clickable(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AdaptiveTokens.Sizes.CardMinHeight)
            .shadow(
                elevation = elevationTokens.resolve(elevation),
                shape = shape,
                clip = false,
                ambientColor = elevationTokens.shadowColor,
                spotColor = elevationTokens.shadowColor,
            )
            .clip(shape)
            .background(surfaceColor, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape)
            .then(clickableModifier)
            .padding(contentPadding),
    ) {
        if (contentSelectionEnabled && onClick == null) {
            AdaptiveSelectionArea {
                Column(modifier = Modifier.fillMaxWidth()) {
                    content()
                }
            }
        } else {
            content()
        }
    }
}
