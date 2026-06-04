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
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.feedback.AdaptiveEmptyState

@Composable
fun WishlistScreen(state: StoreState, modifier: Modifier = Modifier) {
    if (state.wishlistIds.isEmpty()) {
        AdaptiveContainer(modifier = modifier.fillMaxSize()) {
            AdaptiveEmptyState(
                title = "Your wishlist is empty",
                description = "Save items you love here.",
                action = { AdaptiveButton(text = "Explore Products", onClick = { state.navigateTo(Screen.Products) }) },
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            )
        }
        return
    }

    val products = MockData.products.filter { state.wishlistIds.contains(it.id) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("My Wishlist", fontSize = 28.sp, fontWeight = FontWeight.Bold)
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
                                    Text("\$${prod.price}", fontWeight = FontWeight.SemiBold)
                                    Spacer(Modifier.height(16.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        AdaptiveButton(text = "Move to Cart", onClick = { 
                                            state.addToCart(prod.id, prod.variants.firstOrNull()?.id, 1)
                                            state.toggleWishlist(prod.id)
                                        })
                                        AdaptiveButton(text = "Remove", variant = AdaptiveButtonVariant.Danger, onClick = { state.toggleWishlist(prod.id) })
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
