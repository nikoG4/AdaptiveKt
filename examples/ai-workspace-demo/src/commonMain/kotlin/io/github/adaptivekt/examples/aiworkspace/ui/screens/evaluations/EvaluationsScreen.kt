package io.github.adaptivekt.examples.aiworkspace.ui.screens.evaluations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.layout.*
import io.github.adaptivekt.examples.aiworkspace.model.*
import io.github.adaptivekt.examples.aiworkspace.navigation.AiRoute
import io.github.adaptivekt.examples.aiworkspace.state.AiWorkspaceStore
import io.github.adaptivekt.examples.aiworkspace.ui.components.*
import io.github.adaptivekt.navigation.AdaptiveNavigator

@Composable
public fun EvaluationsScreen(store: AiWorkspaceStore, navigator: AdaptiveNavigator<AiRoute>, selectedId: String?) {
    val selectedItem = store.evaluations.find { it.id == selectedId }

    AdaptiveListDetailScaffold(
        selectedItem = selectedItem,
        onBackToList = { navigator.navigate(AiRoute.Evaluations) },
        listPane = {
            AdaptivePage {
                AdaptiveActionBar(leadingContent = { Text("Evaluations", style = MaterialTheme.typography.titleLarge) })
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(store.evaluations) { eval ->
                        EvaluationListItem(
                            eval = eval,
                            isSelected = eval.id == selectedId,
                            onClick = { navigator.navigate(AiRoute.EvaluationDetail(eval.id)) }
                        )
                    }
                }
            }
        },
        detailPane = { eval ->
            AdaptiveScrollablePage {
                AdaptiveActionBar(leadingContent = { Text(eval.name) })

                AdaptiveSection(title = "Metrics") {
                    AdaptiveGrid() {
                        item { MetricCard("Dataset", eval.dataset) }
                        item { MetricCard("Model", eval.model) }
                        item { MetricCard("Pass Rate", "${eval.passRate}%") }
                        item { MetricCard("Avg Latency", eval.avgLatency) }
                        item { MetricCard("Avg Tokens", eval.avgTokens.toString()) }
                    }
                }

                if (eval.cases.isNotEmpty()) {
                    AdaptiveSection(title = "Test Cases") {
                        eval.cases.forEach { case ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Case: ${case.id}", style = MaterialTheme.typography.labelMedium)
                                        Spacer(Modifier.weight(1f))
                                        StatusBadge(if (case.passed) "Passed" else "Failed", isSuccess = case.passed)
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text("Prompt:", style = MaterialTheme.typography.labelMedium)
                                    Text(case.prompt, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Expected:", style = MaterialTheme.typography.labelMedium)
                                    Text(case.expected, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Actual:", style = MaterialTheme.typography.labelMedium)
                                    Text(case.actual, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                } else {
                    AdaptiveSection {
                        Text("No detailed test cases available for this run.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        },
        emptyDetail = {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Select an evaluation run to view details")
            }
        }
    )
}

@Composable
private fun MetricCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EvaluationListItem(
    eval: EvaluationRun,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(eval.name, style = MaterialTheme.typography.titleMedium, color = contentColor)
            Spacer(Modifier.height(4.dp))
            Text(
                "${eval.dataset} • ${eval.model}",
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
        Text(
            "${eval.passRate}%",
            style = MaterialTheme.typography.titleLarge,
            color = if (eval.passRate > 80) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

