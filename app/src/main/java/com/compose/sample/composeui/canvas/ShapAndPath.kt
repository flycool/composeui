package com.compose.sample.composeui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ShapeAndPath() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
            .padding(10.dp),
    ) {
        drawRect(color = Color.Blue)
        drawCircle(color = Color.Yellow, radius = 400f)

        val path = Path().apply {
            moveTo(600f, 1200f)
            lineTo(800f, 800f)
            lineTo(350f, 400f)
            lineTo(160f, 25f)
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                listOf(Color.Blue, Color.Green)
            )
        )
    }
}