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

    val labels = listOf(
        MailLabel("l_1", "Product", "#4CAF50"),
        MailLabel("l_2", "Security", "#F44336"),
        MailLabel("l_3", "Finance", "#FF9800"),
        MailLabel("l_4", "Legal", "#9C27B0"),
        MailLabel("l_5", "Hiring", "#2196F3")
    )

    private fun generateMailThreads(): List<MailThread> {
        val list = mutableListOf<MailThread>()
        val now = Clock.System.now()

        val myContact = MailContact("Alex Johnson", "alex@example.com")
        val pContact1 = MailContact("Product Team", "product@example.com")
        val sContact = MailContact("Security Ops", "security@example.com")

        // Product Launch
        list.add(
            MailThread(
                id = "t_1",
                subject = "AdaptiveKt v1.0 Launch Sequence",
                labels = listOf(labels[0]),
                priority = MailPriority.High,
                messages = listOf(
                    MailMessage(
                        id = "mm_1_1",
                        threadId = "t_1",
                        sender = pContact1,
                        recipients = listOf(myContact),
                        timestamp = now.minus(2.days),
                        body = "Hello team, \n\nWe are preparing for the v1.0 launch. Please review the attached assets.",
                        attachments = listOf(MailAttachment("a_1", "launch_plan.pdf", 4000000, "application/pdf")),
                        isRead = true
                    ),
                    MailMessage(
                        id = "mm_1_2",
                        threadId = "t_1",
                        sender = myContact,
                        recipients = listOf(pContact1),
                        timestamp = now.minus(1.days),
                        body = "Looks good. I will finalize the KMP targets.",
                        isRead = true
                    ),
                    MailMessage(
                        id = "mm_1_3",
                        threadId = "t_1",
                        sender = pContact1,
                        recipients = listOf(myContact),
                        timestamp = now.minus(2.hours),
                        body = "Great, let's proceed.",
                        isRead = false,
                        isStarred = true
                    )
                )
            )
        )

        // Security Review
        list.add(
            MailThread(
                id = "t_2",
                subject = "Q3 Security Audit Findings",
                labels = listOf(labels[1]),
                priority = MailPriority.High,
                messages = listOf(
                    MailMessage(
                        id = "mm_2_1",
                        threadId = "t_2",
                        sender = sContact,
                        recipients = listOf(myContact),
                        timestamp = now.minus(5.days),
                        body = "Please review the Q3 findings. No critical issues, but a few warnings.",
                        isRead = true
                    )
                )
            )
        )

        // Generate 18 more threads to hit 20
        for (i in 3..21) {
            val messages = mutableListOf<MailMessage>()
            for (j in 1..3) {
                messages.add(
                    MailMessage(
                        id = "mm_${i}_$j",
                        threadId = "t_$i",
                        sender = MailContact("Sender $i", "sender$i@example.com"),
                        recipients = listOf(myContact),
                        timestamp = now.minus(i.days).plus(j.hours),
                        body = "This is a generated email body for thread $i message $j. We need to make sure the email reading pane handles long text properly. AdaptiveKt is a powerful UI toolkit.",
                        isRead = j != 3 || i % 2 == 0
                    )
                )
            }
            list.add(
                MailThread(
                    id = "t_$i",
                    subject = "Generated Thread Subject $i",
                    labels = if (i % 4 == 0) listOf(labels.random()) else emptyList(),
                    folder = if (i % 7 == 0) MailFolder.Archive else MailFolder.Inbox,
                    messages = messages
                )
            )
        }

        return list.sortedByDescending { it.latestMessage?.timestamp ?: now }
    }

    val conversations = generateConversations()
    val messages = generateMessages()
    val mailThreads = generateMailThreads()
}
