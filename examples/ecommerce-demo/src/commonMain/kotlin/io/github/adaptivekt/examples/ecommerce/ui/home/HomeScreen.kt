package io.github.adaptivekt.examples.ecommerce.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.model.MockData
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.layout.AdaptiveScrollablePage
import io.github.adaptivekt.layout.AdaptiveSection

import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcons
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcon
import io.github.adaptivekt.examples.ecommerce.ui.components.CategoryVisual
import io.github.adaptivekt.examples.ecommerce.ui.components.CollectionVisual
import io.github.adaptivekt.examples.ecommerce.ui.components.ProductVisual
import io.github.adaptivekt.examples.ecommerce.ui.cart.toPriceString
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.LocalAdaptiveLayoutInfo

@Composable
fun HomeScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveScrollablePage(modifier = modifier) {
        val layoutInfo = LocalAdaptiveLayoutInfo.current
        val compact = layoutInfo.isCompact
        val collectionColumns = if (compact) 2 else 4
        val productColumns = when {
            layoutInfo.isCompact -> 2
            layoutInfo.isMedium -> 3
            else -> 4
        }

        // Hero section
        AdaptiveSection(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 320.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp).widthIn(max = 800.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF3B82F6).copy(alpha = 0.2f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("New for Summer 2026", color = Color(0xFF60A5FA), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Build your perfect setup", 
                        fontSize = 40.sp, 
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 46.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Adaptive tech essentials for work, gaming, and creative flow.", 
                        fontSize = 20.sp, 
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                    Spacer(Modifier.height(32.dp))
                    if (compact) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AdaptiveButton(
                                text = "Shop new arrivals", 
                                onClick = { state.navigateTo(Screen.Products) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            AdaptiveButton(
                                text = "Explore deals", 
                                onClick = { 
                                    state.sortOption = "Best Rated"
                                    state.navigateTo(Screen.Products) 
                                },
                                variant = AdaptiveButtonVariant.Secondary,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AdaptiveButton(
                                text = "Shop new arrivals",
                                onClick = { state.navigateTo(Screen.Products) },
                            )
                            AdaptiveButton(
                                text = "Explore deals",
                                onClick = {
                                    state.sortOption = "Best Rated"
                                    state.navigateTo(Screen.Products)
                                },
                                variant = AdaptiveButtonVariant.Secondary,
                            )
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                    AdaptiveGrid(columns = if (compact) 1 else 3, horizontalGap = 24.dp, verticalGap = 12.dp) {
                        item(span = 1) { BenefitBadge("Free shipping", AppIcons.Truck) }
                        item(span = 1) { BenefitBadge("Secure checkout", AppIcons.Shield) }
                        item(span = 1) { BenefitBadge("Easy returns", AppIcons.Package) }
                    }
                }
            }
        }

        // Featured Collections
        AdaptiveSection(
            title = "Featured Collections",
            modifier = Modifier.fillMaxWidth()
        ) {
            AdaptiveGrid(columns = collectionColumns, horizontalGap = 16.dp, verticalGap = 16.dp) {
                MockData.collections.forEach { col ->
                    item(span = 1) {
                        AdaptiveCard(
                            onClick = {
                                state.selectedCollectionId = col.id
                                state.navigateTo(Screen.Products)
                            },
                            modifier = Modifier.height(200.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CollectionVisual(title = col.name, modifier = Modifier.fillMaxSize())
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            col.name,
                                            color = Color.White,
                                            fontSize = if (compact) 18.sp else 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = if (compact) 20.sp else 22.sp,
                                            maxLines = 3,
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text("Browse collection", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, maxLines = 2)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Trending Now
        AdaptiveSection(
            title = "Trending now",
            actions = {
                AdaptiveButton(
                    text = "View all", 
                    onClick = { state.navigateTo(Screen.Products) },
                    variant = AdaptiveButtonVariant.Ghost
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            AdaptiveGrid(columns = productColumns, horizontalGap = 16.dp, verticalGap = 16.dp) {
                MockData.products.filter { it.isFeatured }.take(4).forEach { prod ->
                    item(span = 1) {
                        ProductCard(prod, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                    }
                }
            }
        }

        // Deal Banner
        AdaptiveSection(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AdaptiveTheme.colors.primary.copy(alpha = 0.12f))
                    .padding(32.dp)
            ) {
                AdaptiveGrid(columns = if (compact) 1 else 2, horizontalGap = 32.dp, verticalGap = 32.dp) {
                    item(span = 1) {
                        Column {
                            Text("Spring setup sale", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                            Spacer(Modifier.height(8.dp))
                            Text("Save up to 25% on workspace gear.", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E3A8A), lineHeight = 44.sp)
                            Spacer(Modifier.height(24.dp))
                            AdaptiveButton(text = "Shop the sale", onClick = { state.navigateTo(Screen.Products) })
                        }
                    }
                    item(span = 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 160.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(AdaptiveTheme.colors.surfaceRaised),
                            contentAlignment = Alignment.Center
                        ) {
                            AppIcon(AppIcons.Package, modifier = Modifier.size(80.dp), tint = Color(0xFFDBEAFE))
                        }
                    }
                }
            }
        }

        // Category Grid
        AdaptiveSection(
            title = "Shop by category",
            modifier = Modifier.fillMaxWidth()
        ) {
            AdaptiveGrid(columns = productColumns, horizontalGap = 16.dp, verticalGap = 16.dp) {
                MockData.categories.forEach { cat ->
                    item(span = 1) {
                        AdaptiveCard(
                            onClick = {
                                state.selectedCategoryId = cat.id
                                state.navigateTo(Screen.Products)
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                CategoryVisual(categoryId = cat.id, modifier = Modifier.size(64.dp))
                                Spacer(Modifier.height(8.dp))
                                Text(cat.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AdaptiveTheme.colors.textPrimary)
                            }
                        }
                    }
                }
            }
        }

        // Why Adaptive Store
        AdaptiveSection(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AdaptiveTheme.colors.surfaceMuted)
                    .padding(vertical = 48.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.widthIn(max = 600.dp)
                ) {
                    Text("Why Adaptive Store?", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "This showcase is built entirely with AdaptiveKt, a premium UI toolkit for Compose Multiplatform. It demonstrates responsive layouts, shared state, and advanced navigation across Web, Mobile, and Desktop.", 
                        textAlign = TextAlign.Center,
                        color = AdaptiveTheme.colors.textSecondary,
                        lineHeight = 28.sp,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(24.dp))
                    AdaptiveButton(
                        text = "Learn more", 
                        onClick = { }, 
                        variant = AdaptiveButtonVariant.Ghost
                    )
                }
            }
        }
    }
}

@Composable
fun BenefitBadge(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        AppIcon(icon, modifier = Modifier.size(16.dp), tint = Color(0xFF94A3B8))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color(0xFF94A3B8), fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProductCard(prod: io.github.adaptivekt.examples.ecommerce.model.Product, onClick: () -> Unit) {
    val layoutInfo = LocalAdaptiveLayoutInfo.current
    val compact = layoutInfo.isCompact
    AdaptiveCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compact) 168.dp else 190.dp),
                contentAlignment = Alignment.Center
            ) {
                ProductVisual(
                    product = prod,
                    compact = compact,
                    modifier = Modifier.fillMaxSize(),
                )
                
                // Badges
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    when {
                        prod.isNew -> Badge("NEW", Color(0xFF10B981))
                        prod.isSale -> Badge("SALE", Color(0xFFEF4444))
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    prod.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    color = AdaptiveTheme.colors.textPrimary,
                )
                Spacer(Modifier.height(4.dp))
                Text(prod.shortDescription, fontSize = 12.sp, lineHeight = 15.sp, color = AdaptiveTheme.colors.textSecondary, maxLines = 2)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("\$${prod.price.toPriceString()}", color = AdaptiveTheme.colors.textPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        if (prod.oldPrice != null) {
                            Text("\$${prod.oldPrice.toPriceString()}", color = AdaptiveTheme.colors.textMuted, fontSize = 12.sp, style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
                        }
                    }
                    AppIcon(AppIcons.Plus, modifier = Modifier.size(20.dp), tint = Color(0xFF3B82F6))
                }
            }
        }
    }
}

@Composable
fun Badge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 10.sp
        )
    }
}
