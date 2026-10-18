package io.github.adaptivekt.admin.demo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.ui.semantics.Role
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
import androidx.compose.ui.draw.shadow
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
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveElevationLevel
import io.github.adaptivekt.core.AdaptiveVisualStyle
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.core.AdaptiveTokens

@Composable
public fun DemoText(
    text: String,
    modifier: Modifier = Modifier,
    emphasis: Emphasis = Emphasis.Default,
    maxLines: Int = 1,
) {
    val modern = AdaptiveTheme.visualStyle == AdaptiveVisualStyle.Modern
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = when (emphasis) {
                Emphasis.Strong -> if (modern) 17.sp else 18.sp
                Emphasis.Subtle -> if (modern) AdaptiveTheme.typography.caption.fontSize else 12.sp
                else -> if (modern) AdaptiveTheme.typography.body.fontSize else 14.sp
            },
            lineHeight = if (modern) AdaptiveTheme.typography.body.lineHeight else TextStyle.Default.lineHeight,
            fontWeight = when (emphasis) {
                Emphasis.Strong -> FontWeight.SemiBold
                else -> FontWeight.Normal
            },
            color = when (emphasis) {
                Emphasis.Strong -> AdaptiveTheme.colors.textPrimary
                Emphasis.Subtle -> AdaptiveTheme.colors.textMuted
                else -> AdaptiveTheme.colors.textPrimary
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
            .border(1.dp, AdaptiveTheme.colors.border, RoundedCornerShape(AdaptiveTokens.Radius.Medium)),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = initialsFor(label).take(2),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary),
            maxLines = 1,
        )
    }
}

@Composable
public fun AdminDemoTopBar(
    initialAccountMenuOpen: Boolean = false,
    darkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
    modernStyle: Boolean = true,
    onVisualStyleToggle: () -> Unit = {},
) {
    val compact = rememberAdaptiveInfo().isCompact
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        ) {
            if (AdaptiveTheme.visualStyle == AdaptiveVisualStyle.Modern) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(AdaptiveTheme.shapes.medium)
                        .background(AdaptiveTheme.colors.primarySubtle),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = "A",
                        style = AdaptiveTheme.typography.subtitle.copy(
                            fontWeight = FontWeight.Bold,
                            color = AdaptiveTheme.colors.primaryText,
                        ),
                    )
                }
            }
            Column {
                DemoText(
                    text = if (compact) "AdaptiveKt" else "AdaptiveKt Workspace",
                    emphasis = Emphasis.Strong,
                )
                if (!compact && AdaptiveTheme.visualStyle == AdaptiveVisualStyle.Modern) {
                    BasicText(
                        text = "COMPONENT SYSTEM · BETA PREVIEW",
                        style = AdaptiveTheme.typography.caption.copy(color = AdaptiveTheme.colors.textMuted),
                        maxLines = 1,
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        ) {
            if (!compact) {
                DemoToggleChip(
                    text = if (modernStyle) "Modern" else "Classic",
                    selected = modernStyle,
                    onClick = onVisualStyleToggle,
                )
            }
            DemoToggleChip(
                text = if (darkTheme) "Dark" else "Light",
                selected = darkTheme,
                onClick = onThemeToggle,
            )
            AccountMenu(initialExpanded = initialAccountMenuOpen)
        }
    }
}

@Composable
private fun AccountMenu(
    initialExpanded: Boolean = false,
) {
    val (expanded, setExpanded) = remember { mutableStateOf(initialExpanded) }
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = AdaptiveTheme.shapes.pill

    Box {
        Row(
            modifier = Modifier
                .clip(shape)
                .background(if (hovered || expanded) AdaptiveTheme.colors.disabledBackground else AdaptiveTheme.colors.surface, shape)
                .border(1.dp, AdaptiveTheme.colors.border, shape)
                .hoverable(interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                ) { setExpanded(!expanded) }
                .padding(horizontal = AdaptiveTokens.Spacing.Small, vertical = AdaptiveTokens.Spacing.XSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        ) {
            AdaptiveAvatar(name = "Lina Torres", size = 32.dp)
            DemoText(text = "Lina", emphasis = Emphasis.Default)
            AdaptiveIcons.ChevronDown(size = 14.dp, tint = AdaptiveTheme.colors.textMuted)
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
                        .shadow(
                            elevation = AdaptiveTheme.elevations.resolve(AdaptiveElevationLevel.High),
                            shape = AdaptiveTheme.shapes.large,
                            ambientColor = AdaptiveTheme.elevations.shadowColor,
                            spotColor = AdaptiveTheme.elevations.shadowColor,
                        )
                        .clip(AdaptiveTheme.shapes.large)
                        .background(AdaptiveTheme.colors.surface, AdaptiveTheme.shapes.large)
                        .border(1.dp, AdaptiveTheme.colors.border, AdaptiveTheme.shapes.large)
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
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AdaptiveTheme.colors.textMuted),
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
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AdaptiveTheme.colors.textPrimary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        BasicText(
            text = subtitle,
            style = TextStyle(fontSize = 12.sp, color = AdaptiveTheme.colors.textMuted),
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
        AdaptiveBadgeTone.Success -> AdaptiveTheme.colors.success
        AdaptiveBadgeTone.Warning -> AdaptiveTheme.colors.warning
        AdaptiveBadgeTone.Danger -> AdaptiveTheme.colors.danger
        AdaptiveBadgeTone.Info -> AdaptiveTheme.colors.info
        AdaptiveBadgeTone.Neutral -> AdaptiveTheme.colors.textSecondary
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
    val colors = AdaptiveTheme.colors
    val shape = AdaptiveTheme.shapes.pill
    val source = remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val ms = AdaptiveTheme.motion.durationMillis(AdaptiveTheme.motion.fastMillis)
    val background by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.surfaceMuted,
        animationSpec = tween(durationMillis = ms),
        label = "Demo chip background",
    )
    val foreground by animateColorAsState(
        targetValue = if (selected) colors.textInverse else colors.textSecondary,
        animationSpec = tween(durationMillis = ms),
        label = "Demo chip foreground",
    )

    Box(
        modifier = Modifier
            .clip(shape)
            .background(background, shape)
            .border(
                if (focused) AdaptiveTheme.states.focusBorderWidth else 1.dp,
                if (focused) colors.focusRing else if (selected) colors.primary else colors.border,
                shape,
            )
            .selectable(
                selected = selected,
                interactionSource = source,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(horizontal = AdaptiveTokens.Spacing.Medium, vertical = AdaptiveTokens.Spacing.Small),
    ) {
        BasicText(
            text = text,
            style = AdaptiveTheme.typography.label.copy(
                fontWeight = FontWeight.SemiBold,
                color = foreground,
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
