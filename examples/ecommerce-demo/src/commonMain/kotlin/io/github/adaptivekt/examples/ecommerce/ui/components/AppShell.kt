package io.github.adaptivekt.examples.ecommerce.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                AdaptiveNavItem("home", "Home", icon = { Text("🏠") }),
                AdaptiveNavItem("products", "Products", icon = { Text("📋") }),
                AdaptiveNavItem("cart", "Cart", icon = { Text("🛒") }),
                AdaptiveNavItem("account", "Account", icon = { Text("👤") })
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
            content = content
        )
    }
}
