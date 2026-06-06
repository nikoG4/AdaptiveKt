package io.github.adaptivekt.examples.ecommerce.navigation

import io.github.adaptivekt.navigation.AdaptiveRouteCodec

object StoreRouteCodec : AdaptiveRouteCodec<Screen> {
    override fun encode(route: Screen): String = when(route) {
        is Screen.Home -> "/"
        is Screen.Products -> "/shop"
        is Screen.ProductDetail -> "/product/${route.productId}"
        is Screen.Cart -> "/cart"
        is Screen.Checkout -> "/checkout"
        is Screen.Account -> "/account"
        is Screen.AuthLogin -> "/login"
        is Screen.AuthRegister -> "/register"
        is Screen.AuthForgotPassword -> "/forgot-password"
        is Screen.Wishlist -> "/wishlist"
        is Screen.OrderSuccess -> "/order-success/${route.orderId}"
        is Screen.Orders -> "/orders"
        is Screen.Settings -> "/settings"
        is Screen.UiStates -> "/ui-states"
    }

    override fun decode(path: String): Screen? {
        val normalized = if (path.startsWith("/")) path else "/$path"
        
        return when {
            normalized == "/" -> Screen.Home
            normalized == "/shop" -> Screen.Products
            normalized == "/cart" -> Screen.Cart
            normalized == "/checkout" -> Screen.Checkout
            normalized == "/account" -> Screen.Account
            normalized == "/login" -> Screen.AuthLogin
            normalized == "/register" -> Screen.AuthRegister
            normalized == "/forgot-password" -> Screen.AuthForgotPassword
            normalized == "/wishlist" -> Screen.Wishlist
            normalized == "/orders" -> Screen.Orders
            normalized == "/settings" -> Screen.Settings
            normalized == "/ui-states" -> Screen.UiStates
            normalized.startsWith("/product/") -> {
                val id = normalized.substringAfter("/product/")
                if (id.isNotEmpty()) Screen.ProductDetail(id) else null
            }
            normalized.startsWith("/order-success/") -> {
                val id = normalized.substringAfter("/order-success/")
                if (id.isNotEmpty()) Screen.OrderSuccess(id) else null
            }
            else -> null
        }
    }
}
