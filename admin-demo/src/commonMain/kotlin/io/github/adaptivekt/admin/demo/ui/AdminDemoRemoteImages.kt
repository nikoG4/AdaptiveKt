package io.github.adaptivekt.admin.demo.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar

@Composable
public expect fun DemoRemoteAvatar(
    name: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
)

@Composable
public expect fun DemoRemoteThumbnail(
    label: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    fallbackTone: Color = Color(0xFFE8F1FF),
)

@Composable
public fun DemoAvatarFallback(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp,
) {
    AdaptiveAvatar(name = name, modifier = modifier, size = size)
}

@Composable
public fun DemoThumbnailFallback(
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
