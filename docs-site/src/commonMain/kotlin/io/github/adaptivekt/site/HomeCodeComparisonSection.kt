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

@Composable
internal fun HomeCodeComparisonSection() {
    val metrics = remember { calculateCodeReduction(AdaptiveDataViewComparisonCode, PlainComposeDataViewComparisonCode) }
    var showMethodology by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveBadge("Less boilerplate", tone = AdaptiveBadgeTone.Info)
        Spacer(modifier = Modifier.height(12.dp))
        SiteText("Same responsive behavior. Less UI plumbing.", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, maxLines = 3)
        Spacer(modifier = Modifier.height(8.dp))
        SiteText("AdaptiveKt keeps the UI in Compose while centralizing the responsive table, compact cards, empty states and theme-aware defaults that applications otherwise rebuild themselves.", color = SiteMuted, fontSize = 15.sp, maxLines = 6)
        Spacer(modifier = Modifier.height(8.dp))
        SiteText("Both examples implement the same table-to-card breakpoint behavior, empty state and status presentation.", color = SiteMuted, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val layoutMode = resolveHomeCodeComparisonLayout(maxWidth)
            
            if (layoutMode == HomeCodeComparisonLayout.Tabbed) {
                CompactCodeComparison(metrics)
            } else {
                DesktopCodeComparison(metrics)
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
                text = if (showMethodology) "Hide methodology" else "How is this measured?",
                variant = AdaptiveButtonVariant.Ghost,
                onClick = { showMethodology = !showMethodology }
            )
            if (showMethodology) {
                Spacer(modifier = Modifier.height(8.dp))
                SiteText(
                    text = "Imports, package declarations, blank lines and full-line comments are excluded. Required helper composables are included.",
                    color = SiteMuted,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun DesktopCodeComparison(metrics: CodeReductionMetrics) {
    val expandedStates = remember { mutableStateMapOf<ComparisonImplementation, Boolean>() }
    
    AdaptiveGrid(columns = 12, horizontalGap = 16.dp, verticalGap = 16.dp) {
        item(span = 6) {
            CodeComparisonPanel(
                implementation = ComparisonImplementation.AdaptiveKt,
                metricsText = formatMeaningfulLineCount(metrics.adaptiveLines),
                code = AdaptiveDataViewComparisonCode,
                expanded = expandedStates[ComparisonImplementation.AdaptiveKt] ?: false,
                onExpandedChange = { expandedStates[ComparisonImplementation.AdaptiveKt] = it }
            )
        }
        item(span = 6) {
            CodeComparisonPanel(
                implementation = ComparisonImplementation.PlainCompose,
                metricsText = formatMeaningfulLineCount(metrics.composeLines),
                code = PlainComposeDataViewComparisonCode,
                expanded = expandedStates[ComparisonImplementation.PlainCompose] ?: false,
                onExpandedChange = { expandedStates[ComparisonImplementation.PlainCompose] = it }
            )
        }
    }
}

@Composable
private fun CompactCodeComparison(metrics: CodeReductionMetrics) {
    var selectedTab by remember { mutableStateOf(ComparisonImplementation.AdaptiveKt) }
    val expandedStates = remember { mutableStateMapOf<ComparisonImplementation, Boolean>() }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveTabs(
            tabs = ComparisonImplementation.values().toList(),
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabLabel = { 
                val lines = if (it == ComparisonImplementation.AdaptiveKt) metrics.adaptiveLines else metrics.composeLines
                "${it.label} · ${lines}" 
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        val code = if (selectedTab == ComparisonImplementation.AdaptiveKt) AdaptiveDataViewComparisonCode else PlainComposeDataViewComparisonCode
        val linesText = if (selectedTab == ComparisonImplementation.AdaptiveKt) metrics.adaptiveLines else metrics.composeLines
        
        CodeComparisonPanel(
            implementation = selectedTab,
            metricsText = formatMeaningfulLineCount(linesText),
            code = code,
            expanded = expandedStates[selectedTab] ?: false,
            onExpandedChange = { expandedStates[selectedTab] = it }
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdaptiveTheme.shapes.medium)
            .background(AdaptiveTheme.colors.surface)
            .border(1.dp, SiteLine, AdaptiveTheme.shapes.medium)
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
                .horizontalScroll(rememberScrollState())
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
                    text = if (expanded) "Collapse" else "Show full code",
                    variant = AdaptiveButtonVariant.Ghost,
                    onClick = { onExpandedChange(!expanded) }
                )
            }
        }
    }
}
