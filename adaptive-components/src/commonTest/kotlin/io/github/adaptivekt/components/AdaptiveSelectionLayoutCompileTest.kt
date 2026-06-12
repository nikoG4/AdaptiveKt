package io.github.adaptivekt.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import kotlin.test.Test

/**
 * A compile-only test to ensure the AdaptiveCard and AdaptiveDialog with selection enabled
 * accept multiple loose children composables correctly and that the DSL does not break.
 */
class AdaptiveSelectionLayoutCompileTest {

    @Suppress("UNUSED")
    @Composable
    fun verifyCardSelectionLayout() {
        AdaptiveCard(contentSelectionEnabled = true) {
            BasicText("Line 1")
            BasicText("Line 2")
        }
    }

    @Suppress("UNUSED")
    @Composable
    fun verifyDialogSelectionLayout() {
        AdaptiveDialog(
            onDismissRequest = {},
            confirmButton = {},
            contentSelectionEnabled = true
        ) {
            BasicText("Line 1")
            BasicText("Line 2")
        }
    }

    @Test
    fun passesCompilation() {
        // This test solely checks if the above functions compile.
    }
}
