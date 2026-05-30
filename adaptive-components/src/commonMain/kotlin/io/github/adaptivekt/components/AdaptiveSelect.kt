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
import io.github.adaptivekt.components.AdaptiveAnchoredDropdownMenu
import io.github.adaptivekt.components.AdaptiveComponentDefaults
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Returns true if [label] contains [query] (case-insensitive). Empty [query] always matches.
 */
public fun selectMatchesQuery(label: String, query: String): Boolean {
    if (query.isEmpty()) return true
    return label.contains(query, ignoreCase = true)
}

/**
 * A single-selection dropdown component styled like [AdaptiveTextField].
 *
 * Internally uses [AdaptiveAnchoredDropdownMenu] for popup positioning.
 *
 * @param options The list of options to display.
 * @param selectedOption The currently selected option, or null for no selection.
 * @param onOptionSelected Called when an option is selected (null when cleared).
 * @param optionLabel Returns the display label for an option.
 * @param modifier Optional modifier.
 * @param label Optional label above the select field.
 * @param placeholder Placeholder text when nothing is selected.
 * @param enabled Whether the select is interactive.
 * @param searchable If true, a search field appears inside the dropdown.
 * @param clearable If true, a clear button appears when an option is selected.
 * @param isError If true, the field shows an error border.
 * @param supportingText Optional supporting or error text below the field.
 * @param maxMenuHeight Maximum height of the dropdown menu.
 * @param initialExpanded Whether the dropdown starts open, useful for deterministic visual capture.
 * @param optionContent Optional custom composable for each option row.
 * @param selectedContent Optional custom composable for the selected value display.
 * @param emptyContent Optional composable shown when no options/search results are available.
 */
@Composable
public fun <T> AdaptiveSelect(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select an option",
    enabled: Boolean = true,
    searchable: Boolean = false,
    clearable: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    maxMenuHeight: Dp = 320.dp,
    initialExpanded: Boolean = false,
    optionContent: (@Composable (option: T, selected: Boolean) -> Unit)? = null,
    selectedContent: (@Composable (option: T) -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
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

    // When closed, reset search query
    if (!expanded && searchQuery.isNotEmpty()) {
        searchQuery = ""
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Label
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

        // The anchor trigger + dropdown wrapper
        AdaptiveAnchoredDropdownMenu(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = it },
            enabled = enabled,
            matchAnchorWidth = true,
            maxHeight = maxMenuHeight,
            anchor = { _, toggle ->
                SelectTrigger(
                    selectedOption = selectedOption,
                    optionLabel = optionLabel,
                    placeholder = placeholder,
                    enabled = enabled,
                    expanded = expanded,
                    clearable = clearable,
                    isError = isError,
                    bgColor = bgColor,
                    borderColor = borderColor,
                    shape = shape,
                    selectedContent = selectedContent,
                    onClear = {
                        onOptionSelected(null)
                        expanded = false
                    },
                    onClick = toggle,
                )
            },
        ) {
            // Search field at top of menu
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

            // Filtered options
            val visibleOptions = if (searchable && searchQuery.isNotEmpty()) {
                options.filter { selectMatchesQuery(optionLabel(it), searchQuery) }
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
                    val isSelected = option == selectedOption
                    if (optionContent != null) {
                        SelectOptionWrapper(
                            selected = isSelected,
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                                searchQuery = ""
                            },
                        ) {
                            optionContent(option, isSelected)
                        }
                    } else {
                        SelectMenuItem(
                            text = optionLabel(option),
                            selected = isSelected,
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                                searchQuery = ""
                            },
                        )
                    }
                }
            }
        }

        // Supporting / error text
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
private fun <T> SelectTrigger(
    selectedOption: T?,
    optionLabel: (T) -> String,
    placeholder: String,
    enabled: Boolean,
    expanded: Boolean,
    clearable: Boolean,
    isError: Boolean,
    bgColor: Color,
    borderColor: Color,
    shape: androidx.compose.ui.graphics.Shape,
    selectedContent: (@Composable (T) -> Unit)?,
    onClear: () -> Unit,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val actualBg = if (hovered && enabled && !expanded) AdaptiveComponentDefaults.SurfaceSubtle else bgColor
    val showClear = clearable && selectedOption != null && enabled

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
        // Selected value or placeholder
        if (selectedOption != null) {
            if (selectedContent != null) {
                selectedContent(selectedOption)
            } else {
                BasicText(
                    text = optionLabel(selectedOption),
                    style = TextStyle(fontSize = 14.sp, color = AdaptiveComponentDefaults.Text),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            BasicText(
                text = placeholder,
                style = TextStyle(fontSize = 14.sp, color = AdaptiveComponentDefaults.MutedText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))

        // Clear button
        if (showClear) {
            AdaptiveIconButton(
                onClick = onClear,
                size = 28.dp,
            ) {
                AdaptiveIcons.Close(
                    size = 16.dp,
                    tint = AdaptiveComponentDefaults.MutedText,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Chevron icon
        val chevronTint = if (enabled) AdaptiveComponentDefaults.MutedText else AdaptiveComponentDefaults.DisabledText
        if (expanded) {
            AdaptiveIcons.ChevronDown(
                size = 16.dp,
                tint = AdaptiveComponentDefaults.Primary,
            )
        } else {
            AdaptiveIcons.ChevronDown(
                size = 16.dp,
                tint = chevronTint,
            )
        }
    }
}

@Composable
private fun SelectMenuItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
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
private fun SelectOptionWrapper(
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
