package io.github.adaptivekt.examples.communication.ui.calls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.communication.data.MockCommunicationData
import io.github.adaptivekt.examples.communication.model.CallDirection
import io.github.adaptivekt.examples.communication.model.CallRecord
import io.github.adaptivekt.examples.communication.model.CallType
import io.github.adaptivekt.examples.communication.model.UserProfile
import io.github.adaptivekt.examples.communication.state.CommunicationState
import io.github.adaptivekt.examples.communication.ui.chat.formatRelativeTime
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveSection
import io.github.adaptivekt.examples.communication.ui.icons.DemoIcons
import androidx.compose.material3.Icon

@Composable
fun CallsArea(state: CommunicationState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.activeCallId != null -> {
                val call = MockCommunicationData.calls.find { it.id == state.activeCallId }
                if (call != null) {
                    ActiveCallScreen(state = state, call = call)
                } else {
                    io.github.adaptivekt.feedback.AdaptiveEmptyState(
                        title = "Call Not Found",
                        description = "The active call could not be located.",
                        actionLabel = "Go Back",
                        onAction = { state.activeCallId = null }
                    )
                }
            }
            state.incomingCallId != null -> {
                val call = MockCommunicationData.calls.find { it.id == state.incomingCallId }
                if (call != null) {
                    IncomingCallScreen(state = state, call = call)
                } else {
                    io.github.adaptivekt.feedback.AdaptiveEmptyState(
                        title = "Call Not Found",
                        description = "The incoming call could not be located.",
                        actionLabel = "Go Back",
                        onAction = { state.incomingCallId = null }
                    )
                }
            }
            else -> {
                CallsList(state = state)
            }
        }
    }
}

@Composable
fun CallsList(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Recent Calls", fontWeight = FontWeight.Bold) },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { Icon(DemoIcons.Call, contentDescription = "Start Audio Call") },
                    onClick = { state.activeCallId = MockCommunicationData.calls.first().id } // Mock starting a call
                )
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveButton("All", variant = if (state.callsFilter == "history") AdaptiveButtonVariant.Primary else AdaptiveButtonVariant.Secondary, onClick = { state.callsFilter = "history" })
            AdaptiveButton("Missed", variant = if (state.callsFilter == "missed") AdaptiveButtonVariant.Primary else AdaptiveButtonVariant.Secondary, onClick = { state.callsFilter = "missed" })
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                val calls = if (state.callsFilter == "missed") {
                    MockCommunicationData.calls.filter { it.direction == CallDirection.Missed }
                } else {
                    MockCommunicationData.calls
                }

                AdaptiveSection(title = if (state.callsFilter == "missed") "Missed Calls" else "Recent") {
                    calls.forEach { call ->
                        CallRow(call = call, state = state)
                    }
                    if (calls.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            AdaptiveText("No calls found.", color = AdaptiveTheme.colors.textMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CallRow(call: CallRecord, state: CommunicationState) {
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
                    CallDirection.Incoming -> DemoIcons.ArrowBack
                    CallDirection.Outgoing -> DemoIcons.ArrowForward
                    CallDirection.Missed -> DemoIcons.Close
                }

                val typeStr = if (call.type == CallType.Video) "Video" else "Audio"
                val timeStr = formatRelativeTime(call.timestamp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(callIcon, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (call.direction == CallDirection.Missed) AdaptiveTheme.colors.danger else AdaptiveTheme.colors.textMuted)
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
                content = { Icon(if (call.type == CallType.Video) DemoIcons.Face else DemoIcons.Call, contentDescription = "Call back") },
                onClick = {
                    if (call.direction == CallDirection.Incoming) {
                        state.incomingCallId = call.id
                    } else {
                        state.activeCallId = call.id
                    }
                }
            )
        }
    }
}

@Composable
fun IncomingCallScreen(state: CommunicationState, call: CallRecord) {
    val caller = call.caller
    Column(
        modifier = Modifier.fillMaxSize().background(AdaptiveTheme.colors.surfaceMuted),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AdaptiveText("Incoming Call", color = AdaptiveTheme.colors.textPrimary, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(32.dp))
        AdaptiveAvatar(name = caller.name, size = 120.dp)
        Spacer(modifier = Modifier.height(24.dp))
        AdaptiveText(caller.name, color = AdaptiveTheme.colors.textPrimary, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        AdaptiveText("Video Call", color = AdaptiveTheme.colors.textSecondary)

        Spacer(modifier = Modifier.height(80.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(AdaptiveTheme.colors.danger).clickable {
                    state.incomingCallId = null
                }, contentAlignment = Alignment.Center) {
                    Icon(DemoIcons.Close, contentDescription = "Decline", tint = AdaptiveTheme.colors.textInverse, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                AdaptiveText("Decline", color = AdaptiveTheme.colors.textPrimary)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(AdaptiveTheme.colors.success).clickable {
                    state.activeCallId = call.id
                    state.incomingCallId = null
                }, contentAlignment = Alignment.Center) {
                    Icon(DemoIcons.Call, contentDescription = "Accept", tint = AdaptiveTheme.colors.textInverse, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                AdaptiveText("Accept", color = AdaptiveTheme.colors.textPrimary)
            }
        }
    }
}

@Composable
fun ActiveCallScreen(state: CommunicationState, call: CallRecord) {
    val isIncoming = call.direction == CallDirection.Incoming || call.direction == CallDirection.Missed
    val otherPerson = if (isIncoming) call.caller else call.receiver

    Column(
        modifier = Modifier.fillMaxSize().background(AdaptiveTheme.colors.surfaceRaised),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        AdaptiveAvatar(name = otherPerson.name, size = 150.dp)
        Spacer(modifier = Modifier.height(24.dp))
        AdaptiveText(otherPerson.name, color = AdaptiveTheme.colors.textPrimary, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        AdaptiveText("02:45", color = AdaptiveTheme.colors.textPrimary, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdaptiveIconButton(
                content = { Icon(DemoIcons.Person, contentDescription = "Mute") },
                onClick = { state.demoState = "offline" }
            )
            Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(AdaptiveTheme.colors.danger).clickable {
                state.activeCallId = null
            }, contentAlignment = Alignment.Center) {
                Icon(DemoIcons.Close, contentDescription = "End Call", tint = AdaptiveTheme.colors.textInverse, modifier = Modifier.size(32.dp))
            }
            AdaptiveIconButton(
                content = { Icon(DemoIcons.Face, contentDescription = "Video") },
                onClick = { state.demoState = "offline" }
            )
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE, style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = style)
}
