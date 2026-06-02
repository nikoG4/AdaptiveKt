package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveCarousel
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveMultiSelect
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTabs
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun SiteHomePage(
    onOpenComponents: () -> Unit,
    onOpenDocs: () -> Unit,
    onOpenDemo: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val compact = maxWidth < 760.dp

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(44.dp)) {
            AdaptiveGrid(columns = 12, horizontalGap = 26.dp, verticalGap = 26.dp) {
                item(span = if (compact) 12 else 7) {
                    HomeHeroText(
                        compact = compact,
                        onOpenComponents = onOpenComponents,
                        onOpenDocs = onOpenDocs,
                        onOpenDemo = onOpenDemo,
                    )
                }
                item(span = if (compact) 12 else 5) {
                    HomeProductPreview()
                }
            }

            HomeInstallSection(compact = compact)
            HomeFeatureSection(compact = compact)
            HomeWhySection(compact = compact, onOpenDocs = onOpenDocs)
        }
    }
}

@Composable
private fun HomeHeroText(
    compact: Boolean,
    onOpenComponents: () -> Unit,
    onOpenDocs: () -> Unit,
    onOpenDemo: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveKtLogo(
            symbolSize = if (compact) 54.dp else 64.dp,
            wordmarkSize = if (compact) 42.sp else 56.sp,
        )
        Spacer(modifier = Modifier.height(18.dp))
        SiteText(
            text = "Build adaptive Kotlin UIs across platforms",
            fontSize = if (compact) 36.sp else 56.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SiteInk,
            maxLines = if (compact) 4 else 3,
        )
        Spacer(modifier = Modifier.height(14.dp))
        SiteText(
            text = "AdaptiveKt is a Compose Multiplatform UI toolkit for responsive admin apps, dashboards, internal tools and documentation surfaces.",
            fontSize = 17.sp,
            color = SiteMuted,
            maxLines = 7,
        )
        Spacer(modifier = Modifier.height(18.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf("Kotlin", "Compose Multiplatform", "Desktop", "Web/Wasm", "Android", "iOS-ready", "Dark mode", "Theme presets").forEach {
                AdaptiveBadge(it, tone = AdaptiveBadgeTone.Info)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            AdaptiveButton("Get started", onClick = onOpenDocs)
            AdaptiveButton("Browse docs", variant = AdaptiveButtonVariant.Secondary, onClick = onOpenDocs)
            AdaptiveButton("View components", variant = AdaptiveButtonVariant.Secondary, onClick = onOpenComponents)
            AdaptiveButton("Open demo", variant = AdaptiveButtonVariant.Ghost, onClick = onOpenDemo)
            AdaptiveButton(
                text = "GitHub",
                variant = AdaptiveButtonVariant.Ghost,
                trailingIcon = { AdaptiveIcons.ChevronRight(size = 15.dp, tint = AdaptiveTheme.colors.textPrimary) },
                onClick = { openSiteUrl("https://github.com/nikoG4/AdaptiveKt") },
            )
        }
    }
}

@Composable
private fun HomeProductPreview() {
    var selectedStatus by remember { mutableStateOf("Active") }
    var selectedTeams by remember { mutableStateOf(listOf("Platform", "Design", "Support")) }
    var tab by remember { mutableStateOf("Overview") }
    var slide by remember { mutableStateOf(0) }

    AdaptiveCard {
        BoxWithConstraints {
            val compact = maxWidth < 600.dp
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        SiteText("Admin workspace", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        SiteText("Live AdaptiveKt preview", color = SiteMuted, maxLines = 2)
                    }
                    AdaptiveBadge("Stable", tone = AdaptiveBadgeTone.Success)
                }
                Spacer(modifier = Modifier.height(18.dp))
                AdaptiveTabs(
                    tabs = listOf("Overview", "Forms", "Data"),
                    selectedTab = tab,
                    onTabSelected = { tab = it },
                    tabLabel = { it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                AdaptiveGrid(columns = 12, horizontalGap = 12.dp, verticalGap = 12.dp) {
                    listOf("Revenue" to "$42k", "Users" to "1,284", "SLA" to "99.9%", "Open" to "37").forEach { (label, value) ->
                        item(span = if (compact) 12 else 6) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 84.dp)
                                    .background(AdaptiveTheme.colors.surfaceMuted, RoundedCornerShape(10.dp))
                                    .border(1.dp, SiteLine, RoundedCornerShape(10.dp))
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
                AdaptiveTextField(value = "AdaptiveKt Inc.", onValueChange = {}, label = "Workspace")
                Spacer(modifier = Modifier.height(12.dp))
                AdaptiveSelect(
                    options = listOf("Active", "Pending", "Paused"),
                    selectedOption = selectedStatus,
                    onOptionSelected = { selectedStatus = it ?: "Active" },
                    optionLabel = { it },
                    label = "Status",
                )
                Spacer(modifier = Modifier.height(12.dp))
                AdaptiveMultiSelect(
                    options = listOf("Platform", "Design", "Support", "Finance"),
                    selectedOptions = selectedTeams,
                    onSelectedOptionsChange = { selectedTeams = it },
                    optionLabel = { it },
                    label = "Teams",
                    maxVisibleChips = if (compact) 1 else 2,
                )
                Spacer(modifier = Modifier.height(16.dp))
                AdaptiveCarousel(
                    items = listOf("Ship admin screens", "Document components", "Verify visually"),
                    selectedIndex = slide,
                    onSelectedIndexChange = { slide = it },
                ) { item, index ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AdaptiveAvatar(name = item, size = 36.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            SiteText(item, fontWeight = FontWeight.Bold, maxLines = 2)
                            SiteText("Step ${index + 1} of the toolkit workflow", color = SiteMuted, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeInstallSection(compact: Boolean) {
    DocsSection(
        title = "Use it locally while Maven Central is prepared",
        description = "AdaptiveKt is not published to Maven Central yet. The current verified path is local publishing from this repository, followed by consuming the generated Maven artifacts from another project.",
    ) {
        AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
            item(span = if (compact) 12 else 5) {
                DocsCallout(
                    title = "Current status",
                    body = "Local publishing is available and smoke-tested. Planned Maven Central coordinates are documented, but remote publication is intentionally blocked until namespace verification is complete.",
                    tone = AdaptiveBadgeTone.Warning,
                )
            }
            item(span = if (compact) 12 else 7) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    DocsCodeBlock(
                        title = "Publish locally",
                        code = "./gradlew publishAllPublicationsToLocalTestRepository",
                    )
                    DocsCodeBlock(
                        title = "Consume local artifacts",
                        code = """
repositories {
    maven {
        url = uri("path/to/AdaptiveKt/build/local-maven")
    }
    mavenCentral()
    google()
}

dependencies {
    implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")
}
                        """,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AdaptiveSurface(contentPadding = PaddingValues(16.dp)) {
            Column {
                SiteText("Planned Maven Central coordinates", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                DocsCodeBlock(
                    title = "Planned after publication",
                    code = """implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")""",
                )
                Spacer(modifier = Modifier.height(8.dp))
                SiteText("These coordinates are planned and will become available only after Maven Central publication.", color = SiteMuted, maxLines = 4)
            }
        }
    }
}

@Composable
private fun HomeFeatureSection(compact: Boolean) {
    DocsSection(
        title = "A toolkit for serious adaptive admin UIs",
        description = "The docs site itself is rendered with Compose Multiplatform/Wasm and uses AdaptiveKt components directly.",
    ) {
        AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
            listOf(
                "Responsive layouts" to "Containers, grids and breakpoint-aware content for compact through large screens.",
                "Form primitives" to "Sections, labels, validation messages and action rows with consistent spacing.",
                "Data views" to "Tables on wide screens and mobile cards on compact screens.",
                "Navigation patterns" to "Responsive shell, navigation tree, breadcrumbs, tabs and disclosure patterns.",
                "Dark mode" to "Theme tokens drive backgrounds, text, selected states and hover states.",
                "Visual verification" to "Desktop and web capture tooling keep responsive regressions visible.",
            ).forEach { (title, body) ->
                item(span = if (compact) 12 else 4) {
                    AdaptiveSurface { FeatureRow(title, body) }
                }
            }
        }
    }
}

@Composable
private fun HomeWhySection(compact: Boolean, onOpenDocs: () -> Unit) {
    AdaptiveCard {
        AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
            item(span = if (compact) 12 else 7) {
                Column {
                    AdaptiveBadge("Why AdaptiveKt", tone = AdaptiveBadgeTone.Info)
                    Spacer(modifier = Modifier.height(12.dp))
                    SiteText("Default simple code should produce a professional UI.", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, maxLines = 4)
                    Spacer(modifier = Modifier.height(12.dp))
                    SiteText(
                        "AdaptiveKt reduces the repeated work of making admin screens responsive, usable and theme-consistent across Desktop, Android, iOS and Web/Wasm targets.",
                        color = SiteMuted,
                        maxLines = 7,
                    )
                }
            }
            item(span = if (compact) 12 else 5) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf(
                        "Less responsive boilerplate",
                        "Admin components by default",
                        "Multiplatform-first source sets",
                        "Theme and state tokens",
                        "Explicit docs with rendered examples",
                    ).forEach { AdaptiveChip(it, tone = AdaptiveChipTone.Primary, selected = true) }
                    Spacer(modifier = Modifier.height(4.dp))
                    AdaptiveButton("Start with the docs", onClick = onOpenDocs)
                }
            }
        }
    }
}
