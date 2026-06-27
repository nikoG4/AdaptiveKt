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
import androidx.compose.runtime.CompositionLocalProvider
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
import io.github.adaptivekt.components.AdaptiveAccordion
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptiveAvatarShape
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
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
import io.github.adaptivekt.components.AdaptiveIconButton
import io.github.adaptivekt.components.AdaptiveMultiSelect
import io.github.adaptivekt.components.AdaptiveOverlayDefaults
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.AdaptiveSelect
import io.github.adaptivekt.components.AdaptiveSelectionArea
import io.github.adaptivekt.components.AdaptiveSurface
import io.github.adaptivekt.components.AdaptiveTabs
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.AdaptiveThumbnail
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveThemeMode
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.data.AdaptiveActionPriority
import io.github.adaptivekt.data.AdaptiveDataAction
import io.github.adaptivekt.data.AdaptiveDataColumn
import io.github.adaptivekt.data.AdaptiveDataContent
import io.github.adaptivekt.data.AdaptiveDataMobileRole
import io.github.adaptivekt.data.AdaptiveDataView
import io.github.adaptivekt.feedback.AdaptiveLoadingIndicatorStyle
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.feedback.AdaptiveErrorState
import io.github.adaptivekt.feedback.AdaptiveLoadingState
import io.github.adaptivekt.forms.AdaptiveFormColumns
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.FieldSpan
import io.github.adaptivekt.forms.AdaptiveValidationMessage
import io.github.adaptivekt.forms.AdaptiveValidationMessageType
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.navigation.AdaptiveNavItem
import io.github.adaptivekt.navigation.AdaptiveNavigationBehavior
import io.github.adaptivekt.navigation.AdaptiveNavigationDefaults
import io.github.adaptivekt.navigation.AdaptiveNavigationItemStyle
import io.github.adaptivekt.navigation.AdaptiveNavigationOverflowBehavior
import io.github.adaptivekt.navigation.AdaptiveNavigationPlacement
import io.github.adaptivekt.navigation.AdaptiveNavigationScaffold
import io.github.adaptivekt.navigation.AdaptiveNavigationTree
import io.github.adaptivekt.navigation.AdaptiveNavigationTreeItem

@Composable
internal fun SiteComponentsPage(
    selectedHash: String,
    onSelectedHashChange: (String) -> Unit,
    sectionId: String?,
    onSectionChange: (String) -> Unit,
) {
    val docs = remember { DocsRegistry.getComponents { componentDocs() } }
    val resolvedId = DocsRegistry.resolveComponentId(selectedHash.takeIf { it.isNotBlank() } ?: DocsRegistry.ID_THEME)
    val componentIndex = docs.indexOfFirst { it.id == resolvedId }
    val component = docs.getOrNull(componentIndex) ?: docs.first()
    val navGroups = docs.groupBy { it.family }.map { (family, items) ->
        DocsNavGroup(
            title = family,
            items = items.map { DocsNavItem(it.id, it.title.removePrefix("Adaptive"), it.summary) },
        )
    }

    DocsShell(
        eyebrow = "Components",
        title = "Rendered examples, code and API tables",
        description = "Browse the AdaptiveKt public component surface. Every page shows what the component is for, a real rendered example, copyable code, parameters, variants and usage notes.",
        navGroups = navGroups,
        selectedId = component.id,
        onSelectedIdChange = onSelectedHashChange,
        onThisPage = component.tocItems,
        onTocItemClick = { onSectionChange(it) },
        sectionId = sectionId,
    ) {
        CompositionLocalProvider(LocalDocsVisualState provides selectedHash) {
            ComponentDocArticle(component)
        }
    }
}

private fun commonNotes(title: String): List<String> = listOf(
    "$title reads AdaptiveTheme colors and shape tokens.",
    "Light and dark modes are handled by the active AdaptiveTheme color scheme.",
)

internal fun componentDocs(): List<ComponentDoc> = listOf(
    ComponentDoc(
        id = DocsRegistry.ID_THEME,
        family = "Foundations",
        title = "AdaptiveTheme",
        summary = "Theme root that provides colors, shapes, typography and interaction state tokens.",
        usage = "Wrap application content once near the root so AdaptiveKt primitives share consistent visual defaults.",
        basicExample = DocsExample(
            "Default theme",
            "Use system theme detection by default, or force light/dark when needed.",
            """
AdaptiveTheme(mode = AdaptiveThemeMode.System) {
    App()
}
            """,
        ) {
            AdaptiveTheme(mode = AdaptiveThemeMode.System) {
                AdaptiveCard {
                    SiteText("Themed surface", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    AdaptiveButton("Action", onClick = {}, modifier = Modifier.docsClickableCursor())
                }
            }
        },
        parameters = listOf(
            ComponentParameter("mode", "AdaptiveThemeMode", "System", false, "System follows the platform color scheme; Light and Dark force defaults."),
            ComponentParameter("colorScheme", "AdaptiveColorScheme?", "null", false, "Optional explicit scheme. When null, mode selects light or dark defaults."),
            ComponentParameter("shapes", "AdaptiveShapeScheme", "AdaptiveShapeScheme.default()", false, "Shape tokens used by cards, buttons, menus and fields."),
            ComponentParameter("typography", "AdaptiveTypography", "AdaptiveTypography.default()", false, "Typography defaults for labels, body text and headings."),
            ComponentParameter("states", "AdaptiveStateScheme", "AdaptiveStateScheme.default()", false, "State tokens for hover, pressed, selected, disabled and focus behavior."),
            ComponentParameter("content", "@Composable () -> Unit", "required", true, "The UI subtree that reads the theme values."),
        ),
        variants = listOf(
            DocsExample(
                "Dark scheme",
                "Switch the color scheme at the root. Components keep their contrast tokens.",
                """AdaptiveTheme(mode = AdaptiveThemeMode.Dark) { App() }""",
            ) {
                AdaptiveTheme(mode = AdaptiveThemeMode.Dark) {
                    AdaptiveCard {
                        SiteText("Dark themed card", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        AdaptiveBadge("Dark", tone = AdaptiveBadgeTone.Info)
                    }
                }
            },
        ),
        themingNotes = commonNotes("AdaptiveTheme"),
        responsiveNotes = listOf("Theme values are global and do not depend on screen size by themselves."),
        accessibilityNotes = listOf("Use color schemes with sufficient contrast for both light and dark modes."),
        limitations = listOf("AdaptiveKt does not yet expose a full design-token editor or runtime theme builder."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_BUTTON,
        family = "Actions",
        title = "AdaptiveButton",
        summary = "Rounded action button with primary, secondary, ghost and danger variants.",
        usage = "Use it for explicit commands. Keep primary actions scarce and use secondary or ghost variants for supporting actions.",
        basicExample = DocsExample(
            "Primary action",
            "The default button is intentionally ready to use without custom modifiers.",
            """AdaptiveButton("Save changes", onClick = { save() })""",
        ) {
            AdaptiveButton("Save changes", onClick = {}, modifier = Modifier.docsClickableCursor())
        },
        parameters = listOf(
            ComponentParameter("text", "String", "required", true, "Visible label."),
            ComponentParameter("onClick", "() -> Unit", "required", true, "Command invoked when the button is pressed."),
            ComponentParameter("variant", "AdaptiveButtonVariant", "Primary", false, "Primary, Secondary, Ghost or Danger visual treatment."),
            ComponentParameter("size", "AdaptiveButtonSize", "Medium", false, "Small, Medium or Large height and padding."),
            ComponentParameter("enabled", "Boolean", "true", false, "Disables click and lowers contrast."),
            ComponentParameter("leadingIcon / trailingIcon", "(@Composable () -> Unit)?", "null", false, "Optional icons rendered inside the pill shape."),
        ),
        variants = listOf(
            DocsExample(
                "Variants and icons",
                "Secondary and ghost actions stay quiet; danger is semantic but not visually loud.",
                """
AdaptiveButton("New", leadingIcon = { AdaptiveIcons.Plus() }, onClick = {})
AdaptiveButton("Cancel", variant = AdaptiveButtonVariant.Secondary, onClick = {})
AdaptiveButton("Delete", variant = AdaptiveButtonVariant.Danger, onClick = {})
                """,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AdaptiveButton("New", leadingIcon = { AdaptiveIcons.Plus(size = 15.dp, tint = AdaptiveTheme.colors.textInverse) }, onClick = {}, modifier = Modifier.docsClickableCursor())
                    AdaptiveButton("Cancel", variant = AdaptiveButtonVariant.Secondary, onClick = {}, modifier = Modifier.docsClickableCursor())
                    AdaptiveButton("Delete", variant = AdaptiveButtonVariant.Danger, onClick = {}, modifier = Modifier.docsClickableCursor())
                }
            },
        ),
        themingNotes = commonNotes("AdaptiveButton"),
        responsiveNotes = listOf("Buttons keep stable height; wrap them in FlowRow or Column on compact screens."),
        accessibilityNotes = listOf("Use clear text labels. Icon-only actions should use AdaptiveIconButton with meaningful icon content descriptions."),
        limitations = listOf("There is no built-in loading button state yet."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_ICON_BUTTON,
        family = "Actions",
        title = "AdaptiveIconButton",
        summary = "Compact rounded icon action for menus, clear buttons and toolbar actions.",
        usage = "Use for recognizable compact actions such as close, search, overflow and navigation controls.",
        basicExample = DocsExample(
            "Toolbar icons",
            "Icon buttons preserve shape in hover and pressed states.",
            """AdaptiveIconButton(onClick = {}) { AdaptiveIcons.MoreVertical() }""",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Search() }
                AdaptiveIconButton(onClick = {}) { AdaptiveIcons.MoreVertical() }
                AdaptiveIconButton(onClick = {}) { AdaptiveIcons.Close() }
            }
        },
        parameters = listOf(
            ComponentParameter("onClick", "() -> Unit", "required", true, "Command invoked when pressed."),
            ComponentParameter("enabled", "Boolean", "true", false, "Disables interaction."),
            ComponentParameter("size", "Dp", "40.dp", false, "Square hit area."),
            ComponentParameter("content", "@Composable () -> Unit", "required", true, "Icon content, typically AdaptiveIcons."),
        ),
        variants = listOf(),
        themingNotes = commonNotes("AdaptiveIconButton"),
        responsiveNotes = listOf("Works well in dense toolbars and table/card overflow areas."),
        accessibilityNotes = listOf("Provide semantic content descriptions on the icon when the action is not already described by nearby text."),
        limitations = listOf("It is not a general icon library; AdaptiveIcons only cover functional affordances."),
    ),
    simpleDisplayDoc(
        id = DocsRegistry.ID_BADGE,
        family = "Display",
        title = "AdaptiveBadge",
        summary = "Pill status label with neutral, success, warning, danger and info tones.",
        code = """AdaptiveBadge("Paid", tone = AdaptiveBadgeTone.Success)""",
        parameters = listOf(
            ComponentParameter("text", "String", "required", true, "Badge label."),
            ComponentParameter("tone", "AdaptiveBadgeTone", "Neutral", false, "Semantic color tone."),
        ),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveBadge("Paid", tone = AdaptiveBadgeTone.Success)
            AdaptiveBadge("Review", tone = AdaptiveBadgeTone.Warning)
            AdaptiveBadge("Blocked", tone = AdaptiveBadgeTone.Danger)
        }
    },
    simpleDisplayDoc(
        id = DocsRegistry.ID_CHIP,
        family = "Display",
        title = "AdaptiveChip",
        summary = "Compact pill for filters, tags and selected values.",
        code = """AdaptiveChip("Active", selected = true, tone = AdaptiveChipTone.Primary, onClick = {})""",
        parameters = listOf(
            ComponentParameter("text", "String", "required", true, "Chip label."),
            ComponentParameter("selected", "Boolean", "false", false, "Applies selected emphasis."),
            ComponentParameter("enabled", "Boolean", "true", false, "Disables interaction."),
            ComponentParameter("tone", "AdaptiveChipTone", "Neutral", false, "Semantic tone."),
            ComponentParameter("onClick", "(() -> Unit)?", "null", false, "Optional click action."),
        ),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveChip("Active", selected = true, tone = AdaptiveChipTone.Primary, onClick = {})
            AdaptiveChip("Syncing", tone = AdaptiveChipTone.Info)
            AdaptiveChip("Late", tone = AdaptiveChipTone.Warning)
        }
    },
    simpleDisplayDoc(
        id = DocsRegistry.ID_AVATAR,
        family = "Display",
        title = "AdaptiveAvatar",
        summary = "Name-based avatar with initials fallback and optional image slot.",
        code = """AdaptiveAvatar(name = "Alicia Romero", size = 40.dp)""",
        parameters = listOf(
            ComponentParameter("name", "String", "required", true, "Name used for initials fallback."),
            ComponentParameter("image", "(@Composable () -> Unit)?", "null", false, "Optional custom image content clipped to the shape."),
            ComponentParameter("size", "Dp", "36.dp", false, "Avatar width and height."),
            ComponentParameter("shape", "AdaptiveAvatarShape", "Circle", false, "Circle or Rounded."),
        ),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            AdaptiveAvatar("Alicia Romero", size = 42.dp)
            AdaptiveAvatar("Noah Kim", size = 42.dp)
            AdaptiveAvatar("Team", size = 42.dp, shape = AdaptiveAvatarShape.Rounded)
        }
    },
    simpleDisplayDoc(
        id = DocsRegistry.ID_THUMBNAIL,
        family = "Display",
        title = "AdaptiveThumbnail",
        summary = "Small object thumbnail for products, files and media with label fallback.",
        code = """AdaptiveThumbnail(label = "Router Gigabit")""",
        parameters = listOf(
            ComponentParameter("label", "String", "required", true, "Text used for fallback initials."),
            ComponentParameter("size", "Dp", "44.dp", false, "Thumbnail size."),
            ComponentParameter("shape", "Shape", "AdaptiveTokens.Radius.Medium", false, "Clipping shape."),
            ComponentParameter("image", "(@Composable () -> Unit)?", "null", false, "Optional thumbnail content."),
            ComponentParameter("tone", "Color", "Color(0xFF64748B)", false, "Fallback tone color."),
        ),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdaptiveThumbnail("Router Gigabit", tone = Color(0xFFE0F2FE))
            AdaptiveThumbnail("Invoice PDF", tone = Color(0xFFDCFCE7))
            AdaptiveThumbnail("Warehouse", tone = Color(0xFFFFEDD5))
        }
    },
    ComponentDoc(
        id = DocsRegistry.ID_CARD_SURFACE,
        family = "Display",
        title = "AdaptiveCard and AdaptiveSurface",
        summary = "Reusable content containers with professional default border, radius and padding.",
        usage = "Use AdaptiveCard for vertical content and optional click behavior; use AdaptiveSurface for neutral framed regions.",
        basicExample = DocsExample(
            "Card and surface",
            "Both containers are Foundation-only and follow AdaptiveTheme shapes and surface colors.",
            """AdaptiveCard { AdaptiveSurface { Content() } }""",
        ) {
            AdaptiveCard(contentPadding = PaddingValues(12.dp)) {
                SiteText("Card title", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                AdaptiveSurface(contentPadding = PaddingValues(10.dp)) {
                    SiteText("Neutral surface content", color = SiteMuted)
                }
            }
        },
        parameters = listOf(
            ComponentParameter("modifier", "Modifier", "Modifier", false, "Root modifier."),
            ComponentParameter("contentPadding", "PaddingValues", "Spacing.Large", false, "Internal padding."),
            ComponentParameter("onClick", "(() -> Unit)?", "null", false, "AdaptiveCard only: optional clickable behavior."),
            ComponentParameter("content", "@Composable scope", "required", true, "Child content."),
        ),
        themingNotes = commonNotes("AdaptiveCard"),
        responsiveNotes = listOf("Containers fill available width and work inside one-column mobile layouts."),
        accessibilityNotes = listOf("If clickable, make content label text explicit."),
        limitations = listOf("No heavy elevation or shadow system yet."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_SELECTION_AREA,
        family = "Display",
        title = "AdaptiveSelectionArea",
        summary = "Opt-in text selection wrapper for paragraphs, code-like content and read-only details.",
        usage = "Wrap content-heavy read-only regions when users may need to copy IDs, logs, message text, product descriptions or generated output.",
        basicExample = DocsExample(
            "Selectable paragraph",
            "Selection is opt-in so controls and navigation remain click-first.",
            """
AdaptiveSelectionArea {
    Text("Users can select this paragraph.")
}
            """,
        ) {
            AdaptiveSelectionArea {
                SiteText(
                    text = "This paragraph is selectable. Try dragging across the workspace ID, owner email and generated summary without making nearby controls selectable.",
                    color = SiteMuted,
                    maxLines = 5,
                )
            }
        },
        parameters = listOf(
            ComponentParameter("enabled", "Boolean", "true", false, "When false, wraps content in DisableSelection to protect nested controls."),
            ComponentParameter("content", "@Composable () -> Unit", "required", true, "Read-only content that can participate in text selection."),
        ),
        variants = listOf(
            DocsExample(
                "Selectable card body",
                "AdaptiveCard can enable body selection while still keeping clickable cards opt-out by default.",
                """
AdaptiveCard(contentSelectionEnabled = true) {
    Text("Selectable card body content")
}
                """,
            ) {
                AdaptiveCard(contentSelectionEnabled = true) {
                    SiteText("Run details", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    SiteText("run_04J7ZK2A | owner: alicia@adaptivekt.io | status: validated", color = SiteMuted, maxLines = 4)
                }
            },
            DocsExample(
                "AI message copy",
                "Use it for chat output, logs and generated text; keep action chips outside the selected area.",
                """
AdaptiveSelectionArea {
    Text("Assistant answer...")
}
                """,
            ) {
                AdaptiveCard {
                    AdaptiveBadge("Assistant", tone = AdaptiveBadgeTone.Info)
                    Spacer(modifier = Modifier.height(8.dp))
                    AdaptiveSelectionArea {
                        SiteText(
                            "The assistant found three candidate sources. Copy this explanation without selecting the action buttons below.",
                            color = SiteMuted,
                            maxLines = 5,
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdaptiveButton("Summarize", size = AdaptiveButtonSize.Small, onClick = {})
                        AdaptiveButton("Cite", size = AdaptiveButtonSize.Small, variant = AdaptiveButtonVariant.Secondary, onClick = {})
                    }
                }
            },
        ),
        themingNotes = commonNotes("AdaptiveSelectionArea"),
        responsiveNotes = listOf("Selection does not impose layout; it follows the wrapped content."),
        accessibilityNotes = listOf("Use selection for read-only copyable content, not for primary controls."),
        limitations = listOf("Touch text-selection affordances are platform-controlled by Compose."),
    ),
    inputDoc("adaptive-text-field", "AdaptiveTextField", "Foundation text input with label, placeholder, disabled state and validation message.") {
        var value by remember { mutableStateOf("AdaptiveKt Inc.") }
        AdaptiveTextField(value = value, onValueChange = { value = it }, label = "Company", placeholder = "Company name")
    },
    ComponentDoc(
        id = DocsRegistry.ID_SEARCH_FIELD,
        family = "Inputs",
        title = "AdaptiveSearchField",
        summary = "Search input with built-in search icon and optional clear action.",
        usage = "Use it for filters and local search controls in toolbars, data views and dropdowns.",
        basicExample = DocsExample(
            "Search with clear",
            "The clear affordance is an embedded vector icon, not a raw character.",
            """AdaptiveSearchField(value = query, onValueChange = { query = it }, onClear = { query = "" })""",
        ) {
            var query by remember { mutableStateOf("invoice") }
            AdaptiveSearchField(value = query, onValueChange = { query = it }, onClear = { query = "" })
        },
        parameters = inputParameters() + listOf(ComponentParameter("onClear", "(() -> Unit)?", "null", false, "Shows a clear button when value is not empty.")),
        themingNotes = commonNotes("AdaptiveSearchField"),
        responsiveNotes = listOf("Fill width on mobile; place inside toolbars or cards."),
        accessibilityNotes = listOf("Use a clear placeholder such as Search invoices."),
        limitations = listOf("Does not perform filtering by itself."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_SELECT,
        family = "Inputs",
        title = "AdaptiveSelect",
        summary = "Single-selection dropdown with optional search, clear and custom option rendering.",
        usage = "Use for bounded option sets such as status, team, category or plan.",
        basicExample = DocsExample(
            "Searchable select",
            "The menu is anchored to the trigger and can match trigger width.",
            """
AdaptiveSelect(
    options = statuses,
    selectedOption = selected,
    onSelectedOptionChange = { selected = it },
    optionLabel = { it },
    searchable = true,
)
            """,
        ) {
            var selected by remember { mutableStateOf<String?>(null) }
            AdaptiveSelect(
                options = listOf("Open", "Pending", "Closed"),
                selectedOption = selected,
                onSelectedOptionChange = { selected = it },
                optionLabel = { it },
                label = "Status",
                searchable = true,
            )
        },
        parameters = selectParameters(multi = false),
        variants = listOf(peopleSelectExample()),
        themingNotes = commonNotes("AdaptiveSelect"),
        responsiveNotes = listOf("On compact screens it fills the available width. Keep menu content concise."),
        accessibilityNotes = listOf("Use label and supportingText to make intent and validation explicit."),
        limitations = listOf("Remote async loading is intentionally not part of the component yet."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_MULTI_SELECT,
        family = "Inputs",
        title = "AdaptiveMultiSelect",
        summary = "Multi-selection dropdown with selected chips, search, custom option rows and overflow count.",
        usage = "Use for assigning people, teams, tags and filters where selected values should remain visible.",
        basicExample = DocsExample(
            "People picker",
            "The existing chipContent and optionContent slots are enough for avatar-based people pickers.",
            """
AdaptiveMultiSelect(
    options = people,
    selectedOptions = selected,
    onSelectedOptionsChange = { selected = it },
    optionLabel = { it.name },
    maxVisibleChips = 2,
    optionContent = { person, selected -> PeopleOption(person, selected) },
    chipContent = { person -> PeopleChip(person) },
)
            """,
        ) {
            val people = remember {
                listOf(
                    Person("Alicia Romero", "Product Manager"),
                    Person("David Chen", "Engineer"),
                    Person("Marta Silva", "QA Lead"),
                    Person("Noah Kim", "Designer"),
                )
            }
            var selected by remember { mutableStateOf(people.take(3)) }
            AdaptiveMultiSelect(
                options = people,
                selectedOptions = selected,
                onSelectedOptionsChange = { selected = it },
                optionLabel = { it.name },
                label = "Assignees",
                maxVisibleChips = 2,
                optionContent = { person, isSelected -> PeopleOption(person, isSelected) },
                chipContent = { person -> PeopleChip(person) },
            )
        },
        parameters = selectParameters(multi = true),
        variants = listOf(),
        themingNotes = commonNotes("AdaptiveMultiSelect"),
        responsiveNotes = listOf("Selected chips collapse into +N overflow after maxVisibleChips."),
        accessibilityNotes = listOf("Custom option rows should still expose readable names and selection state visually."),
        limitations = listOf("Keyboard navigation polish can be expanded in a later accessibility pass."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_TEXT_FIELD,
        family = "Forms",
        title = "AdaptiveTextField",
        summary = "Text input field with support for placeholder, leading/trailing icons, and validation.",
        usage = "Use inside forms or alone for simple text input.",
        basicExample = DocsExample(
            "Basic text field",
            "A standard text field.",
            """AdaptiveTextField(value = text, onValueChange = { text = it }, placeholder = "Enter name")""",
        ) {
            var text by remember { mutableStateOf("") }
            AdaptiveTextField(value = text, onValueChange = { text = it }, placeholder = "Enter name")
        },
        parameters = listOf(
            ComponentParameter("value", "String", "required", true, "Current field value."),
            ComponentParameter("onValueChange", "(String) -> Unit", "required", true, "Callback on value change."),
            ComponentParameter("placeholder", "String?", "null", false, "Placeholder text."),
            ComponentParameter("enabled", "Boolean", "true", false, "Whether the field is enabled."),
            ComponentParameter("isError", "Boolean", "false", false, "Visual error state."),
            ComponentParameter("leadingIcon / trailingIcon", "(@Composable () -> Unit)?", "null", false, "Icons inside the field."),
        ),
        themingNotes = commonNotes("AdaptiveTextField"),
        responsiveNotes = listOf("Fills available width by default."),
        accessibilityNotes = listOf("If used outside of AdaptiveFormLayout, ensure proper labels are provided."),
        limitations = listOf("Advanced input masking is not yet natively supported."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_FORM_LAYOUT,
        family = "Forms",
        title = "AdaptiveFormLayout",
        summary = "Responsive form sections with field spans, labels, validation messages and action slots.",
        usage = "Use it to structure settings and admin forms without hand-rolling compact/desktop grids.",
        basicExample = DocsExample(
            "Settings form",
            "Compact uses one column; wider screens use the configured column counts.",
            """AdaptiveFormLayout { section("Workspace") { field("Name") { AdaptiveTextField(...) } } }""",
        ) {
            var name by remember { mutableStateOf("AdaptiveKt") }
            AdaptiveFormLayout(columns = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 2, large = 2)) {
                section(title = "Workspace", description = "Company defaults") {
                    field("Name", span = FieldSpan.Half, required = true) {
                        AdaptiveTextField(value = name, onValueChange = { name = it })
                    }
                    field("Plan", span = FieldSpan.Half, validationMessage = AdaptiveValidationMessage("Preview account", AdaptiveValidationMessageType.Info)) {
                        AdaptiveTextField(value = "Team", onValueChange = {}, enabled = false)
                    }
                }
                actions {
                    primary { AdaptiveButton("Save", onClick = {}) }
                    secondary { AdaptiveButton("Reset", variant = AdaptiveButtonVariant.Secondary, onClick = {}) }
                }
            }
        },
        parameters = listOf(
            ComponentParameter("columns", "AdaptiveFormColumns", "AdaptiveFormColumns()", false, "Column counts per breakpoint."),
            ComponentParameter("labelPosition", "LabelPosition", "Top", false, "Top or Start labels when columns allow it."),
            ComponentParameter("stickyActionsOnCompact", "Boolean", "false", false, "Whether compact actions stick at bottom."),
            ComponentParameter("maxWidth", "Dp", "AdaptiveTokens.Widths.Form", false, "Maximum form width."),
            ComponentParameter("content", "AdaptiveFormScope.() -> Unit", "required", true, "Sections, fields and actions."),
        ),
        themingNotes = commonNotes("AdaptiveFormLayout"),
        responsiveNotes = listOf("Compact always resolves to top labels and one-column fields unless configured otherwise."),
        accessibilityNotes = listOf("Use required and validationMessage so users understand constraints."),
        limitations = listOf("AdaptiveSelect integration remains slot-based; there is no form field registry."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_DATA_VIEW,
        family = "Data",
        title = "AdaptiveDataView",
        summary = "Responsive data view that renders desktop tables and compact cards from the same column definitions.",
        usage = "Use it for admin lists where mobile users need cards instead of squeezed tables.",
        basicExample = DocsExample(
            "Customers table/card",
            "Column metadata controls mobile title, subtitle and status behavior.",
            """AdaptiveDataView(state = AdaptiveDataContent(rows), columns = columns, rowActions = actions)""",
        ) {
            val rows = listOf(DemoRow("Acme", "Paid", "$1,200"), DemoRow("Northwind", "Open", "$840"))
            AdaptiveDataView(
                state = AdaptiveDataContent(rows),
                columns = listOf(
                    AdaptiveDataColumn("customer", "Customer", weight = 2f, mobileRole = AdaptiveDataMobileRole.Title) { SiteText(it.customer, fontWeight = FontWeight.Bold) },
                    AdaptiveDataColumn("status", "Status", mobileRole = AdaptiveDataMobileRole.Status) { AdaptiveBadge(it.status, tone = if (it.status == "Paid") AdaptiveBadgeTone.Success else AdaptiveBadgeTone.Info) },
                    AdaptiveDataColumn("amount", "Amount") { SiteText(it.amount) },
                ),
                rowActions = listOf(
                    AdaptiveDataAction("view", "View", AdaptiveActionPriority.Primary) {},
                    AdaptiveDataAction("archive", "Archive", AdaptiveActionPriority.Overflow) {},
                ),
            )
        },
        parameters = listOf(
            ComponentParameter("state", "AdaptiveDataState<T>", "required", true, "Loading, error, empty or content state."),
            ComponentParameter("columns", "List<AdaptiveDataColumn<T>>", "required", true, "Column definitions and mobile metadata."),
            ComponentParameter("filterSlot", "AdaptiveFilterSlot?", "null", false, "Optional filters above data."),
            ComponentParameter("actions", "(@Composable () -> Unit)?", "null", false, "Optional toolbar actions."),
            ComponentParameter("rowActions", "List<AdaptiveDataAction<T>>", "emptyList()", false, "Primary, secondary and overflow row actions."),
            ComponentParameter("cardContent", "(@Composable (T) -> Unit)?", "null", false, "Optional custom card renderer; default cards use column metadata."),
        ),
        themingNotes = commonNotes("AdaptiveDataView"),
        responsiveNotes = listOf("Compact and medium use cards; expanded and large use tables."),
        accessibilityNotes = listOf("Use readable column headers and action labels."),
        limitations = listOf("Pagination and sorting are not part of this component yet."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_NAVIGATION_SCAFFOLD,
        family = "Navigation",
        title = "AdaptiveNavigationScaffold",
        summary = "Responsive navigation shell with configurable sidebar, rail, drawer, bottom bar or hidden placements.",
        usage = "Use for admin shells, storefronts and custom layouts where navigation should adapt by breakpoint without duplicating screen logic.",
        basicExample = DocsExample(
            "Navigation shell preview",
            "The scaffold resolves placement from breakpoint and AdaptiveNavigationBehavior.",
            """
AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = selected,
    onItemSelected = { selected = it },
    navigationBehavior = AdaptiveNavigationDefaults.adminBehavior(),
) { padding -> Content(padding) }
            """,
        ) {
            var selected by remember { mutableStateOf("dashboard") }
            Box(modifier = Modifier.fillMaxWidth().height(260.dp).border(1.dp, SiteLine)) {
                AdaptiveNavigationScaffold(
                    navItems = listOf(AdaptiveNavItem("dashboard", "Dashboard"), AdaptiveNavItem("orders", "Orders"), AdaptiveNavItem("settings", "Settings")),
                    selectedItemId = selected,
                    onItemSelected = { selected = it },
                    navigationBehavior = AdaptiveNavigationDefaults.adminBehavior(),
                    navigationItemStyle = AdaptiveNavigationItemStyle.Pill,
                ) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                        SiteText("Selected: $selected", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        parameters = listOf(
            ComponentParameter("navItems", "List<AdaptiveNavItem>", "required", true, "Navigation items."),
            ComponentParameter("selectedItemId", "String", "required", true, "Selected item id."),
            ComponentParameter("onItemSelected", "(String) -> Unit", "required", true, "Selection callback."),
            ComponentParameter("topBar", "(@Composable () -> Unit)?", "null", false, "Optional top bar slot."),
            ComponentParameter("navigationBehavior", "AdaptiveNavigationBehavior?", "null", false, "Breakpoint placement and overflow behavior. Null preserves legacy preferBottomNavigationOnCompact mapping."),
            ComponentParameter("preferBottomNavigationOnCompact", "Boolean", "false", false, "Legacy compact mapping retained for compatibility."),
            ComponentParameter("navigationItemStyle", "AdaptiveNavigationItemStyle", "Pill", false, "Card, Pill or Minimal item style."),
            ComponentParameter("content", "@Composable (PaddingValues) -> Unit", "required", true, "Screen content with scaffold padding."),
        ),
        variants = listOf(
            DocsExample(
                "Storefront bottom navigation",
                "Use the storefront preset for bottom navigation on compact and medium breakpoints, then render a custom top/header area on larger screens.",
                """
AdaptiveNavigationScaffold(
    navigationBehavior = AdaptiveNavigationDefaults.storefrontBehavior(),
    navItems = items,
    selectedItemId = selected,
    onItemSelected = { selected = it },
) { padding -> StoreContent(padding) }
                """,
            ) {
                AdaptiveCard {
                    SiteText("Preset: storefrontBehavior()", fontWeight = FontWeight.Bold)
                    SiteText("Compact/Medium -> BottomBar, Expanded/Large -> Hidden, overflow -> MoreMenu.", color = SiteMuted, maxLines = 3)
                }
            },
            DocsExample(
                "Custom placements",
                "Choose placements per breakpoint and use MoreMenu or Scroll when navigation grows.",
                """
AdaptiveNavigationBehavior(
    compact = AdaptiveNavigationPlacement.Drawer,
    medium = AdaptiveNavigationPlacement.Rail,
    expanded = AdaptiveNavigationPlacement.Sidebar,
    large = AdaptiveNavigationPlacement.Sidebar,
    overflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu,
)
                """,
            ) {
                val behavior = AdaptiveNavigationBehavior(
                    compact = AdaptiveNavigationPlacement.Drawer,
                    medium = AdaptiveNavigationPlacement.Rail,
                    expanded = AdaptiveNavigationPlacement.Sidebar,
                    large = AdaptiveNavigationPlacement.Sidebar,
                    overflowBehavior = AdaptiveNavigationOverflowBehavior.MoreMenu,
                )
                AdaptiveSurface(contentPadding = PaddingValues(12.dp)) {
                    SiteText("Compact: ${behavior.compact} | Medium: ${behavior.medium}", fontWeight = FontWeight.Bold)
                    SiteText("Overflow: ${behavior.overflowBehavior}", color = SiteMuted)
                }
            },
        ),
        themingNotes = commonNotes("AdaptiveNavigationScaffold"),
        responsiveNotes = listOf("Configure compact, medium, expanded and large placements independently. MoreMenu and Scroll keep dense bottom/rail navigation usable."),
        accessibilityNotes = listOf("Provide short labels that remain legible in rail and bottom modes."),
        limitations = listOf("Deep nested sidebar hierarchy is handled by AdaptiveNavigationTree, not the scaffold itself."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_NAVIGATION_TREE,
        family = "Navigation",
        title = "AdaptiveNavigationTree",
        summary = "Controlled hierarchical navigation for nested admin sidebars and settings pages.",
        usage = "Use it when a page needs groups, children, selected child state, expansion state and badges.",
        basicExample = DocsExample(
            "Hierarchical nav",
            "Expansion is controlled by the caller.",
            """AdaptiveNavigationTree(items, selectedItemId, onItemSelected, expandedItemIds, onExpandedItemIdsChange)""",
        ) {
            NavigationTreePreview()
        },
        parameters = listOf(
            ComponentParameter("items", "List<AdaptiveNavigationTreeItem>", "required", true, "Tree roots."),
            ComponentParameter("selectedItemId", "String?", "required", true, "Currently selected item id."),
            ComponentParameter("onItemSelected", "(AdaptiveNavigationTreeItem) -> Unit", "required", true, "Selection callback."),
            ComponentParameter("expandedItemIds", "Set<String>", "required", true, "Expanded node ids."),
            ComponentParameter("onExpandedItemIdsChange", "(Set<String>) -> Unit", "required", true, "Expansion callback."),
            ComponentParameter("maxDepth", "Int", "6", false, "Maximum nesting depth to render."),
        ),
        themingNotes = commonNotes("AdaptiveNavigationTree"),
        responsiveNotes = listOf("Use it inside sidebar, drawer or settings panels; it fills width."),
        accessibilityNotes = listOf("Keep labels concise and expose selected state visually."),
        limitations = listOf("Keyboard tree navigation can be expanded in a future pass."),
    ),
    simpleDisplayDoc(
        id = DocsRegistry.ID_BREADCRUMBS,
        family = "Navigation",
        title = "AdaptiveBreadcrumbs",
        summary = "Horizontal breadcrumb trail for hierarchy and deep links.",
        code = """AdaptiveBreadcrumbs(items, selectedItem, onItemSelected, itemLabel = { it })""",
        parameters = listOf(
            ComponentParameter("items", "List<T>", "required", true, "Breadcrumb items."),
            ComponentParameter("selectedItem", "T?", "required", true, "Current item."),
            ComponentParameter("onItemSelected", "(T) -> Unit", "required", true, "Navigation callback."),
            ComponentParameter("itemLabel", "(T) -> String", "required", true, "Label renderer."),
            ComponentParameter("separator", "@Composable () -> Unit", "ChevronRight", false, "Custom separator icon/content."),
        ),
    ) {
        var selected by remember { mutableStateOf("Billing") }
        AdaptiveBreadcrumbs(listOf("Home", "Projects", "Billing", "Invoice #123"), selected, { selected = it }, itemLabel = { it })
    },
    simpleDisplayDoc(
        id = DocsRegistry.ID_TABS,
        family = "Navigation",
        title = "AdaptiveTabs",
        summary = "Segmented horizontal tabs with scroll support for compact widths.",
        code = """AdaptiveTabs(tabs, selectedTab, onTabSelected, tabLabel = { it })""",
        parameters = listOf(
            ComponentParameter("tabs", "List<T>", "required", true, "Tab models."),
            ComponentParameter("selectedTab", "T", "required", true, "Current selected tab."),
            ComponentParameter("onTabSelected", "(T) -> Unit", "required", true, "Selection callback."),
            ComponentParameter("tabLabel", "(T) -> String", "required", true, "Label renderer."),
        ),
    ) {
        val tabs = listOf("Overview", "Activity", "Team")
        var selected by remember { mutableStateOf(tabs.first()) }
        AdaptiveTabs(tabs, selected, { selected = it }, tabLabel = { it })
    },
    ComponentDoc(
        id = DocsRegistry.ID_CAROUSEL,
        family = "Display",
        title = "AdaptiveCarousel",
        summary = "Controlled carousel with slide, fade, scale or no transition.",
        usage = "Use for compact feature panels, onboarding and dashboard summaries. The caller owns selectedIndex.",
        basicExample = DocsExample(
            "Slide carousel",
            "Controls and indicators are optional; selectedIndex is normalized safely.",
            """AdaptiveCarousel(items, selectedIndex, onSelectedIndexChange) { item, index -> ... }""",
        ) {
            val cards = listOf("Revenue overview", "Team activity", "Support health")
            var selectedIndex by remember { mutableStateOf(0) }
            AdaptiveCarousel(cards, selectedIndex, { selectedIndex = it }, transition = AdaptiveCarouselTransition.Slide) { item, index ->
                Column {
                    AdaptiveBadge("Slide ${index + 1}", tone = AdaptiveBadgeTone.Info)
                    Spacer(modifier = Modifier.height(8.dp))
                    SiteText(item, fontWeight = FontWeight.Bold)
                    SiteText("Controlled content slot.", color = SiteMuted)
                }
            }
        },
        parameters = listOf(
            ComponentParameter("items", "List<T>", "required", true, "Items to display."),
            ComponentParameter("selectedIndex", "Int", "required", true, "Controlled selected index."),
            ComponentParameter("onSelectedIndexChange", "(Int) -> Unit", "required", true, "Selection callback."),
            ComponentParameter("loop", "Boolean", "true", false, "Whether next/previous wraps around."),
            ComponentParameter("showControls", "Boolean", "true", false, "Shows left/right controls."),
            ComponentParameter("showIndicators", "Boolean", "true", false, "Shows indicators."),
            ComponentParameter("transition", "AdaptiveCarouselTransition", "Slide", false, "Slide, Fade, Scale or None."),
            ComponentParameter("emptyContent", "(@Composable () -> Unit)?", "null", false, "Optional empty state."),
        ),
        variants = listOf(
            DocsExample(
                "Fade transition",
                "Use transition = Fade for quiet content changes.",
                """AdaptiveCarousel(..., transition = AdaptiveCarouselTransition.Fade) { item, index -> ... }""",
            ) {
                var selected by remember { mutableStateOf(0) }
                AdaptiveCarousel(listOf("Fade", "Scale", "Slide"), selected, { selected = it }, transition = AdaptiveCarouselTransition.Fade) { item, _ ->
                    SiteText("Transition: $item", fontWeight = FontWeight.Bold)
                }
            },
        ),
        themingNotes = commonNotes("AdaptiveCarousel"),
        responsiveNotes = listOf("The carousel fills width and keeps controls inside the card."),
        accessibilityNotes = listOf("Provide visible content text; avoid relying only on indicator dots."),
        limitations = listOf("Autoplay is intentionally not included."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_FEEDBACK_STATES,
        family = "Feedback",
        title = "AdaptiveEmptyState, AdaptiveLoadingState and AdaptiveErrorState",
        summary = "Polished workflow states with default glyphs, titles, descriptions and optional action slots.",
        usage = "Use these for data loading, empty search results, retryable errors and first-run states.",
        basicExample = DocsExample(
            "State trio",
            "Each state has a sensible default visual treatment.",
            """AdaptiveEmptyState("No results", description = "Try another filter.")""",
        ) {
            androidx.compose.foundation.layout.BoxWithConstraints {
                val compact = maxWidth < 600.dp
                AdaptiveGrid(columns = 12, horizontalGap = 10.dp, verticalGap = 10.dp) {
                    item(span = if (compact) 12 else 4) { Box(Modifier.heightIn(min = 180.dp)) { AdaptiveEmptyState("No results", description = "Try another filter.") } }
                    item(span = if (compact) 12 else 4) { Box(Modifier.heightIn(min = 180.dp)) { AdaptiveLoadingState(message = "Loading", indicatorStyle = AdaptiveLoadingIndicatorStyle.Dots) } }
                    item(span = if (compact) 12 else 4) { Box(Modifier.heightIn(min = 180.dp)) { AdaptiveErrorState("Unable to load", description = "Retry later.") } }
                }
            }
        },
        parameters = listOf(
            ComponentParameter("title", "String", "required", true, "AdaptiveEmptyState/AdaptiveErrorState title."),
            ComponentParameter("description", "String?", "null", false, "Optional supporting text."),
            ComponentParameter("icon", "(@Composable () -> Unit)?", "null", false, "Optional custom icon."),
            ComponentParameter("action / retryAction", "(@Composable () -> Unit)?", "null", false, "Optional user-provided action slot."),
            ComponentParameter("indicatorStyle", "AdaptiveLoadingIndicatorStyle", "Spinner", false, "AdaptiveLoadingState only: Spinner, Dots or Pulse."),
        ),
        variants = listOf(),
        themingNotes = commonNotes("Feedback states"),
        responsiveNotes = listOf("States center themselves and constrain content width."),
        accessibilityNotes = listOf("Use descriptive titles and provide retry actions for recoverable errors."),
        limitations = listOf("The action slot is caller-owned; AdaptiveKt does not infer button labels."),
    ),
    ComponentDoc(
        id = DocsRegistry.ID_ACCORDION_DIALOG,
        family = "Overlay / Disclosure",
        title = "AdaptiveAccordion and AdaptiveDialog",
        summary = "Disclosure and modal primitives for settings, confirmations and long content.",
        usage = "Use Accordion for inline detail and Dialog for focused decisions or confirmations. Dialog renders as a modal overlay and does not push page content down.",
        basicExample = DocsExample(
            "Centered modal dialog",
            "Opening the dialog overlays this page with a theme scrim; the page layout underneath stays fixed.",
            """
if (showDialog) {
    AdaptiveDialog(
        onDismissRequest = { showDialog = false },
        title = "Confirm action",
        dismissButton = { AdaptiveButton("Cancel", onClick = ...) },
        confirmButton = { AdaptiveButton("Confirm", onClick = ...) },
    ) {
        Text("Dialog content")
    }
}
            """,
        ) {
            val visualState = LocalDocsVisualState.current
            var showDialog by remember(visualState) {
                mutableStateOf(visualState == "adaptive-accordion-dialog-centered-open")
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AdaptiveAccordion(
                    title = "Account settings",
                    subtitle = "Manage preferences",
                    defaultExpanded = true,
                    modifier = Modifier.docsClickableCursor()
                ) {
                    AdaptiveButton("Open centered dialog", onClick = { showDialog = true }, modifier = Modifier.docsClickableCursor())
                }
                if (showDialog) {
                    AdaptiveDialog(
                        onDismissRequest = { showDialog = false },
                        title = "Confirm action",
                        dismissButton = { AdaptiveButton("Cancel", variant = AdaptiveButtonVariant.Ghost, onClick = { showDialog = false }, modifier = Modifier.docsClickableCursor()) },
                        confirmButton = { AdaptiveButton("Confirm", onClick = { showDialog = false }, modifier = Modifier.docsClickableCursor()) },
                    ) {
                        SiteText("This modal is rendered above the page. Opening it should not shift the accordion, cards or surrounding documentation.", maxLines = 5)
                    }
                }
            }
        },
        parameters = listOf(
            ComponentParameter("title", "String", "required", true, "AdaptiveAccordion required title."),
            ComponentParameter("subtitle", "String?", "null", false, "AdaptiveAccordion optional subtitle."),
            ComponentParameter("expanded", "Boolean?", "null", false, "AdaptiveAccordion controlled expanded state."),
            ComponentParameter("onExpandedChange", "((Boolean) -> Unit)?", "null", false, "AdaptiveAccordion expansion callback."),
            ComponentParameter("onDismissRequest", "() -> Unit", "required", true, "AdaptiveDialog dismiss callback."),
            ComponentParameter("confirmButton", "@Composable () -> Unit", "required", true, "AdaptiveDialog confirm action slot."),
            ComponentParameter("title (Dialog)", "String?", "null", false, "AdaptiveDialog optional title."),
            ComponentParameter("dismissButton", "@Composable () -> Unit", "{}", false, "AdaptiveDialog dismiss action slot."),
            ComponentParameter("dismissOnBackdropClick", "Boolean", "true", false, "Requests dismissal when the user clicks the scrim/backdrop."),
            ComponentParameter("contentSelectionEnabled", "Boolean", "false", false, "Allows text selection in the dialog body only."),
            ComponentParameter("content", "@Composable () -> Unit", "required", true, "Panel or dialog content."),
        ),
        variants = listOf(
            DocsExample(
                "Long selectable dialog",
                "Long content scrolls inside the modal surface and can opt into text selection.",
                """
AdaptiveDialog(contentSelectionEnabled = true, ...) {
    Text("Long generated content...")
}
                """,
            ) {
                val visualState = LocalDocsVisualState.current
                var showLongDialog by remember(visualState) {
                    mutableStateOf(visualState == "adaptive-accordion-dialog-long-open")
                }
                AdaptiveButton("Open long content dialog", onClick = { showLongDialog = true }, modifier = Modifier.docsClickableCursor())
                if (showLongDialog) {
                    AdaptiveDialog(
                        onDismissRequest = { showLongDialog = false },
                        title = "Generated run summary",
                        contentSelectionEnabled = true,
                        dismissButton = { AdaptiveButton("Close", variant = AdaptiveButtonVariant.Ghost, onClick = { showLongDialog = false }, modifier = Modifier.docsClickableCursor()) },
                        confirmButton = { AdaptiveButton("Copy reviewed", onClick = { showLongDialog = false }, modifier = Modifier.docsClickableCursor()) },
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            repeat(8) { index ->
                                SiteText(
                                    "Section ${index + 1}: AdaptiveKt keeps this generated explanation selectable while action buttons remain click-first.",
                                    color = SiteMuted,
                                    maxLines = 4,
                                )
                            }
                        }
                    }
                }
            },
            DocsExample(
                "Destructive confirmation",
                "Danger actions stay visually clear while the modal keeps the page underneath stable.",
                """
AdaptiveDialog(
    title = "Archive workspace",
    confirmButton = { AdaptiveButton("Archive", variant = AdaptiveButtonVariant.Danger, ...) },
)
                """,
            ) {
                val visualState = LocalDocsVisualState.current
                var showDestructiveDialog by remember(visualState) {
                    mutableStateOf(visualState == "adaptive-accordion-dialog-destructive-open")
                }
                AdaptiveButton("Open destructive dialog", variant = AdaptiveButtonVariant.Danger, onClick = { showDestructiveDialog = true }, modifier = Modifier.docsClickableCursor())
                if (showDestructiveDialog) {
                    AdaptiveDialog(
                        onDismissRequest = { showDestructiveDialog = false },
                        title = "Archive workspace",
                        dismissButton = { AdaptiveButton("Cancel", variant = AdaptiveButtonVariant.Ghost, onClick = { showDestructiveDialog = false }, modifier = Modifier.docsClickableCursor()) },
                        confirmButton = { AdaptiveButton("Archive", variant = AdaptiveButtonVariant.Danger, onClick = { showDestructiveDialog = false }, modifier = Modifier.docsClickableCursor()) },
                    ) {
                        SiteText("This action cannot be undone. The dialog is an overlay and should remain above navigation, cards and dropdowns.", color = SiteMuted, maxLines = 4)
                    }
                }
            },
            DocsExample(
                "Overlay defaults",
                "Dialog sizing and scrim behavior are centralized through AdaptiveOverlayDefaults.",
                """AdaptiveOverlayDefaults.DialogMaxWidth""",
            ) {
                AdaptiveSurface(contentPadding = PaddingValues(12.dp)) {
                    SiteText("Max width: ${AdaptiveOverlayDefaults.DialogMaxWidth}", fontWeight = FontWeight.Bold)
                    SiteText("Max height: ${AdaptiveOverlayDefaults.DialogMaxHeight}", color = SiteMuted)
                }
            },
        ),
        themingNotes = commonNotes("Overlay and disclosure"),
        responsiveNotes = listOf("Dialog constrains width on desktop and uses compact margins on small screens while remaining an overlay."),
        accessibilityNotes = listOf("Use descriptive titles and clear confirm/dismiss actions. Back/escape dismissal is delegated to Compose Dialog where supported."),
        limitations = listOf("Advanced focus trapping can be expanded later; current behavior follows Compose Multiplatform Dialog."),
    ),
)

@Composable
private fun PeopleOption(person: Person, selected: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AdaptiveAvatar(person.name, size = 30.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                SiteText(person.name, fontWeight = FontWeight.Bold, maxLines = 1)
                SiteText(person.detail, color = SiteMuted, maxLines = 1)
            }
        }
        if (selected) AdaptiveBadge("Selected", tone = AdaptiveBadgeTone.Success)
    }
}

@Composable
private fun PeopleChip(person: Person) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AdaptiveAvatar(person.name, size = 20.dp)
        Spacer(modifier = Modifier.width(6.dp))
        SiteText(person.name.substringBefore(" "), fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun NavigationTreePreview() {
    val tree = remember {
        listOf(
            AdaptiveNavigationTreeItem("workspace", "Workspace", badge = "3", children = listOf(AdaptiveNavigationTreeItem("overview", "Overview"), AdaptiveNavigationTreeItem("activity", "Activity"))),
            AdaptiveNavigationTreeItem("operations", "Operations", children = listOf(AdaptiveNavigationTreeItem("employees", "Employees"), AdaptiveNavigationTreeItem("invoices", "Invoices", badge = "New"))),
        )
    }
    var selected by remember { mutableStateOf("overview") }
    var expanded by remember { mutableStateOf(setOf("workspace", "operations")) }
    AdaptiveNavigationTree(tree, selected, { selected = it.id }, expanded, { expanded = it })
}

private data class Person(val name: String, val detail: String)

private data class DemoRow(val customer: String, val status: String, val amount: String)

private fun inputParameters(): List<ComponentParameter> = listOf(
    ComponentParameter("value", "String", "required", true, "Current text."),
    ComponentParameter("onValueChange", "(String) -> Unit", "required", true, "Text change callback."),
    ComponentParameter("label", "String?", "null", false, "Optional label above the field."),
    ComponentParameter("placeholder", "String?", "null", false, "Placeholder shown when empty."),
    ComponentParameter("enabled", "Boolean", "true", false, "Disables input."),
    ComponentParameter("validationMessage", "String?", "null", false, "Shows error styling and message."),
)

private fun selectParameters(multi: Boolean): List<ComponentParameter> = listOf(
    ComponentParameter("options", "List<T>", "required", true, "Available options."),
    ComponentParameter(if (multi) "selectedOptions" else "selectedOption", if (multi) "List<T>" else "T?", "required", true, "Current selection."),
    ComponentParameter(if (multi) "onSelectedOptionsChange" else "onSelectedOptionChange", if (multi) "(List<T>) -> Unit" else "(T?) -> Unit", "required", true, "Selection callback."),
    ComponentParameter("optionLabel", "(T) -> String", "required", true, "Label for search and default rendering."),
    ComponentParameter("searchable", "Boolean", if (multi) "true" else "false", false, "Shows local search in the menu."),
    ComponentParameter("clearable", "Boolean", "true", false, "Shows clear affordance."),
    ComponentParameter("optionContent", "Composable slot", "null", false, "Custom option row."),
    ComponentParameter(if (multi) "chipContent" else "selectedContent", "Composable slot", "null", false, "Custom selected value rendering."),
)

private fun peopleSelectExample(): DocsExample = DocsExample(
    "Custom option",
    "Use optionContent for richer rows while optionLabel remains the searchable text.",
    """AdaptiveSelect(options = people, optionLabel = { it.name }, optionContent = { person, selected -> ... })""",
) {
    val people = listOf(Person("Alicia Romero", "Product Manager"), Person("Noah Kim", "Designer"))
    var selected by remember { mutableStateOf<Person?>(people.first()) }
    AdaptiveSelect(
        options = people,
        selectedOption = selected,
        onSelectedOptionChange = { selected = it },
        optionLabel = { it.name },
        label = "Owner",
        optionContent = { person, isSelected -> PeopleOption(person, isSelected) },
        selectedContent = { person -> PeopleChip(person) },
    )
}

private fun inputDoc(
    id: String,
    title: String,
    summary: String,
    preview: @Composable () -> Unit,
): ComponentDoc = ComponentDoc(
    id = id,
    family = "Inputs",
    title = title,
    summary = summary,
    usage = "Use it where a form or filter needs a text value with clear labeling and validation.",
    basicExample = DocsExample(title, summary, """AdaptiveTextField(value, onValueChange, label = "Company")""", preview),
    parameters = inputParameters(),
    variants = listOf(
        DocsExample(
            "Validation",
            "A validation message changes the border and renders supporting text below.",
            """AdaptiveTextField(value = "", onValueChange = {}, label = "Email", validationMessage = "Email is required")""",
        ) {
            AdaptiveTextField(value = "", onValueChange = {}, label = "Email", placeholder = "team@example.com", validationMessage = "Email is required")
        },
    ),
    themingNotes = commonNotes(title),
    responsiveNotes = listOf("Fields fill width and should be stacked in one column on compact screens."),
    accessibilityNotes = listOf("Always provide label or nearby descriptive text."),
    limitations = listOf("Input masking and complex formatters are not built in."),
)

private fun simpleDisplayDoc(
    id: String,
    family: String,
    title: String,
    summary: String,
    code: String,
    parameters: List<ComponentParameter>,
    preview: @Composable () -> Unit,
): ComponentDoc = ComponentDoc(
    id = id,
    family = family,
    title = title,
    summary = summary,
    usage = "Use this primitive directly when the default visual style matches the information hierarchy you need.",
    basicExample = DocsExample(title, summary, code, preview),
    parameters = parameters,
    variants = emptyList(),
    themingNotes = commonNotes(title),
    responsiveNotes = listOf("This primitive is compact and safe inside mobile one-column layouts."),
    accessibilityNotes = listOf("Keep visible labels concise and meaningful."),
    limitations = listOf("No special limitations beyond its intentionally small scope."),
)

