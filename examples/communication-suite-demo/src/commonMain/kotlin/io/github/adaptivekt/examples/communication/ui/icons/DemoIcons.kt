package io.github.adaptivekt.examples.communication.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
fun DemoIcon(
    imageVector: Any?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = AdaptiveTheme.colors.textPrimary
) {
    Box(modifier = modifier.size(24.dp), contentAlignment = Alignment.Center) {
        Text(
            text = (imageVector as? String) ?: "•",
            color = tint,
            fontWeight = FontWeight.Bold
        )
    }
}

object DemoIcons {
    val Chat = "✉"
    val Contacts = "👤"
    val Call = "📞"
    val Settings = "⚙"
    val Search = "🔍"
    val Info = "ℹ"
    val Videocam = "📹"
    val Add = "+"
    val AttachFile = "📎"
    val InsertDriveFile = "📄"
    val CallMissed = "✖"
    val CallMade = "↗"
    val CallReceived = "↙"
}
