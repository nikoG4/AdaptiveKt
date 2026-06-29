package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor

public fun <T> filterMultiSelectOptions(
    options: List<T>,
    query: String,
    optionLabel: (T) -> String,
): List<T> = if (query.isEmpty()) {
    options
} else {
    options.filter { selectMatchesQuery(optionLabel(it), query) }
}

public fun <T> visibleMultiSelectChips(
    selectedOptions: List<T>,
    maxVisibleChips: Int,
): List<T> = selectedOptions.take(maxVisibleChips.coerceAtLeast(0))

public fun hiddenMultiSelectChipCount(
    selectedCount: Int,
    maxVisibleChips: Int,
): Int = (selectedCount - maxVisibleChips.coerceAtLeast(0)).coerceAtLeast(0)

@Composable
public fun <T> AdaptiveMultiSelect(
    options: List<T>,
    selectedOptions: List<T>,
    onSelectedOptionsChange: (List<T>) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select options",
    enabled: Boolean = true,
    searchable: Boolean = true,
    clearable: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    maxMenuHeight: Dp = 320.dp,
    maxVisibleChips: Int = 3,
    initialExpanded: Boolean = false,
    optionContent: (@Composable (option: T, selected: Boolean) -> Unit)? = null,
    chipContent: (@Composable (option: T) -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    optionKey: ((T) -> Any)? = null,
) {
    var expanded by remember(initialExpanded) { mutableStateOf(initialExpanded) }
    var searchQuery by remember { mutableStateOf("") }

    val shape = AdaptiveComponentDefaults.MediumShape
    val borderColor = when {
        isError -> AdaptiveComponentDefaults.Danger
        expanded -> AdaptiveComponentDefaults.Primary
        else -> AdaptiveComponentDefaults.BorderStrong
    }
    val bgColor = if (enabled) AdaptiveComponentDefaults.Surface else AdaptiveComponentDefaults.DisabledSurface

    if (!expanded && searchQuery.isNotEmpty()) {
        searchQuery = ""
    }

    val visibleOptions = remember(options, searchable, searchQuery) {
        if (searchable) {
            filterMultiSelectOptions(options, searchQuery, optionLabel)
        } else {
            options
        }
    }
    
    val keySelector: (T) -> Any = optionKey ?: { it as Any }
    
    var navState by remember(visibleOptions, expanded) { 
        mutableStateOf(AdaptiveOptionNavigationState<Any>())
    }

    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            BasicText(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AdaptiveComponentDefaults.Text,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
        }

        AdaptiveAnchoredMenuBox(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = visibleOptions,
            itemKey = optionKey,
            policy = AdaptiveAnchoredMenuPolicy(
                matchAnchorWidth = true,
                maxHeight = maxMenuHeight
            ),
            modifier = Modifier.onPreviewKeyEvent { event ->
                if (!expanded) {
                    if (event.key == Key.DirectionDown || event.key == Key.Enter || event.key == Key.Spacebar) {
                        expanded = true
                        return@onPreviewKeyEvent true
                    }
                    return@onPreviewKeyEvent false
                }
                
                when (event.key) {
                    Key.DirectionDown -> {
                        navState = resolveOptionNavigation(navState, visibleOptions, keySelector, OptionNavigationOperation.Next)
                        true
                    }
                    Key.DirectionUp -> {
                        navState = resolveOptionNavigation(navState, visibleOptions, keySelector, OptionNavigationOperation.Previous)
                        true
                    }
                    Key.Enter -> {
                        val highlighted = navState.highlightedKey
                        if (highlighted != null) {
                            val optionToSelect = visibleOptions.find { keySelector(it) == highlighted }
                            if (optionToSelect != null) {
                                val isSelected = selectedOptions.any { it == optionToSelect }
                                val nextSelection = if (isSelected) {
                                    selectedOptions.filterNot { it == optionToSelect }
                                } else {
                                    selectedOptions + optionToSelect
                                }
                                onSelectedOptionsChange(nextSelection)
                                // We don't close expanded on enter for multi-select
                            }
                        }
                        true
                    }
                    Key.Escape -> {
                        expanded = false
                        focusRequester.requestFocus()
                        true
                    }
                    else -> false
                }
            },
            headerContent = if (searchable) {
                {
                    AdaptiveSearchField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search...",
                        onClear = { searchQuery = "" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = AdaptiveTokens.Spacing.XSmall)
                    )
                }
            } else null,
            emptyContent = {
                if (emptyContent != null) {
                    emptyContent()
                } else {
                    BasicText(
                        text = if (searchQuery.isNotEmpty()) "No results for \"$searchQuery\"" else "No options available",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = AdaptiveComponentDefaults.MutedText,
                        ),
                        modifier = Modifier.padding(
                            horizontal = AdaptiveTokens.Spacing.Medium,
                            vertical = AdaptiveTokens.Spacing.Small,
                        ),
                    )
                }
            },
            anchor = {
                MultiSelectTrigger(
                    selectedOptions = selectedOptions,
                    optionLabel = optionLabel,
                    placeholder = placeholder,
                    enabled = enabled,
                    expanded = expanded,
                    clearable = clearable,
                    bgColor = bgColor,
                    borderColor = borderColor,
                    shape = shape,
                    maxVisibleChips = maxVisibleChips,
                    chipContent = chipContent,
                    focusRequester = focusRequester,
                    onRemove = { option ->
                        onSelectedOptionsChange(selectedOptions.filterNot { it == option })
                    },
                    onClearAll = {
                        onSelectedOptionsChange(emptyList())
                        expanded = false
                    },
                    onClick = { if (enabled) expanded = !expanded },
                )
            },
        ) { _, option ->
            val isSelected = selectedOptions.any { it == option }
            val isHighlighted = navState.highlightedKey == keySelector(option)
            val nextSelection = {
                if (isSelected) {
                    selectedOptions.filterNot { it == option }
                } else {
                    selectedOptions + option
                }
            }

            if (optionContent != null) {
                MultiSelectOptionWrapper(
                    selected = isSelected,
                    highlighted = isHighlighted,
                    onClick = { onSelectedOptionsChange(nextSelection()) },
                ) {
                    optionContent(option, isSelected)
                }
            } else {
                MultiSelectMenuItem(
                    text = optionLabel(option),
                    selected = isSelected,
                    highlighted = isHighlighted,
                    onClick = { onSelectedOptionsChange(nextSelection()) },
                )
            }
        }

        if (!supportingText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
            BasicText(
                text = supportingText,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = if (isError) AdaptiveComponentDefaults.Danger else AdaptiveComponentDefaults.MutedText,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun <T> MultiSelectTrigger(
    selectedOptions: List<T>,
    optionLabel: (T) -> String,
    placeholder: String,
    enabled: Boolean,
    expanded: Boolean,
    clearable: Boolean,
    bgColor: Color,
    borderColor: Color,
    shape: androidx.compose.ui.graphics.Shape,
    maxVisibleChips: Int,
    chipContent: (@Composable (T) -> Unit)?,
    focusRequester: FocusRequester,
    onRemove: (T) -> Unit,
    onClearAll: () -> Unit,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    
    val actualBg = if (hovered && enabled && !expanded) AdaptiveComponentDefaults.SurfaceSubtle else bgColor
    val actualBorder = if (focused) AdaptiveComponentDefaults.Primary else borderColor
    val showClear = clearable && selectedOptions.isNotEmpty() && enabled
    val visibleChips = visibleMultiSelectChips(selectedOptions, maxVisibleChips)
    val hiddenCount = hiddenMultiSelectChipCount(selectedOptions.size, maxVisibleChips)

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
        if (selectedOptions.isEmpty()) {
            BasicText(
                text = placeholder,
                style = TextStyle(fontSize = 14.sp, color = AdaptiveComponentDefaults.MutedText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        } else {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                visibleChips.forEach { option ->
                    if (chipContent != null) {
                        MultiSelectCustomChip(enabled = enabled, onRemove = { onRemove(option) }) {
                            chipContent(option)
                        }
                    } else {
                        AdaptiveChip(
                            text = optionLabel(option),
                            enabled = enabled,
                            selected = true,
                            tone = AdaptiveChipTone.Primary,
                            trailingIcon = if (enabled) {
                                {
                                    AdaptiveIcons.Close(
                                        size = 12.dp,
                                        tint = AdaptiveTheme.colors.textInverse,
                                        contentDescription = "Remove ${optionLabel(option)}",
                                    )
                                }
                            } else {
                                null
                            },
                            onClick = if (enabled) {
                                { onRemove(option) }
                            } else {
                                null
                            },
                        )
                    }
                }
                if (hiddenCount > 0) {
                    AdaptiveChip(text = "+$hiddenCount", enabled = enabled, tone = AdaptiveChipTone.Neutral)
                }
            }
        }

        Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))

        if (showClear) {
            AdaptiveIconButton(
                onClick = onClearAll,
                size = 28.dp,
            ) {
                AdaptiveIcons.Close(
                    size = 16.dp,
                    tint = AdaptiveComponentDefaults.MutedText,
                    contentDescription = "Clear selection",
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        AdaptiveIcons.ChevronDown(
            size = 16.dp,
            tint = if (expanded) AdaptiveComponentDefaults.Primary else if (enabled) AdaptiveComponentDefaults.MutedText else AdaptiveComponentDefaults.DisabledText,
        )
    }
}

@Composable
private fun MultiSelectCustomChip(
    enabled: Boolean,
    onRemove: () -> Unit,
    content: @Composable () -> Unit,
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(AdaptiveTokens.Radius.Pill)
    Row(
        modifier = Modifier
            .heightIn(min = 32.dp)
            .clip(shape)
            .background(if (enabled) AdaptiveComponentDefaults.PrimarySubtle else AdaptiveComponentDefaults.DisabledSurface, shape)
            .border(1.dp, if (enabled) AdaptiveComponentDefaults.Primary else AdaptiveComponentDefaults.Border, shape)
            .adaptiveInteractiveCursor(enabled)
            .clickable(enabled = enabled, onClick = onRemove)
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
        if (enabled) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.XSmall))
            AdaptiveIcons.Close(size = 12.dp, tint = AdaptiveComponentDefaults.Primary)
        }
    }
}

@Composable
private fun MultiSelectMenuItem(
    text: String,
    selected: Boolean,
    highlighted: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    val background = when {
        selected -> AdaptiveComponentDefaults.PrimarySubtle
        highlighted -> AdaptiveComponentDefaults.SurfaceSubtle
        hovered -> AdaptiveComponentDefaults.SurfaceSubtle
        else -> Color.Transparent
    }
    val textColor = if (selected) AdaptiveComponentDefaults.Primary else AdaptiveComponentDefaults.Text
    val fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background, shape)
            .hoverable(interactionSource)
            .adaptiveInteractiveCursor()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
        if (selected) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            AdaptiveIcons.Check(size = 14.dp, tint = AdaptiveComponentDefaults.Primary)
        }
    }
}

@Composable
private fun MultiSelectOptionWrapper(
    selected: Boolean,
    highlighted: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    val background = when {
        selected -> AdaptiveComponentDefaults.PrimarySubtle
        highlighted -> AdaptiveComponentDefaults.SurfaceSubtle
        hovered -> AdaptiveComponentDefaults.SurfaceSubtle
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background, shape)
            .hoverable(interactionSource)
            .adaptiveInteractiveCursor()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.XSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        content()
        if (selected) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            AdaptiveIcons.Check(size = 14.dp, tint = AdaptiveComponentDefaults.Primary)
        }
    }
}
