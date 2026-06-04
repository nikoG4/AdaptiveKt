package io.github.adaptivekt.examples.ecommerce.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.forms.AdaptiveFormLayout

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
                        AdaptiveButton(text = "Proceed to Checkout", onClick = { 
                            state.navigateTo(Screen.Checkout)
                        }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutScreen(state: StoreState, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf(state.currentUser?.name ?: "") }
    var email by remember { mutableStateOf(state.currentUser?.email ?: "") }
    var phone by remember { mutableStateOf("") }
    
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    val countries = listOf("United States", "Canada", "United Kingdom", "Australia")
    var selectedCountry by remember { mutableStateOf<String?>(countries[0]) }

    val shippingMethods = listOf("Standard (3-5 days)", "Express (1-2 days)", "Pickup in store")
    var selectedShipping by remember { mutableStateOf<String?>(shippingMethods[0]) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    AdaptiveButton(text = "← Back to Cart", onClick = { state.goBack() })
                    Spacer(Modifier.width(16.dp))
                    Text("Checkout", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            item {
                AdaptiveFormLayout(modifier = Modifier.fillMaxWidth()) {
                    section(title = "Contact Information") {
                        field(label = "Full Name") {
                            AdaptiveTextField(value = name, onValueChange = { name = it }, placeholder = "Jane Doe")
                        }
                        field(label = "Email Address") {
                            AdaptiveTextField(value = email, onValueChange = { email = it }, placeholder = "jane@example.com")
                        }
                        field(label = "Phone Number") {
                            AdaptiveTextField(value = phone, onValueChange = { phone = it }, placeholder = "+1 234 567 890")
                        }
                    }

                    section(title = "Shipping Address") {
                        field(label = "Country") {
                            AdaptiveSelect(
                                options = countries,
                                selectedOption = selectedCountry,
                                onSelectedOptionChange = { selectedCountry = it },
                                optionLabel = { it }
                            )
                        }
                        field(label = "Address") {
                            AdaptiveTextField(value = address, onValueChange = { address = it }, placeholder = "123 Main St")
                        }
                        field(label = "City") {
                            AdaptiveTextField(value = city, onValueChange = { city = it }, placeholder = "Metropolis")
                        }
                    }

                    section(title = "Shipping Method") {
                        field(label = "Method") {
                            AdaptiveSelect(
                                options = shippingMethods,
                                selectedOption = selectedShipping,
                                onSelectedOptionChange = { selectedShipping = it },
                                optionLabel = { it }
                            )
                        }
                    }

                    actions {
                        primary {
                            AdaptiveButton(text = "Place Order - \$${((state.cartTotal() * 100.0).toInt() / 100.0)}", onClick = {
                                val orderId = state.placeOrder()
                                state.navigateTo(Screen.OrderSuccess(orderId))
                            }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSuccessScreen(state: StoreState, orderId: String, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(80.dp).background(Color(0xFF10B981), RoundedCornerShape(40.dp)), contentAlignment = Alignment.Center) {
                    Text("✓", fontSize = 40.sp, color = Color.White)
                }
                Spacer(Modifier.height(24.dp))
                Text("Order Confirmed!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Your order $orderId has been placed.", color = Color.DarkGray)
                Spacer(Modifier.height(32.dp))
                AdaptiveButton(text = "Continue Shopping", onClick = { state.navigateTo(Screen.Home) })
            }
        }
    }
}
