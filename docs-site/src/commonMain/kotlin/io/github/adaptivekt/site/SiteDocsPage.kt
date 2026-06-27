package io.github.adaptivekt.site

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.core.AdaptiveColorSchemes
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveThemeMode
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun SiteDocsPage(
    selectedHash: String,
    onSelectedHashChange: (String) -> Unit,
    sectionId: String?,
    onSectionChange: (String) -> Unit,
) {
    val topics = remember { DocsRegistry.getTopics { docsTopics() } }
    val selectedId = DocsRegistry.resolveTopicId(selectedHash.takeIf { it.isNotBlank() } ?: DocsRegistry.TOPIC_GETTING_STARTED)
    val selected = topics.firstOrNull { it.id == selectedId } ?: topics.first()
    val navGroups = topics.groupBy { it.family }.map { (family, items) ->
        DocsNavGroup(
            title = family,
            items = items.map { DocsNavItem(it.id, it.title, it.summary) },
        )
    }

    DocsShell(
        eyebrow = "Documentation",
        title = "Everything explicit, from setup to components",
        description = "AdaptiveKt docs are written for public consumption: what each primitive does, how to use it, how it behaves responsively, and what is still intentionally pending.",
        navGroups = navGroups,
        selectedId = selected.id,
        onSelectedIdChange = onSelectedHashChange,
        onThisPage = selected.tocItems ?: listOf("Overview", "Basic usage", "Parameters", "Examples", "Theming", "Limitations"),
        onTocItemClick = { onSectionChange(it) },
        sectionId = sectionId,
    ) {
        DocsSectionAnchor(id = "overview", modifier = Modifier.fillMaxWidth()) {
            AdaptiveCard {
                AdaptiveBadge(selected.family, tone = AdaptiveBadgeTone.Info)
                Spacer(modifier = Modifier.height(12.dp))
                SiteText(selected.title, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp, maxLines = 3)
                Spacer(modifier = Modifier.height(10.dp))
                SiteText(selected.summary, color = SiteMuted, fontSize = 16.sp, maxLines = 8)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        selected.content()
    }
}

private fun docsTopics(): List<DocsTopic> = listOf(
    DocsTopic(
        id = DocsRegistry.TOPIC_GETTING_STARTED,
        family = "Getting started",
        title = "Getting started",
        summary = "Set up the published AdaptiveKt alpha from Maven Central.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsCallout(
                title = "Alpha available",
                body = "AdaptiveKt 0.1.0-alpha01 is published on Maven Central. The APIs are intended for early adoption and feedback while the toolkit moves toward a stable 0.1 line.",
                tone = AdaptiveBadgeTone.Success,
            )
            DocsExampleBlock(
                DocsExample(
                    title = "Install from Maven Central",
                    description = "Add the modules you need. Demo apps and docs-site are intentionally not published as library artifacts.",
                    code = """
repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("io.github.nikog4.adaptivekt:adaptive-core:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-layout:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-navigation:0.1.0-alpha01")
}
                    """,
                ) {
                    AdaptiveSurface(contentPadding = PaddingValues(16.dp)) {
                        Column {
                            SiteText("Maven Central alpha", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            SiteText("Group: io.github.nikog4.adaptivekt", color = SiteMuted)
                        }
                    }
                },
            )
            DocsCodeBlock(
                title = "Consume from another project",
                code = """
repositories {
    maven {
        url = uri("path/to/AdaptiveKt/build/local-maven")
    }
    mavenCentral()
    google()
}

dependencies {
    implementation("io.github.nikog4.adaptivekt:adaptive-core:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")
    implementation("io.github.nikog4.adaptivekt:adaptive-layout:0.1.0-alpha01")
}
                """,
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("groupId", "Maven coordinate", "io.github.nikog4.adaptivekt", true, "The published Maven Central group."),
                    ComponentParameter("version", "Maven coordinate", "0.1.0-alpha01", true, "The current published alpha version."),
                    ComponentParameter("artifacts", "Maven modules", "adaptive-core, adaptive-components, ...", true, "Library modules are published independently."),
                ),
            )
        }
    },
    DocsTopic(
        id = DocsRegistry.TOPIC_THEME,
        family = "Foundations",
        title = "AdaptiveTheme",
        summary = "Shared color, shape, typography and state tokens for all AdaptiveKt primitives.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsExampleBlock(
                DocsExample(
                    title = "Wrap your app",
                    description = "AdaptiveTheme provides the tokens used by buttons, cards, forms, data views and navigation.",
                    code = """
AdaptiveTheme(mode = AdaptiveThemeMode.System) {
    App()
}
                    """,
                ) {
                    AdaptiveTheme(mode = AdaptiveThemeMode.System) {
                        AdaptiveCard {
                            SiteText("Theme preview", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            AdaptiveButton("Primary action", onClick = {})
                        }
                    }
                },
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("mode", "AdaptiveThemeMode", "System", false, "System follows the platform color scheme; Light and Dark force a scheme."),
                    ComponentParameter("colorScheme", "AdaptiveColorScheme?", "null", false, "Optional explicit scheme. If null, mode selects light or dark defaults."),
                    ComponentParameter("shapes", "AdaptiveShapeScheme", "AdaptiveShapeScheme.default()", false, "Shape tokens such as medium, large and pill."),
                    ComponentParameter("typography", "AdaptiveTypography", "AdaptiveTypography.default()", false, "Text styles shared by the toolkit."),
                    ComponentParameter("states", "AdaptiveStateScheme", "AdaptiveStateScheme.default()", false, "Interaction state tokens for hover, pressed, selected and disabled states."),
                ),
            )
            DocsCallout("System theme detection", "Consumers can use AdaptiveTheme { App() } or AdaptiveTheme(mode = AdaptiveThemeMode.System) without writing platform expect/actual code.", AdaptiveBadgeTone.Info)
        }
    },
    DocsTopic(
        id = DocsRegistry.TOPIC_RESPONSIVE_NAV,
        family = "Navigation",
        title = "Responsive navigation behavior",
        summary = "Configure whether navigation becomes a sidebar, rail, bottom bar, drawer or hidden custom surface per breakpoint.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsCodeBlock(
                title = "Storefront preset",
                code = """
AdaptiveNavigationScaffold(
    navItems = items,
    selectedItemId = selected,
    onItemSelected = { id -> select(id) },
    navigationBehavior = AdaptiveNavigationDefaults.storefrontBehavior(),
) { padding ->
    StoreContent(Modifier.padding(padding))
}
                """,
            )
            DocsCodeBlock(
                title = "Custom placements",
                code = """
AdaptiveNavigationScaffold(
    navigationBehavior = AdaptiveNavigationBehavior(
        compact = AdaptiveNavigationPlacement.Drawer,
        medium = AdaptiveNavigationPlacement.Rail,
        expanded = AdaptiveNavigationPlacement.Sidebar,
        large = AdaptiveNavigationPlacement.Sidebar,
        overflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu
    ),
    navItems = items,
    selectedItemId = selected,
    onItemSelected = { id -> select(id) },
) { padding -> AppContent(padding) }
                """,
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("AdaptiveNavigationPlacement", "Enum", "Drawer/Rail/Sidebar/BottomBar/Hidden", true, "Placement chosen independently for each breakpoint."),
                    ComponentParameter("AdaptiveNavigationOverflowBehavior", "Enum", "MoreMenu", false, "MoreMenu or Scroll keep dense navigation usable when item counts grow."),
                    ComponentParameter("AdaptiveNavigationDefaults.adminBehavior()", "Preset", "Drawer/Rail/Sidebar", false, "Classic admin kit behavior."),
                    ComponentParameter("AdaptiveNavigationDefaults.storefrontBehavior()", "Preset", "BottomBar/Hidden", false, "Storefront behavior used by the ecommerce demo."),
                ),
            )
        }
    },
    DocsTopic(
        id = DocsRegistry.TOPIC_LAYOUT_SYSTEM,
        family = "Layouts",
        title = "Layout system",
        summary = "AdaptiveContent, AdaptiveContainer and AdaptiveGrid provide the responsive foundation for screens and docs pages.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsExampleBlock(
                DocsExample(
                    title = "Responsive grid",
                    description = "Use spans explicitly. On mobile, choose span 12 so cards become one column.",
                    code = """
AdaptiveGrid(columns = 12) {
    item(span = 12) { HeaderCard() }
    item(span = 6) { MetricCard() }
}
                    """,
                ) {
                    androidx.compose.foundation.layout.BoxWithConstraints {
                        val compact = maxWidth < 600.dp
                        AdaptiveGrid(columns = 12, horizontalGap = 10.dp, verticalGap = 10.dp) {
                            listOf("Compact" to "1 column", "Expanded" to "2-4 columns").forEach { (title, body) ->
                                item(span = if (compact) 12 else 6) {
                                    AdaptiveCard {
                                        SiteText(title, fontWeight = FontWeight.Bold)
                                        SiteText(body, color = SiteMuted)
                                    }
                                }
                            }
                        }
                    }
                },
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("columns", "Int", "12", false, "Total logical columns in the grid."),
                    ComponentParameter("horizontalGap", "Dp", "AdaptiveTokens.Spacing.Medium", false, "Gap between items in a row."),
                    ComponentParameter("verticalGap", "Dp", "AdaptiveTokens.Spacing.Medium", false, "Gap between rows."),
                    ComponentParameter("item(span)", "Int", "12", false, "Span per item. Use 12 for full-width mobile rows."),
                ),
            )
        }
    },
    DocsTopic(
        id = DocsRegistry.TOPIC_PUBLISHING,
        family = "Publishing",
        title = "Publishing status",
        summary = "AdaptiveKt 0.1.0-alpha01 is published to Maven Central; local dry-runs remain available.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsCallout(
                title = "Published alpha",
                body = "The first alpha is available from Maven Central. Future releases remain manual and guarded; local publishing and consumer smoke tests are kept for preflight verification.",
                tone = AdaptiveBadgeTone.Success,
            )
            DocsCodeBlock(
                title = "Local dry-run for maintainers",
                code = """
./gradlew publishAllPublicationsToLocalTestRepository
./tools/verify-local-publishing-consumer.ps1
                """,
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("adaptive-core", "Artifact", "published alpha", true, "Core breakpoints, tokens and theme foundation."),
                    ComponentParameter("adaptive-components", "Artifact", "published alpha", true, "Reusable UI primitives."),
                    ComponentParameter("admin-demo", "Demo module", "not published", true, "Demo app remains in the build but excluded from Maven publishing."),
                    ComponentParameter("docs-site", "Site module", "not published", true, "GitHub Pages docs site remains in the build but excluded from Maven publishing."),
                ),
            )
        }
    },
    DocsTopic(
        id = DocsRegistry.TOPIC_VISUAL_VERIFICATION,
        family = "Quality",
        title = "Visual verification",
        summary = "Desktop and web capture scripts keep responsive states visible before publishing changes.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsCodeBlock(
                title = "Docs and demo captures",
                code = """
./tools/capture-docs-site-web.ps1 -SkipBuild
./tools/capture-admin-demo-web.ps1
./tools/capture-admin-demo.ps1 -Theme both
                """,
            )
            androidx.compose.foundation.layout.BoxWithConstraints {
                val compact = maxWidth < 600.dp
                AdaptiveGrid(columns = 12, horizontalGap = 12.dp, verticalGap = 12.dp) {
                    listOf(
                        "Home compact" to "Checks hero, install block and navbar.",
                        "Components compact" to "Checks one-column docs and code blocks.",
                        "Components dark" to "Checks contrast and selected states.",
                        "Demo app" to "Checks the real admin UI remains usable.",
                    ).forEach { (title, body) ->
                        item(span = if (compact) 12 else 6) {
                            AdaptiveCard {
                                SiteText(title, fontWeight = FontWeight.Bold)
                                SiteText(body, color = SiteMuted, maxLines = 4)
                            }
                        }
                    }
                }
            }
        }
    },
    DocsTopic(
        id = DocsRegistry.TOPIC_ROADMAP,
        family = "Roadmap",
        title = "What is next",
        summary = "The public docs separate what works today from planned release and ecosystem work.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            listOf(
                "Verify Maven Central namespace and signing.",
                "Keep docs examples aligned with public APIs.",
                "Add more accessibility details per component.",
                "Expand platform-specific demos after publishing is unblocked.",
            ).forEach {
                AdaptiveCard {
                    RowLikeBullet(it)
                }
            }
        }
    },
)

@Composable
private fun RowLikeBullet(text: String) {
    androidx.compose.foundation.layout.Row {
        AdaptiveBadge("Next", tone = AdaptiveBadgeTone.Info)
        Spacer(modifier = Modifier.height(1.dp))
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(10.dp))
        SiteText(text, color = SiteMuted, maxLines = 5)
    }
}

