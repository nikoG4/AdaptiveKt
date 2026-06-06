package io.github.adaptivekt.examples.ecommerce.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import io.github.adaptivekt.layout.AdaptiveContainer

import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcons
import io.github.adaptivekt.examples.ecommerce.ui.components.AppIcon
import io.github.adaptivekt.examples.ecommerce.ui.components.CategoryVisual
import io.github.adaptivekt.examples.ecommerce.ui.components.CollectionVisual
import io.github.adaptivekt.examples.ecommerce.ui.components.ProductVisual
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveBreakpoint
import io.github.adaptivekt.core.breakpointForWidth

@Composable
fun HomeScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val breakpoint = breakpointForWidth(maxWidth)
            val compact = breakpoint == AdaptiveBreakpoint.Compact
            val medium = breakpoint == AdaptiveBreakpoint.Medium
            val expanded = breakpoint == AdaptiveBreakpoint.Expanded || breakpoint == AdaptiveBreakpoint.Large
            val sectionHorizontalPadding = if (compact) 16.dp else 24.dp

            LazyColumn(
                contentPadding = PaddingValues(bottom = if (compact) 16.dp else 32.dp),
                verticalArrangement = Arrangement.spacedBy(if (compact) 32.dp else 48.dp)
            ) {
                // Hero section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = if (compact) 260.dp else if (medium) 320.dp else 360.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(if (compact) 24.dp else 32.dp)
                                .widthIn(max = 800.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF3B82F6).copy(alpha = 0.2f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text("New for Summer 2026", color = Color(0xFF60A5FA), fontSize = if (compact) 12.sp else 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Build your perfect setup", 
                                fontSize = if (compact) 30.sp else if (medium) 40.sp else 46.sp, 
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = if (compact) 36.sp else if (medium) 46.sp else 52.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Adaptive tech essentials for work, gaming, and creative flow.", 
                                fontSize = if (compact) 16.sp else 20.sp, 
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center,
                                lineHeight = if (compact) 24.sp else 30.sp
                            )
                            Spacer(Modifier.height(32.dp))
                            if (compact) {
                                Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                                    AdaptiveButton(
                                        text = "Shop new arrivals", 
                                        onClick = { state.navigateTo(Screen.Products) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    AdaptiveButton(
                                        text = "Explore deals", 
                                        onClick = { 
                                            state.sortOption = "Best Rated"
                                            state.navigateTo(Screen.Products) 
                                        },
                                        variant = AdaptiveButtonVariant.Secondary,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            } else {
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    AdaptiveButton(
                                        text = "Shop new arrivals", 
                                        onClick = { state.navigateTo(Screen.Products) }
                                    )
                                    AdaptiveButton(
                                        text = "Explore deals", 
                                        onClick = { 
                                            state.sortOption = "Best Rated"
                                            state.navigateTo(Screen.Products) 
                                        },
                                        variant = AdaptiveButtonVariant.Secondary
                                    )
                                }
                            }
                            Spacer(Modifier.height(32.dp))
                            if (compact) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    BenefitBadge("Free shipping over $75", AppIcons.Truck)
                                    BenefitBadge("Secure checkout", AppIcons.Shield)
                                    BenefitBadge("Easy returns", AppIcons.Package)
                                }
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    BenefitBadge("Free shipping over $75", AppIcons.Truck)
                                    BenefitBadge("Secure checkout", AppIcons.Shield)
                                    BenefitBadge("Easy returns", AppIcons.Package)
                                }
                            }
                        }
                    }
                }

                // Featured Collections
                item {
                    Column(modifier = Modifier.padding(horizontal = sectionHorizontalPadding)) {
                        Text("Featured Collections", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(16.dp))
                        AdaptiveGrid(columns = if (compact) 1 else 2, horizontalGap = 16.dp, verticalGap = 16.dp) {
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
                                                    Text(col.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                                    Spacer(Modifier.height(4.dp))
                                                    Text("Browse collection", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Trending Now
                item {
                    Column(modifier = Modifier.padding(horizontal = sectionHorizontalPadding)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Trending now", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                            AdaptiveButton(
                                text = "View all", 
                                onClick = { state.navigateTo(Screen.Products) },
                                variant = AdaptiveButtonVariant.Ghost
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        AdaptiveGrid(columns = if (compact) 2 else if (medium) 3 else 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
                            MockData.products.filter { it.isFeatured }.take(4).forEach { prod ->
                                item(span = 1) {
                                    ProductCard(prod, compact = compact, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
                                }
                            }
                        }
                    }
                }

                // Deal Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = sectionHorizontalPadding)
                            .clip(RoundedCornerShape(24.dp))
                            .background(AdaptiveTheme.colors.primary.copy(alpha = 0.12f))
                            .padding(if (compact) 24.dp else 48.dp)
                    ) {
                        if (compact) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(AdaptiveTheme.colors.surfaceRaised),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AppIcon(AppIcons.Package, modifier = Modifier.size(50.dp), tint = Color(0xFFDBEAFE))
                                }
                                Spacer(Modifier.height(24.dp))
                                Text("Spring setup sale", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                                Spacer(Modifier.height(8.dp))
                                Text("Save up to 25% on workspace gear.", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary, lineHeight = 36.sp, textAlign = TextAlign.Center)
                                Spacer(Modifier.height(24.dp))
                                AdaptiveButton(text = "Shop the sale", onClick = { state.navigateTo(Screen.Products) }, modifier = Modifier.fillMaxWidth())
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Spring setup sale", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Save up to 25% on workspace gear.", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E3A8A), lineHeight = 48.sp)
                                    Spacer(Modifier.height(24.dp))
                                    AdaptiveButton(text = "Shop the sale", onClick = { state.navigateTo(Screen.Products) })
                                }
                                Box(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(AdaptiveTheme.colors.surfaceRaised),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AppIcon(AppIcons.Package, modifier = Modifier.size(100.dp), tint = Color(0xFFDBEAFE))
                                }
                            }
                        }
                    }
                }

                // Category Grid
                item {
                    Column(modifier = Modifier.padding(horizontal = sectionHorizontalPadding)) {
                        Text("Shop by category", fontSize = if (compact) 24.sp else 24.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary)
                        Spacer(Modifier.height(16.dp))
                        AdaptiveGrid(columns = if (compact) 2 else if (medium) 3 else 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
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
                                            modifier = Modifier.fillMaxWidth().padding(if (compact) 8.dp else 16.dp)
                                        ) {
                                            CategoryVisual(categoryId = cat.id, modifier = Modifier.size(if (compact) 56.dp else 64.dp))
                                            Spacer(Modifier.height(8.dp))
                                            Text(cat.name, fontWeight = FontWeight.Bold, fontSize = if (compact) 14.sp else 16.sp, color = AdaptiveTheme.colors.textPrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Why Adaptive Store
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AdaptiveTheme.colors.surfaceMuted)
                            .padding(vertical = if (compact) 40.dp else 64.dp, horizontal = sectionHorizontalPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.widthIn(max = 600.dp)
                        ) {
                            Text("Why Adaptive Store?", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = AdaptiveTheme.colors.textPrimary)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "This showcase is built entirely with AdaptiveKt, a premium UI toolkit for Compose Multiplatform. It demonstrates responsive layouts, shared state, and advanced navigation across Web, Mobile, and Desktop.", 
                                textAlign = TextAlign.Center,
                                color = AdaptiveTheme.colors.textSecondary,
                                lineHeight = if (compact) 24.sp else 28.sp,
                                fontSize = if (compact) 14.sp else 16.sp
                            )
                            Spacer(Modifier.height(24.dp))
                            AdaptiveButton(
                                text = "Learn more", 
                                onClick = { }, 
                                variant = AdaptiveButtonVariant.Ghost,
                                modifier = if (compact) Modifier.fillMaxWidth() else Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BenefitBadge(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AppIcon(icon, modifier = Modifier.size(16.dp), tint = Color(0xFF94A3B8))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color(0xFF94A3B8), fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProductCard(prod: io.github.adaptivekt.examples.ecommerce.model.Product, compact: Boolean = false, onClick: () -> Unit) {
    AdaptiveCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compact) 150.dp else 210.dp),
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
            Column(modifier = Modifier.padding(if (compact) 12.dp else 16.dp)) {
                Text(prod.name, fontWeight = FontWeight.Bold, fontSize = if (compact) 14.sp else 16.sp, maxLines = 1, color = AdaptiveTheme.colors.textPrimary)
                if (!compact) {
                    Spacer(Modifier.height(4.dp))
                    Text(prod.shortDescription, fontSize = 12.sp, color = AdaptiveTheme.colors.textSecondary, maxLines = 2)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("\$${prod.price}", color = AdaptiveTheme.colors.textPrimary, fontWeight = FontWeight.ExtraBold, fontSize = if (compact) 14.sp else 18.sp)
                        if (prod.oldPrice != null && !compact) {
                            Text("\$${prod.oldPrice}", color = AdaptiveTheme.colors.textMuted, fontSize = 12.sp, style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
                        }
                    }
                    AppIcon(AppIcons.Plus, modifier = Modifier.size(if (compact) 16.dp else 20.dp), tint = Color(0xFF3B82F6))
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
