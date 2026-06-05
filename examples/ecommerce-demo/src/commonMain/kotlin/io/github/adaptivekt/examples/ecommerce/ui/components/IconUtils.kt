package io.github.adaptivekt.examples.ecommerce.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon

object AppIcons {
    val Home = ImageVector.Builder(name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(3f, 9f); lineTo(12f, 2f); lineTo(21f, 9f); verticalLineTo(20f); curveTo(21f, 20.53f, 20.79f, 21.04f, 20.41f, 21.41f); curveTo(20.04f, 21.79f, 19.53f, 22f, 19f, 22f); horizontalLineTo(5f); curveTo(4.47f, 22f, 3.96f, 21.79f, 3.59f, 21.41f); curveTo(3.21f, 21.04f, 3f, 20.53f, 3f, 20f); verticalLineTo(9f); close()
        moveTo(9f, 22f); verticalLineTo(12f); horizontalLineTo(15f); verticalLineTo(22f)
    }.build()

    val ShoppingBag = ImageVector.Builder(name = "ShoppingBag", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(6f, 2f); lineTo(3f, 6f); verticalLineTo(20f); curveTo(3f, 20.53f, 3.21f, 21.04f, 3.59f, 21.41f); curveTo(3.96f, 21.79f, 4.47f, 22f, 5f, 22f); horizontalLineTo(19f); curveTo(19.53f, 22f, 20.04f, 21.79f, 20.41f, 21.41f); curveTo(20.79f, 21.04f, 21f, 20.53f, 21f, 20f); verticalLineTo(6f); lineTo(18f, 2f); horizontalLineTo(6f); close()
        moveTo(3f, 6f); horizontalLineTo(21f); moveTo(16f, 10f); curveTo(16f, 11.06f, 15.58f, 12.08f, 14.83f, 12.83f); curveTo(14.08f, 13.58f, 13.06f, 14f, 12f, 14f); curveTo(10.94f, 14f, 9.92f, 13.58f, 9.17f, 12.83f); curveTo(8.42f, 12.08f, 8f, 11.06f, 8f, 10f)
    }.build()

    val User = ImageVector.Builder(name = "User", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(20f, 21f); verticalLineTo(19f); curveTo(20f, 17.94f, 19.58f, 16.92f, 18.83f, 16.17f); curveTo(18.08f, 15.42f, 17.06f, 15f, 16f, 15f); horizontalLineTo(8f); curveTo(6.94f, 15f, 5.92f, 15.42f, 5.17f, 16.17f); curveTo(4.42f, 16.92f, 4f, 17.94f, 4f, 19f); verticalLineTo(21f); moveTo(12f, 11f); curveTo(14.21f, 11f, 16f, 9.21f, 16f, 7f); curveTo(16f, 4.79f, 14.21f, 3f, 12f, 3f); curveTo(9.79f, 3f, 8f, 4.79f, 8f, 7f); curveTo(8f, 9.21f, 9.79f, 11f, 12f, 11f); close()
    }.build()

    val Search = ImageVector.Builder(name = "Search", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(11f, 19f); curveTo(15.42f, 19f, 19f, 15.42f, 19f, 11f); curveTo(19f, 6.58f, 15.42f, 3f, 11f, 3f); curveTo(6.58f, 3f, 3f, 6.58f, 3f, 11f); curveTo(3f, 15.42f, 6.58f, 19f, 11f, 19f); close(); moveTo(21f, 21f); lineTo(16.65f, 16.65f)
    }.build()
    
    val Heart = ImageVector.Builder(name = "Heart", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(20.84f, 4.61f); curveTo(20.33f, 4.09f, 19.73f, 3.69f, 19.06f, 3.4f); curveTo(18.39f, 3.12f, 17.67f, 2.97f, 16.95f, 2.97f); curveTo(16.22f, 2.97f, 15.51f, 3.12f, 14.84f, 3.4f); curveTo(14.16f, 3.69f, 13.56f, 4.09f, 13.05f, 4.61f); lineTo(12f, 5.67f); lineTo(10.95f, 4.61f); curveTo(9.93f, 3.59f, 8.54f, 3.01f, 7.1f, 3.01f); curveTo(5.65f, 3.01f, 4.26f, 3.59f, 3.24f, 4.61f); curveTo(2.22f, 5.63f, 1.64f, 7.02f, 1.64f, 8.47f); curveTo(1.64f, 9.91f, 2.22f, 11.31f, 3.24f, 12.33f); lineTo(12f, 21.35f); lineTo(20.76f, 12.33f); curveTo(21.27f, 11.82f, 21.68f, 11.22f, 21.96f, 10.55f); curveTo(22.24f, 9.87f, 22.39f, 9.16f, 22.39f, 8.44f); curveTo(22.39f, 7.72f, 22.24f, 7f, 21.96f, 6.33f); curveTo(21.68f, 5.65f, 21.27f, 5.05f, 20.76f, 4.54f); lineTo(20.84f, 4.61f); close()
    }.build()

    val Package = ImageVector.Builder(name = "Package", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(16.5f, 9.4f); lineTo(7.5f, 4.21f); moveTo(21f, 16f); verticalLineTo(8f); curveTo(20.99f, 7.64f, 20.91f, 7.29f, 20.74f, 6.97f); curveTo(20.57f, 6.66f, 20.33f, 6.39f, 20.04f, 6.18f); lineTo(13.04f, 2.18f); curveTo(12.72f, 2.06f, 12.36f, 2f, 12f, 2f); curveTo(11.64f, 2f, 11.28f, 2.06f, 10.96f, 2.18f); lineTo(3.96f, 6.18f); curveTo(3.67f, 6.39f, 3.43f, 6.66f, 3.26f, 6.97f); curveTo(3.09f, 7.29f, 3.01f, 7.64f, 3f, 8f); verticalLineTo(16f); curveTo(3.01f, 16.36f, 3.09f, 16.71f, 3.26f, 17.03f); curveTo(3.43f, 17.34f, 3.67f, 17.61f, 3.96f, 17.82f); lineTo(10.96f, 21.82f); curveTo(11.28f, 21.94f, 11.64f, 22f, 12f, 22f); curveTo(12.36f, 22f, 12.72f, 21.94f, 13.04f, 21.82f); lineTo(20.04f, 17.82f); curveTo(20.33f, 17.61f, 20.57f, 17.34f, 20.74f, 17.03f); curveTo(20.91f, 16.71f, 20.99f, 16.36f, 21f, 16f); close(); moveTo(3.27f, 6.96f); lineTo(12f, 12.01f); lineTo(20.73f, 6.96f); moveTo(12f, 22.08f); verticalLineTo(12f)
    }.build()

    val Settings = ImageVector.Builder(name = "Settings", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(12.22f, 2f); horizontalLineTo(11.78f); curveTo(11.25f, 2f, 10.82f, 2.43f, 10.82f, 2.96f); verticalLineTo(4.34f); curveTo(10.82f, 4.41f, 10.79f, 4.47f, 10.74f, 4.52f); lineTo(9.71f, 5.12f); curveTo(9.66f, 5.15f, 9.6f, 5.16f, 9.54f, 5.14f); lineTo(8.24f, 4.63f); curveTo(7.74f, 4.43f, 7.18f, 4.66f, 6.96f, 5.16f); lineTo(6.46f, 6.03f); curveTo(6.21f, 6.51f, 6.35f, 7.1f, 6.78f, 7.42f); lineTo(7.89f, 8.23f); curveTo(7.94f, 8.27f, 7.97f, 8.33f, 7.97f, 8.4f); verticalLineTo(9.6f); curveTo(7.97f, 9.67f, 7.94f, 9.73f, 7.89f, 9.77f); lineTo(6.78f, 10.58f); curveTo(6.35f, 10.9f, 6.21f, 11.49f, 6.46f, 11.97f); lineTo(6.96f, 12.84f); curveTo(7.18f, 13.34f, 7.74f, 13.57f, 8.24f, 13.37f); lineTo(9.54f, 12.86f); curveTo(9.6f, 12.84f, 9.66f, 12.85f, 9.71f, 12.88f); lineTo(10.74f, 13.48f); curveTo(10.79f, 13.53f, 10.82f, 13.59f, 10.82f, 13.66f); verticalLineTo(15.04f); curveTo(10.82f, 15.57f, 11.25f, 16f, 11.78f, 16f); horizontalLineTo(12.22f); curveTo(12.75f, 16f, 13.18f, 15.57f, 13.18f, 15.04f); verticalLineTo(13.66f); curveTo(13.18f, 13.59f, 13.21f, 13.53f, 13.26f, 13.48f); lineTo(14.29f, 12.88f); curveTo(14.34f, 12.85f, 14.4f, 12.84f, 14.46f, 12.86f); lineTo(15.76f, 13.37f); curveTo(16.26f, 13.57f, 16.82f, 13.34f, 17.04f, 12.84f); lineTo(17.54f, 11.97f); curveTo(17.79f, 11.49f, 17.65f, 10.9f, 17.22f, 10.58f); lineTo(16.11f, 9.77f); curveTo(16.06f, 9.73f, 16.03f, 9.67f, 16.03f, 9.6f); verticalLineTo(8.4f); curveTo(16.03f, 8.33f, 16.06f, 8.27f, 16.11f, 8.23f); lineTo(17.22f, 7.42f); curveTo(17.65f, 7.1f, 17.79f, 6.51f, 17.54f, 6.03f); lineTo(17.04f, 5.16f); curveTo(16.82f, 4.66f, 16.26f, 4.43f, 15.76f, 4.63f); lineTo(14.46f, 5.14f); curveTo(14.4f, 5.16f, 14.34f, 5.15f, 14.29f, 5.12f); lineTo(13.26f, 4.52f); curveTo(13.21f, 4.47f, 13.18f, 4.41f, 13.18f, 4.34f); verticalLineTo(2.96f); curveTo(13.18f, 2.43f, 12.75f, 2f, 12.22f, 2f); close(); moveTo(12f, 11f); curveTo(10.34f, 11f, 9f, 9.66f, 9f, 8f); curveTo(9f, 6.34f, 10.34f, 5f, 12f, 5f); curveTo(13.66f, 5f, 15f, 6.34f, 15f, 8f); curveTo(15f, 9.66f, 13.66f, 11f, 12f, 11f); close()
    }.build()

    val ChevronLeft = ImageVector.Builder(name = "ChevronLeft", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(15f, 18f); lineTo(9f, 12f); lineTo(15f, 6f)
    }.build()

    val ChevronRight = ImageVector.Builder(name = "ChevronRight", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(9f, 18f); lineTo(15f, 12f); lineTo(9f, 6f)
    }.build()

    val Star = ImageVector.Builder(name = "Star", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(12f, 2f); lineTo(15.09f, 8.26f); lineTo(22f, 9.27f); lineTo(17f, 14.14f); lineTo(18.18f, 21.02f); lineTo(12f, 17.77f); lineTo(5.82f, 21.02f); lineTo(7f, 14.14f); lineTo(2f, 9.27f); lineTo(8.91f, 8.26f); lineTo(12f, 2f); close()
    }.build()

    val Plus = ImageVector.Builder(name = "Plus", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(12f, 5f); verticalLineTo(19f); moveTo(5f, 12f); horizontalLineTo(19f)
    }.build()

    val Minus = ImageVector.Builder(name = "Minus", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(5f, 12f); horizontalLineTo(19f)
    }.build()

    val Trash = ImageVector.Builder(name = "Trash", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(3f, 6f); horizontalLineTo(21f); moveTo(19f, 6f); verticalLineTo(20f); curveTo(19f, 20.53f, 18.79f, 21.04f, 18.41f, 21.41f); curveTo(18.04f, 21.79f, 17.53f, 22f, 17f, 22f); horizontalLineTo(7f); curveTo(6.47f, 22f, 5.96f, 21.79f, 5.59f, 21.41f); curveTo(5.21f, 21.04f, 5f, 20.53f, 5f, 20f); verticalLineTo(6f); moveTo(8f, 6f); verticalLineTo(4f); curveTo(8f, 3.47f, 8.21f, 2.96f, 8.59f, 2.59f); curveTo(8.96f, 2.21f, 9.47f, 2f, 10f, 2f); horizontalLineTo(14f); curveTo(14.53f, 2f, 15.04f, 2.21f, 15.41f, 2.59f); curveTo(15.79f, 2.96f, 16f, 3.47f, 16f, 4f); verticalLineTo(6f)
    }.build()

    val Check = ImageVector.Builder(name = "Check", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(20f, 6f); lineTo(9f, 17f); lineTo(4f, 12f)
    }.build()

    val Truck = ImageVector.Builder(name = "Truck", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(1f, 3f); horizontalLineTo(15f); verticalLineTo(16f); horizontalLineTo(1f); close()
        moveTo(15f, 8f); horizontalLineTo(20f); lineTo(23f, 11f); verticalLineTo(16f); horizontalLineTo(15f)
        moveTo(5.5f, 21f); curveTo(6.88f, 21f, 8f, 19.88f, 8f, 18.5f); curveTo(8f, 17.12f, 6.88f, 16f, 5.5f, 16f); curveTo(4.12f, 16f, 3f, 17.12f, 3f, 18.5f); curveTo(3f, 19.88f, 4.12f, 21f, 5.5f, 21f); close()
        moveTo(18.5f, 21f); curveTo(19.88f, 21f, 21f, 19.88f, 21f, 18.5f); curveTo(21f, 17.12f, 19.88f, 16f, 18.5f, 16f); curveTo(17.12f, 16f, 16f, 17.12f, 16f, 18.5f); curveTo(16f, 19.88f, 17.12f, 21f, 18.5f, 21f); close()
    }.build()

    val Shield = ImageVector.Builder(name = "Shield", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(12f, 22f); curveTo(12f, 22f, 20f, 18f, 20f, 12f); verticalLineTo(5f); lineTo(12f, 2f); lineTo(4f, 5f); verticalLineTo(12f); curveTo(4f, 18f, 12f, 22f, 12f, 22f); close()
    }.build()

    val Menu = ImageVector.Builder(name = "Menu", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(4f, 12f); horizontalLineTo(20f); moveTo(4f, 6f); horizontalLineTo(20f); moveTo(4f, 18f); horizontalLineTo(20f)
    }.build()

    val X = ImageVector.Builder(name = "X", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(18f, 6f); lineTo(6f, 18f); moveTo(6f, 6f); lineTo(18f, 18f)
    }.build()

    val Filter = ImageVector.Builder(name = "Filter", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f) {
        moveTo(22f, 3f); horizontalLineTo(2f); lineTo(10f, 12.46f); verticalLineTo(19f); lineTo(14f, 21f); verticalLineTo(12.46f); lineTo(22f, 3f); close()
    }.build()
}

@Composable
fun AppIcon(
    imageVector: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(24.dp),
        tint = if (tint == Color.Unspecified) Color.Black else tint
    )
}
