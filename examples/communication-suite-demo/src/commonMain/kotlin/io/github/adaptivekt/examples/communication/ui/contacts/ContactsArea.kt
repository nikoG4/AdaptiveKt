package io.github.adaptivekt.examples.communication.ui.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.examples.communication.data.MockCommunicationData
import io.github.adaptivekt.examples.communication.model.PresenceStatus
import io.github.adaptivekt.examples.communication.model.UserProfile
import io.github.adaptivekt.examples.communication.state.CommunicationState
import io.github.adaptivekt.examples.communication.ui.icons.*
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.layout.AdaptiveActionBar
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold
import io.github.adaptivekt.layout.AdaptiveSection

@Composable
fun ContactsArea(state: CommunicationState) {
    Box(modifier = Modifier.fillMaxSize()) {
        AdaptiveListDetailScaffold(
            selectedItem = state.selectedContactId,
            onBackToList = { state.selectedContactId = null },
            listPane = {
                ContactsList(state = state)
            },
            detailPane = { selectedId ->
                val contact = MockCommunicationData.allUsers.find { it.id == selectedId }
                if (contact != null) {
                    ContactDetail(state = state, contact = contact)
                } else {
                    AdaptiveEmptyState(title = "Not found", description = "Contact not found.")
                }
            }
        )
    }
}

@Composable
fun ContactsList(state: CommunicationState) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Contacts", fontWeight = FontWeight.Bold) },
            secondaryActions = {
                AdaptiveIconButton(
                    content = { DemoIcon(DemoIcons.Search, contentDescription = "Search Contacts") },
                    onClick = { /* Search Contacts */ }
                )
                AdaptiveIconButton(
                    content = { DemoIcon(DemoIcons.Add, contentDescription = "Add Contact") },
                    onClick = { /* Add Contact */ }
                )
            }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val usersByPresence = MockCommunicationData.allUsers.groupBy { it.status }
            
            // Online
            val online = usersByPresence[PresenceStatus.Online].orEmpty()
            if (online.isNotEmpty()) {
                item {
                    AdaptiveSection(title = "Online (${online.size})") {
                        online.forEach { user ->
                            ContactRow(user = user, isSelected = user.id == state.selectedContactId) {
                                state.selectedContactId = user.id
                            }
                        }
                    }
                }
            }

            // Busy / Away
            val awayOrBusy = usersByPresence.filterKeys { it == PresenceStatus.Away || it == PresenceStatus.Busy }.values.flatten()
            if (awayOrBusy.isNotEmpty()) {
                item {
                    AdaptiveSection(title = "Busy & Away (${awayOrBusy.size})") {
                        awayOrBusy.forEach { user ->
                            ContactRow(user = user, isSelected = user.id == state.selectedContactId) {
                                state.selectedContactId = user.id
                            }
                        }
                    }
                }
            }

            // Offline
            val offline = usersByPresence[PresenceStatus.Offline].orEmpty()
            if (offline.isNotEmpty()) {
                item {
                    AdaptiveSection(title = "Offline (${offline.size})") {
                        offline.forEach { user ->
                            ContactRow(user = user, isSelected = user.id == state.selectedContactId) {
                                state.selectedContactId = user.id
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactRow(user: UserProfile, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) AdaptiveTheme.colors.surfaceMuted else AdaptiveTheme.colors.background
    Box(
        modifier = Modifier.fillMaxWidth().background(bgColor).clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AdaptiveAvatar(name = user.name, size = 48.dp)
                val badgeColor = when (user.status) {
                    PresenceStatus.Online -> AdaptiveTheme.colors.success
                    PresenceStatus.Busy -> AdaptiveTheme.colors.danger
                    PresenceStatus.Away -> AdaptiveTheme.colors.warning
                    PresenceStatus.Offline -> AdaptiveTheme.colors.textMuted
                }
                Box(modifier = Modifier.size(12.dp).background(badgeColor, androidx.compose.foundation.shape.CircleShape).align(Alignment.BottomEnd))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                AdaptiveText(
                    text = user.name, 
                    fontWeight = FontWeight.SemiBold,
                    color = AdaptiveTheme.colors.textPrimary,
                    maxLines = 1
                )
                AdaptiveText(
                    text = user.status.name, 
                    color = AdaptiveTheme.colors.textMuted,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ContactDetail(state: CommunicationState, contact: UserProfile) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdaptiveActionBar(
            leadingContent = { AdaptiveText("Profile", fontWeight = FontWeight.Bold) }
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            AdaptiveAvatar(name = contact.name, size = 120.dp)
            Spacer(modifier = Modifier.height(16.dp))
            AdaptiveText(contact.name, fontWeight = FontWeight.Bold, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            val statusColor = when (contact.status) {
                PresenceStatus.Online -> AdaptiveBadgeTone.Success
                PresenceStatus.Busy -> AdaptiveBadgeTone.Danger
                PresenceStatus.Away -> AdaptiveBadgeTone.Warning
                PresenceStatus.Offline -> AdaptiveBadgeTone.Neutral
            }
            AdaptiveBadge(contact.status.name, tone = statusColor)
            
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AdaptiveButton("Message", onClick = {
                    // Setup conversation or navigate
                    state.selectedContactId = null
                    // If we had a way to jump to chat we would do it here
                })
                AdaptiveButton("Call", onClick = {
                    // Initiate call
                })
            }
        }
    }
}

@Composable
private fun AdaptiveText(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontWeight: FontWeight? = null, maxLines: Int = Int.MAX_VALUE, style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium) {
    androidx.compose.material3.Text(text = text, modifier = modifier, color = color, fontWeight = fontWeight, maxLines = maxLines, style = style)
}
