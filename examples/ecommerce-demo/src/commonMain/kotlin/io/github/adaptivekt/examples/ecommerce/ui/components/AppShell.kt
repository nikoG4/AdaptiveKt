package io.github.adaptivekt.examples.ecommerce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveTextField

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
            is Screen.Products, is Screen.ProductDetail -> "shop"
            is Screen.Wishlist -> "wishlist"
            is Screen.Cart, is Screen.Checkout, is Screen.OrderSuccess -> "cart"
            is Screen.Account, is Screen.Orders, is Screen.Settings -> "account"
            else -> "home"
        }

        AdaptiveNavigationScaffold(
            navItems = listOf(
                AdaptiveNavItem("home", "Home", icon = { AppIcon(AppIcons.Home, tint = Color.Unspecified) }),
                AdaptiveNavItem("shop", "Shop", icon = { AppIcon(AppIcons.Search, tint = Color.Unspecified) }),
                AdaptiveNavItem("wishlist", "Saved", icon = { AppIcon(AppIcons.Heart, tint = Color.Unspecified) }),
                AdaptiveNavItem("cart", "Cart", icon = { 
                    Box {
                        AppIcon(AppIcons.ShoppingBag, tint = Color.Unspecified)
                        if (state.cartItems.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .offset(x = 12.dp, y = (-4).dp)
                                    .background(Color(0xFFEF4444), CircleShape),
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
                AdaptiveNavItem("account", "Me", icon = { AppIcon(AppIcons.User, tint = Color.Unspecified) })
            ),
            selectedItemId = selectedId,
            onItemSelected = { id ->
                when (id) {
                    "home" -> state.navigateTo(Screen.Home)
                    "shop" -> state.navigateTo(Screen.Products)
                    "wishlist" -> state.navigateTo(Screen.Wishlist)
                    "cart" -> state.navigateTo(Screen.Cart)
                    "account" -> if (state.isLoggedIn) state.navigateTo(Screen.Account) else state.navigateTo(Screen.AuthLogin)
                }
            },
            preferBottomNavigationOnCompact = true,
            topBar = {
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val compact = maxWidth < 800.dp
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .background(Color.White)
                            .padding(horizontal = if (compact) 16.dp else 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo
                        Row(
                            modifier = Modifier
                                .clickable { state.resetToHome() }
                                .padding(end = if (compact) 16.dp else 32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFF3B82F6), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("A", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Adaptive Store", 
                                fontSize = if (compact) 18.sp else 20.sp, 
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        if (!compact) {
                            // Desktop Search
                            Box(modifier = Modifier.weight(2f).padding(horizontal = 32.dp)) {
                                AdaptiveTextField(
                                    value = state.searchQuery,
                                    onValueChange = { state.searchQuery = it },
                                    placeholder = "Search products...",
                                    modifier = Modifier.widthIn(max = 500.dp)
                                )
                            }
                            
                            // Action Icons
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (state.isLoggedIn) {
                                    Row(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable { state.navigateTo(Screen.Account) }
                                            .background(Color(0xFFF1F5F9))
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF3B82F6)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                state.currentUser?.name?.take(1)?.uppercase() ?: "U", 
                                                color = Color.White, 
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            state.currentUser?.name?.split(" ")?.first() ?: "User",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else {
                                    AdaptiveButton(
                                        text = "Sign In",
                                        onClick = { state.navigateTo(Screen.AuthLogin) },
                                        variant = AdaptiveButtonVariant.Ghost
                                    )
                                }
                            }
                        } else {
                            // On mobile, just show an icon for search if needed, or let bottom nav handle it
                            if (!state.isLoggedIn) {
                                AdaptiveButton(
                                    text = "Sign In",
                                    onClick = { state.navigateTo(Screen.AuthLogin) },
                                    variant = AdaptiveButtonVariant.Ghost
                                )
                            }
                        }
                    }
                }
            },
            content = content
        )
    }
}
