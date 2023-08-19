package com.compose.sample.composeui.indicator

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.ui.theme.Purple40
import com.compose.sample.composeui.ui.theme.Purple80
import com.compose.sample.composeui.ui.theme.PurpleGrey40

@Composable
fun AnimatedCircularProgressIndicator(
    currentValue: Int,
    maxValue: Int,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    completedColor: Color,
    modifier: Modifier = Modifier
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ProgressStatus(
            currentValue = currentValue,
            maxValue = maxValue,
            backgroundColor = progressBackgroundColor,
            indicatorColor = progressIndicatorColor,
            completedColor = completedColor
        )

        val animateFloat = remember {
            Animatable(0f)
        }
        LaunchedEffect(animateFloat) {
            animateFloat.animateTo(
                targetValue = currentValue / maxValue.toFloat(),
                animationSpec = tween(durationMillis = 2000, easing = FastOutLinearInEasing)
            )
        }

        Canvas(
            modifier = Modifier
                .progressSemantics(currentValue / maxValue.toFloat())
                .size(circularIndicatorDiameter)
        ) {
            val startAngle = 270f
            val sweep = animateFloat.value * 360f
            val diameterOffset = stroke.width / 2

            drawCircle(
                color = progressBackgroundColor,
                style = stroke,
                radius = size.minDimension / 2.0f - diameterOffset
            )
            drawCircularProgressIndicator(startAngle, sweep, progressIndicatorColor, stroke)

            if (currentValue == maxValue) {
                drawCircle(
                    color = completedColor,
                    style = stroke,
                    radius = size.minDimension / 2.0f - diameterOffset
                )
            }
        }
    }
}

@Composable
private fun ProgressStatus(
    currentValue: Int,
    maxValue: Int,
    backgroundColor: Color,
    indicatorColor: Color,
    completedColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            val emphasisSpan = MaterialTheme.typography.titleLarge.copy(
                color = if (currentValue == maxValue) completedColor else indicatorColor
            ).toSpanStyle()
            val defaultSpan =
                MaterialTheme.typography.bodyMedium.copy(color = backgroundColor).toSpanStyle()
            append(AnnotatedString("$currentValue", spanStyle = emphasisSpan))
            append(AnnotatedString("/", spanStyle = defaultSpan))
            append(AnnotatedString("$maxValue", spanStyle = defaultSpan))
        },
    )
}

private fun DrawScope.drawCircularProgressIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

private val circularIndicatorDiameter = 84.dp


@Preview
@Composable
fun AnimatedCircularProgressIndicatorPreview() {
    AnimatedCircularProgressIndicator(
        currentValue = 15,
        maxValue = 20,
        progressBackgroundColor = Purple80,
        progressIndicatorColor = PurpleGrey40,
        completedColor = Purple40
    )
}




























