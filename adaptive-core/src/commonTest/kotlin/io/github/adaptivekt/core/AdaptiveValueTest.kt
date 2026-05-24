package io.github.adaptivekt.core

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveValueTest {
    @Test
    fun `compact always returns compact value`() {
        assertEquals("compact", adaptiveValue(AdaptiveBreakpoint.Compact, "compact", "medium", "expanded", "large"))
    }

    @Test
    fun `medium returns medium when available`() {
        assertEquals("medium", adaptiveValue(AdaptiveBreakpoint.Medium, "compact", "medium", "expanded", "large"))
    }

    @Test
    fun `medium falls back to compact when medium is null`() {
        assertEquals("compact", adaptiveValue(AdaptiveBreakpoint.Medium, "compact", null, "expanded", "large"))
    }

    @Test
    fun `expanded returns expanded when available`() {
        assertEquals("expanded", adaptiveValue(AdaptiveBreakpoint.Expanded, "compact", "medium", "expanded", "large"))
    }

    @Test
    fun `expanded falls back to medium when expanded is null`() {
        assertEquals("medium", adaptiveValue(AdaptiveBreakpoint.Expanded, "compact", "medium", null, "large"))
    }

    @Test
    fun `expanded falls back to compact when expanded and medium are null`() {
        assertEquals("compact", adaptiveValue(AdaptiveBreakpoint.Expanded, "compact", null, null, "large"))
    }

    @Test
    fun `large returns large when available`() {
        assertEquals("large", adaptiveValue(AdaptiveBreakpoint.Large, "compact", "medium", "expanded", "large"))
    }

    @Test
    fun `large falls back to expanded when large is null`() {
        assertEquals("expanded", adaptiveValue(AdaptiveBreakpoint.Large, "compact", "medium", "expanded", null))
    }

    @Test
    fun `large falls back to medium when large and expanded are null`() {
        assertEquals("medium", adaptiveValue(AdaptiveBreakpoint.Large, "compact", "medium", null, null))
    }

    @Test
    fun `large falls back to compact when large, expanded, and medium are null`() {
        assertEquals("compact", adaptiveValue(AdaptiveBreakpoint.Large, "compact", null, null, null))
    }
}
