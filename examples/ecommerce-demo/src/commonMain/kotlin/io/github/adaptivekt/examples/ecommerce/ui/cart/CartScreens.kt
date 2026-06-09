package io.github.adaptivekt.examples.ecommerce.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import io.github.adaptivekt.components.*
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveTwoPane
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.FieldSpan
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcons
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcon
import io.github.adaptivekt.examples.ecommerce.ui.components.ProductVisual
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

@Composable
fun CartScreen(state: StoreState, modifier: Modifier = Modifier) {
    if (state.cartItems.isEmpty()) {
        AdaptiveContainer(modifier = modifier.fillMaxSize()) {
            AdaptiveEmptyState(
                title = "Your shopping bag is empty",
                description = "Discover our latest tech arrivals and find your perfect setup.",
                action = { AdaptiveButton(text = "Start Shopping", onClick = { state.navigateTo(Screen.Products) }) },
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            )
        }
        return
    }

    AdaptiveScrollablePage(modifier = modifier.fillMaxSize()) {
        val layoutInfo = LocalAdaptiveLayoutInfo.current
        val compact = layoutInfo.isCompact

        Spacer(Modifier.height(12.dp))
        Text("Shopping Bag", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
        Spacer(Modifier.height(8.dp))
        Text("${state.cartItems.sumOf { it.quantity }} items in your bag", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
        Spacer(Modifier.height(24.dp))
        
        AdaptiveTwoPane(
            primaryWeight = 1.5f,
            secondaryWeight = 1f,
            gap = 48.dp,
            primary = {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    state.cartItems.forEach { item ->
                        val product = MockData.products.find { it.id == item.productId }
                        if (product != null) {
                            AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                                if (compact) {
                                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CartProductVisual(product = product, size = 88.dp)
                                            Spacer(Modifier.width(16.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    product.name,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp,
                                                    lineHeight = 22.sp,
                                                    color = AdaptiveTheme.colors.textPrimary,
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    MockData.categories.find { it.id == product.categoryId }?.name ?: "",
                                                    color = AdaptiveTheme.colors.textSecondary,
                                                    fontSize = 14.sp,
                                                )
                                                Spacer(Modifier.height(6.dp))
                                                Text(
                                                    "\$${product.price.toPriceString()}",
                                                    color = AdaptiveTheme.colors.textPrimary,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            QuantityStepper(
                                                quantity = item.quantity,
                                                onDecrease = { state.updateCartQuantity(product.id, item.quantity - 1) },
                                                onIncrease = { state.updateCartQuantity(product.id, item.quantity + 1) },
                                            )
                                            IconButton(AppIcons.Trash, tint = AdaptiveTheme.colors.danger) { state.removeFromCart(product.id) }
                                        }
                                    }
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CartProductVisual(product = product, size = 100.dp)
                                        Spacer(Modifier.width(24.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AdaptiveTheme.colors.textPrimary)
                                            Spacer(Modifier.height(4.dp))
                                            Text(MockData.categories.find { it.id == product.categoryId }?.name ?: "", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
                                            Spacer(Modifier.height(8.dp))
                                            Text("\$${product.price.toPriceString()}", color = AdaptiveTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                                        }

                                        QuantityStepper(
                                            quantity = item.quantity,
                                            onDecrease = { state.updateCartQuantity(product.id, item.quantity - 1) },
                                            onIncrease = { state.updateCartQuantity(product.id, item.quantity + 1) },
                                        )

                                        Spacer(Modifier.width(24.dp))
                                        IconButton(AppIcons.Trash, tint = AdaptiveTheme.colors.danger) { state.removeFromCart(product.id) }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            secondary = {
                AdaptiveCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(24.dp)) {
                    Column {
                        Text("Order Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(24.dp))
                        
                        SummaryRow("Subtotal", "\$${state.cartSubtotal().toPriceString()}")
                        SummaryRow("Shipping", "Calculated at checkout")
                        SummaryRow("Tax", "\$${(state.cartSubtotal() * 0.1).toPriceString()}")
                        
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)).padding(vertical = 24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = AdaptiveTheme.colors.textPrimary)
                            Text("\$${state.cartTotal().toPriceString()}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Color(0xFF3B82F6))
                        }
                        
                        Spacer(Modifier.height(32.dp))
                        AdaptiveButton(
                            text = "Checkout", 
                            onClick = { state.navigateTo(Screen.Checkout) }, 
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        )
                        
                        Spacer(Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            AppIcon(AppIcons.Shield, modifier = Modifier.size(16.dp), tint = AdaptiveTheme.colors.textSecondary)
                            Spacer(Modifier.width(8.dp))
                            Text("Secure Checkout", color = AdaptiveTheme.colors.textSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if (isBold) AdaptiveTheme.colors.textPrimary else AdaptiveTheme.colors.textSecondary, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = AdaptiveTheme.colors.textPrimary, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun IconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color = Color.Black, onClick: () -> Unit) {
    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(4.dp)).clickable { onClick() }, contentAlignment = Alignment.Center) {
        AppIcon(icon, modifier = Modifier.size(16.dp), tint = tint)
    }
}

@Composable
private fun CartProductVisual(
    product: io.github.adaptivekt.examples.ecommerce.model.Product,
    size: androidx.compose.ui.unit.Dp,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(AdaptiveTheme.colors.surfaceMuted),
        contentAlignment = Alignment.Center,
    ) {
        ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun QuantityStepper(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(AdaptiveTheme.colors.surfaceMuted, RoundedCornerShape(8.dp))
            .padding(4.dp),
    ) {
        IconButton(AppIcons.Minus, tint = AdaptiveTheme.colors.textPrimary, onClick = onDecrease)
        Text(
            "$quantity",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold,
            color = AdaptiveTheme.colors.textPrimary,
        )
        IconButton(AppIcons.Plus, tint = AdaptiveTheme.colors.textPrimary, onClick = onIncrease)
    }
}

@Composable
fun CheckoutScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveScrollablePage(modifier = modifier.fillMaxSize()) {
        Spacer(Modifier.height(12.dp))
        Text("Checkout", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
        Spacer(Modifier.height(32.dp))
        
        AdaptiveTwoPane(
            primaryWeight = 1.5f,
            secondaryWeight = 1f,
            gap = 48.dp,
            primary = {
                AdaptiveFormLayout {
                    section(title = "Contact Information") {
                        field(label = "Email Address", required = true) {
                            AdaptiveTextField(value = "", onValueChange = {}, placeholder = "alex@example.com")
                        }
                    }
                    
                    section(title = "Shipping Address") {
                        field(label = "Full Name", required = true) {
                            AdaptiveTextField(value = "", onValueChange = {}, placeholder = "Alex Developer")
                        }
                        field(label = "Street Address", required = true) {
                            AdaptiveTextField(value = "", onValueChange = {}, placeholder = "123 Tech Lane")
                        }
                        field(label = "City", span = FieldSpan.Half) { 
                            AdaptiveTextField(value = "", onValueChange = {}, placeholder = "Innovation") 
                        }
                        field(label = "ZIP Code", span = FieldSpan.Half) { 
                            AdaptiveTextField(value = "", onValueChange = {}, placeholder = "12345") 
                        }
                    }
                    
                    section(title = "Payment Method (Mock)") {
                        field(label = "Payment") {
                            AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF3B82F6)))
                                    Spacer(Modifier.width(16.dp))
                                    Text("Credit Card (Demo Only)", fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.weight(1f))
                                    Text("**** 4242", color = AdaptiveTheme.colors.textSecondary)
                                }
                            }
                        }
                    }
                }
            },
            secondary = {
                AdaptiveCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(24.dp)) {
                    Column {
                        Text("Your Order", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(24.dp))
                        
                        state.cartItems.take(3).forEach { item ->
                            val product = MockData.products.find { it.id == item.productId }
                            if (product != null) {
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF1F5F9)), contentAlignment = Alignment.Center) {
                                        ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                        Text("Qty: ${item.quantity}", fontSize = 12.sp, color = AdaptiveTheme.colors.textSecondary)
                                    }
                                    Text("\$${(product.price * item.quantity).toPriceString()}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)).padding(vertical = 16.dp))
                        
                        SummaryRow("Subtotal", "\$${state.cartSubtotal().toPriceString()}")
                        SummaryRow("Shipping", "FREE")
                        SummaryRow("Tax", "\$${(state.cartSubtotal() * 0.1).toPriceString()}")
                        
                        Spacer(Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            Text("\$${state.cartTotal().toPriceString()}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF3B82F6))
                        }
                        
                        Spacer(Modifier.height(32.dp))
                        AdaptiveButton(
                            text = "Place Order", 
                            onClick = { 
                                val id = state.placeOrder()
                                state.navigateTo(Screen.OrderSuccess(id))
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun OrderSuccessScreen(state: StoreState, orderId: String, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(max = 500.dp).padding(32.dp)) {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFDCFCE7)), 
                    contentAlignment = Alignment.Center
                ) {
                    AppIcon(AppIcons.Check, modifier = Modifier.size(40.dp), tint = Color(0xFF16A34A))
                }
                Spacer(Modifier.height(32.dp))
                Text("Order confirmed!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                Spacer(Modifier.height(16.dp))
                Text("Thank you for your purchase. Your order #$orderId has been placed and will be shipped soon.", textAlign = TextAlign.Center, color = AdaptiveTheme.colors.textSecondary, lineHeight = 24.sp)
                Spacer(Modifier.height(48.dp))
                AdaptiveButton(
                    text = "Track your order", 
                    onClick = { state.navigateTo(Screen.Orders) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                AdaptiveButton(
                    text = "Continue Shopping", 
                    onClick = { state.resetToHome() },
                    variant = AdaptiveButtonVariant.Ghost,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun Double.toPriceString(): String {
    val rounded = (this * 100).toInt() / 100.0
    val s = rounded.toString()
    return if (s.contains(".")) {
        val parts = s.split(".")
        if (parts[1].length == 1) s + "0" else parts[0] + "." + parts[1].take(2)
    } else {
        "$s.00"
    }
}
