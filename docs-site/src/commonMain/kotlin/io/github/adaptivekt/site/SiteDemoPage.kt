package io.github.adaptivekt.site

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun SiteDemoPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        PageHeader(
            eyebrow = "Demos",
            title = "Showcase Applications",
            description = "Explore full applications built with AdaptiveKt to see how components, layout, theming, and validation come together in production-like scenarios.",
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        
        // AI Workspace Showcase
        AdaptiveCard {
            AdaptiveBadge("ai-workspace-demo", tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            SiteText("AI Workspace", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            SiteText("A complex SaaS/product layout demonstrating chat workspaces, prompt libraries, knowledge bases, and multi-pane AdaptiveListDetailScaffold.", color = SiteMuted, maxLines = 3)
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            AdaptiveButton(
                text = "Open AI Workspace",
                onClick = { openSiteUrl("examples/ai-workspace/") },
            )
        }
        
        // Ecommerce Showcase
        AdaptiveCard {
            AdaptiveBadge("ecommerce-demo", tone = AdaptiveBadgeTone.Success)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            SiteText("Adaptive Store", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            SiteText("A full frontend-only AdaptiveKt ecommerce showcase built in commonMain for Android, Desktop, Web/Wasm and iOS.", color = SiteMuted, maxLines = 3)
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            AdaptiveButton(
                text = "Open Adaptive Store",
                onClick = { openSiteUrl("examples/ecommerce/") },
            )
        }
        
        // Communication Suite Showcase
        AdaptiveCard {
            AdaptiveBadge("communication-suite-demo", tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            SiteText("Communication Suite", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            SiteText("Chat and mail productivity demo showcasing adaptive list-detail layouts, modal compose flows, selectable content and responsive panes.", color = SiteMuted, maxLines = 3)
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            AdaptiveButton(
                text = "Open Communication Suite",
                onClick = { openSiteUrl("examples/communication-suite/") },
            )
        }
        
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))

        // Admin Demo
        AdaptiveCard {
            AdaptiveBadge("admin-demo", tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            SiteText("Admin Dashboard", fontWeight = FontWeight.Bold)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            SiteText("The complete adaptive admin application with charts, tables, and complex forms.", color = SiteMuted, maxLines = 2)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            AdaptiveButton(
                text = "Open Admin Demo",
                onClick = { openSiteUrl("demo/app/") },
                variant = AdaptiveButtonVariant.Secondary
            )
        }
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        androidx.compose.foundation.layout.BoxWithConstraints {
            val compact = maxWidth < 600.dp
            AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
                listOf("Dashboard", "Employees", "Products", "Invoices", "Settings", "UI Kit").forEach { screen ->
                    item(span = if (compact) 12 else 4) {
                        AdaptiveCard {
                            SiteText(screen, fontWeight = FontWeight.Bold)
                            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                            SiteText("Available in the complete admin demo.", color = SiteMuted)
                            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
                            AdaptiveButton(
                                text = "Open",
                                variant = AdaptiveButtonVariant.Ghost,
                                onClick = { openSiteUrl("demo/app/?screen=${screen.lowercase().replace(" ", "-")}") },
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = androidx.compose.ui.Modifier.height(48.dp))
        SiteFooter()
    }
}
