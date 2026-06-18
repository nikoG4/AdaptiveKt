package io.github.adaptivekt.examples.communication.data

import io.github.adaptivekt.examples.communication.model.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object MockCommunicationData {
    val currentUser = UserProfile(
        id = "u_me",
        name = "Alex Johnson",
        status = PresenceStatus.Online
    )

    val teamAlpha = listOf(
        UserProfile("u_1", "Sarah Chen", status = PresenceStatus.Online),
        UserProfile("u_2", "Marcus Rodriguez", status = PresenceStatus.Busy),
        UserProfile("u_3", "Emma Watson", status = PresenceStatus.Away)
    )

    val supportDesk = listOf(
        UserProfile("u_4", "David Kim", status = PresenceStatus.Online),
        UserProfile("u_5", "Lisa Thorne", status = PresenceStatus.Offline)
    )

    val marketingTeam = listOf(
        UserProfile("u_6", "Rachel Green", status = PresenceStatus.Away),
        UserProfile("u_7", "Joey Tribbiani", status = PresenceStatus.Online),
        UserProfile("u_8", "Chandler Bing", status = PresenceStatus.Busy)
    )

    val externalPartners = listOf(
        UserProfile("u_9", "Phoebe Buffay", status = PresenceStatus.Offline),
        UserProfile("u_10", "Ross Geller", status = PresenceStatus.Online),
        UserProfile("u_11", "Monica Geller", status = PresenceStatus.Busy),
        UserProfile("u_12", "Gunther Centralperk", status = PresenceStatus.Away)
    )

    val contractors = listOf(
        UserProfile("u_13", "Janice Hosenstein", status = PresenceStatus.Online),
        UserProfile("u_14", "Mike Hannigan", status = PresenceStatus.Offline),
        UserProfile("u_15", "Richard Burke", status = PresenceStatus.Away),
        UserProfile("u_16", "Carol Willick", status = PresenceStatus.Online),
        UserProfile("u_17", "Susan Bunch", status = PresenceStatus.Busy)
    )

    val allUsers = teamAlpha + supportDesk + marketingTeam + externalPartners + contractors + currentUser

    private fun generateConversations(): List<Conversation> {
        val now = Clock.System.now()
        val list = mutableListOf<Conversation>()

        list.add(
            Conversation(
                id = "c_1",
                slug = "team-alpha",
                type = ConversationType.Channel,
                title = "team-alpha",
                participants = teamAlpha + currentUser,
                unreadCount = 3,
                isPinned = true,
                lastMessageAt = now.minus(5.minutes)
            )
        )
        list.add(
            Conversation(
                id = "c_2",
                slug = "support-desk",
                type = ConversationType.Channel,
                title = "support-desk",
                participants = supportDesk + currentUser,
                unreadCount = 0,
                isPinned = false,
                lastMessageAt = now.minus(2.hours)
            )
        )
        list.add(
            Conversation(
                id = "c_3",
                type = ConversationType.Direct,
                title = "Sarah Chen",
                participants = listOf(teamAlpha[0], currentUser),
                unreadCount = 1,
                isPinned = true,
                lastMessageAt = now.minus(10.minutes)
            )
        )
        list.add(
            Conversation(
                id = "c_4",
                type = ConversationType.Channel,
                title = "engineering",
                participants = allUsers.take(8),
                unreadCount = 15,
                isPinned = false,
                lastMessageAt = now.minus(1.days)
            )
        )
        list.add(
            Conversation(
                id = "c_5",
                type = ConversationType.Channel,
                title = "design",
                participants = teamAlpha + currentUser,
                unreadCount = 0,
                isPinned = false,
                lastMessageAt = now.minus(2.days)
            )
        )
        list.add(
            Conversation(
                id = "c_6",
                type = ConversationType.Direct,
                title = "Marcus Rodriguez",
                participants = listOf(teamAlpha[1], currentUser),
                unreadCount = 0,
                isPinned = false,
                lastMessageAt = now.minus(3.days)
            )
        )

        // Add more conversations to reach 14
        val additionalUserNames = listOf(
            "Emma Watson", "David Kim", "Lisa Thorne", "Rachel Green",
            "Joey Tribbiani", "Chandler Bing", "Phoebe Buffay", "Ross Geller"
        )
        for (i in 7..14) {
            val user = allUsers.find { it.name == additionalUserNames[i - 7] } ?: allUsers[i]
            list.add(
                Conversation(
                    id = "c_$i",
                    type = ConversationType.Direct,
                    title = user.name,
                    participants = listOf(user, currentUser),
                    unreadCount = if (i % 3 == 0) 2 else 0,
                    isPinned = false,
                    lastMessageAt = now.minus(i.days)
                )
            )
        }

        return list.sortedByDescending { it.lastMessageAt }
    }

    private fun generateMessages(): List<Message> {
        val list = mutableListOf<Message>()
        val now = Clock.System.now()

        // team-alpha messages
        val c1Id = "c_1"
        for (i in 0..30) {
            val sender = if (i % 3 == 0) currentUser else teamAlpha[i % teamAlpha.size]
            list.add(
                Message(
                    id = "m_1_$i",
                    conversationId = c1Id,
                    sender = sender,
                    content = "This is a mock message $i for team-alpha. We are discussing the new AdaptiveKt primitives.",
                    timestamp = now.minus((30 - i).hours).minus(5.minutes),
                    deliveryStatus = if (sender == currentUser) MessageDeliveryStatus.Read else MessageDeliveryStatus.Sent,
                    reactions = if (i == 5 || i == 15) listOf(MessageReaction("👍", 2, true), MessageReaction("🚀", 1, false)) else emptyList()
                )
            )
        }

        // Add a code snippet message
        list.add(
            Message(
                id = "m_1_code",
                conversationId = c1Id,
                sender = teamAlpha[0],
                content = "Check out this snippet:\n```kotlin\nfun hello() {\n    println(\"World\")\n}\n```",
                timestamp = now.minus(4.minutes),
                attachments = listOf(
                    MessageAttachment("att_1", "design_mockup.png", 1024 * 1024 * 2, "image/png")
                )
            )
        )

        // Sarah Chen DMs
        val c3Id = "c_3"
        for (i in 0..15) {
            val sender = if (i % 2 == 0) teamAlpha[0] else currentUser
            list.add(
                Message(
                    id = "m_3_$i",
                    conversationId = c3Id,
                    sender = sender,
                    content = "Hey, let's sync up on the roadmap later today. Message $i",
                    timestamp = now.minus((15 - i).minutes)
                )
            )
        }

        // Fill remaining messages without using .random()
        for (c in 4..14) {
            val cId = "c_$c"
            for (i in 0..5) {
                list.add(
                    Message(
                        id = "m_${c}_$i",
                        conversationId = cId,
                        sender = if (i % 2 == 0) currentUser else allUsers[(c + i) % allUsers.size],
                        content = "Deterministic mock message $i for conversation $c.",
                        timestamp = now.minus((10 * c).days).plus(i.hours)
                    )
                )
            }
        }

        return list
    }

    private fun generateCalls(): List<CallRecord> {
        val list = mutableListOf<CallRecord>()
        val now = Clock.System.now()

        // Ensure exactly 12 calls
        val callSetups = listOf(
            Triple(teamAlpha[0], CallDirection.Missed, CallType.Video),
            Triple(teamAlpha[1], CallDirection.Outgoing, CallType.Audio),
            Triple(supportDesk[0], CallDirection.Incoming, CallType.Video),
            Triple(marketingTeam[1], CallDirection.Missed, CallType.Audio),
            Triple(externalPartners[0], CallDirection.Outgoing, CallType.Video),
            Triple(contractors[2], CallDirection.Incoming, CallType.Audio),
            Triple(teamAlpha[2], CallDirection.Incoming, CallType.Video),
            Triple(supportDesk[1], CallDirection.Outgoing, CallType.Audio),
            Triple(marketingTeam[0], CallDirection.Missed, CallType.Video),
            Triple(externalPartners[2], CallDirection.Incoming, CallType.Audio),
            Triple(contractors[0], CallDirection.Outgoing, CallType.Video),
            Triple(marketingTeam[2], CallDirection.Missed, CallType.Audio)
        )

        callSetups.forEachIndexed { index, setup ->
            list.add(
                CallRecord(
                    id = "call_${index + 1}",
                    caller = if (setup.second == CallDirection.Incoming || setup.second == CallDirection.Missed) setup.first else currentUser,
                    receiver = if (setup.second == CallDirection.Incoming || setup.second == CallDirection.Missed) currentUser else setup.first,
                    timestamp = now.minus((index * 3 + 1).hours),
                    durationSeconds = if (setup.second == CallDirection.Missed) 0 else (index * 120 + 30),
                    type = setup.third,
                    direction = setup.second
                )
            )
        }

        return list.sortedByDescending { it.timestamp }
    }

    val conversations = generateConversations()
    val messages = generateMessages()
    val calls = generateCalls()
}
