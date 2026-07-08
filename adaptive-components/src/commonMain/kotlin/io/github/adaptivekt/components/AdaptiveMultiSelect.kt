package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
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

/**
 * Uncontrolled wrapper for [AdaptiveMultiSelect].
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
    footerContent: (@Composable () -> Unit)? = null,
    optionKey: ((T) -> Any)? = null,
    optionEnabled: (T) -> Boolean = { true },
) {
    var expanded by remember(initialExpanded) { mutableStateOf(initialExpanded) }

    AdaptiveMultiSelect(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        options = options,
        selectedOptions = selectedOptions,
        onSelectedOptionsChange = onSelectedOptionsChange,
        optionLabel = optionLabel,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        searchable = searchable,
        clearable = clearable,
        isError = isError,
        supportingText = supportingText,
        maxMenuHeight = maxMenuHeight,
        maxVisibleChips = maxVisibleChips,
        optionContent = optionContent,
        chipContent = chipContent,
        emptyContent = emptyContent,
        footerContent = footerContent,
        optionKey = optionKey,
        optionEnabled = optionEnabled,
    )
}

/**
 * A controlled multi-selection dropdown component.
 */
@Composable
public fun <T> AdaptiveMultiSelect(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
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
    optionContent: (@Composable (option: T, selected: Boolean) -> Unit)? = null,
    chipContent: (@Composable (option: T) -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    optionKey: ((T) -> Any)? = null,
    optionEnabled: (T) -> Boolean = { true },
    searchQuery: String? = null,
    onSearchQueryChange: ((String) -> Unit)? = null,
) {
    var internalSearchQuery by remember { mutableStateOf("") }
    val effectiveSearchQuery = searchQuery ?: internalSearchQuery
    val setEffectiveSearchQuery: (String) -> Unit = { 
        if (onSearchQueryChange != null) onSearchQueryChange(it) 
        else internalSearchQuery = it 
    }
    
    val lazyListState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    val visibleOptions = remember(options, searchable, effectiveSearchQuery) {
        if (searchable && onSearchQueryChange == null) {
            filterMultiSelectOptions(options, effectiveSearchQuery, optionLabel)
        } else {
            options
        }
    }
    
    val keySelector: (T) -> Any = optionKey ?: { it as Any }
    
    remember(visibleOptions) {
        validateOptionKeys(visibleOptions, optionKey)
    }

    val disabledKeys = remember(visibleOptions, optionEnabled) {
        visibleOptions.filterNot(optionEnabled).map(keySelector).toSet()
    }
    
    var navState by remember(visibleOptions) { 
        mutableStateOf(
            AdaptiveOptionNavigationState<Any>(
                disabledKeys = disabledKeys
            )
        ) 
    }

    LaunchedEffect(expanded) {
        if (!expanded) {
            setEffectiveSearchQuery("")
            navState = navState.copy(highlightedKey = null)
        } else {
            val initialHighlight = visibleOptions.firstOrNull(optionEnabled)?.let(keySelector)
            navState = navState.copy(highlightedKey = initialHighlight)
        }
    }

    LaunchedEffect(navState.highlightedKey, expanded) {
        val highlighted = navState.highlightedKey
        if (expanded && highlighted != null) {
            val index = visibleOptions.indexOfFirst { keySelector(it) == highlighted }
            if (index >= 0) {
                lazyListState.animateScrollToItem(index)
            }
        }
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

        AdaptiveAnchoredMenuBox(
            expanded = expanded,
            onDismissRequest = { 
                onExpandedChange(false)
                focusRequester.requestFocus()
            },
            items = visibleOptions,
            itemKey = optionKey,
            lazyListState = lazyListState,
            policy = AdaptiveAnchoredMenuPolicy(
                matchAnchorWidth = true,
                maxHeight = maxMenuHeight
            ),
            modifier = Modifier.onPreviewKeyEvent { event ->
                resolveAdaptiveSelectKeyboardNavigation(
                    event = event,
                    isExpanded = expanded,
                    onExpand = { onExpandedChange(true) },
                    onCollapse = { 
                        onExpandedChange(false)
                        focusRequester.requestFocus() 
                    },
                    onNavigateNext = {
                        navState = resolveOptionNavigation(navState, visibleOptions, keySelector, OptionNavigationOperation.Next)
                    },
                    onNavigatePrevious = {
                        navState = resolveOptionNavigation(navState, visibleOptions, keySelector, OptionNavigationOperation.Previous)
                    },
                    onNavigateFirst = {
                        navState = resolveOptionNavigation(navState, visibleOptions, keySelector, OptionNavigationOperation.First)
                    },
                    onNavigateLast = {
                        navState = resolveOptionNavigation(navState, visibleOptions, keySelector, OptionNavigationOperation.Last)
                    },
                    onSelectHighlighted = {
                        val highlighted = navState.highlightedKey
                        if (highlighted != null) {
                            val optionToSelect = visibleOptions.find { keySelector(it) == highlighted }
                            if (optionToSelect != null && optionEnabled(optionToSelect)) {
                                onSelectedOptionsChange(toggleOptionByKey(selectedOptions, optionToSelect, optionKey))
                                // Multi-select does NOT collapse on enter by default
                            }
                        }
                    },
                    isSearchFocused = false
                )
            },
            headerContent = if (searchable) {
                {
                    AdaptiveSearchField(
                        value = effectiveSearchQuery,
                        onValueChange = setEffectiveSearchQuery,
                        placeholder = "Search...",
                        onClear = { setEffectiveSearchQuery("") },
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
                        text = if (effectiveSearchQuery.isNotEmpty()) "No results for \"$effectiveSearchQuery\"" else "No options available",
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
            footerContent = footerContent,
            anchor = {
                AdaptiveSelectTriggerFrame(
                    enabled = enabled,
                    expanded = expanded,
                    isError = isError,
                    focusRequester = focusRequester,
                    onClick = { if (enabled) onExpandedChange(!expanded) },
                ) { _, _, _, chevronTint ->
                    val showClearLocal = clearable && selectedOptions.isNotEmpty() && enabled
                    val visibleChips = visibleMultiSelectChips(selectedOptions, maxVisibleChips)
                    val hiddenCount = hiddenMultiSelectChipCount(selectedOptions.size, maxVisibleChips)

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
                                    MultiSelectCustomChip(enabled = enabled, onRemove = { 
                                        onSelectedOptionsChange(toggleOptionByKey(selectedOptions, option, optionKey)) 
                                    }) {
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
                                            { onSelectedOptionsChange(toggleOptionByKey(selectedOptions, option, optionKey)) }
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

                    if (showClearLocal) {
                        AdaptiveIconButton(
                            onClick = {
                                onSelectedOptionsChange(emptyList())
                                onExpandedChange(false)
                                focusRequester.requestFocus()
                            },
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
                        tint = chevronTint,
                    )
                }
            },
        ) { _, option ->
            val isSelected = selectedOptions.any { isOptionSame(it, option, optionKey) }
            val isHighlighted = navState.highlightedKey == keySelector(option)
            val isEnabled = optionEnabled(option)
            
            AdaptiveOptionRow(
                text = optionLabel(option),
                selected = isSelected,
                highlighted = isHighlighted,
                enabled = isEnabled,
                onClick = {
                    if (isEnabled) {
                        onSelectedOptionsChange(toggleOptionByKey(selectedOptions, option, optionKey))
                    }
                },
                customContent = if (optionContent != null) {
                    { optionContent(option, isSelected) }
                } else null
            )
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
