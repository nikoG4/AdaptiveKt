package io.github.adaptivekt.examples.ecommerce.model

object MockData {
    val categories = listOf(
        Category("cat1", "Laptops", "High performance notebooks", "💻"),
        Category("cat2", "Phones", "Latest smartphones", "📱"),
        Category("cat3", "Audio", "Headphones and speakers", "🎧"),
        Category("cat4", "Gaming", "Consoles and accessories", "🎮"),
        Category("cat5", "Smart Home", "Connected home devices", "🏠"),
        Category("cat6", "Cameras", "Photography and video", "📷"),
        Category("cat7", "Wearables", "Watches and trackers", "⌚"),
        Category("cat8", "Accessories", "Cables, chargers, cases", "🔌")
    )
    
    val tags = listOf("Pro", "Wireless", "4K", "Noise Cancelling", "Portable", "Waterproof")
    
    // Let's generate 32 products
    val products = (1..32).map { i ->
        val cat = categories[i % categories.size]
        val isSale = i % 5 == 0
        val isNew = i % 7 == 0
        val price = 50.0 + (i * 30.5)
        Product(
            id = "prod_$i",
            name = "${cat.name.split(" ").first()} $i",
            categoryId = cat.id,
            description = "This is a premium ${cat.name.lowercase()} that offers amazing performance and great value. Ideal for both everyday use and professional environments.",
            price = price,
            oldPrice = if (isSale) price * 1.2 else null,
            rating = 3.5 + ((i % 15) / 10.0),
            reviewCount = i * 14,
            stock = if (i % 8 == 0) 2 else 50,
            isNew = isNew,
            isSale = isSale,
            variants = listOf(
                ProductVariant("v1", "Black", "Color"),
                ProductVariant("v2", "White", "Color")
            ),
            tags = listOf(tags[i % tags.size]),
            accentColorHex = null
        )
    }
    
    val mockUser = UserProfile(
        name = "Alex Developer",
        email = "alex@adaptivestore.dev",
        tier = "Pro"
    )
    
    val mockOrders = listOf(
        Order("ORD-1029", "2026-06-01", 1250.00, OrderStatus.Processing, listOf(CartItem("prod_1", 1))),
        Order("ORD-0988", "2026-05-15", 340.50, OrderStatus.Shipped, listOf(CartItem("prod_2", 2))),
        Order("ORD-0842", "2026-04-10", 89.99, OrderStatus.Delivered, listOf(CartItem("prod_3", 1))),
        Order("ORD-0711", "2026-02-28", 210.00, OrderStatus.Cancelled, listOf(CartItem("prod_4", 1)))
    )
}
