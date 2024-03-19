package com.compose.sample.composeui.statdium

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun FootballGround(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val lightStadiumGreen = Color(0xFF4CBB17)
        val darkStadiumGreen = Color(0xFF3A9D23)
        val screenHeight = size.height
        val rectHeight = screenHeight / 10

        for (i in 0 until 10) {
            val color = if (i % 2 == 0) lightStadiumGreen else darkStadiumGreen
            drawRect(
                color = color,
                topLeft = Offset(0f, i * rectHeight),
                size = Size(size.width, rectHeight)
            )
        }

        val pithOutLine = AndroidPath().apply {
            moveTo(50f, 50f)
            lineTo(size.width - 50f, 50f)
            lineTo(size.width - 50f, size.height - 50f)
            lineTo(50f, size.height - 50f)
            close()

            moveTo(50f, size.height / 2)
            lineTo(size.width - 50f, size.height / 2)
        }
        drawPath(
            path = pithOutLine,
            color = Color.White,
            style = Stroke(3.dp.toPx())
        )

        drawCircle(
            color = Color.White,
            radius = 10f,
            center = Offset(size.width / 2, size.height / 2)
        )

        drawCircle(
            color = Color.White,
            radius = 100f,
            center = Offset(size.width / 2, size.height / 2),
            style = Stroke(3.dp.toPx())
        )

        drawArc(
            color = Color.White,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            size = Size(100f, 100f),
            topLeft = Offset(0f, 0f),
            style = Stroke(3.dp.toPx())
        )

        drawArc(
            color = Color.White,
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            size = Size(100f, 100f),
            topLeft = Offset(size.width - 100f, 0f),
            style = Stroke(3.dp.toPx())
        )

        drawArc(
            color = Color.White,
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            size = Size(100f, 100f),
            topLeft = Offset(size.width - 100f, size.height - 100f),
            style = Stroke(3.dp.toPx())
        )

        drawArc(
            color = Color.White,
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            size = Size(100f, 100f),
            topLeft = Offset(0f, size.height - 100f),
            style = Stroke(3.dp.toPx())
        )

        drawRect(
            color = Color.White,
            topLeft = Offset(size.width.div(2).minus(100f), 50f),
            size = Size(200f, 200f),
            style = Stroke(3.dp.toPx())
        )

        drawRect(
            color = Color.White,
            topLeft = Offset(size.width.div(2).minus(300), 50f),
            size = Size(600f, 300f),
            style = Stroke(3.dp.toPx())
        )

        drawRect(
            color = Color.White,
            topLeft = Offset(size.width.div(2).minus(100f), size.height - 50),
            size = Size(200f, -200f),
            style = Stroke(3.dp.toPx())
        )

        drawRect(
            color = Color.White,
            topLeft = Offset(size.width.div(2).minus(300f), size.height - 50),
            size = Size(600f, -300f),
            style = Stroke(3.dp.toPx())
        )

        drawCircle(
            color = Color.White,
            radius = 10f,
            center = Offset(size.width.div(2), 200f)
        )
        drawCircle(
            color = Color.White,
            radius = 10f,
            center = Offset(size.width.div(2), size.height - 200f)
        )

        drawArc(
            color = Color.White,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            size = Size(200f, 200f),
            topLeft = Offset(size.width.div(2).minus(100f), 250f),
            style = Stroke(3.dp.toPx())
        )

        drawArc(
            color = Color.White,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            size = Size(200f, 200f),
            topLeft = Offset(size.width.div(2).minus(100f), size.height.minus(450f)),
            style = Stroke(3.dp.toPx())
        )

    }
}




























