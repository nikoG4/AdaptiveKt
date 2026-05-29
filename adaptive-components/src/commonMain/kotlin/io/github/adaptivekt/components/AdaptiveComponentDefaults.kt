package io.github.adaptivekt.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.github.adaptivekt.core.AdaptiveTheme

internal object AdaptiveComponentDefaults {
    val Surface: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.surface

    val SurfaceSubtle: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.surfaceMuted

    val Border: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.border

    val BorderStrong: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.borderStrong

    val Text: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.textPrimary

    val MutedText: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.textMuted

    val DisabledSurface: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.disabledBackground

    val DisabledText: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.disabledText

    val Primary: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.primary

    val PrimarySubtle: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.primarySubtle

    val PrimaryHover: Color
        @Composable
        @ReadOnlyComposable
        get() = Color(0xFF315FDC)

    val PrimaryPressed: Color
        @Composable
        @ReadOnlyComposable
        get() = Color(0xFF1D4ED8)

    val Danger: Color
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.colors.danger

    val MediumShape: Shape
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.shapes.medium

    val LargeShape: Shape
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.shapes.large

    val PillShape: Shape
        @Composable
        @ReadOnlyComposable
        get() = AdaptiveTheme.shapes.pill
}
