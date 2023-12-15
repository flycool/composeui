package com.compose.sample.composeui.canvas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MovingCircles() {
    val position = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        position.animateTo(
            targetValue = 550f,
            animationSpec = tween(durationMillis = 3000)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        drawCircle(color = Color.Red, center = Offset(position.value, 500f), radius = 50f)
        drawCircle(color = Color.Blue, center = Offset(position.value, 700f), radius = 80f)
        drawCircle(color = Color.Magenta, center = Offset(position.value, 1000f), radius = 150f)
        drawCircle(color = Color.Yellow, center = Offset(position.value, 1400f), radius = 190f)
    }
}