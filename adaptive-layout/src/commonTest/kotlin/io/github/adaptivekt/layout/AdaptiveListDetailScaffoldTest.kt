package io.github.adaptivekt.layout

import io.github.adaptivekt.core.AdaptiveBreakpoint
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveListDetailScaffoldTest {

    @Test
    fun testCompactWithoutSelectionResolvesToListOnly() {
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Compact,
            hasSelection = false,
            behavior = AdaptiveListDetailBehavior()
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListOnly, mode)
    }

    @Test
    fun testCompactWithSelectionResolvesToDetailOnly() {
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Compact,
            hasSelection = true,
            behavior = AdaptiveListDetailBehavior()
        )
        assertEquals(AdaptiveListDetailResolvedMode.DetailOnly, mode)
    }

    @Test
    fun testMediumResolvesToListAndDetail() {
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Medium,
            hasSelection = false,
            behavior = AdaptiveListDetailBehavior()
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListAndDetail, mode)
    }

    @Test
    fun testExpandedResolvesToListAndDetail() {
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Expanded,
            hasSelection = true,
            behavior = AdaptiveListDetailBehavior()
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListAndDetail, mode)
    }

    @Test
    fun testLargeResolvesToListAndDetail() {
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Large,
            hasSelection = false,
            behavior = AdaptiveListDetailBehavior()
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListAndDetail, mode)
    }

    @Test
    fun testCompactAlwaysShowListResolvesToListOnlyEvenWithSelection() {
        val behavior = AdaptiveListDetailBehavior(
            compact = AdaptiveListDetailCompactBehavior.AlwaysShowList
        )
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Compact,
            hasSelection = true,
            behavior = behavior
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListOnly, mode)
    }

    @Test
    fun testCompactAlwaysShowDetailResolvesToDetailOnly() {
        val behavior = AdaptiveListDetailBehavior(
            compact = AdaptiveListDetailCompactBehavior.AlwaysShowDetail
        )
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Compact,
            hasSelection = false,
            behavior = behavior
        )
        assertEquals(AdaptiveListDetailResolvedMode.DetailOnly, mode)
    }

    @Test
    fun testMediumBehaviorOverrideListOnlyWorks() {
        val behavior = AdaptiveListDetailBehavior(
            medium = AdaptiveListDetailPaneMode.ListOnly
        )
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Medium,
            hasSelection = true,
            behavior = behavior
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListOnly, mode)
    }

    @Test
    fun testExpandedBehaviorOverrideDetailOnlyWorks() {
        val behavior = AdaptiveListDetailBehavior(
            expanded = AdaptiveListDetailPaneMode.DetailOnly
        )
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Expanded,
            hasSelection = false,
            behavior = behavior
        )
        assertEquals(AdaptiveListDetailResolvedMode.DetailOnly, mode)
    }

    @Test
    fun testLargeBehaviorOverrideListAndDetailWorks() {
        val behavior = AdaptiveListDetailBehavior(
            large = AdaptiveListDetailPaneMode.ListAndDetail
        )
        val mode = resolveAdaptiveListDetailMode(
            breakpoint = AdaptiveBreakpoint.Large,
            hasSelection = true,
            behavior = behavior
        )
        assertEquals(AdaptiveListDetailResolvedMode.ListAndDetail, mode)
    }

    @Test
    fun testPaneSpecDefaultWeightsAreSane() {
        val spec1 = AdaptivePaneSpec()
        assertEquals(1f, spec1.weight)
    }

    @Test
    fun testCustomPaneSpecPreservesProvidedValues() {
        // Need to import Dp but Dp comes from Compose, since this is testing simple data classes we just check float
        val spec = AdaptivePaneSpec(weight = 0.35f)
        assertEquals(0.35f, spec.weight)
    }

    @Test
    fun testPanePolicyUsesFullAvailableWidthByDefault() {
        val widths = resolveAdaptiveListDetailPaneWidths(
            containerWidth = 1280.dp,
            policy = AdaptiveListDetailPanePolicy()
        )

        assertEquals(1280.dp, widths.rowWidth)
        assertEquals(360.dp, widths.listWidth)
    }

    @Test
    fun testPanePolicyCapsListWidthAtMax() {
        val widths = resolveAdaptiveListDetailPaneWidths(
            containerWidth = 1800.dp,
            policy = AdaptiveListDetailPanePolicy(
                listPreferredWidth = 520.dp,
                listMaxWidth = 440.dp,
            )
        )

        assertEquals(440.dp, widths.listWidth)
    }

    @Test
    fun testPanePolicyShrinksListToPreserveDetailMinimum() {
        val widths = resolveAdaptiveListDetailPaneWidths(
            containerWidth = 768.dp,
            policy = AdaptiveListDetailPanePolicy(
                listMinWidth = 240.dp,
                listPreferredWidth = 360.dp,
                detailMinWidth = 460.dp,
                gap = 24.dp,
            )
        )

        assertEquals(768.dp, widths.rowWidth)
        assertEquals(284.dp, widths.listWidth)
    }

    @Test
    fun testPanePolicyCanUseIntrinsicWidthWhenNotFilling() {
        val widths = resolveAdaptiveListDetailPaneWidths(
            containerWidth = 1440.dp,
            policy = AdaptiveListDetailPanePolicy(
                listPreferredWidth = 320.dp,
                detailPreferredWidth = 640.dp,
                gap = 24.dp,
                fillAvailableWidth = false,
            )
        )

        assertEquals(984.dp, widths.rowWidth)
        assertEquals(320.dp, widths.listWidth)
    }
}
