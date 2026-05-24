package io.github.adaptivekt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

public enum class AdaptiveAvatarShape {
    Circle,
    Rounded,
}

@Composable
public fun AdaptiveAvatar(
    name: String,
    modifier: Modifier = Modifier,
    image: (@Composable () -> Unit)? = null,
    size: Dp = 36.dp,
    shape: AdaptiveAvatarShape = AdaptiveAvatarShape.Circle,
) {
    val avatarShape = when (shape) {
        AdaptiveAvatarShape.Circle -> AdaptiveComponentDefaults.PillShape
        AdaptiveAvatarShape.Rounded -> AdaptiveComponentDefaults.LargeShape
    }
    val initials = initialsForName(name)

    Box(
        modifier = modifier
            .size(size)
            .clip(avatarShape)
            .background(avatarBackground(initials), avatarShape)
            .border(1.dp, Color(0xFFBFDBFE), avatarShape),
        contentAlignment = Alignment.Center,
    ) {
        if (image != null) {
            image()
        } else {
            BasicText(
                text = initials,
                style = TextStyle(
                    fontSize = avatarFontSize(size),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8),
                ),
                maxLines = 1,
            )
        }
    }
}

public fun initialsForName(name: String): String {
    val initials = name
        .trim()
        .split(Regex("\\s+"))
        .mapNotNull { part -> part.firstOrNull { it.isLetterOrDigit() } }
        .take(2)
        .joinToString("")
        .uppercase()

    return initials.ifBlank { "?" }
}

private fun avatarFontSize(size: Dp) = when {
    size < 32.dp -> 11.sp
    size < 44.dp -> 13.sp
    else -> 15.sp
}

private fun avatarBackground(initials: String): Color {
    val seed = initials.fold(0) { total, char -> total + char.code }
    val colors = listOf(
        Color(0xFFEFF6FF),
        Color(0xFFF0FDFA),
        Color(0xFFF5F3FF),
        Color(0xFFFFF7ED),
        Color(0xFFF0FDF4),
    )
    return colors[seed.mod(colors.size)]
}
