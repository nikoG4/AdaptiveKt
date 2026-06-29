package io.github.adaptivekt.site

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Stable
internal class DocsSectionRegistry {
    private val sectionPositions = mutableStateMapOf<String, Int>()
    private val sectionRequesters = mutableMapOf<String, BringIntoViewRequester>()

    fun register(id: String, yPos: Int, requester: BringIntoViewRequester) {
        sectionPositions[id] = yPos
        sectionRequesters[id] = requester
    }

    fun unregister(id: String) {
        sectionPositions.remove(id)
        sectionRequesters.remove(id)
    }

    suspend fun scrollToSection(id: String) {
        for (i in 0..20) {
            val requester = sectionRequesters[id]
            if (requester != null) {
                try {
                    requester.bringIntoView()
                } catch (e: Exception) {
                    // Ignore cancellation or unattached errors
                }
                return
            }
            kotlinx.coroutines.delay(50)
        }
    }

    fun getActiveSection(currentScroll: Int): String? {
        if (sectionPositions.isEmpty()) return null

        val targetY = currentScroll + 100

        var bestId: String? = null
        var bestY = -1

        for ((id, y) in sectionPositions) {
            if (y <= targetY && y > bestY) {
                bestY = y
                bestId = id
            }
        }

        if (bestId == null && sectionPositions.isNotEmpty()) {
            return sectionPositions.entries.minByOrNull { it.value }?.key
        }

        return bestId
    }
}

internal val LocalDocsSectionRegistry = compositionLocalOf { DocsSectionRegistry() }

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DocsSectionAnchor(
    id: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val registry = LocalDocsSectionRegistry.current
    val requester = remember { BringIntoViewRequester() }

    androidx.compose.runtime.DisposableEffect(id) {
        onDispose {
            registry.unregister(id)
        }
    }

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .bringIntoViewRequester(requester)
            .onGloballyPositioned { coordinates ->
                val y = coordinates.positionInRoot().y.roundToInt()
                registry.register(id, y, requester)
            }
    ) {
        content()
    }
}
