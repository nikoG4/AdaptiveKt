package io.github.adaptivekt.site

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.assertEquals

class HomeCodeComparisonCompileGuardTest {

    @Test
    fun adaptiveSnippet_containsRequiredElements() {
        val snippet = AdaptiveDataViewComparisonCode
        assertTrue(snippet.contains("AdaptiveDataView"), "Missing AdaptiveDataView")
        assertTrue(snippet.contains("AdaptiveDataContent"), "Missing AdaptiveDataContent")
        assertTrue(snippet.contains("AdaptiveDataDisplayMode.Auto"), "Missing AdaptiveDataDisplayMode.Auto")
        assertTrue(snippet.contains("AdaptiveDataMobileRole.Title"), "Missing AdaptiveDataMobileRole.Title")
        assertTrue(snippet.contains("AdaptiveDataMobileRole.Subtitle"), "Missing AdaptiveDataMobileRole.Subtitle")
        assertTrue(snippet.contains("AdaptiveDataMobileRole.Status"), "Missing AdaptiveDataMobileRole.Status")
    }

    @Test
    fun plainComposeSnippet_containsRequiredElements() {
        val snippet = PlainComposeDataViewComparisonCode
        assertTrue(snippet.contains("maxWidth < 840.dp"), "Missing correct breakpoint threshold (840.dp)")
        assertFalse(snippet.contains("maxWidth < 600.dp"), "Still contains old 600.dp breakpoint")
        assertTrue(snippet.contains("EmptyUsersState"), "Missing empty state component")
        assertTrue(snippet.contains("UserMobileCard"), "Missing mobile card component")
        assertTrue(snippet.contains("UserTableHeader"), "Missing table header component")
        assertTrue(snippet.contains("UserTableRow"), "Missing table row component")
        assertTrue(snippet.contains("UserStatusBadge"), "Missing status badge component")
    }

    private fun plainComposeUsesCards(width: Dp): Boolean =
        width < 840.dp

    @Test
    fun breakpointParity_exactBehavior() {
        val cases = listOf(0, 320, 390, 599, 600, 767, 768, 839, 840, 1024, 1199, 1200, 1920)
        
        cases.forEach { widthInt ->
            val width = widthInt.dp
            val composeUsesCards = plainComposeUsesCards(width)
            val adaptiveMode = io.github.adaptivekt.data.resolveAdaptiveDataDisplayMode(
                breakpoint = io.github.adaptivekt.core.breakpointForWidth(width),
                displayMode = io.github.adaptivekt.data.AdaptiveDataDisplayMode.Auto
            )
            
            if (composeUsesCards) {
                assertEquals(io.github.adaptivekt.data.AdaptiveDataDisplayMode.Cards, adaptiveMode, "At $width, plain compose uses Cards but AdaptiveKt uses $adaptiveMode")
            } else {
                assertEquals(io.github.adaptivekt.data.AdaptiveDataDisplayMode.Table, adaptiveMode, "At $width, plain compose uses Table but AdaptiveKt uses $adaptiveMode")
            }
        }
    }
}
