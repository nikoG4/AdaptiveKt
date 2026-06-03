package io.github.adaptivekt.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.core.AdaptiveContent
import io.github.adaptivekt.core.AdaptiveInfo
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.components.AdaptiveDivider
import io.github.adaptivekt.layout.AdaptiveGrid

public interface AdaptiveFormScope {
    public fun section(
        title: String? = null,
        description: String? = null,
        content: AdaptiveFormSectionScope.() -> Unit,
    )

    public fun actions(
        content: AdaptiveFormActionsScope.() -> Unit,
    )
}

public interface AdaptiveFormSectionScope {
    public fun field(
        label: String,
        span: FieldSpan = FieldSpan.Full,
        validationMessage: AdaptiveValidationMessage? = null,
        required: Boolean = false,
        content: @Composable () -> Unit,
    )
}

public interface AdaptiveFormActionsScope {
    public fun primary(content: @Composable () -> Unit)
    public fun secondary(content: @Composable () -> Unit)
    public fun danger(content: @Composable () -> Unit)
}

/**
 * Responsive form layout component that organizes fields into sections and columns.
 *
 * @param modifier Modifier applied to the root form container.
 * @param columns Configuration for the number of columns at different breakpoints.
 * @param labelPosition Configuration for placing labels (Top or Inline).
 * @param stickyActionsOnCompact If true, form actions stay fixed at the bottom on compact screens.
 * @param maxWidth Maximum width of the form container.
 * @param content DSL builder block for defining form sections, fields, and actions.
 */
@Composable
public fun AdaptiveFormLayout(
    modifier: Modifier = Modifier,
    columns: AdaptiveFormColumns = AdaptiveFormColumns(),
    labelPosition: LabelPosition = LabelPosition.Top,
    stickyActionsOnCompact: Boolean = false,
    maxWidth: Dp = AdaptiveTokens.Widths.Form,
    content: AdaptiveFormScope.() -> Unit,
) {
    AdaptiveContent(modifier = modifier.fillMaxWidth()) {
        val activeColumns = columnsForBreakpoint(adaptiveInfo.breakpoint, columns)
        val formState = remember { AdaptiveFormScopeImpl() }
        formState.clear()
        formState.apply(content)

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = maxWidth),
            ) {
                if (adaptiveInfo.isCompact && stickyActionsOnCompact && formState.hasActions()) {
                    CompactStickyForm(
                        activeColumns = activeColumns,
                        formState = formState,
                        defaultLabelPosition = labelPosition,
                    )
                } else {
                    StandardForm(
                        adaptiveInfo = adaptiveInfo,
                        activeColumns = activeColumns,
                        formState = formState,
                        defaultLabelPosition = labelPosition,
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardForm(
    adaptiveInfo: AdaptiveInfo,
    activeColumns: Int,
    formState: AdaptiveFormScopeImpl,
    defaultLabelPosition: LabelPosition,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        formState.sections.forEachIndexed { index, section ->
            FormSection(
                section = section,
                activeColumns = activeColumns,
                defaultLabelPosition = defaultLabelPosition,
                showDivider = index > 0,
            )
        }

        if (formState.hasActions()) {
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
            FormActions(
                actions = formState.actions,
                isCompact = adaptiveInfo.isCompact,
            )
        }
    }
}

@Composable
private fun CompactStickyForm(
    activeColumns: Int,
    formState: AdaptiveFormScopeImpl,
    defaultLabelPosition: LabelPosition,
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = AdaptiveTokens.Sizes.ButtonHeight + AdaptiveTokens.Spacing.Large * 2),
        ) {
            formState.sections.forEachIndexed { index, section ->
                FormSection(
                    section = section,
                    activeColumns = activeColumns,
                    defaultLabelPosition = defaultLabelPosition,
                    showDivider = index > 0,
                )
            }
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large * 2))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(AdaptiveTheme.colors.surfaceMuted)
                .padding(AdaptiveTokens.Spacing.Medium),
        ) {
            FormActions(
                actions = formState.actions,
                isCompact = true,
            )
        }
    }
}

@Composable
private fun FormSection(
    section: FormSectionModel,
    activeColumns: Int,
    defaultLabelPosition: LabelPosition,
    showDivider: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = AdaptiveTokens.Spacing.Medium)) {
        if (showDivider) {
            AdaptiveDivider()
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        }

        section.title?.let {
            BasicText(
                text = it,
                style = AdaptiveTheme.typography.subtitle.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AdaptiveTheme.colors.textPrimary,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        }

        section.description?.let {
            BasicText(
                text = it,
                style = AdaptiveTheme.typography.body.copy(color = AdaptiveTheme.colors.textMuted),
            )
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        }

        AdaptiveGrid(
            columns = activeColumns,
            horizontalGap = AdaptiveTokens.Spacing.Medium,
            verticalGap = AdaptiveTokens.Spacing.Medium,
        ) {
            section.fields.forEach { field ->
                item(span = resolveFieldSpan(field.span, activeColumns)) {
                    FormFieldItem(
                        field = field,
                        defaultLabelPosition = defaultLabelPosition,
                        activeColumns = activeColumns,
                    )
                }
            }
        }
    }
}

@Composable
private fun FormFieldItem(
    field: FormFieldModel,
    defaultLabelPosition: LabelPosition,
    activeColumns: Int,
) {
    val resolvedLabelPosition = if (activeColumns == 1) {
        LabelPosition.Top
    } else {
        defaultLabelPosition
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (resolvedLabelPosition == LabelPosition.Top) {
            FieldLabel(field)
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            field.content()
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FieldLabel(field)
                Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
                Box(modifier = Modifier.fillMaxWidth()) {
                    field.content()
                }
            }
        }

        field.validationMessage?.let { message ->
            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
            ValidationMessageView(message)
        }
    }
}

@Composable
private fun FieldLabel(field: FormFieldModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicText(
            text = field.label,
            style = AdaptiveTheme.typography.label.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AdaptiveTheme.colors.textPrimary,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (field.required) {
            Spacer(modifier = Modifier.width(AdaptiveTokens.Spacing.Small))
            BasicText(
                text = "*",
                style = AdaptiveTheme.typography.label.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AdaptiveTheme.colors.danger,
                ),
            )
        }
    }
}

@Composable
private fun ValidationMessageView(message: AdaptiveValidationMessage) {
    val color = when (message.type) {
        AdaptiveValidationMessageType.Error -> AdaptiveTheme.colors.danger
        AdaptiveValidationMessageType.Warning -> AdaptiveTheme.colors.warning
        AdaptiveValidationMessageType.Info -> AdaptiveTheme.colors.info
    }

    BasicText(
        text = message.message,
        style = AdaptiveTheme.typography.caption.copy(color = color),
    )
}

@Composable
private fun FormActions(
    actions: FormActionsModel,
    isCompact: Boolean,
) {
    if (!actions.hasActions()) return

    if (isCompact) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            actions.primary.forEach { it() }
            actions.secondary.forEach { it() }
            actions.danger.forEach { it() }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
        ) {
            actions.primary.forEach { it() }
            actions.secondary.forEach { it() }
            actions.danger.forEach { it() }
        }
    }
}

private data class FormSectionModel(
    val title: String?,
    val description: String?,
    val fields: MutableList<FormFieldModel> = mutableListOf(),
)

private data class FormFieldModel(
    val label: String,
    val span: FieldSpan,
    val validationMessage: AdaptiveValidationMessage?,
    val required: Boolean,
    val content: @Composable () -> Unit,
)

private data class FormActionsModel(
    val primary: MutableList<@Composable () -> Unit> = mutableListOf(),
    val secondary: MutableList<@Composable () -> Unit> = mutableListOf(),
    val danger: MutableList<@Composable () -> Unit> = mutableListOf(),
) {
    fun hasActions(): Boolean = primary.isNotEmpty() || secondary.isNotEmpty() || danger.isNotEmpty()
}

private class AdaptiveFormScopeImpl : AdaptiveFormScope {
    val sections = mutableListOf<FormSectionModel>()
    val actions = FormActionsModel()
    private var currentSection: FormSectionModel? = null

    override fun section(
        title: String?,
        description: String?,
        content: AdaptiveFormSectionScope.() -> Unit,
    ) {
        val section = FormSectionModel(title = title, description = description)
        currentSection = section
        AdaptiveFormSectionScopeImpl(section).content()
        sections.add(section)
        currentSection = null
    }

    override fun actions(content: AdaptiveFormActionsScope.() -> Unit) {
        content(AdaptiveFormActionsScopeImpl(actions))
    }

    fun hasActions(): Boolean = actions.hasActions()

    fun clear() {
        sections.clear()
        actions.primary.clear()
        actions.secondary.clear()
        actions.danger.clear()
    }
}

private class AdaptiveFormSectionScopeImpl(
    private val section: FormSectionModel,
) : AdaptiveFormSectionScope {
    override fun field(
        label: String,
        span: FieldSpan,
        validationMessage: AdaptiveValidationMessage?,
        required: Boolean,
        content: @Composable () -> Unit,
    ) {
        section.fields.add(
            FormFieldModel(
                label = label,
                span = span,
                validationMessage = validationMessage,
                required = required,
                content = content,
            ),
        )
    }
}

private class AdaptiveFormActionsScopeImpl(
    private val actions: FormActionsModel,
) : AdaptiveFormActionsScope {
    override fun primary(content: @Composable () -> Unit) {
        actions.primary.add(content)
    }

    override fun secondary(content: @Composable () -> Unit) {
        actions.secondary.add(content)
    }

    override fun danger(content: @Composable () -> Unit) {
        actions.danger.add(content)
    }
}
