package io.github.adaptivekt.data

import androidx.compose.ui.draw.alpha

import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.isMetaPressed
import androidx.compose.ui.input.pointer.isShiftPressed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveMenuItem
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveBreakpoint
import io.github.adaptivekt.core.AdaptiveContent
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.adaptiveInteractiveCursor
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.feedback.AdaptiveErrorState
import io.github.adaptivekt.feedback.AdaptiveLoadingState

/**
 * Displays adaptive data using a responsive table or card layout depending on the current breakpoint.
 *
 * @param T The type of each row item.
 * @param state Adaptive loading/content/error/empty state.
 * @param columns Column definitions for table layout.
 * @param modifier Optional modifier for the root container.
 * @param filterSlot Optional composable slot for filters displayed above the data.
 * @param actions Optional composable slot for actions displayed above the data.
 * @param rowActions Optional actions available for each row.
 * @param onItemClick Optional click handler for row/card items.
 * @param cardContent Optional composable rendering a single item in card mode. When null, a default
 * mobile card is generated from column metadata and heuristics.
 * @param displayMode Explicit display mode. [AdaptiveDataDisplayMode.Auto] preserves the original
 * responsive table-to-card behavior.
 * @param autoSwitchToCards When false, [AdaptiveDataDisplayMode.Auto] stays in table mode even on
 * compact breakpoints.
 */
@Composable
public fun <T> AdaptiveDataView(
    state: AdaptiveDataState<T>,
    columns: List<AdaptiveDataColumn<T>>,
    modifier: Modifier = Modifier,
    filterSlot: AdaptiveFilterSlot? = null,
    actions: (@Composable () -> Unit)? = null,
    rowActions: List<AdaptiveDataAction<T>> = emptyList(),
    onItemClick: ((T) -> Unit)? = null,
    cardContent: (@Composable (T) -> Unit)? = null,
    displayMode: AdaptiveDataDisplayMode = AdaptiveDataDisplayMode.Auto,
    autoSwitchToCards: Boolean = true,
    queryState: AdaptiveQueryState? = null,
    onQueryChange: ((AdaptiveQueryState) -> Unit)? = null,
    pagination: AdaptivePaginationState? = null,
    searchEnabled: Boolean = false,
    filters: List<AdaptiveFilterOption> = emptyList(),
    sortOptions: List<AdaptiveSortOption> = emptyList(),
) {
    AdaptiveDataView<T, Any>(
        state = state,
        columns = columns,
        rowKey = { throw IllegalStateException("rowKey should not be evaluated when selectionMode is None") },
        selectionMode = AdaptiveDataSelectionMode.None,
        selectionState = AdaptiveDataSelectionState(),
        onSelectionStateChange = {},
        modifier = modifier,
        rowEnabled = { true },
        rowClickBehavior = AdaptiveDataRowClickBehavior.Activate,
        filterSlot = filterSlot,
        actions = actions,
        rowActions = rowActions,
        onItemClick = onItemClick,
        cardContent = cardContent,
        displayMode = displayMode,
        autoSwitchToCards = autoSwitchToCards,
        queryState = queryState,
        onQueryChange = onQueryChange,
        pagination = pagination,
        searchEnabled = searchEnabled,
        filters = filters,
        sortOptions = sortOptions,
    )
}

@Composable
public fun <T, K : Any> AdaptiveDataView(
    state: AdaptiveDataState<T>,
    columns: List<AdaptiveDataColumn<T>>,
    rowKey: (T) -> K,
    selectionMode: AdaptiveDataSelectionMode,
    selectionState: AdaptiveDataSelectionState<K>,
    onSelectionStateChange: (AdaptiveDataSelectionState<K>) -> Unit,
    modifier: Modifier = Modifier,
    rowEnabled: (T) -> Boolean = { true },
    rowClickBehavior: AdaptiveDataRowClickBehavior = if (selectionMode == AdaptiveDataSelectionMode.None) AdaptiveDataRowClickBehavior.Activate else AdaptiveDataRowClickBehavior.Select,
    filterSlot: AdaptiveFilterSlot? = null,
    actions: (@Composable () -> Unit)? = null,
    rowActions: List<AdaptiveDataAction<T>> = emptyList(),
    onItemClick: ((T) -> Unit)? = null,
    cardContent: (@Composable (T) -> Unit)? = null,
    displayMode: AdaptiveDataDisplayMode = AdaptiveDataDisplayMode.Auto,
    autoSwitchToCards: Boolean = true,
    queryState: AdaptiveQueryState? = null,
    onQueryChange: ((AdaptiveQueryState) -> Unit)? = null,
    pagination: AdaptivePaginationState? = null,
    searchEnabled: Boolean = false,
    filters: List<AdaptiveFilterOption> = emptyList(),
    sortOptions: List<AdaptiveSortOption> = emptyList(),
) {
    AdaptiveContent(modifier = modifier) {
        val adaptiveInfo = rememberAdaptiveInfo()
        val resolvedDisplayMode = resolveAdaptiveDataDisplayMode(
            breakpoint = adaptiveInfo.breakpoint,
            displayMode = displayMode,
            autoSwitchToCards = autoSwitchToCards,
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            if (queryState != null && onQueryChange != null) {
                AdaptiveDataQueryControls(
                    queryState = queryState,
                    onQueryChange = onQueryChange,
                    searchEnabled = searchEnabled,
                    filters = filters,
                    sortOptions = sortOptions,
                    pagination = pagination,
                )
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
            }

            if (filterSlot != null || actions != null) {
                DataToolbar(filterSlot = filterSlot, actions = actions)
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
            }

            when (state) {
                is AdaptiveDataLoading -> AdaptiveLoadingState(message = "Loading data")
                is AdaptiveDataError -> AdaptiveErrorState(
                    title = state.title,
                    description = state.description,
                )
                is AdaptiveDataEmpty -> AdaptiveEmptyState(
                    title = state.title,
                    description = state.description,
                )
                is AdaptiveDataContent -> {
                    if (state.items.isEmpty()) {
                        AdaptiveEmptyState(
                            title = "No data available",
                            description = "Try adjusting filters or create a new record.",
                        )
                    } else {
                        val visibleKeys = if (selectionMode != AdaptiveDataSelectionMode.None) state.items.map(rowKey) else emptyList()
                        if (selectionMode != AdaptiveDataSelectionMode.None) {
                            validateAdaptiveRowKeys(visibleKeys)
                        }
                        val disabledKeys = if (selectionMode != AdaptiveDataSelectionMode.None) state.items.asSequence().filterNot(rowEnabled).map(rowKey).toSet() else emptySet()

                        if (resolvedDisplayMode == AdaptiveDataDisplayMode.Table) {
                            AdaptiveDataTable(
                                items = state.items,
                                rowKey = rowKey,
                                columns = columns,
                                breakpoint = adaptiveInfo.breakpoint,
                                onItemClick = onItemClick,
                                rowActions = rowActions,
                                selectionMode = selectionMode,
                                selectionState = selectionState,
                                onSelectionStateChange = onSelectionStateChange,
                                visibleKeys = visibleKeys,
                                disabledKeys = disabledKeys,
                                rowEnabled = rowEnabled,
                                rowClickBehavior = rowClickBehavior,
                            )
                        } else {
                            AdaptiveDataCards(
                                items = state.items,
                                rowKey = rowKey,
                                columns = columns,
                                onItemClick = onItemClick,
                                rowActions = rowActions,
                                cardContent = cardContent,
                                selectionMode = selectionMode,
                                selectionState = selectionState,
                                onSelectionStateChange = onSelectionStateChange,
                                visibleKeys = visibleKeys,
                                disabledKeys = disabledKeys,
                                rowEnabled = rowEnabled,
                                rowClickBehavior = rowClickBehavior,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DataToolbar(
    filterSlot: AdaptiveFilterSlot?,
    actions: (@Composable () -> Unit)?,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        filterSlot?.invoke()

        if (filterSlot != null && actions != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        }

        actions?.invoke()
    }
}

@Composable
private fun <T, K : Any> AdaptiveDataTable(
    items: List<T>,
    rowKey: (T) -> K,
    columns: List<AdaptiveDataColumn<T>>,
    breakpoint: AdaptiveBreakpoint,
    onItemClick: ((T) -> Unit)?,
    rowActions: List<AdaptiveDataAction<T>>,
    selectionMode: AdaptiveDataSelectionMode,
    selectionState: AdaptiveDataSelectionState<K>,
    onSelectionStateChange: (AdaptiveDataSelectionState<K>) -> Unit,
    visibleKeys: List<K>,
    disabledKeys: Set<K>,
    rowEnabled: (T) -> Boolean,
    rowClickBehavior: AdaptiveDataRowClickBehavior,
) {
    val includeSelection = selectionMode != AdaptiveDataSelectionMode.None
    val visibleSelectable = visibleKeys.filter { it !in disabledKeys }
    val visibleColumns = visibleColumnsForBreakpoint(columns, breakpoint)
    val displayedColumns = if (visibleColumns.isEmpty()) columns else visibleColumns
    val includeActions = rowActions.isNotEmpty()
    val windowInfo = androidx.compose.ui.platform.LocalWindowInfo.current

    AdaptiveCard(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
    ) {
        // Header
        val headerSelectAllState = if (selectionMode == AdaptiveDataSelectionMode.Multiple) {
            resolveAdaptiveSelectAllState(
                selectedKeys = selectionState.selectedKeys,
                visibleKeys = visibleKeys,
                disabledKeys = disabledKeys,
                mode = selectionMode
            )
        } else {
            androidx.compose.ui.state.ToggleableState.Off
        }

        val onHeaderSelectionClick: (() -> Unit)? = if (selectionMode == AdaptiveDataSelectionMode.Multiple && headerSelectAllState != io.github.adaptivekt.data.AdaptiveSelectAllState.Disabled) {
            {
                val op = if (headerSelectAllState == io.github.adaptivekt.data.AdaptiveSelectAllState.Checked) {
                    AdaptiveDataSelectionOperation.ClearVisible
                } else {
                    AdaptiveDataSelectionOperation.SelectAllVisible
                }
                onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
            }
        } else null

        val mappedHeaderState = when (headerSelectAllState) {
            io.github.adaptivekt.data.AdaptiveSelectAllState.Checked -> androidx.compose.ui.state.ToggleableState.On
            io.github.adaptivekt.data.AdaptiveSelectAllState.Indeterminate -> androidx.compose.ui.state.ToggleableState.Indeterminate
            io.github.adaptivekt.data.AdaptiveSelectAllState.Unchecked, io.github.adaptivekt.data.AdaptiveSelectAllState.Disabled -> androidx.compose.ui.state.ToggleableState.Off
            else -> androidx.compose.ui.state.ToggleableState.Off
        }

        AdaptiveDataTableRow(
            columns = displayedColumns,
            isHeader = true,
            includeActions = includeActions,
            includeSelection = includeSelection,
            selectionCheckboxState = mappedHeaderState,
            selectionCheckboxEnabled = headerSelectAllState != io.github.adaptivekt.data.AdaptiveSelectAllState.Disabled,
            onSelectionClick = onHeaderSelectionClick,
        )

        items.forEach { item ->
            DataDivider()
            val key = if (includeSelection) rowKey(item) else null
            val isSelected = if (key != null) key in selectionState.selectedKeys else false
            val isEnabled = rowEnabled(item)
            
            val onSelectionClick: (() -> Unit)? = if (includeSelection && isEnabled && key != null) {
                {
                    val modifiers = windowInfo.keyboardModifiers
                    val op = resolveAdaptiveRowSelectionOperation(
                        key = key,
                        shiftPressed = modifiers.isShiftPressed,
                        togglePressed = modifiers.isCtrlPressed || modifiers.isMetaPressed,
                        source = AdaptiveRowSelectionSource.Checkbox
                    )
                    onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
                }
            } else null

            val onRowSelectionClick: (() -> Unit)? = if (includeSelection && isEnabled && key != null && (rowClickBehavior == AdaptiveDataRowClickBehavior.Select || rowClickBehavior == AdaptiveDataRowClickBehavior.SelectAndActivate)) {
                {
                    val modifiers = windowInfo.keyboardModifiers
                    val op = resolveAdaptiveRowSelectionOperation(
                        key = key,
                        shiftPressed = modifiers.isShiftPressed,
                        togglePressed = modifiers.isCtrlPressed || modifiers.isMetaPressed,
                        source = AdaptiveRowSelectionSource.Row
                    )
                    onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
                }
            } else null

            val mappedRowState = if (isSelected) androidx.compose.ui.state.ToggleableState.On else androidx.compose.ui.state.ToggleableState.Off

            AdaptiveDataTableRow(
                columns = displayedColumns,
                isHeader = false,
                item = item,
                onItemClick = if (isEnabled && (rowClickBehavior == AdaptiveDataRowClickBehavior.Activate || rowClickBehavior == AdaptiveDataRowClickBehavior.SelectAndActivate)) onItemClick else null,
                rowActions = rowActions,
                includeActions = includeActions,
                includeSelection = includeSelection,
                selectionCheckboxState = mappedRowState,
                selectionCheckboxEnabled = isEnabled,
                onSelectionClick = onSelectionClick,
                onRowSelectionClick = onRowSelectionClick,
                isSelected = isSelected,
                isEnabled = isEnabled,
            )
        }
    }
}

@Composable
private fun <T> AdaptiveDataTableRow(
    columns: List<AdaptiveDataColumn<T>>,
    isHeader: Boolean,
    includeActions: Boolean,
    includeSelection: Boolean = false,
    selectionCheckboxState: androidx.compose.ui.state.ToggleableState = androidx.compose.ui.state.ToggleableState.Off,
    selectionCheckboxEnabled: Boolean = true,
    onSelectionClick: (() -> Unit)? = null,
    onRowSelectionClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isEnabled: Boolean = true,
    item: T? = null,
    onItemClick: ((T) -> Unit)? = null,
    rowActions: List<AdaptiveDataAction<T>> = emptyList(),
) {
    val baseBackground = if (isHeader) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.surface
    val rowBackground = if (!isHeader && isSelected) AdaptiveTheme.colors.primarySubtle else baseBackground
    val rowInteractionSource = remember { MutableInteractionSource() }
    
    val canClickRow = !isHeader && isEnabled && (onItemClick != null || onRowSelectionClick != null)

    val rowModifier = Modifier
        .fillMaxWidth()
        .heightIn(min = AdaptiveTokens.Sizes.TableRowMinHeight)
        .background(rowBackground)
        .then(
            if (canClickRow) {
                Modifier
                    .adaptiveInteractiveCursor()
                    .clickable(
                        interactionSource = rowInteractionSource,
                        indication = null,
                    ) {
                        if (onRowSelectionClick != null) onRowSelectionClick()
                        if (onItemClick != null && item != null) onItemClick(item)
                    }
            } else {
                Modifier
            },
        )
        .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small)
        .then(
            if (!isHeader && !isEnabled) Modifier.background(AdaptiveTheme.colors.surfaceMuted.copy(alpha = 0.5f)) else Modifier
        )

    val weights = columns.map { normalizeColumnWeight(it.weight) }
    
    WeightedDataRow(
        weights = weights, 
        modifier = rowModifier,
        leadingWidth = if (includeSelection) 48.dp else null,
        trailingWidth = if (includeActions) 80.dp else null
    ) {
        if (includeSelection) {
            Box(
                modifier = Modifier.width(48.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (isHeader || selectionCheckboxState != androidx.compose.ui.state.ToggleableState.Off || onSelectionClick != null) {
                    io.github.adaptivekt.components.AdaptiveCheckbox(
                        state = selectionCheckboxState,
                        onClick = onSelectionClick,
                        enabled = selectionCheckboxEnabled
                    )
                }
            }
        }

        columns.forEachIndexed { index, column ->
            DataCellBox {
                if (isHeader) {
                    HeaderText(column.header)
                } else if (item != null) {
                    val contentAlpha = if (isEnabled) 1f else 0.5f
                    androidx.compose.ui.Modifier.alpha(contentAlpha) // Not applying directly, just placeholder, ideally the content uses LocalContentColor but here we let the row handle it or we can just render. Actually we'll wrap in Box with alpha if disabled.
                    Box(modifier = if (!isEnabled) Modifier.background(Color.Transparent).then(Modifier) else Modifier) { // Using graphicsLayer alpha is better, but this is multiplatform. We just rely on the row background.
                        if (inferredMobileRole(index, column) == AdaptiveDataMobileRole.Status) {
                            DefaultStatusBadge { column.cell(item) }
                        } else {
                            column.cell(item)
                        }
                    }
                }
            }
        }

        if (includeActions) {
            Box(
                modifier = Modifier.width(80.dp).padding(start = AdaptiveTokens.Spacing.Small),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (isHeader) {
                    HeaderText("Actions")
                } else if (item != null) {
                    DesktopActions(rowActions = rowActions, item = item)
                }
            }
        }
    }
}

@Composable
private fun WeightedDataRow(
    weights: List<Float>,
    modifier: Modifier = Modifier,
    leadingWidth: androidx.compose.ui.unit.Dp? = null,
    trailingWidth: androidx.compose.ui.unit.Dp? = null,
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        if (measurables.isEmpty()) {
            layout(width = constraints.minWidth, height = constraints.minHeight) {}
        } else {
            val density = this
            val leadingPx = leadingWidth?.roundToPx() ?: 0
            val trailingPx = trailingWidth?.roundToPx() ?: 0
            
            val totalFixedPx = leadingPx + trailingPx
            val availableWidthForWeights = (constraints.maxWidth.coerceAtLeast(constraints.minWidth) - totalFixedPx).coerceAtLeast(0)
            
            val normalizedWeights = weights.ifEmpty { List(measurables.size) { 1f } }
            val totalWeight = normalizedWeights.sum().takeIf { it > 0f } ?: measurables.size.toFloat()
            
            var currentMeasurableIndex = 0
            val widths = mutableListOf<Int>()
            
            if (leadingWidth != null) {
                widths.add(leadingPx)
                currentMeasurableIndex++
            }
            
            val weightCount = weights.size
            for (i in 0 until weightCount) {
                val weight = normalizedWeights[i]
                if (i == weightCount - 1) {
                    val used = (0 until i).sumOf { usedIndex ->
                        (availableWidthForWeights * normalizedWeights[usedIndex] / totalWeight).toInt()
                    }
                    widths.add((availableWidthForWeights - used).coerceAtLeast(0))
                } else {
                    widths.add((availableWidthForWeights * weight / totalWeight).toInt().coerceAtLeast(0))
                }
                currentMeasurableIndex++
            }
            
            if (trailingWidth != null) {
                widths.add(trailingPx)
            }

            val placeables = measurables.mapIndexed { index, measurable ->
                val width = widths.getOrElse(index) { 0 }
                measurable.measure(
                    Constraints(
                        minWidth = width,
                        maxWidth = width,
                        minHeight = 0,
                        maxHeight = constraints.maxHeight,
                    ),
                )
            }

            val height = placeables.maxOfOrNull { it.height }?.coerceAtLeast(constraints.minHeight)
                ?: constraints.minHeight

            layout(width = constraints.maxWidth, height = height) {
                var x = 0
                placeables.forEach { placeable ->
                    placeable.placeRelative(x = x, y = (height - placeable.height) / 2)
                    x += placeable.width
                }
            }
        }
    }
}

@Composable
private fun DataCellBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = AdaptiveTokens.Spacing.Small),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}

@Composable
private fun HeaderText(text: String) {
    BasicText(
        text = text,
        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = AdaptiveTheme.colors.textSecondary,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun DataDivider() {
    AdaptiveDivider()
}

@Composable
private fun <T, K : Any> AdaptiveDataCards(
    items: List<T>,
    rowKey: (T) -> K,
    columns: List<AdaptiveDataColumn<T>>,
    onItemClick: ((T) -> Unit)?,
    rowActions: List<AdaptiveDataAction<T>>,
    cardContent: (@Composable (T) -> Unit)? = null,
    selectionMode: AdaptiveDataSelectionMode,
    selectionState: AdaptiveDataSelectionState<K>,
    onSelectionStateChange: (AdaptiveDataSelectionState<K>) -> Unit,
    visibleKeys: List<K>,
    disabledKeys: Set<K>,
    rowEnabled: (T) -> Boolean,
    rowClickBehavior: AdaptiveDataRowClickBehavior,
) {
    val includeSelection = selectionMode != AdaptiveDataSelectionMode.None
    val visibleSelectable = visibleKeys.filter { it !in disabledKeys }
    val windowInfo = androidx.compose.ui.platform.LocalWindowInfo.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
    ) {
        if (includeSelection && selectionMode == AdaptiveDataSelectionMode.Multiple) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val headerSelectAllState = resolveAdaptiveSelectAllState(
                    selectedKeys = selectionState.selectedKeys,
                    visibleKeys = visibleKeys,
                    disabledKeys = disabledKeys,
                    mode = selectionMode
                )
                
                val isSomeSelected = headerSelectAllState == io.github.adaptivekt.data.AdaptiveSelectAllState.Indeterminate || headerSelectAllState == io.github.adaptivekt.data.AdaptiveSelectAllState.Checked
                
                io.github.adaptivekt.components.AdaptiveCheckbox(
                    state = when (headerSelectAllState) {
                        io.github.adaptivekt.data.AdaptiveSelectAllState.Checked -> androidx.compose.ui.state.ToggleableState.On
                        io.github.adaptivekt.data.AdaptiveSelectAllState.Indeterminate -> androidx.compose.ui.state.ToggleableState.Indeterminate
                        io.github.adaptivekt.data.AdaptiveSelectAllState.Unchecked, io.github.adaptivekt.data.AdaptiveSelectAllState.Disabled -> androidx.compose.ui.state.ToggleableState.Off
                    },
                    enabled = headerSelectAllState != io.github.adaptivekt.data.AdaptiveSelectAllState.Disabled,
                    onClick = {
                        val op = if (headerSelectAllState == io.github.adaptivekt.data.AdaptiveSelectAllState.Checked) {
                            AdaptiveDataSelectionOperation.ClearVisible
                        } else {
                            AdaptiveDataSelectionOperation.SelectAllVisible
                        }
                        onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
                    }
                )
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Medium))
                BasicText(
                    text = if (isSomeSelected) " selected" else "Select all",
                    style = TextStyle(fontSize = 14.sp, color = AdaptiveTheme.colors.textMuted)
                )
            }
        }
    
        items.forEach { item ->
            val key = if (includeSelection) rowKey(item) else null
            val isSelected = if (key != null) key in selectionState.selectedKeys else false
            val isEnabled = rowEnabled(item)
            
            val canClickCard = isEnabled && (onItemClick != null || includeSelection)
            
            AdaptiveCard(
                modifier = Modifier.fillMaxWidth().then(
                    if (!isEnabled) Modifier.alpha(0.5f) else Modifier
                ),
                contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
                onClick = if (canClickCard) {
                    { 
                        if (includeSelection && key != null && (rowClickBehavior == AdaptiveDataRowClickBehavior.Select || rowClickBehavior == AdaptiveDataRowClickBehavior.SelectAndActivate)) {
                            val modifiers = windowInfo.keyboardModifiers
                            val operation = resolveAdaptiveRowSelectionOperation(
                                key = key,
                                shiftPressed = modifiers.isShiftPressed,
                                togglePressed = modifiers.isCtrlPressed || modifiers.isMetaPressed,
                                source = AdaptiveRowSelectionSource.Row
                            )
                            onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, operation, selectionMode, visibleKeys, disabledKeys))
                        }
                        if (isEnabled && (rowClickBehavior == AdaptiveDataRowClickBehavior.Activate || rowClickBehavior == AdaptiveDataRowClickBehavior.SelectAndActivate) && onItemClick != null) {
                            onItemClick(item)
                        }
                    }
                } else null,
            ) {
                if (cardContent != null) {
                    Row(verticalAlignment = Alignment.Top) {
                        if (includeSelection) {
                            io.github.adaptivekt.components.AdaptiveCheckbox(
                                state = if (isSelected) androidx.compose.ui.state.ToggleableState.On else androidx.compose.ui.state.ToggleableState.Off,
                                enabled = isEnabled,
                                onClick = {
                                    val modifiers = windowInfo.keyboardModifiers
                                    val shiftPressed = modifiers.isShiftPressed
                                    val ctrlPressed = modifiers.isCtrlPressed
                                    val metaPressed = modifiers.isMetaPressed
                                    val op = resolveAdaptiveRowSelectionOperation(
                                        key = key!!,
                                        shiftPressed = shiftPressed,
                                        togglePressed = ctrlPressed || metaPressed,
                                        source = AdaptiveRowSelectionSource.Checkbox
                                    )
                                    onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
                                },
                                modifier = Modifier.padding(top = 4.dp, end = AdaptiveTokens.Spacing.Medium)
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            cardContent(item)
                        }
                    }
                } else {
                    val resolvedCols = resolveMobileColumns(columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        if (includeSelection) {
                            io.github.adaptivekt.components.AdaptiveCheckbox(
                                state = if (isSelected) androidx.compose.ui.state.ToggleableState.On else androidx.compose.ui.state.ToggleableState.Off,
                                enabled = isEnabled,
                                onClick = {
                                    val modifiers = windowInfo.keyboardModifiers
                                    val shiftPressed = modifiers.isShiftPressed
                                    val ctrlPressed = modifiers.isCtrlPressed
                                    val metaPressed = modifiers.isMetaPressed
                                    val op = resolveAdaptiveRowSelectionOperation(
                                        key = key!!,
                                        shiftPressed = shiftPressed,
                                        togglePressed = ctrlPressed || metaPressed,
                                        source = AdaptiveRowSelectionSource.Checkbox
                                    )
                                    onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, op, selectionMode, visibleKeys, disabledKeys))
                                },
                                modifier = Modifier.padding(top = 4.dp, end = AdaptiveTokens.Spacing.Medium)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
                        ) {
                            val titleCol = resolvedCols.firstOrNull { it.role == AdaptiveDataMobileRole.Title }
                            val subtitleCol = resolvedCols.firstOrNull { it.role == AdaptiveDataMobileRole.Subtitle }
                            val statusCol = resolvedCols.firstOrNull { it.role == AdaptiveDataMobileRole.Status }

                            if (titleCol != null || subtitleCol != null || statusCol != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        if (titleCol != null) {
                                            Box(modifier = Modifier.heightIn(min = 20.dp)) {
                                                titleCol.column.cell(item)
                                            }
                                        }
                                        if (subtitleCol != null) {
                                            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
                                            Box(modifier = Modifier.heightIn(min = 16.dp)) {
                                                subtitleCol.column.cell(item)
                                            }
                                        }
                                    }
                                    if (statusCol != null) {
                                        Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Medium))
                                        DefaultStatusBadge {
                                            statusCol.column.cell(item)
                                        }
                                    }
                                }
                                AdaptiveDivider(modifier = Modifier.padding(vertical = AdaptiveTokens.Spacing.Small))
                            }

                            val metaCols = resolvedCols.filter { it.role == AdaptiveDataMobileRole.Metadata }
                            metaCols.forEach { col ->
                                MetadataRow(column = col.column, item = item)
                            }
                            
                            MobileActions(rowActions = rowActions, item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> MetadataRow(column: AdaptiveDataColumn<T>, item: T) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        BasicText(
            text = column.header,
            modifier = Modifier.padding(end = AdaptiveTokens.Spacing.Medium),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AdaptiveTheme.colors.textMuted,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Box(
            modifier = Modifier.widthIn(max = 220.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            column.cell(item)
        }
    }
}

@Composable
private fun <T> DesktopActions(
    rowActions: List<AdaptiveDataAction<T>>,
    item: T,
) {
    if (rowActions.isEmpty()) return

    Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
        rowActions
            .filter { it.priority != AdaptiveActionPriority.Overflow }
            .forEach { action ->
                DefaultActionButton(
                    text = action.label,
                    destructive = action.destructive,
                    style = DefaultButtonStyle.Secondary,
                ) {
                    action.onClick(item)
                }
            }

        val overflowActions = rowActions.filter { it.priority == AdaptiveActionPriority.Overflow }
        if (overflowActions.isNotEmpty()) {
            DefaultOverflowMenu(actions = overflowActions, item = item)
        }
    }
}

@Composable
private fun <T> MobileActions(
    rowActions: List<AdaptiveDataAction<T>>,
    item: T,
) {
    if (rowActions.isEmpty()) return

    val primaryAction = rowActions.firstOrNull { it.priority == AdaptiveActionPriority.Primary }
    val menuActions = rowActions.filter { it != primaryAction }

    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (primaryAction != null) {
            DefaultActionButton(
                text = primaryAction.label,
                destructive = primaryAction.destructive,
                style = DefaultButtonStyle.Primary,
            ) {
                primaryAction.onClick(item)
            }
        } else {
            Spacer(modifier = Modifier.size(AdaptiveTokens.Sizes.ButtonHeight))
        }

        if (menuActions.isNotEmpty()) {
            DefaultOverflowMenu(actions = menuActions, item = item)
        }
    }
}

@Composable
private fun DefaultStatusBadge(content: @Composable () -> Unit) {
    val shape = AdaptiveTheme.shapes.pill
    Box(
        modifier = Modifier
            .background(AdaptiveTheme.colors.infoSubtle, shape)
            .border(1.dp, AdaptiveTheme.colors.borderStrong, shape)
            .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.XSmall),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

private enum class DefaultButtonStyle {
    Primary,
    Secondary,
}

@Composable
private fun DefaultActionButton(
    text: String,
    destructive: Boolean,
    style: DefaultButtonStyle,
    onClick: () -> Unit,
) {
    AdaptiveButton(
        text = text,
        onClick = onClick,
        variant = when {
            destructive -> AdaptiveButtonVariant.Danger
            style == DefaultButtonStyle.Primary -> AdaptiveButtonVariant.Primary
            else -> AdaptiveButtonVariant.Secondary
        },
        size = AdaptiveButtonSize.Small,
    )
}

@Composable
private fun <T> DefaultOverflowMenu(actions: List<AdaptiveDataAction<T>>, item: T) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val menuWidth = 148.dp
    val popupOffset = IntOffset(
        x = 0,
        y = with(density) { (AdaptiveTokens.Sizes.ButtonHeight + AdaptiveTokens.Spacing.Small).roundToPx() },
    )

    Box {
        AdaptiveIconButton(
            onClick = { setExpanded(!expanded) },
            size = AdaptiveTokens.Sizes.ButtonHeight,
        ) {
            AdaptiveIcons.MoreVertical(size = 18.dp, tint = AdaptiveTheme.colors.primaryText)
        }
        if (expanded) {
            val shape = AdaptiveTheme.shapes.medium
            Popup(
                alignment = Alignment.TopEnd,
                offset = popupOffset,
                onDismissRequest = { setExpanded(false) },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .width(menuWidth)
                        .background(AdaptiveTheme.colors.surface, shape)
                        .border(1.dp, AdaptiveTheme.colors.border, shape)
                        .padding(AdaptiveTokens.Spacing.Small),
                ) {
                    actions.forEach { action ->
                        AdaptiveMenuItem(
                            text = action.label,
                            destructive = action.destructive,
                            onClick = {
                                    action.onClick(item)
                                    setExpanded(false)
                            },
                        )
                    }
                }
            }
        }
    }
}

internal data class ResolvedMobileColumn<T>(
    val column: AdaptiveDataColumn<T>,
    val role: AdaptiveDataMobileRole,
    val priority: Int,
)

internal fun <T> resolveMobileColumns(
    columns: List<AdaptiveDataColumn<T>>,
): List<ResolvedMobileColumn<T>> {
    return columns
        .mapIndexed { index, column ->
            val role = inferredMobileRole(index, column)
            ResolvedMobileColumn(
                column = column,
                role = role,
                priority = inferredMobilePriority(index = index, column = column, role = role),
            )
        }
        .filter { it.column.showInMobileCard && it.role != AdaptiveDataMobileRole.Hidden }
        .sortedWith(compareBy({ it.priority }, { it.column.header }))
}

internal fun <T> inferredMobileRole(
    index: Int,
    column: AdaptiveDataColumn<T>,
): AdaptiveDataMobileRole {
    if (!column.showInMobileCard) return AdaptiveDataMobileRole.Hidden

    val key = "${column.id} ${column.header}".lowercase()
    val looksLikeStatus = listOf("status", "state", "estado").any { key.contains(it) }
    val looksLikeMedia = listOf("avatar", "image", "photo", "thumb", "thumbnail", "logo", "media").any { key.contains(it) }

    if (column.mobileRole != AdaptiveDataMobileRole.Metadata) {
        return column.mobileRole
    }

    return when {
        looksLikeMedia -> AdaptiveDataMobileRole.Media
        looksLikeStatus -> AdaptiveDataMobileRole.Status
        index == 0 -> AdaptiveDataMobileRole.Title
        index == 1 -> AdaptiveDataMobileRole.Subtitle
        index in 2..4 -> AdaptiveDataMobileRole.Metadata
        else -> AdaptiveDataMobileRole.Hidden
    }
}

private fun <T> inferredMobilePriority(
    index: Int,
    column: AdaptiveDataColumn<T>,
    role: AdaptiveDataMobileRole,
): Int {
    if (column.mobilePriority != 100) return column.mobilePriority

    return when (role) {
        AdaptiveDataMobileRole.Title -> 0
        AdaptiveDataMobileRole.Subtitle -> 10
        AdaptiveDataMobileRole.Status -> 20
        AdaptiveDataMobileRole.Media -> 30
        AdaptiveDataMobileRole.Metadata -> 40 + index
        AdaptiveDataMobileRole.Actions -> 80
        AdaptiveDataMobileRole.Hidden -> 1000
    }
}
