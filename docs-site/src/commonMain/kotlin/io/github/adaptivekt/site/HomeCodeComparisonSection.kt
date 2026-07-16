package io.github.adaptivekt.site

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveTabs
import androidx.compose.foundation.ScrollState

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
        SiteText("Both examples implement the same table-to-card breakpoint behavior, empty state and status presentation.", color = SiteMuted, fontSize = 14.sp, maxLines = 4)
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
                CodeComparisonViewer(
                    beforeTitle = ComparisonImplementation.AdaptiveKt.label,
                    beforeCode = AdaptiveDataViewComparisonCode,
                    beforeBadge = formatMeaningfulLineCount(metrics.adaptiveLines),
                    beforeExpanded = expandedAdaptiveKt,
                    onBeforeExpandedChange = { expandedAdaptiveKt = it },
                    afterTitle = ComparisonImplementation.PlainCompose.label,
                    afterCode = PlainComposeDataViewComparisonCode,
                    afterBadge = formatMeaningfulLineCount(metrics.composeLines),
                    afterExpanded = expandedPlainCompose,
                    onAfterExpandedChange = { expandedPlainCompose = it },
                    beforePanelTag = "comparison-panel-adaptivekt",
                    afterPanelTag = "comparison-panel-plain-compose",
                    beforeExpandTag = "comparison-expand-adaptivekt",
                    afterExpandTag = "comparison-expand-plain-compose",
                    beforeScrollState = adaptiveScrollState,
                    afterScrollState = composeScrollState,
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
                maxLines = 4,
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
                    maxLines = 6,
                    modifier = Modifier.semantics { testTag = "comparison-metrics" }
                )
            }
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
        val panelTag = if (selectedTab == ComparisonImplementation.AdaptiveKt) "comparison-panel-adaptivekt" else "comparison-panel-plain-compose"
        val expandTag = if (selectedTab == ComparisonImplementation.AdaptiveKt) "comparison-expand-adaptivekt" else "comparison-expand-plain-compose"

        CodeViewer(
            code = code,
            title = selectedTab.label,
            badge = formatMeaningfulLineCount(linesText),
            showLineNumbers = true,
            collapsedMaxLines = 28,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            expandTag = expandTag,
            scrollState = scrollState,
            modifier = Modifier.semantics { testTag = panelTag },
        )
    }
}
