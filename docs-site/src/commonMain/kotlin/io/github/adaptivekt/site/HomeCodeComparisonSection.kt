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

internal data class HomeCodeComparisonUiState(
    var selectedTab: ComparisonImplementation = ComparisonImplementation.AdaptiveKt,
    var expandedAdaptiveKt: Boolean = false,
    var expandedPlainCompose: Boolean = false,
    var methodologyExpanded: Boolean = false
)

@Composable
internal fun HomeCodeComparisonSection() {
    val metrics = remember { calculateCodeReduction(AdaptiveDataViewComparisonCode, PlainComposeDataViewComparisonCode) }
    val uiState = remember { HomeCodeComparisonUiState() }

    Column(modifier = Modifier.fillMaxWidth().semantics { testTag = "home-code-comparison" }) {
        SiteText("Less boilerplate, more focus", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        SiteText("Both examples implement the same table-to-card breakpoint behavior, empty state and status presentation.", color = SiteMuted, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val layoutMode = resolveHomeCodeComparisonLayout(maxWidth)
            
            if (layoutMode == HomeCodeComparisonLayout.Tabbed) {
                CompactCodeComparison(metrics, uiState)
            } else {
                DesktopCodeComparison(metrics, uiState)
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
                text = if (uiState.methodologyExpanded) "Hide methodology" else "How is this measured?",
                variant = AdaptiveButtonVariant.Ghost,
                onClick = { uiState.methodologyExpanded = !uiState.methodologyExpanded }
            )
            if (uiState.methodologyExpanded) {
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
private fun DesktopCodeComparison(metrics: CodeReductionMetrics, uiState: HomeCodeComparisonUiState) {
    AdaptiveGrid(columns = 12, horizontalGap = 16.dp, verticalGap = 16.dp) {
        item(span = 6) {
            CodeComparisonPanel(
                implementation = ComparisonImplementation.AdaptiveKt,
                metricsText = formatMeaningfulLineCount(metrics.adaptiveLines),
                code = AdaptiveDataViewComparisonCode,
                expanded = uiState.expandedAdaptiveKt,
                onExpandedChange = { uiState.expandedAdaptiveKt = it }
            )
        }
        item(span = 6) {
            CodeComparisonPanel(
                implementation = ComparisonImplementation.PlainCompose,
                metricsText = formatMeaningfulLineCount(metrics.composeLines),
                code = PlainComposeDataViewComparisonCode,
                expanded = uiState.expandedPlainCompose,
                onExpandedChange = { uiState.expandedPlainCompose = it }
            )
        }
    }
}

@Composable
private fun CompactCodeComparison(metrics: CodeReductionMetrics, uiState: HomeCodeComparisonUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveTabs(
            tabs = ComparisonImplementation.values().toList(),
            selectedTab = uiState.selectedTab,
            onTabSelected = { uiState.selectedTab = it },
            tabLabel = { 
                val lines = if (it == ComparisonImplementation.AdaptiveKt) metrics.adaptiveLines else metrics.composeLines
                "${it.label} — ${lines}" 
            },
            tabModifier = { tab ->
                Modifier.semantics {
                    testTag = if (tab == ComparisonImplementation.AdaptiveKt) "comparison-tab-adaptivekt" else "comparison-tab-plain-compose"
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        val code = if (uiState.selectedTab == ComparisonImplementation.AdaptiveKt) AdaptiveDataViewComparisonCode else PlainComposeDataViewComparisonCode
        val linesText = if (uiState.selectedTab == ComparisonImplementation.AdaptiveKt) metrics.adaptiveLines else metrics.composeLines
        val expanded = if (uiState.selectedTab == ComparisonImplementation.AdaptiveKt) uiState.expandedAdaptiveKt else uiState.expandedPlainCompose
        val onExpandedChange = { e: Boolean -> 
            if (uiState.selectedTab == ComparisonImplementation.AdaptiveKt) uiState.expandedAdaptiveKt = e
            else uiState.expandedPlainCompose = e
        }
        
        CodeComparisonPanel(
            implementation = uiState.selectedTab,
            metricsText = formatMeaningfulLineCount(linesText),
            code = code,
            expanded = expanded,
            onExpandedChange = onExpandedChange
        )
    }
}

@Composable
private fun CodeComparisonPanel(
    implementation: ComparisonImplementation,
    metricsText: String,
    code: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    val lines = code.lines()
    val maxCollapsedLines = 28
    val isCollapsible = lines.size > maxCollapsedLines
    val displayedCode = if (expanded || !isCollapsible) code else lines.take(maxCollapsedLines).joinToString("\n")
    val panelTag = if (implementation == ComparisonImplementation.AdaptiveKt) "comparison-panel-adaptivekt" else "comparison-panel-plain-compose"
    val expandTag = if (implementation == ComparisonImplementation.AdaptiveKt) "comparison-expand-adaptivekt" else "comparison-expand-plain-compose"

    val scrollState = remember(implementation) { ScrollState(0) }

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
