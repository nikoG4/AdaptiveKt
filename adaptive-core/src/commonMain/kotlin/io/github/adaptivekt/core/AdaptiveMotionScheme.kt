package io.github.adaptivekt.core

/**
 * Shared timing tokens for motion in Compose components.
 *
 * Set animationsEnabled to false when the host app requests reduced motion.
 * Existing animations should opt into durationMillis as components are migrated.
 */
public data class AdaptiveMotionScheme(
    val fastMillis: Int = 120,
    val standardMillis: Int = 180,
    val emphasizedMillis: Int = 260,
    val animationsEnabled: Boolean = true,
) {
    public fun durationMillis(requestedMillis: Int): Int =
        if (animationsEnabled) requestedMillis.coerceAtLeast(0) else 0

    public companion object {
        public fun default(): AdaptiveMotionScheme = AdaptiveMotionScheme()
        public fun reduced(): AdaptiveMotionScheme = AdaptiveMotionScheme(animationsEnabled = false)
    }
}
