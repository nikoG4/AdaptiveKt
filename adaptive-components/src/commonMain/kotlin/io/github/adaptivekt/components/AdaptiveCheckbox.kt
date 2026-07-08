package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.semantics.disabled
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
    val minTouchTarget = 48.dp
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
            .defaultMinSize(minWidth = minTouchTarget, minHeight = minTouchTarget)
            .then(
                if (onClick != null) {
                    Modifier
                        .adaptiveInteractiveCursor(enabled = enabled)
                        .triStateToggleable(
                            state = state,
                            onClick = onClick,
                            enabled = enabled,
                            role = Role.Checkbox
                        )
                } else Modifier
            )
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
                if (onClick == null) {
                    this.role = Role.Checkbox
                    this.toggleableState = state
                    if (!enabled) disabled()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .background(backgroundColor)
                .border(1.dp, borderColor, shape),
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
}
