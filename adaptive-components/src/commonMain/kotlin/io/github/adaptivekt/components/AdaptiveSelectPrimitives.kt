package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

public fun selectMatchesQuery(label: String, query: String): Boolean {
    if (query.isEmpty()) return true
    return label.contains(query, ignoreCase = true)
}

internal fun resolveAdaptiveSelectKeyboardNavigation(
    event: KeyEvent,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
    onNavigateFirst: () -> Unit,
    onNavigateLast: () -> Unit,
    onSelectHighlighted: () -> Unit,
    isSearchFocused: Boolean = false,
): Boolean {
    if (!isExpanded) {
        if (event.key == Key.DirectionDown || event.key == Key.Enter || event.key == Key.Spacebar) {
            onExpand()
            return true
        }
        return false
    }

    return when (event.key) {
        Key.DirectionDown -> {
            onNavigateNext()
            true
        }
        Key.DirectionUp -> {
            onNavigatePrevious()
            true
        }
        Key.MoveHome -> {
            onNavigateFirst()
            true
        }
        Key.MoveEnd -> {
            onNavigateLast()
            true
        }
        Key.Enter -> {
            onSelectHighlighted()
            true
        }
        Key.Spacebar -> {
            if (!isSearchFocused) {
                onSelectHighlighted()
                true
            } else {
                false
            }
        }
        Key.Escape -> {
            onCollapse()
            true
        }
        else -> false
    }
}

@Composable
internal fun AdaptiveSelectTriggerFrame(
    enabled: Boolean,
    expanded: Boolean,
    isError: Boolean,
    focusRequester: FocusRequester,
    onClick: () -> Unit,
    content: @Composable RowScope.(actualBg: Color, actualBorder: Color, showClear: Boolean, chevronTint: Color) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()

    val shape = AdaptiveComponentDefaults.MediumShape
    val baseBorderColor = when {
        isError -> AdaptiveComponentDefaults.Danger
        expanded -> AdaptiveComponentDefaults.Primary
        else -> AdaptiveComponentDefaults.BorderStrong
    }
    val bgColor = if (enabled) AdaptiveComponentDefaults.Surface else AdaptiveComponentDefaults.DisabledSurface

    val actualBg = if (hovered && enabled && !expanded) AdaptiveComponentDefaults.SurfaceSubtle else bgColor
    val actualBorder = if (focused) AdaptiveComponentDefaults.Primary else baseBorderColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = AdaptiveTokens.Sizes.ButtonHeight)
            .clip(shape)
            .background(actualBg, shape)
            .border(if (focused) 2.dp else 1.dp, actualBorder, shape)
            .hoverable(interactionSource)
            .focusable(enabled = enabled, interactionSource = interactionSource)
            .focusRequester(focusRequester)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val chevronTint = if (expanded) AdaptiveComponentDefaults.Primary else if (enabled) AdaptiveComponentDefaults.MutedText else AdaptiveComponentDefaults.DisabledText
        content(actualBg, actualBorder, enabled, chevronTint)
    }
}

@Composable
internal fun AdaptiveOptionRow(
    text: String,
    selected: Boolean,
    highlighted: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    customContent: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    
    val isEffectivelyHighlighted = highlighted || (hovered && enabled)
    
    val background = when {
        selected && enabled -> AdaptiveComponentDefaults.PrimarySubtle
        selected && !enabled -> AdaptiveComponentDefaults.SurfaceSubtle
        isEffectivelyHighlighted -> AdaptiveComponentDefaults.SurfaceSubtle
        else -> Color.Transparent
    }
    
    val textColor = when {
        !enabled -> AdaptiveComponentDefaults.DisabledText
        selected -> AdaptiveComponentDefaults.Primary
        else -> AdaptiveComponentDefaults.Text
    }
    val fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background, shape)
            .hoverable(interactionSource)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(
                horizontal = if (customContent != null) AdaptiveTokens.Spacing.Small else AdaptiveTokens.Spacing.Medium, 
                vertical = if (customContent != null) AdaptiveTokens.Spacing.XSmall else AdaptiveTokens.Spacing.Small
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (customContent != null) {
            customContent()
        } else {
            BasicText(
                text = text,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = fontWeight,
                    color = textColor,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
        
        if (selected) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            AdaptiveIcons.Check(
                size = 14.dp, 
                tint = if (enabled) AdaptiveComponentDefaults.Primary else AdaptiveComponentDefaults.DisabledText
            )
        }
    }
}
