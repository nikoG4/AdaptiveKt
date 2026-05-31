package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import io.github.adaptivekt.components.AdaptiveAnchoredDropdownMenu
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveAvatarShape
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveCarousel
import io.github.adaptivekt.components.AdaptiveCarouselTransition
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveMenuItem
import io.github.adaptivekt.components.AdaptiveMultiSelect
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.AdaptiveThumbnail
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveColorSchemes
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.data.AdaptiveDataColumn
import io.github.adaptivekt.data.AdaptiveDataContent
import io.github.adaptivekt.data.AdaptiveDataMobileRole
import io.github.adaptivekt.data.AdaptiveDataView
import io.github.adaptivekt.feedback.EmptyState
import io.github.adaptivekt.feedback.ErrorState
import io.github.adaptivekt.feedback.AdaptiveLoadingIndicatorStyle
import io.github.adaptivekt.feedback.LoadingState
import io.github.adaptivekt.forms.AdaptiveFormColumns
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.FieldSpan
import io.github.adaptivekt.forms.ValidationMessage
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.navigation.AdaptiveNavItem
import io.github.adaptivekt.navigation.AdaptiveNavigationScaffold
import io.github.adaptivekt.navigation.AdaptiveNavigationItemStyle
import io.github.adaptivekt.navigation.AdaptiveNavigationTree
import io.github.adaptivekt.navigation.AdaptiveNavigationTreeItem

@Composable
internal fun SiteComponentsPage() {
    PageHeader(
        eyebrow = "Live catalog",
        title = "Components rendered in Wasm",
        description = "Each example below uses the real AdaptiveKt component APIs. Snippets are embedded as text for this phase.",
    )
    Spacer(modifier = Modifier.height(24.dp))

    AdaptiveGrid(columns = 12, horizontalGap = 18.dp, verticalGap = 18.dp) {
        componentExamples().forEach { example ->
            item(span = 6) {
                SiteComponentDetailPage(example)
            }
        }
    }
}

internal data class LiveExample(
    val title: String,
    val description: String,
    val code: String,
    val content: @Composable () -> Unit,
)

@Composable
internal fun SiteComponentDetailPage(example: LiveExample) {
    AdaptiveCard {
        SiteText(example.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        SiteText(example.description, color = SiteMuted, maxLines = 3)
        Spacer(modifier = Modifier.height(16.dp))
        AdaptiveSurface(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(AdaptiveTokens.Spacing.Medium),
        ) {
            example.content()
        }
        Spacer(modifier = Modifier.height(16.dp))
        CodeBlock(example.code)
    }
}

private fun componentExamples(): List<LiveExample> = listOf(
    LiveExample(
        title = "AdaptiveTheme",
        description = "Shared colors, shapes, typography, and state tokens for AdaptiveKt primitives.",
        code = """AdaptiveTheme(
    colorScheme = AdaptiveColorSchemes.defaultLight().copy(
        primary = Color(0xFF0F766E),
    ),
) { App() }""",
    ) {
        val teal = AdaptiveColorSchemes.defaultLight().copy(
            primary = Color(0xFF0F766E),
            primarySubtle = Color(0xFFCCFBF1),
            primaryText = Color(0xFF134E4A),
        )
        AdaptiveTheme(colorScheme = teal) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AdaptiveButton("Themed", onClick = {})
                AdaptiveChip("Token", tone = AdaptiveChipTone.Primary, selected = true)
                AdaptiveBadge("Light")
            }
        }
    },
    LiveExample(
        title = "AdaptiveButton",
        description = "Button variants, sizes, and icon slots.",
        code = """AdaptiveButton("Save", onClick = {})
AdaptiveButton("Cancel", variant = AdaptiveButtonVariant.Secondary, onClick = {})""",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AdaptiveButton("Save", onClick = {})
            AdaptiveButton("Cancel", variant = AdaptiveButtonVariant.Secondary, onClick = {})
            AdaptiveButton("Delete", variant = AdaptiveButtonVariant.Danger, size = AdaptiveButtonSize.Small, onClick = {})
        }
    },
    LiveExample(
        title = "AdaptiveIconButton",
        description = "Compact icon actions for toolbars and rows.",
        code = """AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Search() }""",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Search() }
            AdaptiveIconButton(onClick = {}) { AdaptiveIcons.MoreVertical() }
            AdaptiveIconButton(onClick = {}, enabled = false) { AdaptiveIcons.Close(tint = Color(0xFF94A3B8)) }
        }
    },
    LiveExample(
        title = "AdaptiveBadge",
        description = "Semantic status labels for admin surfaces.",
        code = """AdaptiveBadge("Paid", tone = AdaptiveBadgeTone.Success)""",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveBadge("Paid", tone = AdaptiveBadgeTone.Success)
            AdaptiveBadge("Review", tone = AdaptiveBadgeTone.Warning)
            AdaptiveBadge("Blocked", tone = AdaptiveBadgeTone.Danger)
        }
    },
    LiveExample(
        title = "AdaptiveAvatar",
        description = "Initials fallback with circle or rounded shape.",
        code = """AdaptiveAvatar(name = "Alicia Romero")""",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            AdaptiveAvatar("Alicia Romero", size = 42.dp)
            AdaptiveAvatar("Noah Kim", size = 42.dp)
            AdaptiveAvatar("Team", size = 42.dp, shape = AdaptiveAvatarShape.Rounded)
        }
    },
    LiveExample(
        title = "AdaptiveThumbnail",
        description = "Object thumbnail with label fallback.",
        code = """AdaptiveThumbnail(label = "Router Gigabit")""",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdaptiveThumbnail("Router Gigabit", tone = Color(0xFFE0F2FE))
            AdaptiveThumbnail("Invoice PDF", tone = Color(0xFFDCFCE7))
            AdaptiveThumbnail("Warehouse", tone = Color(0xFFFFEDD5))
        }
    },
    LiveExample(
        title = "AdaptiveChip",
        description = "Compact tags and filter pills.",
        code = """AdaptiveChip("Active", selected = true, tone = AdaptiveChipTone.Primary)""",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveChip("Active", selected = true, tone = AdaptiveChipTone.Primary, onClick = {})
            AdaptiveChip("Syncing", tone = AdaptiveChipTone.Info)
            AdaptiveChip("Late", tone = AdaptiveChipTone.Warning)
        }
    },
    LiveExample(
        title = "AdaptiveCard / AdaptiveSurface",
        description = "Reusable containers for admin content.",
        code = """AdaptiveCard { AdaptiveSurface { ... } }""",
    ) {
        AdaptiveCard(contentPadding = PaddingValues(12.dp)) {
            SiteText("Card surface", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            AdaptiveSurface(contentPadding = PaddingValues(10.dp)) {
                SiteText("Nested neutral surface", color = SiteMuted)
            }
        }
    },
    LiveExample(
        title = "AdaptiveTextField",
        description = "Foundation text input with label and validation.",
        code = """AdaptiveTextField(value, onValueChange, label = "Company")""",
    ) {
        var value by remember { mutableStateOf("AdaptiveKt Inc.") }
        AdaptiveTextField(
            value = value,
            onValueChange = { value = it },
            label = "Company",
            validationMessage = if (value.isBlank()) "Company is required" else null,
        )
    },
    LiveExample(
        title = "AdaptiveSearchField",
        description = "Search input with clear affordance.",
        code = """AdaptiveSearchField(value = query, onValueChange = { query = it })""",
    ) {
        var query by remember { mutableStateOf("invoice") }
        AdaptiveSearchField(value = query, onValueChange = { query = it }, onClear = { query = "" })
    },
    LiveExample(
        title = "AdaptiveAnchoredDropdownMenu",
        description = "Popup menu anchored to a trigger.",
        code = """AdaptiveAnchoredDropdownMenu(expanded, onExpandedChange, anchor = { _, toggle -> ... })""",
    ) {
        var expanded by remember { mutableStateOf(false) }
        AdaptiveAnchoredDropdownMenu(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            anchor = { _, toggle ->
                AdaptiveButton("Actions", variant = AdaptiveButtonVariant.Secondary, onClick = toggle)
            },
        ) {
            AdaptiveMenuItem("Open", onClick = { expanded = false })
            AdaptiveMenuItem("Archive", onClick = { expanded = false })
            AdaptiveMenuItem("Delete", destructive = true, onClick = { expanded = false })
        }
    },
    LiveExample(
        title = "AdaptiveSelect",
        description = "Nullable single-select dropdown with local search.",
        code = """AdaptiveSelect(options, selectedOption, onOptionSelected, optionLabel = { it })""",
    ) {
        var selected by remember { mutableStateOf<String?>(null) }
        AdaptiveSelect(
            options = listOf("Open", "Pending", "Closed"),
            selectedOption = selected,
            onOptionSelected = { selected = it },
            optionLabel = { it },
            label = "Status",
            searchable = true,
            clearable = true,
        )
    },
    LiveExample(
        title = "AdaptiveMultiSelect",
        description = "Multi-select dropdown with selected chips, local search, overflow, and custom option rows.",
        code = """AdaptiveMultiSelect(
    options = teams,
    selectedOptions = selected,
    onSelectedOptionsChange = { selected = it },
    optionLabel = { it },
    maxVisibleChips = 2,
)""",
    ) {
        val teams = listOf("Operations", "Finance", "Support", "Security", "Sales")
        var selected by remember { mutableStateOf(listOf("Operations", "Finance", "Support")) }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AdaptiveMultiSelect(
                options = teams,
                selectedOptions = selected,
                onSelectedOptionsChange = { selected = it },
                optionLabel = { it },
                label = "Teams",
                placeholder = "Choose teams",
                searchable = true,
                clearable = true,
                maxVisibleChips = 2,
            )
            AdaptiveMultiSelect(
                options = listOf("Alicia Romero", "Noah Kim", "Marta Silva"),
                selectedOptions = listOf("Alicia Romero"),
                onSelectedOptionsChange = {},
                optionLabel = { it },
                label = "Custom options",
                optionContent = { name, selectedOption ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AdaptiveAvatar(name = name, size = 28.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            SiteText(name, fontWeight = FontWeight.Bold)
                        }
                        if (selectedOption) {
                            AdaptiveBadge("Selected")
                        }
                    }
                },
            )
        }
    },
    LiveExample(
        title = "AdaptiveCarousel",
        description = "Animated controlled carousel for compact admin summaries, onboarding cards, or feature panels.",
        code = """AdaptiveCarousel(
    items = cards,
    selectedIndex = selectedIndex,
    onSelectedIndexChange = { selectedIndex = it },
    transition = AdaptiveCarouselTransition.Slide,
) { item, index -> CardContent(item) }""",
    ) {
        val cards = listOf("Revenue overview", "Team activity", "Support health")
        var selectedIndex by remember { mutableStateOf(0) }
        AdaptiveCarousel(
            items = cards,
            selectedIndex = selectedIndex,
            onSelectedIndexChange = { selectedIndex = it },
            transition = AdaptiveCarouselTransition.Slide,
        ) { item, index ->
            Column {
                AdaptiveBadge("Slide ${index + 1}", tone = AdaptiveBadgeTone.Info)
                Spacer(modifier = Modifier.height(8.dp))
                SiteText(item, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                SiteText("Carousel content stays controlled by the caller.", color = SiteMuted)
            }
        }
    },
    LiveExample(
        title = "AdaptiveNavigationTree",
        description = "Controlled hierarchical navigation for nested admin sidebars and settings pages.",
        code = """AdaptiveNavigationTree(
    items = tree,
    selectedItemId = selected,
    onItemSelected = { selected = it.id },
    expandedItemIds = expanded,
    onExpandedItemIdsChange = { expanded = it },
)""",
    ) {
        val tree = remember {
            listOf(
                AdaptiveNavigationTreeItem(
                    id = "workspace",
                    label = "Workspace",
                    badge = "3",
                    children = listOf(
                        AdaptiveNavigationTreeItem("overview", "Overview"),
                        AdaptiveNavigationTreeItem("activity", "Activity"),
                    ),
                ),
                AdaptiveNavigationTreeItem(
                    id = "operations",
                    label = "Operations",
                    children = listOf(
                        AdaptiveNavigationTreeItem("employees", "Employees"),
                        AdaptiveNavigationTreeItem("invoices", "Invoices", badge = "New"),
                    ),
                ),
            )
        }
        var selected by remember { mutableStateOf("overview") }
        var expanded by remember { mutableStateOf(setOf("workspace", "operations")) }
        AdaptiveNavigationTree(
            items = tree,
            selectedItemId = selected,
            onItemSelected = { selected = it.id },
            expandedItemIds = expanded,
            onExpandedItemIdsChange = { expanded = it },
        )
    },
    LiveExample(
        title = "AdaptiveNavigationScaffold",
        description = "Small preview of the navigation shell with the default pill sidebar style.",
        code = """AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = selected,
    onItemSelected = { selected = it },
    navigationItemStyle = AdaptiveNavigationItemStyle.Pill,
) { padding -> ... }""",
    ) {
        var selected by remember { mutableStateOf("dashboard") }
        Box(modifier = Modifier.fillMaxWidth().height(260.dp).border(1.dp, SiteLine)) {
            AdaptiveNavigationScaffold(
                navItems = listOf(
                    AdaptiveNavItem("dashboard", "Dashboard"),
                    AdaptiveNavItem("orders", "Orders"),
                    AdaptiveNavItem("settings", "Settings"),
                ),
                selectedItemId = selected,
                onItemSelected = { selected = it },
                navigationItemStyle = AdaptiveNavigationItemStyle.Pill,
            ) { padding ->
                Box(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                    SiteText("Selected: $selected", fontWeight = FontWeight.Bold)
                }
            }
        }
    },
    LiveExample(
        title = "AdaptiveDataView",
        description = "Responsive data view with real columns and content state.",
        code = """AdaptiveDataView(state = AdaptiveDataContent(items), columns = columns)""",
    ) {
        val rows = listOf(
            DemoRow("Acme", "Paid", "$1,200"),
            DemoRow("Northwind", "Open", "$840"),
        )
        AdaptiveDataView(
            state = AdaptiveDataContent(rows),
            columns = listOf(
                AdaptiveDataColumn("customer", "Customer", weight = 2f, mobileRole = AdaptiveDataMobileRole.Title) { SiteText(it.customer, fontWeight = FontWeight.Bold) },
                AdaptiveDataColumn("status", "Status", mobileRole = AdaptiveDataMobileRole.Status) { SiteText(it.status) },
                AdaptiveDataColumn("amount", "Amount") { SiteText(it.amount) },
            ),
        )
    },
    LiveExample(
        title = "AdaptiveFormLayout",
        description = "Responsive sections, field spans, validation, and actions.",
        code = """AdaptiveFormLayout { section { field(label = "Name") { AdaptiveTextField(...) } } }""",
    ) {
        var name by remember { mutableStateOf("AdaptiveKt") }
        AdaptiveFormLayout(columns = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 2, large = 2)) {
            section(title = "Workspace") {
                field(label = "Name", span = FieldSpan.Half, required = true) {
                    AdaptiveTextField(value = name, onValueChange = { name = it }, placeholder = "Name")
                }
                field(label = "Plan", span = FieldSpan.Half, validationMessage = ValidationMessage("Preview account")) {
                    AdaptiveTextField(value = "Team", onValueChange = {}, enabled = false)
                }
            }
            actions {
                primary { AdaptiveButton("Save", onClick = {}) }
                secondary { AdaptiveButton("Reset", variant = AdaptiveButtonVariant.Secondary, onClick = {}) }
            }
        }
    },
    LiveExample(
        title = "Feedback states",
        description = "Empty, loading, and error states for workflows.",
        code = """EmptyState(title = "No results")
LoadingState(message = "Loading", indicatorStyle = AdaptiveLoadingIndicatorStyle.Dots)
ErrorState(title = "Unable to load")""",
    ) {
        AdaptiveGrid(columns = 12, horizontalGap = 10.dp, verticalGap = 10.dp) {
            item(span = 4) { Box(Modifier.heightIn(min = 180.dp)) { EmptyState("No results", description = "Try another filter.") } }
            item(span = 4) {
                Box(Modifier.heightIn(min = 180.dp)) {
                    LoadingState(
                        message = "Loading",
                        indicatorStyle = AdaptiveLoadingIndicatorStyle.Dots,
                    )
                }
            }
            item(span = 4) { Box(Modifier.heightIn(min = 180.dp)) { ErrorState("Unable to load", description = "Retry later.") } }
        }
    },
)

private data class DemoRow(
    val customer: String,
    val status: String,
    val amount: String,
)
