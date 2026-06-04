package io.github.adaptivekt.examples.ecommerce.ui.products

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
fun ProductListScreen(state: StoreState, modifier: Modifier = Modifier) {
    val products = state.filteredProducts()

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Products", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    if (state.selectedCategoryId != null || state.searchQuery.isNotBlank()) {
                        AdaptiveButton(text = "Clear Filters", onClick = { state.clearFilters() })
                    }
                }
            }

            item {
                AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                    products.forEach { prod ->
                        item(span = 1) {
                            AdaptiveCard(onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) }) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                        Text(MockData.categories.find { it.id == prod.categoryId }?.iconSymbol ?: "📦", fontSize = 64.sp)
                                    }
                                    Spacer(Modifier.height(16.dp))
                                    Text(prod.name, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("\$${prod.price}", fontWeight = FontWeight.SemiBold)
                                        if (prod.oldPrice != null) {
                                            Spacer(Modifier.width(8.dp))
                                            Text("\$${prod.oldPrice}", color = Color.Gray, fontSize = 12.sp) // Strikethrough not trivial without Material3 TextStyle
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
}

@Composable
fun ProductDetailScreen(state: StoreState, productId: String, modifier: Modifier = Modifier) {
    val product = MockData.products.find { it.id == productId }
    if (product == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Product not found") }
        return
    }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                AdaptiveButton(text = "← Back", onClick = { state.goBack() })
            }
            item {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFE5E7EB), RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Text(MockData.categories.find { it.id == product.categoryId }?.iconSymbol ?: "📦", fontSize = 100.sp)
                }
            }
            item {
                Text(product.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("\$${product.price}", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2563EB))
                    if (product.oldPrice != null) {
                        Spacer(Modifier.width(16.dp))
                        Text("Was \$${product.oldPrice}", color = Color.Gray)
                    }
                }
            }
            item {
                Text("Description", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(product.description, color = Color.DarkGray)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AdaptiveButton(text = "Add to Cart", onClick = { 
                        state.addToCart(product.id, product.variants.firstOrNull()?.id, 1)
                        state.navigateTo(Screen.Cart)
                    })
                    AdaptiveButton(text = if (state.wishlistIds.contains(product.id)) "Remove from Wishlist" else "Add to Wishlist", onClick = { state.toggleWishlist(product.id) })
                }
            }
        }
    }
}
