package io.github.adaptivekt.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Uncontrolled wrapper for [AdaptiveSelect].
 */
@Composable
public fun <T> AdaptiveSelect(
    options: List<T>,
    selectedOption: T?,
    onSelectedOptionChange: (T?) -> Unit,
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
    footerContent: (@Composable () -> Unit)? = null,
    optionKey: ((T) -> Any)? = null,
    optionEnabled: (T) -> Boolean = { true },
) {
    var expanded by remember(initialExpanded) { mutableStateOf(initialExpanded) }
    
    AdaptiveSelect(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        options = options,
        selectedOption = selectedOption,
        onSelectedOptionChange = onSelectedOptionChange,
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
        optionContent = optionContent,
        selectedContent = selectedContent,
        emptyContent = emptyContent,
        footerContent = footerContent,
        optionKey = optionKey,
        optionEnabled = optionEnabled,
    )
}

/**
 * A controlled single-selection dropdown component.
 */
@Composable
public fun <T> AdaptiveSelect(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<T>,
    selectedOption: T?,
    onSelectedOptionChange: (T?) -> Unit,
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
    optionContent: (@Composable (option: T, selected: Boolean) -> Unit)? = null,
    selectedContent: (@Composable (option: T) -> Unit)? = null,
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
        if (searchable && effectiveSearchQuery.isNotEmpty() && onSearchQueryChange == null) {
            // Only filter locally if onSearchQueryChange is null (internal search)
            options.filter { selectMatchesQuery(optionLabel(it), effectiveSearchQuery) }
        } else {
            options
        }
    }
    
    val keySelector: (T) -> Any = optionKey ?: { it as Any }
    
    // Validate keys in debug/dev ideally, but we'll do it on composition for safety
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

    // Reset state on collapse
    LaunchedEffect(expanded) {
        if (!expanded) {
            setEffectiveSearchQuery("")
            // Clear highlight so it recalculates on next open
            navState = navState.copy(highlightedKey = null)
        } else {
            // When opening, initialize highlight to selected or first enabled
            val initialHighlight = if (selectedOption != null) {
                keySelector(selectedOption)
            } else {
                visibleOptions.firstOrNull(optionEnabled)?.let(keySelector)
            }
            navState = navState.copy(highlightedKey = initialHighlight)
        }
    }

    // Auto-scroll to highlighted item
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
                                onSelectedOptionChange(optionToSelect)
                                onExpandedChange(false)
                                focusRequester.requestFocus()
                            }
                        }
                    },
                    isSearchFocused = false // For now assume search is not focused natively because we aren't using a focusable input field yet
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
                ) { _, _, showClearArg, chevronTint ->
                    val showClearLocal = clearable && selectedOption != null && enabled
                    
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

                    if (showClearLocal) {
                        AdaptiveIconButton(
                            onClick = {
                                onSelectedOptionChange(null)
                                onExpandedChange(false)
                            },
                            size = 28.dp,
                        ) {
                            AdaptiveIcons.Close(
                                size = 16.dp,
                                tint = AdaptiveComponentDefaults.MutedText,
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
            val isSelected = selectedOption != null && isOptionSame(option, selectedOption, optionKey)
            val isHighlighted = navState.highlightedKey == keySelector(option)
            val isEnabled = optionEnabled(option)
            
            AdaptiveOptionRow(
                text = optionLabel(option),
                selected = isSelected,
                highlighted = isHighlighted,
                enabled = isEnabled,
                onClick = {
                    if (isEnabled) {
                        onSelectedOptionChange(option)
                        onExpandedChange(false)
                        focusRequester.requestFocus()
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
