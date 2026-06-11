package io.github.adaptivekt.components

import kotlin.test.Test
import kotlin.test.assertTrue

class AdaptiveOverlayDefaultsTest {
    @Test
    fun dialogDefaultsKeepModalWithinUsableBounds() {
        assertTrue(AdaptiveOverlayDefaults.DialogMaxWidth.value > AdaptiveOverlayDefaults.DialogCompactMargin.value)
        assertTrue(AdaptiveOverlayDefaults.DialogMaxHeight.value > AdaptiveOverlayDefaults.DialogCompactMargin.value)
        assertTrue(AdaptiveOverlayDefaults.DialogContentPadding.calculateTopPadding().value > 0f)
        assertTrue(AdaptiveOverlayDefaults.DialogContentPadding.calculateBottomPadding().value > 0f)
    }
}

