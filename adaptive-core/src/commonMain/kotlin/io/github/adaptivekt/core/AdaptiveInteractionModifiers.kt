package io.github.adaptivekt.core

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

/**
 * Applies the standard AdaptiveKt interactive cursor to clickable controls.
 *
 * Use this only when the element actually performs an action. Static text,
 * read-only badges, avatars, surfaces, and text input regions should keep
 * their platform default cursor.
 */
public fun Modifier.adaptiveInteractiveCursor(
    enabled: Boolean = true,
): Modifier {
    return if (enabled) {
        pointerHoverIcon(PointerIcon.Hand)
    } else {
        this
    }
}

public object AdaptiveInteractionDefaults {
    /**
     * Whether enabled interactive controls should request a hand/pointer cursor
     * on pointer-capable platforms.
     */
    public const val InteractiveCursorEnabled: Boolean = true
}
