package io.github.adaptivekt.examples.communication.ui.calls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.communication.data.MockCommunicationData
import io.github.adaptivekt.examples.communication.model.CallDirection
import io.github.adaptivekt.examples.communication.model.CallRecord
import io.github.adaptivekt.examples.communication.model.CallType
import io.github.adaptivekt.examples.communication.state.CommunicationState
import io.github.adaptivekt.examples.communication.ui.chat.formatRelativeTime
import io.github.adaptivekt.examples.communication.ui.icons.*
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveSection

@Composable
fun CallsArea(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Recent Calls", fontWeight = FontWeight.Bold) },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { DemoIcon(DemoIcons.Call, contentDescription = "Start Audio Call") },
                    onClick = { /* Start Audio Call */ }
                )
            }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                AdaptiveSection(title = "Recent") {
                    MockCommunicationData.calls.forEach { call ->
                        CallRow(call = call)
                    }
                }
            }
        }
    }
}

@Composable
fun CallRow(call: CallRecord) {
    val isIncoming = call.direction == CallDirection.Incoming || call.direction == CallDirection.Missed
    val otherPerson = if (isIncoming) call.caller else call.receiver
    
    Box(
        modifier = Modifier.fillMaxWidth().background(AdaptiveTheme.colors.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdaptiveAvatar(name = otherPerson.name, size = 48.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                AdaptiveText(
                    text = otherPerson.name, 
                    fontWeight = FontWeight.SemiBold,
                    color = if (call.direction == CallDirection.Missed) AdaptiveTheme.colors.danger else AdaptiveTheme.colors.textPrimary,
                    maxLines = 1
                )
                
                val callIcon = when (call.direction) {
                    CallDirection.Incoming -> DemoIcons.CallReceived
                    CallDirection.Outgoing -> DemoIcons.CallMade
                    CallDirection.Missed -> DemoIcons.CallMissed
                }
                
                val typeStr = if (call.type == CallType.Video) "Video" else "Audio"
                val timeStr = formatRelativeTime(call.timestamp)
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DemoIcon(callIcon, modifier = Modifier.size(16.dp), tint = if (call.direction == CallDirection.Missed) AdaptiveTheme.colors.danger else AdaptiveTheme.colors.textMuted)
                    Spacer(modifier = Modifier.width(4.dp))
                    AdaptiveText(
                        text = "$typeStr • $timeStr", 
                        color = AdaptiveTheme.colors.textMuted,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            AdaptiveIconButton(
                content = { DemoIcon(if (call.type == CallType.Video) DemoIcons.Videocam else DemoIcons.Call, contentDescription = "Call back") },
                onClick = { /* Call back */ }
            )
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE, style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = style)
}
