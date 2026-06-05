package io.github.adaptivekt.examples.ecommerce.model

object MockData {
    val categories = listOf(
        Category("cat-laptops", "Laptops", "High-performance workstations and ultraportables.", "💻", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?q=80&w=500&auto=format&fit=crop"),
        Category("cat-audio", "Audio", "Immersive sound experiences for music and work.", "🎧", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=500&auto=format&fit=crop"),
        Category("cat-gaming", "Gaming", "Gear designed for the ultimate competitive edge.", "🎮", "https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=500&auto=format&fit=crop"),
        Category("cat-workspace", "Workspace", "Essentials for a productive and inspired desk.", "⌨️", "https://images.unsplash.com/photo-1493934558415-9d19f0b2b4d2?q=80&w=500&auto=format&fit=crop"),
        Category("cat-smart-home", "Smart Home", "Connected devices for a modern lifestyle.", "🏠", "https://images.unsplash.com/photo-1558002038-1055907df827?q=80&w=500&auto=format&fit=crop"),
        Category("cat-cameras", "Cameras", "Capture your world in stunning detail.", "📷", "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=500&auto=format&fit=crop"),
        Category("cat-wearables", "Wearables", "Track your health and stay connected.", "⌚", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=500&auto=format&fit=crop"),
        Category("cat-accessories", "Accessories", "Premium cables, cases, and tools.", "🔌", "https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?q=80&w=500&auto=format&fit=crop")
    )

    val collections = listOf(
        Collection("col-new", "New Arrivals", "The latest tech just landed in our store.", "https://images.unsplash.com/photo-1519389950473-47ba0277781c?q=80&w=800&auto=format&fit=crop"),
        Collection("col-best", "Best Sellers", "Our community's favorite gear.", "https://images.unsplash.com/photo-1460925895917-afdab827c52f?q=80&w=800&auto=format&fit=crop"),
        Collection("col-desk", "Desk Setup Essentials", "Create your perfect creative space.", "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?q=80&w=800&auto=format&fit=crop"),
        Collection("col-creator", "Creator Gear", "Tools for the modern content creator.", "https://images.unsplash.com/photo-1492724441997-5dc865305da7?q=80&w=800&auto=format&fit=crop")
    )

    val promos = listOf(
        Promo("pr1", "Spring Setup Sale", "Save big on workspace essentials.", "Save up to 25%", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?q=80&w=800&auto=format&fit=crop"),
        Promo("pr2", "Audio Upgrade", "Premium sound for your daily flow.", "15% OFF Headphones", "https://images.unsplash.com/photo-1546435770-a3e426bf472b?q=80&w=800&auto=format&fit=crop")
    )

    val tags = listOf("Premium", "Wireless", "USB-C", "Ergonomic", "Portable", "Studio Grade")

    val initialProducts = listOf(
        Product(
            id = "p1", name = "NovaBook Pro 14", categoryId = "cat-laptops", collectionId = "col-new",
            shortDescription = "Ultimate performance in a sleek 14-inch form factor.",
            longDescription = "The NovaBook Pro 14 is designed for creators and professionals who need uncompromising power on the go. Featuring the latest M-series chips and a stunning ProDisplay XDR.",
            price = 1999.0, oldPrice = 2199.0, discountPercent = 10, rating = 4.9, reviewCount = 128, stock = 15, isNew = true, isFeatured = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?q=80&w=800&auto=format&fit=crop"),
            specs = listOf(ProductSpec("Processor", "8-Core CPU"), ProductSpec("Memory", "16GB Unified"), ProductSpec("Storage", "512GB SSD"))
        ),
        Product(
            id = "p2", name = "SonicBuds Air", categoryId = "cat-audio", collectionId = "col-best",
            shortDescription = "Immersive sound with active noise cancellation.",
            longDescription = "Experience audio like never before with SonicBuds Air. Custom-built drivers deliver crisp highs and deep bass, while advanced ANC blocks out the world.",
            price = 199.0, rating = 4.7, reviewCount = 1240, stock = 50, isFeatured = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=800&auto=format&fit=crop")
        ),
        Product(
            id = "p3", name = "GridMouse Elite", categoryId = "cat-workspace", collectionId = "col-desk",
            shortDescription = "Master your workflow with precision.",
            longDescription = "The GridMouse Elite features an ergonomic design and ultra-precise sensor for a smoother creative experience. Customize every button to fit your unique flow.",
            price = 99.0, oldPrice = 129.0, discountPercent = 23, rating = 4.8, reviewCount = 450, stock = 22, isSale = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=800&auto=format&fit=crop")
        ),
        Product(
            id = "p4", name = "FlowKeyboard Pro", categoryId = "cat-workspace", collectionId = "col-desk",
            shortDescription = "The perfect mechanical typing experience.",
            longDescription = "Built for writers and coders, the FlowKeyboard Pro offers tactile feedback and customizable RGB lighting. Low-profile switches ensure comfort during long sessions.",
            price = 159.0, rating = 4.9, reviewCount = 312, stock = 10, isNew = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?q=80&w=800&auto=format&fit=crop")
        ),
        Product(
            id = "p5", name = "OrbitCam Mini", categoryId = "cat-cameras", collectionId = "col-creator",
            shortDescription = "Crystal clear 4K video in a pocket-sized camera.",
            longDescription = "Perfect for vlogging and capturing memories on the move. The OrbitCam Mini features 3-axis stabilization and smart object tracking.",
            price = 349.0, rating = 4.6, reviewCount = 89, stock = 5, isFeatured = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=800&auto=format&fit=crop")
        ),
        Product(
            id = "p6", name = "ChronoBand Fit", categoryId = "cat-wearables",
            shortDescription = "Track your health with style.",
            longDescription = "Stay on top of your fitness goals with heart rate monitoring, sleep tracking, and built-in GPS. The ChronoBand Fit is waterproof and lasts up to 10 days on a single charge.",
            price = 129.0, oldPrice = 149.0, discountPercent = 13, rating = 4.5, reviewCount = 567, stock = 40, isSale = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=800&auto=format&fit=crop")
        ),
        Product(
            id = "p7", name = "StudioDock 4K", categoryId = "cat-accessories", collectionId = "col-desk",
            shortDescription = "The ultimate hub for your professional setup.",
            longDescription = "Connect everything with a single cable. Features dual 4K HDMI ports, 10Gbps USB-A/C ports, and 100W power delivery for your laptop.",
            price = 249.0, rating = 4.8, reviewCount = 156, stock = 12,
            imageUrls = listOf("https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?q=80&w=800&auto=format&fit=crop")
        ),
        Product(
            id = "p8", name = "PulsePods Max", categoryId = "cat-audio", collectionId = "col-creator",
            shortDescription = "Studio-grade sound for professionals.",
            longDescription = "Over-ear headphones designed for mixing and critical listening. Features ultra-wide frequency response and premium memory foam cushions.",
            price = 399.0, rating = 4.9, reviewCount = 84, stock = 8, isNew = true,
            imageUrls = listOf("https://images.unsplash.com/photo-1546435770-a3e426bf472b?q=80&w=800&auto=format&fit=crop")
        )
    )

    val products = initialProducts + (9..40).map { i ->
        val cat = categories[i % categories.size]
        val price = 20.0 + (i * 15.0)
        Product(
            id = "p$i", name = "${cat.name.split(" ").first()} Gear ${i - 8}", categoryId = cat.id,
            shortDescription = "A reliable ${cat.name.lowercase()} accessory.",
            longDescription = "High quality construction and reliable performance. This ${cat.name.lowercase()} product is built to last.",
            price = price, rating = 4.0 + (i % 10) / 10.0, reviewCount = i * 5, stock = 20,
            imageUrls = listOf(cat.imageUrl ?: "")
        )
    }

    val mockUser = UserProfile(
        name = "Alex Developer",
        email = "alex@adaptivestore.dev",
        tier = "Pro"
    )

    val mockOrders = listOf(
        Order("ORD-1029", "2026-06-01", 1250.00, OrderStatus.Processing, listOf(CartItem("p1", 1))),
        Order("ORD-0988", "2026-05-15", 340.50, OrderStatus.Shipped, listOf(CartItem("p2", 2))),
        Order("ORD-0842", "2026-04-10", 89.99, OrderStatus.Delivered, listOf(CartItem("p3", 1))),
        Order("ORD-0711", "2026-02-28", 210.00, OrderStatus.Cancelled, listOf(CartItem("p4", 1)))
    )
}
