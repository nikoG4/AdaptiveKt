package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveTabs
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.layout.AdaptiveGrid
import androidx.compose.foundation.ScrollState
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag

@Composable
internal fun HomeCodeComparisonSection() {
    val metrics = remember { calculateCodeReduction(AdaptiveDataViewComparisonCode, PlainComposeDataViewComparisonCode) }
    var selectedTab by remember { mutableStateOf(ComparisonImplementation.AdaptiveKt) }
    var expandedAdaptiveKt by remember { mutableStateOf(false) }
    var expandedPlainCompose by remember { mutableStateOf(false) }
    var methodologyExpanded by remember { mutableStateOf(false) }

    val adaptiveScrollState = rememberScrollState()
    val composeScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth().semantics { testTag = "home-code-comparison" }) {
        SiteText("Less boilerplate, more focus", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        SiteText("Both examples implement the same table-to-card breakpoint behavior, empty state and status presentation.", color = SiteMuted, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val layoutMode = resolveHomeCodeComparisonLayout(maxWidth)

            if (layoutMode == HomeCodeComparisonLayout.Tabbed) {
                CompactCodeComparison(
                    metrics = metrics,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    expandedAdaptiveKt = expandedAdaptiveKt,
                    onExpandedAdaptiveKtChange = { expandedAdaptiveKt = it },
                    expandedPlainCompose = expandedPlainCompose,
                    onExpandedPlainComposeChange = { expandedPlainCompose = it },
                    adaptiveScrollState = adaptiveScrollState,
                    composeScrollState = composeScrollState
                )
            } else {
                DesktopCodeComparison(
                    metrics = metrics,
                    expandedAdaptiveKt = expandedAdaptiveKt,
                    onExpandedAdaptiveKtChange = { expandedAdaptiveKt = it },
                    expandedPlainCompose = expandedPlainCompose,
                    onExpandedPlainComposeChange = { expandedPlainCompose = it },
                    adaptiveScrollState = adaptiveScrollState,
                    composeScrollState = composeScrollState
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AdaptiveCard {
            SiteText(
                text = "${metrics.reductionPercent}% fewer meaningful UI lines in this example",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            SiteText(
                text = "AdaptiveKt: ${metrics.adaptiveLines} meaningful lines vs Plain Compose: ${metrics.composeLines} meaningful lines (${metrics.savedLines} fewer lines).",
                color = SiteMuted,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AdaptiveButton(
                modifier = Modifier.semantics { testTag = "comparison-methodology" },
                text = if (methodologyExpanded) "Hide methodology" else "How is this measured?",
                variant = AdaptiveButtonVariant.Ghost,
                onClick = { methodologyExpanded = !methodologyExpanded }
            )
            if (methodologyExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                SiteText(
                    text = "Imports, package declarations, blank lines and full-line comments are excluded. Required helper composables are included.",
                    color = SiteMuted,
                    fontSize = 14.sp,
                    modifier = Modifier.semantics { testTag = "comparison-metrics" }
                )
            }
        }
    }
}

@Composable
private fun DesktopCodeComparison(
    metrics: CodeReductionMetrics,
    expandedAdaptiveKt: Boolean,
    onExpandedAdaptiveKtChange: (Boolean) -> Unit,
    expandedPlainCompose: Boolean,
    onExpandedPlainComposeChange: (Boolean) -> Unit,
    adaptiveScrollState: ScrollState,
    composeScrollState: ScrollState
) {
    AdaptiveGrid(columns = 12, horizontalGap = 16.dp, verticalGap = 16.dp) {
        item(span = 6) {
            CodeComparisonPanel(
                implementation = ComparisonImplementation.AdaptiveKt,
                metricsText = formatMeaningfulLineCount(metrics.adaptiveLines),
                code = AdaptiveDataViewComparisonCode,
                expanded = expandedAdaptiveKt,
                onExpandedChange = onExpandedAdaptiveKtChange,
                scrollState = adaptiveScrollState
            )
        }
        item(span = 6) {
            CodeComparisonPanel(
                implementation = ComparisonImplementation.PlainCompose,
                metricsText = formatMeaningfulLineCount(metrics.composeLines),
                code = PlainComposeDataViewComparisonCode,
                expanded = expandedPlainCompose,
                onExpandedChange = onExpandedPlainComposeChange,
                scrollState = composeScrollState
            )
        }
    }
}

@Composable
private fun CompactCodeComparison(
    metrics: CodeReductionMetrics,
    selectedTab: ComparisonImplementation,
    onTabSelected: (ComparisonImplementation) -> Unit,
    expandedAdaptiveKt: Boolean,
    onExpandedAdaptiveKtChange: (Boolean) -> Unit,
    expandedPlainCompose: Boolean,
    onExpandedPlainComposeChange: (Boolean) -> Unit,
    adaptiveScrollState: ScrollState,
    composeScrollState: ScrollState
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveTabs(
            tabs = ComparisonImplementation.values().toList(),
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            tabLabel = {
                val lines = if (it == ComparisonImplementation.AdaptiveKt) metrics.adaptiveLines else metrics.composeLines
                "${it.label} — ${lines}"
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        val code = if (selectedTab == ComparisonImplementation.AdaptiveKt) AdaptiveDataViewComparisonCode else PlainComposeDataViewComparisonCode
        val linesText = if (selectedTab == ComparisonImplementation.AdaptiveKt) metrics.adaptiveLines else metrics.composeLines
        val expanded = if (selectedTab == ComparisonImplementation.AdaptiveKt) expandedAdaptiveKt else expandedPlainCompose
        val onExpandedChange = { e: Boolean ->
            if (selectedTab == ComparisonImplementation.AdaptiveKt) onExpandedAdaptiveKtChange(e)
            else onExpandedPlainComposeChange(e)
        }
        val scrollState = if (selectedTab == ComparisonImplementation.AdaptiveKt) adaptiveScrollState else composeScrollState

        CodeComparisonPanel(
            implementation = selectedTab,
            metricsText = formatMeaningfulLineCount(linesText),
            code = code,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            scrollState = scrollState
        )
    }
}

@Composable
private fun CodeComparisonPanel(
    implementation: ComparisonImplementation,
    metricsText: String,
    code: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    scrollState: ScrollState
) {
    val lines = code.lines()
    val maxCollapsedLines = 28
    val isCollapsible = lines.size > maxCollapsedLines
    val displayedCode = if (expanded || !isCollapsible) code else lines.take(maxCollapsedLines).joinToString("\n")
    val panelTag = if (implementation == ComparisonImplementation.AdaptiveKt) "comparison-panel-adaptivekt" else "comparison-panel-plain-compose"
    val expandTag = if (implementation == ComparisonImplementation.AdaptiveKt) "comparison-expand-adaptivekt" else "comparison-expand-plain-compose"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdaptiveTheme.shapes.medium)
            .background(AdaptiveTheme.colors.surface)
            .border(1.dp, SiteLine, AdaptiveTheme.shapes.medium)
            .semantics { testTag = panelTag }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(AdaptiveTheme.colors.surfaceMuted)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (maxWidth < 200.dp) {
                Column {
                    SiteText(implementation.label, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    AdaptiveBadge(metricsText, tone = AdaptiveBadgeTone.Info)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SiteText(implementation.label, fontWeight = FontWeight.Bold)
                    AdaptiveBadge(metricsText, tone = AdaptiveBadgeTone.Info)
                }
            }
        }

        AdaptiveDivider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(16.dp)
        ) {
            AdaptiveSelectionArea {
                BasicText(
                    text = displayedCode,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = AdaptiveTheme.colors.textPrimary,
                        fontFamily = FontFamily.Monospace,
                    ),
                    softWrap = false,
                )
            }
        }

        if (isCollapsible && !expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AdaptiveTheme.colors.surfaceMuted.copy(alpha = 0.5f))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                SiteText("// ... ${lines.size - maxCollapsedLines} more lines", color = AdaptiveTheme.colors.textMuted, fontSize = 12.sp)
            }
        }

        if (isCollapsible) {
            AdaptiveDivider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AdaptiveButton(
                    modifier = Modifier.semantics { testTag = expandTag },
                    text = if (expanded) "Collapse" else "Show full code",
                    variant = AdaptiveButtonVariant.Ghost,
                    onClick = { onExpandedChange(!expanded) }
                )
            }
        }
    }
}
