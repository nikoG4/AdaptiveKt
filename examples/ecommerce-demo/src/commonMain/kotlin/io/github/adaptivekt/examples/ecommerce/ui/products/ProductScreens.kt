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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.feedback.AdaptiveEmptyState

@Composable
fun ProductListScreen(state: StoreState, modifier: Modifier = Modifier) {
    val products = state.filteredProducts()

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.semantics { contentDescription = "Product List Screen" }
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween, 
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Products", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    if (state.selectedCategoryId != null || state.searchQuery.isNotBlank()) {
                        AdaptiveButton(
                            text = "Clear Filters", 
                            onClick = { state.clearFilters() },
                            variant = AdaptiveButtonVariant.Secondary
                        )
                    }
                }
            }

            if (products.isEmpty()) {
                item {
                    AdaptiveEmptyState(
                        title = "No products found",
                        description = "Try adjusting your search or filters to find what you're looking for.",
                        action = {
                            AdaptiveButton(text = "Clear Filters", onClick = { state.clearFilters() })
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp)
                    )
                }
            } else {
                item {
                    AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        products.forEach { prod ->
                            item(span = 1) {
                                AdaptiveCard(onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) }) {
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
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("\$${prod.price}", color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                                            if (prod.oldPrice != null) {
                                                Spacer(Modifier.width(8.dp))
                                                Text("\$${prod.oldPrice}", color = Color.Gray, fontSize = 12.sp, textDecoration = TextDecoration.LineThrough)
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
}

@Composable
fun ProductDetailScreen(state: StoreState, productId: String, modifier: Modifier = Modifier) {
    val product = MockData.products.find { it.id == productId }
    if (product == null) {
        AdaptiveContainer(modifier = modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
                Text("Product not found", fontSize = 20.sp, color = Color.Gray) 
            }
        }
        return
    }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp), 
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.semantics { contentDescription = "Product Detail Screen" }
        ) {
            item {
                AdaptiveButton(
                    text = "← Back", 
                    onClick = { state.goBack() },
                    variant = AdaptiveButtonVariant.Ghost
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(16.dp)), 
                    contentAlignment = Alignment.Center
                ) {
                    Text(MockData.categories.find { it.id == product.categoryId }?.iconSymbol ?: "📦", fontSize = 120.sp)
                }
            }
            item {
                Text(product.name, fontSize = 32.sp, fontWeight = FontWeight.Bold, lineHeight = 40.sp)
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("\$${product.price}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    if (product.oldPrice != null) {
                        Spacer(Modifier.width(16.dp))
                        Text("Was \$${product.oldPrice}", color = Color.Gray, fontSize = 16.sp, textDecoration = TextDecoration.LineThrough, modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
            }
            item {
                Text("Description", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text(product.description, color = Color.DarkGray, fontSize = 16.sp, lineHeight = 24.sp)
            }
            item {
                Spacer(Modifier.height(16.dp))
                AdaptiveGrid(columns = 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
                    item(span = 1) {
                        AdaptiveButton(
                            text = "Add to Cart", 
                            onClick = { 
                                state.addToCart(product.id, product.variants.firstOrNull()?.id, 1)
                                state.navigateTo(Screen.Cart)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item(span = 1) {
                        AdaptiveButton(
                            text = if (state.wishlistIds.contains(product.id)) "Remove from Wishlist" else "Add to Wishlist", 
                            onClick = { state.toggleWishlist(product.id) },
                            variant = if (state.wishlistIds.contains(product.id)) AdaptiveButtonVariant.Secondary else AdaptiveButtonVariant.Ghost,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
