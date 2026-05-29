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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveAvatarShape
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonSize
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.components.AdaptiveDropdownMenu
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveMenuItem
import io.github.adaptivekt.components.AdaptiveMultiSelect
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveSectionHeader
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.layout.AdaptiveGrid

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
}

@Composable
internal fun ComponentsShowcaseScreen(
    focusSection: ComponentsShowcaseSection? = null,
    initialSelectExpanded: Boolean = false,
    initialMultiSelectExpanded: Boolean = false,
) {
    val adaptiveInfo = rememberAdaptiveInfo()
    val cardSpan = if (adaptiveInfo.isCompact) 12 else 6
    val title = focusSection?.title ?: "UI Kit"
    val description = focusSection?.description ?: "Visual smoke test for shared AdaptiveKt components."

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (focusSection == null) Modifier.verticalScroll(rememberScrollState()) else Modifier),
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
                leadingIcon = { AdaptiveIcons.Plus(size = 16.dp, tint = Color.White) },
                trailingIcon = { AdaptiveIcons.ChevronRight(size = 16.dp, tint = Color.White) },
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
            AdaptiveIconButton(onClick = {}, enabled = false) { AdaptiveIcons.Plus(tint = Color(0xFF94A3B8)) }
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
                            .background(Color(0xFFE0F2FE)),
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
            leadingIcon = { AdaptiveIcons.Search(size = 16.dp, tint = Color(0xFF64748B)) },
            trailingIcon = { AdaptiveIcons.Check(size = 16.dp, tint = Color(0xFF047857)) },
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
        DemoPerson("Alicia Romero", "Operations"),
        DemoPerson("Noah Kim", "Finance"),
        DemoPerson("Marta Silva", "Support"),
        DemoPerson("Dina Patel", "Security"),
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
            label = "Assignees",
            maxVisibleChips = 2,
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
                            Body(person.team)
                        }
                    }
                    if (selected) {
                        AdaptiveBadge("Selected")
                    }
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
                        color = Color(0xFF1D4ED8),
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
    val team: String,
)

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
            color = Color(0xFF0F172A),
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Body(text: String) {
    BasicText(
        text = text,
        style = TextStyle(fontSize = 13.sp, color = Color(0xFF64748B)),
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
            .background(Color(0xFFEFF6FF))
            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(AdaptiveTokens.Radius.Pill)),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D4ED8),
            ),
            maxLines = 1,
        )
    }
}
