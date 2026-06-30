package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.adaptiveInteractiveCursor

@Composable
public fun AdaptiveCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
) {
    val size = 18.dp
    val shape = AdaptiveTheme.shapes.small
    val backgroundColor = when {
        !enabled -> AdaptiveTheme.colors.surfaceMuted
        state != ToggleableState.Off -> AdaptiveTheme.colors.primary
        else -> AdaptiveTheme.colors.surface
    }
    val borderColor = when {
        !enabled -> AdaptiveTheme.colors.border
        state != ToggleableState.Off -> AdaptiveTheme.colors.primary
        else -> AdaptiveTheme.colors.borderStrong
    }
    
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .semantics {
                this.toggleableState = state
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            }
            .then(
                if (enabled && onClick != null) {
                    Modifier
                        .adaptiveInteractiveCursor()
                        .clickable(role = Role.Checkbox) {
                            onClick()
                        }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (state == ToggleableState.On) {
            AdaptiveIcons.Check(size = 14.dp, tint = AdaptiveTheme.colors.textInverse)
        } else if (state == ToggleableState.Indeterminate) {
            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 2.dp)
                    .background(AdaptiveTheme.colors.textInverse)
            )
        }
    }
}
