package io.github.adaptivekt.examples.ecommerce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.ecommerce.model.Product

@Composable
fun ProductVisual(
    product: Product,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val palette = productPalette(product.categoryId)
    val icon = productIcon(product.categoryId)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(if (compact) 16.dp else 24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(palette.first, palette.second),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(if (compact) 120.dp else 190.dp)
                .offset(x = if (compact) 34.dp else 56.dp, y = if (compact) (-36).dp else (-54).dp)
                .background(Color.White.copy(alpha = 0.16f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(if (compact) 86.dp else 136.dp)
                .offset(x = if (compact) (-46).dp else (-76).dp, y = if (compact) 42.dp else 66.dp)
                .background(Color.White.copy(alpha = 0.13f), RoundedCornerShape(28.dp)),
        )
        Box(
            modifier = Modifier
                .size(if (compact) 78.dp else 116.dp)
                .clip(RoundedCornerShape(if (compact) 20.dp else 28.dp))
                .background(Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                imageVector = icon,
                tint = Color.White,
                modifier = Modifier.size(if (compact) 42.dp else 64.dp),
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(if (compact) 10.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = product.name.take(2).uppercase(),
                color = Color.White.copy(alpha = 0.88f),
                fontSize = if (compact) 10.sp else 12.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun CollectionVisual(
    title: String,
    modifier: Modifier = Modifier,
) {
    val palette = collectionPalette(title)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(palette.first, palette.second))),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = 80.dp, y = (-70).dp)
                .background(Color.White.copy(alpha = 0.14f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(128.dp)
                .offset(x = (-90).dp, y = 58.dp)
                .background(Color.White.copy(alpha = 0.13f), RoundedCornerShape(30.dp)),
        )
        Text(
            text = title.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }.take(2).joinToString(""),
            color = Color.White.copy(alpha = 0.82f),
            fontSize = 56.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Composable
fun CategoryVisual(
    categoryId: String,
    modifier: Modifier = Modifier,
) {
    val palette = productPalette(categoryId)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(palette.first, palette.second))),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(productIcon(categoryId), tint = Color.White, modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun productPalette(categoryId: String): Pair<Color, Color> {
    val dark = AdaptiveTheme.colors.background == Color(0xFF0F172A)
    return when (categoryId) {
        "cat-laptops" -> Color(0xFF2563EB) to Color(0xFF7C3AED)
        "cat-audio" -> Color(0xFF0891B2) to Color(0xFF0F766E)
        "cat-gaming" -> Color(0xFF7C3AED) to Color(0xFFDB2777)
        "cat-workspace" -> Color(0xFF475569) to Color(0xFF0F172A)
        "cat-smart-home" -> Color(0xFF059669) to Color(0xFF2563EB)
        "cat-cameras" -> Color(0xFFEA580C) to Color(0xFFDC2626)
        "cat-wearables" -> Color(0xFF0EA5E9) to Color(0xFF6366F1)
        else -> if (dark) Color(0xFF334155) to Color(0xFF1E293B) else Color(0xFF64748B) to Color(0xFF334155)
    }
}

private fun collectionPalette(title: String): Pair<Color, Color> = when {
    title.contains("New", ignoreCase = true) -> Color(0xFF2563EB) to Color(0xFF14B8A6)
    title.contains("Best", ignoreCase = true) -> Color(0xFF7C3AED) to Color(0xFFEC4899)
    title.contains("Desk", ignoreCase = true) -> Color(0xFF475569) to Color(0xFF0F172A)
    else -> Color(0xFFEA580C) to Color(0xFFDC2626)
}

private fun productIcon(categoryId: String): ImageVector = when (categoryId) {
    "cat-laptops" -> AppIcons.Package
    "cat-audio" -> AppIcons.Search
    "cat-gaming" -> AppIcons.Star
    "cat-workspace" -> AppIcons.Settings
    "cat-smart-home" -> AppIcons.Shield
    "cat-cameras" -> AppIcons.Package
    "cat-wearables" -> AppIcons.Heart
    else -> AppIcons.Package
}
