package io.github.adaptivekt.examples.ecommerce.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.core.AdaptiveThemeMode
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.model.OrderStatus
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
fun AccountScreen(state: StoreState, modifier: Modifier = Modifier) {
    val user = state.currentUser ?: return

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AdaptiveAvatar(name = user.name, size = 80.dp)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(user.email, color = Color.DarkGray)
                        Spacer(Modifier.height(4.dp))
                        AdaptiveBadge(text = user.tier, tone = AdaptiveBadgeTone.Info)
                    }
                }
            }

            item {
                AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                    item(span = 1) {
                        AdaptiveCard(onClick = { state.navigateTo(Screen.Orders) }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text("Orders", fontWeight = FontWeight.Bold)
                                Text("${MockData.mockOrders.size}", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    item(span = 1) {
                        AdaptiveCard(onClick = { state.navigateTo(Screen.Settings) }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text("Settings", fontWeight = FontWeight.Bold)
                                Text("Preferences", fontSize = 14.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }

            item {
                AdaptiveButton(text = "Sign Out", variant = AdaptiveButtonVariant.Danger, onClick = { state.logout() }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun OrdersScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    AdaptiveButton(text = "Back", onClick = { state.goBack() })
                    Spacer(Modifier.width(16.dp))
                    Text("My Orders", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            MockData.mockOrders.forEach { order ->
                item {
                    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(order.id, fontWeight = FontWeight.Bold)
                                Text(order.dateString, color = Color.DarkGray, fontSize = 14.sp)
                                Text("${order.items.size} items", fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("\$${order.total}", fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                val tone = when(order.status) {
                                    OrderStatus.Delivered -> AdaptiveBadgeTone.Success
                                    OrderStatus.Cancelled -> AdaptiveBadgeTone.Danger
                                    OrderStatus.Shipped -> AdaptiveBadgeTone.Info
                                    OrderStatus.Processing -> AdaptiveBadgeTone.Warning
                                }
                                AdaptiveBadge(text = order.status.name, tone = tone)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(state: StoreState, modifier: Modifier = Modifier) {
    var pushEnabled by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(false) }
    val currencies = listOf("USD ($)", "EUR (EUR)", "GBP (GBP)", "JPY (JPY)")
    var selectedCurrency by remember { mutableStateOf<String?>(currencies[0]) }

    val languages = listOf("English", "Spanish", "French", "German")
    var selectedLanguage by remember { mutableStateOf<String?>(languages[0]) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    AdaptiveButton(text = "Back", onClick = { state.goBack() })
                    Spacer(Modifier.width(16.dp))
                    Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            item {
                AdaptiveFormLayout {
                    section(title = "Preferences") {
                        field(label = "Currency") {
                            AdaptiveSelect(
                                options = currencies,
                                selectedOption = selectedCurrency,
                                onSelectedOptionChange = { selectedCurrency = it },
                                optionLabel = { it }
                            )
                        }
                        field(label = "Language") {
                            AdaptiveSelect(
                                options = languages,
                                selectedOption = selectedLanguage,
                                onSelectedOptionChange = { selectedLanguage = it },
                                optionLabel = { it }
                            )
                        }
                    }
                    
                    section(title = "Notifications") {
                        field(label = "Push Notifications") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Receive order updates on your device")
                                Switch(checked = pushEnabled, onCheckedChange = { pushEnabled = it })
                            }
                        }
                        field(label = "Email Marketing") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Receive offers and news")
                                Switch(checked = emailEnabled, onCheckedChange = { emailEnabled = it })
                            }
                        }
                    }

                    section(title = "Appearance") {
                        field(label = "Theme") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                listOf(
                                    AdaptiveThemeMode.System to "System",
                                    AdaptiveThemeMode.Light to "Light",
                                    AdaptiveThemeMode.Dark to "Dark",
                                ).forEach { (mode, label) ->
                                    AdaptiveChip(
                                        text = label,
                                        selected = state.themeMode == mode,
                                        tone = if (mode == AdaptiveThemeMode.System) AdaptiveChipTone.Primary else AdaptiveChipTone.Neutral,
                                        onClick = { state.themeMode = mode },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
