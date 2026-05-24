package io.github.adaptivekt.admin.demo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveMenuItem
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
public fun DemoText(
    text: String,
    modifier: Modifier = Modifier,
    emphasis: Emphasis = Emphasis.Default,
    maxLines: Int = 1,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = when (emphasis) {
                Emphasis.Strong -> 18.sp
                Emphasis.Subtle -> 12.sp
                else -> 14.sp
            },
            fontWeight = when (emphasis) {
                Emphasis.Strong -> FontWeight.SemiBold
                else -> FontWeight.Normal
            },
            color = when (emphasis) {
                Emphasis.Strong -> Color(0xFF0F172A)
                Emphasis.Subtle -> Color(0xFF64748B)
                else -> Color(0xFF0F172A)
            },
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

public enum class Emphasis {
    Default,
    Strong,
    Subtle,
}

@Composable
public fun DemoThumbnail(
    label: String,
    tone: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(AdaptiveTokens.Radius.Medium))
            .background(tone, RoundedCornerShape(AdaptiveTokens.Radius.Medium))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(AdaptiveTokens.Radius.Medium)),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = initialsFor(label).take(2),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)),
            maxLines = 1,
        )
    }
}

@Composable
public fun AdminDemoTopBar(
    initialAccountMenuOpen: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DemoText(
            text = "AdaptiveKt Admin Demo",
            emphasis = Emphasis.Strong,
        )
        AccountMenu(initialExpanded = initialAccountMenuOpen)
    }
}

@Composable
private fun AccountMenu(
    initialExpanded: Boolean = false,
) {
    val (expanded, setExpanded) = remember { mutableStateOf(initialExpanded) }
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(AdaptiveTokens.Radius.Pill)

    Box {
        Row(
            modifier = Modifier
                .clip(shape)
                .background(if (hovered || expanded) Color(0xFFF1F5F9) else Color(0xFFFFFFFF), shape)
                .border(1.dp, Color(0xFFE2E8F0), shape)
                .hoverable(interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { setExpanded(!expanded) }
                .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.XSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        ) {
            AdaptiveAvatar(name = "Lina Torres", size = 32.dp)
            DemoText(text = "Lina", emphasis = Emphasis.Default)
        }

        if (expanded) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(0, 46),
                onDismissRequest = { setExpanded(false) },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .width(220.dp)
                        .clip(RoundedCornerShape(AdaptiveTokens.Radius.Large))
                        .background(Color.White, RoundedCornerShape(AdaptiveTokens.Radius.Large))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(AdaptiveTokens.Radius.Large))
                        .padding(AdaptiveTokens.Spacing.Small),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AdaptiveTokens.Spacing.Small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
                    ) {
                        AdaptiveAvatar(name = "Lina Torres", size = 36.dp)
                        Column {
                            DemoText(text = "Lina Torres", emphasis = Emphasis.Strong)
                            DemoText(text = "Admin workspace", emphasis = Emphasis.Subtle)
                        }
                    }
                    AdaptiveMenuItem(text = "Manage profile", onClick = { setExpanded(false) })
                    AdaptiveMenuItem(text = "Account settings", onClick = { setExpanded(false) })
                    AdaptiveMenuItem(text = "Sign out", destructive = true, onClick = { setExpanded(false) })
                }
            }
        }
    }
}

@Composable
public fun DemoCard(
    title: String,
    value: String,
    subtitle: String,
    indicator: String? = null,
) {
    AdaptiveCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp),
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.Large),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                text = title,
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (indicator != null) {
                DemoBadge(indicator)
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        BasicText(
            text = value,
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        BasicText(
            text = subtitle,
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
public fun DemoPanel(
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit,
) {
    AdaptiveCard(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(AdaptiveTokens.Spacing.Large),
    ) {
        DemoText(text = title, emphasis = Emphasis.Strong)
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
            DemoText(text = subtitle, emphasis = Emphasis.Subtle, maxLines = 2)
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        content()
    }
}

@Composable
public fun DemoBadge(text: String) {
    AdaptiveBadge(text = text, tone = badgeToneFor(text))
}

@Composable
public fun DemoStatusText(text: String) {
    val tone = badgeToneFor(text)
    val foreground = when (tone) {
        AdaptiveBadgeTone.Success -> Color(0xFF047857)
        AdaptiveBadgeTone.Warning -> Color(0xFFB45309)
        AdaptiveBadgeTone.Danger -> Color(0xFFB91C1C)
        AdaptiveBadgeTone.Info -> Color(0xFF1D4ED8)
        AdaptiveBadgeTone.Neutral -> Color(0xFF334155)
    }

    BasicText(
        text = text,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = foreground),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
public fun DemoToggleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                if (selected) Color(0xFF2563EB) else Color(0xFFF8FAFC),
                RoundedCornerShape(AdaptiveTokens.Radius.Pill),
            )
            .border(
                1.dp,
                if (selected) Color(0xFF2563EB) else Color(0xFFD7E0EA),
                RoundedCornerShape(AdaptiveTokens.Radius.Pill),
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) Color.White else Color(0xFF334155),
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun badgeToneFor(text: String): AdaptiveBadgeTone {
    val value = text.lowercase()
    return when {
        listOf("active", "paid", "in stock", "enabled").any { value.contains(it) } -> AdaptiveBadgeTone.Success
        listOf("pending", "leave", "low", "draft").any { value.contains(it) } -> AdaptiveBadgeTone.Warning
        listOf("overdue", "out", "disabled", "error").any { value.contains(it) } -> AdaptiveBadgeTone.Danger
        listOf("new", "info").any { value.contains(it) } -> AdaptiveBadgeTone.Info
        else -> AdaptiveBadgeTone.Neutral
    }
}

private fun initialsFor(name: String): String {
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "?"
    }
}
