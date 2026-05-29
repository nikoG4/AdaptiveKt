package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun SiteHomePage(
    onOpenComponents: () -> Unit,
    onOpenDocs: () -> Unit,
    onOpenDemo: () -> Unit,
) {
    AdaptiveGrid(columns = 12, horizontalGap = 24.dp, verticalGap = 24.dp) {
        item(span = 7) {
            Column {
                AdaptiveBadge("KMP/Wasm site", tone = AdaptiveBadgeTone.Info)
                Spacer(modifier = Modifier.height(16.dp))
                SiteText("AdaptiveKt", fontSize = 58.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(10.dp))
                SiteText(
                    "Compose Multiplatform Admin UI Toolkit",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SiteAccent,
                )
                Spacer(modifier = Modifier.height(12.dp))
                SiteText(
                    "Build adaptive admin dashboards, data views, forms, navigation shells, feedback states, and reusable components with one Compose codebase.",
                    fontSize = 16.sp,
                    color = SiteMuted,
                    maxLines = 5,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AdaptiveButton("View Demo", onClick = onOpenDemo)
                    AdaptiveButton("Browse Components", variant = AdaptiveButtonVariant.Secondary, onClick = onOpenComponents)
                    AdaptiveButton("Read Docs", variant = AdaptiveButtonVariant.Ghost, onClick = onOpenDocs)
                }
            }
        }
        item(span = 5) {
            DashboardPreview()
        }
    }

    Spacer(modifier = Modifier.height(36.dp))

    AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
        listOf(
            "Desktop" to "JVM demo and capture tooling",
            "Android" to "Library targets enabled",
            "iOS" to "Declared, pending macOS validation",
            "Web/Wasm" to "Docs site and admin demo distribution",
        ).forEach { (title, body) ->
            item(span = 3) {
                AdaptiveCard {
                    SiteText(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    SiteText(body, color = SiteMuted, maxLines = 3)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(36.dp))

    PageHeader(
        eyebrow = "Features",
        title = "Built from the real toolkit",
        description = "This page is rendered by Compose Multiplatform/Wasm and uses AdaptiveKt components directly.",
    )
    Spacer(modifier = Modifier.height(20.dp))
    AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
        listOf(
            "Layouts" to "AdaptiveContent, containers, and grid primitives.",
            "Navigation" to "Admin shell primitives with responsive modes.",
            "DataView" to "Cards on compact screens, table on wider screens.",
            "Forms" to "Sections, fields, validation, and actions.",
            "Feedback" to "Empty, loading, and error states.",
            "Components" to "Buttons, badges, avatars, chips, fields, menus, and select.",
        ).forEach { (title, body) ->
            item(span = 4) { AdaptiveSurface { FeatureRow(title, body) } }
        }
    }
}

@Composable
private fun DashboardPreview() {
    AdaptiveCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                SiteText("Operations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                SiteText("Live component preview", color = SiteMuted)
            }
            AdaptiveBadge("Stable", tone = AdaptiveBadgeTone.Success)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveChip("Revenue", selected = true, tone = AdaptiveChipTone.Primary)
            AdaptiveChip("Orders", tone = AdaptiveChipTone.Info)
            AdaptiveChip("Risk", tone = AdaptiveChipTone.Warning)
        }
        Spacer(modifier = Modifier.height(16.dp))
        AdaptiveGrid(columns = 12, horizontalGap = 10.dp, verticalGap = 10.dp) {
            listOf("MRR" to "$42k", "Users" to "1,284", "Open" to "37", "SLA" to "99.9%").forEach { (label, value) ->
                item(span = 6) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 82.dp)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                            .border(1.dp, SiteLine, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                    ) {
                        Column {
                            SiteText(label, color = SiteMuted, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            SiteText(value, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).background(SiteAccent))
            Spacer(modifier = Modifier.width(8.dp))
            SiteText("Rendered with AdaptiveCard, AdaptiveBadge, AdaptiveChip, and AdaptiveGrid.", color = SiteMuted, maxLines = 2)
        }
    }
}
