package io.github.adaptivekt.examples.ecommerce.navigation

sealed class Screen {
    object AuthLogin : Screen()
    object AuthRegister : Screen()
    object AuthForgotPassword : Screen()
    
    object Home : Screen()
    object Products : Screen()
    data class ProductDetail(val productId: String) : Screen()
    
    object Wishlist : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    data class OrderSuccess(val orderId: String) : Screen()
    
    object Account : Screen()
    object Orders : Screen()
    object Settings : Screen()
    
    object UiStates : Screen()
}
