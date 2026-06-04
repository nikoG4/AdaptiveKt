package io.github.adaptivekt.examples.ecommerce.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveContainer

@Composable
fun AccountScreen(state: StoreState, modifier: Modifier = Modifier) {
    val user = state.currentUser ?: return

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF3B82F6)), contentAlignment = Alignment.Center) {
                        Text(user.name.firstOrNull()?.toString() ?: "U", fontSize = 32.sp, color = Color.White)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(user.email, color = Color.DarkGray)
                        Spacer(Modifier.height(4.dp))
                        Text(user.tier, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                    }
                }
            }

            item {
                Text("Recent Orders", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                MockData.mockOrders.forEach { order ->
                    AdaptiveCard(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(order.id, fontWeight = FontWeight.Bold)
                                Text(order.dateString, color = Color.DarkGray, fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("\$${order.total}", fontWeight = FontWeight.SemiBold)
                                Text(order.status.name, color = Color(0xFF8B5CF6), fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            item {
                AdaptiveButton(text = "Sign Out", onClick = { state.logout() }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
