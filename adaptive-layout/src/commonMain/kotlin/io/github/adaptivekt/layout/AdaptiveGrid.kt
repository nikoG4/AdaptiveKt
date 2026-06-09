package io.github.adaptivekt.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import io.github.adaptivekt.core.AdaptiveTokens

internal data class GridItem(val span: Int, val content: @Composable () -> Unit)

internal fun coerceSpan(span: Int, columns: Int): Int {
    if (span < 1) return 1
    if (span > columns) return columns
    return span
}

internal fun remainingColumns(row: List<GridItem>, columns: Int): Int {
    val totalSpan = row.sumOf { it.span }
    return (columns - totalSpan).coerceAtLeast(0)
}

internal fun groupGridItemsIntoRows(items: List<GridItem>, columns: Int): List<List<GridItem>> {
    val rows = mutableListOf<MutableList<GridItem>>()
    var currentRow = mutableListOf<GridItem>()
    var remaining = columns
    for (item in items) {
        val s = coerceSpan(item.span, columns)
        if (s > remaining) {
            // start new row
            if (currentRow.isNotEmpty()) rows.add(currentRow)
            currentRow = mutableListOf()
            remaining = columns
        }
        currentRow.add(item.copy(span = s))
        remaining -= s
        if (remaining == 0) {
            rows.add(currentRow)
            currentRow = mutableListOf()
            remaining = columns
        }
    }
    if (currentRow.isNotEmpty()) rows.add(currentRow)
    return rows
}

interface AdaptiveGridScope {
    fun item(span: Int = 12, content: @Composable () -> Unit)
}

@Composable
fun AdaptiveGrid(
    modifier: Modifier = Modifier,
    columns: Int? = null, // null means use AdaptiveLayoutInfo
    horizontalGap: Dp = AdaptiveTokens.Spacing.Medium,
    verticalGap: Dp = AdaptiveTokens.Spacing.Medium,
    content: AdaptiveGridScope.() -> Unit,
) {
    val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current
    val effectiveColumns = columns ?: layoutInfo.columns
    val items = mutableListOf<GridItem>()

    val scope = object : AdaptiveGridScope {
        override fun item(span: Int, content: @Composable () -> Unit) {
            items.add(GridItem(span = coerceSpan(span, effectiveColumns), content = content))
        }
    }

    scope.content()

    val rows = groupGridItemsIntoRows(items, effectiveColumns)

    Column(modifier = modifier.fillMaxWidth()) {
        rows.forEachIndexed { rowIndex, row ->
            AdaptiveGridRow(
                modifier = Modifier.fillMaxWidth(),
                row = row,
                columns = effectiveColumns,
                horizontalGap = horizontalGap,
            )

            if (rowIndex < rows.lastIndex) {
                Spacer(modifier = Modifier.height(verticalGap))
            }
        }
    }
}

@Composable
private fun AdaptiveGridRow(
    row: List<GridItem>,
    columns: Int,
    horizontalGap: Dp,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            row.forEach { gridItem ->
                Box {
                    gridItem.content()
                }
            }
        },
    ) { measurables, constraints ->
        if (measurables.isEmpty()) {
            layout(width = constraints.minWidth, height = constraints.minHeight) {}
        } else {
            val gapPx = horizontalGap.roundToPx()
            val totalGap = gapPx * (measurables.size - 1)
            val availableWidth = (constraints.maxWidth - totalGap).coerceAtLeast(0)
            val widths = row.map { gridItem ->
                (availableWidth * gridItem.span / columns).coerceAtLeast(0)
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
            val layoutWidth = constraints.maxWidth.coerceAtLeast(constraints.minWidth)

            layout(width = layoutWidth, height = height) {
                var x = 0
                placeables.forEach { placeable ->
                    placeable.placeRelative(x = x, y = 0)
                    x += placeable.width + gapPx
                }
            }
        }
    }
}
