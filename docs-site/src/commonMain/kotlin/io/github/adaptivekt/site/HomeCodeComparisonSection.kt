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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTabs
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.layout.AdaptiveGrid
import kotlin.math.max

internal data class CodeReductionMetrics(
    val adaptiveLines: Int,
    val composeLines: Int,
    val savedLines: Int,
    val reductionPercent: Int,
)

internal fun countMeaningfulCodeLines(code: String): Int {
    return code.lines().count { line ->
        val trimmed = line.trim()
        trimmed.isNotEmpty() &&
        !trimmed.startsWith("import ") &&
        !trimmed.startsWith("package ") &&
        !trimmed.startsWith("//") &&
        !(trimmed.startsWith("/*") && trimmed.endsWith("*/"))
    }
}

internal fun calculateCodeReduction(adaptiveCode: String, composeCode: String): CodeReductionMetrics {
    val adaptiveLines = countMeaningfulCodeLines(adaptiveCode)
    val composeLines = countMeaningfulCodeLines(composeCode)
    val savedLines = max(0, composeLines - adaptiveLines)
    val reductionPercent = if (composeLines > 0) (savedLines * 100) / composeLines else 0

    return CodeReductionMetrics(
        adaptiveLines = adaptiveLines,
        composeLines = composeLines,
        savedLines = savedLines,
        reductionPercent = reductionPercent,
    )
}

internal val AdaptiveDataViewComparisonCode = """
@Composable
fun UserDirectory(users: List<User>) {
    val columns = listOf(
        AdaptiveDataColumn<User>(
            id = "name",
            header = "Name",
            mobileRole = AdaptiveDataMobileRole.Title,
            cell = { user -> Text(user.name, fontWeight = FontWeight.Bold) },
        ),
        AdaptiveDataColumn<User>(
            id = "role",
            header = "Role",
            mobileRole = AdaptiveDataMobileRole.Subtitle,
            cell = { user -> Text(user.role) },
        ),
        AdaptiveDataColumn<User>(
            id = "status",
            header = "Status",
            mobileRole = AdaptiveDataMobileRole.Status,
            cell = { user -> Text(user.status) },
        )
    )

    AdaptiveDataView(
        state = AdaptiveDataContent(users),
        columns = columns,
        displayMode = AdaptiveDataDisplayMode.Auto,
    )
}
""".trimIndent()

internal val PlainComposeDataViewComparisonCode = """
@Composable
fun UserDirectory(users: List<User>) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val compact = maxWidth < 600.dp
        
        if (users.isEmpty()) {
            EmptyUsersState()
        } else if (compact) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(users) { user ->
                    UserMobileCard(user)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            ) {
                UserTableHeader()
                Divider()
                users.forEachIndexed { index, user ->
                    UserTableRow(user)
                    if (index < users.lastIndex) Divider()
                }
            }
        }
    }
}

@Composable
private fun UserMobileCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(user.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(user.role, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
                }
                UserStatusBadge(user.status)
            }
        }
    }
}

@Composable
private fun UserTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text("Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Role", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Status", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UserTableRow(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(user.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(user.role, modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(1f)) {
            UserStatusBadge(user.status)
        }
    }
}

@Composable
private fun EmptyUsersState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No users available", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UserStatusBadge(status: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(status, color = MaterialTheme.colors.primary, fontSize = 12.sp)
    }
}
""".trimIndent()

@Composable
internal fun HomeCodeComparisonSection(compact: Boolean) {
    val metrics = remember { calculateCodeReduction(AdaptiveDataViewComparisonCode, PlainComposeDataViewComparisonCode) }

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveBadge("Less boilerplate", tone = AdaptiveBadgeTone.Info)
        Spacer(modifier = Modifier.height(12.dp))
        SiteText("Same adaptive result. Less UI plumbing.", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, maxLines = 3)
        Spacer(modifier = Modifier.height(8.dp))
        SiteText("AdaptiveKt keeps the UI in Compose while centralizing the responsive table, compact cards, empty states and theme-aware defaults that applications otherwise rebuild themselves.", color = SiteMuted, fontSize = 15.sp, maxLines = 6)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (compact) {
            CompactCodeComparison(metrics)
        } else {
            DesktopCodeComparison(metrics)
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
        }
    }
}

@Composable
private fun DesktopCodeComparison(metrics: CodeReductionMetrics) {
    AdaptiveGrid(columns = 12, horizontalGap = 16.dp, verticalGap = 16.dp) {
        item(span = 6) {
            CodeComparisonPanel(
                title = "AdaptiveKt",
                metricsText = "${metrics.adaptiveLines} meaningful lines",
                code = AdaptiveDataViewComparisonCode
            )
        }
        item(span = 6) {
            CodeComparisonPanel(
                title = "Plain Compose",
                metricsText = "${metrics.composeLines} meaningful lines",
                code = PlainComposeDataViewComparisonCode
            )
        }
    }
}

@Composable
private fun CompactCodeComparison(metrics: CodeReductionMetrics) {
    var selectedTab by remember { mutableStateOf("AdaptiveKt") }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveTabs(
            tabs = listOf("AdaptiveKt", "Plain Compose"),
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabLabel = { 
                val lines = if (it == "AdaptiveKt") metrics.adaptiveLines else metrics.composeLines
                "${it} · ${lines}" 
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedTab == "AdaptiveKt") {
            CodeComparisonPanel(
                title = "AdaptiveKt",
                metricsText = "${metrics.adaptiveLines} meaningful lines",
                code = AdaptiveDataViewComparisonCode
            )
        } else {
            CodeComparisonPanel(
                title = "Plain Compose",
                metricsText = "${metrics.composeLines} meaningful lines",
                code = PlainComposeDataViewComparisonCode
            )
        }
    }
}

@Composable
private fun CodeComparisonPanel(title: String, metricsText: String, code: String) {
    var expanded by remember { mutableStateOf(false) }
    val lines = code.lines()
    val maxCollapsedLines = 28
    val isCollapsible = lines.size > maxCollapsedLines
    val displayedCode = if (expanded || !isCollapsible) code else lines.take(maxCollapsedLines).joinToString("\n") + "\n\n  // ... ${lines.size - maxCollapsedLines} more lines"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdaptiveTheme.shapes.medium)
            .background(AdaptiveTheme.colors.surface)
            .border(1.dp, SiteLine, AdaptiveTheme.shapes.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AdaptiveTheme.colors.surfaceMuted)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SiteText(title, fontWeight = FontWeight.Bold)
            AdaptiveBadge(metricsText, tone = AdaptiveBadgeTone.Info)
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
                    onClick = { expanded = !expanded }
                )
            }
        }
    }
}
