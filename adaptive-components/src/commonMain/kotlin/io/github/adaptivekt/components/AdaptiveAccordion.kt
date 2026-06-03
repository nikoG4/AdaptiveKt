package io.github.adaptivekt.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveTokens

/**
 * Expandable disclosure component for inline detail or nested settings.
 *
 * @param title Primary text label.
 * @param modifier Modifier applied to the root accordion container.
 * @param subtitle Optional secondary text label.
 * @param expanded When non-null, the expansion state is externally controlled.
 * @param defaultExpanded Initial expansion state when internally controlled.
 * @param onExpandedChange Optional callback invoked when the user toggles the accordion.
 * @param content Composable slot for the expandable content area.
 */
@Composable
public fun AdaptiveAccordion(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    expanded: Boolean? = null,
    defaultExpanded: Boolean = false,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var internalExpanded by rememberSaveable { mutableStateOf(defaultExpanded) }
    val isExpanded = expanded ?: internalExpanded
    val updateExpanded: (Boolean) -> Unit = { newValue ->
        if (expanded == null) {
            internalExpanded = newValue
        }
        onExpandedChange?.invoke(newValue)
    }

    val shape = AdaptiveComponentDefaults.MediumShape
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AdaptiveComponentDefaults.Surface, shape)
            .border(1.dp, AdaptiveComponentDefaults.Border, shape)
            .padding(AdaptiveTokens.Spacing.Medium),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { updateExpanded(!isExpanded) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                BasicText(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AdaptiveTheme.colors.textPrimary,
                    ),
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    BasicText(
                        text = subtitle,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = AdaptiveTheme.colors.textSecondary,
                        ),
                    )
                }
            }
            AdaptiveIcons.ChevronDown(
                size = 18.dp,
                tint = AdaptiveTheme.colors.textSecondary,
                modifier = Modifier.rotate(if (isExpanded) 180f else 0f),
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = AdaptiveTokens.Spacing.Medium)) {
                content()
            }
        }
    }
}
