package io.github.adaptivekt.examples.ecommerce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.navigation.AdaptiveNavigationScaffold
import io.github.adaptivekt.navigation.AdaptiveNavItem

@Composable
fun AppShell(
    state: StoreState,
    content: @Composable (PaddingValues) -> Unit
) {
    val isAuthScreen = state.currentScreen is Screen.AuthLogin || 
                       state.currentScreen is Screen.AuthRegister || 
                       state.currentScreen is Screen.AuthForgotPassword

    if (isAuthScreen) {
        Box(Modifier.fillMaxSize()) {
            content(PaddingValues())
        }
    } else {
        val selectedId = when (state.currentScreen) {
            is Screen.Home -> "home"
            is Screen.Products, is Screen.ProductDetail -> "products"
            is Screen.Cart, is Screen.Checkout, is Screen.OrderSuccess -> "cart"
            is Screen.Account, is Screen.Orders, is Screen.Settings, is Screen.Wishlist -> "account"
            else -> "home"
        }

        AdaptiveNavigationScaffold(
            navItems = listOf(
                AdaptiveNavItem("home", "Home", icon = { Text("🏠", fontSize = 20.sp) }),
                AdaptiveNavItem("products", "Products", icon = { Text("📋", fontSize = 20.sp) }),
                AdaptiveNavItem("cart", "Cart", icon = { 
                    Box {
                        Text("🛒", fontSize = 20.sp)
                        if (state.cartItems.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .offset(x = 12.dp, y = (-4).dp)
                                    .background(Color.Red, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${state.cartItems.sumOf { it.quantity }}", 
                                    color = Color.White, 
                                    fontSize = 10.sp, 
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }),
                AdaptiveNavItem("account", "Account", icon = { Text("👤", fontSize = 20.sp) })
            ),
            selectedItemId = selectedId,
            onItemSelected = { id ->
                when (id) {
                    "home" -> state.navigateTo(Screen.Home)
                    "products" -> state.navigateTo(Screen.Products)
                    "cart" -> state.navigateTo(Screen.Cart)
                    "account" -> if (state.isLoggedIn) state.navigateTo(Screen.Account) else state.navigateTo(Screen.AuthLogin)
                }
            },
            preferBottomNavigationOnCompact = true,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Adaptive Store", 
                        fontSize = 20.sp, 
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.isLoggedIn) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(state.currentUser?.name?.take(1)?.uppercase() ?: "U", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            io.github.adaptivekt.components.AdaptiveButton(
                                text = "Sign In",
                                onClick = { state.navigateTo(Screen.AuthLogin) },
                                variant = io.github.adaptivekt.components.AdaptiveButtonVariant.Ghost
                            )
                        }
                    }
                }
            },
            content = content
        )
    }
}
