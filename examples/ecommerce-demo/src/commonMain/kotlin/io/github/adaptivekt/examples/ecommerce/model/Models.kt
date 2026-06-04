package io.github.adaptivekt.examples.ecommerce.model

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconSymbol: String
)

data class ProductVariant(
    val id: String,
    val name: String,
    val type: String // "Color", "Size", "Storage"
)

data class Product(
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Double,
    val reviewCount: Int,
    val stock: Int,
    val isNew: Boolean = false,
    val isSale: Boolean = false,
    val variants: List<ProductVariant> = emptyList(),
    val tags: List<String> = emptyList(),
    val accentColorHex: String? = null
)

data class CartItem(
    val productId: String,
    val quantity: Int,
    val selectedVariantId: String? = null
)

enum class OrderStatus {
    Processing, Shipped, Delivered, Cancelled
}

data class Order(
    val id: String,
    val dateString: String,
    val total: Double,
    val status: OrderStatus,
    val items: List<CartItem>
)

data class UserProfile(
    val name: String,
    val email: String,
    val tier: String = "Member" // "Member", "Pro", "Elite"
)
