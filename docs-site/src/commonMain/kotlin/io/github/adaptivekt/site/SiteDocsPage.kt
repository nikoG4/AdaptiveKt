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
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun SiteDocsPage() {
    val topics = remember { docsTopics() }
    var selectedId by remember { mutableStateOf("getting-started") }
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
        onSelectedIdChange = { selectedId = it },
        onThisPage = listOf("Overview", "Basic usage", "Parameters", "Examples", "Theming", "Limitations"),
    ) {
        AdaptiveCard {
            AdaptiveBadge(selected.family, tone = AdaptiveBadgeTone.Info)
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            SiteText(selected.title, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp, maxLines = 3)
            Spacer(modifier = androidx.compose.ui.Modifier.height(10.dp))
            SiteText(selected.summary, color = SiteMuted, fontSize = 16.sp, maxLines = 8)
        }
        Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
        selected.content()
    }
}

private fun docsTopics(): List<DocsTopic> = listOf(
    DocsTopic(
        id = "getting-started",
        family = "Getting started",
        title = "Getting started",
        summary = "Set up AdaptiveKt from locally published artifacts while Maven Central is pending.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsCallout(
                title = "Current status",
                body = "AdaptiveKt is not published to Maven Central yet. Use local publishing while the namespace is being verified. The planned coordinates are documented so the migration to Maven Central is straightforward later.",
                tone = AdaptiveBadgeTone.Warning,
            )
            DocsExampleBlock(
                DocsExample(
                    title = "Publish local Maven artifacts",
                    description = "Run this from the AdaptiveKt repository. It writes artifacts to build/local-maven and does not publish anything remotely.",
                    code = "./gradlew publishAllPublicationsToLocalTestRepository",
                ) {
                    AdaptiveSurface(contentPadding = PaddingValues(16.dp)) {
                        Column {
                            SiteText("Local publishing / dry-run", fontWeight = FontWeight.Bold)
                            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                            SiteText("Outputs: build/local-maven", color = SiteMuted)
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
                    ComponentParameter("GROUP", "Gradle property", "io.github.nikog4.adaptivekt", true, "The Maven group configured once in gradle.properties."),
                    ComponentParameter("VERSION_NAME", "Gradle property", "0.1.0-alpha01", true, "The current local publishing version."),
                    ComponentParameter("build/local-maven", "Directory", "generated", true, "The local repository consumed by smoke tests and external projects."),
                ),
            )
        }
    },
    DocsTopic(
        id = "theme",
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
AdaptiveTheme(
    colorScheme = AdaptiveColorSchemes.defaultLight(),
) {
    App()
}
                    """,
                ) {
                    AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultLight()) {
                        AdaptiveCard {
                            SiteText("Theme preview", fontWeight = FontWeight.Bold)
                            Spacer(modifier = androidx.compose.ui.Modifier.height(10.dp))
                            AdaptiveButton("Primary action", onClick = {})
                        }
                    }
                },
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("colorScheme", "AdaptiveColorScheme", "AdaptiveColorSchemes.defaultLight()", false, "Semantic colors for surfaces, text, feedback tones and component states."),
                    ComponentParameter("shapes", "AdaptiveShapeScheme", "AdaptiveShapeScheme.default()", false, "Shape tokens such as medium, large and pill."),
                    ComponentParameter("typography", "AdaptiveTypography", "AdaptiveTypography.default()", false, "Text styles shared by the toolkit."),
                    ComponentParameter("states", "AdaptiveStateScheme", "AdaptiveStateScheme.default()", false, "Interaction state tokens for hover, pressed, selected and disabled states."),
                ),
            )
            DocsCallout("Dark mode", "Use AdaptiveColorSchemes.defaultDark() at the theme root. Components read tokens instead of hardcoded colors.", AdaptiveBadgeTone.Info)
        }
    },
    DocsTopic(
        id = "layout-system",
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
        id = "publishing",
        family = "Publishing",
        title = "Publishing status",
        summary = "Local publishing is configured and verified; Maven Central is intentionally not active yet.",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            DocsCallout(
                title = "No remote publish",
                body = "The repository has local publishing and consumer smoke tests. Maven Central release workflows are guarded and should not run until namespace, signing and release policy are finalized.",
                tone = AdaptiveBadgeTone.Danger,
            )
            DocsCodeBlock(
                title = "Local dry-run",
                code = """
./gradlew publishAllPublicationsToLocalTestRepository
./tools/verify-local-publishing-consumer.ps1
                """,
            )
            DocsParameterTable(
                listOf(
                    ComponentParameter("adaptive-core", "Artifact", "published locally", true, "Core breakpoints, tokens and theme foundation."),
                    ComponentParameter("adaptive-components", "Artifact", "published locally", true, "Reusable UI primitives."),
                    ComponentParameter("admin-demo", "Demo module", "not published", true, "Demo app remains in the build but excluded from Maven publishing."),
                    ComponentParameter("docs-site", "Site module", "not published", true, "GitHub Pages docs site remains in the build but excluded from Maven publishing."),
                ),
            )
        }
    },
    DocsTopic(
        id = "visual-verification",
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
        id = "roadmap",
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
        Spacer(modifier = androidx.compose.ui.Modifier.height(1.dp))
        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(10.dp))
        SiteText(text, color = SiteMuted, maxLines = 5)
    }
}
