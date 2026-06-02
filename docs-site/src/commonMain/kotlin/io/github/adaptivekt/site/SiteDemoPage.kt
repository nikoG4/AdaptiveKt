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
            eyebrow = "Demo",
            title = "Admin demo stays separate",
            description = "The docs site is the landing and component catalog. The full admin application remains published as its own Wasm app under /demo/app/.",
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        AdaptiveCard {
            AdaptiveBadge("admin-demo", tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            SiteText("Open the complete adaptive admin app.", fontWeight = FontWeight.Bold)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            AdaptiveButton(
                text = "Open /demo/app/",
                onClick = { openSiteUrl("demo/app/") },
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
                                variant = AdaptiveButtonVariant.Secondary,
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
