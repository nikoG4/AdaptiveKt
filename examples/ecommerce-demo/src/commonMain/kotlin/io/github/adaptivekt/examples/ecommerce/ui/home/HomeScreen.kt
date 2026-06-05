package io.github.adaptivekt.examples.ecommerce.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun HomeScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            // Hero section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 450.dp)
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
                        Spacer(Modifier.height(24.dp))
                        Text(
                            "Build your perfect setup", 
                            fontSize = 56.sp, 
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 64.sp
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            "Adaptive tech essentials for work, gaming, and creative flow. High performance gear, designed for the modern professional.", 
                            fontSize = 20.sp, 
                            color = Color(0xFF94A3B8),
                            textAlign = TextAlign.Center,
                            lineHeight = 30.sp
                        )
                        Spacer(Modifier.height(40.dp))
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
                        Spacer(Modifier.height(48.dp))
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

            // Featured Collections
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text("Featured Collections", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(Modifier.height(24.dp))
                    AdaptiveGrid(columns = 2, horizontalGap = 24.dp, verticalGap = 24.dp) {
                        MockData.collections.forEach { col ->
                            item(span = 1) {
                                AdaptiveCard(
                                    onClick = {
                                        state.selectedCollectionId = col.id
                                        state.navigateTo(Screen.Products)
                                    },
                                    modifier = Modifier.height(240.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        // Placeholder for collection image
                                        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF1F5F9)), contentAlignment = Alignment.Center) {
                                            Text(col.name.take(1), fontSize = 64.sp, color = Color(0xFFCBD5E1), fontWeight = FontWeight.Bold)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))),
                                            contentAlignment = Alignment.BottomStart
                                        ) {
                                            Column(modifier = Modifier.padding(24.dp)) {
                                                Text(col.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                                Spacer(Modifier.height(8.dp))
                                                Text("Browse collection →", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
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
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Trending now", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        AdaptiveButton(
                            text = "View all", 
                            onClick = { state.navigateTo(Screen.Products) },
                            variant = AdaptiveButtonVariant.Ghost
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    AdaptiveGrid(columns = 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
                        MockData.products.filter { it.isFeatured }.take(4).forEach { prod ->
                            item(span = 1) {
                                ProductCard(prod, onClick = { state.navigateTo(Screen.ProductDetail(prod.id)) })
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
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(48.dp)
                ) {
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
                        // Visual placeholder
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            AppIcon(AppIcons.Package, modifier = Modifier.size(100.dp), tint = Color(0xFFDBEAFE))
                        }
                    }
                }
            }

            // Category Grid
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text("Shop by category", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Spacer(Modifier.height(24.dp))
                    AdaptiveGrid(columns = 4, horizontalGap = 16.dp, verticalGap = 16.dp) {
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
                                        Text(cat.iconSymbol, fontSize = 40.sp)
                                        Spacer(Modifier.height(12.dp))
                                        Text(cat.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                        .background(Color(0xFFF8FAFC))
                        .padding(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.widthIn(max = 600.dp)
                    ) {
                        Text("Why Adaptive Store?", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        Spacer(Modifier.height(24.dp))
                        Text(
                            "This showcase is built entirely with AdaptiveKt, a premium UI toolkit for Compose Multiplatform. It demonstrates responsive layouts, shared state, and advanced navigation across Web, Mobile, and Desktop.", 
                            textAlign = TextAlign.Center,
                            color = Color(0xFF475569),
                            lineHeight = 28.sp
                        )
                        Spacer(Modifier.height(32.dp))
                        AdaptiveButton(text = "Learn more about AdaptiveKt", onClick = { /* External link */ }, variant = AdaptiveButtonVariant.Ghost)
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
fun ProductCard(prod: io.github.adaptivekt.examples.ecommerce.model.Product, onClick: () -> Unit) {
    AdaptiveCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                // Image placeholder
                Text("📷", fontSize = 48.sp)
                
                // Badges
                Row(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    if (prod.isNew) {
                        Badge("NEW", Color(0xFF10B981))
                    } else if (prod.isSale) {
                        Badge("SALE", Color(0xFFEF4444))
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Text(prod.shortDescription, fontSize = 12.sp, color = Color(0xFF64748B), maxLines = 2)
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("\$${prod.price}", color = Color(0xFF0F172A), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        if (prod.oldPrice != null) {
                            Text("\$${prod.oldPrice}", color = Color(0xFF94A3B8), fontSize = 12.sp, style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
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
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
