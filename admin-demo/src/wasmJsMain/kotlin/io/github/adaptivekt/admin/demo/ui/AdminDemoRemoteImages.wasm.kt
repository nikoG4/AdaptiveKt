package io.github.adaptivekt.admin.demo.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
public actual fun DemoRemoteAvatar(
    name: String,
    avatarUrl: String?,
    modifier: Modifier,
    size: Dp,
) {
    DemoAvatarFallback(name = name, modifier = modifier, size = size)
}

@Composable
public actual fun DemoRemoteThumbnail(
    label: String,
    imageUrl: String?,
    modifier: Modifier,
    size: Dp,
    fallbackTone: Color,
) {
    DemoThumbnailFallback(label = label, tone = fallbackTone, modifier = modifier, size = size)
}
