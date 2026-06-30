package io.github.adaptivekt.data

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
        rowKey = { (it ?: Unit) as Any },
        selectionMode = AdaptiveDataSelectionMode.None,
        selectionState = AdaptiveDataSelectionState(),
        onSelectionStateChange = {},
        modifier = modifier,
        rowEnabled = { true },
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
                        val visibleKeys = state.items.map(rowKey)
                        if (selectionMode != AdaptiveDataSelectionMode.None) {
                            validateAdaptiveRowKeys(visibleKeys)
                        }
                        val disabledKeys = state.items.asSequence().filterNot(rowEnabled).map(rowKey).toSet()

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
) {
    val includeSelection = selectionMode != AdaptiveDataSelectionMode.None
    val visibleSelectable = visibleKeys.filter { it !in disabledKeys }
    val visibleColumns = visibleColumnsForBreakpoint(columns, breakpoint)
    val displayedColumns = if (visibleColumns.isEmpty()) columns else visibleColumns
    val includeActions = rowActions.isNotEmpty()

    AdaptiveCard(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
    ) {
        AdaptiveDataTableRow(
            columns = displayedColumns,
            isHeader = true,
            includeActions = includeActions,
        )

        items.forEach { item ->
            DataDivider()
            AdaptiveDataTableRow(
                columns = displayedColumns,
                isHeader = false,
                item = item,
                onItemClick = onItemClick,
                rowActions = rowActions,
                includeActions = includeActions,
            )
        }
    }
}

@Composable
private fun <T> AdaptiveDataTableRow(
    columns: List<AdaptiveDataColumn<T>>,
    isHeader: Boolean,
    includeActions: Boolean,
    item: T? = null,
    onItemClick: ((T) -> Unit)? = null,
    rowActions: List<AdaptiveDataAction<T>> = emptyList(),
) {
    val rowBackground = if (isHeader) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.surface
    val rowInteractionSource = remember { MutableInteractionSource() }
    val rowModifier = Modifier
        .fillMaxWidth()
        .heightIn(min = AdaptiveTokens.Sizes.TableRowMinHeight)
        .background(rowBackground)
        .then(
            if (!isHeader && onItemClick != null && item != null) {
                Modifier
                    .adaptiveInteractiveCursor()
                    .clickable(
                        interactionSource = rowInteractionSource,
                        indication = null,
                    ) {
                        onItemClick(item)
                    }
            } else {
                Modifier
            },
        )
        .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small)

    val weights = columns.map { normalizeColumnWeight(it.weight) } +
        if (includeActions) listOf(1.6f) else emptyList()

    WeightedDataRow(weights = weights, modifier = rowModifier) {
        columns.forEachIndexed { index, column ->
            DataCellBox {
                if (isHeader) {
                    HeaderText(column.header)
                } else if (item != null) {
                    if (inferredMobileRole(index, column) == AdaptiveDataMobileRole.Status) {
                        DefaultStatusBadge { column.cell(item) }
                    } else {
                        column.cell(item)
                    }
                }
            }
        }

        if (includeActions) {
            DataCellBox {
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
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        if (measurables.isEmpty()) {
            layout(width = constraints.minWidth, height = constraints.minHeight) {}
        } else {
            val normalizedWeights = weights.ifEmpty { List(measurables.size) { 1f } }
            val totalWeight = normalizedWeights.sum().takeIf { it > 0f } ?: measurables.size.toFloat()
            val availableWidth = constraints.maxWidth.coerceAtLeast(constraints.minWidth)
            val widths = measurables.indices.map { index ->
                val weight = normalizedWeights.getOrElse(index) { 1f }
                if (index == measurables.lastIndex) {
                    val used = measurables.indices.take(index).sumOf { usedIndex ->
                        (availableWidth * normalizedWeights.getOrElse(usedIndex) { 1f } / totalWeight).toInt()
                    }
                    (availableWidth - used).coerceAtLeast(0)
                } else {
                    (availableWidth * weight / totalWeight).toInt().coerceAtLeast(0)
                }
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

            layout(width = availableWidth, height = height) {
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
                val isAllSelected = visibleSelectable.isNotEmpty() && visibleSelectable.all { it in selectionState.selectedKeys }
                val isSomeSelected = visibleSelectable.any { it in selectionState.selectedKeys }
                
                io.github.adaptivekt.components.AdaptiveCheckbox(
                    state = when {
                        isAllSelected -> androidx.compose.ui.state.ToggleableState.On
                        isSomeSelected -> androidx.compose.ui.state.ToggleableState.Indeterminate
                        else -> androidx.compose.ui.state.ToggleableState.Off
                    },
                    onClick = {
                        val op: AdaptiveDataSelectionOperation<K> = if (isAllSelected) {
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
            val key = rowKey(item)
            val isSelected = selectionState.selectedKeys.contains(key)
            val isDisabled = disabledKeys.contains(key)
            
            AdaptiveCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
                onClick = if (!isDisabled && (onItemClick != null || includeSelection)) {
                    { 
                        if (includeSelection) {
                            val modifiers = windowInfo.keyboardModifiers
                            val shiftPressed = modifiers.isShiftPressed
                            val ctrlPressed = modifiers.isCtrlPressed
                            val metaPressed = modifiers.isMetaPressed
                            
                            val operation = resolveAdaptiveRowSelectionOperation(
                                key = key,
                                selected = isSelected,
                                shiftPressed = shiftPressed,
                                togglePressed = ctrlPressed || metaPressed,
                                source = AdaptiveRowSelectionSource.Row
                            )
                            onSelectionStateChange(resolveAdaptiveDataSelection(selectionState, operation, selectionMode, visibleKeys, disabledKeys))
                        }
                        if (onItemClick != null) onItemClick(item)
                    }
                } else null,
            ) {
                if (cardContent != null) {
                    Row(verticalAlignment = Alignment.Top) {
                        if (includeSelection) {
                            io.github.adaptivekt.components.AdaptiveCheckbox(
                                state = if (isSelected) androidx.compose.ui.state.ToggleableState.On else androidx.compose.ui.state.ToggleableState.Off,
                                enabled = !isDisabled,
                                onClick = {
                                    val modifiers = windowInfo.keyboardModifiers
                                    val shiftPressed = modifiers.isShiftPressed
                                    val ctrlPressed = modifiers.isCtrlPressed
                                    val metaPressed = modifiers.isMetaPressed
                                    val op = resolveAdaptiveRowSelectionOperation(
                                        key = key,
                                        selected = isSelected,
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
                                enabled = !isDisabled,
                                onClick = {
                                    val modifiers = windowInfo.keyboardModifiers
                                    val shiftPressed = modifiers.isShiftPressed
                                    val ctrlPressed = modifiers.isCtrlPressed
                                    val metaPressed = modifiers.isMetaPressed
                                    val op = resolveAdaptiveRowSelectionOperation(
                                        key = key,
                                        selected = isSelected,
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
