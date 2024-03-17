package com.compose.sample.composeui.draganddrop.indicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableIndicator(
    modifier: Modifier = Modifier,
    state: PagerState,
    itemCount: Int,
    onPageSelect: (Int) -> Unit
) {

    val haptics = LocalHapticFeedback.current
    val density = LocalDensity.current
    val threshold = remember {
        with(density) {
            ((80.dp / (itemCount.coerceAtLeast(1))) + 10.dp).toPx()
        }
    }
    val accumulatedDragAmount = remember { mutableFloatStateOf(0f) }
    var enableDrag by remember { mutableStateOf(false) }
    val lazyListSate = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val currentPage = state.currentPage

    LaunchedEffect(key1 = currentPage) {
        scope.launch {
            state.animateScrollToPage(currentPage)
        }
    }

    Box(
        modifier = modifier
            .background(
                color = if (enableDrag) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                else Color.Transparent,
                shape = RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.Center,
    ) {
        LazyRow(
            state = lazyListSate,
            modifier = Modifier
                .padding(8.dp)
                .widthIn(50.dp)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            accumulatedDragAmount.floatValue = 0f
                            enableDrag = true
                        },
                        onDrag = { change, dragAmount ->
                            if (enableDrag) {
                                change.consume()
                                accumulatedDragAmount.floatValue += dragAmount.x
                                if (abs(accumulatedDragAmount.floatValue) >= threshold) {
                                    val nextPage =
                                        if (accumulatedDragAmount.floatValue < 0) state.currentPage - 1 else state.currentPage + 1
                                    val correctedNextPage = nextPage.coerceIn(0, itemCount - 1)

                                    if (correctedNextPage != state.currentPage) {
                                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onPageSelect(correctedNextPage)
                                    }
                                    accumulatedDragAmount.floatValue = 0f
                                }
                            }
                        },
                        onDragEnd = {
                            enableDrag = false
                            accumulatedDragAmount.floatValue = 0f
                        }
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(itemCount) { i ->
                val scaleFactor = 1f - (0.1f * abs(i - currentPage)).coerceAtMost(0.4f)
                val color = if (i == currentPage) Color(0xFF03A9F4) else Color.DarkGray
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .graphicsLayer {
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        .drawBehind {
                            drawCircle(color = color)
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun DraggableIndicatorPreview() {
    DraggableIndicator(
        state = rememberPagerState(
            0, 0f
        ) { colors.size },
        itemCount = colors.size,
        onPageSelect = {}
    )
}