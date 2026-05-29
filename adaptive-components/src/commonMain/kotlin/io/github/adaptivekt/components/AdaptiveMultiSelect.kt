package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens

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

/**
 * Multi-selection dropdown with local search and removable selected chips.
 *
 * Internally uses [AdaptiveAnchoredDropdownMenu] and keeps the dropdown open while options are toggled.
 */
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
) {
    var expanded by remember(initialExpanded) { mutableStateOf(initialExpanded) }
    var searchQuery by remember { mutableStateOf("") }

    val shape = AdaptiveComponentDefaults.MediumShape
    val borderColor = when {
        isError -> Color(0xFFFCA5A5)
        expanded -> AdaptiveComponentDefaults.Primary
        else -> AdaptiveComponentDefaults.BorderStrong
    }
    val bgColor = if (enabled) AdaptiveComponentDefaults.Surface else AdaptiveComponentDefaults.DisabledSurface

    if (!expanded && searchQuery.isNotEmpty()) {
        searchQuery = ""
    }

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

        AdaptiveAnchoredDropdownMenu(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = it },
            enabled = enabled,
            matchAnchorWidth = true,
            maxHeight = maxMenuHeight,
            anchor = { _, toggle ->
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
                    onRemove = { option ->
                        onSelectedOptionsChange(selectedOptions.filterNot { it == option })
                    },
                    onClearAll = {
                        onSelectedOptionsChange(emptyList())
                        expanded = false
                    },
                    onClick = toggle,
                )
            },
        ) {
            if (searchable) {
                AdaptiveSearchField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search...",
                    onClear = { searchQuery = "" },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
            }

            val visibleOptions = if (searchable) {
                filterMultiSelectOptions(options, searchQuery, optionLabel)
            } else {
                options
            }

            if (visibleOptions.isEmpty()) {
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
            } else {
                visibleOptions.forEach { option ->
                    val isSelected = selectedOptions.any { it == option }
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
                            onClick = { onSelectedOptionsChange(nextSelection()) },
                        ) {
                            optionContent(option, isSelected)
                        }
                    } else {
                        MultiSelectMenuItem(
                            text = optionLabel(option),
                            selected = isSelected,
                            onClick = { onSelectedOptionsChange(nextSelection()) },
                        )
                    }
                }
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
    onRemove: (T) -> Unit,
    onClearAll: () -> Unit,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val actualBg = if (hovered && enabled && !expanded) AdaptiveComponentDefaults.SurfaceSubtle else bgColor
    val showClear = clearable && selectedOptions.isNotEmpty() && enabled
    val visibleChips = visibleMultiSelectChips(selectedOptions, maxVisibleChips)
    val hiddenCount = hiddenMultiSelectChipCount(selectedOptions.size, maxVisibleChips)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = AdaptiveTokens.Sizes.ButtonHeight)
            .clip(shape)
            .background(actualBg, shape)
            .border(1.dp, borderColor, shape)
            .hoverable(interactionSource)
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
                                        tint = Color.White,
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
    onClick: () -> Unit,
) {
    if (!selected) {
        AdaptiveMenuItem(text = text, onClick = onClick)
        return
    }

    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    val background = when {
        selected -> AdaptiveComponentDefaults.PrimarySubtle
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
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = AdaptiveComponentDefaults.MediumShape
    val background = when {
        selected -> AdaptiveComponentDefaults.PrimarySubtle
        hovered -> AdaptiveComponentDefaults.SurfaceSubtle
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background, shape)
            .hoverable(interactionSource)
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
