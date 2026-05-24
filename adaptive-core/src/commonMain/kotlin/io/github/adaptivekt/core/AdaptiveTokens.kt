package io.github.adaptivekt.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AdaptiveTokens {
    object Spacing {
        val XSmall: Dp = 4.dp
        val Small: Dp = 8.dp
        val Medium: Dp = 16.dp
        val Large: Dp = 24.dp
        val XLarge: Dp = 32.dp
        val XXLarge: Dp = 48.dp

        val ExtraSmall: Dp = XSmall
        val ExtraLarge: Dp = XLarge
    }

    object Radius {
        val Small: Dp = 8.dp
        val Medium: Dp = 12.dp
        val Large: Dp = 16.dp
        val XLarge: Dp = 24.dp
        val Pill: Dp = 999.dp
    }

    object Widths {
        val AuthForm: Dp = 360.dp
        val Form: Dp = 720.dp
        val Content: Dp = 1000.dp
        val Page: Dp = 1200.dp
        val Wide: Dp = 1400.dp
        val Card: Dp = 320.dp
    }

    object PaneWidths {
        val NavigationRail: Dp = 96.dp
        val Sidebar: Dp = 280.dp
        val Master: Dp = 360.dp
        val DetailMin: Dp = 320.dp
        val Filter: Dp = 320.dp
    }

    object Sizes {
        val NavItemHeight: Dp = 44.dp
        val ButtonHeight: Dp = 44.dp
        val IconBox: Dp = 32.dp
        val CardMinHeight: Dp = 72.dp
        val TableRowMinHeight: Dp = 48.dp
        val TopBarHeight: Dp = 56.dp
    }
}
