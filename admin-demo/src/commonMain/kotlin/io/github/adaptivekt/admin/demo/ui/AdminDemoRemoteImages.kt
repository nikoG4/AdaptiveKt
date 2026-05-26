package io.github.adaptivekt.admin.demo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.core.AdaptiveTokens
import io.kamel.core.ExperimentalKamelApi
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalKamelApi::class)
@Composable
public fun DemoRemoteAvatar(
    name: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
) {
    if (avatarUrl.isNullOrBlank()) {
        AdaptiveAvatar(name = name, modifier = modifier, size = size)
        return
    }

    val shape = CircleShape
    KamelImage(
        resource = asyncPainterResource(data = avatarUrl),
        contentDescription = "$name avatar",
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(Color(0xFFEFF6FF), shape)
            .border(width = 1.dp, color = Color(0xFFBFDBFE), shape = shape),
        contentScale = ContentScale.Crop,
        onLoading = { AvatarFallback(name = name, size = size) },
        onFailure = { AvatarFallback(name = name, size = size) },
    )
}

@OptIn(ExperimentalKamelApi::class)
@Composable
public fun DemoRemoteThumbnail(
    label: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    fallbackTone: Color = Color(0xFFE8F1FF),
) {
    if (imageUrl.isNullOrBlank()) {
        ThumbnailFallback(label = label, tone = fallbackTone, modifier = modifier, size = size)
        return
    }

    val shape = RoundedCornerShape(AdaptiveTokens.Radius.Medium)
    KamelImage(
        resource = asyncPainterResource(data = imageUrl),
        contentDescription = "$label thumbnail",
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(fallbackTone, shape)
            .border(width = 1.dp, color = Color(0xFFE2E8F0), shape = shape),
        contentScale = ContentScale.Crop,
        onLoading = { ThumbnailFallback(label = label, tone = fallbackTone, size = size) },
        onFailure = { ThumbnailFallback(label = label, tone = fallbackTone, size = size) },
    )
}

@Composable
private fun AvatarFallback(name: String, size: Dp) {
    AdaptiveAvatar(name = name, size = size)
}

@Composable
private fun ThumbnailFallback(
    label: String,
    tone: Color,
    modifier: Modifier = Modifier,
    size: Dp,
) {
    DemoThumbnail(
        label = label,
        tone = tone,
        modifier = modifier.size(size),
    )
}
