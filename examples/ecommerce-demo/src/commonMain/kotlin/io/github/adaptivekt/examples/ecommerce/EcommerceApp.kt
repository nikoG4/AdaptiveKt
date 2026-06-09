package io.github.adaptivekt.examples.ecommerce

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
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
import io.github.adaptivekt.examples.ecommerce.ui.products.WishlistScreen
import io.github.adaptivekt.examples.ecommerce.ui.cart.CartScreen
import io.github.adaptivekt.examples.ecommerce.ui.account.AccountScreen
import io.github.adaptivekt.examples.ecommerce.ui.auth.ForgotPasswordScreen
import io.github.adaptivekt.examples.ecommerce.ui.account.OrdersScreen
import io.github.adaptivekt.examples.ecommerce.ui.account.SettingsScreen
import io.github.adaptivekt.examples.ecommerce.ui.cart.CheckoutScreen
import io.github.adaptivekt.examples.ecommerce.ui.cart.OrderSuccessScreen
import io.github.adaptivekt.examples.ecommerce.ui.states.UiStatesScreen

import io.github.adaptivekt.navigation.rememberAdaptiveNavigator
import io.github.adaptivekt.navigation.AdaptiveNavigationOptions
import io.github.adaptivekt.navigation.AdaptiveWebNavigationMode
import io.github.adaptivekt.examples.ecommerce.navigation.StoreRouteCodec

@Composable
fun EcommerceApp(storeState: StoreState = remember { StoreState() }) {
    val navigator = rememberAdaptiveNavigator(
        initialRoute = Screen.Home,
        codec = StoreRouteCodec,
        options = AdaptiveNavigationOptions(
            webMode = AdaptiveWebNavigationMode.Hash
        )
    )
    
    LaunchedEffect(navigator) {
        storeState.navigator = navigator
    }
    
    LaunchedEffect(navigator.currentRoute) {
        val route = navigator.currentRoute
        if (route is Screen.Cart || route is Screen.Checkout) {
            if (storeState.cartItems.isEmpty()) {
                val product = io.github.adaptivekt.examples.ecommerce.model.MockData.products.firstOrNull()
                if (product != null) {
                    storeState.addToCart(
                        productId = product.id,
                        variantId = product.variants.firstOrNull()?.id,
                        quantity = 1
                    )
                }
            }
        }
    }

    io.github.adaptivekt.core.AdaptiveApp {
        AdaptiveTheme(mode = storeState.themeMode) {
            AppShell(state = storeState) { paddingValues ->
                val modifier = Modifier.padding(paddingValues)
                when (val screen = storeState.currentScreen) {
                    is Screen.AuthLogin -> LoginScreen(storeState, modifier)
                    is Screen.AuthRegister -> RegisterScreen(storeState, modifier)
                    is Screen.AuthForgotPassword -> ForgotPasswordScreen(storeState, modifier)
                    
                    is Screen.Home -> HomeScreen(storeState, modifier)
                    is Screen.Products -> ProductListScreen(storeState, modifier)
                    is Screen.ProductDetail -> ProductDetailScreen(storeState, screen.productId, modifier)
                    
                    is Screen.Wishlist -> WishlistScreen(storeState, modifier)
                    is Screen.Cart -> CartScreen(storeState, modifier)
                    is Screen.Checkout -> CheckoutScreen(storeState, modifier)
                    is Screen.OrderSuccess -> OrderSuccessScreen(storeState, screen.orderId, modifier)
                    
                    is Screen.Account -> AccountScreen(storeState, modifier)
                    is Screen.Orders -> OrdersScreen(storeState, modifier)
                    is Screen.Settings -> SettingsScreen(storeState, modifier)
                    
                    is Screen.UiStates -> UiStatesScreen(storeState, modifier)
                }
            }
        }
    }
}
