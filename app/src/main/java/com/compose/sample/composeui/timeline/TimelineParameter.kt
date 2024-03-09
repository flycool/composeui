package com.compose.sample.composeui.timeline

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class TimelineNodePosition {
    FIRST, MIDDLE, LAST
}

data class CircleParameters(
    val radius: Dp,
    val backgroundColor: Color,
    val stroke: StrokeParameters? = null,
    @DrawableRes val icon: Int? = null
)

data class LineParameters(
    val strokeWidth: Dp,
    val brush: Brush
)

data class StrokeParameters(
    val color: Color,
    val width: Dp
)

object CircleParametersDefault {
    private val defaultCircleRadius = 12.dp

    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Color.Cyan,
        stroke: StrokeParameters? = null,
        @DrawableRes icon: Int? = null
    ) = CircleParameters(radius, backgroundColor, stroke, icon)
}

object LineParametersDefault {
    private val defaultStrokeWidth = 3.dp

    fun linearGradient(
        strokeWidth: Dp = defaultStrokeWidth,
        startColor: Color,
        endColor: Color,
        startY: Float = 0.0f,
        endY: Float = Float.POSITIVE_INFINITY
    ): LineParameters {
        val brush = Brush.verticalGradient(
            colors = listOf(startColor, endColor),
            startY = startY,
            endY = endY
        )
        return LineParameters(strokeWidth, brush)
    }
}