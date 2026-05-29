package io.github.adaptivekt.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalAdaptiveColors = staticCompositionLocalOf { AdaptiveColorSchemes.defaultLight() }
private val LocalAdaptiveShapes = staticCompositionLocalOf { AdaptiveShapeScheme.default() }
private val LocalAdaptiveTypography = staticCompositionLocalOf { AdaptiveTypography.default() }
private val LocalAdaptiveStates = staticCompositionLocalOf { AdaptiveStateScheme.default() }

@Composable
public fun AdaptiveTheme(
    colorScheme: AdaptiveColorScheme = AdaptiveColorSchemes.defaultLight(),
    shapes: AdaptiveShapeScheme = AdaptiveShapeScheme.default(),
    typography: AdaptiveTypography = AdaptiveTypography.default(),
    states: AdaptiveStateScheme = AdaptiveStateScheme.default(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAdaptiveColors provides colorScheme,
        LocalAdaptiveShapes provides shapes,
        LocalAdaptiveTypography provides typography,
        LocalAdaptiveStates provides states,
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
}
