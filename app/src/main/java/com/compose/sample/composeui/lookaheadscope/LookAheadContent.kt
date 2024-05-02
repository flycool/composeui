package com.compose.sample.composeui.lookaheadscope

import androidx.compose.animation.core.DeferredTargetAnimation
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.approachLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round

@Composable
fun LookAheadContent(
    modifier: Modifier = Modifier,
) {
    val colors = listOf(
        Color(0xffff6f69),
        Color(0xffffcc5c),
        Color(0xff264653),
        Color(0xFF679138),
    )

    var isInColumn by remember { mutableStateOf(true) }
    LookaheadScope {
        val items = remember {
            movableContentWithReceiverOf<LookaheadScope> {
                colors.forEach { color ->
                    Box(
                        modifier = modifier
                            .padding(8.dp)
                            .size(if (isInColumn) 80.dp else 20.dp)
                            .animateBounds()
                            .background(color = color, shape = RoundedCornerShape(10))
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { isInColumn = !isInColumn },
            contentAlignment = Alignment.Center
        ) {
            if (isInColumn) {
                Column { items() }
            } else {
                Row { items() }
            }
        }
    }
}

context (LookaheadScope)
@OptIn(ExperimentalAnimatableApi::class, ExperimentalComposeUiApi::class)
fun Modifier.animateBounds(): Modifier = composed {
    val offsetAnim = remember { DeferredTargetAnimation(IntOffset.VectorConverter) }
    val sizeAnim = remember { DeferredTargetAnimation(IntSize.VectorConverter)}
    val scope = rememberCoroutineScope()
    this.approachLayout(
        isMeasurementApproachInProgress = {
            sizeAnim.updateTarget(it, scope, tween(2000))
            sizeAnim.isIdle
        },
        isPlacementApproachInProgress = {
            val target = lookaheadScopeCoordinates.localLookaheadPositionOf(it).round()
            offsetAnim.updateTarget(target, scope, tween(durationMillis = 2000))
            offsetAnim.isIdle
        },
    ) { measurable, constraints ->
        val (animWidth, animHeight) = sizeAnim.updateTarget(lookaheadSize, scope)
        measurable.measure(Constraints.fixed(animWidth, animHeight)).run {
            layout(width, height) {
                coordinates?.let {
                    val target = lookaheadScopeCoordinates.localLookaheadPositionOf(it).round()
                    val animOffset = offsetAnim.updateTarget(target, scope)
                    val currentOffset = lookaheadScopeCoordinates.localPositionOf(
                        it,
                        Offset.Zero
                    ).round()
                    val (x, y) = animOffset - currentOffset
                    place(x, y)
                } ?: place(0, 0)
            }
        }
    }
}

@Preview
@Composable
private fun LookAheadContentPreview() {
    LookAheadContent()
}