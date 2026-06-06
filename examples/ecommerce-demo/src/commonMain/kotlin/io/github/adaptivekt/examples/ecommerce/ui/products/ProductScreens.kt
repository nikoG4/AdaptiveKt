package io.github.adaptivekt.examples.ecommerce.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import io.github.adaptivekt.components.*
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcons
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcon
import io.github.adaptivekt.examples.ecommerce.ui.components.ProductVisual
import io.github.adaptivekt.examples.ecommerce.ui.home.ProductCard
import io.github.adaptivekt.examples.ecommerce.ui.home.Badge
import io.github.adaptivekt.examples.ecommerce.ui.cart.toPriceString
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
fun ProductListScreen(state: StoreState, modifier: Modifier = Modifier) {
    val products = state.filteredProducts()
    val categories = MockData.categories

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compact = maxWidth < 800.dp
            val sectionPadding = if (compact) 16.dp else 24.dp

            Row(modifier = Modifier.fillMaxSize()) {
                // Filters Sidebar (hidden on mobile)
                if (!compact) {
                    Column(
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                            .background(AdaptiveTheme.colors.surfaceMuted)
                            .padding(24.dp)
                    ) {
                        Text("Filters", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(24.dp))
                        
                        Text("Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AdaptiveTheme.colors.textSecondary)
                        Spacer(Modifier.height(12.dp))
                        categories.forEach { cat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { state.selectedCategoryId = if (state.selectedCategoryId == cat.id) null else cat.id }
                                    .padding(vertical = 8.dp)
                            ) {
                                val isSelected = state.selectedCategoryId == cat.id
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isSelected) AdaptiveTheme.colors.primary else AdaptiveTheme.colors.disabledBackground),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) AppIcon(AppIcons.Check, modifier = Modifier.size(12.dp), tint = Color.White)
                                }
                                Spacer(Modifier.width(12.dp))
                                Text(cat.name, fontSize = 14.sp, color = if (isSelected) AdaptiveTheme.colors.textPrimary else AdaptiveTheme.colors.textSecondary)
                            }
                        }
                        
                        Spacer(Modifier.height(32.dp))
                        Text("Sort By", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AdaptiveTheme.colors.textSecondary)
                        Spacer(Modifier.height(12.dp))
                        val sortOptions = listOf("Popular", "Price: Low to High", "Price: High to Low", "Newest", "Best Rated")
                        AdaptiveSelect(
                            options = sortOptions,
                            selectedOption = state.sortOption,
                            onSelectedOptionChange = { state.sortOption = it ?: "Popular" },
                            optionLabel = { it }
                        )
                        
                        Spacer(Modifier.weight(1f))
                        AdaptiveButton(
                            text = "Clear All Filters", 
                            onClick = { state.clearFilters() },
                            variant = AdaptiveButtonVariant.Ghost,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Product Grid
                LazyColumn(
                    contentPadding = PaddingValues(sectionPadding),
                    verticalArrangement = Arrangement.spacedBy(if (compact) 16.dp else 24.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Column {
                            val title = when {
                                state.selectedCategoryId != null -> categories.find { it.id == state.selectedCategoryId }?.name ?: "Products"
                                state.selectedCollectionId != null -> MockData.collections.find { it.id == state.selectedCollectionId }?.name ?: "Products"
                                state.searchQuery.isNotBlank() -> "Search results for \"${state.searchQuery}\""
                                else -> "All Products"
                            }
                            Text(title, fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text("${products.size} products found", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
                            
                            if (compact) {
                                Spacer(Modifier.height(16.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Mobile filters summary
                                    if (state.selectedCategoryId != null) {
                                        Badge(categories.find { it.id == state.selectedCategoryId }?.name ?: "Category", Color(0xFF3B82F6))
                                    }
                                    if (state.selectedCollectionId != null) {
                                        Badge("Collection", Color(0xFF8B5CF6))
                                    }
                                    if (state.sortOption != "Popular") {
                                        Badge(state.sortOption, Color(0xFF10B981))
                                    }
                                }
                            }
                        }
                    }

                    if (products.isEmpty()) {
                        item {
                            AdaptiveEmptyState(
                                title = "No products found",
                                description = "We couldn't find any products matching your current filters.",
                                action = {
                                    AdaptiveButton(text = "Clear Filters", onClick = { state.clearFilters() })
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = if (compact) 40.dp else 80.dp)
                            )
                        }
                    } else {
                        item {
                            AdaptiveGrid(columns = if (compact) 2 else 3, horizontalGap = if (compact) 16.dp else 24.dp, verticalGap = if (compact) 16.dp else 24.dp) {
                                products.forEach { prod ->
                                    item(span = 1) {
                                        ProductCard(prod, compact = compact, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(state: StoreState, productId: String, modifier: Modifier = Modifier) {
    val product = MockData.products.find { it.id == productId }
    if (product == null) {
        AdaptiveContainer(modifier = modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
                Text("Product not found", fontSize = 20.sp, color = Color.Gray) 
            }
        }
        return
    }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compact = maxWidth < 800.dp
            val sectionPadding = if (compact) 16.dp else 24.dp

            LazyColumn(
                contentPadding = PaddingValues(sectionPadding), 
                verticalArrangement = Arrangement.spacedBy(if (compact) 24.dp else 48.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Shop", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp, modifier = Modifier.clickable { state.navigateTo(Screen.Products) })
                        Text(" / ", color = AdaptiveTheme.colors.border, fontSize = 14.sp)
                        Text(MockData.categories.find { it.id == product.categoryId }?.name ?: "Category", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
                        if (!compact) {
                            Text(" / ", color = Color(0xFFCBD5E1), fontSize = 14.sp)
                            Text(product.name, color = AdaptiveTheme.colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                
                item {
                    if (compact) {
                        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                            // Image Gallery Mobile
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                ) {
                                    ProductVisual(product = product, modifier = Modifier.fillMaxSize())
                                }
                                Spacer(Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    repeat(4) {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                        }
                                    }
                                }
                            }
                            
                            // Product Info Mobile
                            Column {
                                if (product.isNew) Badge("NEW ARRIVAL", Color(0xFF10B981))
                                else if (product.isSale) Badge("SPECIAL OFFER", Color(0xFFEF4444))
                                
                                Spacer(Modifier.height(12.dp))
                                Text(product.name, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary, lineHeight = 34.sp)
                                
                                Spacer(Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row {
                                        repeat(5) { i ->
                                            AppIcon(AppIcons.Star, modifier = Modifier.size(16.dp), tint = if (i < product.rating.toInt()) Color(0xFFF59E0B) else Color(0xFFE2E8F0))
                                        }
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text("${product.rating} (${product.reviewCount})", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
                                }
                                
                                Spacer(Modifier.height(24.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text("\$${product.price.toPriceString()}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                                    if (product.oldPrice != null) {
                                        Spacer(Modifier.width(12.dp))
                                        Text("\$${product.oldPrice.toPriceString()}", color = Color(0xFF94A3B8), fontSize = 18.sp, textDecoration = TextDecoration.LineThrough, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                }
                                
                                Spacer(Modifier.height(24.dp))
                                Text(product.shortDescription, color = AdaptiveTheme.colors.textSecondary, fontSize = 16.sp, lineHeight = 24.sp)
                                
                                Spacer(Modifier.height(32.dp))
                                Text("Color", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    listOf(Color.Black, Color.White, Color.Gray, Color.Blue).forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .clickable { }
                                                .padding(2.dp)
                                        ) {
                                            if (color == Color.Black) {
                                                Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color.Transparent).padding(2.dp).clip(CircleShape).background(Color.White))
                                            }
                                        }
                                    }
                                }
                                
                                Spacer(Modifier.height(32.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    AdaptiveButton(
                                        text = "Add to Cart", 
                                        onClick = { 
                                            state.addToCart(product.id, product.variants.firstOrNull()?.id, 1)
                                        },
                                        modifier = Modifier.weight(1f).height(56.dp)
                                    )
                                    AdaptiveButton(
                                        text = "", 
                                        onClick = { state.toggleWishlist(product.id) },
                                        variant = if (state.wishlistIds.contains(product.id)) AdaptiveButtonVariant.Secondary else AdaptiveButtonVariant.Ghost,
                                        modifier = Modifier.size(56.dp)
                                    ) {
                                        AppIcon(AppIcons.Heart, tint = if (state.wishlistIds.contains(product.id)) Color(0xFFEF4444) else Color.Black)
                                    }
                                }
                            }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                            // Image Gallery Desktop
                            Column(modifier = Modifier.weight(1.2f)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(500.dp)
                                        .clip(RoundedCornerShape(24.dp)),
                                ) {
                                    ProductVisual(product = product, modifier = Modifier.fillMaxSize())
                                }
                                Spacer(Modifier.height(16.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    repeat(4) {
                                        Box(
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                            .clickable { },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                        }
                                    }
                                }
                            }
                            
                            // Product Info Desktop
                            Column(modifier = Modifier.weight(1f)) {
                                if (product.isNew) Badge("NEW ARRIVAL", Color(0xFF10B981))
                                else if (product.isSale) Badge("SPECIAL OFFER", Color(0xFFEF4444))
                                
                                Spacer(Modifier.height(16.dp))
                                Text(product.name, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary, lineHeight = 56.sp)
                                
                                Spacer(Modifier.height(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row {
                                        repeat(5) { i ->
                                            AppIcon(AppIcons.Star, modifier = Modifier.size(18.dp), tint = if (i < product.rating.toInt()) Color(0xFFF59E0B) else Color(0xFFE2E8F0))
                                        }
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Text("${product.rating} (${product.reviewCount} reviews)", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
                                }
                                
                                Spacer(Modifier.height(32.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text("\$${product.price.toPriceString()}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                                    if (product.oldPrice != null) {
                                        Spacer(Modifier.width(16.dp))
                                        Text("\$${product.oldPrice.toPriceString()}", color = Color(0xFF94A3B8), fontSize = 24.sp, textDecoration = TextDecoration.LineThrough, modifier = Modifier.padding(bottom = 4.dp))
                                        Spacer(Modifier.width(12.dp))
                                        Text("${product.discountPercent}% OFF", color = Color(0xFFEF4444), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                                    }
                                }
                                
                                Spacer(Modifier.height(32.dp))
                                Text(product.shortDescription, color = AdaptiveTheme.colors.textSecondary, fontSize = 18.sp, lineHeight = 28.sp)
                                
                                Spacer(Modifier.height(40.dp))
                                
                                // Variants
                                Text("Color", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    listOf(Color.Black, Color.White, Color.Gray, Color.Blue).forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .clickable { }
                                                .padding(2.dp)
                                        ) {
                                            if (color == Color.Black) {
                                                Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color.Transparent).padding(2.dp).clip(CircleShape).background(Color.White))
                                            }
                                        }
                                    }
                                }
                                
                                Spacer(Modifier.height(48.dp))
                                
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    AdaptiveButton(
                                        text = "Add to Cart", 
                                        onClick = { 
                                            state.addToCart(product.id, product.variants.firstOrNull()?.id, 1)
                                        },
                                        modifier = Modifier.weight(1f).height(56.dp)
                                    )
                                    AdaptiveButton(
                                        text = "", 
                                        onClick = { state.toggleWishlist(product.id) },
                                        variant = if (state.wishlistIds.contains(product.id)) AdaptiveButtonVariant.Secondary else AdaptiveButtonVariant.Ghost,
                                        modifier = Modifier.size(56.dp)
                                    ) {
                                        AppIcon(AppIcons.Heart, tint = if (state.wishlistIds.contains(product.id)) Color(0xFFEF4444) else Color.Black)
                                    }
                                }
                                
                                Spacer(Modifier.height(32.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AppIcon(AppIcons.Truck, modifier = Modifier.size(20.dp), tint = Color(0xFF10B981))
                                    Spacer(Modifier.width(12.dp))
                                    Text("Free shipping on orders over \$75", color = Color(0xFF10B981), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
                
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Product Specifications", fontSize = if (compact) 20.sp else 24.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(16.dp))
                        AdaptiveGrid(columns = if (compact) 1 else 2, horizontalGap = if (compact) 16.dp else 48.dp, verticalGap = 16.dp) {
                            product.specs.forEach { spec ->
                                item(span = 1) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(spec.name, color = AdaptiveTheme.colors.textSecondary, fontWeight = FontWeight.Medium)
                                        Text(spec.value, color = AdaptiveTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                                    }
                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AdaptiveTheme.colors.border))
                                }
                            }
                        }
                    }
                }
                
                item {
                    Column {
                        Text("You might also like", fontSize = if (compact) 20.sp else 24.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(16.dp))
                        AdaptiveGrid(columns = if (compact) 2 else 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
                            MockData.products.filter { it.categoryId == product.categoryId && it.id != product.id }.take(4).forEach { related ->
                                item(span = 1) {
                                    ProductCard(related, compact = compact, onClick = { state.navigateTo(Screen.ProductDetail(related.id)) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistScreen(state: StoreState, modifier: Modifier = Modifier) {
    val products = MockData.products.filter { state.wishlistIds.contains(it.id) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compact = maxWidth < 800.dp
            val sectionPadding = if (compact) 16.dp else 24.dp

            LazyColumn(
                contentPadding = PaddingValues(sectionPadding),
                verticalArrangement = Arrangement.spacedBy(if (compact) 16.dp else 24.dp)
            ) {
                item {
                    Text("Your Wishlist", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                    Spacer(Modifier.height(8.dp))
                    Text("${products.size} items saved", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
                }

                if (products.isEmpty()) {
                    item {
                        AdaptiveEmptyState(
                            title = "Your wishlist is empty",
                            description = "Save your favorite tech to keep track of them here.",
                            action = {
                                AdaptiveButton(text = "Explore Products", onClick = { state.navigateTo(Screen.Products) })
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = if (compact) 40.dp else 80.dp)
                        )
                    }
                } else {
                    item {
                        AdaptiveGrid(columns = if (compact) 2 else 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
                            products.forEach { prod ->
                                item(span = 1) {
                                    ProductCard(prod, compact = compact, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
