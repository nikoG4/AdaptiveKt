package io.github.adaptivekt.examples.ecommerce.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.derivedStateOf
import io.github.adaptivekt.core.AdaptiveThemeMode
import io.github.adaptivekt.navigation.AdaptiveNavigator
import io.github.adaptivekt.examples.ecommerce.model.*
import io.github.adaptivekt.examples.ecommerce.navigation.Screen

class StoreState {
    var navigator: AdaptiveNavigator<Screen>? = null
        
    val currentScreen: Screen
        get() = navigator?.currentRoute ?: Screen.Home
        
    val canGoBack: Boolean
        get() = navigator?.canGoBack ?: false
        
    var isLoggedIn by mutableStateOf(false)
        private set
    var currentUser by mutableStateOf<UserProfile?>(null)
        private set
        
    var searchQuery by mutableStateOf("")
    var selectedCategoryId by mutableStateOf<String?>(null)
    var selectedCollectionId by mutableStateOf<String?>(null)
    var selectedTags = mutableStateListOf<String>()
    var sortOption by mutableStateOf("Popular")
    var themeMode by mutableStateOf(AdaptiveThemeMode.System)
    
    val wishlistIds = mutableStateListOf<String>()
    var cartItems = mutableStateListOf<CartItem>()
        private set
        
    var onNavigate: ((Screen) -> Unit)? = null
        
    fun navigateTo(screen: Screen, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            navigator?.navigate(screen)
        } else {
            navigator?.replace(screen)
        }
        onNavigate?.invoke(screen)
    }
    
    fun goBack() {
        navigator?.goBack()
        onNavigate?.invoke(currentScreen)
    }
    
    fun resetToHome() {
        navigator?.navigate(Screen.Home)
        onNavigate?.invoke(Screen.Home)
    }
    
    fun loginDemo() {
        isLoggedIn = true
        currentUser = MockData.mockUser
        navigateTo(Screen.Home)
    }
    
    fun registerDemo() {
        isLoggedIn = true
        currentUser = MockData.mockUser.copy(name = "New User", email = "demo@adaptivestore.dev")
        navigateTo(Screen.Home)
    }
    
    fun logout() {
        isLoggedIn = false
        currentUser = null
        navigateTo(Screen.AuthLogin)
    }
    
    fun addToCart(productId: String, variantId: String?, quantity: Int) {
        val existing = cartItems.find { it.productId == productId && it.selectedVariantId == variantId }
        if (existing != null) {
            val index = cartItems.indexOf(existing)
            cartItems[index] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            cartItems.add(CartItem(productId, quantity, variantId))
        }
    }
    
    fun removeFromCart(productId: String) {
        cartItems.removeAll { it.productId == productId }
    }
    
    fun updateCartQuantity(productId: String, quantity: Int) {
        val existing = cartItems.find { it.productId == productId }
        if (existing != null) {
            val index = cartItems.indexOf(existing)
            if (quantity <= 0) {
                cartItems.removeAt(index)
            } else {
                cartItems[index] = existing.copy(quantity = quantity)
            }
        }
    }
    
    fun toggleWishlist(productId: String) {
        if (wishlistIds.contains(productId)) {
            wishlistIds.remove(productId)
        } else {
            wishlistIds.add(productId)
        }
    }
    
    fun clearFilters() {
        searchQuery = ""
        selectedCategoryId = null
        selectedCollectionId = null
        selectedTags.clear()
    }
    
    fun filteredProducts(): List<Product> {
        var list = MockData.products
        if (searchQuery.isNotBlank()) {
            list = list.filter { it.name.contains(searchQuery, ignoreCase = true) || it.shortDescription.contains(searchQuery, ignoreCase = true) }
        }
        if (selectedCategoryId != null) {
            list = list.filter { it.categoryId == selectedCategoryId }
        }
        if (selectedCollectionId != null) {
            list = list.filter { it.collectionId == selectedCollectionId }
        }
        if (selectedTags.isNotEmpty()) {
            list = list.filter { it.tags.any { tag -> selectedTags.contains(tag) } }
        }
        list = when (sortOption) {
            "Price: Low to High" -> list.sortedBy { it.price }
            "Price: High to Low" -> list.sortedByDescending { it.price }
            "Newest" -> list.sortedByDescending { it.isNew }
            "Best Rated" -> list.sortedByDescending { it.rating }
            else -> list
        }
        return list
    }
    
    fun cartSubtotal(): Double {
        return cartItems.sumOf { item ->
            val product = MockData.products.find { it.id == item.productId }
            (product?.price ?: 0.0) * item.quantity
        }
    }
    
    fun cartTotal(): Double {
        return cartSubtotal() * 1.1 // Add 10% mock tax
    }
    
    fun clearCart() {
        cartItems.clear()
    }
    
    fun placeOrder(): String {
        val orderId = "ORD-" + (1000..9999).random()
        clearCart()
        return orderId
    }
}
