package io.github.adaptivekt.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalAdaptiveColors = staticCompositionLocalOf { AdaptiveColorSchemes.defaultLight() }
private val LocalAdaptiveShapes = staticCompositionLocalOf { AdaptiveShapeScheme.default() }
private val LocalAdaptiveTypography = staticCompositionLocalOf { AdaptiveTypography.default() }
private val LocalAdaptiveStates = staticCompositionLocalOf { AdaptiveStateScheme.default() }
private val LocalAdaptiveElevations = staticCompositionLocalOf { AdaptiveElevationScheme.defaultLight() }
private val LocalAdaptiveMotion = staticCompositionLocalOf { AdaptiveMotionScheme.default() }

/**
 * Root theme provider that supplies colors, shapes, typography, and state tokens to the composition.
 *
 * @param colorScheme Optional semantic colors for surfaces, text, feedback, selections, and component states.
 * @param shapes Shape tokens used by cards, buttons, menus, and fields.
 * @param typography Typography defaults for labels, body text, and headings.
 * @param states State tokens for hover, pressed, selected, disabled, and focus behaviors.
 * @param mode Theme mode used when colorScheme is not provided. System follows the platform color scheme.
 * @param elevations Optional elevation scheme; defaults follow the selected light/dark mode.
 * @param motion Shared animation durations and explicit reduced-motion policy.
 * @param content The UI subtree that consumes the theme values.
 */
@Composable
public fun AdaptiveTheme(
    colorScheme: AdaptiveColorScheme? = null,
    shapes: AdaptiveShapeScheme = AdaptiveShapeScheme.default(),
    typography: AdaptiveTypography = AdaptiveTypography.default(),
    states: AdaptiveStateScheme = AdaptiveStateScheme.default(),
    mode: AdaptiveThemeMode = AdaptiveThemeMode.System,
    elevations: AdaptiveElevationScheme? = null,
    motion: AdaptiveMotionScheme = AdaptiveMotionScheme.default(),
    content: @Composable () -> Unit,
) {
    val isDark = resolveAdaptiveThemeDarkMode(mode, isSystemInDarkTheme())
    val resolvedColorScheme = colorScheme ?: if (isDark) {
        AdaptiveColorSchemes.defaultDark()
    } else {
        AdaptiveColorSchemes.defaultLight()
    }
    val resolvedElevations = elevations ?: if (isDark) {
        AdaptiveElevationScheme.defaultDark()
    } else {
        AdaptiveElevationScheme.defaultLight()
    }

    CompositionLocalProvider(
        LocalAdaptiveColors provides resolvedColorScheme,
        LocalAdaptiveShapes provides shapes,
        LocalAdaptiveTypography provides typography,
        LocalAdaptiveStates provides states,
        LocalAdaptiveElevations provides resolvedElevations,
        LocalAdaptiveMotion provides motion,
        content = content,
    )
}

public object AdaptiveTheme {
    public val colors: AdaptiveColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveColors.current

    public val shapes: AdaptiveShapeScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveShapes.current

    public val typography: AdaptiveTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveTypography.current

    public val states: AdaptiveStateScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveStates.current

    public val elevations: AdaptiveElevationScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveElevations.current

    public val motion: AdaptiveMotionScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveMotion.current
}
