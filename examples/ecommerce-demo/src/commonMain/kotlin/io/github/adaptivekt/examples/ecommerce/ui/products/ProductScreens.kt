package io.github.adaptivekt.examples.ecommerce.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import io.github.adaptivekt.data.AdaptiveCollectionDisplayMode
import io.github.adaptivekt.data.AdaptiveCollectionView
import io.github.adaptivekt.data.AdaptiveFilterOption
import io.github.adaptivekt.data.AdaptiveFilterValue
import io.github.adaptivekt.data.AdaptivePaginationState
import io.github.adaptivekt.data.AdaptiveQueryState
import io.github.adaptivekt.data.AdaptiveSortOption
import io.github.adaptivekt.data.coerceAdaptivePage
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveTwoPane
import io.github.adaptivekt.layout.AdaptiveSection
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
    var collectionPage by remember { mutableStateOf(1) }
    var collectionPageSize by remember { mutableStateOf(10) }
    val sortOptions = listOf(
        AdaptiveSortOption("popular", "Popular"),
        AdaptiveSortOption("price-asc", "Price: Low to High"),
        AdaptiveSortOption("price-desc", "Price: High to Low"),
        AdaptiveSortOption("newest", "Newest"),
        AdaptiveSortOption("rated", "Best Rated"),
    )
    val queryState = AdaptiveQueryState(
        search = state.searchQuery,
        filters = state.selectedCategoryId?.let { mapOf("category" to setOf(it)) }.orEmpty(),
        sortKey = sortOptions.firstOrNull { it.label == state.sortOption }?.key ?: "popular",
        page = collectionPage,
        pageSize = collectionPageSize,
    )
    val safePage = coerceAdaptivePage(collectionPage, products.size, collectionPageSize)
    val pageItems = products
        .drop((safePage - 1) * collectionPageSize)
        .take(collectionPageSize)

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current
        val productColumns = when {
            layoutInfo.isCompact -> 2
            layoutInfo.isMedium -> 3
            layoutInfo.isExpanded -> 4
            else -> 5
        }

        AdaptiveScrollablePage(modifier = Modifier.fillMaxSize()) {
            val title = when {
                state.selectedCategoryId != null -> categories.find { it.id == state.selectedCategoryId }?.name ?: "Products"
                state.selectedCollectionId != null -> MockData.collections.find { it.id == state.selectedCollectionId }?.name ?: "Products"
                state.searchQuery.isNotBlank() -> "Search results for \"${state.searchQuery}\""
                else -> "All Products"
            }
            Text(title, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
            Spacer(Modifier.height(4.dp))
            Text("${products.size} products found", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(24.dp))

            AdaptiveCollectionView(
                items = pageItems,
                displayMode = AdaptiveCollectionDisplayMode.Grid,
                gridColumns = productColumns,
                queryState = queryState,
                onQueryChange = { next ->
                    state.searchQuery = next.search
                    state.selectedCategoryId = next.filters["category"]?.firstOrNull()
                    state.sortOption = sortOptions.firstOrNull { it.key == next.sortKey }?.label ?: "Popular"
                    collectionPage = next.page.coerceAtLeast(1)
                    collectionPageSize = next.pageSize.coerceAtLeast(1)
                },
                pagination = AdaptivePaginationState(
                    page = safePage,
                    pageSize = collectionPageSize,
                    totalItems = products.size,
                    pageSizeOptions = listOf(10, 20, 40),
                ),
                searchEnabled = true,
                filters = listOf(
                    AdaptiveFilterOption(
                        key = "category",
                        label = "Category",
                        options = categories.map { category ->
                            AdaptiveFilterValue(
                                value = category.id,
                                label = category.name,
                                count = MockData.products.count { it.categoryId == category.id },
                            )
                        },
                    ),
                ),
                sortOptions = sortOptions,
                emptyContent = {
                    AdaptiveEmptyState(
                        title = "No products found",
                        description = "We couldn't find any products matching your current filters.",
                        action = {
                            AdaptiveButton(text = "Clear Filters", onClick = { state.clearFilters() })
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp)
                    )
                },
                listItemContent = { prod ->
                    ProductCard(prod, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                },
                gridItemContent = { prod ->
                    ProductCard(prod, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                },
            )
        }
    }
}

@Composable
fun ProductDetailScreen(state: StoreState, productId: String, modifier: Modifier = Modifier) {
    val product = MockData.products.find { it.id == productId }
    val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current
    val compact = layoutInfo.isCompact
    val productColumns = when {
        layoutInfo.isCompact -> 2
        layoutInfo.isMedium -> 3
        else -> 4
    }
    if (product == null) {
        AdaptiveContainer(modifier = modifier.fillMaxSize()) {
            AdaptiveEmptyState(title = "Product not found", description = "We couldn't find the product you're looking for.")
        }
        return
    }

    AdaptiveScrollablePage(modifier = modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Shop", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp, modifier = Modifier.clickable { state.navigateTo(Screen.Products) })
            Text(" / ", color = AdaptiveTheme.colors.border, fontSize = 14.sp)
            Text(MockData.categories.find { it.id == product.categoryId }?.name ?: "Category", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
            Text(" / ", color = Color(0xFFCBD5E1), fontSize = 14.sp)
            Text(product.name, color = AdaptiveTheme.colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        
        AdaptiveTwoPane(
            primaryWeight = 1.2f,
            secondaryWeight = 1f,
            gap = 48.dp,
            primary = {
                // Image Gallery
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp, max = 500.dp)
                            .clip(RoundedCornerShape(24.dp)),
                    ) {
                        ProductVisual(product = product, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.height(16.dp))
                    AdaptiveGrid(columns = 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        repeat(4) {
                            item(span = 1) {
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    ProductVisual(product = product, compact = true, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                }
            },
            secondary = {
                // Product Info
                Column {
                    if (product.isNew) Badge("NEW ARRIVAL", Color(0xFF10B981))
                    else if (product.isSale) Badge("SPECIAL OFFER", Color(0xFFEF4444))
                    
                    Spacer(Modifier.height(16.dp))
                    Text(product.name, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary, lineHeight = 44.sp)
                    
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
        )
        
        AdaptiveSection(title = "Product Specifications") {
            AdaptiveGrid(columns = if (compact) 1 else 2, horizontalGap = 24.dp, verticalGap = 8.dp) {
                product.specs.forEach { spec ->
                    item(span = 1) {
                        Column {
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
        
        AdaptiveSection(title = "You might also like") {
            AdaptiveGrid(columns = productColumns, horizontalGap = 16.dp, verticalGap = 18.dp) {
                MockData.products.filter { it.categoryId == product.categoryId && it.id != product.id }.take(4).forEach { related ->
                    item(span = 1) {
                        ProductCard(related, onClick = { state.navigateTo(Screen.ProductDetail(related.id)) })
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistScreen(state: StoreState, modifier: Modifier = Modifier) {
    val products = MockData.products.filter { state.wishlistIds.contains(it.id) }
    val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current
    val productColumns = when {
        layoutInfo.isCompact -> 2
        layoutInfo.isMedium -> 3
        layoutInfo.isExpanded -> 4
        else -> 5
    }

    AdaptiveScrollablePage(modifier = modifier.fillMaxSize()) {
        Column {
            Text("Your Wishlist", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
            Spacer(Modifier.height(8.dp))
            Text("${products.size} items saved", color = AdaptiveTheme.colors.textSecondary, fontSize = 14.sp)
        }

        if (products.isEmpty()) {
            AdaptiveEmptyState(
                title = "Your wishlist is empty",
                description = "Save your favorite tech to keep track of them here.",
                action = {
                    AdaptiveButton(text = "Explore Products", onClick = { state.navigateTo(Screen.Products) })
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp)
            )
        } else {
            AdaptiveGrid(columns = productColumns, horizontalGap = 16.dp, verticalGap = 18.dp) {
                products.forEach { prod ->
                    item(span = 1) {
                        ProductCard(prod, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                    }
                }
            }
        }
    }
}
