package io.github.adaptivekt.examples.ecommerce.model

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconSymbol: String,
    val imageUrl: String? = null
)

data class Collection(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String? = null
)

data class ProductVariant(
    val id: String,
    val name: String,
    val type: String // "Color", "Size", "Storage"
)

data class ProductSpec(
    val name: String,
    val value: String
)

data class Product(
    val id: String,
    val name: String,
    val categoryId: String,
    val collectionId: String? = null,
    val shortDescription: String,
    val longDescription: String,
    val price: Double,
    val oldPrice: Double? = null,
    val discountPercent: Int? = null,
    val rating: Double,
    val reviewCount: Int,
    val stock: Int,
    val isNew: Boolean = false,
    val isSale: Boolean = false,
    val isFeatured: Boolean = false,
    val variants: List<ProductVariant> = emptyList(),
    val tags: List<String> = emptyList(),
    val specs: List<ProductSpec> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val accentColorHex: String? = null,
    val shippingLabel: String = "Standard Shipping"
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

data class Promo(
    val id: String,
    val title: String,
    val subtitle: String,
    val discountText: String,
    val imageUrl: String? = null,
    val actionText: String = "Shop Now"
)
