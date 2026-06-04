package io.github.adaptivekt.examples.ecommerce.ui.states

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.feedback.AdaptiveLoadingState
import io.github.adaptivekt.feedback.AdaptiveErrorState
import io.github.adaptivekt.feedback.AdaptiveEmptyState

@Composable
fun UiStatesScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
            item {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    AdaptiveButton(text = "← Back to Home", onClick = { state.navigateTo(Screen.Home) })
                    Spacer(Modifier.width(16.dp))
                    Text("Component States", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            item {
                Text("Badges", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                AdaptiveCard {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdaptiveBadge(text = "Neutral", tone = AdaptiveBadgeTone.Neutral)
                        AdaptiveBadge(text = "Success", tone = AdaptiveBadgeTone.Success)
                        AdaptiveBadge(text = "Warning", tone = AdaptiveBadgeTone.Warning)
                        AdaptiveBadge(text = "Danger", tone = AdaptiveBadgeTone.Danger)
                        AdaptiveBadge(text = "Info", tone = AdaptiveBadgeTone.Info)
                    }
                }
            }

            item {
                Text("Loading State", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                AdaptiveCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    AdaptiveLoadingState(message = "Loading products...", modifier = Modifier.fillMaxSize())
                }
            }

            item {
                Text("Error State", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                AdaptiveCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    AdaptiveErrorState(
                        title = "Connection Error",
                        description = "Failed to load catalog. Please check your network.",
                        retryAction = { AdaptiveButton(text = "Retry", onClick = {}) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            item {
                Text("Empty State", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                AdaptiveCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    AdaptiveEmptyState(
                        title = "No results found",
                        description = "Try adjusting your filters.",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
