package com.compose.sample.composeui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Chart() {
    val data = listOf(
        DataPoint(20f, Color.LightGray),
        DataPoint(45f, Color.Blue),
        DataPoint(130f, Color.DarkGray),
        DataPoint(80f, Color.LightGray),
        DataPoint(65f, Color.Cyan)
    )
    BarChart(data = data)
}

@Composable
private fun BarChart(data: List<DataPoint>) {
    val maxBarValue = data.maxOf { it.value }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.White,
                        Color.Red,
                        Color.Yellow
                    )
                )
            )
            .padding(20.dp)
    ) {

        val maxBarHeight = size.height
        val barWidth = size.width / data.size

        data.forEachIndexed { index, dataPoint ->
            val barHeight = (dataPoint.value / maxBarValue) * maxBarHeight
            drawRect(
                color = dataPoint.color,
                topLeft = Offset(index * barWidth, size.height - barHeight),
                size = Size(barWidth, barHeight)
            )
        }
    }

}

data class DataPoint(val value: Float, val color: Color)