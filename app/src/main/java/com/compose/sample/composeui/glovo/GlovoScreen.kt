package com.compose.sample.composeui.glovo

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


data class GlovoItem(
    val title: String,
    val path: Path
)

@Composable
fun GlovoScreen(
    mainItem: GlovoItem,
    modifier: Modifier = Modifier,
    iconScale: Float = 3f,
    items: List<GlovoItem> = emptyList(),
    mainCircleRadius: Dp = 140.dp,
    innerCircleRadius: Dp = 50.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onGoalClick: (GlovoItem) -> Unit,
) {

    val glovoUiItems = remember { mutableStateMapOf<Offset, GlovoItem>() }
    val animateFloat = remember { Animatable(0f) }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }


    var angle by remember { mutableFloatStateOf(0f) }
    var dragStartedAngle by remember { mutableFloatStateOf(0f) }
    var oldAngle by remember { mutableFloatStateOf(angle) }

    LaunchedEffect(key1 = items) {
        animateFloat.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    }

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier
        .pointerInput(true) {
            detectTapGestures { clickOffset ->
                glovoUiItems.forEach { item ->
                    val rect = Rect(item.key, innerCircleRadius.toPx())
                    if (rect.contains(clickOffset)) {
                        onGoalClick(item.value)
                    }
                }
            }
        }
        .pointerInput(true) {
            detectDragGestures(
                onDragStart = { offset ->
                    dragStartedAngle = -atan2(
                        circleCenter.x - offset.x,
                        circleCenter.y - offset.y
                    ) * (180f / PI.toFloat())
                    dragStartedAngle = dragStartedAngle.mod(360f)
                },
                onDragEnd = {
                    oldAngle = angle
                }
            ) { change, _ ->
                var touchAngle = -atan2(
                    circleCenter.x - change.position.x,
                    circleCenter.y - change.position.y,
                ) * (180f / PI.toFloat())
                touchAngle = touchAngle.mod(360f)

                val changeAngle = touchAngle - dragStartedAngle
                angle = (oldAngle + changeAngle).mod(360f)
            }
        }
    ) {
        glovoUiItems.clear()

        circleCenter = Offset(center.x, center.y)

        val distance = 360f / items.size
        val mainCircleOffset = Offset(circleCenter.x, circleCenter.y)
        glovoUiItems[mainCircleOffset] = mainItem

        drawCircleInfo(
            item = mainItem,
            circleRadius = innerCircleRadius,
            iconScale = iconScale,
            textStyle = textStyle,
            textMeasurer = textMeasurer,
            animationValue = animateFloat.value,
            currentOffset = mainCircleOffset
        )

        items.forEachIndexed { i, item ->
            val angleInDegrees = (i * distance + angle - 90)
            val angleInRad = angleInDegrees * (PI / 180).toFloat()

            val currentOffset = Offset(
                x = mainCircleRadius.toPx() * cos(angleInRad) + circleCenter.x,
                y = mainCircleRadius.toPx() * sin(angleInRad) + circleCenter.y
            )

            glovoUiItems[currentOffset] = item

            drawCircleInfo(
                item = item,
                circleRadius = innerCircleRadius,
                iconScale = iconScale,
                textStyle = textStyle,
                textMeasurer = textMeasurer,
                animationValue = animateFloat.value,
                currentOffset = currentOffset
            )
        }

    }
}

fun DrawScope.drawCircleInfo(
    item: GlovoItem,
    circleRadius: Dp,
    iconScale: Float,
    textStyle: TextStyle,
    textMeasurer: TextMeasurer,
    animationValue: Float,
    currentOffset: Offset,
) {
    drawCircle(
        color = Color.White,
        radius = circleRadius.toPx() * animationValue,
        center = currentOffset
    )

    val pathBounds = item.path.getBounds()

    translate(
        left = currentOffset.x - (pathBounds.right * iconScale) / 2,
        top = currentOffset.y - pathBounds.bottom - 15.dp.toPx()
    ) {
        scale(
            scale = iconScale * animationValue,
            pivot = pathBounds.topLeft
        ) {
            drawPath(
                path = item.path,
                color = Color.Black
            )
        }
    }

    val measurementResult = textMeasurer.measure(
        text = item.title,
        constraints = Constraints(
            maxWidth = (circleRadius.toPx() * 2 - 16.dp.toPx()).toInt()
        ),
        style = textStyle.copy(
            textAlign = TextAlign.Center,
            fontSize = textStyle.fontSize * animationValue
        )
    )

    drawText(
        textLayoutResult = measurementResult,
        topLeft = Offset(
            x = currentOffset.x - measurementResult.size.width / 2,
            y = currentOffset.y + pathBounds.height
        )
    )
}

@Composable
fun GlovoScreen() {
    val context = LocalContext.current
    val defaultPath =
        PathParser().parsePathString(context.getString(R.string.default_path)).toPath()

    val mainItem = GlovoItem("main", defaultPath)
    GlovoScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        mainItem = mainItem,
        items = listOf(
            GlovoItem("secondary 1", defaultPath),
            GlovoItem("secondary 2", defaultPath),
            GlovoItem("secondary 3", defaultPath),
            GlovoItem("secondary 4", defaultPath),
            GlovoItem("secondary 5", defaultPath),
        )
    ) {
        Toast.makeText(context, it.title, Toast.LENGTH_SHORT).show()
    }
}

@Preview
@Composable
private fun GlovoPreivew() {
    val context = LocalContext.current
    val defaultPath =
        PathParser().parsePathString(context.getString(R.string.default_path)).toPath()

    val mainItem = GlovoItem("main", defaultPath)
    GlovoScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        mainItem = mainItem,
        items = listOf(
            GlovoItem("secondary 1", defaultPath),
            GlovoItem("secondary 2", defaultPath),
            GlovoItem("secondary 3", defaultPath),
            GlovoItem("secondary 4", defaultPath),
            GlovoItem("secondary 5", defaultPath),
        )
    ) {
        println("glovo item: ${it.title}")
    }
}





























