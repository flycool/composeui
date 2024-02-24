package com.compose.sample.composeui.overlaprow

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OverlappingRow(
    modifier: Modifier = Modifier,
    overlappingPercentage: Float,
    content: @Composable () -> Unit
) {
    val factor = (1 - overlappingPercentage)

    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            val widthExceptFirst = placeables.subList(1, placeables.size).sumOf { it.width }
            val firstWidth = placeables.getOrNull(0)?.width ?: 0
            val width = (firstWidth + widthExceptFirst * factor).toInt()
            val height = placeables.maxOf { it.height }

            layout(width, height) {
                var x = 0
                for (placeable in placeables) {
                    placeable.placeRelative(x, 0, 0f)
                    x += (placeable.width * factor).toInt()
                }
            }
        }
    )
}

@Preview
@Composable
fun CustomOverlappingRow(
    modifier: Modifier = Modifier
) {
    LazyRow {
        item { Spacer(modifier = modifier.width(12.dp)) }

        item {
            OverlappingRow(
                overlappingPercentage = 0.20f
            ) {
                for (i in images) {
                    Image(
                        imageVector = i, contentDescription = "image_$i",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color(0xFFFFA0A0), CircleShape)
                    )
                }
            }
        }

        item { Spacer(modifier = modifier.width(12.dp)) }
    }
}

val images = listOf(
    Icons.Filled.Build,
    Icons.Filled.Add,
    Icons.Filled.Home,
    Icons.Filled.AccountBox,
    Icons.Filled.AddCircle,
    Icons.Filled.AccountCircle,
    Icons.Filled.CheckCircle,
    Icons.Filled.Close,
    Icons.Filled.Call,
    Icons.Filled.DateRange,
)
