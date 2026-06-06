package io.github.adaptivekt.examples.ecommerce.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.FieldSpan
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcons
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcon
import io.github.adaptivekt.examples.ecommerce.ui.components.ProductVisual

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

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compact = maxWidth < 800.dp
            val sectionPadding = if (compact) 16.dp else 24.dp

            if (compact) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(sectionPadding),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Text("My Bag", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        Spacer(Modifier.height(8.dp))
                        Text("${state.cartItems.sumOf { it.quantity }} items", color = Color(0xFF64748B), fontSize = 14.sp)
                    }

                    state.cartItems.forEach { item ->
                        val product = MockData.products.find { it.id == item.productId }
                        if (product != null) {
                            item {
                                AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFF1F5F9)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                            }
                                            Spacer(Modifier.width(16.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F172A))
                                                Spacer(Modifier.height(4.dp))
                                                Text(MockData.categories.find { it.id == product.categoryId }?.name ?: "", color = Color(0xFF64748B), fontSize = 14.sp)
                                                Spacer(Modifier.height(8.dp))
                                                Text("\$${product.price}", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            // Quantity Stepper
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)).padding(4.dp)) {
                                                IconButton(AppIcons.Minus) { state.updateCartQuantity(product.id, item.quantity - 1) }
                                                Text("${item.quantity}", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold)
                                                IconButton(AppIcons.Plus) { state.updateCartQuantity(product.id, item.quantity + 1) }
                                            }
                                            IconButton(AppIcons.Trash, tint = Color(0xFFEF4444)) { state.removeFromCart(product.id) }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        AdaptiveCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
                            Column {
                                Text("Order Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Spacer(Modifier.height(16.dp))
                                
                                SummaryRow("Subtotal", "\$${state.cartSubtotal().toPriceString()}")
                                SummaryRow("Shipping", "Calculated at checkout")
                                SummaryRow("Tax", "\$${(state.cartSubtotal() * 0.1).toPriceString()}")
                                
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)).padding(vertical = 16.dp))
                                
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Total", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF0F172A))
                                    Text("\$${state.cartTotal().toPriceString()}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF3B82F6))
                                }
                                
                                Spacer(Modifier.height(24.dp))
                                AdaptiveButton(
                                    text = "Checkout", 
                                    onClick = { state.navigateTo(Screen.Checkout) }, 
                                    modifier = Modifier.fillMaxWidth().height(56.dp)
                                )
                                
                                Spacer(Modifier.height(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                    AppIcon(AppIcons.Shield, modifier = Modifier.size(16.dp), tint = Color(0xFF64748B))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Secure Checkout", color = Color(0xFF64748B), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                    // Cart Items Desktop
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.weight(1.5f)) {
                        item {
                            Text("Shopping Bag", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                            Spacer(Modifier.height(8.dp))
                            Text("${state.cartItems.sumOf { it.quantity }} items in your bag", color = Color(0xFF64748B), fontSize = 14.sp)
                        }

                        state.cartItems.forEach { item ->
                            val product = MockData.products.find { it.id == item.productId }
                            if (product != null) {
                                item {
                                    AdaptiveCard(modifier = Modifier.fillMaxWidth()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFF1F5F9)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                            }
                                            Spacer(Modifier.width(24.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
                                                Spacer(Modifier.height(4.dp))
                                                Text(MockData.categories.find { it.id == product.categoryId }?.name ?: "", color = Color(0xFF64748B), fontSize = 14.sp)
                                                Spacer(Modifier.height(8.dp))
                                                Text("\$${product.price}", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                                            }
                                            
                                            // Quantity Stepper
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)).padding(4.dp)) {
                                                IconButton(AppIcons.Minus) { state.updateCartQuantity(product.id, item.quantity - 1) }
                                                Text("${item.quantity}", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold)
                                                IconButton(AppIcons.Plus) { state.updateCartQuantity(product.id, item.quantity + 1) }
                                            }
                                            
                                            Spacer(Modifier.width(24.dp))
                                            IconButton(AppIcons.Trash, tint = Color(0xFFEF4444)) { state.removeFromCart(product.id) }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Order Summary Desktop
                    Column(modifier = Modifier.weight(1f)) {
                        AdaptiveCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(24.dp)) {
                            Column {
                                Text("Order Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Spacer(Modifier.height(24.dp))
                                
                                SummaryRow("Subtotal", "\$${state.cartSubtotal().toPriceString()}")
                                SummaryRow("Shipping", "Calculated at checkout")
                                SummaryRow("Tax", "\$${(state.cartSubtotal() * 0.1).toPriceString()}")
                                
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)).padding(vertical = 24.dp))
                                
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Total", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Color(0xFF0F172A))
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
                                    AppIcon(AppIcons.Shield, modifier = Modifier.size(16.dp), tint = Color(0xFF64748B))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Secure Checkout", color = Color(0xFF64748B), fontSize = 12.sp)
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
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if (isBold) Color(0xFF0F172A) else Color(0xFF64748B), fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = Color(0xFF0F172A), fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun IconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color = Color.Black, onClick: () -> Unit) {
    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(4.dp)).clickable { onClick() }, contentAlignment = Alignment.Center) {
        AppIcon(icon, modifier = Modifier.size(16.dp), tint = tint)
    }
}

@Composable
fun CheckoutScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compact = maxWidth < 800.dp
            val sectionPadding = if (compact) 16.dp else 24.dp

            if (compact) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(sectionPadding), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    item {
                        Text("Checkout", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    }
                    
                    item {
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
                                            Text("Credit Card", fontWeight = FontWeight.Bold)
                                            Spacer(Modifier.weight(1f))
                                            Text("**** 4242", color = Color(0xFF64748B))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        AdaptiveCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
                            Column {
                                Text("Your Order", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Spacer(Modifier.height(16.dp))
                                
                                state.cartItems.take(3).forEach { item ->
                                    val product = MockData.products.find { it.id == item.productId }
                                    if (product != null) {
                                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF1F5F9)), contentAlignment = Alignment.Center) {
                                                ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                            }
                                            Spacer(Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                                Text("Qty: ${item.quantity}", fontSize = 12.sp, color = Color(0xFF64748B))
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
                                
                                Spacer(Modifier.height(24.dp))
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
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    Text("Checkout", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(Modifier.height(32.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                        // Checkout Form
                        Column(modifier = Modifier.weight(1.5f)) {
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
                                                Text("**** 4242", color = Color(0xFF64748B))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Sidebar Summary
                        Column(modifier = Modifier.weight(1f)) {
                            AdaptiveCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(24.dp)) {
                                Column {
                                    Text("Your Order", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
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
                                                    Text("Qty: ${item.quantity}", fontSize = 12.sp, color = Color(0xFF64748B))
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
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(max = 500.dp).padding(32.dp)) {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFDCFCE7)), 
                    contentAlignment = Alignment.Center
                ) {
                    AppIcon(AppIcons.Check, modifier = Modifier.size(40.dp), tint = Color(0xFF16A34A))
                }
                Spacer(Modifier.height(32.dp))
                Text("Order confirmed!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                Spacer(Modifier.height(16.dp))
                Text("Thank you for your purchase. Your order #$orderId has been placed and will be shipped soon.", textAlign = TextAlign.Center, color = Color(0xFF64748B), lineHeight = 24.sp)
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
