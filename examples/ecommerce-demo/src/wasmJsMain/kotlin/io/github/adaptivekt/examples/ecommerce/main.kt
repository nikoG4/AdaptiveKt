package io.github.adaptivekt.examples.ecommerce

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("APP_STARTED")
    
    val storeState = StoreState()
    
    // Sync browser history with app state
    storeState.onNavigate = { screen ->
        val path = when (screen) {
            is Screen.Home -> "/"
            is Screen.Products -> "/shop"
            is Screen.ProductDetail -> "/product/${screen.productId}"
            is Screen.Cart -> "/cart"
            is Screen.Checkout -> "/checkout"
            is Screen.Account -> "/account"
            else -> "/${screen::class.simpleName?.lowercase() ?: ""}"
        }
        window.history.pushState(null, "", path)
    }
    
    window.onpopstate = {
        if (storeState.canGoBack) {
            storeState.goBack()
        } else {
            // Handle root level back
        }
    }

    ComposeViewport(document.body!!) {
        EcommerceApp(storeState)
    }
}
