package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveElevationLevel
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveVisualStyle
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
    val shape = if (AdaptiveTheme.visualStyle == AdaptiveVisualStyle.Modern) {
        AdaptiveComponentDefaults.LargeShape
    } else {
        AdaptiveComponentDefaults.MediumShape
    }
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val pressed by interactionSource.collectIsPressedAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val elevationTokens = AdaptiveTheme.elevations
    val baseElevation = elevationTokens.resolve(elevation)
    val targetElevation = if (onClick != null && (hovered || focused) && !pressed) {
        baseElevation + elevationTokens.low
    } else {
        baseElevation
    }
    val currentElevation by animateDpAsState(
        targetValue = targetElevation,
        animationSpec = tween(durationMillis = AdaptiveTheme.motion.durationMillis(AdaptiveTheme.motion.fastMillis)),
        label = "Card elevation",
    )
    val surfaceColor = if (elevation == AdaptiveElevationLevel.None) {
        AdaptiveComponentDefaults.Surface
    } else {
        AdaptiveTheme.colors.surfaceRaised
    }
    val clickableModifier = if (onClick != null) {
        Modifier
            .hoverable(interactionSource = interactionSource)
            .adaptiveInteractiveCursor()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AdaptiveTokens.Sizes.CardMinHeight)
            .shadow(
                elevation = currentElevation,
                shape = shape,
                clip = false,
                ambientColor = elevationTokens.shadowColor,
                spotColor = elevationTokens.shadowColor,
            )
            .clip(shape)
            .background(surfaceColor, shape)
            .border(
                if (focused && onClick != null) AdaptiveTheme.states.focusBorderWidth else 1.dp,
                if (focused && onClick != null) AdaptiveTheme.colors.focusRing else AdaptiveComponentDefaults.Border,
                shape,
            )
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
