package io.github.adaptivekt.examples.communication.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object DemoIcons {
    val Person: ImageVector
        get() = ImageVector.Builder(
            name = "Person",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 12f)
            curveToRelative(2.21f, 0f, 4f, -1.79f, 4f, -4f)
            reflectiveCurveToRelative(-1.79f, -4f, -4f, -4f)
            reflectiveCurveToRelative(-4f, 1.79f, -4f, 4f)
            reflectiveCurveToRelative(1.79f, 4f, 4f, 4f)
            close()
            moveTo(12f, 14f)
            curveToRelative(-2.67f, 0f, -8f, 1.34f, -8f, 4f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(16f)
            verticalLineToRelative(-2f)
            curveToRelative(0f, -2.66f, -5.33f, -4f, -8f, -4f)
            close()
        }.build()

    val Settings: ImageVector
        get() = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(19.14f, 12.94f)
            curveToRelative(0.04f, -0.3f, 0.06f, -0.61f, 0.06f, -0.94f)
            curveToRelative(0f, -0.32f, -0.02f, -0.64f, -0.06f, -0.94f)
            lineToRelative(2.03f, -1.58f)
            curveToRelative(0.18f, -0.14f, 0.23f, -0.41f, 0.12f, -0.61f)
            lineToRelative(-1.92f, -3.32f)
            curveToRelative(-0.12f, -0.22f, -0.37f, -0.29f, -0.59f, -0.22f)
            lineToRelative(-2.39f, 0.96f)
            curveToRelative(-0.5f, -0.38f, -1.03f, -0.7f, -1.62f, -0.94f)
            lineToRelative(-0.36f, -2.54f)
            curveToRelative(-0.04f, -0.24f, -0.24f, -0.41f, -0.48f, -0.41f)
            horizontalLineToRelative(-3.84f)
            curveToRelative(-0.24f, 0f, -0.43f, 0.17f, -0.47f, 0.41f)
            lineToRelative(-0.36f, 2.54f)
            curveToRelative(-0.59f, 0.24f, -1.13f, 0.56f, -1.62f, 0.94f)
            lineToRelative(-2.39f, -0.96f)
            curveToRelative(-0.22f, -0.08f, -0.47f, 0f, -0.59f, 0.22f)
            lineTo(2.73f, 8.9f)
            curveToRelative(-0.12f, 0.21f, -0.08f, 0.47f, 0.12f, 0.61f)
            lineToRelative(2.03f, 1.58f)
            curveToRelative(-0.05f, 0.3f, -0.08f, 0.62f, -0.08f, 0.94f)
            reflectiveCurveToRelative(0.03f, 0.64f, 0.08f, 0.94f)
            lineToRelative(-2.03f, 1.58f)
            curveToRelative(-0.18f, 0.14f, -0.23f, 0.41f, -0.12f, 0.61f)
            lineToRelative(1.92f, 3.32f)
            curveToRelative(0.12f, 0.22f, 0.37f, 0.29f, 0.59f, 0.22f)
            lineToRelative(2.39f, -0.96f)
            curveToRelative(0.5f, 0.38f, 1.03f, 0.7f, 1.62f, 0.94f)
            lineToRelative(0.36f, 2.54f)
            curveToRelative(0.05f, 0.24f, 0.24f, 0.41f, 0.48f, 0.41f)
            horizontalLineToRelative(3.84f)
            curveToRelative(0.24f, 0f, 0.43f, -0.17f, 0.47f, -0.41f)
            lineToRelative(0.36f, -2.54f)
            curveToRelative(0.59f, -0.24f, 1.13f, -0.56f, 1.62f, -0.94f)
            lineToRelative(2.39f, 0.96f)
            curveToRelative(0.22f, 0.08f, 0.47f, 0f, 0.59f, -0.22f)
            lineToRelative(1.92f, -3.32f)
            curveToRelative(0.12f, -0.22f, 0.07f, -0.49f, -0.12f, -0.61f)
            lineToRelative(-2.01f, -1.58f)
            close()
            moveTo(12f, 15.6f)
            curveToRelative(-1.98f, 0f, -3.6f, -1.62f, -3.6f, -3.6f)
            reflectiveCurveToRelative(1.62f, -3.6f, 3.6f, -3.6f)
            reflectiveCurveToRelative(3.6f, 1.62f, 3.6f, 3.6f)
            reflectiveCurveToRelative(-1.62f, 3.6f, -3.6f, 3.6f)
            close()
        }.build()

    val Notifications: ImageVector
        get() = ImageVector.Builder(
            name = "Notifications",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 22f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            horizontalLineToRelative(-4f)
            curveToRelative(0f, 1.1f, 0.89f, 2f, 2f, 2f)
            close()
            moveTo(18f, 16f)
            verticalLineToRelative(-5f)
            curveToRelative(0f, -3.07f, -1.64f, -5.64f, -4.5f, -6.32f)
            lineTo(13.5f, 4f)
            curveToRelative(0f, -0.83f, -0.67f, -1.5f, -1.5f, -1.5f)
            reflectiveCurveToRelative(-1.5f, 0.67f, -1.5f, 1.5f)
            verticalLineToRelative(0.68f)
            curveTo(7.63f, 5.36f, 6f, 7.92f, 6f, 11f)
            verticalLineToRelative(5f)
            lineToRelative(-2f, 2f)
            verticalLineToRelative(1f)
            horizontalLineToRelative(16f)
            verticalLineToRelative(-1f)
            lineToRelative(-2f, -2f)
            close()
        }.build()

    val Lock: ImageVector
        get() = ImageVector.Builder(
            name = "Lock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(18f, 8f)
            horizontalLineToRelative(-1f)
            lineTo(17f, 6f)
            curveToRelative(0f, -2.76f, -2.24f, -5f, -5f, -5f)
            reflectiveCurveTo(7f, 3.24f, 7f, 6f)
            verticalLineToRelative(2f)
            lineTo(6f, 8f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(10f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(12f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            lineTo(20f, 10f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveTo(9f, 6f)
            curveToRelative(0f, -1.66f, 1.34f, -3f, 3f, -3f)
            reflectiveCurveToRelative(3f, 1.34f, 3f, 3f)
            verticalLineToRelative(2f)
            lineTo(9f, 8f)
            lineTo(9f, 6f)
            close()
            moveTo(12f, 17f)
            curveToRelative(-1.1f, 0f, -2f, -0.9f, -2f, -2f)
            reflectiveCurveToRelative(0.9f, -2f, 2f, -2f)
            reflectiveCurveToRelative(2f, 0.9f, 2f, 2f)
            reflectiveCurveToRelative(-0.9f, 2f, -2f, 2f)
            close()
        }.build()

    val Build: ImageVector
        get() = ImageVector.Builder(
            name = "Build",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(22.7f, 19f)
            lineToRelative(-9.1f, -9.1f)
            curveToRelative(0.9f, -2.3f, 0.4f, -5f, -1.5f, -6.9f)
            curveToRelative(-2f, -2f, -5f, -2.4f, -7.4f, -1.1f)
            lineTo(7.6f, 4.8f)
            lineTo(5.6f, 6.8f)
            lineTo(2.7f, 3.9f)
            lineTo(1.3f, 5.3f)
            lineToRelative(2.9f, 2.9f)
            lineToRelative(-2f, 2f)
            lineToRelative(2.9f, 2.9f)
            curveToRelative(1.3f, 2.4f, 4.5f, 2.8f, 6.5f, 0.8f)
            curveToRelative(1.9f, -1.9f, 2.4f, -4.6f, 1.5f, -6.9f)
            lineToRelative(9.1f, 9.1f)
            curveToRelative(0.4f, 0.4f, 1f, 0.4f, 1.4f, 0f)
            lineToRelative(1.1f, -1.1f)
            curveToRelative(0.4f, -0.4f, 0.4f, -1f, 0f, -1.4f)
            close()
        }.build()

    val Info: ImageVector
        get() = ImageVector.Builder(
            name = "Info",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveToRelative(4.48f, 10f, 10f, 10f)
            reflectiveCurveToRelative(10f, -4.48f, 10f, -10f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()
            moveTo(13f, 17f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-6f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(6f)
            close()
            moveTo(13f, 9f)
            horizontalLineToRelative(-2f)
            lineTo(11f, 7f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            close()
        }.build()

    val KeyboardArrowRight: ImageVector
        get() = ImageVector.Builder(
            name = "KeyboardArrowRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(8.59f, 16.59f)
            lineTo(13.17f, 12f)
            lineTo(8.59f, 7.41f)
            lineTo(10f, 6f)
            lineToRelative(6f, 6f)
            lineToRelative(-6f, 6f)
            lineToRelative(-1.41f, -1.41f)
            close()
        }.build()

    val Call: ImageVector
        get() = ImageVector.Builder(
            name = "Call",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(20.01f, 15.38f)
            curveToRelative(-1.23f, 0f, -2.42f, -0.2f, -3.53f, -0.56f)
            curveToRelative(-0.35f, -0.12f, -0.74f, -0.03f, -1.01f, 0.24f)
            lineToRelative(-1.57f, 1.97f)
            curveToRelative(-2.83f, -1.35f, -5.48f, -3.9f, -6.89f, -6.83f)
            lineToRelative(1.95f, -1.66f)
            curveToRelative(0.27f, -0.28f, 0.35f, -0.67f, 0.24f, -1.02f)
            curveToRelative(-0.37f, -1.11f, -0.56f, -2.3f, -0.56f, -3.53f)
            curveToRelative(0f, -0.55f, -0.45f, -1f, -1f, -1f)
            lineTo(2.19f, 2.99f)
            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
            curveToRelative(0f, 9.39f, 7.61f, 17f, 17f, 17f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-3.49f)
            curveToRelative(0f, -0.55f, -0.45f, -1.0f, -1f, -1.0f)
            close()
        }.build()

    val Face: ImageVector
        get() = ImageVector.Builder(
            name = "Face",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(9f, 11.75f)
            curveToRelative(-0.69f, 0f, -1.25f, 0.56f, -1.25f, 1.25f)
            reflectiveCurveToRelative(0.56f, 1.25f, 1.25f, 1.25f)
            reflectiveCurveToRelative(1.25f, -0.56f, 1.25f, -1.25f)
            reflectiveCurveToRelative(-0.56f, -1.25f, -1.25f, -1.25f)
            close()
            moveTo(15f, 11.75f)
            curveToRelative(-0.69f, 0f, -1.25f, 0.56f, -1.25f, 1.25f)
            reflectiveCurveToRelative(0.56f, 1.25f, 1.25f, 1.25f)
            reflectiveCurveToRelative(1.25f, -0.56f, 1.25f, -1.25f)
            reflectiveCurveToRelative(-0.56f, -1.25f, -1.25f, -1.25f)
            close()
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveToRelative(4.48f, 10f, 10f, 10f)
            reflectiveCurveToRelative(10f, -4.48f, 10f, -10f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()
            moveTo(12f, 20f)
            curveToRelative(-4.41f, 0f, -8f, -3.59f, -8f, -8f)
            curveToRelative(0f, -0.29f, 0.02f, -0.58f, 0.05f, -0.86f)
            curveToRelative(2.36f, 1.05f, 4.23f, 2.98f, 5.21f, 5.37f)
            curveTo(11.07f, 13.33f, 14.05f, 11f, 17.42f, 11f)
            curveToRelative(0.78f, 0f, 1.53f, 0.09f, 2.25f, 0.26f)
            curveToRelative(0.21f, 0.85f, 0.33f, 1.77f, 0.33f, 2.74f)
            curveToRelative(0f, 4.41f, -3.59f, 8f, -8f, 8f)
            close()
        }.build()

    val Email: ImageVector
        get() = ImageVector.Builder(
            name = "Email",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 4f)
            lineTo(4f, 4f)
            curveToRelative(-1.1f, 0f, -1.99f, 0.9f, -1.99f, 2f)
            lineTo(2f, 18f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(16f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            lineTo(22f, 6f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveTo(20f, 8f)
            lineToRelative(-8f, 5f)
            lineToRelative(-8f, -5f)
            lineTo(4f, 6f)
            lineToRelative(8f, 5f)
            lineToRelative(8f, -5f)
            verticalLineToRelative(2f)
            close()
        }.build()

    val AddCircle: ImageVector
        get() = ImageVector.Builder(
            name = "AddCircle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveToRelative(4.48f, 10f, 10f, 10f)
            reflectiveCurveToRelative(10f, -4.48f, 10f, -10f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()
            moveTo(17f, 13f)
            horizontalLineToRelative(-4f)
            verticalLineToRelative(4f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-4f)
            lineTo(7f, 13f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(4f)
            lineTo(11f, 7f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(4f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(2f)
            close()
        }.build()

    val Star: ImageVector
        get() = ImageVector.Builder(
            name = "Star",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 17.27f)
            lineTo(18.18f, 21f)
            lineToRelative(-1.64f, -7.03f)
            lineTo(22f, 9.24f)
            lineToRelative(-7.19f, -0.61f)
            lineTo(12f, 2f)
            lineTo(9.19f, 8.63f)
            lineTo(2f, 9.24f)
            lineToRelative(5.46f, 4.73f)
            lineTo(5.82f, 21f)
            close()
        }.build()
    val Add: ImageVector
    get() = ImageVector.Builder(
        name = "Add",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color.Black)) {
        moveTo(19f, 13f)
        horizontalLineToRelative(-6f)
        verticalLineToRelative(6f)
        horizontalLineToRelative(-2f)
        verticalLineToRelative(-6f)
        lineTo(5f, 13f)
        verticalLineToRelative(-2f)
        horizontalLineToRelative(6f)
        lineTo(11f, 5f)
        horizontalLineToRelative(2f)
        verticalLineToRelative(6f)
        horizontalLineToRelative(6f)
        verticalLineToRelative(2f)
        close()
    }.build()

val Search: ImageVector
    get() = ImageVector.Builder(
        name = "Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color.Black)) {
        moveTo(15.5f, 14f)
        horizontalLineToRelative(-0.79f)
        lineToRelative(-0.28f, -0.27f)
        curveTo(15.41f, 12.59f, 16f, 11.11f, 16f, 9.5f)
        curveTo(16f, 5.91f, 13.09f, 3f, 9.5f, 3f)
        reflectiveCurveTo(3f, 5.91f, 3f, 9.5f)
        reflectiveCurveTo(5.91f, 16f, 9.5f, 16f)
        curveToRelative(1.61f, 0f, 3.09f, -0.59f, 4.23f, -1.57f)
        lineToRelative(0.27f, 0.28f)
        verticalLineToRelative(0.79f)
        lineToRelative(5f, 4.99f)
        lineTo(20.49f, 19f)
        lineToRelative(-4.99f, -5f)
        close()
        moveTo(9.5f, 14f)
        curveTo(7.01f, 14f, 5f, 11.99f, 5f, 9.5f)
        reflectiveCurveTo(7.01f, 5f, 9.5f, 5f)
        reflectiveCurveTo(14f, 7.01f, 14f, 9.5f)
        reflectiveCurveTo(11.99f, 14f, 9.5f, 14f)
        close()
    }.build()

val Close: ImageVector
    get() = ImageVector.Builder(
        name = "Close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color.Black)) {
        moveTo(19f, 6.41f)
        lineTo(17.59f, 5f)
        lineTo(12f, 10.59f)
        lineTo(6.41f, 5f)
        lineTo(5f, 6.41f)
        lineTo(10.59f, 12f)
        lineTo(5f, 17.59f)
        lineTo(6.41f, 19f)
        lineTo(12f, 13.41f)
        lineTo(17.59f, 19f)
        lineTo(19f, 17.59f)
        lineTo(13.41f, 12f)
        close()
    }.build()

    val ArrowBack: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineToRelative(5.59f, -5.59f)
            lineTo(12f, 4f)
            lineToRelative(-8f, 8f)
            lineToRelative(8f, 8f)
            lineToRelative(1.41f, -1.41f)
            lineTo(7.83f, 13f)
            horizontalLineTo(20f)
            verticalLineToRelative(-2f)
            close()
        }.build()

    val ArrowForward: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowForward",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 4f)
            lineToRelative(-1.41f, 1.41f)
            lineTo(16.17f, 11f)
            horizontalLineTo(4f)
            verticalLineToRelative(2f)
            horizontalLineTo(16.17f)
            lineToRelative(-5.58f, 5.59f)
            lineTo(12f, 20f)
            lineToRelative(8f, -8f)
            lineToRelative(-8f, -8f)
            close()
        }.build()
}
