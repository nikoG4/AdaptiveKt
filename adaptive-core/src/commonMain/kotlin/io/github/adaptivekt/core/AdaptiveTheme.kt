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
private val LocalAdaptiveVisualStyle = staticCompositionLocalOf { AdaptiveVisualStyle.Modern }

/**
 * Root theme provider that supplies colors, shapes, typography, and state tokens to the composition.
 *
 * @param colorScheme Optional semantic colors for surfaces, text, feedback, selections, and component states.
 * @param shapes Optional shape override; visual style supplies the default.
 * @param typography Optional typography override; visual style supplies the default.
 * @param states Optional state override; visual style supplies the default.
 * @param mode Theme mode used when colorScheme is not provided. System follows the platform color scheme.
 * @param elevations Optional elevation scheme; defaults follow the selected light/dark mode.
 * @param motion Shared animation durations and explicit reduced-motion policy.
 * @param visualStyle Modern by default; Classic retains the alpha presentation.
 * @param content The UI subtree that consumes the theme values.
 */
@Composable
public fun AdaptiveTheme(
    colorScheme: AdaptiveColorScheme? = null,
    shapes: AdaptiveShapeScheme? = null,
    typography: AdaptiveTypography? = null,
    states: AdaptiveStateScheme? = null,
    mode: AdaptiveThemeMode = AdaptiveThemeMode.System,
    elevations: AdaptiveElevationScheme? = null,
    motion: AdaptiveMotionScheme = AdaptiveMotionScheme.default(),
    visualStyle: AdaptiveVisualStyle = AdaptiveVisualStyle.Modern,
    content: @Composable () -> Unit,
) {
    val isDark = resolveAdaptiveThemeDarkMode(mode, isSystemInDarkTheme())
    val resolvedColorScheme = colorScheme ?: when (visualStyle) {
        AdaptiveVisualStyle.Modern -> if (isDark) AdaptiveVisualPresets.modernDark() else AdaptiveVisualPresets.modernLight()
        AdaptiveVisualStyle.Classic -> if (isDark) AdaptiveColorSchemes.defaultDark() else AdaptiveColorSchemes.defaultLight()
    }
    val resolvedShapes = shapes ?: when (visualStyle) {
        AdaptiveVisualStyle.Modern -> AdaptiveVisualPresets.modernShapes()
        AdaptiveVisualStyle.Classic -> AdaptiveShapeScheme.default()
    }
    val resolvedTypography = typography ?: when (visualStyle) {
        AdaptiveVisualStyle.Modern -> AdaptiveVisualPresets.modernTypography()
        AdaptiveVisualStyle.Classic -> AdaptiveTypography.default()
    }
    val resolvedStates = states ?: when (visualStyle) {
        AdaptiveVisualStyle.Modern -> AdaptiveVisualPresets.modernStates()
        AdaptiveVisualStyle.Classic -> AdaptiveStateScheme.default()
    }
    val resolvedElevations = elevations ?: if (isDark) {
        AdaptiveElevationScheme.defaultDark()
    } else {
        AdaptiveElevationScheme.defaultLight()
    }

    CompositionLocalProvider(
        LocalAdaptiveColors provides resolvedColorScheme,
        LocalAdaptiveShapes provides resolvedShapes,
        LocalAdaptiveTypography provides resolvedTypography,
        LocalAdaptiveStates provides resolvedStates,
        LocalAdaptiveVisualStyle provides visualStyle,
        LocalAdaptiveElevations provides resolvedElevations,
        LocalAdaptiveMotion provides motion,
        content = content,
    )
}

public object AdaptiveTheme {
    public val visualStyle: AdaptiveVisualStyle
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveVisualStyle.current

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
