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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import io.github.adaptivekt.core.AdaptiveTheme
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

/** A pure keyboard command that can be verified without a platform KeyEvent. */
internal enum class AdaptiveSelectKeyAction { Expand, Collapse, Next, Previous, First, Last, Select }

internal fun resolveAdaptiveSelectKeyAction(
    key: Key,
    isExpanded: Boolean,
    isSearchFocused: Boolean,
): AdaptiveSelectKeyAction? {
    if (!isExpanded) return when (key) {
        Key.DirectionDown, Key.Enter, Key.Spacebar -> AdaptiveSelectKeyAction.Expand
        else -> null
    }
    return when (key) {
        Key.Escape -> AdaptiveSelectKeyAction.Collapse
        Key.DirectionDown -> AdaptiveSelectKeyAction.Next
        Key.DirectionUp -> AdaptiveSelectKeyAction.Previous
        Key.MoveHome -> if (isSearchFocused) null else AdaptiveSelectKeyAction.First
        Key.MoveEnd -> if (isSearchFocused) null else AdaptiveSelectKeyAction.Last
        Key.Enter -> AdaptiveSelectKeyAction.Select
        Key.Spacebar -> if (isSearchFocused) null else AdaptiveSelectKeyAction.Select
        else -> null
    }
}

/** Shared local-vs-external query policy for Select and MultiSelect. */
internal fun <T> resolveVisibleSelectOptions(
    options: List<T>,
    query: String,
    searchable: Boolean,
    externallyFiltered: Boolean,
    optionLabel: (T) -> String,
): List<T> {
    if (!searchable || externallyFiltered || query.isEmpty()) return options
    return options.filter { selectMatchesQuery(optionLabel(it), query) }
}

internal fun isSelectCommandPress(type: KeyEventType): Boolean = type == KeyEventType.KeyDown

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
    if (!isSelectCommandPress(event.type)) return false
    return when (resolveAdaptiveSelectKeyAction(event.key, isExpanded, isSearchFocused)) {
        AdaptiveSelectKeyAction.Expand -> { onExpand(); true }
        AdaptiveSelectKeyAction.Collapse -> { onCollapse(); true }
        AdaptiveSelectKeyAction.Next -> { onNavigateNext(); true }
        AdaptiveSelectKeyAction.Previous -> { onNavigatePrevious(); true }
        AdaptiveSelectKeyAction.First -> { onNavigateFirst(); true }
        AdaptiveSelectKeyAction.Last -> { onNavigateLast(); true }
        AdaptiveSelectKeyAction.Select -> { onSelectHighlighted(); true }
        null -> false
    }
}

@Composable
internal fun AdaptiveSelectTriggerFrame(
    enabled: Boolean,
    expanded: Boolean,
    isError: Boolean,
    focusRequester: FocusRequester,
    onClick: () -> Unit,
    onPreviewKeyEvent: ((KeyEvent) -> Boolean)? = null,
    contentDescription: String? = null,
    content: @Composable RowScope.(actualBg: Color, actualBorder: Color, showClear: Boolean, chevronTint: Color) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()

    val shape = AdaptiveComponentDefaults.MediumShape
    val baseBorderColor = when {
        isError -> AdaptiveComponentDefaults.Danger
        expanded -> AdaptiveTheme.colors.focusRing
        else -> AdaptiveComponentDefaults.Border
    }
    val bgColor = if (enabled) AdaptiveComponentDefaults.Surface else AdaptiveComponentDefaults.DisabledSurface

    val actualBg = if (hovered && enabled && !expanded) AdaptiveComponentDefaults.SurfaceSubtle else bgColor
    val actualBorder = if (focused) AdaptiveTheme.colors.focusRing else baseBorderColor
    val transitionMs = AdaptiveTheme.motion.durationMillis(AdaptiveTheme.motion.fastMillis)
    val animatedBackground by animateColorAsState(
        actualBg, animationSpec = tween(durationMillis = transitionMs), label = "Select background",
    )
    val animatedBorder by animateColorAsState(
        actualBorder, animationSpec = tween(durationMillis = transitionMs), label = "Select border",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = AdaptiveTokens.Sizes.ButtonHeight)
            .clip(shape)
            .background(animatedBackground, shape)
            .border(if (focused) AdaptiveTheme.states.focusBorderWidth else 1.dp, animatedBorder, shape)
            .semantics {
                stateDescription = if (expanded) "Expanded" else "Collapsed"
                if (contentDescription != null) this.contentDescription = contentDescription
            }
            .hoverable(interactionSource, enabled = enabled)
            .focusRequester(focusRequester)
            .then(if (onPreviewKeyEvent != null) Modifier.onPreviewKeyEvent(onPreviewKeyEvent) else Modifier)
            .focusable(enabled = enabled, interactionSource = interactionSource)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
        horizontalArrangement = Arrangement.Start,
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
    multiSelect: Boolean = false,
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
            .semantics { this.selected = selected }
            .hoverable(interactionSource, enabled = enabled)
            .adaptiveInteractiveCursor(enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = if (multiSelect) Role.Checkbox else Role.RadioButton,
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
