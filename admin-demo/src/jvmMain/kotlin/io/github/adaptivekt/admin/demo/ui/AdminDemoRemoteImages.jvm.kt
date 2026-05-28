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
import io.github.adaptivekt.core.AdaptiveTokens
import io.kamel.core.ExperimentalKamelApi
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalKamelApi::class)
@Composable
public actual fun DemoRemoteAvatar(
    name: String,
    avatarUrl: String?,
    modifier: Modifier,
    size: Dp,
) {
    if (avatarUrl.isNullOrBlank()) {
        DemoAvatarFallback(name = name, modifier = modifier, size = size)
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
        onLoading = { DemoAvatarFallback(name = name, size = size) },
        onFailure = { DemoAvatarFallback(name = name, size = size) },
    )
}

@OptIn(ExperimentalKamelApi::class)
@Composable
public actual fun DemoRemoteThumbnail(
    label: String,
    imageUrl: String?,
    modifier: Modifier,
    size: Dp,
    fallbackTone: Color,
) {
    if (imageUrl.isNullOrBlank()) {
        DemoThumbnailFallback(label = label, tone = fallbackTone, modifier = modifier, size = size)
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
        onLoading = { DemoThumbnailFallback(label = label, tone = fallbackTone, size = size) },
        onFailure = { DemoThumbnailFallback(label = label, tone = fallbackTone, size = size) },
    )
}
