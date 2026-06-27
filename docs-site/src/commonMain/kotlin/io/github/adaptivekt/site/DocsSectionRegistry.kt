package io.github.adaptivekt.site

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import kotlin.math.roundToInt

@Stable
internal class DocsSectionRegistry {
    private val sectionPositions = mutableStateMapOf<String, Int>()

    fun register(id: String, yPos: Int) {
        sectionPositions[id] = yPos
    }

    fun unregister(id: String) {
        sectionPositions.remove(id)
    }

    fun getPosition(id: String): Int? {
        return sectionPositions[id]
    }

    // For TOC tracking (not perfectly accurate without scroll offset, but good enough for static TOC)
    fun getActiveSection(currentScroll: Int): String? {
        if (sectionPositions.isEmpty()) return null

        // Find the section with the largest Y that is <= currentScroll + offset
        // We add an offset so clicking a link doesn't just put it exactly at the top.
        val targetY = currentScroll + 100

        var bestId: String? = null
        var bestY = -1

        for ((id, y) in sectionPositions) {
            if (y <= targetY && y > bestY) {
                bestY = y
                bestId = id
            }
        }

        // If we are at the very top, return the first section if any
        if (bestId == null && sectionPositions.isNotEmpty()) {
            return sectionPositions.entries.minByOrNull { it.value }?.key
        }

        return bestId
    }
}

internal val LocalDocsSectionRegistry = compositionLocalOf { DocsSectionRegistry() }

@Composable
internal fun DocsSectionAnchor(
    id: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val registry = LocalDocsSectionRegistry.current

    androidx.compose.foundation.layout.Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            val y = coordinates.positionInRoot().y.roundToInt()
            registry.register(id, y)
        }
    ) {
        content()
    }
}


