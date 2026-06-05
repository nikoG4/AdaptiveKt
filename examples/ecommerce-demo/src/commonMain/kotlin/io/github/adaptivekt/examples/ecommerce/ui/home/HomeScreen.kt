package io.github.adaptivekt.examples.ecommerce.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.semantics { contentDescription = "Home Screen" }
        ) {
            // Hero section
            item {
                AdaptiveCard(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 280.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    val gradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(gradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                "Build your perfect setup", 
                                fontSize = 36.sp, 
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 40.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Shop the latest tech and accessories.", 
                                fontSize = 18.sp, 
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(32.dp))
                            AdaptiveButton(
                                text = "Shop new arrivals", 
                                onClick = { state.navigateTo(Screen.Products) }
                            )
                        }
                    }
                }
            }

            // Categories
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Categories", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        AdaptiveButton(
                            text = "View All", 
                            onClick = { state.navigateTo(Screen.Products) },
                            variant = io.github.adaptivekt.components.AdaptiveButtonVariant.Ghost
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        MockData.categories.take(4).forEach { cat ->
                            item(span = 1) {
                                AdaptiveCard(
                                    onClick = {
                                        state.selectedCategoryId = cat.id
                                        state.navigateTo(Screen.Products)
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), 
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Box(
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .background(Color(0xFFEFF6FF), RoundedCornerShape(32.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(cat.iconSymbol, fontSize = 32.sp)
                                            }
                                            Spacer(Modifier.height(12.dp))
                                            Text(cat.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
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
                    Text("Trending now", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        MockData.products.take(4).forEach { prod ->
                            item(span = 1) {
                                AdaptiveCard(
                                    onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) }
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(160.dp)
                                                .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp)), 
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(MockData.categories.find { it.id == prod.categoryId }?.iconSymbol ?: "📦", fontSize = 64.sp)
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                                        Spacer(Modifier.height(4.dp))
                                        Text("\$${prod.price}", color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
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
