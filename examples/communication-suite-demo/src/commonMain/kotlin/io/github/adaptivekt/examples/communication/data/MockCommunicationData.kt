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
    
    val allUsers = teamAlpha + supportDesk + currentUser

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
                participants = allUsers,
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
        // Add 6 more to reach 12 requirement
        for (i in 7..12) {
            list.add(
                Conversation(
                    id = "c_$i",
                    type = ConversationType.Direct,
                    title = "User $i",
                    participants = listOf(UserProfile("u_$i", "User $i", status = PresenceStatus.Offline), currentUser),
                    unreadCount = 0,
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
                    deliveryStatus = if (sender == currentUser) MessageDeliveryStatus.Read else MessageDeliveryStatus.Sent
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

        // Fill remaining messages to hit 80+ requirement
        for (c in 4..12) {
            val cId = "c_$c"
            for (i in 0..5) {
                list.add(
                    Message(
                        id = "m_${c}_$i",
                        conversationId = cId,
                        sender = if (i % 2 == 0) currentUser else allUsers.random(),
                        content = "Random placeholder message $i for conversation $c",
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
        
        list.add(
            CallRecord(
                id = "call_1",
                caller = teamAlpha[0],
                receiver = currentUser,
                timestamp = now.minus(30.minutes),
                durationSeconds = 0,
                type = CallType.Video,
                direction = CallDirection.Missed
            )
        )
        list.add(
            CallRecord(
                id = "call_2",
                caller = currentUser,
                receiver = teamAlpha[1],
                timestamp = now.minus(2.hours),
                durationSeconds = 1450,
                type = CallType.Audio,
                direction = CallDirection.Outgoing
            )
        )
        list.add(
            CallRecord(
                id = "call_3",
                caller = supportDesk[0],
                receiver = currentUser,
                timestamp = now.minus(1.days),
                durationSeconds = 300,
                type = CallType.Video,
                direction = CallDirection.Incoming
            )
        )
        return list
    }

    val conversations = generateConversations()
    val messages = generateMessages()
    val calls = generateCalls()
}
