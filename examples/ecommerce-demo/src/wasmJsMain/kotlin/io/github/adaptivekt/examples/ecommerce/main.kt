package io.github.adaptivekt.examples.ecommerce

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.examples.ecommerce.model.MockData

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("APP_STARTED")
    
    val storeState = StoreState()
    var isNavigatingFromHistory = false
    
    // Support deep linking for tests
    val path = window.location.pathname
    if (path.contains("/shop")) storeState.navigateTo(Screen.Products, false)
    else if (path.contains("/cart")) {
        seedCartForDirectRoute(storeState)
        storeState.navigateTo(Screen.Cart, false)
    }
    else if (path.contains("/checkout")) {
        seedCartForDirectRoute(storeState)
        storeState.navigateTo(Screen.Checkout, false)
    }
    else if (path.contains("/login")) storeState.navigateTo(Screen.AuthLogin, false)
    else if (path.contains("/product")) {
        storeState.navigateTo(Screen.ProductDetail(io.github.adaptivekt.examples.ecommerce.model.MockData.products.first().id), false)
    }

    // Sync browser history with app state
    storeState.onNavigate = { screen ->
        if (!isNavigatingFromHistory) {
            val newPath = when (screen) {
                is Screen.Home -> "/"
                is Screen.Products -> "/shop"
                is Screen.ProductDetail -> "/product/${screen.productId}"
                is Screen.Cart -> "/cart"
                is Screen.Checkout -> "/checkout"
                is Screen.Account -> "/account"
                else -> "/${screen::class.simpleName?.lowercase() ?: ""}"
            }
            window.history.pushState(null, "", newPath)
        }
    }
    
    window.onpopstate = {
        isNavigatingFromHistory = true
        if (storeState.canGoBack) {
            storeState.goBack()
        } else {
            // Reached root
            storeState.resetToHome()
        }
        isNavigatingFromHistory = false
        null
    }

    ComposeViewport(document.body!!) {
        EcommerceApp(storeState)
    }
}

private fun seedCartForDirectRoute(storeState: StoreState) {
    if (storeState.cartItems.isNotEmpty()) return
    val product = MockData.products.firstOrNull() ?: return
    storeState.addToCart(
        productId = product.id,
        variantId = product.variants.firstOrNull()?.id,
        quantity = 1,
    )
}
