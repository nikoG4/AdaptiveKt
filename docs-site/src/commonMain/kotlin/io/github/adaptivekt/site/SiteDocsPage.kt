package io.github.adaptivekt.site

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun SiteDocsPage() {
    PageHeader(
        eyebrow = "Docs",
        title = "Documentation source stays in Markdown",
        description = "The Wasm site focuses on live examples. Detailed component notes remain in the repository for now.",
    )
    Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
    AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
        listOf(
            "Component docs" to "docs/components/",
            "Development" to "docs/development/",
            "Roadmap" to "docs/roadmap/",
            "Historical notes" to "docs/adaptive-kt/",
        ).forEach { (title, body) ->
            item(span = 6) {
                AdaptiveCard {
                    SiteText(title, fontWeight = FontWeight.Bold)
                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                    SiteText(body, color = SiteMuted)
                    Spacer(modifier = androidx.compose.ui.Modifier.height(14.dp))
                    AdaptiveButton(
                        text = "Open on GitHub",
                        variant = AdaptiveButtonVariant.Secondary,
                        onClick = { openSiteUrl("https://github.com/adaptivekt/adaptive-kt/tree/main/$body") },
                    )
                }
            }
        }
    }
}
