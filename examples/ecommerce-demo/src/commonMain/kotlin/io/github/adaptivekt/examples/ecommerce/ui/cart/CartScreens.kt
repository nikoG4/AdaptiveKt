package io.github.adaptivekt.examples.ecommerce.ui.cart

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
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.feedback.AdaptiveEmptyState

@Composable
fun CartScreen(state: StoreState, modifier: Modifier = Modifier) {
    if (state.cartItems.isEmpty()) {
        AdaptiveContainer(modifier = modifier.fillMaxSize()) {
            AdaptiveEmptyState(
                title = "Your cart is empty",
                description = "Looks like you haven't added anything to your cart yet.",
                action = { AdaptiveButton(text = "Start Shopping", onClick = { state.navigateTo(Screen.Products) }) },
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            )
        }
        return
    }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                Text("Shopping Cart", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }

            state.cartItems.forEach { item ->
                val product = MockData.products.find { it.id == item.productId }
                if (product != null) {
                    item {
                        AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(80.dp).background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                    Text(MockData.categories.find { it.id == product.categoryId }?.iconSymbol ?: "📦", fontSize = 32.sp)
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.name, fontWeight = FontWeight.Bold)
                                    Text("\$${product.price}", color = Color.DarkGray)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AdaptiveButton(text = "-", onClick = { state.updateCartQuantity(product.id, item.quantity - 1) })
                                    Spacer(Modifier.width(8.dp))
                                    Text("${item.quantity}")
                                    Spacer(Modifier.width(8.dp))
                                    AdaptiveButton(text = "+", onClick = { state.updateCartQuantity(product.id, item.quantity + 1) })
                                }
                            }
                        }
                    }
                }
            }

            item {
                AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = Color.DarkGray)
                            Text("\$${((state.cartSubtotal() * 100.0).toInt() / 100.0)}")
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tax (10%)", color = Color.DarkGray)
                            Text("\$${(((state.cartSubtotal() * 0.1) * 100.0).toInt() / 100.0)}")
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text("\$${((state.cartTotal() * 100.0).toInt() / 100.0)}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Spacer(Modifier.height(24.dp))
                        AdaptiveButton(text = "Checkout", onClick = { 
                            val orderId = state.placeOrder()
                            state.navigateTo(Screen.OrderSuccess(orderId))
                        }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
