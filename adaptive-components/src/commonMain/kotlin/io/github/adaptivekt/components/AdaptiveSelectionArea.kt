package io.github.adaptivekt.components

import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable

/**
 * Opt-in text selection wrapper for content-heavy AdaptiveKt surfaces.
 *
 * Keep interactive controls such as buttons, tabs, menu items and navigation labels outside this
 * wrapper so drag selection does not compete with click and hover interactions.
 */
@Composable
public fun AdaptiveSelectionArea(
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (enabled) {
        SelectionContainer(content = content)
    } else {
        DisableSelection(content = content)
    }
}
