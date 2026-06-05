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
    var isNavigatingFromHistory = false
    
    // Sync browser history with app state
    storeState.onNavigate = { screen ->
        if (!isNavigatingFromHistory) {
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
