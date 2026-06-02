package io.github.adaptivekt.site

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal object DocsIcons {

    val Copy: ImageVector
        @Composable
        get() = remember {
            ImageVector.Builder(
                name = "Copy",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(8f, 8f)
                    lineTo(16f, 8f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18f, 10f)
                    lineTo(18f, 18f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 20f)
                    lineTo(8f, 20f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 18f)
                    lineTo(6f, 10f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 8f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(16f, 8f)
                    lineTo(16f, 6f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14f, 4f)
                    lineTo(6f, 4f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4f, 6f)
                    lineTo(4f, 14f)
                    arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6f, 16f)
                    lineTo(8f, 16f)
                }
            }.build()
        }

    val Check: ImageVector
        @Composable
        get() = remember {
            ImageVector.Builder(
                name = "Check",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(20f, 6f)
                    lineTo(9f, 17f)
                    lineTo(4f, 12f)
                }
            }.build()
        }

    val GitHub: ImageVector
        @Composable
        get() = remember {
            ImageVector.Builder(
                name = "GitHub",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeLineWidth = 0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                ) {
                    moveTo(12f, 2f)
                    arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.84f, 21.5f)
                    curveTo(9.34f, 21.59f, 9.5f, 21.28f, 9.5f, 21.03f)
                    curveTo(9.5f, 20.8f, 9.49f, 20.08f, 9.49f, 19.33f)
                    curveTo(6.71f, 19.93f, 6.13f, 18.2f, 6.13f, 18.2f)
                    curveTo(5.67f, 17.05f, 5f, 16.74f, 5f, 16.74f)
                    curveTo(4.08f, 16.11f, 5.07f, 16.12f, 5.07f, 16.12f)
                    curveTo(6.09f, 16.19f, 6.62f, 17.13f, 6.62f, 17.13f)
                    curveTo(7.53f, 18.68f, 8.98f, 18.24f, 9.55f, 17.98f)
                    curveTo(9.64f, 17.33f, 9.9f, 16.89f, 10.19f, 16.63f)
                    curveTo(7.97f, 16.38f, 5.64f, 15.52f, 5.64f, 11.69f)
                    curveTo(5.64f, 10.59f, 6.03f, 9.7f, 6.67f, 9f)
                    curveTo(6.56f, 8.75f, 6.22f, 7.72f, 6.77f, 6.3f)
                    curveTo(6.77f, 6.3f, 7.6f, 6.04f, 9.49f, 7.32f)
                    arcTo(10.73f, 10.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 7.15f)
                    arcTo(10.73f, 10.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14.51f, 7.32f)
                    curveTo(16.4f, 6.04f, 17.23f, 6.3f, 17.23f, 6.3f)
                    curveTo(17.78f, 7.72f, 17.44f, 8.75f, 17.34f, 9f)
                    curveTo(17.98f, 9.7f, 18.36f, 10.59f, 18.36f, 11.69f)
                    curveTo(18.36f, 15.53f, 16.03f, 16.38f, 13.8f, 16.63f)
                    curveTo(14.16f, 16.94f, 14.49f, 17.56f, 14.49f, 18.52f)
                    curveTo(14.49f, 19.89f, 14.48f, 21.01f, 14.48f, 21.03f)
                    curveTo(14.48f, 21.28f, 14.65f, 21.6f, 15.17f, 21.5f)
                    arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 2f)
                    close()
                }
            }.build()
        }

    val Sun: ImageVector
        @Composable
        get() = remember {
            ImageVector.Builder(
                name = "Sun",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(12f, 12f)
                    moveToRelative(-4f, 0f)
                    arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 16f, 12f)
                    arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 8f, 12f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(12f, 2f)
                    lineTo(12f, 4f)
                    moveTo(12f, 20f)
                    lineTo(12f, 22f)
                    moveTo(4.93f, 4.93f)
                    lineTo(6.34f, 6.34f)
                    moveTo(17.66f, 17.66f)
                    lineTo(19.07f, 19.07f)
                    moveTo(2f, 12f)
                    lineTo(4f, 12f)
                    moveTo(20f, 12f)
                    lineTo(22f, 12f)
                    moveTo(4.93f, 19.07f)
                    lineTo(6.34f, 17.66f)
                    moveTo(17.66f, 6.34f)
                    lineTo(19.07f, 4.93f)
                }
            }.build()
        }

    val Moon: ImageVector
        @Composable
        get() = remember {
            ImageVector.Builder(
                name = "Moon",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(21f, 12.79f)
                    arcTo(9f, 9f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.21f, 3f)
                    arcTo(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = false, 21f, 12.79f)
                    close()
                }
            }.build()
        }
}
