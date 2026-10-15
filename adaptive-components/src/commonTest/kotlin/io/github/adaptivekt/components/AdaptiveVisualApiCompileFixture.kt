package io.github.adaptivekt.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import io.github.adaptivekt.core.AdaptiveElevationLevel
import io.github.adaptivekt.core.AdaptiveElevationScheme
import io.github.adaptivekt.core.AdaptiveMotionScheme
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.core.AdaptiveThemeMode

/**
 * Compile-only API smoke fixture. Kept intentionally small and not executed by test runners:
 * it proves the new optional parameters and trailing-lambda patterns still resolve.
 */
@Composable
internal fun AdaptiveVisualApiCompileFixture() {
    AdaptiveTheme(
        mode = AdaptiveThemeMode.Dark,
        elevations = AdaptiveElevationScheme.defaultDark(),
        motion = AdaptiveMotionScheme.reduced(),
    ) {
        AdaptiveSurface(elevation = AdaptiveElevationLevel.None) {
            BasicText("Flat grouped content")
        }
        AdaptiveCard(onClick = {}, elevation = AdaptiveElevationLevel.Medium) {
            AdaptiveButton(text = "Save", onClick = {})
            AdaptiveIconButton(onClick = {}, contentDescription = "More options") {
                BasicText("...")
            }
            AdaptiveChip(text = "Selected", selected = true, onClick = {})
            AdaptiveTabs(tabs = listOf("First", "Second"), selectedTab = "First",
                onTabSelected = {}, tabLabel = { it })
            AdaptiveAccordion(title = "Details") {
                BasicText("Panel content")
            }
        }
        AdaptiveDialog(onDismissRequest = {}, confirmButton = {
            AdaptiveButton(text = "OK", onClick = {})
        }) {
            BasicText("Confirm operation")
        }
    }
}
