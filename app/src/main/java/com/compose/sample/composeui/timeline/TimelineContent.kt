package com.compose.sample.composeui.timeline

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R
import com.compose.sample.composeui.ui.theme.Gray200
import com.compose.sample.composeui.ui.theme.Green500
import com.compose.sample.composeui.ui.theme.Orange500
import kotlinx.collections.immutable.PersistentList

@Composable
fun TimelineContent(stages: PersistentList<HiringStage>) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(stages) { index, hiringStage ->
            TimelineNode(
                position = mapToTimelineNodePosition(index, stages.size),
                circleParameters = CircleParametersDefault.circleParameters(
                    backgroundColor = getIconColor(hiringStage),
                    stroke = getIconStrokeColor(hiringStage),
                    icon = getIcon(hiringStage)
                ),
                lineParameters = getLineBrush(
                    circleRadius = 12.dp,
                    index = index,
                    items = stages
                ),
                contentStartOffset = 16.dp,
                spacer = 24.dp
            ) { modifier ->
                Message(hiringStage, modifier)
            }
        }
    }
}

@Composable
private fun getLineBrush(circleRadius: Dp, index: Int, items: PersistentList<HiringStage>): LineParameters? {
    return if (index != items.lastIndex) {
        val currentStage: HiringStage = items[index]
        val nextStage: HiringStage = items[index + 1]
        val circleRadiusInPx = with(LocalDensity.current) { circleRadius.toPx() }
        LineParametersDefault.linearGradient(
            strokeWidth = 3.dp,
            startColor = (getIconStrokeColor(currentStage)?.color ?: getIconColor(currentStage)),
            endColor = (getIconStrokeColor(nextStage)?.color ?: getIconColor(items[index + 1])),
            startY = circleRadiusInPx * 2
        )
    } else {
        null
    }
}

private fun getIconColor(stage: HiringStage): Color {
    return when (stage.status) {
        HiringStageStatus.FINISHED -> Green500
        HiringStageStatus.CURRENT -> Orange500
        HiringStageStatus.UPCOMING -> Color.White
    }
}

private fun getIconStrokeColor(stage: HiringStage): StrokeParameters? {
    return if (stage.status == HiringStageStatus.UPCOMING) {
        StrokeParameters(color = Gray200, width = 2.dp)
    } else {
        null
    }
}

@Composable
private fun getIcon(stage: HiringStage): Int? {
    return if (stage.status == HiringStageStatus.CURRENT) {
        R.drawable.logo
    } else {
        null
    }
}

private fun mapToTimelineNodePosition(index: Int, collectionSize: Int) = when (index) {
    0 -> TimelineNodePosition.FIRST
    collectionSize - 1 -> TimelineNodePosition.LAST
    else -> TimelineNodePosition.MIDDLE
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun TimelineContentPreview() {
    TimelineContent(stages = DATA)
}