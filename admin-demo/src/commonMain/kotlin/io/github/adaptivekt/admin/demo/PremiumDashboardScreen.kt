package io.github.adaptivekt.admin.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.admin.demo.ui.DemoBadge
import io.github.adaptivekt.admin.demo.ui.DemoStatusText
import io.github.adaptivekt.admin.demo.ui.DemoToggleChip
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDialog
import io.github.adaptivekt.components.AdaptiveSectionHeader
import io.github.adaptivekt.core.AdaptiveElevationLevel
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.layout.AdaptiveGrid

/**
 * Polished, responsive demo of actual AdaptiveKt components rather than a static
 * website mockup. All amounts and business events are explicit sample data.
 */
@Composable
internal fun PremiumDashboardScreen() {
    val info = rememberAdaptiveInfo()
    val compact = info.isCompact
    val metricSpan = premiumDashboardMetricSpan(info.isCompact, info.isMedium)
    var reportOpen by remember { mutableStateOf(false) }
    var trendRange by remember { mutableStateOf(PremiumTrendRange.Month) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = AdaptiveTokens.Spacing.XLarge)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                DashboardEyebrow("WORKSPACE / OVERVIEW")
                Spacer(modifier = Modifier.height(6.dp))
                AdaptiveSectionHeader(title = if (compact) "Overview" else "Good morning, Lina")
                Spacer(modifier = Modifier.height(5.dp))
                DashboardBody("A clearer view of what your workspace is doing today.", muted = true)
            }
            if (!compact) {
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Medium))
                AdaptiveButton(
                    text = "View report",
                    variant = AdaptiveButtonVariant.Secondary,
                    onClick = { reportOpen = true },
                )
            }
        }

        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        DashboardWelcomeBanner(
            compact = compact,
            onViewReport = { reportOpen = true },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))

        AdaptiveGrid(
            columns = 12,
            horizontalGap = AdaptiveTokens.Spacing.Medium,
            verticalGap = AdaptiveTokens.Spacing.Medium,
        ) {
            AdminDemoData.dashboardMetrics.forEachIndexed { index, metric ->
                item(span = metricSpan) {
                    PremiumMetricCard(metric, index)
                }
            }
        }

        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        AdaptiveGrid(
            columns = 12,
            horizontalGap = AdaptiveTokens.Spacing.Medium,
            verticalGap = AdaptiveTokens.Spacing.Medium,
        ) {
            item(span = if (compact) 12 else 7) {
                PremiumRevenuePanel(range = trendRange, onRangeChange = { trendRange = it })
            }
            item(span = if (compact) 12 else 5) {
                PremiumActivityPanel()
            }
        }

        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        AdaptiveGrid(
            columns = 12,
            horizontalGap = AdaptiveTokens.Spacing.Medium,
            verticalGap = AdaptiveTokens.Spacing.Medium,
        ) {
            item(span = if (compact) 12 else 7) { PremiumInvoicesPanel() }
            item(span = if (compact) 12 else 5) { PremiumInventoryPanel() }
        }
    }

    if (reportOpen) {
        AdaptiveDialog(
            onDismissRequest = { reportOpen = false },
            title = "Workspace report",
            confirmButton = {
                AdaptiveButton(text = "Done", onClick = { reportOpen = false })
            },
        ) {
            DashboardBody("Preview summary — sample data from the AdaptiveKt UI Kit.")
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
            AdminDemoData.dashboardMetrics.forEach { metric ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = AdaptiveTokens.Spacing.Small),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DashboardBody(metric.title, muted = true)
                    DashboardBody(metric.value, strong = true)
                }
            }
        }
    }
}

/** Four KPIs on expanded widths, two on medium screens and one on compact. */
internal fun premiumDashboardMetricSpan(isCompact: Boolean, isMedium: Boolean): Int = when {
    isCompact -> 12
    isMedium -> 6
    else -> 3
}

@Composable
private fun DashboardWelcomeBanner(compact: Boolean, onViewReport: () -> Unit) {
    val palette = AdaptiveTheme.colors
    val shape = AdaptiveTheme.shapes.large
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(palette.primarySubtle, palette.surfaceRaised),
                ),
                shape = shape,
            )
            .border(1.dp, palette.border, shape)
            .padding(AdaptiveTokens.Spacing.Large),
    ) {
        if (compact) {
            Column {
                AdaptiveBadge(text = "WORKSPACE HEALTH", tone = AdaptiveBadgeTone.Success)
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                DashboardHeading("Your workspace is in sync")
                Spacer(modifier = Modifier.height(6.dp))
                DashboardBody("Team, billing and catalog activity in one view.", muted = true)
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                AdaptiveButton(
                    text = "View report",
                    size = AdaptiveButtonSize.Small,
                    onClick = onViewReport,
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AdaptiveBadge(text = "WORKSPACE HEALTH", tone = AdaptiveBadgeTone.Success)
                    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
                    DashboardHeading("Your workspace is in sync")
                    Spacer(modifier = Modifier.height(6.dp))
                    DashboardBody("Team, billing and catalog activity in one place.", muted = true)
                }
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Large))
                Column(horizontalAlignment = Alignment.End) {
                    DashboardEyebrow("LAST 30 DAYS")
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicText(
                        text = "+12.8%",
                        style = AdaptiveTheme.typography.title.copy(
                            fontWeight = FontWeight.Bold,
                            color = palette.success,
                        ),
                    )
                    DashboardBody("Revenue growth", muted = true)
                }
            }
        }
    }
}

@Composable
private fun PremiumMetricCard(metric: DashboardMetric, index: Int) {
    val colors = AdaptiveTheme.colors
    val tint = when (index) {
        0 -> colors.primary
        1 -> colors.info
        2 -> colors.warning
        else -> colors.success
    }
    val indicator = when (index) {
        0 -> "+4 this month"
        1 -> "Catalog healthy"
        2 -> "Needs attention"
        else -> "+12.8% vs prior"
    }
    AdaptiveCard(
        modifier = Modifier.fillMaxWidth().heightIn(min = 148.dp),
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        elevation = AdaptiveElevationLevel.Low,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier.size(32.dp)
                    .clip(AdaptiveTheme.shapes.small)
                    .background(tint.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier.size(10.dp)
                        .clip(CircleShape)
                        .background(tint),
                )
            }
            DashboardEyebrow(if (index == 2) "REVIEW" else "ACTIVE")
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        BasicText(
            text = metric.value,
            style = AdaptiveTheme.typography.title.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        DashboardBody(metric.title, muted = true)
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        BasicText(
            text = indicator,
            style = AdaptiveTheme.typography.caption.copy(color = tint, fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

internal enum class PremiumTrendRange { Week, Month, Quarter }

internal data class PremiumTrendSeries(val labels: List<String>, val values: List<Int>) {
    init { require(labels.size == values.size && values.all { it >= 0 }) }
}

internal fun premiumRevenueTotal(range: PremiumTrendRange): String = when (range) {
    PremiumTrendRange.Week -> "$18,420"
    PremiumTrendRange.Month -> "$72,300"
    PremiumTrendRange.Quarter -> "$210,800"
}

internal fun premiumRevenueSeries(range: PremiumTrendRange): PremiumTrendSeries = when (range) {
    PremiumTrendRange.Week -> PremiumTrendSeries(
        labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        values = listOf(39, 55, 48, 74, 62, 82, 94),
    )
    PremiumTrendRange.Month -> PremiumTrendSeries(
        labels = listOf("1", "5", "9", "13", "17", "21", "25", "30"),
        values = listOf(35, 51, 45, 65, 61, 75, 70, 93),
    )
    PremiumTrendRange.Quarter -> PremiumTrendSeries(
        labels = listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct"),
        values = listOf(43, 49, 58, 55, 79, 93),
    )
}

/** Returns a safe bounded bar height even if the input data is empty or constant. */
internal fun premiumChartBarHeight(value: Int, maxValue: Int, chartHeight: Int = 112): Int {
    if (chartHeight <= 0) return 0
    if (maxValue <= 0) return 4.coerceAtMost(chartHeight)
    val bounded = value.coerceIn(0, maxValue)
    return (4 + (bounded.toFloat() / maxValue * (chartHeight - 4))).toInt()
}

@Composable
private fun PremiumRevenuePanel(range: PremiumTrendRange, onRangeChange: (PremiumTrendRange) -> Unit) {
    val colors = AdaptiveTheme.colors
    val data = premiumRevenueSeries(range)
    PremiumDashboardPanel("Revenue trend", "Sample performance over the selected period.") {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.XSmall),
        ) {
            PremiumTrendRange.entries.forEach { entry ->
                val label = when (entry) {
                    PremiumTrendRange.Week -> "7 days"
                    PremiumTrendRange.Month -> "30 days"
                    PremiumTrendRange.Quarter -> "90 days"
                }
                DemoToggleChip(text = label, selected = entry == range, onClick = { onRangeChange(entry) })
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        Row(verticalAlignment = Alignment.Bottom) {
            BasicText(
                text = premiumRevenueTotal(range),
                style = AdaptiveTheme.typography.title.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            BasicText(
                text = "+12.8%",
                style = AdaptiveTheme.typography.label.copy(color = colors.success),
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        ) {
            val maxValue = data.values.maxOrNull() ?: 0
            data.values.forEachIndexed { index, point ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.64f)
                                .height(premiumChartBarHeight(point, maxValue).dp)
                                .clip(AdaptiveTheme.shapes.small)
                                .background(
                                    if (index == data.values.lastIndex) colors.primary
                                    else colors.primary.copy(alpha = 0.29f),
                                ),
                        )
                    }
                    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
                    BasicText(
                        text = data.labels[index],
                        style = AdaptiveTheme.typography.caption.copy(color = colors.textMuted),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumActivityPanel() {
    val colors = AdaptiveTheme.colors
    PremiumDashboardPanel("Recent activity", "What's changed around the workspace.") {
        val updates = listOf(
            Triple("Catalog sync", "All 42 products are up to date", colors.success),
            Triple("Invoice reminders", "8 invoices need a follow-up", colors.warning),
            Triple("Access review", "Permissions reviewed for 4 teams", colors.primary),
        )
        updates.forEachIndexed { index, (title, subtitle, tint) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier.padding(top = 5.dp).size(9.dp)
                        .clip(CircleShape).background(tint),
                )
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Medium))
                Column(modifier = Modifier.weight(1f)) {
                    DashboardBody(title, strong = true)
                    Spacer(modifier = Modifier.height(4.dp))
                    DashboardBody(subtitle, muted = true, maxLines = 2)
                }
            }
            if (index < updates.lastIndex) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(colors.border))
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveBadge(text = "3 recent updates", tone = AdaptiveBadgeTone.Info)
    }
}

@Composable
private fun PremiumInvoicesPanel() {
    val colors = AdaptiveTheme.colors
    PremiumDashboardPanel("Recent invoices", "A quick look at current billing.") {
        AdminDemoData.invoices.forEachIndexed { index, invoice ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = AdaptiveTokens.Spacing.Small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    DashboardBody(invoice.customer, strong = true)
                    Spacer(modifier = Modifier.height(4.dp))
                    DashboardBody(invoice.issueDate, muted = true)
                }
                Column(horizontalAlignment = Alignment.End) {
                    DashboardBody(invoice.amount, strong = true)
                    Spacer(modifier = Modifier.height(4.dp))
                    DemoBadge(invoice.status)
                }
            }
            if (index < AdminDemoData.invoices.lastIndex) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(colors.border))
            }
        }
    }
}

@Composable
private fun PremiumInventoryPanel() {
    val colors = AdaptiveTheme.colors
    PremiumDashboardPanel("Inventory health", "Availability in the sample catalog.") {
        AdminDemoData.products.take(3).forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = AdaptiveTokens.Spacing.Small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DashboardBody(item.name, maxLines = 1)
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                DashboardBody(item.stock.toString(), strong = true)
            }
            Spacer(modifier = Modifier.height(7.dp))
            val fraction = (item.stock.toFloat() / 100f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier.fillMaxWidth().height(7.dp)
                    .clip(CircleShape).background(colors.surfaceMuted),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(fraction).height(7.dp)
                        .clip(CircleShape)
                        .background(if (item.stock <= 20) colors.warning else colors.primary),
                )
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        }
        DashboardBody("Bars represent sample stock on a 100-unit scale.", muted = true, maxLines = 2)
    }
}

@Composable
private fun PremiumDashboardPanel(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    AdaptiveCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.Large),
        elevation = AdaptiveElevationLevel.Low,
    ) {
        BasicText(
            text = title,
            style = AdaptiveTheme.typography.subtitle.copy(color = AdaptiveTheme.colors.textPrimary),
        )
        Spacer(modifier = Modifier.height(5.dp))
        DashboardBody(subtitle, muted = true, maxLines = 2)
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        content()
    }
}

@Composable
private fun DashboardEyebrow(text: String) {
    BasicText(
        text = text,
        style = AdaptiveTheme.typography.label.copy(
            color = AdaptiveTheme.colors.textMuted,
            fontWeight = FontWeight.SemiBold,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun DashboardHeading(text: String) {
    BasicText(
        text = text,
        style = AdaptiveTheme.typography.subtitle.copy(
            color = AdaptiveTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        ),
        maxLines = 2,
    )
}

@Composable
private fun DashboardBody(
    text: String,
    muted: Boolean = false,
    strong: Boolean = false,
    maxLines: Int = 1,
) {
    BasicText(
        text = text,
        style = AdaptiveTheme.typography.bodySmall.copy(
            color = if (muted) AdaptiveTheme.colors.textMuted else AdaptiveTheme.colors.textPrimary,
            fontWeight = if (strong) FontWeight.SemiBold else FontWeight.Normal,
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}
