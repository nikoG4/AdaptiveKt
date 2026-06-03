package io.github.adaptivekt.admin.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveAvatarShape
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveAccordion
import io.github.adaptivekt.components.AdaptiveBreadcrumbs
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveCarousel
import io.github.adaptivekt.components.AdaptiveCarouselTransition
import io.github.adaptivekt.components.AdaptiveChip
import io.github.adaptivekt.components.AdaptiveChipTone
import io.github.adaptivekt.components.AdaptiveDialog
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveDropdownMenu
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveMenuItem
import io.github.adaptivekt.components.AdaptiveMultiSelect
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveSectionHeader
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTabs
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.AdaptiveThumbnail
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.feedback.AdaptiveLoadingIndicatorStyle
import io.github.adaptivekt.feedback.AdaptiveLoadingState
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.navigation.AdaptiveNavigationTree
import io.github.adaptivekt.navigation.AdaptiveNavigationTreeItem
import io.github.adaptivekt.navigation.AdaptiveNavigationDensity
import io.github.adaptivekt.navigation.AdaptiveNavigationItemStyle

internal enum class ComponentsShowcaseSection(
    val title: String,
    val description: String,
) {
    Buttons("Buttons", "Button variants and icon button actions."),
    Badges("Badges", "Status badge tones and pill styling."),
    Avatars("Avatars", "Initials fallback, rounded mode, and image slots."),
    Cards("Cards", "Cards, surfaces, section headers, and dividers."),
    Dropdowns("Dropdowns", "Dropdown/menu panel and menu item states."),
    Fields("Fields", "TextField, SearchField, validation, disabled, and clear states."),
    Selects("Selects", "Single-select dropdown states, search, and clearing."),
    MultiSelects("MultiSelects", "Multi-select dropdowns with chips, search, and custom content."),
    Carousels("Carousels", "Animated carousel transitions, controls, indicators, and edge cases."),
    NavigationTree("Navigation tree", "Hierarchical sidebar navigation with controlled expansion."),
    Breadcrumbs("Breadcrumbs", "Clickable breadcrumb navigation for page hierarchy."),
    Accordions("Accordions", "Expandable sections for nested content and disclosure controls."),
    Tabs("Tabs", "Tabbed content navigation for compact section switching."),
    Dialogs("Dialogs", "Modal dialog surfaces for confirm/dismiss workflows."),
    Feedback("Feedback", "Animated loading states and feedback defaults."),
}

@Composable
internal fun ComponentsShowcaseScreen(
    focusSection: ComponentsShowcaseSection? = null,
    initialSelectExpanded: Boolean = false,
    initialMultiSelectExpanded: Boolean = false,
    initialDialogOpen: Boolean = false,
    externalContentScroll: Boolean = false,
) {
    val adaptiveInfo = rememberAdaptiveInfo()
    val cardSpan = if (adaptiveInfo.isCompact) 12 else 6
    val title = focusSection?.title ?: "UI Kit"
    val description = focusSection?.description ?: "Visual smoke test for shared AdaptiveKt components."

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (focusSection == null && !externalContentScroll) Modifier.verticalScroll(rememberScrollState()) else Modifier),
    ) {
        AdaptiveSectionHeader(
            title = title,
            description = description,
            action = {
                AdaptiveBadge(text = if (focusSection == null) "B3" else "B3.1", tone = AdaptiveBadgeTone.Info)
            },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))

        AdaptiveGrid(columns = 12, horizontalGap = AdaptiveTokens.Spacing.Large, verticalGap = AdaptiveTokens.Spacing.Large) {
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Buttons) {
                item(span = sectionSpan(focusSection, cardSpan)) { ButtonsSection() }
                item(span = sectionSpan(focusSection, cardSpan)) { IconButtonsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Badges) {
                item(span = sectionSpan(focusSection, cardSpan)) { BadgesSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Avatars) {
                item(span = sectionSpan(focusSection, cardSpan)) { AvatarsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Cards) {
                item(span = sectionSpan(focusSection, cardSpan)) { CardsAndSurfacesSection() }
                item(span = sectionSpan(focusSection, cardSpan)) { SectionHeaderDividerSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Dropdowns) {
                item(span = sectionSpan(focusSection, cardSpan)) { DropdownSection(initialExpanded = focusSection != null) }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Fields) {
                item(span = sectionSpan(focusSection, cardSpan)) { TextFieldsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Selects) {
                item(span = sectionSpan(focusSection, cardSpan)) {
                    SelectsSection(initialExpanded = initialSelectExpanded)
                }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.MultiSelects) {
                item(span = sectionSpan(focusSection, cardSpan)) {
                    MultiSelectsSection(initialExpanded = initialMultiSelectExpanded)
                }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Carousels) {
                item(span = sectionSpan(focusSection, cardSpan)) { CarouselsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.NavigationTree) {
                item(span = sectionSpan(focusSection, cardSpan)) { NavigationTreeSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Breadcrumbs) {
                item(span = sectionSpan(focusSection, cardSpan)) { BreadcrumbsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Accordions) {
                item(span = sectionSpan(focusSection, cardSpan)) { AccordionsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Tabs) {
                item(span = sectionSpan(focusSection, cardSpan)) { TabsSection() }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Dialogs) {
                item(span = sectionSpan(focusSection, cardSpan)) { DialogsSection(initialOpen = initialDialogOpen) }
            }
            if (focusSection == null || focusSection == ComponentsShowcaseSection.Feedback) {
                item(span = sectionSpan(focusSection, cardSpan)) { FeedbackSection() }
            }
        }
    }
}

private fun sectionSpan(focusSection: ComponentsShowcaseSection?, defaultSpan: Int): Int =
    if (focusSection == null) defaultSpan else 12

@Composable
private fun ButtonsSection() {
    ShowcaseCard(title = "Buttons", description = "Variants, sizes, disabled state, and inline slots.") {
        ShowcaseRow {
            AdaptiveButton("Small", size = AdaptiveButtonSize.Small, onClick = {})
            AdaptiveButton("Medium", size = AdaptiveButtonSize.Medium, onClick = {})
            AdaptiveButton("Large", size = AdaptiveButtonSize.Large, onClick = {})
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        ShowcaseRow {
            AdaptiveButton("Secondary", variant = AdaptiveButtonVariant.Secondary, onClick = {})
            AdaptiveButton("Ghost", variant = AdaptiveButtonVariant.Ghost, onClick = {})
            AdaptiveButton("Danger", variant = AdaptiveButtonVariant.Danger, onClick = {})
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        ShowcaseRow {
            AdaptiveButton("Disabled", enabled = false, onClick = {})
            AdaptiveButton(
                text = "Create",
                leadingIcon = { AdaptiveIcons.Plus(size = 16.dp, tint = AdaptiveTheme.colors.textInverse) },
                trailingIcon = { AdaptiveIcons.ChevronRight(size = 16.dp, tint = AdaptiveTheme.colors.textInverse) },
                onClick = {},
            )
        }
    }
}

@Composable
private fun IconButtonsSection() {
    ShowcaseCard(title = "Icon buttons", description = "Shape-safe icon actions for overflow, clear, close, and disabled states.") {
        ShowcaseRow {
            AdaptiveIconButton(onClick = {}) { AdaptiveIcons.MoreVertical() }
            AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Close() }
            AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Search() }
            AdaptiveIconButton(onClick = {}) { AdaptiveIcons.ChevronDown() }
            AdaptiveIconButton(onClick = {}, enabled = false) {
                AdaptiveIcons.Plus(tint = AdaptiveTheme.colors.disabledText)
            }
        }
    }
}

@Composable
private fun BadgesSection() {
    ShowcaseCard(title = "Badges", description = "Pill badges with neutral, semantic, and informational tones.") {
        ShowcaseRow {
            AdaptiveBadge("Neutral")
            AdaptiveBadge("Success", tone = AdaptiveBadgeTone.Success)
            AdaptiveBadge("Warning", tone = AdaptiveBadgeTone.Warning)
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        ShowcaseRow {
            AdaptiveBadge("Danger", tone = AdaptiveBadgeTone.Danger)
            AdaptiveBadge("Info", tone = AdaptiveBadgeTone.Info)
        }
    }
}

@Composable
private fun AvatarsSection() {
    ShowcaseCard(title = "Avatars", description = "Initials fallback, rounded mode, and image slot.") {
        ShowcaseRow {
            AdaptiveAvatar(name = "Alicia Romero", size = 40.dp)
            AdaptiveAvatar(name = "David", size = 40.dp)
            AdaptiveAvatar(name = "", size = 40.dp)
            AdaptiveAvatar(name = "Noah Kim", size = 40.dp, shape = AdaptiveAvatarShape.Rounded)
            AdaptiveAvatar(
                name = "Image Slot",
                size = 44.dp,
                shape = AdaptiveAvatarShape.Rounded,
                image = {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(AdaptiveTheme.colors.infoSubtle),
                        contentAlignment = Alignment.Center,
                    ) {
                        Glyph("IMG")
                    }
                },
            )
        }
    }
}

@Composable
private fun CardsAndSurfacesSection() {
    var clicked by remember { mutableStateOf(false) }

    ShowcaseCard(title = "Cards and surfaces", description = "Shared containers with professional defaults.") {
        AdaptiveSurface {
            Column {
                Label("AdaptiveSurface")
                Body("A neutral panel for grouped admin content.")
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveCard {
            Label("AdaptiveCard")
            Body("Default padding, border, and radius.")
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveCard(onClick = { clicked = !clicked }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Label("Clickable card")
                    Body(if (clicked) "Clicked state toggled." else "Tap to toggle demo state.")
                }
                AdaptiveBadge(if (clicked) "Selected" else "Idle", tone = if (clicked) AdaptiveBadgeTone.Success else AdaptiveBadgeTone.Neutral)
            }
        }
    }
}

@Composable
private fun DropdownSection(initialExpanded: Boolean = false) {
    var expanded by remember(initialExpanded) { mutableStateOf(initialExpanded) }

    ShowcaseCard(title = "Dropdown and menu", description = "Simple menu panel for account and overflow actions.") {
        AdaptiveButton(
            text = if (expanded) "Close menu" else "Open menu",
            variant = AdaptiveButtonVariant.Secondary,
            trailingIcon = { AdaptiveIcons.ChevronDown(size = 16.dp) },
            onClick = { expanded = !expanded },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            AdaptiveMenuItem("Manage profile", leadingIcon = { Glyph("P") }, onClick = { expanded = false })
            AdaptiveMenuItem("Account settings", leadingIcon = { Glyph("S") }, onClick = { expanded = false })
            AdaptiveMenuItem("Sign out", destructive = true, onClick = { expanded = false })
        }
    }
}

@Composable
private fun TextFieldsSection() {
    var company by remember { mutableStateOf("AdaptiveKt Inc.") }
    var empty by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("invoice") }

    ShowcaseCard(title = "Text fields", description = "Foundation inputs with labels, validation, and search affordances.") {
        AdaptiveTextField(
            value = company,
            onValueChange = { company = it },
            label = "Company name",
            placeholder = "Company name",
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveTextField(
            value = empty,
            onValueChange = { empty = it },
            placeholder = "Placeholder only",
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveTextField(
            value = "Locked workspace",
            onValueChange = {},
            label = "Disabled",
            enabled = false,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveTextField(
            value = "",
            onValueChange = {},
            label = "Required field",
            placeholder = "Required value",
            validationMessage = "This field is required.",
            leadingIcon = { AdaptiveIcons.Search(size = 16.dp, tint = AdaptiveTheme.colors.textMuted) },
            trailingIcon = { AdaptiveIcons.Check(size = 16.dp, tint = AdaptiveTheme.colors.success) },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveSearchField(
            value = query,
            onValueChange = { query = it },
            onClear = { query = "" },
        )
    }
}

@Composable
private fun SelectsSection(initialExpanded: Boolean = false) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    ShowcaseCard(
        title = "Selects",
        description = "Single-select dropdown with clear and search states.",
    ) {
        AdaptiveSelect(
            options = listOf("Option A", "Option B", "Option C"),
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
            optionLabel = { it },
            label = "Choose an option",
            placeholder = "Pick one",
            searchable = true,
            clearable = true,
            initialExpanded = initialExpanded,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveSelect(
            options = listOf("USD", "EUR", "PYG"),
            selectedOption = "USD",
            onOptionSelected = {},
            optionLabel = { it },
            label = "Disabled select",
            enabled = false,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveSelect(
            options = listOf("Operations", "Finance", "Support"),
            selectedOption = null,
            onOptionSelected = {},
            optionLabel = { it },
            label = "Team",
            placeholder = "Required team",
            isError = true,
            supportingText = "Choose a team before saving.",
        )
    }
}

@Composable
private fun MultiSelectsSection(initialExpanded: Boolean = false) {
    val departments = listOf("Operations", "Finance", "Support", "Sales", "Security")
    val people = listOf(
        DemoPerson("Alicia Romero", "Product Manager", "alicia@adaptivekt.io"),
        DemoPerson("Noah Kim", "UX Designer", "noah@adaptivekt.io"),
        DemoPerson("Marta Silva", "QA Lead", "marta@adaptivekt.io"),
        DemoPerson("Dina Patel", "Security Analyst", "dina@adaptivekt.io"),
    )
    var selectedSimple by remember { mutableStateOf(listOf("Operations", "Finance")) }
    var selectedSearchable by remember { mutableStateOf(listOf("Support")) }
    var selectedOverflow by remember { mutableStateOf(departments.take(4)) }
    var selectedPeople by remember { mutableStateOf(people.take(2)) }
    var selectedCustomChips by remember { mutableStateOf(listOf("High priority", "Needs review")) }

    ShowcaseCard(
        title = "MultiSelects",
        description = "Multi-select dropdowns with removable chips, search, overflow, and custom rows.",
    ) {
        AdaptiveMultiSelect(
            options = departments,
            selectedOptions = selectedSimple,
            onSelectedOptionsChange = { selectedSimple = it },
            optionLabel = { it },
            label = "Departments",
            placeholder = "Choose departments",
            initialExpanded = initialExpanded,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = departments,
            selectedOptions = selectedSearchable,
            onSelectedOptionsChange = { selectedSearchable = it },
            optionLabel = { it },
            label = "Searchable teams",
            searchable = true,
            clearable = true,
            maxVisibleChips = 2,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = departments,
            selectedOptions = selectedOverflow,
            onSelectedOptionsChange = { selectedOverflow = it },
            optionLabel = { it },
            label = "Overflow chips",
            maxVisibleChips = 1,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = departments,
            selectedOptions = listOf("Finance", "Security"),
            onSelectedOptionsChange = {},
            optionLabel = { it },
            label = "Disabled multi-select",
            enabled = false,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = people,
            selectedOptions = selectedPeople,
            onSelectedOptionsChange = { selectedPeople = it },
            optionLabel = { it.name },
            label = "People picker",
            maxVisibleChips = 2,
            searchable = true,
            optionContent = { person, selected ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AdaptiveAvatar(name = person.name, size = 28.dp)
                        Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                        Column {
                            Label(person.name)
                            Body("${person.role} · ${person.email}")
                        }
                    }
                    if (selected) {
                        AdaptiveBadge("Selected")
                    }
                }
            },
            chipContent = { person ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AdaptiveAvatar(name = person.name, size = 20.dp)
                    Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.XSmall))
                    BasicText(
                        text = person.name,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AdaptiveTheme.colors.primaryText,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = listOf("High priority", "Needs review", "Escalated", "Customer visible"),
            selectedOptions = selectedCustomChips,
            onSelectedOptionsChange = { selectedCustomChips = it },
            optionLabel = { it },
            label = "Custom chip content",
            chipContent = { option ->
                BasicText(
                    text = option,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AdaptiveTheme.colors.primaryText,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = departments,
            selectedOptions = emptyList(),
            onSelectedOptionsChange = {},
            optionLabel = { it },
            label = "Required teams",
            placeholder = "Pick at least one",
            isError = true,
            supportingText = "Select at least one team before saving.",
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveMultiSelect(
            options = emptyList<String>(),
            selectedOptions = emptyList(),
            onSelectedOptionsChange = {},
            optionLabel = { it },
            label = "Empty options",
            emptyContent = { Body("No teams available.") },
        )
    }
}

private data class DemoPerson(
    val name: String,
    val role: String,
    val email: String,
)

private data class DemoCarouselItem(
    val title: String,
    val description: String,
    val metric: String,
    val label: String,
    val tone: AdaptiveBadgeTone,
    val chipTone: AdaptiveChipTone,
)

@Composable
private fun CarouselsSection() {
    val items = listOf(
        DemoCarouselItem(
            title = "Revenue overview",
            description = "Monthly recurring revenue is up across the admin workspace.",
            metric = "$42.8k",
            label = "MRR",
            tone = AdaptiveBadgeTone.Success,
            chipTone = AdaptiveChipTone.Success,
        ),
        DemoCarouselItem(
            title = "Team activity",
            description = "Eight open reviews need attention before the deployment window.",
            metric = "8",
            label = "Reviews",
            tone = AdaptiveBadgeTone.Warning,
            chipTone = AdaptiveChipTone.Warning,
        ),
        DemoCarouselItem(
            title = "Support health",
            description = "Response time remains inside SLA for priority customers.",
            metric = "96%",
            label = "SLA",
            tone = AdaptiveBadgeTone.Info,
            chipTone = AdaptiveChipTone.Info,
        ),
    )
    var selectedIndex by remember { mutableStateOf(0) }
    var edgeIndex by remember { mutableStateOf(0) }
    var fadeIndex by remember { mutableStateOf(1) }

    ShowcaseCard(title = "Carousels", description = "Animated controlled carousel with slide/fade transitions, polished indicators, and edge states.") {
        AdaptiveCarousel(
            items = items,
            selectedIndex = selectedIndex,
            onSelectedIndexChange = { selectedIndex = it },
        ) { item, index ->
            CarouselItemContent(item = item, eyebrow = "Slide ${index + 1} of ${items.size}")
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveCarousel(
            items = items,
            selectedIndex = fadeIndex,
            onSelectedIndexChange = { fadeIndex = it },
            transition = AdaptiveCarouselTransition.Fade,
        ) { item, index ->
            CarouselItemContent(item = item, eyebrow = "Fade ${index + 1} of ${items.size}")
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveCarousel(
            items = items.take(2),
            selectedIndex = edgeIndex,
            onSelectedIndexChange = { edgeIndex = it },
            loop = false,
        ) { item, _ ->
            CarouselItemContent(item = item, eyebrow = "Loop disabled")
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveCarousel(
            items = emptyList<DemoCarouselItem>(),
            selectedIndex = 0,
            onSelectedIndexChange = {},
            emptyContent = { Body("Empty carousel content stays stable and readable.") },
        ) { item, _ ->
            CarouselItemContent(item = item, eyebrow = "Unused")
        }
    }
}

@Composable
private fun CarouselItemContent(item: DemoCarouselItem, eyebrow: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AdaptiveThumbnail(label = item.title, size = 54.dp)
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Body(eyebrow)
                AdaptiveBadge(item.tone.name.lowercase().replaceFirstChar { it.uppercase() }, tone = item.tone)
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            Label(item.title)
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
            Body(item.description)
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
                AdaptiveChip(text = item.metric, selected = true, tone = item.chipTone)
                AdaptiveChip(text = item.label, tone = AdaptiveChipTone.Neutral)
            }
        }
    }
}

@Composable
private fun FeedbackSection() {
    val adaptiveInfo = rememberAdaptiveInfo()
    val span = if (adaptiveInfo.isCompact) 12 else 4
    ShowcaseCard(title = "Feedback loading", description = "Animated loading states with spinner, dots, and pulse styles.") {
        AdaptiveGrid(columns = 12, horizontalGap = AdaptiveTokens.Spacing.Medium, verticalGap = AdaptiveTokens.Spacing.Medium) {
            item(span = span) {
                AdaptiveLoadingState(message = "Syncing workspace")
            }
            item(span = span) {
                AdaptiveLoadingState(
                    message = "Loading activity",
                    indicatorStyle = AdaptiveLoadingIndicatorStyle.Dots,
                )
            }
            item(span = span) {
                AdaptiveLoadingState(
                    message = "Preparing report",
                    indicatorStyle = AdaptiveLoadingIndicatorStyle.Pulse,
                )
            }
        }
    }
}

@Composable
private fun NavigationTreeSection() {
    val treeItems = remember {
        listOf(
            AdaptiveNavigationTreeItem(
                id = "workspace",
                label = "Workspace",
                badge = "4",
                children = listOf(
                    AdaptiveNavigationTreeItem("overview", "Overview"),
                    AdaptiveNavigationTreeItem("activity", "Activity"),
                    AdaptiveNavigationTreeItem("reports", "Reports", badge = "New"),
                ),
            ),
            AdaptiveNavigationTreeItem(
                id = "operations",
                label = "Operations",
                children = listOf(
                    AdaptiveNavigationTreeItem("employees", "Employees"),
                    AdaptiveNavigationTreeItem("products", "Products"),
                    AdaptiveNavigationTreeItem(
                        id = "billing",
                        label = "Billing",
                        children = listOf(
                            AdaptiveNavigationTreeItem("invoices", "Invoices"),
                            AdaptiveNavigationTreeItem("statements", "Statements", enabled = false),
                        ),
                    ),
                ),
            ),
            AdaptiveNavigationTreeItem("settings", "Settings"),
        )
    }
    var selectedItem by remember { mutableStateOf("overview") }
    var expandedItems by remember { mutableStateOf(setOf("workspace", "operations", "billing")) }

    ShowcaseCard(title = "Navigation tree", description = "Controlled hierarchy for admin sidebars and nested settings.") {
        AdaptiveNavigationTree(
            items = treeItems,
            selectedItemId = selectedItem,
            onItemSelected = { selectedItem = it.id },
            expandedItemIds = expandedItems,
            onExpandedItemIdsChange = { expandedItems = it },
        )
    }

    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))

    ShowcaseCard(title = "Navigation item styles", description = "Default pill navigation plus card and minimal variants.") {
        AdaptiveGrid(columns = 12, horizontalGap = AdaptiveTokens.Spacing.Medium, verticalGap = AdaptiveTokens.Spacing.Medium) {
            item(span = 4) {
                NavigationStylePreview("Pill default", AdaptiveNavigationItemStyle.Pill, AdaptiveNavigationDensity.Comfortable)
            }
            item(span = 4) {
                NavigationStylePreview("Card", AdaptiveNavigationItemStyle.Card, AdaptiveNavigationDensity.Comfortable)
            }
            item(span = 4) {
                NavigationStylePreview("Minimal compact", AdaptiveNavigationItemStyle.Minimal, AdaptiveNavigationDensity.Compact)
            }
        }
    }
}

@Composable
private fun NavigationStylePreview(
    title: String,
    style: AdaptiveNavigationItemStyle,
    density: AdaptiveNavigationDensity,
) {
    Column {
        Label(title)
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        AdaptiveNavigationTree(
            items = listOf(
                AdaptiveNavigationTreeItem("overview", "Overview"),
                AdaptiveNavigationTreeItem("reports", "Reports", badge = "New"),
                AdaptiveNavigationTreeItem("settings", "Settings", enabled = style != AdaptiveNavigationItemStyle.Minimal),
            ),
            selectedItemId = "reports",
            onItemSelected = {},
            expandedItemIds = emptySet(),
            onExpandedItemIdsChange = {},
            itemStyle = style,
            density = density,
        )
    }
}

@Composable
private fun BreadcrumbsSection() {
    var selected by remember { mutableStateOf("Overview") }
    val items = listOf("Overview", "Projects", "Billing", "Invoice #123")

    ShowcaseCard(title = "Breadcrumbs", description = "Hierarchical page navigation with clickable path items.") {
        AdaptiveBreadcrumbs(
            items = items,
            selectedItem = selected,
            onItemSelected = { selected = it },
            itemLabel = { it },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        BasicText(
            text = "Selected: $selected",
            style = TextStyle(fontSize = 13.sp, color = AdaptiveTheme.colors.textSecondary),
        )
    }
}

@Composable
private fun AccordionsSection() {
    ShowcaseCard(title = "Accordions", description = "Expandable disclosure panels for nested details.") {
        AdaptiveAccordion(
            title = "Account settings",
            subtitle = "Manage notification and billing preferences.",
            defaultExpanded = true,
        ) {
            Column {
                Body("Change your password, email, and user preferences in this panel.")
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
                AdaptiveButton(text = "Edit settings", size = AdaptiveButtonSize.Small, onClick = {})
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveAccordion(
            title = "Support details",
            subtitle = "Frequently requested help topics.",
        ) {
            Column {
                Body("View answers to common questions and contact support from the admin console.")
            }
        }
    }
}

@Composable
private fun TabsSection() {
    val tabs = listOf("Overview", "Activity", "Team")
    var selectedTab by remember { mutableStateOf(tabs.first()) }

    ShowcaseCard(title = "Tabs", description = "Segmented tabs for switching page sections.") {
        AdaptiveTabs(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabLabel = { it },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveCard {
            Label("$selectedTab content")
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            Body("This panel updates when the active tab changes.")
        }
    }
}

@Composable
private fun DialogsSection(initialOpen: Boolean = false) {
    var showDialog by remember { mutableStateOf(initialOpen) }

    ShowcaseCard(title = "Dialogs", description = "Modal dialogs for confirmation and decision flows.") {
        AdaptiveButton(text = "Open dialog", onClick = { showDialog = true })
        if (showDialog) {
            AdaptiveDialog(
                onDismissRequest = { showDialog = false },
                title = "Confirm action",
                dismissButton = {
                    AdaptiveButton(text = "Cancel", variant = AdaptiveButtonVariant.Ghost, onClick = { showDialog = false })
                },
                confirmButton = {
                    AdaptiveButton(text = "Confirm", onClick = { showDialog = false })
                },
            ) {
                Body("Are you sure you want to continue? This dialog demonstrates a modal surface with actions.")
            }
        }
    }
}

@Composable
private fun SectionHeaderDividerSection() {
    AdaptiveCard {
        AdaptiveSectionHeader(
            title = "Section header and divider",
            description = "Composable page headings with optional action slot.",
            action = {
                AdaptiveButton(
                    text = "Action",
                    size = AdaptiveButtonSize.Small,
                    variant = AdaptiveButtonVariant.Secondary,
                    onClick = {},
                )
            },
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        AdaptiveDivider()
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        Body("This row verifies section copy, action alignment, and divider rendering inside a card.")
    }
}

@Composable
private fun ShowcaseCard(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    AdaptiveCard {
        AdaptiveSectionHeader(title = title, description = description)
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        content()
    }
}

@Composable
private fun ShowcaseRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
private fun Label(text: String) {
    BasicText(
        text = text,
        style = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = AdaptiveTheme.colors.textPrimary,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Body(text: String) {
    BasicText(
        text = text,
        style = TextStyle(fontSize = 13.sp, color = AdaptiveTheme.colors.textMuted),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Glyph(text: String) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(AdaptiveTokens.Radius.Pill))
            .background(AdaptiveTheme.colors.primarySubtle)
            .border(1.dp, AdaptiveTheme.colors.borderStrong, RoundedCornerShape(AdaptiveTokens.Radius.Pill)),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AdaptiveTheme.colors.primaryText,
            ),
            maxLines = 1,
        )
    }
}
