package io.github.adaptivekt.examples.ecommerce

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.examples.ecommerce.ui.components.AppShell

import io.github.adaptivekt.examples.ecommerce.ui.auth.LoginScreen
import io.github.adaptivekt.examples.ecommerce.ui.auth.RegisterScreen
import io.github.adaptivekt.examples.ecommerce.ui.home.HomeScreen
import io.github.adaptivekt.examples.ecommerce.ui.products.ProductListScreen
import io.github.adaptivekt.examples.ecommerce.ui.products.ProductDetailScreen
import io.github.adaptivekt.examples.ecommerce.ui.cart.CartScreen
import io.github.adaptivekt.examples.ecommerce.ui.account.AccountScreen

@Composable
fun EcommerceApp() {
    val storeState = remember { StoreState() }

    AdaptiveTheme {
        AppShell(state = storeState) { paddingValues ->
            val modifier = Modifier.padding(paddingValues)
            when (val screen = storeState.currentScreen) {
                is Screen.AuthLogin -> LoginScreen(storeState, modifier)
                is Screen.AuthRegister -> RegisterScreen(storeState, modifier)
                is Screen.AuthForgotPassword -> LoginScreen(storeState, modifier) // Placeholder
                
                is Screen.Home -> HomeScreen(storeState, modifier)
                is Screen.Products -> ProductListScreen(storeState, modifier)
                is Screen.ProductDetail -> ProductDetailScreen(storeState, screen.productId, modifier)
                
                is Screen.Wishlist -> AccountScreen(storeState, modifier) // Placeholder
                is Screen.Cart -> CartScreen(storeState, modifier)
                is Screen.Checkout -> CartScreen(storeState, modifier) // Placeholder
                is Screen.OrderSuccess -> CartScreen(storeState, modifier) // Placeholder
                
                is Screen.Account -> AccountScreen(storeState, modifier)
                is Screen.Orders -> AccountScreen(storeState, modifier) // Placeholder
                is Screen.Settings -> AccountScreen(storeState, modifier) // Placeholder
                
                is Screen.UiStates -> HomeScreen(storeState, modifier) // Placeholder
            }
        }
    }
}
