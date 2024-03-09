package com.compose.sample.composeui.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R
import com.compose.sample.composeui.ui.theme.ComposeuiTheme
import com.compose.sample.composeui.ui.theme.Coral
import com.compose.sample.composeui.ui.theme.LightBlue
import com.compose.sample.composeui.ui.theme.Purple

@Composable
fun TimelineNode(
    position: TimelineNodePosition,
    circleParameters: CircleParameters,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 16.dp,
    spacer: Dp = 32.dp,
    content: @Composable BoxScope.(Modifier) -> Unit
) {
    val iconPainter = circleParameters.icon?.let { painterResource(id = it) }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()
                drawCircle(
                    color = circleParameters.backgroundColor,
                    radius = circleRadiusInPx,
                    center = Offset(circleRadiusInPx, circleRadiusInPx)
                )
                lineParameters?.let {
                    drawLine(
                        brush = lineParameters.brush,
                        start = Offset(circleRadiusInPx, circleRadiusInPx * 2),
                        end = Offset(circleRadiusInPx, this.size.height),
                        strokeWidth = lineParameters.strokeWidth.toPx()
                    )
                }
                circleParameters.stroke?.let { stroke ->
                    val widthPx = stroke.width.toPx()
                    drawCircle(
                        color = stroke.color,
                        radius = circleRadiusInPx - widthPx / 2,
                        center = Offset(x = circleRadiusInPx, y = circleRadiusInPx),
                        style = Stroke(width = widthPx)
                    )
                }
                iconPainter?.let { painter ->
                    this.withTransform(
                        transformBlock = {
                            translate(
                                left = circleRadiusInPx - painter.intrinsicSize.width / 2f,
                                top = circleRadiusInPx - painter.intrinsicSize.height / 2f
                            )
                        },
                        drawBlock = {
                            this.drawIntoCanvas {
                                with(painter) {
                                    draw(intrinsicSize)
                                }
                            }
                        })
                }
            }

    ) {
        content(
            Modifier
                .defaultMinSize(minHeight = circleParameters.radius * 2)
                .padding(
                    start = circleParameters.radius * 2 + contentStartOffset,
                    bottom = if (position != TimelineNodePosition.LAST) {
                        spacer
                    } else 0.dp
                )
        )
    }
}

@Composable
private fun MessageBubble(
    modifier: Modifier,
    containerColor: Color,
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {}
}


@Preview(showBackground = true)
@Composable
private fun TimelinePreview() {
    ComposeuiTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TimelineNode(
                position = TimelineNodePosition.FIRST,
                circleParameters = CircleParameters(10.dp, LightBlue),
                lineParameters = LineParametersDefault.linearGradient(
                    startColor = LightBlue,
                    endColor = Purple
                ),
            ) {
                MessageBubble(modifier = it, containerColor = LightBlue)
            }
            TimelineNode(
                position = TimelineNodePosition.MIDDLE,
                circleParameters = CircleParameters(10.dp, Purple),
                lineParameters = LineParametersDefault.linearGradient(
                    startColor = Purple,
                    endColor = Coral
                ),
            ) { MessageBubble(modifier = it, containerColor = Purple) }
            TimelineNode(
                position = TimelineNodePosition.LAST,
                circleParameters = CircleParameters(10.dp, Coral,
                    icon = R.drawable.ic_bubble_warning),
            ) { MessageBubble(modifier = it, containerColor = Coral) }
        }
    }
}
































