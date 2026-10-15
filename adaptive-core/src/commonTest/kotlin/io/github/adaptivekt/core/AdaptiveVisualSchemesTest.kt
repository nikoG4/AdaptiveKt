package io.github.adaptivekt.core

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdaptiveVisualSchemesTest {
    @Test
    fun elevationLevelsAreOrdered() {
        for (scheme in listOf(AdaptiveElevationScheme.defaultLight(), AdaptiveElevationScheme.defaultDark())) {
            assertEquals(0.dp, scheme.resolve(AdaptiveElevationLevel.None))
            assertTrue(scheme.resolve(AdaptiveElevationLevel.Low) > 0.dp)
            assertTrue(scheme.resolve(AdaptiveElevationLevel.Medium) > scheme.resolve(AdaptiveElevationLevel.Low))
            assertTrue(scheme.resolve(AdaptiveElevationLevel.High) > scheme.resolve(AdaptiveElevationLevel.Medium))
            assertTrue(scheme.resolve(AdaptiveElevationLevel.Floating) > scheme.resolve(AdaptiveElevationLevel.High))
        }
    }

    @Test
    fun shadowsAdaptToDarkMode() {
        assertTrue(AdaptiveElevationScheme.defaultDark().shadowColor.alpha >
            AdaptiveElevationScheme.defaultLight().shadowColor.alpha)
    }

    @Test
    fun motionIsOrderedAndCanBeDisabled() {
        val motion = AdaptiveMotionScheme.default()
        assertTrue(motion.fastMillis > 0)
        assertTrue(motion.fastMillis < motion.standardMillis)
        assertTrue(motion.standardMillis < motion.emphasizedMillis)
        assertEquals(motion.standardMillis, motion.durationMillis(motion.standardMillis))
        assertEquals(0, motion.durationMillis(-5))
        assertEquals(0, AdaptiveMotionScheme.reduced().durationMillis(motion.emphasizedMillis))
    }
}
