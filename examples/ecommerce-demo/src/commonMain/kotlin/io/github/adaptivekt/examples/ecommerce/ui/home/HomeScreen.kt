package io.github.adaptivekt.examples.ecommerce.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveContainer

@Composable
fun HomeScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Hero section
            item {
                AdaptiveCard(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 250.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Color(0xFFF3F4F6)), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Build your perfect setup", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            Text("Shop the latest tech and accessories.", fontSize = 16.sp, color = Color.DarkGray)
                            Spacer(Modifier.height(24.dp))
                            AdaptiveButton(text = "Shop new arrivals", onClick = { state.navigateTo(Screen.Products) })
                        }
                    }
                }
            }

            // Categories
            item {
                Column {
                    Text("Categories", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        MockData.categories.take(4).forEach { cat ->
                            item(span = 1) {
                                AdaptiveCard(onClick = {
                                    state.selectedCategoryId = cat.id
                                    state.navigateTo(Screen.Products)
                                }) {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(cat.iconSymbol, fontSize = 48.sp)
                                            Spacer(Modifier.height(8.dp))
                                            Text(cat.name, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Featured Products
            item {
                Column {
                    Text("Trending now", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        MockData.products.take(4).forEach { prod ->
                            item(span = 1) {
                                AdaptiveCard(onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) }) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                            Text(MockData.categories.find { it.id == prod.categoryId }?.iconSymbol ?: "📦", fontSize = 64.sp)
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        Text(prod.name, fontWeight = FontWeight.Bold)
                                        Spacer(Modifier.height(4.dp))
                                        Text("\$${prod.price}", color = Color.DarkGray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
